#ifndef _ANALYSIS_C
#define _ANALYSIS_C

#include "../Headers/soundanalysis.h"

double s_calc_mean(void* vdata, uint32_t length, uint8_t data_bitsize)
{
	uint32_t i;
	double ret = 0.0;

	if(vdata == (void*)BAD_PTR || vdata == (void*)NULL || vdata == (void*)UNDEF_PTR)
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	if(data_bitsize == 8)
	{
		uint8_t* data = (uint8_t*)vdata;

		for(i = 0;i < length;i++)
			ret += (double)data[i] / (double)length;
	}
	else if(data_bitsize == 16)
	{
		uint16_t* data = (uint16_t*)vdata;

		for(i = 0;i < length;i++)
			ret += (double)data[i] / (double)length;
	}
	else if(data_bitsize == 32)
	{
		uint32_t* data = (uint32_t*)vdata;

		for(i = 0;i < length;i++)
			ret += (double)data[i] / (double)length;
	}
	else
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	return ret;
}

double s_calc_varaince(void* vdata, uint32_t length, uint8_t data_bitsize)
{
	uint32_t i;
	double mean;
	double ret = 0.0;

	if(vdata == (void*)BAD_PTR || vdata == (void*)NULL || vdata == (void*)UNDEF_PTR)
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	mean = s_calc_mean(vdata,length,data_bitsize);

	if(data_bitsize == 8)
	{
		uint8_t* data = (uint8_t*)vdata;

		for(i = 0;i < length;i++)
		{
			double partial = (double)data[i] - mean;
			ret += partial * partial / (double)length;
		}
	}
	else if(data_bitsize == 16)
	{
		uint16_t* data = (uint16_t*)vdata;

		for(i = 0;i < length;i++)
		{
			double partial = (double)data[i] - mean;
			ret += partial * partial / (double)length;
		}
	}
	else if(data_bitsize == 32)
	{
		uint32_t* data = (uint32_t*)vdata;

		for(i = 0;i < length;i++)
		{
			double partial = (double)data[i] - mean;
			ret += partial * partial / (double)length;
		}
	}
	else
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	return ret;
}

double s_calc_covariance(void* vdata1, void* vdata2, uint32_t length, uint8_t data_bitsize)
{
	uint32_t i;
	double mean1;
	double mean2;
	double ret = 0.0;

	if(vdata1 == (void*)BAD_PTR || vdata1 == (void*)NULL || vdata1 == (void*)UNDEF_PTR || vdata2 == (void*)BAD_PTR || vdata2 == (void*)NULL || vdata2 == (void*)UNDEF_PTR)
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	mean1 = s_calc_mean(vdata1,length,data_bitsize);
	mean2 = s_calc_mean(vdata2,length,data_bitsize);

	if(data_bitsize == 8)
	{
		uint8_t* data1 = (uint8_t*)vdata1;
		uint8_t* data2 = (uint8_t*)vdata2;

		for(i = 0;i < length;i++)
			ret += ((double)data1[i] - mean1) * ((double)data2[i] - mean2) / (double)length;
	}
	else if(data_bitsize == 16)
	{
		uint16_t* data1 = (uint16_t*)vdata1;
		uint16_t* data2 = (uint16_t*)vdata2;

		for(i = 0;i < length;i++)
			ret += ((double)data1[i] - mean1) * ((double)data2[i] - mean2) / (double)length;
	}
	else if(data_bitsize == 32)
	{
		uint32_t* data1 = (uint32_t*)vdata1;
		uint32_t* data2 = (uint32_t*)vdata2;

		for(i = 0;i < length;i++)
			ret += ((double)data1[i] - mean1) * ((double)data2[i] - mean2) / (double)length;
	}
	else
#ifdef WINDOWS
		return HUGE_VAL;
#else
		return HUGE_VAL();
#endif

	return ret;
}

double s_calc_X_minus_Y_variance(void* X, void* Y, uint32_t length, uint8_t data_bitsize)
{
	return s_calc_varaince((void*)X,length,data_bitsize) + s_calc_varaince((void*)Y,length,data_bitsize) - 2.0 * s_calc_covariance((void*)X,(void*)Y,length,data_bitsize);
}

uint32_t s_calc_matching_samples(void* vdata1, void* vdata2, uint32_t offset, uint32_t length, uint8_t data_bitsize)
{
	uint32_t i;
	uint32_t count = 0;

	if(vdata1 == (void*)BAD_PTR || vdata1 == (void*)NULL || vdata1 == (void*)UNDEF_PTR || vdata2 == (void*)BAD_PTR || vdata2 == (void*)NULL || vdata2 == (void*)UNDEF_PTR)
		return NULL_POINTER;

	if(data_bitsize == 8)
	{
		uint8_t* data1 = (uint8_t*)vdata1;
		uint8_t* data2 = (uint8_t*)vdata2;

		for(i = 0;i < length;i++)
			if(data1[i] == data2[i] + (uint8_t)offset)
				++count;
	}
	else if(data_bitsize == 16)
	{
		uint16_t* data1 = (uint16_t*)vdata1;
		uint16_t* data2 = (uint16_t*)vdata2;

		for(i = 0;i < length;i++)
			if(data1[i] == data2[i] + (uint16_t)offset)
				++count;
	}
	else if(data_bitsize == 32)
	{
		uint32_t* data1 = (uint32_t*)vdata1;
		uint32_t* data2 = (uint32_t*)vdata2;

		for(i = 0;i < length;i++)
			if(data1[i] == data2[i] + (uint32_t)offset)
				++count;
	}
	else
		return INVALID_BITSIZE;

	return count;
}

#endif