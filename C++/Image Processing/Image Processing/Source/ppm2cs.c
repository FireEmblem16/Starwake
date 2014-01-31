/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// ppm2cs.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a ppm image from stdin and outputs it to stdout in cs229 format.      ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _PPM2CS_C
#define _PPM2CS_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"
#include "../Headers/PPMImage.h"

/* See start of ppm2cs.c file.
 */
int main(int argc, char** argv)
{
    /* Create space for our pictures */
    CSPIC cs_picture;
    PPMIMG ppm_picture;

    /* Create a variable to hold errors */
    uint32_t err;

    /* Read a picture in from stdin */
    err = read_ppmpicture(&ppm_picture,stdin);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Convert our ppm image to cs229. */
    err = convert_ppm_to_cs229(&ppm_picture,&cs_picture);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Write our image to stdout */
    err = write_cs229_image(&cs_picture,stdout);

    /* If we had an error then we don't want to do anything more */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    return SUCCESS;
}

#endif
