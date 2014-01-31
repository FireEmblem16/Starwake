#ifndef TEXTURE_LIBRARY_CPP
#define TEXTURE_LIBRARY_CPP

#include "Texture Library.h"

TextureLibrary::TextureLibrary()
{
	library = NULL;
	end = NULL;

	return;
}

TextureLibrary::~TextureLibrary()
{
	while(library)
	{
		PLIBRARYNODE old = library;
		library = library->next;
		delete old->data;
		delete old;
	}

	return;
}

uint32_t TextureLibrary::LoadTexture(char *filename)
{
	if(GetTexture(filename))
		return TEXTURE_ALREADY_LOADED;

	PFILE f = fopen(filename,"r");
	
	if(f == NULL)
		return TEXTURE_NOT_FOUND;

	fclose(f);

	if(library)
	{
		end->next = new LIBRARYNODE();
		end = end->next;
		
		end->next = NULL;
		end->data = new Texture(filename);
	}
	else
	{
		library = new LIBRARYNODE();
		end = library;

		library->next = NULL;
		library->data = new Texture(filename);
	}

	return SUCCESS;
}

uint32_t TextureLibrary::RemoveTexture(const char* filename)
{
	PLIBRARYNODE n = library;
	PLIBRARYNODE last = NULL;

	while(n)
		if(!strcmp(((Texture*)n->data)->get_filename(),(const char*)filename))
		{
			if(last)
				last->next = n->next;

			if(n == library)
				library = NULL;

			if(n == end)
				end = last;

			delete n->data;
			delete n;

			return SUCCESS;
		}
		else
		{
			last = n;
			n = n->next;
		}

	return TEXTURE_NOT_FOUND;
}

Texture* TextureLibrary::GetTexture(char* filename)
{
	PLIBRARYNODE n = library;

	while(n)
		if(!strcmp(((Texture*)n->data)->get_filename(),(const char*)filename))
			return (Texture*)n->data;
		else
			n = n->next;

	return NULL;
}

#endif