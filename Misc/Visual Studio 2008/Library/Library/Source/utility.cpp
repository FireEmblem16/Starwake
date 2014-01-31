#ifndef STRING_CPP
#define STRING_CPP

#include "utility.h"

void strrem(char* str, uint32_t len)
{
	if(strlen(str) < len)
	{
		*str = '\0';
		return;
	}

	strcpy(str,(const char*)(str + len));
	return;
}

char* strchr(char* str, char* str2)
{
	do
	{
		if(*str == *str2)
			for(char* i = str, * j = str2, * k = i;j < (str2 + strlen(str2) + 1) && i < (str + strlen(str) + 1);i++,j++)
				if(*i != *j)
					break;
				else
					if(*(j + 1) == 0)
						return k;
	}
	while(*str++);
	
	return NULL;
}

char* strchrlast(char* str, char character)
{
	char* end = (char*)(strlen(str) + str);

	do
	{
		if(*end == character)
			return end;
	}
	while(str != end--);

	return NULL;
}

char* strchrnot(char* str, char character)
{
	do
	{
		if(*str != character)
			return str;
	}
	while(*str++);

	return NULL;
}

void gluLookAt(Vector eye, Vector target, Vector up)
{
	gluLookAt(eye[0],eye[1],eye[2],target[0],target[1],target[2],up[0],up[1],up[2]);

	return;
}

#endif