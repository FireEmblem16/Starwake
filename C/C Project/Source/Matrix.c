/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Matrix.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Attempts to interact with java in a way that doesn't destroy the minds of   ///
/// all mortals within ten feet and thus multiply some matrixes. Multiplies two ///
/// valid matricies together of any size. The third array must be the exact     ///
/// same size as the resulting matrix from the multiplication.                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _MATRIX_C
#define	_MATRIX_C

#include <jni.h>
#include <stdlib.h>
#include "../Headers/Matrix.h"
#include "../Headers/Defines.h"

/* Free up the memory used by temp arrays */
void freeeverything(jint**,jsize,jint**,jsize,jint**,jsize);

/* See start of file. Also note that we seem to assume that arr3 is already defined.
 */
JNIEXPORT void JNICALL Java_Matrix_multiply(JNIEnv* env, jobject this, jobjectArray arr1, jobjectArray arr2, jobjectArray arr3)
{
    /* Create local variables */
    jint** arr_1;
    jsize len1;
    jsize len1_in;
    jint** arr_2;
    jsize len2;
    jsize len2_in;
    jint** arr_3;
    jsize len3;
    jsize len3_in;

    /* Destroy some dumb errors */
    len1_in = 0;
    len2_in = 0;

    /* Get the elements of our first array */
    {
        /* Create some local variables */
        uint32_t i;
        
        /* Figure out how long our first array is */
        len1 = (*env)->GetArrayLength(env,arr1);

        /* Define our first array level */
        arr_1 = (jint**)malloc((size_t)(sizeof(jint*) * len1));
        
        /* Go through each element of the array and process the sub arrays */
        for(i = 0;i < len1;i++)
        {
            /* Create some local variables */
            jintArray int_arr;
            jint* orig;
            uint32_t j;

            /* Get the internal arrays */
            int_arr = (*env)->GetObjectArrayElement(env,arr1,i);
            
            /* Get the length of the ith array */
            len1_in = (*env)->GetArrayLength(env,int_arr);

            /* Get the internal int* */
            orig = (*env)->GetIntArrayElements(env,int_arr,0);

            /* Create our stoarage array */
            arr_1[i] = (jint*)malloc((size_t)(sizeof(jint) * len1_in));

            /* Copy the array elements */
            for(j = 0;j < len1_in;j++)
                arr_1[i][j] = orig[j];

            /* Release the array */
            (*env)->ReleaseIntArrayElements(env,int_arr,orig,0);
        }
    }

    /* Get the elements of our second array */
    {
        /* Create some local variables */
        uint32_t i;

        /* Figure out how long our first array is */
        len2 = (*env)->GetArrayLength(env,arr2);

        /* Define our first array level */
        arr_2 = (jint**)malloc((size_t)(sizeof(jint*) * len2));

        /* Go through each element of the array and process the sub arrays */
        for(i = 0;i < len2;i++)
        {
            /* Create some local variables */
            jintArray int_arr;
            jint* orig;
            uint32_t j;

            /* Get the internal arrays */
            int_arr = (*env)->GetObjectArrayElement(env,arr2,i);
            
            /* Get the length of the ith array */
            len2_in = (*env)->GetArrayLength(env,int_arr);

            /* Get the internal int* */
            orig = (*env)->GetIntArrayElements(env,int_arr,0);

            /* Create our stoarage array */
            arr_2[i] = (jint*)malloc((size_t)(sizeof(jint) * len2_in));

            /* Copy the array elements */
            for(j = 0;j < len2_in;j++)
                arr_2[i][j] = orig[j];

            /* Release the array */
            (*env)->ReleaseIntArrayElements(env,int_arr,orig,0);
        }
    }

    /* Check if the matrixes are valid */
    if(len1_in != len2)
        return;

    /* Create our outer array */
    len3 = len1;
    len3_in = len2_in;
    arr_3 = (jint**)malloc((size_t)(sizeof(jint*) * len3));

    /* Make a new scope */
    {
        /* Create some local variables */
        uint32_t i;

        /* Create our inner arrays */
        for(i = 0;i < len3;i++)
            arr_3[i] = (jint*)malloc((size_t)(sizeof(jint) * len3_in));
    }

    /* Define our new array */
    {
        /* Create some local variables */
        uint32_t i;
        uint32_t j;

        /* Loop through each array index */
        for(i = 0;i < len3;i++)
            for(j = 0;j < len3_in;j++)
            {
                /* Create some local variables */
                uint32_t k;
                uint32_t val;

                /* Initialize val */
                val = 0;

                /* Do some math */
                for(k = 0;k < len1_in;k++)
                    val += arr_1[i][k] * arr_2[k][j];

                /* Set the value in the array */
                arr_3[i][j] = val;
            }
    }

    /* Set the output */
    {
        /* Create some local variables */
        uint32_t i;

        /* Check array length */
        if(len3 != (*env)->GetArrayLength(env,arr3))
        {
            freeeverything(arr_1,len1,arr_2,len2,arr_3,len3);
            return;
        }

        /* Go through each element of the array and process the sub arrays */
        for(i = 0;i < len1;i++)
        {
            /* Create some local variables */
            jintArray int_arr;
            jint* orig;
            uint32_t j;

            /* Get the internal arrays */
            int_arr = (*env)->GetObjectArrayElement(env,arr3,i);

            /* Check the length of the ith array */
            if(len3_in != (*env)->GetArrayLength(env,int_arr))
            {
                freeeverything(arr_1,len1,arr_2,len2,arr_3,len3);
                return;
            }

            /* Get the internal int* */
            orig = (*env)->GetIntArrayElements(env,int_arr,0);

            /* Set the array elements */
            for(j = 0;j < len1_in;j++)
                orig[j] = arr_3[i][j];

            /* Release the array */
            (*env)->ReleaseIntArrayElements(env,int_arr,orig,0);
        }
    }

    /* Free all of the memory we used */
    freeeverything(arr_1,len1,arr_2,len2,arr_3,len3);

    /* Return from the scary function call */
    return;
}

#endif

/* Free up the memory used by temp arrays */
void freeeverything(jint** arr_1, jsize len1, jint** arr_2, jsize len2, jint** arr_3, jsize len3)
{
    /* Create some local variables */
    uint32_t i;

    /* Delele the first set of internal arrays */
    for(i = 0;i < len1;i++)
        free((void*)arr_1[i]);

    /* Delele the second set of internal arrays */
    for(i = 0;i < len1;i++)
        free((void*)arr_2[i]);

    /* Delele the third set of internal arrays */
    for(i = 0;i < len1;i++)
        free((void*)arr_3[i]);

    /* Delete the external arrrays */
    free((void*)arr_1);
    free((void*)arr_2);
    free((void*)arr_3);

    /* We're done freeing things */
    return;
}
