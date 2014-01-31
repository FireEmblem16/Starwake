/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// PPMImage.c ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides functionality to interface with ppm image files including:         ///
/// image operations, initialization, read in, write out and conversion.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _PPMIMAGE_C
#define _PPMIMAGE_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/PPMImage.h"

/* Reads an image file into the first parameter from the supplied file pointer.
 * It is the user's responsibility to both open and close the file stream in it
 * is required. Any data already in the supplied in the image structure will
 * be overwritten without warning. If an error is returned then the supplied
 * file will be in an undefined location in the file.
 */
uint32_t read_ppmpicture(PPPMIMG dest, PFILE source)
{
    /* Create a variable for error checking */
    uint32_t err;

    /* Create buffers for reading */
    uint32_t magic;
    uint8_t hold;
    uint8_t* pdata;
    uint8_t* final;

    /* If we don't have a destination then we have failed */
    if(!dest)
        return NULL_POINTER;

    /* If we don't have a source then we have failed */
    if(!source)
        return FILE_NOT_FOUND;

    /* Read in the magic number of this image */
    if((err = fscanf(source,"%c",&hold)) == EOF)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we read in our magic number */
    if(err != 1)
        return INVALID_IMAGE_FORMAT;

    /* Update our magic number */
    magic = hold << 8;

    /* Read in the magic number of this image */
    if((err = fscanf(source,"%c",&hold)) == EOF)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we read in our magic number */
    if(err != 1)
        return INVALID_IMAGE_FORMAT;

    /* Update our magic number */
    magic += hold;

    /* If we didn't get a good magic number don't do anything else */
    if(magic != PPM_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Make our magic number */
    dest->magic_number = PPM_MAGIC_NUMBER;

    /* Read in the width of the image */
    if((err = fscanf(source,"%u",&(dest->width))) == EOF)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we read in our integer */
    if(err != 1)
        return INVALID_IMAGE_FORMAT;

    /* Read in the width of the image */
    if((err = fscanf(source,"%u",&(dest->height))) == EOF)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we read in our integer */
    if(err != 1)
        return INVALID_IMAGE_FORMAT;

    /* Read in the width of the image */
    if((err = fscanf(source,"%hu",&dest->max_color_value)) == EOF)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we read in our integer */
    if(err != 1 || dest->max_color_value == 0)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Determine the width of a pixel */
    dest->pixel_width = 3 * (dest->max_color_value > 0xFF ? 2 : 1);

    dest->data.byte_ptr = (uint8_t*)malloc((size_t)(dest->height * dest->width * dest->pixel_width));

    /* Do some math to determine when data ends */
    pdata = dest->data.byte_ptr;
    final = pdata + dest->height * dest->width * dest->pixel_width;

    /* Read in our image data */
    while(pdata < final && fscanf(source,"%c",pdata++) != EOF);
    
    /* If we didn't read in all of the data we need then don't expect the image to be correct */
    if(pdata != final)
        return UNEXPECTED_END_OF_FILE;

    /* We successfully read our image */
    return SUCCESS;
}

/* Writes an image to the file provided. It is the user's responsibility to both
 * open and close the file used. */
uint32_t write_ppm_image(PPPMIMG source, PFILE dest)
{
    /* Loop math answers */
    uint32_t i;
    uint32_t len;

    /* If we don't have a destination then we have failed */
    if(!source)
        return NULL_POINTER;

    /* If we don't have a source then we have failed */
    if(!dest)
        return FILE_NOT_FOUND;

    /* Write the image header */
    fprintf(dest,"%c%c%c%hu%c%hu%c%hu%c",(source->magic_number & 0xFF00) >> 8,source->magic_number & 0xFF,
            ' ',source->width,' ',source->height,' ',source->max_color_value,'\n');

    /* Determine the length of the image to avoid extra multiplication */
    len = source->height * source->width * source->pixel_width;

    /* Write image data */
    for(i = 0;i < len;i++)
        fputc(source->data.byte_ptr[i],dest);

    /* Image successfully written */
    return SUCCESS;
}

#endif
