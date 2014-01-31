#ifndef _DEFINES_H
#define _DEFINES_H

#include <stdio.h>

#ifndef NULL
#define NULL 0
#endif

#ifndef UNDEF_PTR
#define UNDEF_PTR 0xCCCCCCCC
#endif

#ifndef BAD_PTR
#define BAD_PTR 0xCDCDCDCD
#endif

#ifndef PTR
#define PTR unsigned int
#endif

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#ifndef EXIT_SUCCESS
#define EXIT_SUCCESS 0
#endif

#ifndef EXIT_FAILURE
#define EXIT_FAILURE 1
#endif

#ifndef PI
#define PI 3.141592653589793
#endif

#define SUCCESS 0x00000000
#define FAILURE 0x00000001
#define NULL_POINTER 0x00000001
#define IO_EXCEPTION 0x00000002
#define NO_HEADER_FOUND 0x00000003
#define NO_FREQUENCY_FOUND 0x00000004
#define NO_BITRES_FOUND 0x0000005
#define INVALID_CHANNEL_FORMAT 0x00000006
#define INVALID_NUMBER_FORMAT 0x00000007
#define INVALID_HEADER_TAG 0x00000008
#define INVALID_HEADER 0x00000009
#define INVALID_BITRES_FORMAT 0x0000000A
#define REDECLARATION_OF_TAG 0x0000000B
#define UNEXPECTED_END_OF_FILE 0x0000000C
#define UNEXPECTED_CONTINUATION_OF_FILE 0x0000000D
#define BUFFER_OVERFLOW 0x0000000E
#define SAMPLE_SIZE_MISMATCH 0x0000000F
#define FILE_NOT_FOUND 0x00000010
#define FILE_LEFT_OPEN 0x00000011
#define INCOMPATABLE_FILE_TYPES 0x00000012
#define INVALID_CONVERSION 0x00000013
#define INVALID_FREQUENCY_COMPONENT 0x00000014
#define WINDOW_TOO_BIG 0x00000015
#define AUDIO_DEVICE_NOT_FOUND 0x00000016
#define UNSUPPORTED_WAV_FORMAT 0x00000017
#define WAV_HEADER_CANNOT_BE_PREPARED 0x00000018
#define WAV_REJECTS_BUFFER 0x00000019
#define WAV_REFUSES_TO_START 0x0000001A
#define INVALID_WAV_HEADER 0x0000001B
#define INVALID_BITSIZE 0x0000001C
#define UNEXPECTED_FILE_TYPE 0x0000001D

typedef unsigned int uint32_t;
typedef unsigned short uint16_t;
typedef unsigned char uint8_t;

typedef signed int int32_t;
typedef signed short int16_t;
typedef signed char int8_t;

typedef FILE* PFILE;

typedef const char* c_string;

typedef struct complex
{
	double real;
	double imag;
} COMPLEX, *PCOMPLEX;

#endif