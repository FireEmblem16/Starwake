/*
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Error.h /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides some standard error inferfacing to report problems to the user.    ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _ERROR_H
#define	_ERROR_H

#include "Defines.h"

/* Define our error codes */
#define TOO_MANY_ARGUMENTS 0x1
#define TOO_FEW_ARGUMENTS 0x2
#define INVALID_NUMBER_OF_ARGUMENTS 0x3
#define INVALID_IMAGE_COLOR_FORMAT 0x4
#define INVALID_ARGUMENT 0x5
#define INVALID_ARGUMENT_NUMBER_EXPECTED 0x6
#define INVALID_ARGUMENT_STRING_EXPECTED 0x7
#define UNEXPECTED_END_OF_FILE 0x8
#define ARGUMENT_OUT_OF_RANGE 0x9
#define INVALID_CHANNEL_SIZE 0xA
#define INVALID_IMAGE_FORMAT 0xB
#define NULL_POINTER 0xC
#define FILE_NOT_FOUND 0xD
#define FILE_NOT_CLOSED 0xE
#define GENERAL_ERROR 0xF
#define KERNEL_FORMAT_ERROR 0x10
#define COLOR_KERNEL_EXPECTED 0x11
#define BLACKWHITE_KERNEL_EXPECTED 0x12
#define INVALID_KERNEL_SIZE 0x13

/* This will take an error code and output a relevant error to stderr */
extern void ReportError(uint32_t);

/* This will take an error code and output a relevant string to stderr */
extern void ReportDebugError(uint32_t,uint8_t*,uint32_t);

#endif
