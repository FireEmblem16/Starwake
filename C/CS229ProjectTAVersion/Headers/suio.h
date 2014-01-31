#ifndef _SU_H
#define _SU_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "defines.h"
#include "util.h"

#ifdef WINDOWS
#include <math.h>
#endif

#define SU_STEREO 0x01 /* Mono functionality can be observed by the lack of this bit being set */
#define SU_8_BIT 0x02 /* 32 bit functionality can be observed by the lack of these bits being set */
#define SU_16_BIT 0x04

typedef struct su_header
{
	uint32_t freq;
	uint32_t num_samples;
	uint8_t flags;
} SU_HEADER, *PSU_HEADER;

typedef struct su_file
{
	SU_HEADER header;
	uint8_t* samples;
} SU_FILE, *PSU_FILE;

/* Writes the su sound file [f] to the file stream [out] in the appropriate format. */
/* Includes optional data when writing [f]. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [f] or [out] are not valid. */
/* Returns IO_EXCEPTION if an error occurs during use of the file stream [out] or if [out] is already errored. */
/* User is responsible for opening the file stream [out] as well as closing the file stream after use. */
uint32_t write_su_file(PSU_FILE f,PFILE out);

/* Reads a su sound file from [in] into [dest]. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [dest] or [in] is not valid. */
/* Returns IO_EXCEPTION if an error occurs during use of the file stream [in] or if [in] is already errored. */
/* Returns NO_HEADER_FOUND if [in] doesn't contain a su file. */
/* Returns NO_FREQUENCY_FOUND if [in] does not specify the sound frequency. */
/* Returns NO_BITRES_FOUND if [in] does not specify the sound sample size. */
/* Returns INVALID_CHANNEL_FORMAT if [in] specifies a non-MONO and non-STEREO value for channel. */
/* Returns INVALID_NUMBER_FORMAT if [in] contains a non-valid expression when a number is expected. */
/* Returns INVALID_HEADER_TAG if [in] contains an invalid tag in the header. */
/* Returns INVALID_BITRES_FORMAT if [in] contains an invalid value for the bit resolution. */
/* Returns REDELCARATION_OF_TAG if [in] tries to define a tag more than once. */
/* Returns UNEXPECTED_END_OF_FILE if [in] is valid until an early termination is detected. */
/* Returns UNEXPECTED_CONTINUATION_OF_FILE if [in] has more data than we could read. This is highly unlikely. */
/* Returns BUFFER_OVERFLOW if [in] is too long. */
/* Returns SAMPLE_SIZE_MISMATCH if [in] specifies a value for num_samples but contains a different amount of data. */
/* User is responsible for opening and closing [in]. */
uint32_t read_su_file(PSU_FILE dest,PFILE in);

/* Converts the data in [f] from little to big endian or from big to little endian. */
/* No flag is set to record which format [f] has so the user is responsible for knowing. */
/* Does nothing if [f] has an 8-bit resolution. */
/* Returns NULL_POINTER if [f] is invalid. */
uint32_t flip_endian(PSU_FILE f);

/* Creates a new su sound file in [dest] by adding together the data in each file multiplied by their intensity value. */
/* The highest value will be scaled to the highest possible value in the su sound file. Similarly for the lowerst value. */
/* Each file need not be the same length but the channel type, bit resolution and sample rate must be the same. */
/* Expects data in [files] to be in little endian format. */
/* Returns SUCCESS if everything goes as planned. */
/* Returns NULL_POINTER if [dest], [files] or [file_intensities] is not valid. */
/* Returns INCOMPATABLE_FILE_TYPES if the channel type, bit resolution or sample rate of every su sound file are not the same. */
/* Returns BUFFER_OVERFLOW if we run out of memory. */
uint32_t mix_su_file(PSU_FILE dest,PSU_FILE files,double* file_intensities,uint32_t num_files);

/* As mix_su_file but the maximum volume is garunteed to be preserved. */
uint32_t mix_su_file_no_volume_gain(PSU_FILE dest, PSU_FILE files, double* file_intensities, uint32_t num_files);

/* Joins the two given su sound files together sequentially into [dest]. */
/* Returns SUCCESS if everything goes as planned. */
/* Returns NULL_POINTER if [dest], [first] or [second] is not valid. */
/* Returns INCOMPATABLE_FILE_TYPES if the channel type, bit resolution or sample rate of both su sound files are not the same. */
/* Returns BUFFER_OVERFLOW if we run out of memory. */
uint32_t join_su_file(PSU_FILE dest,PSU_FILE first,PSU_FILE second);

/* Returns the Fourier Transform of the given file for the given window size at the kth frequency component. */
/* If [source] is a stereo file then if lc is not zero the transform is calculated from the left channel and from the right channel if zero. */
/* Expects [source] data to be in little endian format. */
/* Returns NULL_POINTER if [dest] or [source] is not valid. */
/* Returns INVALID_FREQUENCY_COMPONENT if k >= N/2. */
/* Returns WINDOW_TOO_BIG if N is larger than the number of available samples. */
uint32_t fourier_transform(PCOMPLEX dest,PSU_FILE source,uint32_t N,uint32_t k,uint32_t lc);

/* Returns the RMS of the given file. */
/* Returns -HUGE_VAL if [f] is not valid. */
double su_rms(PSU_FILE f);

/* Scales each value in [f] by [factor]. */
/* Returns NULL_POINTER if [f] is invalid. */
uint32_t scale_su_file(PSU_FILE f, double factor);

#endif