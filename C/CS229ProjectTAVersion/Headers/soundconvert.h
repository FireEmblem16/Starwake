#ifndef _SOUND_CONVERT_H
#define _SOUND_CONVERT_H

#include "suio.h"
#include "wavio.h"

/* Converts [src] to a wav file and stores it in [dest]. */
/* Expects [src] data to be in little endian format. */
/* Returns SUCCESS if no errors were encountered. */
/* Returns NULL_POINTER if [dest] or [src] are not valid. */
/* Returns BUFFER_OVERFLOW if there was not enough room to store data in [dest]. */
/* Returns INVALID_CONVERSION is [src] can not be converted into a wav file. */
uint32_t su_to_wav(PWAV_FILE dest,PSU_FILE src);

/* Converts [src] to a su file and stores it in [dest]. */
/* Returns [dest] data in little endian format. */
/* Returns SUCCESS if no errors were encountered. */
/* Returns NULL_POINTER if [dest] or [src] are not valid. */
/* Returns BUFFER_OVERFLOW if there was not enough room to store data in [dest]. */
/* Returns INVALID_CONVERSION is [src] can not be converted into a su file. */
uint32_t wav_to_su(PSU_FILE dest,PWAV_FILE src);

#endif