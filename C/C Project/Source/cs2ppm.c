/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// cs2ppm.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a cs229 image from stdin and outputs it to stdout in ppm format.      ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _CS2PPM_C
#define _CS2PPM_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"
#include "../Headers/PPMImage.h"

/* See start of cs2ppm.c file.
 */
int main(int argc, char** argv)
{
    /* Create space for our pictures */
    CSPIC cs_picture;
    PPMIMG ppm_picture;

    /* Create a variable to hold errors */
    uint32_t err;

    /* Read a picture in from stdin */
    err = read_cs229picture(&cs_picture,stdin);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Convert our cs229 image to ppm. */
    err = convert_cs229_to_ppm(&cs_picture,&ppm_picture);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Write our image to stdout */
    err = write_ppm_image(&ppm_picture,stdout);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    return SUCCESS;
}

#endif
