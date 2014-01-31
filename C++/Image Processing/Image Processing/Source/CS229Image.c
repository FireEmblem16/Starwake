/*
///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// CS229Image.c //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides functionality to interface with cs229 image files including:       ///
/// image operations, initialization, read in, write out and conversion.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _CS229IMAGE_C
#define _CS229IMAGE_C

#include "../Headers/CS229Image.h"

/* Returns a value that represents twice the size of a single pixel in
 * the image file supplied in bytes based on color mode and channel size.
 */
uint32_t determine_multiplier(PCSPIC);

/* Reads an image file into the first parameter from the supplied file pointer.
 * It is the user's responsibility to both open and close the file stream in it
 * is required. Any data already in the supplied in the image structure will
 * be overwritten without warning. If an error is returned then the supplied
 * file will be in an undefined location in the file.
 */
uint32_t read_cs229picture(PCSPIC dest, PFILE source)
{
    /* Create usefull local variables */
    uint8_t* pdata;
    uint8_t* final;
    uint32_t mul;

    /* If we don't have a destination then we have failed */
    if(!dest)
        return NULL_POINTER;
    
    /* If we don't have a source then we have failed */
    if(!source)
        return FILE_NOT_FOUND;

    /* Read in the magic number of this image */
    if(fscanf(source,"%c",&dest->magic_number) != 1)
        return UNEXPECTED_END_OF_FILE;

    /* If we didn't get a good magic number don't do anything else */
    if(dest->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Read in our first channel size */
    if(fscanf(source,"%c",&dest->first_channel.red_channel_size) != 1)
        return UNEXPECTED_END_OF_FILE;

    /* Read in our second channel size */
    if(fscanf(source,"%c",&dest->green_channel_size) != 1)
        return UNEXPECTED_END_OF_FILE;

    /* Read in our third channel size */
    if(fscanf(source,"%c",&dest->blue_channel_size) != 1)
        return UNEXPECTED_END_OF_FILE;

    /* Read in the color mode of this image */
    if(fscanf(source,"%c",&dest->color_mode) != 1)
        return UNEXPECTED_END_OF_FILE;

    /* Check that we have a valid color format */
    if(dest->color_mode != MODE_COLOR && dest->color_mode != MODE_BLACKWHITE)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Read in the image width */
    if(fscanf(source,"%c%c%c%c",&dest->width.bytes.byte1,&dest->width.bytes.byte2,&dest->width.bytes.byte3,&dest->width.bytes.byte4) != 4)
        return UNEXPECTED_END_OF_FILE;

    /* Read in the image height */
    if(fscanf(source,"%c%c%c%c",&dest->height.bytes.byte1,&dest->height.bytes.byte2,&dest->height.bytes.byte3,&dest->height.bytes.byte4) != 4)
        return UNEXPECTED_END_OF_FILE;

    /* Determine how much space a single pixel takes up */
    mul = determine_multiplier(dest);

    /* Check if we have valid channel sizes */
    if(mul == 0x0)
        return INVALID_CHANNEL_SIZE;

    /* Allocate space for the image */
    dest->data = (uint8_t*)malloc((size_t)(dest->height.value * dest->width.value * mul / 2 + (mul % 2 ? 1 : 0)));

    /* Do some math to determine when data ends */
    pdata = dest->data;
    final = pdata + dest->height.value * dest->width.value * mul / 2 + (mul % 2 ? 1 : 0);

    /* Read in our image data */
    while(pdata < final && fscanf(source,"%c",pdata++) != EOF);

    /* If we didn't read in all of the data we need then don't expect the image to be correct */
    if(pdata != final)
        return UNEXPECTED_END_OF_FILE;

    /* Successfully loaded image */
    return SUCCESS;
}

/* Writes an image to the file provided. It is the user's responsibility to both
 * open and close the file used. */
uint32_t write_cs229_image(PCSPIC pic, PFILE dest)
{
    /* Loop math answers */
    uint32_t i;
    uint32_t len;
    uint32_t mul;

    /* If we don't have a destination then we have failed */
    if(!dest)
        return NULL_POINTER;

    /* If we don't have a source then we have failed */
    if(!dest)
        return FILE_NOT_FOUND;

    /* Write the image header */
    fprintf(dest,"%c%c%c%c%c%c%c%c%c%c%c%c%c",pic->magic_number,pic->first_channel.red_channel_size,
            pic->green_channel_size,pic->blue_channel_size,pic->color_mode,
            pic->width.bytes.byte1,pic->width.bytes.byte2,pic->width.bytes.byte3,pic->width.bytes.byte4,
            pic->height.bytes.byte1,pic->height.bytes.byte2,pic->height.bytes.byte3,pic->height.bytes.byte4);

    /* Determine the length of the image to avoid extra multiplication */
    len = pic->height.value * pic->width.value;

    /* Determine how much data a single pixle takes up */
    mul = determine_multiplier(pic);

    /* Check if we have valid channel sizes */
    if(mul == 0x0)
        return INVALID_CHANNEL_SIZE;

    /* Determine how much data we need to write */
    len *= mul / 2 + (mul % 2 ? 1 : 0);

    /* Write image data */
    for(i = 0;i < len;i++)
        fputc(pic->data[i],dest);

    /* Image successfully written */
    return SUCCESS;
}

/* Returns a value that represents twice the size of a single pixel in
 * the image file supplied in bytes based on color mode and channel size.
 */
uint32_t determine_multiplier(PCSPIC dest)
{
    /* We will return this value */
    uint32_t mul;

    /* Check for NULL */
    if(!dest)
        return NULL_POINTER;

    if(dest->color_mode == MODE_BLACKWHITE)
    {
        /* Black and white images have only one channel */
        switch(dest->first_channel.blackwhite_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                mul = 0x1;
                break;
            case EIGHT_BIT_CHANNEL:
                mul = 0x2;
                break;
            case SIXTEEN_BIT_CHANNEL:
                mul = 0x4;
                break;
            default:
                return 0x0;
                break;
        }
    }
    else if(dest->color_mode == MODE_COLOR)
    {
        /* Color images have three channels */
        switch(dest->first_channel.red_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                mul = 0x1;
                break;
            case EIGHT_BIT_CHANNEL:
                mul = 0x2;
                break;
            case SIXTEEN_BIT_CHANNEL:
                mul = 0x4;
                break;
            default:
                return 0x0;
                break;
        }

        switch(dest->green_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                mul += 0x1;
                break;
            case EIGHT_BIT_CHANNEL:
                mul += 0x2;
                break;
            case SIXTEEN_BIT_CHANNEL:
                mul += 0x4;
                break;
            default:
                return 0x0;
                break;
        }

        switch(dest->blue_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                mul += 0x1;
                break;
            case EIGHT_BIT_CHANNEL:
                mul += 0x2;
                break;
            case SIXTEEN_BIT_CHANNEL:
                mul += 0x4;
                break;
            default:
                return 0x0;
                break;
        }
    }
    else
        return 0x0;

    /* Return twice the size of a single pixel */
    return mul;
}

/* Returns the pixel number or the coordinates given. Returns -1 if out of bounds.
 * Picture coordinates start at (0,0) at the top left of the picture.
 */
uint32_t coordinate_to_linear_address(PCSPIC pic, uint32_t x, uint32_t y)
{
    /* Bounds check */
    if(x < 0 || y < 0 || x >= pic->width.value || y >= pic->height.value)
        return -1;

    /* Determine the pixel number */
    return x + y * pic->width.value;
}

/* Returns the coordinates of the pixel given. Returns 1 if out of bounds
 * and 0 otherwise.
 */
uint32_t linear_to_coordinate_address(PCSPIC pic, uint32_t pixel, uint32_t* dest)
{
    /* Null check */
    if(pic == NULL || dest == NULL)
        return FAIL;

    /* Sanity check */
    if(pixel < 0 || pixel >= pic->width.value * pic->height.value)
        return FAIL;

    /* Calculate the pixel coordinates */
    dest[0] = pixel % pic->width.value;
    dest[1] = pixel / pic->width.value;

    /* We found the pixel */
    return SUCCESS;
}

/* Returns the next pixel in the supplied picture [pixel] pixels
 * into the picture in dest.
 */
uint32_t get_pixel(PCSPIC picture, uint32_t pixel_number, uint32_t* dest)
{
    /* Create some local variables */
    uint32_t mul;
    uint32_t offset;
    uint32_t half_in;
    uint32_t len;
    uint8_t* pixel;

    /* Check for NULL */
    if(!picture || !dest)
        return NULL_POINTER;

    /* Further sanity checks */
    if(picture->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Check our bounds */
    if(pixel_number < 0 || pixel_number > picture->width.value * picture->height.value)
        return ARGUMENT_OUT_OF_RANGE;

    /* Create the aray we will be returning */
    if(picture->color_mode == MODE_COLOR)
        len = 3;
    else if(picture->color_mode == MODE_BLACKWHITE)
        len = 1;
    else
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Determine twice the space a single pixel takes up */
    mul = determine_multiplier(picture);

    /* Check for image corruption */
    if(mul == 0)
        return INVALID_CHANNEL_SIZE;

    /* If mul is odd and we are looking for an even pixel then we have a pixel cluster
       that starts half way into a byte */
    if((mul & 0x1) && !(pixel_number & 0x1))
        half_in = TRUE;
    else
        half_in = FALSE;

    /* Calculate the offset of the pixel we are seeking */
    offset = mul * pixel_number / 2;

    /* Find the start of the pixel to collect */
    pixel = (uint8_t*)((uint32_t)picture->data + offset);

    /* If pixel is even and half_in is TRUE then we need to read starting from the
       second half of our first byte. If pixel is odd and half_in is TRUE then we need
       to read starting from the beginning of our first byte. Otherwise we can just read
       the appropriate number of bytes. */
    if(len == 1)
    {
        if(half_in)
        {
            if(picture->first_channel.blackwhite_channel_size == FOUR_BIT_CHANNEL)
                dest[0] = (uint32_t)*pixel & 0xF;
            else if(picture->first_channel.blackwhite_channel_size == EIGHT_BIT_CHANNEL)
                dest[0] = (uint32_t)(*(uint16_t*)pixel & 0x0FF0) >> 4;
            else
                dest[0] = (uint32_t)(*(uint16_t*)pixel & 0x0FFFF000) >> 12;
        }
        else
        {
            if(picture->first_channel.blackwhite_channel_size == FOUR_BIT_CHANNEL)
                dest[0] = (uint32_t)*pixel >> 4;
            else if(picture->first_channel.blackwhite_channel_size == EIGHT_BIT_CHANNEL)
                dest[0] = (uint32_t)*pixel;
            else
                dest[0] = (uint32_t)*(uint16_t*)pixel;
        }
    }
    else
    {
        if(picture->first_channel.red_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[0] = (uint32_t)*pixel & 0xF;
                half_in = FALSE;
                pixel++;
            }
            else
            {
                dest[0] = (uint32_t)(*pixel >> 4);
                half_in = TRUE;
            }
        }
        else if(picture->first_channel.red_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[0] = (uint32_t)((*(uint16_t*)pixel & 0x0FF0) >> 4);
                pixel++;
            }
            else
            {
                dest[0] = (uint32_t)*pixel;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                dest[0] = (uint32_t)((*(uint32_t*)pixel & 0x0FFFF000) >> 12);
                pixel += 2;
            }
            else
            {
                dest[0] = (uint32_t)*(uint16_t*)pixel;
                pixel += 2;
            }
        }

        if(picture->green_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[1] = (uint32_t)(*pixel & 0xF);
                half_in = FALSE;
                pixel++;
            }
            else
            {
                dest[1] = (uint32_t)(*pixel >> 4);
                half_in = TRUE;
            }
        }
        else if(picture->green_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[1] = (uint32_t)((*(uint16_t*)pixel & 0x0FF0) >> 4);
                pixel++;
            }
            else
            {
                dest[1] = (uint32_t)*pixel;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                dest[1] = (uint32_t)((*(uint32_t*)pixel & 0x0FFFF000) >> 12);
                pixel += 2;
            }
            else
            {
                dest[1] = (uint32_t)*(uint16_t*)pixel;
                pixel += 2;
            }
        }

        if(picture->blue_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[2] = (uint32_t)(*pixel & 0xF);
                half_in = FALSE;
                pixel++;
            }
            else
            {
                dest[2] = (uint32_t)(*pixel >> 4);
                half_in = TRUE;
            }
        }
        else if(picture->blue_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                dest[2] = (uint32_t)((*(uint16_t*)pixel & 0x0FF0) >> 4);
                pixel++;
            }
            else
            {
                dest[2] = (uint32_t)*pixel;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                dest[2] = (uint32_t)((*(uint32_t*)pixel & 0x0FFFF000) >> 12);
                pixel += 2;
            }
            else
            {
                dest[2] = (uint32_t)*(uint16_t*)pixel;
                pixel += 2;
            }
        }
    }

    /* Return */
    return SUCCESS;
}

/* Sets the pixel_numberth pixel to the value provided in color.
 */
uint32_t set_pixel(PCSPIC picture, uint32_t pixel_number, uint32_t* color)
{
    /* Create some local variables */
    uint32_t mul;
    uint32_t offset;
    uint32_t half_in;
    uint32_t len;
    uint8_t* pixel;

    /* Check for NULL */
    if(!picture || !color)
        return NULL_POINTER;

    /* Further sanity checks */
    if(picture->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Check our bounds */
    if(pixel_number < 0 || pixel_number > picture->width.value * picture->height.value)
        return ARGUMENT_OUT_OF_RANGE;

    /* Create the aray we will be returning */
    if(picture->color_mode == MODE_COLOR)
        len = 3;
    else if(picture->color_mode == MODE_BLACKWHITE)
        len = 1;
    else
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Determine twice the space a single pixel takes up */
    mul = determine_multiplier(picture);

    /* Check for image corruption */
    if(mul == 0)
        return INVALID_CHANNEL_SIZE;

    /* If mul is odd and we are looking for an even pixel then we have a pixel cluster
       that starts half way into a byte */
    if((mul & 0x1) && !(pixel_number & 0x1))
        half_in = TRUE;
    else
        half_in = FALSE;

    /* Calculate the offset of the pixel we are seeking */
    offset = mul * pixel_number / 2;

    /* Find the start of the pixel to collect */
    pixel = (uint8_t*)((uint32_t)picture->data + offset);

    /* If pixel is even and half_in is TRUE then we need to read starting from the
       second half of our first byte. If pixel is odd and half_in is TRUE then we need
       to read starting from the beginning of our first byte. Otherwise we can just read
       the appropriate number of bytes. */
    if(len == 1)
    {
        if(half_in)
        {
            if(picture->first_channel.blackwhite_channel_size == FOUR_BIT_CHANNEL)
            {
                /* Clean the part we want to write to */
                *pixel &= 0xF0;

                /* Set the pixel value */
                *pixel |= color[0] & 0x0F;
            }
            else if(picture->first_channel.blackwhite_channel_size == EIGHT_BIT_CHANNEL)
            {
                /* Clean the part we want to write to */
                *(uint16_t*)pixel &= 0xF00F;

                /* Set the pixel value */
                *pixel |= (color[0] & 0xFF) << 4;
            }
            else
            {
                /* Clean the part we want to write to */
                *(uint32_t*)pixel &= 0xF0000FFF;

                /* Set the pixel value */
                *pixel |= (color[0] & 0xFFFF) << 12;
            }
        }
        else
        {
            if(picture->first_channel.blackwhite_channel_size == FOUR_BIT_CHANNEL)
            {
                /* Clean the part we want to write to */
                *pixel &= 0x0F;

                /* Set the pixel value */
                *pixel |= (color[0] << 4) & 0xF0;
            }
            else if(picture->first_channel.blackwhite_channel_size == EIGHT_BIT_CHANNEL)
                *pixel = color[0] & 0xFF;
            else
                *(uint16_t*)pixel = color[0] & 0xFFFF;
        }
    }
    else
    {
        if(picture->first_channel.red_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *pixel &= 0xF0;

                /* Set the pixel value */
                *pixel |= color[0] & 0x0F;

                /* We are finished with this byte */
                half_in = FALSE;
                pixel++;
            }
            else
            {
                /* Clean the part we want to write to */
                *pixel &= 0x0F;

                /* Set the pixel value */
                *pixel |= (color[0] << 4) & 0xF0;

                /* We are now half way into a byte */
                half_in = TRUE;
            }
        }
        else if(picture->first_channel.red_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint16_t*)pixel &= 0xF00F;

                /* Set the pixel value */
                *pixel |= (color[0] & 0xFF) << 4;

                /* We are done with this byte and the first half of the next */
                pixel++;
            }
            else
            {
                /* Set the pixel value and go to the next byte */
                *pixel = color[0] & 0xFF;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint32_t*)pixel &= 0xF0000FFF;

                /* Set the pixel value */
                *pixel |= (color[0] & 0xFFFF) << 12;

                /* We are done with the next "two bytes" */
                pixel += 2;
            }
            else
            {
                /* Set the pixel value */
                *(uint16_t*)pixel = color[0] & 0xFFFF;

                /* We are done with the next two byes */
                pixel += 2;
            }
        }

        if(picture->green_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *pixel &= 0xF0;

                /* Set the pixel value */
                *pixel |= color[1] & 0x0F;

                /* We are finished with this byte */
                half_in = FALSE;
                pixel++;
            }
            else
            {
                /* Clean the part we want to write to */
                *pixel &= 0x0F;

                /* Set the pixel value */
                *pixel |= (color[1] << 4) & 0xF0;

                /* We are now half way into a byte */
                half_in = TRUE;
            }
        }
        else if(picture->green_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint16_t*)pixel &= 0xF00F;

                /* Set the pixel value */
                *pixel |= (color[1] & 0xFF) << 4;

                /* We are done with this byte and the first half of the next */
                pixel++;
            }
            else
            {
                /* Set the pixel value and go to the next byte */
                *pixel = color[1] & 0xFF;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint32_t*)pixel &= 0xF0000FFF;

                /* Set the pixel value */
                *pixel |= (color[1] & 0xFFFF) << 12;

                /* We are done with the next "two bytes" */
                pixel += 2;
            }
            else
            {
                /* Set the pixel value */
                *(uint16_t*)pixel = color[1] & 0xFFFF;

                /* We are done with the next two byes */
                pixel += 2;
            }
        }

        if(picture->blue_channel_size == FOUR_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *pixel &= 0xF0;

                /* Set the pixel value */
                *pixel |= color[2] & 0x0F;

                /* We are finished with this byte */
                half_in = FALSE;
                pixel++;
            }
            else
            {
                /* Clean the part we want to write to */
                *pixel &= 0x0F;

                /* Set the pixel value */
                *pixel |= (color[2] << 4) & 0xF0;

                /* We are now half way into a byte */
                half_in = TRUE;
            }
        }
        else if(picture->blue_channel_size == EIGHT_BIT_CHANNEL)
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint16_t*)pixel &= 0xF00F;

                /* Set the pixel value */
                *pixel |= (color[2] & 0xFF) << 4;

                /* We are done with this byte and the first half of the next */
                pixel++;
            }
            else
            {
                /* Set the pixel value and go to the next byte */
                *pixel = color[2] & 0xFF;
                pixel++;
            }
        }
        else
        {
            if(half_in)
            {
                /* Clean the part we want to write to */
                *(uint32_t*)pixel &= 0xF0000FFF;

                /* Set the pixel value */
                *pixel |= (color[2] & 0xFFFF) << 12;

                /* We are done with the next "two bytes" */
                pixel += 2;
            }
            else
            {
                /* Set the pixel value */
                *(uint16_t*)pixel = color[2] & 0xFFFF;

                /* We are done with the next two byes */
                pixel += 2;
            }
        }
    }

    /* Return */
    return SUCCESS;
}

/* Takes an array of colors of length one for black and white images and of
 * length three for color images and counts how many times that colored pixel
 * occurs in the supplied picture. Returns zero if the supplied picture is
 * invalid, null or otherwise unusable.
 */
uint32_t count_pixels_of_color(PCSPIC picture, uint32_t color[])
{
    /* Create some local variables */
    uint32_t i;
    uint32_t len;
    uint32_t count;
    uint32_t err;

    /* Check for NULL */
    if(!picture || !color)
        return NULL_POINTER;

    /* Further sanity checks */
    if(picture->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Error check */
    if(picture->color_mode != MODE_BLACKWHITE && picture->color_mode != MODE_COLOR)
        return 0;

    /* Initaialize variables */
    count = 0;
    len = picture->height.value * picture->width.value;

    /* Check if we were given a valid picture */
    if(!picture)
        return 0x0;

    /* Look through every pixel for the color desired */
    for(i = 0;i < len;i++)
    {
        /* Store our colors here */
        uint32_t colors[3];

        /* Get the color of the ith pixel */
        err = get_pixel(picture,i,colors);

        /* Error check */
        if(err)
            return err;

        /* Check how many number we need to match */
        if(picture->color_mode == MODE_BLACKWHITE)
        {
            /* Check if we have the same black and white color */
            if(colors[0] == color[0])
                count++;
        }
        else
        {
            /* Check if we have the same rbg color */
            if(colors[0] == color[0] && colors[1] == color[1] && colors[2] == color[2])
                count++;
        }
    }

    /* Return how many times the requested pixel was found */
    return count;
}

/* Darkens the image provided by the given percentage (decimal value times 100).
 * If a negative percentage is given it will istead lighten the image.
 */
uint32_t darken_cs229_image(PCSPIC picture, int32_t percent)
{
    /* Create some local variables */
    uint32_t color[3];
    uint32_t i;
    uint32_t len;
    uint32_t err;

    /* If we have a negative percentage lighten instead */
    if(percent < 0)
        return lighten_cs229_image(picture,-percent);

    /* Check for NULL */
    if(picture == NULL)
        return NULL_POINTER;

    /* Further sanity checks */
    if(picture->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Error check */
    if(picture->color_mode != MODE_BLACKWHITE && picture->color_mode != MODE_COLOR)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Just so we don't waste time */
    if(percent == 0)
        return SUCCESS;

    /* Calculate how many pixels exist in this image */
    len = picture->height.value * picture->width.value;

    /* Darken every pixel */
    for(i = 0;i < len;i++)
    {
        /* Get our ith pixel */
        err = get_pixel(picture,i,color);

        /* If we go an error then abandon the program */
        if(err)
            return err;

        /* To speed up function execution by almost three times we will
           divide the job based on color mode instead of bashing forward */
        if(picture->color_mode == MODE_BLACKWHITE)
        {
            /* Black has a convientent value of zero */
            color[0] -= color[0] * percent / 100;
        }
        else
        {
            /* Black has a convientent value of zero */
            color[0] -= color[0] * percent / 100;
            color[1] -= color[1] * percent / 100;
            color[2] -= color[2] * percent / 100;
        }

        /* Set the ith pixel */
        err = set_pixel(picture,i,color);

        /* If we go an error then abandon the program */
        if(err)
            return err;
    }

    /* Image successfully darkened */
    return SUCCESS;
}

/* Lights the image provided by the given percentage (decimal value times 100).
 * If a negative percentage is given it will istead darken the image.
 */
uint32_t lighten_cs229_image(PCSPIC picture, int32_t percent)
{
    /* Create some local variables */
    uint32_t color[3];
    uint32_t i;
    uint32_t len;
    uint32_t err;

    /* The maximum value of our channels */
    uint32_t max_val[3];

    /* Make sure warnings don't become errors upon submission */
    max_val[0] = 0;
    max_val[1] = 0;
    max_val[2] = 0;

    /* If we have a negative percentage darken instead */
    if(percent < 0)
        return darken_cs229_image(picture,-percent);

    /* Check for NULL */
    if(picture == NULL)
        return NULL_POINTER;

    /* Further sanity checks */
    if(picture->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Error check */
    if(picture->color_mode != MODE_BLACKWHITE && picture->color_mode != MODE_COLOR)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Just so we don't waste time */
    if(percent == 0)
        return SUCCESS;

    /* Determine what is white, set_pixel clamps our data automatically but our
       math requires the acutal maximum value to function unfotunately. */
    if(picture->color_mode == MODE_BLACKWHITE)
    {
        /* Determine the maximum value of our black channel */
        switch(picture->first_channel.blackwhite_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                max_val[0] = 0xF;
                break;
            case EIGHT_BIT_CHANNEL:
                max_val[0] = 0xFF;
                break;
            case SIXTEEN_BIT_CHANNEL:
                max_val[0] = 0xFFFF;
                break;
            default:
                return INVALID_CHANNEL_SIZE;
        }
    }
    else
    {
        /* Determine the maximum value of our red channel */
        switch(picture->first_channel.red_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                max_val[0] = 0xF;
                break;
            case EIGHT_BIT_CHANNEL:
                max_val[0] = 0xFF;
                break;
            case SIXTEEN_BIT_CHANNEL:
                max_val[0] = 0xFFFF;
                break;
            default:
                return INVALID_CHANNEL_SIZE;
        }

        /* Determine the maximum value of our green channel */
        switch(picture->green_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                max_val[1] = 0xF;
                break;
            case EIGHT_BIT_CHANNEL:
                max_val[1] = 0xFF;
                break;
            case SIXTEEN_BIT_CHANNEL:
                max_val[1] = 0xFFFF;
                break;
            default:
                return INVALID_CHANNEL_SIZE;
        }

        /* Determine the maximum value of our blue channel */
        switch(picture->blue_channel_size)
        {
            case FOUR_BIT_CHANNEL:
                max_val[2] = 0xF;
                break;
            case EIGHT_BIT_CHANNEL:
                max_val[2] = 0xFF;
                break;
            case SIXTEEN_BIT_CHANNEL:
                max_val[2] = 0xFFFF;
                break;
            default:
                return INVALID_CHANNEL_SIZE;
        }
    }

    /* Calculate how many pixels exist in this image */
    len = picture->height.value * picture->width.value;

    /* Darken every pixel */
    for(i = 0;i < len;i++)
    {
        /* Get our ith pixel */
        err = get_pixel(picture,i,color);

        /* If we go an error then abandon the program */
        if(err)
            return err;

        /* Different channels may have different white values */
        if(picture->color_mode == MODE_BLACKWHITE)
        {
            color[0] += (max_val[0] - color[0]) * percent / 100;
        }
        else
        {
            color[0] += (max_val[0] - color[0]) * percent / 100;
            color[1] += (max_val[1] - color[1]) * percent / 100;
            color[2] += (max_val[2] - color[2]) * percent / 100;
        }

        /* Set the ith pixel */
        err = set_pixel(picture,i,color);

        /* If we go an error then abandon the program */
        if(err)
            return err;
    }

    /* Image successfully lightened */
    return SUCCESS;
}

/* Rotates the image by the value given. The only acceptable values are
 * multiples of ninety (degrees). Radians will not produce expected results.
 */
uint32_t rotate_cs229_image(PCSPIC pic, int32_t angle)
{
    /* Create some local variables */
    CSPIC newpic;
    uint32_t err;

    /* Image successfully rotated */
    if(!angle)
        return SUCCESS;

    /* Check that we have a valid rotation angle */
    if(angle % 90)
        return INVALID_ARGUMENT;

    /* Check for a null pointer */
    if(pic == NULL)
        return NULL_POINTER;

    /* Further sanity checks */
    if(pic->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Make the angle sane */
    while(angle < 0)
        angle += 360;

    /* Make the angle sane */
    while(angle >= 360)
        angle -= 360;

    /* Set values for our new picture */
    newpic.first_channel.red_channel_size = pic->first_channel.red_channel_size;
    newpic.green_channel_size = pic->green_channel_size;
    newpic.blue_channel_size = pic->blue_channel_size;
    newpic.color_mode = pic->color_mode;
    newpic.height.value = pic->width.value;
    newpic.width.value = pic->height.value;
    newpic.magic_number = pic->magic_number;

    /* Determine the color format multipler */
    err = determine_multiplier(&newpic);

    /* Allocate data space for our new image */
    if(err)
        newpic.data = (uint8_t*)malloc((size_t)(newpic.width.value * newpic.height.value * err / 2 + (err % 2 ? 1 : 0)));
    else
        return INVALID_CHANNEL_SIZE;

    /* Do the rotation */
    {
        /* Create a place to store our pixel values */
        uint32_t color[3];
        uint32_t len;
        uint32_t i;
        uint32_t pos;

        /* Determine the length of our image */
        len = newpic.width.value * newpic.height.value;

        /* For each pixel move it to its new location */
        for(i = 1;i <= len;i++)
        {
            /* Figure out where we are getting our pixel from */
            if(i % pic->height.value)
                pos = pic->width.value * (pic->height.value - (i % pic->height.value)) + (i / pic->height.value);
            else
                pos = i / pic->height.value - 1;

            /* Get the next pixel */
            err = get_pixel(pic,pos,color);
            
            /* Check that we successfully got the pixel value */
            if(err)
            {
                free((void*)newpic.data);
                return err;
            }

            /* Set the next pixel in our image, zero based index so subtract one */
            err = set_pixel(&newpic,i - 1,color);
            
            /* Check that we successfully set the pixel value */
            if(err)
            {
                free((void*)newpic.data);
                return err;
            }
        }
    }

    /* Rotate the image more */
    err = rotate_cs229_image(&newpic,angle - 90);

    /* If we didn't get an error we should change our image data */
    if(err)
        free((void*)newpic.data);
    else
    {
        /* Change the image specs */
        pic->width.value = newpic.width.value;
        pic->height.value = newpic.height.value;

        /* Free our old data space */
        free((void*)pic->data);

        /* Change our picture data location */
        pic->data = newpic.data;
    }

    /* Image sucessfully rotated */
    return err;
}

/* Flips the provided image either horizontally or vertically based on
 * the char passed to this function. h for a horizontal flip and v
 * for a vertical flip.
 */
uint32_t flip_cs229_image(PCSPIC pic, int8_t axis)
{
    /* Create some local variables */
    uint32_t err;
    uint32_t i;
    uint32_t len;
    uint32_t colorA[3];
    uint32_t colorB[3];
    uint32_t mod;

    /* Check that axis is valid */
    if(!(axis == 'h'  || axis == 'v'))
        return INVALID_ARGUMENT;

    /* Sanity check */
    if(pic == NULL)
        return NULL_POINTER;

    /* Further sanity checks */
    if(pic->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Determine the length of the image */
    len = pic->width.value * pic->height.value;
    
    /* Do the actual transform */
    if(axis == 'v')
        for(i = 0;i < len;i++)
        {
            /* Determine where we are in the picture width-wise */
            mod = i % pic->width.value;

            /* Check if we are past or at the halfway point */
            if(mod >= pic->width.value / 2)
                continue;

            /* Determine the paired pixel */
            mod = (i - mod) + (pic->width.value - mod - 1);
            
            /* Get the left pixel */
            err = get_pixel(pic,i,colorA);

            /* Error check */
            if(err)
                return err;

            /* Get the right pixel */
            err = get_pixel(pic,mod,colorB);

            /* Error check */
            if(err)
                return err;

            /* Set the new left pixel */
            err = set_pixel(pic,i,colorB);

            /* Error check */
            if(err)
                return err;

            /* Set the new right pixel */
            err = set_pixel(pic,mod,colorA);

            /* Error check */
            if(err)
                return err;
        }
    else
        for(i = 0;i / pic->width.value < pic->height.value / 2;i++)
        {
            /* Determine where we are in the picture hieght-wise */
            mod = i / pic->width.value + 1;

            /* Determine where the opposite pixel is */
            mod = len + (i % pic->width.value) - (mod * pic->width.value);

            /* Get the left pixel */
            err = get_pixel(pic,i,colorA);

            /* Error check */
            if(err)
                return err;

            /* Get the right pixel */
            err = get_pixel(pic,mod,colorB);

            /* Error check */
            if(err)
                return err;

            /* Set the new left pixel */
            err = set_pixel(pic,i,colorB);

            /* Error check */
            if(err)
                return err;

            /* Set the new right pixel */
            err = set_pixel(pic,mod,colorA);

            /* Error check */
            if(err)
                return err;
        }
    
    /* Image sucessfully flipped */
    return SUCCESS;
}

/* Takes a kernel as an array containing the values to use in the convolution
 * process and an unsigned integer pointer indicating the width and height of the
 * array for each kernel. This implies that the kernel must be a square. Odd square sizes
 * produce the most predictable results though even sizes result in consistent pictures.
 */
uint32_t convolute(PCSPIC pic, float** kernel, uint32_t* width)
{
    /* Create some local variables */
    CSPIC newpic;
    uint32_t err;
    uint32_t len;
    uint32_t i;
    uint32_t j;
    uint32_t x;
    uint32_t y;
    uint32_t cord[2];
    float pixel;
    uint32_t int_pixel[3];
    uint32_t new_i;

    /* Sanity check */
    if(pic == NULL || kernel == NULL)
        return NULL_POINTER;

    /* Further sanity checks */
    if(pic->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* If our kernel is of size zero then we don't need to do anything. */
    if(!width)
        return SUCCESS;

    /* Set values for our new picture */
    newpic.first_channel.red_channel_size = pic->first_channel.red_channel_size;
    newpic.green_channel_size = pic->green_channel_size;
    newpic.blue_channel_size = pic->blue_channel_size;
    newpic.color_mode = pic->color_mode;
    newpic.width.value = pic->width.value;
    newpic.height.value = pic->height.value;
    newpic.magic_number = pic->magic_number;

    /* Determine the color format multipler */
    err = determine_multiplier(&newpic);

    /* Allocate data space for our new image */
    if(err)
        newpic.data = (uint8_t*)malloc((size_t)(newpic.width.value * newpic.height.value * err / 2 + (err % 2 ? 1 : 0)));
    else
        return INVALID_CHANNEL_SIZE;

    /* Determine the length of the picture */
    len = newpic.width.value * newpic.height.value;

    /* Loop through each pixel to create the new data */
    for(i = 0;i < len;i++)
    {
        /* Get the newest pixel in the old picture */
        err = get_pixel(pic,i,int_pixel);

        /* Error check */
        if(err)
        {
            free((void*)newpic.data);
            return err;
        }

        /* Set the newest pixel in the new picture */
        err = set_pixel(&newpic,i,int_pixel);

        /* Error check */
        if(err)
        {
            free((void*)newpic.data);
            return err;
        }

        /* Loop through each kernel */
        for(j = 0;j < (pic->color_mode == MODE_COLOR ? 3 : 1);j++)
        {
            /* Empty the data out of our floating pixel */
            pixel = 0.0f;

            for(x = - (width[j] / 2);x < (width[j] + 1) / 2;x++)
                for(y = - (width[j] / 2);y < (width[j] + 1) / 2;y++)
                {
                    /* Get the coordinates of the current pixel */
                    if(linear_to_coordinate_address(pic,i,cord))
                    {
                        free((void*)newpic.data);
                        return GENERAL_ERROR;
                    }

                    /* Get the linear address of the new pixel */
                    new_i = coordinate_to_linear_address(pic,cord[0] + x,cord[1] + y);

                    /* If we are out of bounds then no matter what we do we get zero */
                    if(new_i == -1)
                        continue;

                    /* Get the next pixel to add */
                    err = get_pixel(&newpic,new_i,int_pixel);

                    /* Error check */
                    if(err)
                    {
                        free((void*)newpic.data);
                        return err;
                    }

                    /* Convolute the pixel data with the appropriate value */
                    pixel += ((float)int_pixel[j]) * kernel[j][(x + width[j] / 2) + (y + width[j] / 2) * width[j]];
                }

            /* Get the final value of the pixel */
            int_pixel[j] = (uint32_t)roundf(pixel);

            /* Set the new value of the pixel */
            err = set_pixel(&newpic,i,int_pixel);

            /* Error check */
            if(err)
            {
                free((void*)newpic.data);
                return err;
            }
        }
    }

    /* Free our old data */
    free((void*)pic->data);

    /* Change our image data pointer */
    pic->data = newpic.data;

    /* Image successfully convoluted */
    return SUCCESS;
}

/* Converts a cs229 image to a ppm image and stores it in the PPMING provided.
 */
uint32_t convert_cs229_to_ppm(PCSPIC source, PPPMIMG dest)
{
    /* Check if we have null pointers */
    if(!source || !dest)
        return NULL_POINTER;
    
    /* We only convert color images */
    if(source->color_mode != MODE_COLOR)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* We only convert images with eight and sixteen bit channels */
    if(source->first_channel.red_channel_size == FOUR_BIT_CHANNEL)
        return INVALID_CHANNEL_SIZE;

    /* We only convert images with eight and sixteen bit channels */
    if(source->green_channel_size == FOUR_BIT_CHANNEL)
        return INVALID_CHANNEL_SIZE;

    /* We only convert images with eight and sixteen bit channels */
    if(source->blue_channel_size == FOUR_BIT_CHANNEL)
        return INVALID_CHANNEL_SIZE;

    /* We only convert images with equal channel sizes */
    if(source->first_channel.red_channel_size != source->green_channel_size)
        return INVALID_CHANNEL_SIZE;

    /* We only convert images with equal channel sizes */
    if(source->blue_channel_size != source->green_channel_size)
        return INVALID_CHANNEL_SIZE;

    /* Set our magic number */
    dest->magic_number = PPM_MAGIC_NUMBER;

    /* The width of a pixel is three times the number of bytes a single channel uses */
    dest->pixel_width = source->blue_channel_size / 2 * 3;

    /* Define our maximum color value */
    dest->max_color_value = dest->pixel_width == 3 ? 0xFF : 0xFFFF;

    /* Define our dimensions */
    dest->height = source->height.value;
    dest->width = source->width.value;

    /* Allocate data for our new image format */
    dest->data.byte_ptr = (uint8_t*)malloc((size_t)(dest->height * dest->width * dest->pixel_width));

    /* Since data is stored indentically in both image formats just copy it directly. */
    memcpy((void*)dest->data.byte_ptr,(const void*)source->data,(size_t)(dest->height * dest->width * dest->pixel_width));

    /* We successfully converted the image */
    return SUCCESS;
}

/* Converts a ppm image to a cs229 image and stores it in the PCSPIC provided.
 */
extern uint32_t convert_ppm_to_cs229(PPPMIMG source, PCSPIC dest)
{
    /* Check if we have null pointers */
    if(!source || !dest)
        return NULL_POINTER;

    /* Write our magic number */
    dest->magic_number = CS229_MAGIC_NUMBER;

    /* We only write color images */
    dest->color_mode = MODE_COLOR;

    /* If pixels are three bytes long we have eight byte channels */
    if(source->pixel_width == 3)
    {
        dest->first_channel.red_channel_size = EIGHT_BIT_CHANNEL;
        dest->green_channel_size = EIGHT_BIT_CHANNEL;
        dest->blue_channel_size = EIGHT_BIT_CHANNEL;
    }
    else /* We must have sixteen byte channels */
    {
        dest->first_channel.red_channel_size = SIXTEEN_BIT_CHANNEL;
        dest->green_channel_size = SIXTEEN_BIT_CHANNEL;
        dest->blue_channel_size = SIXTEEN_BIT_CHANNEL;
    }

    /* Define our image dimensions */
    dest->height.value = source->height;
    dest->width.value = source->width;

    /* Allocate data for our new image format */
    dest->data = (uint8_t*)malloc((size_t)(dest->height.value * dest->width.value * source->pixel_width));

    /* Since data is stored indentically in both image formats just copy it directly. */
    memcpy((void*)dest->data,(const void*)source->data.byte_ptr,(size_t)(dest->height.value * dest->width.value * source->pixel_width));

    /* We successfully converted the image */
    return SUCCESS;
}

/* Computes the error of pic1 and pic2 at (x,y) as specified by the stitch_image
 * algorithm. If x or y is out of range then -1.0f is returned. (1,1) is the
 * first pixel. (0,0) is out of range. No error checking is performed on
 * pic1 or pic2. They are assumed to be of the same color format and channel
 * sizes in order to function correctly. Also returns -1.0f if there is not
 * at least ten percent of the smaller picture in the overlap. o_w and o_w
 * will store the value of the overlap size if null pointers aren't pasted as
 * thier value. If they recieve null pointers they are not used.
 */
float check_average_error(PCSPIC pic1, PCSPIC pic2, uint32_t x, uint32_t y, uint32_t* o_w, uint32_t* o_h)
{
    /* Create some local variables */
    uint32_t size;
    uint32_t pic;
    uint32_t over_left;
    uint32_t over_top;
    int32_t overlap_width;
    int32_t overlap_height;
    uint32_t overlap;
    uint32_t xi;
    uint32_t yi;
    long double error;

    /* Check which image is smaller */
    if(pic1->width.value * pic1->height.value < pic2->width.value * pic2->height.value)
        size = pic2->width.value * pic2->height.value;
    else
        size = pic1->width.value * pic1->height.value;

    /* Check if picture two is wider than picutre one */
    if(pic1->width.value < pic2->width.value)
        pic = 0;
    else
        pic = 1;

    /* Determine our overlap width */
    if(pic)
    {
        if(x > pic2->width.value)
        {
            if(x > pic1->width.value)
                overlap_width = pic2->width.value + pic1->width.value - x;
            else
                overlap_width = pic2->width.value;

            over_left = FALSE;
        }
        else
        {
            overlap_width = x;
            over_left = TRUE;
        }
    }
    else
    {
        if(x > pic1->width.value)
        {
            if(x > pic2->width.value)
                overlap_width = pic1->width.value + pic2->width.value - x;
            else
                overlap_width = pic1->width.value;

            over_left = FALSE;
        }
        else
        {
            overlap_width = x;
            over_left = TRUE;
        }
    }

    /* Check if picture two is taller than picutre one */
    if(pic1->height.value < pic2->height.value)
        pic = 0;
    else
        pic = 1;

    /* Determine our overlap height */
    if(pic)
    {
        if(y > pic2->height.value)
        {
            if(y > pic1->height.value)
                overlap_height = pic2->height.value + pic1->height.value - y;
            else
                overlap_height = pic2->height.value;

            over_top = FALSE;
        }
        else
        {
            overlap_height = y;
            over_top = TRUE;
        }
    }
    else
    {
        if(y > pic1->height.value)
        {
            if(y > pic2->height.value)
                overlap_height = pic1->height.value + pic2->height.value - y;
            else
                overlap_height = pic1->height.value;

            over_top = FALSE;
        }
        else
        {
            overlap_height = y;
            over_top = TRUE;
        }
    }

    /* Check if we are in range */
    if(overlap_width <= 0 || overlap_height <= 0)
        return -1.0f;

    /* Determine how big our overlap is */
    overlap = overlap_height * overlap_width;

    /* Save some data if we don't have null pointers */
    if(o_w && o_h)
    {
        *o_w = overlap_width;
        *o_h = overlap_height;
    }

    /* Check if we are overlapping at least ten percent of the smaller image */
    if(((float)overlap)/((float)size) < 0.1f)
        return -1.0f;

    /* Initialize error */
    error = 0.0;

    /* Get the error at each overlap position */
    for(xi = (over_left ? 0 : x - 1);xi < (over_left ? overlap_width : x + overlap_width);xi++)
        for(yi = (over_top ? 0 : y - 1);yi < (over_top ? overlap_height : y + overlap_height);yi++)
        {
            /* Create some local variables */
            uint32_t color1[3];
            uint32_t color2[3];
            uint32_t x_pos1;
            uint32_t y_pos1;
            uint32_t x_pos2;
            uint32_t y_pos2;

            /* Determine the (x,y) value of the first picture */
            x_pos1 = over_left ? pic1->width.value - xi : xi - x + 1;
            y_pos1 = over_top ? pic1->height.value - yi : yi - y + 1;

            /* Determine the (x,y) value of the second picture */
            x_pos2 = xi;
            y_pos2 = yi;

            /* Get the color of the current pixels */
            get_pixel(pic1,x_pos1 + y_pos1 * pic1->width.value,color1);
            get_pixel(pic2,x_pos2 + y_pos2 * pic2->width.value,color2);

            /* Compute the additional error */
            if(pic1->color_mode == MODE_BLACKWHITE)
                error += (color1[0] - color2[0]) * (color1[0] - color2[0]);
            else
                error += (color1[0] - color2[0]) * (color1[0] - color2[0]) +
                         (color1[1] - color2[1]) * (color1[1] - color2[1]) +
                         (color1[2] - color2[2]) * (color1[2] - color2[2]);
        }
    
    /* Return the average error of each pixel */
    return (float)(error / ((long double)overlap));
}

/* Stitches together two images. Both images must have all channel sizes 8.
 * If images of zero size are provided then the stitch will simply not be
 * preformed as it is pointless. If there are equally valid locations for the
 * two images to be stitched together then the frist one encountered will be
 * used. Stitching is evaluated from top to bottom, left to right such that the
 * first overlap has the bottom-right pixel of the first image on top of the
 * top-left pixel of the second image. Empty pixels will be filled with white.
 */
uint32_t stitch_images(PCSPIC pic1, PCSPIC pic2, PCSPIC dest)
{
    /* Create some local variables */
    uint32_t x;
    uint32_t y;
    uint32_t best_x;
    uint32_t best_y;
    uint32_t overlap_width;
    uint32_t overlap_height;
    float best_error;

    /* Make sure warnings don't become errors upon submission */
    overlap_width = 0;
    overlap_height = 0;
    best_x = 0;
    best_y = 0;

    /* Check for null pointers */
    if(!pic1 || !pic2 || !dest)
        return NULL_POINTER;

    /* Check the magic number */
    if(pic1->magic_number != CS229_MAGIC_NUMBER || pic2->magic_number != CS229_MAGIC_NUMBER)
        return INVALID_IMAGE_FORMAT;

    /* Check that we have the same color mode */
    if(pic1->color_mode != pic2->color_mode)
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Check that we have non-zero image dimensions */
    if(!pic1->height.value || !pic1->width.value || !pic2->height.value || !pic2->width.value)
        return SUCCESS;

    /* Check the channel sizes of the pictures are all eight bits */
    if(pic1->color_mode == MODE_BLACKWHITE)
    {
        if(pic1->first_channel.blackwhite_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
    }
    else if(pic1->color_mode == MODE_COLOR)
    {
        if(pic1->first_channel.red_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
        if(pic1->green_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
        if(pic1->blue_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
    }
    else
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Check the channel sizes of the pictures are all eight bits */
    if(pic2->color_mode == MODE_BLACKWHITE)
    {
        if(pic2->first_channel.blackwhite_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
    }
    else if(pic2->color_mode == MODE_COLOR)
    {
        if(pic2->first_channel.red_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
        if(pic2->green_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
        if(pic2->blue_channel_size != EIGHT_BIT_CHANNEL)
            return INVALID_CHANNEL_SIZE;
    }
    else
        return INVALID_IMAGE_COLOR_FORMAT;

    /* Initialize the destination picture */
    dest->magic_number = pic1->magic_number;
    dest->color_mode = pic1->color_mode;
    dest->first_channel.blackwhite_channel_size = EIGHT_BIT_CHANNEL;
    dest->green_channel_size = EIGHT_BIT_CHANNEL;
    dest->blue_channel_size = EIGHT_BIT_CHANNEL;

    /* Initialize our stitch best_error value */
    best_error = -1.0f;

    /* Check the overlap error of the pictures */
    for(x = 1;x < 2 * pic2->width.value;x++)
        for(y = 1;y < 2 * pic2->height.value;y++)
        {
            uint32_t o_w[1];
            uint32_t o_h[1];

            /* Determine the error of our current overlap */
            float err = check_average_error(pic1,pic2,x,y,o_w,o_h);

            /* Check that we have a valid overlap */
            if(err < 0.0f)
                continue;

            /* Check if we need to update our statistics */
            if(err < best_error || best_error < 0.0f)
            {
                /* Update overlap statistics */
                best_error = err;
                best_x = x;
                best_y = y;
                overlap_width = o_w[0];
                overlap_height = o_h[0];
            }
        }

    /* Check if we don't have a valid overlap */
    if(best_error < 0.0f)
        return GENERAL_ERROR;

    /* Determine our new picture size */
    dest->width.value = pic1->width.value + pic2->width.value - overlap_width;
    dest->height.value = pic1->height.value + pic2->height.value - overlap_height;

    /* Make a new block so we can get some local variables that aren't a mile away */
    {
        /* Local variables */
        uint32_t len;
        uint32_t i;

        /* Determine how big our picture is */
        len = dest->width.value * dest->height.value * (dest->color_mode == MODE_BLACKWHITE ? 1 : 3);

        /* Initialize our new picture data to white */
        dest->data = (uint8_t*)malloc((size_t)len);

        /* Set all values to white */
        for(i = 0;i < len;i++)
            dest->data[i] = 0xFF;
    }

    /* Make a new block so we can get some local variables that aren't a mile away */
    {
        /* Local variables */
        uint32_t i;
        uint32_t j;

        /* Copy our first image into the new image */
        for(i = 0;i < pic1->width.value;i++)
            for(j = 0;j < pic1->width.value;j++)
                dest->data[i + j * dest->width.value] = pic1->data[i + j * pic1->width.value];

        /* Copy our second image into the new image averaging values if necessary */
        for(i = best_x;i < best_x + pic2->width.value;i++)
            for(j = best_y;j < best_y + pic2->height.value;j++)
                if(i < pic1->width.value && j < pic1->height.value) /* If we are in our overlap range average the values */
                    dest->data[i + j * dest->width.value] = (dest->data[i + j * dest->width.value] + pic2->data[(i - best_x) + (j - best_y) * pic2->width.value]) / 2;
                else /* We aren't in the overlap range so just copy the value directly */
                    dest->data[i + j * dest->width.value] = pic2->data[(i - best_x) + (j - best_y) * pic2->width.value];
    }

    return SUCCESS;
}

#endif
