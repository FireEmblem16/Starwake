#ifndef MODEL_H
#define MODEL_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "Defines.h"
#include "utility.h"
#include "Vector.h"

#define MESH "Mesh"
#define NORM "Norms"
#define TEX "TexCoord"

class Model
{
public:
	Model(char* filename);
	~Model();

	const char* get_filename();
	uint32_t get_num_vert();
	uint32_t get_num_norm();
	uint32_t get_num_tex();
	Vector* get_vert();
	Vector* get_norm();
	Vector* get_tex();
protected:
	
private:
	bool gotoblock(PFILE file, char* str);
	bool read_line(PFILE file, char* buf, uint32_t len);
	uint32_t parse_int(char* str, uint32_t ordinal_index);
	GLfloat parse_float(char* str, uint32_t ordinal_index);
	Vector* load_vert(FILE* file);
	Vector* load_norm(FILE* file);
	Vector* load_tex(FILE* file);

	char* filename;
	Vector* vert;
	Vector* norm;
	Vector* tex;
	uint32_t num_vert;
	uint32_t num_norm;
	uint32_t num_tex;
};

#endif