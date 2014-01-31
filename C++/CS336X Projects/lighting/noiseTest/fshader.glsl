#version 150



uniform sampler3D noiseTexture;
uniform int state;
uniform float pCoord;
in  vec2 fTexCoord;

out vec4 color;

void main() 
{   
  // The noise is a 3D texture, use p = 0 for now
  vec3 texCoord = vec3(fTexCoord, pCoord);
  vec4 sample = texture(noiseTexture, texCoord);
  
  // red component - frequency 4, range [0, 0.5]
  // green component - frequency 8, range [0, 0.25]
  // blue component - frequency 16, range [0, 0.125]
  // alpha component - frequency 32, range [0, 0.0625]
    
  // use a greyscale value in [0, 1], exaggerate contrast
  float c;
  switch (state)
  {
    case 0: c = sample.r * 2.0; break;
    case 1: c = sample.g * 4.0; break;
    case 2: c = sample.b * 8.0; break;
    case 3: c = sample.a * 16.0; break;
    case 4: c = sample.r * 2.0; break;
    case 5: c = (sample.r + sample.g) * 1.33; break;
    case 6: c = (sample.r + sample.g + sample.b) * 1.14; break;
    case 7: c = (sample.r + sample.g + sample.b + sample.a) * 1.06; break;
  }
  
  color = vec4(c, c, c, 1.0);  

} 

