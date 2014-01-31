/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// rotate.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a cs229 image from stdin and outputs it to stdout a new rotated image.///
/// Valid inputs for rotation angle are 90 -90 and 180 which specify rotation   ///
/// in degrees relative to its original orientation.                            ///
/// Positive values are clockwise.                                              ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _ROTATE_C
#define _ROTATE_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See start of rotate.c file.
 */
int main(int argc, char** argv)
{
    /* We will be working on this picture */
    CSPIC picture;

    /* Let's create some local variables */
    int32_t angle;
    uint32_t err;

    /* Check that we have our percentage to rotate by */
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

    /* If we got an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Get the value that we should rotate by */
    angle = atoi((const char*)argv[1]);

    /* Check that we have a valid rotation size */
    if(angle != 90 && angle != -90 && angle != 180)
    {
        ReportError(INVALID_ARGUMENT);
        return FAIL;
    }
    
    /* Rotate our image */
    err = rotate_cs229_image(&picture,angle);

    /* If we got an error then report it and abandon the program */
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

    /* Image was rotated successfully */
    return SUCCESS;
}

#endif
