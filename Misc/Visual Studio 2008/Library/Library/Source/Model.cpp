#ifndef MODEL_CPP
#define MODEL_CPP

#pragma warning(disable:4800)

#include "Model.h"

Model::Model(char* filename)
{
	this->filename = (char*)malloc(strlen(filename) + 1);
	strcpy(this->filename,filename);
	PFILE file = fopen(filename,"r");

	vert = load_vert(file);
	norm = load_norm(file);
	tex = load_tex(file);

	if(file)
		fclose(file);

	return;
}

Model::~Model()
{
	if(filename)
		free(filename);

	if(vert)
		delete[] vert;
	
	if(norm)
		delete[] norm;

	if(tex)
		delete[] tex;
	
	return;
}

const char* Model::get_filename()
{
	return (const char*)filename;
}

uint32_t Model::get_num_vert()
{
	return num_vert;
}

uint32_t Model::get_num_norm()
{
	return num_norm;
}

uint32_t Model::get_num_tex()
{
	return num_tex;
}

Vector* Model::get_vert()
{
	return vert;
}

Vector* Model::get_norm()
{
	return norm;
}

Vector* Model::get_tex()
{
	return tex;
}

bool Model::gotoblock(PFILE file, char* str)
{
	if(!file)
		return false;

	char buf[256];

	while(fgets(buf,256,file))
	{
		*(strchr(buf,'\0') - 1) = '\0';

		if(!strcmp(buf,str))
			break;
	}

	if(!feof(file))
		fgets(buf,256,file);

	return buf[0] == '{';
}

bool Model::read_line(PFILE file, char* buf, uint32_t len)
{
	if(!file || feof(file))
		return false;

	return fgets(buf,len,file);
}

uint32_t Model::parse_int(char* str, uint32_t ordinal_index)
{
	char buf[256];
	strcpy(buf,str);
	strrem(buf,(uint32_t)(strchrnot(buf,'\t') - buf));

	char* start = &buf[0];

	for(uint32_t i = ordinal_index;i > 1;i--)
		if(start)
			start = strchr(start,';') + 1;
		else
			return NULL;
	
	char* end = strchr(start,';');

	if(!end)
		return NULL;
	
	*end = '\0';
	return atoi(start);
}

GLfloat Model::parse_float(char* str, uint32_t ordinal_index)
{
	char buf[256];
	strcpy(buf,str);
	strrem(buf,(uint32_t)(strchrnot(buf,'\t') - buf));

	char* start = &buf[0];

	for(uint32_t i = ordinal_index;i > 1;i--)
		if(start)
			start = strchr(start,';') + 1;
		else
			return NULL;
	
	char* end = strchr(start,';');

	if(!end)
		return NULL;
	
	*end = '\0';
	return (GLfloat)atof(start);
}

Vector* Model::load_vert(FILE* file)
{
	num_vert = 0;

	if(!file || !gotoblock(file,MESH))
		return NULL;

	char buf[256];

	if(read_line(file,buf,256))
	{
		if((num_vert = parse_int(buf,1)) == 0)
			return NULL;
	}
	else
		return NULL;

	Vector* ret = new Vector[num_vert];

	for(uint32_t i = 0;i < num_vert;i++)
	{
		if(!read_line(file,buf,256))
			return NULL;

		ret[i] = Vector(parse_float(buf,2),parse_float(buf,3),parse_float(buf,4));;
	}

	if(!read_line(file,buf,256))
		return NULL;

	if(buf[0] != '}')
		return NULL;

	return ret;
}

Vector* Model::load_norm(FILE* file)
{
	if(!file || !gotoblock(file,NORM))
	{
		if(!vert)
			return NULL;

		num_norm = num_vert >> 2;

		if(num_norm <= 0)
			return NULL;

		Vector* ret = new Vector[num_norm];

		for(uint32_t i = 0;i < num_norm && i + 2 < num_vert;i += 4)
			ret[i] = (vert[i+1] - vert[i]).cross(vert[i+1] - vert[i+2]);

		return ret;
	}

	char buf[256];

	if(read_line(file,buf,256))
	{
		if((num_norm = parse_int(buf,1)) == 0)
			return NULL;
	}
	else
		return NULL;

	Vector* ret = new Vector[num_norm];

	for(uint32_t i = 0;i < num_norm;i++)
	{
		if(!read_line(file,buf,256))
			return NULL;

		ret[i] = Vector(parse_float(buf,2),parse_float(buf,3),parse_float(buf,4));
	}

	if(!read_line(file,buf,256))
		return NULL;

	if(buf[0] != '}')
		return NULL;

	return ret;
}

Vector* Model::load_tex(FILE* file)
{
	num_tex = 0;

	if(!file || !gotoblock(file,TEX))
		return NULL;

	char buf[256];

	if(read_line(file,buf,256))
	{
		if((num_tex = parse_int(buf,1)) == 0)
			return NULL;
	}
	else
		return NULL;

	Vector* ret = new Vector[num_tex];

	for(uint32_t i = 0;i < num_tex;i++)
	{
		if(!read_line(file,buf,256))
			return NULL;

		ret[i] = Vector(parse_float(buf,2),parse_float(buf,3),0.0f);
	}

	if(!read_line(file,buf,256))
		return NULL;

	if(buf[0] != '}')
		return NULL;

	return ret;
}

#endif