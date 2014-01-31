#ifndef MODEL_LIBRARY_H
#define MODEL_LIBRARY_H

#include <string.h>

#include "Defines.h"
#include "Model.h"

#define MODEL_NOT_FOUND 0x1
#define MODEL_ALREADY_LOADED 0x2

class ModelLibrary
{
public:
	ModelLibrary();
	~ModelLibrary();

	uint32_t LoadModel(char* filename);
	uint32_t RemoveModel(const char* filename);
	Model* GetModel(char* filename);
protected:
	
private:
	PLIBRARYNODE library;
	PLIBRARYNODE end;
};

#endif