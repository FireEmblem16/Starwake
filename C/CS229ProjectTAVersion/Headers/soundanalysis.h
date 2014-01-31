#ifndef _ANALYSIS_H
#define _ANALYSIS_H

#include "defines.h"

#ifdef WINDOWS
#include <math.h>
#else
#include "util.h"
#endif

/* Returns the mean of the samples in [data]. */
/* Returns HUGE_VAL if [vdata] is not valid. */
/* Returns HUGE_VAL if data_bitsize is not 8, 16 or 32. */
double s_calc_mean(void* vdata, uint32_t length, uint8_t data_bitsize);

/* Returns the variance of the samples in [data]. */
/* Returns HUGE_VAL if [vdata] is not valid. */
/* Returns HUGE_VAL if data_bitsize is not 8, 16 or 32. */
double s_calc_varaince(void* vdata, uint32_t length, uint8_t data_bitsize);

/* Returns the covariance of [vdata1] and [vdata2]. */
/* Returns HUGE_VAL if [vdata1] or [vdata2] is not valid. */
/* Returns HUGE_VAL if data_bitsize is not 8, 16 or 32. */
double s_calc_covariance(void* vdata1, void* vdata2, uint32_t length, uint8_t data_bitsize);

/* Returns the variance of [X] - [Y]. */
/* Returns HUGE_VAL if [X] or [Y] is not valid. */
/* Returns HUGE_VAL if data_bitsize is not 8, 16 or 32. */
double s_calc_X_minus_Y_variance(void* X, void* Y, uint32_t length, uint8_t data_bitsize);

/* Returns the number of samples in [vdata1] that match the sample sample index in [vdata2]. */
/* [offset] will be added to each sample in [vdata2] during calculation. */
/* Returns NULL_POINTER if [vdata1] or [vdata2] is not valid. */
/* Returns INVALID_BITSIZE if data_bitsize is not 8, 16 or 32. */
uint32_t s_calc_matching_samples(void* vdata1, void* vdata2, uint32_t offset, uint32_t length, uint8_t data_bitsize);

#endif