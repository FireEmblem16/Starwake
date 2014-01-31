#ifndef MODEL_LIBRARY_CPP
#define MODEL_LIBRARY_CPP

#include "Model Library.h"

ModelLibrary::ModelLibrary()
{
	library = NULL;
	end = NULL;

	return;
}

ModelLibrary::~ModelLibrary()
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

uint32_t ModelLibrary::LoadModel(char *filename)
{
	if(GetModel(filename))
		return MODEL_ALREADY_LOADED;

	PFILE f = fopen(filename,"r");
	
	if(f == NULL)
		return MODEL_NOT_FOUND;

	fclose(f);

	if(library)
	{
		end->next = new LIBRARYNODE();
		end = end->next;
		
		end->next = NULL;
		end->data = new Model(filename);
	}
	else
	{
		library = new LIBRARYNODE();
		end = library;

		library->next = NULL;
		library->data = new Model(filename);
	}

	return SUCCESS;
}

uint32_t ModelLibrary::RemoveModel(const char* filename)
{
	PLIBRARYNODE n = library;
	PLIBRARYNODE last = NULL;

	while(n)
		if(!strcmp(((Model*)n->data)->get_filename(),(const char*)filename))
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

	return MODEL_NOT_FOUND;
}

Model* ModelLibrary::GetModel(char* filename)
{
	PLIBRARYNODE n = library;

	while(n)
		if(!strcmp(((Model*)n->data)->get_filename(),(const char*)filename))
			return (Model*)n->data;
		else
			n = n->next;

	return NULL;
}

#endif