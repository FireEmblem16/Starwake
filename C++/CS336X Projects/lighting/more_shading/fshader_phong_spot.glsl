#version 150

uniform mat3 materialProperties;
uniform mat3 lightProperties;
uniform float shininess;
uniform bool useHalfVector;
uniform vec3 spotDirection;       // needs to be in eye coordinates
uniform float spotCosine;         // cosine of spot angle
uniform float spotExponent;       // falloff from center of spot light

in vec3 fN;
in vec3 fL;
in vec3 fV;

out vec4 color;

void main() 
{ 
  // have to normalize after interpolation
  vec3 N = normalize(fN);
  vec3 L = normalize(fL);
  vec3 V = normalize(fV);
  
  // Remaining code is directly copied from the vertex shader for per-vertex lighting
  
  vec3 R = normalize(reflect(-L, N));
  //Or use: vec3 R = normalize(2 * (dot(L, N)) * N - L);
  
  // Multiply material by light componentwise and then extract the rows.
  // Rows are A, D, and S, and columns are R, G, and B components
  // Note that products[i] is ith *column* in GLSL, which is why we
  // have to transpose
  mat3 products = matrixCompMult(lightProperties, materialProperties);
  products = transpose(products);
  
  vec4 ambientProduct = vec4(products[0], 1);
  vec4 diffuseProduct = vec4(products[1], 1);
  vec4 specularProduct = vec4(products[2], 1);

  // ambient intensity
  vec4 ia = ambientProduct;
  
  // diffuse intensity
  float diffuseFactor = max(dot(L, N), 0.0);
  vec4 id = diffuseFactor * diffuseProduct;
  
  float spotAttenuation = 0;
  if (dot(L, N) > 0)
  {
    float cosine = dot(-L, spotDirection);
	if (cosine > spotCosine) 
	{
		spotAttenuation = pow(cosine, spotExponent);
	}
  }
  

  // specular intensity
  vec4 is = vec4(0.0, 0.0, 0.0, 1.0);  
  if (dot(L, N) >= 0.0) 
  {
    float specularFactor;
    if (useHalfVector)
    {  
      // alternative calculation uses "half" vector
      vec3 h = normalize(L + V);  
      specularFactor = pow(max(dot(N, h), 0.0), shininess);    
    }
    else
    {
      specularFactor = pow(max(dot(R, V), 0.0), shininess);
    }
    is = specularFactor * specularProduct;
  }
  

  color = spotAttenuation * (ia + id + is);
  color.a = 1.0;
} 
