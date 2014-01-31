
//
// Examples of texture mapping with lighting. See the comments in 
// shader fshader_phong.glsl
// for some techniques to try with different image files.  Edit the init()
// function to change image file or shaders.
// 
// Camera and animation controls are similar to other examples.
//
// Try "fshader_phong_tex_stripes.glsl" for example of procedural texture
// Try "fshader_toon.glsl" for another example (which doesn't actually
// use texture mapping at all, but it's fun)
//
#include "Angel.h"
#include "Camera.h"
#include "Shader.h"
#include "VertexArray.h"
#include "ObjFile.h"
#include "Texture2D.h"

char* objFileName = "../models/Zack.obj";

Camera * camera;
Shader* vertexColorShader;
Shader* phongShader;
Shader* texShader;

VertexArray* modelVao;
VertexArray* axesVao;

Texture2D* texture;

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

// Shiny green plastic?
//mat3 material = mat3(
//  vec3(0.0, 0.3, 0.3),  // blue-green in ambient light
//  vec3(0.0, 0.8, 0.0),  // green surface
//  vec3(0.8, 0.8, 0.8)); // specular highlights reflect light color
//GLfloat shininess = 30.0;

// Brass-like material
mat3 material = mat3(
  vec3(0.33, 0.22, 0.03),  // 
  vec3(0.78, 0.57, 0.11),  // 
  vec3(0.99, 0.91, 0.81)); // 
GLfloat shininess = 28.0;

//mat3 material = mat3(
//  vec3(0.33, 0.22, 0.50),  // 
//  vec3(0.18, 0.07, 0.51),  // 
//  vec3(0.09, 0.91, 0.81)); // 
//GLfloat shininess = 28.0;

// White light
mat3 light = mat3(
  vec3(0.4, 0.4, 0.4),
  vec3(1.0, 1.0, 1.0),
  vec3(1.0, 1.0, 1.0));


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


  // Create a texture from image file
  texture = new Texture2D("../images/sword.png");
  //texture = new Texture2D("../images/stone.tga");
  //texture = new Texture2D("../images/steve.png");
  //texture = new Texture2D("../images/tarnish.tga");

  // Pick your shaders
  phongShader      = new Shader("vshader_phong.glsl", "fshader_phong.glsl");
  //phongShader      = new Shader("vshader_phong.glsl", "fshader_phong_tex_stripes.glsl");
  //phongShader      = new Shader("vshader_phong.glsl", "fshader_toon.glsl");

  // Shader for axes
  vertexColorShader  = new Shader("vshader.glsl", "fshader.glsl");


  // Load model from obj file
  ObjFile m(objFileName);

  modelVao = new VertexArray();
  modelVao->AddAttribute("vPosition", m.GetVertices(), m.GetNumVertices());
  modelVao->AddAttribute("vTexCoord", m.GetTexCoords(), m.GetNumVertices());
  modelVao->AddAttribute("vNormal", m.GetNormals(), m.GetNumVertices());
  modelVao->AddIndices((const unsigned int*) m.GetIndices(), m.GetNumIndices());

  model = Scale(m.GetScaleFactor());


  axesVao = new VertexArray();
  axesVao->AddAttribute("vPosition", axes, 6);
  axesVao->AddAttribute("vColor", colorsForAxes, 6);

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.0, 0.0, 1.0 ); 

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

  // Create normal matrix
  mat4 mv = view * model;
  mat3 normalMatrix = mat3(vec3(mv[0][0], mv[0][1], mv[0][2]),
                           vec3(mv[1][0], mv[1][1], mv[1][2]),
                           vec3(mv[2][0], mv[2][1], mv[2][2]));

  // Bind texture to a texture unit
  texture->Bind(1);

  // Bind shader and set uniforms
  phongShader->Bind();
  phongShader->SetUniform("normalMatrix", normalMatrix);
  phongShader->SetUniform("lightPosition", vec4(0.0, 2.0, 0.0, 1.0));
  phongShader->SetUniform("model", model);
  phongShader->SetUniform("view", view);
  phongShader->SetUniform("projection", projection);
  phongShader->SetUniform("materialProperties", material);
  phongShader->SetUniform("lightProperties", light);
  phongShader->SetUniform("shininess", shininess);
  phongShader->SetUniform("texture", texture->GetTextureUnit());

  // Bind model VAO and draw.  The Draw() function will automatically 
  // use indices if they were added to the VAO
  modelVao->Bind(*phongShader);
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
  // Allow for models to be specified on the command line
  if (argc >= 2)
  {
      objFileName = argv[1];
  }
  glutInit( &argc, argv );

  // Set up the graphics context with double-buffering
  glutInitDisplayMode( GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH );

  glutInitWindowSize( 512, 512 );
  glutCreateWindow( " " );

  glewInit();

  init();

  glutDisplayFunc(display);
  glutKeyboardFunc(keyboard);
  glutSpecialFunc(keyboardSpecial);
  glutIdleFunc( idle );

  elapsedTime = glutGet(GLUT_ELAPSED_TIME);

  glutMainLoop();
  return 0;
}
