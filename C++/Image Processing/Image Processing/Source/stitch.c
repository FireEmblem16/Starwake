/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// stitch.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes two files specified by commandline arguments and stitches them        ///
/// together such that the difference between overlaps is minimized and the     ///
/// color values of the overlapped regions are averaged. Must have at least ten ///
/// percent overlap on the smaller image to be stitched.                        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _STITCH_C
#define _STITCH_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See the beginning of the file.
 */
int main(int argc, char** argv)
{
    /* Create some local variables */
    CSPIC pic1;
    CSPIC pic2;
    CSPIC pic3;
    PFILE f;
    uint32_t err;

    /* Check that we have the appropriate number of arguments */
    if(argc != 4)
    {
        ReportError(argc < 4 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);
        return FAIL;
    }

    /* Try to open the first file */
    f = fopen(argv[1],"r");
    
    /* Check that the file is open */
    if(!f)
    {
        ReportError(FILE_NOT_FOUND);
        return FAIL;
    }

    /* Load the picture */
    err = read_cs229picture(&pic1,f);
    
    /* Check that the image was loaded */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }
    
    /* Close the file */
    if(fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Try to open the second file */
    f = fopen(argv[2],"r");

    /* Check that the file is open */
    if(!f)
    {
        ReportError(FILE_NOT_FOUND);
        return FAIL;
    }

    /* Load the picture */
    err = read_cs229picture(&pic2,f);

    /* Check that the image was loaded */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Close the file */
    if(fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Try to open the third file now to avoid wasting time due to no out file */
    f = fopen(argv[3],"w");

    /* Check that the file is open */
    if(!f)
    {
        ReportError(FILE_NOT_FOUND);
        return FAIL;
    }

    /* Stitch the images */
    err = stitch_images(&pic1,&pic2,&pic3);

    /* Error check */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Write the picture */
    err = write_cs229_image(&pic2,f);

    /* Check that the image was loaded */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Close the file */
    if(fclose(f))
        ReportError(FILE_NOT_CLOSED);

    return SUCCESS;
}

#endif
