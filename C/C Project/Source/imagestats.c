/*
///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// imagestats.c ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a cs229 image and produces statistics that are feed to stdout.        ///
/// If a file path (relative or absolute) is supplied, this program will read   ///
/// from that. Otherwise it will expect the image to be supplied from stdin.    ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _IMAGESTATS_C
#define _IMAGESTATS_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See start of imagestats.c file.
 */
int main(int argc, char** argv)
{
    /* We will store our picture here for processing. */
    CSPIC picture;
    
    /* Create for error checking */
    uint32_t err;

    /* If we were not given an argument we should read from stdin */
    if(argc == 1)
    {
        err = read_cs229picture(&picture,stdin);

        /* If we got an error report it and return FAIL */
        if(err)
        {
            ReportError(err);
            return FAIL;
        }
    }
    else if(argc == 2) /* We were given a file to read from so do so */
    {
        /* Create a file structure so we can read in our image */
        PFILE f;

        /* Open our file for reading, do so, then close the file */
        f = fopen((const char*)argv[1],"r");
        err = read_cs229picture(&picture,f);
        if(f)
            fclose(f);

        /* If we got an error report it and return FAIL */
        if(err)
        {
            ReportError(err);
            return FAIL;
        }
    }
    else
    {
        /* We recieved too many arguments and we don't know what to do */
        ReportError(TOO_MANY_ARGUMENTS);
        return FAIL;
    }

    /* Report the type of image we were asked to analyize */
    printf("The given image is a %s image.\n",picture.color_mode == MODE_BLACKWHITE ? "black and white" : "color");

    /* Report the size of the image */
    printf("The image is %u by %u pixels.\n",picture.width.value,picture.height.value);

    /* Report the bits in each relevant channel */
    if(picture.color_mode == MODE_BLACKWHITE)
        printf("Pixel channel size: %u bits\n",picture.first_channel.blackwhite_channel_size * 4);
    else
    {
        printf("Red channel size: %u bits\n",picture.first_channel.red_channel_size * 4);
        printf("Green channel size: %u bits\n",picture.green_channel_size * 4);
        printf("Blue channel size: %u bits\n",picture.blue_channel_size * 4);
    }

    /* Determine how many white and black pixels there are as a percentage */
    {
        uint32_t size;
        uint32_t color[3];

        /* Create a white pixel */
        /* Set the first value */
        if(picture.first_channel.blackwhite_channel_size == FOUR_BIT_CHANNEL)
            color[0] = 0xF;
        else if(picture.first_channel.blackwhite_channel_size == EIGHT_BIT_CHANNEL)
            color[0] = 0xFF;
        else if(picture.first_channel.blackwhite_channel_size == SIXTEEN_BIT_CHANNEL)
            color[0] = 0xFFFF;

        /* If we have a color image we need to set two more values */
        if(picture.color_mode == MODE_COLOR)
        {
            /* Set the second value */
            if(picture.green_channel_size == FOUR_BIT_CHANNEL)
                color[1] = 0xF;
            else if(picture.green_channel_size == EIGHT_BIT_CHANNEL)
                color[1] = 0xFF;
            else if(picture.green_channel_size == SIXTEEN_BIT_CHANNEL)
                color[1] = 0xFFFF;

            /* Set the third value */
            if(picture.blue_channel_size == FOUR_BIT_CHANNEL)
                color[2] = 0xF;
            else if(picture.blue_channel_size == EIGHT_BIT_CHANNEL)
                color[2] = 0xFF;
            else if(picture.blue_channel_size == SIXTEEN_BIT_CHANNEL)
                color[2] = 0xFFFF;
        }

        /* Figure out how big the image is */
        size = picture.height.value * picture.width.value;

        /* Print the percent of white pixels we have */
        printf("Percent of pixels that are white: %.2f%%\n",((float)count_pixels_of_color(&picture,color)) / size * 100.0f);

        /* Create a black pixel */
        color[0] = 0x0;
        color[1] = 0x0;
        color[2] = 0x0;

        /* Print the percent of black pixels we have */
        printf("Percent of pixels that are black: %.2f%%",((float)count_pixels_of_color(&picture,color)) / size * 100.0f);
    }

    /* Program executed successfully */
    return SUCCESS;
}

#endif
