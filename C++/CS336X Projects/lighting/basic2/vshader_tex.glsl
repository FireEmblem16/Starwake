#version 150

uniform mat4 transform;
in  vec4 vPosition;
in  vec2 vTexCoord;
out vec2 fTexCoord;

void main() 
{
  // pass to fragment shader as varying
  fTexCoord = vTexCoord;
  gl_Position = transform * vPosition;
} 
