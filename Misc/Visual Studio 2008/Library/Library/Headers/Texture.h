#ifndef TEXTURE_H
#define TEXTURE_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "Defines.h"
#include "bmp_functions.h"

class Texture
{
public:
	Texture(char* filename);
	~Texture();

	const char* get_filename();
	PBMP get_texture();

	uint32_t GetPixel(uint8_t* store, int32_t x, int32_t y);
	uint32_t SetPixel(uint8_t* data, int32_t x, int32_t y);

	uint32_t GetWidth();
	uint32_t GetHeight();

	uint8_t* GetData();
protected:
	
private:
	char* filename;
	PBMP texture;
};

#endif