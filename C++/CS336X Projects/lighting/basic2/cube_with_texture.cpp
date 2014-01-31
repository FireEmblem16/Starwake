
//
// Demonstration of texture sampling parameters for minification.
// Use F1 through F6 for different options. Edit init() to load
// brick texture instead of checkerboard.  Camera controls
// are "standard".
//


#include "Angel.h"
#include "square.h"
#include "images.h"
#include "Camera.h"
#include "Shader.h"
#include "VertexArray.h"

Camera* camera;
Shader* vertexColorShader;
Shader* texShader;
VertexArray* modelVao;
VertexArray* axesVao;

GLfloat maxAnisotropy;

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

  //get the checkerboard texture, or load an image file
  imageBytes = checkerboard(&nWidth, &nHeight, &nComponents, &eFormat);
  //imageBytes = loadFile("../images/brick.png", &nWidth, &nHeight, &nComponents, &eFormat);

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

  // ** Generate mipmaps
  glGenerateMipmap( GL_TEXTURE_2D );

  // Set some basic texture sampling parameters
  // These values are stored with the texture register, not the texture
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT );
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT );
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
  glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );

  vertexColorShader  = new Shader("vshader.glsl", "fshader.glsl");
  texShader      = new Shader("vshader_tex.glsl", "fshader_tex.glsl");

  // This generates a long thin rectangle
  Square m(30);
  modelVao = new VertexArray();
  modelVao->AddAttribute("vPosition", m.getVertices(), m.getNumVertices());
  modelVao->AddAttribute("vTexCoord", m.getTexCoords(), m.getNumVertices());

  axesVao = new VertexArray();
  axesVao->AddAttribute("vPosition", axes, 6);
  axesVao->AddAttribute("vColor", colorsForAxes, 6);

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.2, 0.2, 1.0 ); 

  camera->OrbitLeft(length(camera->GetPosition()), 15);

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

  // the model has its z-direction up, so rotate it around

  mat4 model = RotateY(90) * RotateZ(90);
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
  case 'a':
    camera->MoveLeft(0.5);
    break;
  case 's':   
    camera->MoveBackward(0.5);
    break;
  case 'd':
    camera->MoveRight(0.5);
    break;
  case 'r':
    camera->MoveUp(0.5);
    break;
  case 'f':
    camera->MoveDown(0.5);
    break;
  case 'i':
    camera->LookUp(5);
    break;
  case 'j':
    camera->LookLeft(5);
    break;
  case 'k':
    camera->LookDown(5);
    break;
  case 'l':
    camera->LookRight(5);
    break;
  case 'I':
    camera->PitchUp(5);
    break;
  case 'J':
    camera->HeadLeft(5);
    break;
  case 'K':
    camera->PitchDown(5);
    break;
  case 'L':
    camera->HeadRight(5);
    break;
  case '<':
    camera->RollCCW(5);
    break;
  case '>':
    camera->RollCW(5);
    break;
  case 'o':
    camera->LookAt(vec3(0, 0, 0));
    break;
  case 'u':
    camera->SetFieldOfView(camera->GetFieldOfView() + 5);
    break;
  case 'U':
    camera->SetFieldOfView(camera->GetFieldOfView() - 5);
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
    std::cout << "Min filter: nearest" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 1 ); // set to default value
    break;
  case GLUT_KEY_F2:
    std::cout << "Min filter: linear" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 1 ); // set to default value
    break;
  case GLUT_KEY_F3:
    std::cout << "Min filter: nearest w/ nearest mipmap" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 1 ); // set to default value
    break;
  case GLUT_KEY_F4:
    std::cout << "Min filter: nearest w/ linear interpolation between mipmaps" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 1 ); // set to default value
    break;
  case GLUT_KEY_F5:
    std::cout << "Min filter: linear w/ linear interpolation between mipmaps" << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR  );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 1 ); // set to default value
    break;
  case GLUT_KEY_F6:
    std::cout << "Min filter: linear w/ max anisotropy " << maxAnisotropy << std::endl;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR );
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy ); // set to max value
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

  // Check the max anisotropy
  int ret = glutExtensionSupported("GL_EXT_texture_filter_anisotropic");
  if (ret) 
  {
    glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, &maxAnisotropy);
    std::cout << "max anisotropy: " << maxAnisotropy << std::endl;
  }
  else
  {
    std::cout << "GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT not supported" << std::endl;
  }

  init();

  // Determine the number of texture units
  int max;
  glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, &max);
  std::cout << "Texture units: " << max << std::endl;

  glutDisplayFunc(display);
  glutKeyboardFunc(keyboard);
  glutSpecialFunc(keyboardSpecial);
 
  glutMainLoop();
  return 0;
}
