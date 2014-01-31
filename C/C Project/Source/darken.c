/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// darken.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a cs229 image from stdin and outputs it to stdout a new darker image. ///
/// The new image will be darker accourding to the following:                   ///
/// The only command line argument should be the percentage by which we are to  ///
/// darken the image. This number will be multiplied by the difference of all   ///
/// pixel values and black then divided by 100. Then for each pixel, the        ///
/// value of that pixel will be reduced by the above formula for it's value.    ///
/// A value of 100 will reduce the entire image to black and a value of zero    ///
/// will leave the image unchanged. Negative percents will lighten instead.     ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _DARKEN_C
#define _DARKEN_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See start of darken.c file.
 */
int main(int argc, char** argv)
{
    /* We will be working on this picture */
    CSPIC picture;

    /* Let's create some local variables */
    int32_t percent;
    uint32_t err;

    /* Check that we have our percentage to darken by */
    if(argc != 2)
    {
        ReportError(argc < 2 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);
        return FAIL;
    }

    /* The argument we we given should be a number */
    if(!isnumber((uint8_t*)argv[1]))
    {
        ReportError(INVALID_ARGUMENT_NUMBER_EXPECTED);
        return FAIL;
    }

    /* Read in the image */
    err = read_cs229picture(&picture,stdin);

    /* If we go an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Get the value that we should darken by */
    percent = atoi((const char*)argv[1]);

    /* Darken our image */
    err = darken_cs229_image(&picture,percent);

    /* If we go an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Write the image to stdout */
    err = write_cs229_image(&picture,stdout);

    /* If we got an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Image was darkened successfully */
    return SUCCESS;
}

#endif
