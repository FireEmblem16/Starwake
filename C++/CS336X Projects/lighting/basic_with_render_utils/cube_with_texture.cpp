
//
// This example demonstrates the use of the Texture2D class from
// RenderUtils to define two textures.  The fragment shader then
// uses alpha blending to paint one image over the other.
// 
// In addition, instead of the header files such as sphere.h
// and teapot.h, we use the ObjFile class to load models
// defined in .obj files.  Note that most of the models in
// the models directory don't have texture coordinates defined
// (but you could try sphere.tex).
//
// Camera controls are "standard".  Use x, y, and z to control
// axis of rotation and space to pause.
//


#include "Angel.h"
#include "images.h"
#include "Camera.h"
#include "Shader.h"
#include "VertexArray.h"
#include "Texture2D.h"
#include "ObjFile.h"

Camera* camera;
Shader* vertexColorShader;
Shader* texShader;

VertexArray* modelVao;
VertexArray* axesVao;

Texture2D* checkerboardTexture;
Texture2D* nameTexture;

// Default obj file
const char* objFileName = "../models/teapot.obj";
//const char* objFileName = "../models/sphere_tex.obj";

enum { Xaxis = 0, Yaxis = 1, Zaxis = 2, NumAxes = 3 };

// current axis of rotation
int  Axis = Xaxis;

// current model transformation
mat4 model;

// degree change in each frame
GLfloat increment = 0.5;

// elapsed time
int elapsedTime;

// frame rate in millis for 30 frames/sec
const int frameRate = 1000.0 / 30;

bool paused = 1;

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
  camera = new Camera (vec3(0.0, 0.0, 4.0),   // position
              vec3(0.0, 0.0, -1.0),  // forward
              vec3(0.0, 1.0, 0.0),   // up
              1.0f,                  // aspect
              30.0f,                 // fovy
              0.1f,                  // near
              1000.0f);              // far


  // Image and image parameters

  // get the checkerboard texture as a byte stream (from images.h)
  // using default sampling parameters
  int nWidth, nHeight, nComponents;
  GLenum eFormat;
  GLubyte *imageBytes = checkerboard(&nWidth, &nHeight, &nComponents, &eFormat);
  checkerboardTexture = new Texture2D(
        imageBytes,
        GL_RGB,
        nWidth,
        nHeight);
  delete[] imageBytes;

  // load texture from an image file
  // using default sampling parameters
  nameTexture = new Texture2D("../images/steve.png");

  // alternatively, set sampling parameters
    //texture = new Texture2D(
    //    "../images/clover.jpg",
    //    GL_LINEAR_MIPMAP_LINEAR,
    //    GL_LINEAR,
    //    GL_REPEAT,
    //    GL_REPEAT,
    //    1.0f);

  vertexColorShader  = new Shader("vshader.glsl", "fshader.glsl");
  texShader      = new Shader("vshader_tex.glsl", "fshader_tex.glsl");

  // Load the model and create VAO
  ObjFile m(objFileName);
  modelVao = new VertexArray();
  modelVao->AddAttribute("vPosition", m.GetVertices(), m.GetNumVertices());
  modelVao->AddAttribute("vTexCoord", m.GetTexCoords(), m.GetNumVertices());
  modelVao->AddIndices(m.GetIndices(), m.GetNumIndices());
  model = Scale(vec3(m.GetScaleFactor()));

  // VAO for axes
  axesVao = new VertexArray();
  axesVao->AddAttribute("vPosition", axes, 6);
  axesVao->AddAttribute("vColor", colorsForAxes, 6);

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.2, 0.2, 1.0 ); 

  camera->SetPosition(vec3(0.0, 0.0, 4.0));
}

//----------------------------------------------------------------------------

void display( void )
{
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

  // Update model matrix
  if (!paused)
  {
  mat4 newTransform;
  switch(Axis)
  {
  case Xaxis:
    newTransform = RotateX(increment);
    break;
  case Yaxis:
    newTransform = RotateY(increment);
    break;
  case Zaxis:
    newTransform = RotateZ(increment);
    break;
  }
  model = newTransform * model; 
  }

  mat4 view = camera->GetView();
  mat4 projection = camera->GetProjection();

  // Bind each texture to a texture register
  checkerboardTexture->Bind(3);
  nameTexture->Bind(4);
  texShader->Bind();
  texShader->SetUniform("transform", projection * view * model);
  texShader->SetUniform("baseTexture", checkerboardTexture->GetTextureUnit());
  texShader->SetUniform("overlayTexture", nameTexture->GetTextureUnit());

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

  case 'x':  // axis of rotation
    Axis = Xaxis;
    break;
  case 'y':
    Axis = Yaxis;
    break;
  case 'z':
    Axis = Zaxis;
    break;
  case '+':
    increment += 0.1;
    break;
  case '-':
    increment -= 0.1;
    break;
  case ' ':   // pause
    paused = 1 - paused;
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
  //glutPostRedisplay();
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
  }
}


void idle( void )
{
  int now = glutGet(GLUT_ELAPSED_TIME);
  if (now - elapsedTime > frameRate)
  {
    elapsedTime = now;
    glutPostRedisplay();
  }
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
  glutIdleFunc( idle );

  elapsedTime = glutGet(GLUT_ELAPSED_TIME);

  glutMainLoop();
  return 0;
}
