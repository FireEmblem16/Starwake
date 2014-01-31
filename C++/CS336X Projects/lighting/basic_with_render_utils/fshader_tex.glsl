#version 150

uniform sampler2D baseTexture;
uniform sampler2D overlayTexture;

in  vec2 fTexCoord;
out vec4 color;

void main() 
{ 
    // set to the color sampled from the texture
    vec4 base = texture2D( baseTexture, fTexCoord );  
	vec4 overlay = texture2D( overlayTexture, fTexCoord );  
	float alpha = overlay.a;
	color = (1 - alpha) * base + alpha * overlay;
} 

