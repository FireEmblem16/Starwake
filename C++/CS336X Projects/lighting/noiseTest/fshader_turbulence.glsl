#version 150



uniform sampler3D noiseTexture;
uniform int state;
uniform float pCoord;
in  vec2 fTexCoord;

out vec4 color;

void main() 
{   
  // The noise is a 3D texture, default is to use p = 0 for now
  vec3 texCoord = vec3(fTexCoord, pCoord);
  vec4 sample = texture(noiseTexture, texCoord);
  
  // red component - frequency 4, range [0, 0.5]
  // green component - frequency 8, range [0, 0.25]
  // blue component - frequency 16, range [0, 0.125]
  // alpha component - frequency 32, range [0, 0.0625]
  
  // use a greyscale value in [0, 1]
  
  // recenter and take absolute value first
  sample.r = abs(sample.r - 0.25);
  sample.g = abs(sample.g - 0.125);
  sample.b = abs(sample.b - 0.0625);
  sample.a = abs(sample.a - 0.03125);
  
  float c;
  switch (state)
  {
    case 0: c = sample.r; break;
    case 1: c = sample.g; break;
    case 2: c = sample.b; break;
    case 3: c = sample.a; break;
    case 4: c = sample.r; break;
    case 5: c = (sample.r + sample.g); break;
    case 6: c = (sample.r + sample.g + sample.b); break;
    case 7: c = (sample.r + sample.g + sample.b + sample.a); break;
  }
  
  // use sampled value to modulate between red and yellow
  vec4 red =  vec4(1.0, 0.0, 0.0, 1.0);
  vec4 yellow = vec4(1.0, 1.0, 0.0, 1.0);
  float m = c * 2.5; // scale up a bit
  color = (1 - m) * yellow + m * red;

} 

