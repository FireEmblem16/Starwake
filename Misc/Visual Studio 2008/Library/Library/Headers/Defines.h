#ifndef DEFINES_H
#define DEFINES_H

#include <stdio.h>

#define SUCCESS 0x0
#define FILE_NOT_FOUND 0x1

#define BAD_PTR 0xCCCCCCCC

typedef signed char int8_t;
typedef unsigned char uint8_t;

typedef signed short int int16_t;
typedef unsigned short int uint16_t;

typedef signed int int32_t;
typedef unsigned int uint32_t;

typedef signed long long int int64_t;
typedef unsigned long long int uint64_t;

typedef FILE* PFILE;

typedef struct Color
{
	uint8_t R;
	uint8_t G;
	uint8_t B;
	uint8_t A;
} COLOR, *PCOLOR;

typedef struct LibraryNode
{
	void* data;
	LibraryNode* next;
} LIBRARYNODE, *PLIBRARYNODE;

#endif