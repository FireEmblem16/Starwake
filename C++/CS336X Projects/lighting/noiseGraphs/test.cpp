//
// Noise demo that shows graphs of 1 and 2 dimensional noise functions.
// Note that in these functions, the z axis is "up".  Creates a very simple
// height map just by plotting individual points, using a shader that modulates
// the color based on height.
//
// Use "f" to increase base frequency and "F" to decrease it.
// 
// Number keys 1 - 7 determine the number of octaves added.
//
// Use "d" to toggle between 1 dimensional and two dimensional.
//
// Other controls as usual, ("w", "s" and arrow keys)
//

#include "Angel.h"
#include "square.h"

const int fieldSize = 256;

// declarations from Noise.cpp
double noise2(double vec[2]);
double noise1(double v);
void SetNoiseFrequency(int frequency);

// Creates vertices for graph of a two-dimensional noise function
// This plots points in a unit square [0, 1] x [0, 1]
vec3 * makeNoise(int size, int frequency, int octaves)
{
  SetNoiseFrequency(frequency);
  //numVertices = size * size;
  int sizeOfPoints = size * size * sizeof(vec3);
  vec3 * points = (vec3 *) malloc(sizeOfPoints);

  float delta = (1.0 / size);
  for (int i = 0; i < size; ++i)
  {
    float x = i * delta;
    for (int j = 0; j < size; ++j)
    {
      float y = j * delta;
      
      float nn = 0.0;
      int f = frequency;
      for (int k = 0; k < octaves; ++k)
      {
        double arg[2] = {x * f, y * f};
        nn += noise2(arg) / f;
        f *= 2;
      }
      vec3 point = vec3(x, y, nn);
      int offset = i * size + j;
      *(points + offset) = point;
    }
  }
  return points;
}

// Creates vertices for graph of a one-dimensional noise function
vec3 * makeNoise1d(int size, int frequency, int octaves)
{
  float max = 0.0;
  SetNoiseFrequency(frequency);
  //numVertices = size;
  int sizeOfPoints = size * sizeof(vec3);
  vec3 * points = (vec3 *) malloc(sizeOfPoints);

  float delta = (1.0 / size);
    float x = 0.5;
    for (int j = 0; j < size; ++j)
    {
      float y = j * delta;
      
      float nn = 0.0;
      int f = frequency;
      for (int k = 0; k < octaves; ++k)
      {
        double arg = y * f;
        nn += noise1(arg) / f;
        f *= 2;
      }
      vec3 point = vec3(x, y, nn * 5); // exaggerate scale * 5 for visibility
      //int offset = i * size + j;
      int offset = j;
      *(points + offset) = point;
    }
  
  return points;
}

int state = 0;
int frequency = 4;
int octaves = 1;
int dimensions = 1;


// Viewing direction, similar to ortho.cpp
GLfloat theta = 0.0;
GLfloat phiPrime = 0.0;

// distance from origin
GLfloat r = 10.0;

// Default field of view for Perspective() matrix
GLfloat fovy = 30.0;

// Center the unit square 
mat4 model = Translate(-0.5, -0.5, 0.0);// = RotateY(90.0);

// Viewing transformation
mat4 view;

// Projection matrix
mat4 projection;

GLuint vao, axisVao;
GLuint program;
GLuint buffer;


void init()
{
  // Create and initialize buffer object

  glGenBuffers(1, &buffer);
 
  GLuint buffer2;
  glGenBuffers(1, &buffer2);
  glBindBuffer(GL_ARRAY_BUFFER, buffer2);
  vec3 axis[2] = {vec3(0.0, -1.0, 0.0), vec3(0.0, 1.0, 0.0)};
  glBufferData(GL_ARRAY_BUFFER, 2 * sizeof(vec3), axis, GL_STATIC_DRAW);

  // Load shaders 
  program = InitShader( "vshader.glsl", "fshader.glsl" );
  glUseProgram( program );

  // set up vertex arrays
  glGenVertexArrays(1, &vao);
  glBindVertexArray(vao);
  glBindBuffer(GL_ARRAY_BUFFER, buffer);

  // vPosition is an "in" variable in the vertex shader
  GLuint vPosition = glGetAttribLocation( program, "vPosition" );
  glEnableVertexAttribArray( vPosition );
  glVertexAttribPointer( vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0) );

  glGenVertexArrays(1, &axisVao);
  glBindVertexArray(axisVao);
  glBindBuffer(GL_ARRAY_BUFFER, buffer2);

  vPosition = glGetAttribLocation( program, "vPosition" );
  glEnableVertexAttribArray( vPosition );
  glVertexAttribPointer( vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0) );


  glEnable( GL_DEPTH_TEST );
  glClearColor( 0.0, 0.0, 0.0, 1.0 ); 
}

//----------------------------------------------------------------------------

void display( void )
{

  vec3 * points;
  int sizeOfPoints;
  int numVertices;

  // Generate the vertices and send to GPU
  if (dimensions == 2)
  {
    points = makeNoise(fieldSize, frequency, octaves);
    numVertices = fieldSize * fieldSize;
    sizeOfPoints = fieldSize * fieldSize * sizeof(vec3);
  }
  else
  {
    points = makeNoise1d(fieldSize, frequency, octaves);
    numVertices = fieldSize;
    sizeOfPoints = fieldSize * sizeof(vec3);
  }
  glBindBuffer(GL_ARRAY_BUFFER, buffer);
  glBufferData(GL_ARRAY_BUFFER, sizeOfPoints, points, GL_STATIC_DRAW);
  free(points);

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

  projection = Perspective(fovy, 1.0, 0.1, r + 100.0);

  GLint loc = glGetUniformLocation(program, "vertexColor");
  glUniform4fv(loc, 1, vec4(0.0, 0.0, 0.0, 0.0)); 

  loc = glGetUniformLocation(program, "model");
  glUniformMatrix4fv(loc, 1, GL_TRUE, model); 
  loc = glGetUniformLocation(program, "view");
  glUniformMatrix4fv(loc, 1, GL_TRUE, view); 
  loc = glGetUniformLocation(program, "projection");
  glUniformMatrix4fv(loc, 1, GL_TRUE, projection); 

  glBindVertexArray(vao);
  glPointSize(2);
  glDrawArrays(GL_POINTS, 0, numVertices);

  // draw y-axis
  loc = glGetUniformLocation(program, "model");
  glUniformMatrix4fv(loc, 1, GL_TRUE, mat4()); 
  loc = glGetUniformLocation(program, "vertexColor");
  glUniform4fv(loc, 1, vec4(1.0, 0.0, 0.0, 1.0)); 
 
  glBindVertexArray(axisVao);
  glDrawArrays(GL_LINES, 0, 2);


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

  case 's':
    r += 1.0;
    break;
  case 'w':
    r -= 1.0;
    std::cout << r << std::endl;
    break;

  case 'f':
    frequency *= 2;
    break;
  case 'F':
    frequency *= 0.5;
    frequency = frequency < 1 ? 1 : frequency;
    break;

  case 'd':
    dimensions = dimensions == 1 ? 2 : 1;
    break;
  case '1':
    octaves = 1;
    break;
  case '2':
    octaves = 2;
    break;
  case '3':
    octaves = 3;
    break;
  case '4':
    octaves = 4;
    break;
  case '5':
    octaves = 5;
    break;
  case '6':
    octaves = 6;
    break;
  case '7':
    octaves = 7;
    break;
  }
  std::cout << "Frequency " << frequency << std::endl;
  std::cout << "Octaves " << octaves << std::endl;
  glutPostRedisplay();
}

// Get key events for arrow keys
void keyboardSpecial(int key, int x, int y)
{
  switch( key ) {
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
  }
  glutPostRedisplay();
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

  glutMainLoop();
  return 0;
}
