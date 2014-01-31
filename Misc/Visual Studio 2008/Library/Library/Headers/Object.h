#ifndef OBJECT_H
#define OBJECT_H

#include <stdlib.h>
#include <GL/gl.h>

#include "Defines.h"
#include "Vector.h"
#include "Model.h"
#include "Texture.h"
#include "Light.h"

class Object
{
public:
	Object(Model* m, Texture* t);
	Object(Object& o2);
	Object(Object* o2);
	Object operator =(Object o2);
	~Object();

	bool operator ==(Object o2);

	Vector displacement();
	Vector rotation();
	Vector scale();

	void move(Vector dist);
	void moveto(Vector pos);
	void rotate(Vector rotation);
	void rotateto(Vector angles);
	void scale(Vector scale_inc);
	void scaleto(Vector percent_size);

	Model* getmodel();
	Texture* gettexture();

	void draw();
protected:
	void wrap();

	Model* model;
	Texture* texture;

	uint32_t tex_num;

	uint32_t num_vert;
	uint32_t num_norm;
	uint32_t num_tex;

	Vector* vert;
	Vector* norm;
	Vector* tex;

	Vector disp;
	Vector distortion;
	Vector rot;
};

#endif