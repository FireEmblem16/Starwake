/*
///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// CS229Image.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides functionality to interface with cs229 image files including:       ///
/// image operations, initialization, read in, write out and conversion.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _CS229IMAGE_H
#define _CS229IMAGE_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "Error.h"
#include "Defines.h"
#include "PPMImage.h"

/* cs229 images have 0x42 as their magic number */
#define CS229_MAGIC_NUMBER 0x42

/* cs229 images stores channel sizes as these numbers */
#define FOUR_BIT_CHANNEL 0x1
#define EIGHT_BIT_CHANNEL 0x2
#define SIXTEEN_BIT_CHANNEL 0x4

/* cs229 images use these values to indicate color mode */
#define MODE_BLACKWHITE 0x0
#define MODE_COLOR 0xFF

/* cs229 images store image size in reverse order so create a convient struct */
typedef union cs229dimension
{
    struct
    {
        uint8_t byte1;
        uint8_t byte2;
        uint8_t byte3;
        uint8_t byte4;
    } bytes;

    uint32_t value;
} CSDIM, *PCSDIM;

/* Represent the header of a cs229 image using this struct */
typedef struct cs229image
{
    uint8_t magic_number;

    uint8_t color_mode;

    union
    {
        uint8_t red_channel_size;
        uint8_t blackwhite_channel_size;
    } first_channel;
    uint8_t green_channel_size;
    uint8_t blue_channel_size;

    CSDIM height;
    CSDIM width;

    uint8_t* data;
} CSPIC, *PCSPIC;

/* Reads an image file into the first parameter from the supplied file pointer.
 * It is the user's responsibility to both open and close the file stream in it
 * is required. Any data already in the supplied in the image structure will
 * be overwritten without warning. If an error is returned then the supplied
 * file will be in an undefined location in the file.
 */
extern uint32_t read_cs229picture(PCSPIC,PFILE);

/* Writes an image to the file provided. It is the user's responsibility to both
 * open and close the file used. */
extern uint32_t write_cs229_image(PCSPIC,PFILE);

/* Returns the pixel number or the coordinates given. Returns -1 if out of bounds.
 * Picture coordinates start at (0,0) at the top left of the picture.
 */
extern uint32_t coordinate_to_linear_address(PCSPIC,uint32_t,uint32_t);

/* Returns the coordinates of the pixel given. Returns 1 if out of bounds
 * and 0 otherwise.
 */
extern uint32_t linear_to_coordinate_address(PCSPIC,uint32_t,uint32_t*);

/* Returns the next pixel in the supplied picture [pixel] pixels
 * into the picture in dest.
 */
extern uint32_t get_pixel(PCSPIC,uint32_t,uint32_t*);

/* Takes an array of colors of length one for black and white images and of
 * length three for color images and counts how many times that colored pixel
 * occurs in the supplied picture. Returns zero if the supplied picture is
 * invalid, null or otherwise unusable.
 */
extern uint32_t count_pixels_of_color(PCSPIC,uint32_t[]);

/* Darkens the image provided by the given percentage (decimal value times 100).
 * If a negative percentage is given it will istead lighten the image.
 */
extern uint32_t darken_cs229_image(PCSPIC,int32_t);

/* Lights the image provided by the given percentage (decimal value times 100).
 * If a negative percentage is given it will istead darken the image.
 */
extern uint32_t lighten_cs229_image(PCSPIC,int32_t);

/* Rotates the image by the value given. The only acceptable values are
 * multiples of ninety (degrees). Radians will not produce expected results.
 */
extern uint32_t rotate_cs229_image(PCSPIC,int32_t);

/* Flips the provided image either horizontally or vertically based on
 * the char passed to this function. h for a horizontal flip and v
 * for a vertical flip.
 */
extern uint32_t flip_cs229_image(PCSPIC,int8_t);

/* Takes a kernel as an array containing the values to use in the convolution
 * process and an unsigned integer pointer indicating the width and height of the
 * array for each kernel. This implies that the kernel must be a square. Odd square sizes
 * produce the most predictable results though even sizes result in consistent pictures.
 */
extern uint32_t convolute(PCSPIC,float**,uint32_t*);

/* Converts a cs229 image to a ppm image and stores it in the PPMING provided.
 */
extern uint32_t convert_cs229_to_ppm(PCSPIC,PPPMIMG);

/* Converts a ppm image to a cs229 image and stores it in the PCSPIC provided.
 */
extern uint32_t convert_ppm_to_cs229(PPPMIMG,PCSPIC);

/* Stitches together two images. Both images must have all channel sizes 8.
 * If images of zero size are provided then the stitch will simply not be
 * preformed as it is pointless. If there are equally valid locations for the
 * two images to be stitched together then the frist one encountered will be
 * used. Stitching is evaluated from top to bottom, left to right such that the
 * first overlap has the bottom-right pixel of the first image on top of the
 * top-left pixel of the second image. Empty pixels will be filled with white.
 */
extern uint32_t stitch_images(PCSPIC,PCSPIC,PCSPIC);

#endif
