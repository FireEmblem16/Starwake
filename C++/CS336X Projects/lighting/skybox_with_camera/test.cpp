//
// Simple demo of rendering a cube map onto the 6 inside faces of a cube
// to create a "skybox".  Adapted from the OpenGL Superbible 5e (which
// is also where the images came from).
//
// Camera controls are "standard".
//

#include "Angel.h"
#include "cube.h"
#include "sphere.h"
#include "teapot.h"
#include "Camera.h"
#include "TextureCube.h"
#include <Shader.h>
#include <VertexArray.h>

TextureCube * skyboxTexture;
VertexArray * vao;
Shader * shader;
Camera * camera = new Camera();  

mat4 model;

int numVertices;

void init()
{
  // Load the skybox textures
  skyboxTexture = new TextureCube(
    "../images/pos_x.tga",
    "../images/neg_x.tga",
    "../images/pos_y.tga",
    "../images/neg_y.tga",
    "../images/pos_z.tga",
    "../images/neg_z.tga");

  // Normally we want to map onto a cube, but you could also
  // project the images onto any model
  //Teapot cube;
  //Sphere cube;
  Cube cube;
  vao = new VertexArray();
  vao->AddAttribute("vPosition", cube.getVertices(), cube.getNumVertices());

  // Scale it up so we can move the camera around inside it
  model = Scale(20.0);

  // Make a shader
  shader= new Shader("vshader_cube_tex.glsl", "fshader_cube_tex.glsl");

  glEnable( GL_DEPTH_TEST );
  glClearColor( 1.0, 1.0, 1.0, 1.0 ); 
}

//----------------------------------------------------------------------------

void display( void )
{
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

  skyboxTexture->Bind(1);
  shader->Bind();
  shader->SetUniform("model", model);
  shader->SetUniform("view", camera->GetView());
  shader->SetUniform("projection", camera->GetProjection());
  shader->SetUniform("textureCube", skyboxTexture->GetTextureUnit());  
  vao->Bind(*shader);
  vao->Draw(GL_TRIANGLES);
  vao->Unbind();
  shader->Unbind();
  
  glFlush();
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
  }
  glutPostRedisplay();
}

int main( int argc, char **argv )
{
  glutInit( &argc, argv );
  glutInitDisplayMode( GLUT_RGBA | GLUT_DEPTH );
  glutInitWindowSize( 512, 512 );
  glutCreateWindow( " " );

  glewInit();

  init();

  glutDisplayFunc(display);
  glutKeyboardFunc(keyboard);
  glutSpecialFunc(keyboardSpecial);

  glutMainLoop();
  return 0;
}
