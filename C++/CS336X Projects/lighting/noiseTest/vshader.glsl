#version 150

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec4 lightVector;


in  vec4 vPosition;
in vec3 vNormal;
in vec2 vTexCoord;

out vec3 fN;
out vec3 fL;
out vec3 fV;
out vec2 fTexCoord;



void main() 
{
  // for lighting calculation do everything in eye coordinates
  fN = ((view * model * vec4(vNormal, 0.0)).xyz);
  fL = ((view * lightVector).xyz);
  fV = (-(view * model * vPosition).xyz);
 
  fTexCoord = vTexCoord;
  gl_Position = projection * view * model * vPosition;
} 
