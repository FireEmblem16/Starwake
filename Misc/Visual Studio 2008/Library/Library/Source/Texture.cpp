#ifndef TEXTURE_CPP
#define TEXTURE_CPP

#include "Texture.h"

Texture::Texture(char *filename)
{
	this->filename = (char*)malloc(strlen(filename) + 1);
	strcpy(this->filename,filename);

	texture = load_bmp(filename);
	return;
}

Texture::~Texture()
{
	if(filename)
		free(filename);

	delete_bmp(texture);
	return;
}

const char* Texture::get_filename()
{
	return (const char*)filename;
}

PBMP Texture::get_texture()
{
	return texture;
}

uint32_t Texture::GetPixel(uint8_t* store, int32_t x, int32_t y)
{
	return get_pixel(texture,store,x,y);
}

uint32_t Texture::SetPixel(uint8_t* data, int32_t x, int32_t y)
{
	return get_pixel(texture,data,x,y);
}

uint32_t Texture::GetWidth()
{
	return texture->dib.width;
}

uint32_t Texture::GetHeight()
{
	return texture->dib.height;
}

uint8_t* Texture::GetData()
{
	return texture->data;
}

#endif