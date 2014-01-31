/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// flip.c ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a cs229 image from stdin and outputs it to stdout a new flipped image.///
/// Valid inputs are v and h for vertical and horizontal flipping respectively. ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _FLIP_C
#define _FLIP_C

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See start of flip.c file.
 */
int main(int argc, char** argv)
{
    /* We will be working on this picture */
    CSPIC picture;

    /* Let's create some local variables */
    uint32_t err;

    /* Check that we have our percentage to rotate by */
    if(argc != 2)
    {
        ReportError(argc < 2 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);
        return FAIL;
    }

    /* The argument we we given should be a letter */
    if(!isalpha((int32_t)*argv[1]))
    {
        ReportError(INVALID_ARGUMENT_STRING_EXPECTED);
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
    
    /* Check that we have a valid flip */
    if(!(strcmp((const char*)argv[1],"h") || strcmp((const char*)argv[1],"v")))
    {
        ReportError(INVALID_ARGUMENT);
        return FAIL;
    }
    
    /* Rotate our image */
    err = flip_cs229_image(&picture,*(argv[1]));

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

    /* Image was flipped successfully */
    return SUCCESS;
}

#endif
