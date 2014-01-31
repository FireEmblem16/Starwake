//
// Noise demo. All this does is draw a square and apply a noise texture with 
// exaggerated contrast.  
//
// Use fshader.glsl to view noise as a greyscale value.
// Use fshader_turbulence.glsl for a turbulence example (absolute value of noise).
// 
// Number keys 0-3 display 4 frequencies by themselves, keys 4-7 display 1, 2
// 3, or four summed octaves.  Use w and s to zoom in and out.  
//
// The examples use a 3D noise with the four frequencies stored in the red, green,
// blue, and alpha channels.  By default the third coordinate ("p") is fixed at zero. 
// Press the 'p' key to animate the texture by increasing the p-coordinate over time.
// You can also use the + and - keys to speed up or slow down the animation.
//
// The noise function file Noise.cpp came from the "Orange Book".
//

#include "Angel.h"
#include "square.h"
#include "teapot.h"
// declaration from Noise.cpp
void CreateNoise3D();

int numVertices; 

int state = 0;
float pCoord = 0.0;
float pIncrement = 0.001;
bool pPaused = true;


// elapsed time
int elapsedTime;

// frame rate in millis for 30 frames/sec
const int frameRate = 1000.0 / 30;


// Viewing direction
GLfloat theta = 0.0;
GLfloat phiPrime = 0.0;

// distance from origin
GLfloat r = 10.0;

// Default field of view for Perspective() matrix
GLfloat fovy = 30.0;

// Rotation 
mat4 model = RotateY(90.0);

// Viewing transformation
mat4 view;

// Projection matrix
mat4 projection;

GLuint vao;
GLuint program;



void init()
{
  GLuint texture;
  glGenTextures(1, &texture);
  glBindTexture(GL_TEXTURE_3D, texture);

  // This creates the 3D noise texture, binds it, and sets texture parameters
  CreateNoise3D();

  Square square;
  vec3 * points = square.getVertices();
  vec3 * normals = square.getNormals();
  vec2 * texCoords = square.getTexCoords();
  numVertices = square.getNumVertices();

  int sizeOfPoints = numVertices * sizeof(vec3);
  int sizeOfNormals = numVertices * sizeof(vec3);
  int sizeOfTexCoords = numVertices * sizeof(vec2);

  // Create and initialize buffer object
  GLuint buffer;
  glGenBuffers(1, &buffer);
  glBindBuffer(GL_ARRAY_BUFFER, buffer);
  glBufferData(GL_ARRAY_BUFFER, sizeOfPoints + sizeOfNormals + sizeOfTexCoords, NULL, GL_STATIC_DRAW);
  glBufferSubData(GL_ARRAY_BUFFER, 0, sizeOfPoints, points);
  glBufferSubData(GL_ARRAY_BUFFER, sizeOfPoints, sizeOfNormals, normals);
  glBufferSubData(GL_ARRAY_BUFFER, sizeOfPoints + sizeOfNormals, sizeOfTexCoords, texCoords);

  // Load shaders and use the resulting shader program
  //program = InitShader( "vshader.glsl", "fshader.glsl" );
  program = InitShader( "vshader.glsl", "fshader_turbulence.glsl" );
  glUseProgram( program );

  // set up vertex arrays
  glGenVertexArrays(1, &vao);
  glBindVertexArray(vao);
  glBindBuffer(GL_ARRAY_BUFFER, buffer);

  // vPosition is an "in" variable in the vertex shader
  GLuint vPosition = glGetAttribLocation( program, "vPosition" );
  glEnableVertexAttribArray( vPosition );
  glVertexAttribPointer( vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0) );

  GLuint vNormal = glGetAttribLocation( program, "vNormal" ); 
  glEnableVertexAttribArray( vNormal );
  glVertexAttribPointer(vNormal, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(sizeOfPoints));

  GLuint vTexCoord = glGetAttribLocation( program, "vTexCoord" ); 
  glEnableVertexAttribArray( vTexCoord );
  glVertexAttribPointer(vTexCoord, 2, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(sizeOfPoints + sizeOfNormals));

  glActiveTexture( GL_TEXTURE0 );
  glBindTexture( GL_TEXTURE_3D, texture );
  glUniform1i( glGetAttribLocation( program, "noiseTexture" ), 0 );

  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.0, 0.0, 1.0 ); 
}

//----------------------------------------------------------------------------

void display( void )
{
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
  glUseProgram(program);

  // Convert from spherical to rectangular coordinates to find the view point p
  GLfloat phi = 90.0 - phiPrime;
  GLfloat x = sin(DegreesToRadians * phi) * cos(DegreesToRadians * theta);
  GLfloat y = sin(DegreesToRadians * phi) * sin(DegreesToRadians * theta);
  GLfloat z = cos(DegreesToRadians * phi);
  vec3 p = vec3(x, y, z) * r;

  // The viewing transformation is the *inverse* of the following
  //   rotate 90 about z axis
  //   rotate 90 about y axis
  //   rotate -phiPrime about y axis
  //   rotate theta about z axis
  //   translate to point p
  // When last two rotations are zero, this just changes the view to look in the
  // direction of the negative x-axis, similar to looking down the z-axis in
  // clip coordinates
  view = RotateZ(-90) * RotateY(-90) * RotateY(phiPrime) * RotateZ(-theta) * Translate(-p);


  if (!pPaused)
  {
    pCoord += pIncrement;
    if (pCoord > 1.0) pCoord = 0.0;
  }
  projection = Perspective(fovy, 1.0, 0.1, r + 100.0);

  GLint loc = glGetUniformLocation(program, "model");
  glUniformMatrix4fv(loc, 1, GL_TRUE, model); 
  loc = glGetUniformLocation(program, "view");
  glUniformMatrix4fv(loc, 1, GL_TRUE, view); 
  loc = glGetUniformLocation(program, "projection");
  glUniformMatrix4fv(loc, 1, GL_TRUE, projection); 

  loc = glGetUniformLocation(program, "state");
  glUniform1i(loc, state);

  loc = glGetUniformLocation(program, "pCoord");
  glUniform1f(loc, pCoord);

  glBindVertexArray(vao);
  glDrawArrays(GL_TRIANGLES, 0, numVertices);
  glBindVertexArray(0);
  glUseProgram(0);

  // display the back buffer now
  glutSwapBuffers();
}

//----------------------------------------------------------------------------

void keyboard( unsigned char key, int x, int y )
{
  switch( key ) {
  case 033: // Escape Key
  case 'q': case 'Q':
    exit( EXIT_SUCCESS );
    break;
  case 'p':
    pPaused = 1 - pPaused;
    break;

  case '+':
    pIncrement += 0.005;
    break;
  case '-':
    pIncrement -= 0.005;
    break;
    break;
  case GLUT_KEY_UP:
    phiPrime += 5;
    break;
  case GLUT_KEY_DOWN:
    phiPrime -= 5;
    break;
  case GLUT_KEY_RIGHT:
    theta += 5;
    break;
  case GLUT_KEY_LEFT:
    theta -= 5;
    break;
  case 's':
    r += 1.0;
    break;
  case 'w':
    r -= 1.0;
    //std::cout << r << std::endl;
    break;

  case '0':
    state = 0;
    break;
  case '1':
    state = 1;
    break;
  case '2':
    state = 2;
    break;
  case '3':
    state = 3;
    break;
  case '4':
    state = 4;
    break;
  case '5':
    state = 5;
    break;
  case '6':
    state = 6;
    break;
  case '7':
    state = 7;
    break;
  }
}

// Get key events for arrow keys
void keyboardSpecial(int key, int x, int y)
{
  if (key < 256)
  {
    keyboard((unsigned char) key, x, y);
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

//----------------------------------------------------------------------------

int main( int argc, char **argv )
{
  glutInit( &argc, argv );

  // Set up the graphics context with double-buffering
  glutInitDisplayMode( GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH );
  glutInitWindowSize( 512, 512 );
  glutCreateWindow( "Lighting and shading examples" );

  glewInit();

  init();

  glutDisplayFunc( display );
  glutKeyboardFunc( keyboard );
  glutSpecialFunc( keyboardSpecial );
  glutIdleFunc( idle );

  elapsedTime = glutGet(GLUT_ELAPSED_TIME);

  glutMainLoop();
  return 0;
}
