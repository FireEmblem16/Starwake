#version 150

//
// Vertex shader for per-fragment lighting calculation (Phong shading).
//

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat3 normalMatrix;
uniform vec4 lightPosition;

in  vec4 vPosition;
in vec3 vNormal;
out vec3 fN;
out vec3 fL;
out vec3 fV;

void main() 
{
  // Transform the relevant vectors into eye space, and pass
  // to the rasterizer.  Normalization will be done after
  // interpolation.

  // Transform normal vector to eye space
  fN = normalMatrix * vNormal;

  // Positional light, vector depends on position
  fL = (view * lightPosition - view * model * vPosition).xyz;

  fV = -(view * model * vPosition).xyz;
  
  gl_Position = projection * view * model * vPosition;
} 
