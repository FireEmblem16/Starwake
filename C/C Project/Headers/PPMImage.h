/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// PPMImage.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides functionality to interface with ppm image files including:         ///
/// image operations, initialization, read in, write out and conversion.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _PPMIMAGE_H
#define	_PPMIMAGE_H

#include <stdlib.h>
#include "Defines.h"

/* This is the magic number for ppm images */
#define PPM_MAGIC_NUMBER 0x5036

/* Represents the header of a ppm image */
typedef struct ppmimage
{
    uint16_t magic_number;

    uint32_t height;
    uint32_t width;

    uint16_t max_color_value;
    uint32_t pixel_width;

    union
    {
        uint8_t* byte_ptr;
        uint16_t* word_ptr;
    } data;
} PPMIMG, *PPPMIMG;

/* Reads an image file into the first parameter from the supplied file pointer.
 * It is the user's responsibility to both open and close the file stream in it
 * is required. Any data already in the supplied in the image structure will
 * be overwritten without warning. If an error is returned then the supplied
 * file will be in an undefined location in the file.
 */
extern uint32_t read_ppmpicture(PPPMIMG,PFILE);

/* Writes an image to the file provided. It is the user's responsibility to both
 * open and close the file used. */
extern uint32_t write_ppm_image(PPPMIMG,PFILE);

#endif
