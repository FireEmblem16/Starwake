#ifndef TEXTURE_LIBRARY_H
#define TEXTURE_LIBRARY_H

#include <string.h>

#include "Defines.h"
#include "Texture.h"

#define TEXTURE_NOT_FOUND 0x1
#define TEXTURE_ALREADY_LOADED 0x2

class TextureLibrary
{
public:
	TextureLibrary();
	~TextureLibrary();

	uint32_t LoadTexture(char* filename);
	uint32_t RemoveTexture(const char* filename);
	Texture* GetTexture(char* filename);
protected:
	
private:
	PLIBRARYNODE library;
	PLIBRARYNODE end;
};

#endif