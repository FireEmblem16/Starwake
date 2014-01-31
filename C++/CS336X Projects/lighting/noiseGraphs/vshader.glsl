#version 150

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec4 vertexColor;

in  vec4 vPosition;
out vec4 fColor;

void main() 
{
  // height should always be between -0.5 and 0.5
  vec4 red = vec4(1.0, 0.0, 0.0, 1.0);
  vec4 green = vec4(0.0, 1.0, 0.0, 1.0);
  float m = vPosition.z + 0.5;

  // this is kind of a hack, uses a zero alpha value to indicate that
  // vertex color should be based on height
  if (vertexColor.a > 0.0)
  {
    fColor = vertexColor;
  }
  else
  {
    fColor = red * m + green * (1 - m);
  }
  
  gl_Position = projection * view * model * vPosition;
} 
