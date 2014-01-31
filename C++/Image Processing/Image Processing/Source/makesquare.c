/*
///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// makesquare.c ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Creates a cs229 image file that contains a left-top aligned rectangle as    ///
/// as specified by the command arguments or by stdin if no command arguments   ///
/// are given.                                                                  ///
/// The arguments are as follows: [-bw/-c] -bw -> black white image             ///
/// -c -> color image, uint32_t picture width, uint32_t picture height,         ///
/// uint32_t square width, uint32_t square height, if -bw given (uint8_t) for   ///
/// the color to make the square and (uint8_t,uint8_t,uint8_t) for -c [These    ///
/// colors should be given as number with a space between each].                ///
/// All numbers are to be given as a char* and negative numbers are treated as  ///
/// their equivalant unsigned number.                                           ///
/// If an invalid size for the square (or image) is given and error is sent     ///
/// to stderr and no image is produced (Note that zero is an acceptable size).  ///
/// If color values exceed the channel size they will be forced to the channel  ///
/// size, in this case a single byte.                                           ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _MAKESQUARE_C
#define _MAKESQUARE_C

#include <stdlib.h>
#include <string.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* Prototypes */
uint32_t read_arguments(uint32_t,uint8_t**,PCSPIC,uint32_t[],uint32_t*,uint32_t*);

/* See start of makesquare.c file.
 */
int main(int argc, char** argv)
{
    /* Create local variables */
    uint32_t s_width;
    uint32_t s_height;
    uint8_t colors[3];
    uint32_t color[3];
    uint32_t i;
    uint32_t j;
    uint32_t len;
    uint32_t err;
    
    /* Create the picture we are going to output to our cs229 file and initialize it */
    CSPIC picture;
    picture.magic_number = CS229_MAGIC_NUMBER;
    picture.first_channel.red_channel_size = EIGHT_BIT_CHANNEL;
    picture.green_channel_size = EIGHT_BIT_CHANNEL;
    picture.blue_channel_size = EIGHT_BIT_CHANNEL;

    /* Parse the arguments given by stdin or the command line (use mul as a temp variable) */
    err = read_arguments((uint32_t)argc,(uint8_t**)argv,(PCSPIC)&picture,color,&s_width,&s_height);

    /* If we got an error report it and return FAIL */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Check that our square is less than or equal to the image size */
    if(s_width > picture.width.value || s_height > picture.height.value)
    {
        ReportError(INVALID_ARGUMENT);
        return FAIL;
    }

    /* Prepare to create the image */
    len = picture.height.value * picture.width.value;
    colors[0] = (uint8_t)color[0];
    colors[1] = (uint8_t)color[1];
    colors[2] = (uint8_t)color[2];

    /* If we are making a color image then the data will be three times as long */
    if(picture.color_mode == MODE_COLOR)
        len *= 3;
    
    /* Allocate the image data space */
    picture.data = (uint8_t*)malloc((size_t)len);

    /* Initialize the picture data to white */
    for(i = 0;i < len;i++)
        picture.data[i] = 0xFF;

    /* Create a new scope so we don't have variable clutter */
    {
        /* We are going to do some pre-loop math to save time */
        int i_base;
        int j_base;

        /* Find out where to center the square at. We subtract and extra one only if the square and not the
           image has a odd width or height. */
        i_base = (picture.height.value >> 1) - (s_height >> 1) - (s_height & 0x1 ? (picture.height.value & 0x1 ? 0 : 1) : 0);
        j_base = (picture.width.value >> 1) - (s_width >> 1) - (s_width & 0x1 ? (picture.width.value & 0x1 ? 0 : 1) : 0);

        /* Create our square */
        for(i = i_base;i < i_base + s_height;i++) /* Square height = s_height */
            for(j = j_base;j < j_base + s_width;j++) /* Square width = s_width */
                if(picture.color_mode == MODE_BLACKWHITE) /* Black and white has one byte per pixel */
                    picture.data[i * picture.width.value + j] = colors[0];
                else /* Color has three bytes per pixel */
                {
                    picture.data[i * picture.width.value * 3 + j * 3] = colors[0];
                    picture.data[i * picture.width.value * 3 + j * 3 + 1] = colors[1];
                    picture.data[i * picture.width.value * 3 + j * 3 + 2] = colors[2];
                }
    }

    /* Write our image data to disk */
    err = write_cs229_image(&picture,stdout);

    /* If we got an error report it and return FAIL */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* We sucessfully executed the program */
    return SUCCESS;
}

#endif

/* This function will take arguments from argv or from stdin if not given in argv
 * and puts them into the locations given in the prameters. If reading from stdin
 * the buffer is expected to be at the very first parameter at time of passing.
 * The uint32_t[] should have at least three elements to ensure no segfaults occur.
 */
uint32_t read_arguments(uint32_t argc, uint8_t** argv, PCSPIC picture, uint32_t color[], uint32_t* s_width, uint32_t* s_height)
{
    /* Create an place to catch error return values */
    uint32_t err;
    
    /* If we were given no arguments then we need to ask for them from stadard input.
       The first argument that is always passed is the executed file that is the program.*/
    if(argc == 1) /* One argument is valid for stdin input */
    {
        /* Create a place to hold our first string arugment and to be safe make sure it is a c string */
        uint8_t cmd[0x10];
        cmd[0xF] = '\0';

        /* Read in -bw or -c hopefully */
        if((err = fscanf(stdin,"%s",cmd)) == EOF)
            return UNEXPECTED_END_OF_FILE;

        /* Check that we read all of our arguments properly */
        if(err != 1)
            return INVALID_ARGUMENT_STRING_EXPECTED;

        /* Check what we read */
        if(!strcmp((const char*)cmd,"-c")) /* We read the parameter for color images */
            picture->color_mode = MODE_COLOR;
        else if(!strcmp((const char*)cmd,"-bw")) /* We read the parameter for black and white images */
            picture->color_mode = MODE_BLACKWHITE;
        else /* We recieved something invalid */
            return INVALID_IMAGE_COLOR_FORMAT;

        /* Read in the dimensions of the image and square */
        if((err = fscanf(stdin,"%u%u%u%u",&picture->width.value,&picture->height.value,s_width,s_height)) == EOF)
            return UNEXPECTED_END_OF_FILE;

        /* Check that we read all of our arguments properly */
        if(err != 4)
            return INVALID_ARGUMENT_NUMBER_EXPECTED;

        /* Read in the color the square should be */
        if(picture->color_mode == MODE_BLACKWHITE)
        {
            /* Black and white images only need one number */
            if((err = fscanf(stdin,"%u",color)) == EOF)
                return UNEXPECTED_END_OF_FILE;

            /* Check that we read all of our arguments properly */
            if(err != 1)
                return INVALID_ARGUMENT_NUMBER_EXPECTED;
        }
        else if(picture->color_mode == MODE_COLOR)
        {
            /* Color images need three number to represent a pixel */
            if((err = fscanf(stdin,"%u%u%u",color,&color[1],&color[2])) == EOF)
                return UNEXPECTED_END_OF_FILE;

            /* Check that we read all of our arguments properly */
            if(err != 3)
                return INVALID_ARGUMENT_NUMBER_EXPECTED;
        }
        else /* We somehow have an invalid color format */
            return INVALID_IMAGE_COLOR_FORMAT;
    }
    else if(argc == 7 || argc == 9) /* Seven or nine arguments is valid for -bw and -c respectivly */
    {
        /* Check what type of image color we are supposed to make */
        if(!strcmp((const char*)argv[1],"-c")) /* -c indicates color images should be made */
            picture->color_mode = MODE_COLOR;
        else if(!strcmp((const char*)argv[1],"-bw")) /* -bw indicates black and white images whould be made */
            picture->color_mode = MODE_BLACKWHITE;
        else /* We were given an invalid parameter */
            return INVALID_IMAGE_COLOR_FORMAT;

        /* Check if we have the apporpriate number of arguments for our image type */
        if(picture->color_mode == MODE_BLACKWHITE && argc != 7)
            return (argc < 7 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);
        else if(picture->color_mode == MODE_COLOR && argc != 9)
            return (argc < 9 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);

        /* Perform some error checking (use err for loop counter to save space) */
        for(err = 2;err < 6;err++)
            if(!isnumber(argv[err]))
                return INVALID_ARGUMENT_NUMBER_EXPECTED;

        /* Obtain out image and square dimensions */
        picture->width.value = atoi((const char*)argv[2]);
        picture->height.value = atoi((const char*)argv[3]);
        *s_width = atoi((const char*)argv[4]);
        *s_height = atoi((const char*)argv[5]);

        /* Obtain the color that we should output our square too. */
        if(picture->color_mode == MODE_BLACKWHITE)
        {
            if(!isnumber(argv[6]))
                return INVALID_ARGUMENT_NUMBER_EXPECTED;

            color[0] = atoi((const char*)argv[6]);
        }
        else
        {
            /* Perform some error checking (use err for loop counter to save space) */
            for(err = 6;err < 9;err++)
                if(!isnumber(argv[err]))
                    return INVALID_ARGUMENT_NUMBER_EXPECTED;

            color[0] = atoi((const char*)argv[6]);
            color[1] = atoi((const char*)argv[7]);
            color[2] = atoi((const char*)argv[8]);
        }
    }
    else /* We were given an invalid number of arguments */
        return INVALID_NUMBER_OF_ARGUMENTS;

    /* The function executed sucessfully */
    return SUCCESS;
}
