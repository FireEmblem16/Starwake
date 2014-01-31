
//
// Spinning cube example with phong lighting.  Surface color is passed to
// fragment shader as a uniform matrix, edit materialProperties to change.
//
// Light is positional and can be moved up or down with the 'p' and 'P' keys.
// 
// Model comes from an obj file, edit objFileName to change.  See
// the models directory for some things to try.
//
// Camera controls are "standard". See the keyboard and keyboardSpecial function.
// 
// You can control the spinning with:
// 
//    x: spin about x-axis
//    y: spin about y-axis
//    z: spin about z-axis
//    space: pause spinning
//
// You can adjust the specular exponent (shininess) with:
// 
//    e: increase exponent
//    E: decrease exponent
//
// and you can toggle using the half-vector calculation with the 'h' key.
//

#include "Angel.h"
#include <Shader.h>
#include <VertexArray.h>
#include <ObjFile.h>
#include <Camera.h>


// Default obj file
const char* objFileName = "../models/teapot.obj";

Shader * program;
Shader * programForAxes;
VertexArray * modelVao;
VertexArray * axesVao;
Camera * camera;

enum { Xaxis = 0, Yaxis = 1, Zaxis = 2, NumAxes = 3 };

// current axis of rotation
int  Axis = Xaxis;

// current model transformation
mat4 model;

// degree change in each frame
GLfloat increment = 0.5;

// elapsed time
int elapsedTime;

// pause rotation
int paused = 1;

// frame rate in millis for 30 frames/sec
const int frameRate = 1000.0 / 30;

// light position
vec4 lightPosition = vec4(0.0, 1.0, 0.0, 1.0);

// use half-vector calculation
int useHalfVector = 0;

// material properties

// Dull clay-like material
//mat3 material = mat3(
//  vec3(0.75, 0.38, 0.26),  // 
//  vec3(0.75, 0.38, 0.26),  // terracotta color
//  vec3(0.25, 0.20, 0.15) ); // pale specular highlight similar to diffuse color
//GLfloat shininess = 10.0;

// Shiny green plastic (?)
mat3 material = mat3(
  vec3(0.0, 0.3, 0.3),  // blue-green in ambient light
  vec3(0.0, 0.8, 0.0),  // green surface
  vec3(0.8, 0.8, 0.8)); // specular highlights reflect light color
GLfloat shininess = 30.0;

// Brass-like material
//mat3 material = mat3(
//  vec3(0.33, 0.22, 0.03),  // 
//  vec3(0.78, 0.57, 0.11),  // 
//  vec3(0.99, 0.91, 0.81)); // 
//GLfloat shininess = 28.0;

// White light
mat3 light = mat3(
  vec3(0.35, 0.35, 0.35),
  vec3(1.0, 1.0, 1.0),
  vec3(1.0, 1.0, 1.0));

void init()
{
  // Set camera with initial position out the z axis
  camera = new Camera(vec3(0, 0, 5));

  ObjFile m(objFileName);
  model = Scale(m.GetScaleFactor());

  // nonmoving set of axes
  vec3 axes[6] = {
    vec3(0.0, 0.0, 0.0), vec3(0.9, 0.0, 0.0),
    vec3(0.0, 0.0, 0.0), vec3(0.0, 0.9, 0.0), 
    vec3(0.0, 0.0, 0.0), vec3(0.0, 0.0, 0.9)};

  // Axes colored r, g, b
  vec4 colorsForAxes[6] = {
    vec4(1.0, 0.0, 0.0, 1.0), vec4(1.0, 0.0, 0.0, 1.0), 
    vec4(0.0, 1.0, 0.0, 1.0), vec4(0.0, 1.0, 0.0, 1.0), 
    vec4(0.0, 0.0, 1.0, 1.0), vec4(0.0, 0.0, 1.0, 1.0) };

  modelVao = new VertexArray();
  modelVao->AddAttribute("vPosition", m.GetVertices(), m.GetNumVertices());
  modelVao->AddAttribute("vNormal", m.GetNormals(), m.GetNumVertices());
  modelVao->AddIndices(m.GetIndices(), m.GetNumIndices());

  axesVao = new VertexArray();
  axesVao->AddAttribute("vPosition", axes, 6);
  axesVao->AddAttribute("vColor", colorsForAxes, 6);

  program = new Shader("vshader_phong.glsl", "fshader_phong.glsl");
  programForAxes = new Shader("vshader.glsl", "fshader.glsl");

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.0, 0.3, 1.0 ); 

}

//----------------------------------------------------------------------------

void display( void )
{
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

  // update model matrix
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

  // We need to transform the normal vectors into eye space along with the cube.  
  // Since we aren't doing any shearing or nonuniform scaling in this case,
  // we can just use the same model-view as for the cube itself 
  // (upper-left 3x3 submatrix of the model-view matrix).  This assumes
  // that the lighting calculation will be done in eye space.
  mat4 mv = view * model;
  mat3 normalMatrix = mat3(vec3(mv[0][0], mv[0][1], mv[0][2]),
                           vec3(mv[1][0], mv[1][1], mv[1][2]),
                           vec3(mv[2][0], mv[2][1], mv[2][2]));

  // clear the window
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );     

  // bind shader program
  program->Bind();

  // draw triangle with transformation
  modelVao->Bind(*program);
  program->SetUniform("model", model);
  program->SetUniform("view", view);
  program->SetUniform("projection", projection);
  program->SetUniform("normalMatrix", normalMatrix);
  program->SetUniform("lightPosition", lightPosition);
  program->SetUniform("shininess", shininess);
  program->SetUniform("materialProperties", material);
  program->SetUniform("lightProperties", light);
  program->SetUniform("useHalfVector", useHalfVector != 0);

  modelVao->Draw(GL_TRIANGLES);
  modelVao->Unbind();
  program->Unbind();

  // use identity transformation for axes
  programForAxes->Bind();
  axesVao->Bind(*programForAxes);
  programForAxes->SetUniform("transform", projection * view);
  glLineWidth(3);
  axesVao->Draw(GL_LINES);
  axesVao->Unbind();
  programForAxes->Unbind();

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

  case 'x':
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
  case 'p':
    lightPosition += vec4(0.0, 0.2, 0.0, 0.0);
    break;
  case 'P':
    lightPosition -= vec4(0.0, 0.2, 0.0, 0.0);
    break;
  case 'e':
    shininess += 1; 
    std::cout << "shininess " << shininess << std::endl;
    break;
  case 'E':
    shininess -= 1;
    std::cout << "shininess " << shininess << std::endl;
    break;
  case 'h':
    useHalfVector = 1 - useHalfVector;
    std::cout << ( useHalfVector ? "using half vector" : "not using half vector" ) << std::endl;
    break;

  case ' ':
    paused = 1 - paused;
    break;

  }
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
