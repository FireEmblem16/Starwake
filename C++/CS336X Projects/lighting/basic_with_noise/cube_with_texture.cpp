
//
// Simple demonstration of using a 2D texture. Same
// as the code in the 'basic' project except this
// generates a 2D noise texture.
//


#include "Angel.h"
#include "cube.h"
#include "sphere.h"
#include "square.h"
#include "teapot.h"

#include "images.h"
#include "Camera.h"
#include "Shader.h"
#include "VertexArray.h"

// Declaration for functions in Noise.cpp

// This one can be used just like the checkerboard-generating function
GLubyte* noiseTexture2D(int startFrequency, 
                        int numOctaves, 
                        int * nWidth, 
                        int * nHeight, 
                        int * nComponents, 
                        GLenum * eFormat);



Camera* camera;
Shader* vertexColorShader;
Shader* texShader;
VertexArray* modelVao;
VertexArray* axesVao;

// current model transformation
mat4 model;

//
// Create a unit square in x-y plane whose
// texture coordinates we can fool around with.
// Use second line for a trapezoidal example.
//
vec3 points[4] = {
  vec3(-0.5, -0.5, 0), vec3(0.5, -0.5, 0), vec3(0.5, 0.5, 0), vec3(-0.5, 0.5, 0)};
//  vec3(-0.5, -0.5, 0), vec3(0.5, -0.5, 0), vec3(0.5, 0.5, 0), vec3(0, 0.5, 0)};  

//
// Define tex coords for above square
//
vec2 texCoords[4] = {
  vec2(0, 0), vec2(1, 0), vec2(1, 1), vec2(0, 1)};
  
  // alternatively, start in middle and take advantage of wrapping
  //vec2(0.5, 0.5), vec2(1.5, 0.5), vec2(1.5, 1.5), vec2(0.5, 1.5)};

  // or, have the pattern repeat twice
  //vec2(0, 0), vec2(2, 0), vec2(2, 2), vec2(0, 2)};

  // or something completely weird...
  //vec2(0, 0), vec2(1, 0), vec2(1, 1), vec2(0.5, 1)};
  //vec2(0, 0), vec2(1, 0), vec2(0, 1), vec2(0, 1)};
  //vec2(0, 0), vec2(1, 0), vec2(0, 1), vec2(1, 1)};

// indices for drawing square
int squareIndices[6] = {0, 1, 2, 0, 2, 3};

// nonmoving set of axes
vec3 axes[6] = {
  vec3(0.0, 0.0, 0.0), vec3(0.9, 0.0, 0.0),
  vec3(0.0, 0.0, 0.0), vec3(0.0, 0.9, 0.0), 
  vec3(0.0, 0.0, 0.0), vec3(0.0, 0.0, 0.9)};

// Axes colored R, G, B
vec4 colorsForAxes[6] = {
  vec4(1.0, 0.0, 0.0, 1.0), vec4(1.0, 0.0, 0.0, 1.0), 
  vec4(0.0, 1.0, 0.0, 1.0), vec4(0.0, 1.0, 0.0, 1.0), 
  vec4(0.0, 0.0, 1.0, 1.0), vec4(0.0, 0.0, 1.0, 1.0) };

void init()
{
  camera = new Camera(vec3(0.0, 0.0, 4.0),   // position
              vec3(0.0, 0.0, -1.0),  // forward
              vec3(0.0, 1.0, 0.0),   // up
              1.0f,                  // aspect
              30.0f,                 // fovy
              0.1f,                  // near
              1000.0f);              // far


  // Image and image parameters
  GLubyte *imageBytes;
  int nWidth, nHeight, nComponents;
  GLenum eFormat;

  // Get the checkerboard texture, or load an image file
  // ('checkerboard2' has a red border)

  // Generate a greyscale noise texture, frequency 4, one octave
  int frequency = 4;
  int octaves = 1;
  // Scale is exaggerated so values occupy full color range 0 to 255
  imageBytes = noiseTexture2D(frequency, octaves, &nWidth, &nHeight, &nComponents, &eFormat);

  //imageBytes = checkerboard(&nWidth, &nHeight, &nComponents, &eFormat);
  //imageBytes = checkerboard2(&nWidth, &nHeight, &nComponents, &eFormat);
  //imageBytes = loadFile("../images/clover.jpg", &nWidth, &nHeight, &nComponents, &eFormat);
  //imageBytes = loadFile("../images/clover_really_small.jpg", &nWidth, &nHeight, &nComponents, &eFormat);
  //imageBytes = loadFile("../images/stone.tga", &nWidth, &nHeight, &nComponents, &eFormat);

  // Generate a handle for the texture object
  GLuint texture;
  glGenTextures(1, &texture);

  // A texture is always bound to a texture register ("texture unit")
  // 0 is the default, but we will make it explicit for clarity
  glActiveTexture( GL_TEXTURE0 );

  // Bind the texture so we can load the image data
  glBindTexture( GL_TEXTURE_2D, texture );
  glTexImage2D( GL_TEXTURE_2D, 0, nComponents, nWidth, nHeight, 0, eFormat, GL_UNSIGNED_BYTE, imageBytes );
  free(imageBytes);

  // Set some basic texture sampling parameters
  // These values are stored with the texture register, not the texture
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT );
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT );
  //glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
  //glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );

  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
  //glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
  //glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );


  vertexColorShader  = new Shader("vshader.glsl", "fshader.glsl");
  texShader      = new Shader("vshader_tex.glsl", "fshader_tex.glsl");

  // VAO for model
  modelVao = new VertexArray();

  // Use the simple square defined above  
  modelVao->AddAttribute("vPosition", points, 4);
  modelVao->AddAttribute("vTexCoord", texCoords, 4);
  modelVao->AddIndices((const unsigned int*) squareIndices, 6);

  // Alternatively, comment out the three lines above and try a sphere or teapot 
  //Sphere m;
  //Teapot m;
  //modelVao->AddAttribute("vPosition", m.getVertices(), m.getNumVertices());
  //modelVao->AddAttribute("vTexCoord", m.getTexCoords(), m.getNumVertices());
  

 
  // VAO for axes
  axesVao = new VertexArray();
  axesVao->AddAttribute("vPosition", axes, 6);
  axesVao->AddAttribute("vColor", colorsForAxes, 6);

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.2, 0.2, 1.0 ); 

  camera ->SetPosition(vec3(0.0, 0.0, 4.0));

  // Tell the fragment shader which texture register to use
  texShader->Bind();
  texShader->SetUniform("texture", 0);  // 0 means GL_TEXTURE0
}

//----------------------------------------------------------------------------

void display( void )
{
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );


  mat4 view = camera->GetView();
  mat4 projection = camera->GetProjection();

  texShader->Bind();
  texShader->SetUniform("transform", projection * view * model);

  // Bind model VAO and draw.  The Draw() function will automatically 
  // use indices if they were added to the VAO
  modelVao->Bind(*texShader);
  modelVao->Draw(GL_TRIANGLES);

  // Bind shader and set uniforms
  vertexColorShader->Bind();
  vertexColorShader->SetUniform("transform", projection * view);

  // Bind VAO for axes and draw
  axesVao->Bind(*vertexColorShader);
  glLineWidth(2);
  axesVao->Draw(GL_LINES);

  vertexColorShader->Unbind();
  axesVao->Unbind();

  // instead of glFlush, display the back buffer now
  glutSwapBuffers();

}

void keyboard( unsigned char key, int x, int y )
{
  switch( key ) {
  case 033: // Escape Key
  case 'q': case 'Q':
    exit( EXIT_SUCCESS );
    break; 
  case 'w':   // forward
    camera->MoveForward(0.5);
    break;
  case 's':
    camera->MoveBackward(0.5);
    break;
  }
  glutPostRedisplay();
}



// Needed to get key events for arrow keys
void keyboardSpecial(int key, int x, int y)
{
  switch( key ) {
  case GLUT_KEY_UP:
    camera->OrbitUp(length(camera->GetPosition()), 5);
    break;
  case GLUT_KEY_DOWN:
    camera->OrbitDown(length(camera->GetPosition()), 5);
    break;
  case GLUT_KEY_RIGHT:
    camera->OrbitRight(length(camera->GetPosition()), 5);
    break;
  case GLUT_KEY_LEFT:
    camera->OrbitLeft(length(camera->GetPosition()), 5);
    break;
  case GLUT_KEY_F1:
    std::cout << "nearest" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
    break;
  case GLUT_KEY_F2:
    std::cout << "linear" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    break;
  }
  glutPostRedisplay();
}


int main( int argc, char **argv )
{
  glutInit( &argc, argv );

  // Set up the graphics context with double-buffering
  glutInitDisplayMode( GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH );

  glutInitWindowSize( 512, 512 );
  glutCreateWindow( " " );

  glewInit();

  init();

  int max;
  glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, &max);
  std::cout << max << std::endl;

  glutDisplayFunc(display);
  glutKeyboardFunc(keyboard);
  glutSpecialFunc(keyboardSpecial);

  glutMainLoop();
  return 0;
}
