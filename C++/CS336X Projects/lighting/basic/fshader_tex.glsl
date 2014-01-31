#version 150

uniform sampler2D texture;

in  vec2 fTexCoord;
out vec4 color;

void main() 
{ 
    // set to the color sampled from the texture
    color = texture2D( texture, fTexCoord );  
} 

