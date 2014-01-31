/*
///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// convolute.c ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes an image and a kernel file and modifies the image by applying the     ///
/// kernel to it. Kernels should be odd sized squares stored in a single file.  ///
/// If a kernel is used on a color image three lines are expected, one for each ///
/// RGB value.                                                                  ///
/// There is no requirement for the kernel to be the same size for each RGB     ///
/// channel for color kernels. Kernels should be specified by listing the       ///
/// the width of the square and then the elements in row-column order           ///
/// delineated by whitespace.                                                   ///
/// The image should be given in stdin and the kernel file should be the only   ///
/// command line parameter.                                                     ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _CONVOLUTE_C
#define _CONVOLUTE_C

#include <stdio.h>
#include "../Headers/Defines.h"
#include "../Headers/Error.h"
#include "../Headers/CS229Image.h"

/* See the beginning of the file.
 */
int main(int argc, char** argv)
{
    /* We will be working on this picture */
    CSPIC picture;

    /* Create some local variables */
    uint32_t err;
    uint32_t i;
    uint32_t j;
    uint32_t end;
    int32_t* n;
    float** kernel;
    PFILE f;

    /* Check that we have our percentage to rotate by */
    if(argc != 2)
    {
        ReportError(argc < 2 ? TOO_FEW_ARGUMENTS : TOO_MANY_ARGUMENTS);
        return FAIL;
    }

    /* Open the kernel file */
    f = fopen(argv[1],"r");

    /* Check that we opened our kernel file successfully */
    if(f == NULL)
    {
        ReportError(FILE_NOT_FOUND);
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

    /* Check how many kernels we need */
    end = (picture.color_mode == MODE_COLOR ? 3 : 1);

    /* Create our outer kernel */
    kernel = (float**)malloc((size_t)(sizeof(float*) * end));

    /* Create the size pointer for the kernels */
    n = (int32_t*)malloc((size_t)(sizeof(uint32_t) * end));

    /* Read in the kernels */
    for(i = 0;i < end;i++)
    {
        /* Read in the size of the kernel */
        if(fscanf(f,"%i",&(n[i])) != 1)
        {
            /* Don't worry about freeing memory since it will be gone anyways */
            ReportError(KERNEL_FORMAT_ERROR);
            return FAIL;
        }

        /* Check that the kernel is of good size. */
        if(!(n[i] & 0x1) || n[i] < 1)
        {
            ReportError(INVALID_KERNEL_SIZE);
            return FAIL;
        }

        /* Create the sub-kernel */
        kernel[i] = (float*)malloc((size_t)(sizeof(float) * n[i] * n[i]));

        /* Read in each kernel value */
        for(j = 0;j < n[i] * n[i];j++)
            if(fscanf(f,"%f",&(kernel[i][j])) != 1)
            {
                ReportError(KERNEL_FORMAT_ERROR);
                return FAIL;
            }

        /* Check if we have an invalid kernel */
        if(i == end - 1)
        {
            if(!feof(f))
            {
                ReportError(end == 3 ? KERNEL_FORMAT_ERROR : BLACKWHITE_KERNEL_EXPECTED);
                return FAIL;
            }
        }
        else if(feof(f))
        {
            ReportError(end == 3 ? (i == 0 ? COLOR_KERNEL_EXPECTED : KERNEL_FORMAT_ERROR) : KERNEL_FORMAT_ERROR);
            return FAIL;
        }
    }

    /* Close the file */
    if(fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Convolute the image */
    err = convolute(&picture,kernel,(uint32_t*)n);

    /* If we got an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Write out the image */
    err = write_cs229_image(&picture,stdout);

    /* If we got an error then report it and abandon the program */
    if(err)
    {
        ReportError(err);
        return FAIL;
    }

    /* Image successfully convoluted */
    /* Don't worry about freeing memory since it will be gone anyways */
    return SUCCESS;
}

#endif
