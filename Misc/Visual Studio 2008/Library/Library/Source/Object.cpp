#ifndef OBJECT_CPP
#define OBJECT_CPP

#include "Object.h"

Object::Object(Model* m, Texture* t)
{
	model = m;
	texture = t;

	vert = model->get_vert();
	norm = model->get_norm();
	tex = model->get_tex();

	num_vert = model->get_num_vert();
	num_norm = model->get_num_norm();
	num_tex = model->get_num_tex();

	disp = Vector(0.0f,0.0f,0.0f);
	distortion = Vector(1.0f,1.0f,1.0f);
	rot = Vector(0.0f,0.0f,0.0f);

	return;
}

Object::Object(Object& o2)
{
	model = o2.model;
	texture = o2.texture;

	num_vert = o2.num_vert;
	num_norm = o2.num_norm;
	num_tex = o2.num_tex;

	vert = o2.vert;
	norm = o2.norm;
	tex = o2.tex;

	disp = o2.disp;
	distortion = o2.distortion;
	rot = o2.rot;

	return;
}

Object::Object(Object* o2)
{
	model = o2->model;
	texture = o2->texture;

	num_vert = o2->num_vert;
	num_norm = o2->num_norm;
	num_tex = o2->num_tex;

	vert = o2->vert;
	norm = o2->norm;
	tex = o2->tex;

	disp = o2->disp;
	distortion = o2->distortion;
	rot = o2->rot;

	return;
}

Object Object::operator =(Object o2)
{
	model = o2.model;
	texture = o2.texture;

	num_vert = o2.num_vert;
	num_norm = o2.num_norm;
	num_tex = o2.num_tex;

	vert = o2.vert;
	norm = o2.norm;
	tex = o2.tex;

	disp = o2.disp;
	distortion = o2.distortion;
	rot = o2.rot;

	return *this;
}

Object::~Object()
{
	return;
}

bool Object::operator ==(Object o2)
{
	return !strcmp(model->get_filename(),o2.model->get_filename());
}

Vector Object::displacement()
{
	return disp;
}

Vector Object::rotation()
{
	return rot;
}

Vector Object::scale()
{
	return distortion;
}

void Object::move(Vector dist)
{
	disp += dist;

	return;
}

void Object::moveto(Vector pos)
{
	disp = pos;

	return;
}

void Object::rotate(Vector rotation)
{
	rot += rotation;
	wrap();

	return;
}

void Object::rotateto(Vector angles)
{
	rot = angles;
	wrap();

	return;
}

void Object::scale(Vector scale_inc)
{
	distortion += scale_inc;

	return;
}

void Object::scaleto(Vector percent_size)
{
	distortion = percent_size;

	return;
}

Model* Object::getmodel()
{
	return model;
}

void Object::draw()
{
	glPushMatrix();
	{
		glScalef(distortion[0],distortion[1],distortion[2]);
		glTranslatef(disp[0],disp[1],disp[2]);
		glRotatef(rot[0],1.0f,0.0f,0.0f);
		glRotatef(rot[1],0.0f,1.0f,0.0f);
		glRotatef(rot[2],0.0f,0.0f,1.0f);

		glColor4f(1.0f,1.0f,1.0f,0.0f);
		
		if(texture != NULL)
		{
			glPixelStorei(GL_UNPACK_ALIGNMENT,1);
			glTexImage2D(GL_TEXTURE_2D,0,4,texture->GetWidth(),texture->GetHeight(),0,GL_RGB,GL_UNSIGNED_BYTE,(GLvoid*)texture->GetData());
			glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
			glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
			glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
			glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
		}

		glBegin(GL_QUADS);
		{
			for(uint32_t i = 0;i < num_vert;i++)
			{
				if(!(i & 0x3))
					glNormal3fv(norm[i >> 2]);

				if(i < model->get_num_tex())
					glTexCoord2fv(tex[i]);
				
				glVertex3fv(vert[i]);
			}
		}
		glEnd();
	}
	glPopMatrix();

	return;
}

void Object::wrap()
{
	GLfloat* vals = rot;

	for(uint32_t i = 0;i < 3;i++)
	{
		if(vals[i] < 0)
			while(vals[i] < 0.0f)
				vals[i] += 360.0f;
		else
			while(vals[i] >= 360.0f)
				vals[i] -= 360.0f;
	}

	return;
}

#endif