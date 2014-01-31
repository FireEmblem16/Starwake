#ifndef LIGHT_CPP
#define LIGHT_CPP

#include "Light.h"

Light::Light(Vector p, Vector c)
{
	pos = p;
	color = c;

	return;
}

Light::~Light()
{
	return;
}

Vector Light::GetColor()
{
	return color;
}

Vector Light::GetPosition()
{
	return pos;
}

uint32_t Light::GenerateLightmap(Vector* verts, uint32_t lightmap_size)
{
	SURFACE surf;

	for(uint32_t i = 0;i < 4;i++)
		surf.vertices[i] = verts[i];

	surf.matrix[0] = surf.vertices[3] - surf.vertices[0];
	surf.s_dist = surf.matrix[0].magnitude();
	surf.matrix[0].normalize();

	surf.matrix[1] = surf.vertices[1] - surf.vertices[0];
	surf.t_dist = surf.matrix[1].magnitude();
	surf.matrix[1].normalize();

	surf.matrix[2] = surf.matrix[0].cross(surf.matrix[1]);

	uint8_t* data = new uint8_t[lightmap_size * lightmap_size * 3];
	uint32_t texture_num;
	Vector position;
	GLfloat step = 1.0f / lightmap_size;
	GLfloat s = 0.0f, t = 0.0f;

	glGenTextures(1,&texture_num);
	
	for(uint32_t i = 0;i < lightmap_size;i++,t += step,s = 0.0f)
		for(uint32_t j = 0;j < lightmap_size;j++,s += step)
		{
			position[0] = surf.s_dist * s;
			position[1] = surf.t_dist * t;
			position[2] = 0.0f;

			GLfloat temp[3];

			temp[0] = position[0] * surf.matrix[0][0] + position[1] * surf.matrix[1][0] + position[2] * surf.matrix[2][0];
			temp[1] = position[0] * surf.matrix[0][1] + position[1] * surf.matrix[1][1] + position[2] * surf.matrix[2][1];
			temp[2] = position[0] * surf.matrix[0][2] + position[1] * surf.matrix[1][2] + position[2] * surf.matrix[2][2];

			position[0] = temp[0];
			position[1] = temp[1];
			position[2] = temp[2];

			position += surf.vertices[0];
			position -= pos;

			GLfloat d = 0.5f * position.magnitude();
			d = d < 1.0f ? 1.0f : d;
			d = 1.0f / d;

			data[i * lightmap_size * 3 + j * 3] = (uint8_t)(0xFF * d * color[0]);
			data[i * lightmap_size * 3 + j * 3 + 1] = (uint8_t)(0xFF * d * color[1]);
			data[i * lightmap_size * 3 + j * 3 + 2] = (uint8_t)(0xFF * d * color[2]);
		}


	glBindTexture(GL_TEXTURE_2D, texture_num);
	glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
	glTexImage2D(GL_TEXTURE_2D, 0, 3, lightmap_size, lightmap_size, 0, GL_RGB, GL_UNSIGNED_BYTE, data);


	return texture_num;
}

#endif