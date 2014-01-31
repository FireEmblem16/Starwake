/*
///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// CS229_Image.c //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Attempts to interact with java in a way that doesn't destroy the minds of   ///
/// all mortals within ten feet and thus do some image stuff.                   ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _MATRIX_C
#define	_MATRIX_C

#include <jni.h>
#include <stdlib.h>
#include "../Headers/CS229Image.h"
#include "../Headers/CS229_Image.h"
#include "../Headers/Error.h"
#include "../Headers/Defines.h"

/* Convolutes an image. See convolute.c, but slightly modified.
 */
uint32_t convolute_this(uint8_t*,PCSPIC);

/* Loads an image named file into memory.
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_Load(JNIEnv* env, jobject this, jcharArray file)
{
    /* Create some local variables */
    PCSPIC pic;
    PFILE f;
    jsize len;
    jchar* arr;
    uint32_t i;
    uint8_t loc[258]; /* File names don't exceed 256 characters */

    /* Figure our where our image is */
    len = (*env)->GetArrayLength(env,file);
    arr = (*env)->GetCharArrayElements(env,file,0);
    
    /* Copy the file name into a useful form */
    for(i = 0;i < len;i++)
        loc[i] = arr[i];

    /* Just to be safe */
    loc[i] = '\0';

    /* Release the array */
    (*env)->ReleaseCharArrayElements(env,file,arr,0);

    /* Open the file */
    f = fopen((const char*)loc,"r");

    /* Make sure the file was found */
    if(f == NULL)
        return FILE_NOT_FOUND;

    /* Initialize our picture */
    pic = (PCSPIC)malloc((size_t)sizeof(CSPIC));

    /* Load our image */
    i = read_cs229picture(pic,f);

    /* Error check */
    if(i)
    {
        if(pic->data != NULL)
            free((void*)pic->data);
        free((void*)pic);
        return i;
    }

    /* Set the class values */
    {
        /* Create some local variables */
        jclass cls;
        jfieldID fid;

        /* Do some magic and set magic_number*/
        cls = (*env)->GetObjectClass(env,this);
        fid = (*env)->GetFieldID(env,cls,"magic_number","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->magic_number);

        /* Do some magic and set color_mode*/
        fid = (*env)->GetFieldID(env,cls,"color_mode","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->color_mode);

        /* Do some magic and set first_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"first_channel_size","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->first_channel.blackwhite_channel_size);

        /* Do some magic and set second_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"second_channel_size","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->green_channel_size);

        /* Do some magic and set third_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"third_channel_size","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->blue_channel_size);

        /* Do some magic and set width*/
        fid = (*env)->GetFieldID(env,cls,"width","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->width.value);
        
        /* Do some magic and set height*/
        fid = (*env)->GetFieldID(env,cls,"height","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->height.value);

        /* Do some magic and set data*/
        fid = (*env)->GetFieldID(env,cls,"data","I");

        if(!fid)
        {
            free((void*)pic->data);
            free((void*)pic);
            return FAIL;
        }

        (*env)->SetIntField(env,this,fid,(jint)pic->data);
    }

    /* Close the file */
    if(!fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Free the data we don't need to keep around */
    free((void*)pic);

    /* We loaded successfully */
    /* We don't free memory, this is not a leak */
    return SUCCESS;
}

/* Saves an image to disk.
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_Save(JNIEnv* env, jobject this, jcharArray file)
{
    /* Create some local variables */
    CSPIC pic;
    PFILE f;
    jsize len;
    jchar* arr;
    uint32_t i;
    uint8_t loc[258]; /* File names don't exceed 256 characters */

    /* Figure our where our image is */
    len = (*env)->GetArrayLength(env,file);
    arr = (*env)->GetCharArrayElements(env,file,0);

    /* Copy the file name into a useful form */
    for(i = 0;i < len;i++)
        loc[i] = arr[i];

    /* Just to be safe */
    loc[i] = '\0';

    /* Release the array */
    (*env)->ReleaseCharArrayElements(env,file,arr,0);

    /* Get the class values */
    {
        /* Create some local variables */
        jclass cls;
        jfieldID fid;

        /* Do some magic and get magic_number*/
        cls = (*env)->GetObjectClass(env,this);
        fid = (*env)->GetFieldID(env,cls,"magic_number","I");
        pic.magic_number = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get color_mode*/
        fid = (*env)->GetFieldID(env,cls,"color_mode","I");
        pic.color_mode = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get first_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"first_channel_size","I");
        pic.first_channel.blackwhite_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get second_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"second_channel_size","I");
        pic.green_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get third_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"third_channel_size","I");
        pic.blue_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get width*/
        fid = (*env)->GetFieldID(env,cls,"width","I");
        pic.width.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get height*/
        fid = (*env)->GetFieldID(env,cls,"height","I");
        pic.height.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get data*/
        fid = (*env)->GetFieldID(env,cls,"data","I");
        pic.data = (uint8_t*)(*env)->GetIntField(env,this,fid);
    }

    /* Open the file */
    f = fopen((const char*)loc,"w");

    /* Make sure the file was found */
    if(f == NULL)
        return FILE_NOT_FOUND;

    /* Attempt to write the image */
    i = write_cs229_image(&pic,f);
    
    /* Error check */
    if(i)
        return i;

    /* Close the file */
    if(!fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Image successfully saved */
    return SUCCESS;
}

/* Convolutes the image. No changes are made to the image class.
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_Convolute(JNIEnv* env, jobject this, jcharArray file)
{
    /* Create some local variables */
    CSPIC pic;
    jsize len;
    jchar* arr;
    uint32_t i;
    uint8_t loc[258]; /* File names don't exceed 256 characters */

    /* Figure our where our image is */
    len = (*env)->GetArrayLength(env,file);
    arr = (*env)->GetCharArrayElements(env,file,0);

    /* Copy the file name into a useful form */
    for(i = 0;i < len;i++)
        loc[i] = arr[i];

    /* Just to be safe */
    loc[i] = '\0';

    /* Release the array */
    (*env)->ReleaseCharArrayElements(env,file,arr,0);

    /* Get the class values */
    {
        /* Create some local variables */
        jclass cls;
        jfieldID fid;

        /* Do some magic and get magic_number*/
        cls = (*env)->GetObjectClass(env,this);
        fid = (*env)->GetFieldID(env,cls,"magic_number","I");
        pic.magic_number = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get color_mode*/
        fid = (*env)->GetFieldID(env,cls,"color_mode","I");
        pic.color_mode = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get first_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"first_channel_size","I");
        pic.first_channel.blackwhite_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get second_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"second_channel_size","I");
        pic.green_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get third_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"third_channel_size","I");
        pic.blue_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get width*/
        fid = (*env)->GetFieldID(env,cls,"width","I");
        pic.width.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get height*/
        fid = (*env)->GetFieldID(env,cls,"height","I");
        pic.height.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get data*/
        fid = (*env)->GetFieldID(env,cls,"data","I");
        pic.data = (uint8_t*)(*env)->GetIntField(env,this,fid);
    }

    /* Try to convlute the image */
    i = convolute_this(loc,&pic);

    /* Error check */
    if(i)
        return i;

    /* Image successfully convoluted */
    return SUCCESS;
}

/* Gets the value of a pixel at (x,y).
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_GetPixel(JNIEnv* env, jobject this, jint x, jint y, jintArray color)
{
    /* Create some local variables */
    CSPIC pic;
    uint32_t err;
    uint32_t ret_color[3];
    uint32_t pos;

    /* Get the class values */
    {
        /* Create some local variables */
        jclass cls;
        jfieldID fid;
        jsize len;

        /* Do some magic and get magic_number*/
        cls = (*env)->GetObjectClass(env,this);
        fid = (*env)->GetFieldID(env,cls,"magic_number","I");
        pic.magic_number = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get color_mode*/
        fid = (*env)->GetFieldID(env,cls,"color_mode","I");
        pic.color_mode = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Figure our where our image is */
        len = (*env)->GetArrayLength(env,color);

        /* Error check */
        if(len < (pic.color_mode == MODE_COLOR ? 3 : 1))
            return INVALID_IMAGE_COLOR_FORMAT;

        /* Do some magic and get first_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"first_channel_size","I");
        pic.first_channel.blackwhite_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get second_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"second_channel_size","I");
        pic.green_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get third_channel_size*/
        fid = (*env)->GetFieldID(env,cls,"third_channel_size","I");
        pic.blue_channel_size = (uint8_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get width*/
        fid = (*env)->GetFieldID(env,cls,"width","I");
        pic.width.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get height*/
        fid = (*env)->GetFieldID(env,cls,"height","I");
        pic.height.value = (uint32_t)(*env)->GetIntField(env,this,fid);

        /* Do some magic and get data*/
        fid = (*env)->GetFieldID(env,cls,"data","I");
        pic.data = (uint8_t*)(*env)->GetIntField(env,this,fid);
    }

    /* Figure out where our pixel is */
    pos = coordinate_to_linear_address(&pic,(uint32_t)x,(uint32_t)y);

    /* Get our pixel */
    err = get_pixel(&pic,pos,ret_color);

    /* Error check */
    if(err)
        return err;

    /* Set our return values */
    {
        /* Create some local variables */
        jint* orig;
        uint32_t i;

        /* Get the internal int* */
        orig = (*env)->GetIntArrayElements(env,color,0);

        /* Set the array elements */
        for(i = 0;i < (pic.color_mode == MODE_COLOR ? 3 : 1);i++)
            orig[i] = ret_color[i];

        /* Release the array */
        (*env)->ReleaseIntArrayElements(env,color,orig,0);
    }

    /* We successfully got the pixel */
    return SUCCESS;
}

/* Frees data used by this picture.
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_Free(JNIEnv* env, jobject this)
{
    /* Create some local varaibles */
    uint8_t* data;

    /* Get the class values */
    {
        /* Create some local variables */
        jclass cls;
        jfieldID fid;

        /* Do some magic */
        cls = (*env)->GetObjectClass(env,this);

        /* Do some magic and get data*/
        fid = (*env)->GetFieldID(env,cls,"data","I");
        data = (uint8_t*)(*env)->GetIntField(env,this,fid);
    }

    /* Free the data */
    free((void*)data);

    return SUCCESS;
}

/* Reports an error using our C error library.
 */
JNIEXPORT jint JNICALL Java_CS229_1Image_ReportError(JNIEnv* env, jobject this, jint err)
{
    ReportError(err);
    return SUCCESS;
}

/* Convolutes an image. See convolute.c, but slightly modified.
 */
uint32_t convolute_this(uint8_t* file, PCSPIC picture)
{
    /* Create some local variables */
    uint32_t err;
    uint32_t i;
    uint32_t j;
    uint32_t end;
    int32_t* n;
    float** kernel;
    PFILE f;

    /* Open the kernel file */
    f = fopen((const char*)file,"r");

    /* Check that we opened our kernel file successfully */
    if(f == NULL)
        return FILE_NOT_FOUND;

    /* Check how many kernels we need */
    end = (picture->color_mode == MODE_COLOR ? 3 : 1);

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
            return KERNEL_FORMAT_ERROR;
        }

        /* Check that the kernel is of good size. */
        if(!(n[i] & 0x1) || n[i] < 1)
        {
            return INVALID_KERNEL_SIZE;
        }

        /* Create the sub-kernel */
        kernel[i] = (float*)malloc((size_t)(sizeof(float) * n[i] * n[i]));

        /* Read in each kernel value */
        for(j = 0;j < n[i] * n[i];j++)
            if(fscanf(f,"%f",&(kernel[i][j])) != 1)
            {
                return KERNEL_FORMAT_ERROR;
            }

        /* Check if we have an invalid kernel */
        if(i == end - 1)
        {
            if(!feof(f))
                return (end == 3 ? KERNEL_FORMAT_ERROR : BLACKWHITE_KERNEL_EXPECTED);
        }
        else if(feof(f))
            return (end == 3 ? (i == 0 ? COLOR_KERNEL_EXPECTED : KERNEL_FORMAT_ERROR) : KERNEL_FORMAT_ERROR);
    }

    /* Close the file */
    if(fclose(f))
        ReportError(FILE_NOT_CLOSED);

    /* Convolute the image */
    err = convolute(picture,kernel,(uint32_t*)n);

    /* Free the kernel memory */
    for(i = 0;i < end;i++)
        free((void*)kernel[i]);

    /* Free some arrays */
    free((void*)kernel);
    free((void*)n);

    /* If we got an error then report it and abandon the program */
    if(err)
        return err;

    /* Image successfully convoluted */
    /* Don't worry about freeing memory since it will be gone anyways */
    return SUCCESS;
}

#endif
