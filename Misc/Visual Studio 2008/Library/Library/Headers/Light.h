#ifndef LIGHT_H
#define LIGHT_H

#include <GL/gl.h>

#include "Defines.h"
#include "Vector.h"

typedef struct surface
{
	Vector vertices[4];
	Vector matrix[3];
	GLfloat s_dist;
	GLfloat t_dist;
} SURFACE, *PSURFACE;

class Light
{
public:
	Light(Vector p, Vector c);
	~Light();

	Vector GetPosition();
	Vector GetColor();
	uint32_t GenerateLightmap(Vector* verts, uint32_t lightmap_size);
protected:

private:
	Vector pos;
	Vector color;
};

#endif