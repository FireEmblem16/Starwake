#ifndef _UTIL_C
#define _UTIL_C

#pragma warning(disable:4047) /* Indirection level difference warning */

#include "../Headers/util.h"

uint32_t write_error(uint32_t err, PFILE out)
{
	if(out == (PFILE)NULL || out == (PFILE)NULL_POINTER || out == (PFILE)UNDEF_PTR)
		return NULL_POINTER;

	/* Wouldn't it be great if this compiled into assembly where we jump [err] times a fixed distance, because it would be */
	if(err == SUCCESS)
		return SUCCESS;
	else if(err == NULL_POINTER)
	{
		if(!fputs("A null poitner was detected. Be glad you didn't get a segmentation fault.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == IO_EXCEPTION)
	{
		if(!fputs("An io stream has experienced a malfunction.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == NO_HEADER_FOUND)
	{
		if(!fputs("Su sound file did not contain a header.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == NO_FREQUENCY_FOUND)
	{
		if(!fputs("Su sound file did not contain required field: frequency.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == NO_BITRES_FOUND)
	{
		if(!fputs("Su sound file did not contain required field: bitres.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_CHANNEL_FORMAT)
	{
		if(!fputs("Su sound file contained an invalid channel format.\nOnly stereo and mono are supported.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_NUMBER_FORMAT)
	{
		if(!fputs("String found when a number was expected.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_HEADER_TAG)
	{
		if(!fputs("Su sound file contains an invalid tag within header.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_HEADER)
	{
		if(!fputs("Su sound file contains an invalid header.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_BITRES_FORMAT)
	{
		if(!fputs("Su sound file contains an invalid bitres format.\nSupported values are: 8, 16 and 32.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == REDECLARATION_OF_TAG)
	{
		if(!fputs("Su sound file contains multipule copies of a single tag.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == UNEXPECTED_END_OF_FILE)
	{
		if(!fputs("A stream ended abruptly when more data was expected.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == UNEXPECTED_CONTINUATION_OF_FILE)
	{
		if(!fputs("A stream contained excess data when it was expected to be empty.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == BUFFER_OVERFLOW)
	{
		if(!fputs("Ran out of memory or exceeded the program's memory space.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == SAMPLE_SIZE_MISMATCH)
	{
		if(!fputs("Su sound file contained an amount of data not matching the amount it claimed to contain.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == FILE_NOT_FOUND)
	{
		if(!fputs("A file was not found.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == FILE_LEFT_OPEN)
	{
		if(!fputs("A file was left open.\nIt is highly likely that the stream had data not written.\n",out))
			return IO_EXCEPTION;
	}
	else if(err == INCOMPATABLE_FILE_TYPES)
	{
		if(!fputs("The given file(s) were incompatable with their intended function.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_CONVERSION)
	{
		if(!fputs("An attempt to convert between two incompatable types was made or the source type was ill defined.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_FREQUENCY_COMPONENT)
	{
		if(!fputs("When attempting to compute the Fourier Transform a k >= N/2 was provided.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == WINDOW_TOO_BIG)
	{
		if(!fputs("When attempting to compute the Fourier Transform an N greater than the number of samples available was provided.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == AUDIO_DEVICE_NOT_FOUND)
	{
		if(!fputs("No audio device could be found.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == UNSUPPORTED_WAV_FORMAT)
	{
		if(!fputs("Wave file format not supported.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == WAV_HEADER_CANNOT_BE_PREPARED)
	{
		if(!fputs("Wave header could not be prepared.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == WAV_REJECTS_BUFFER)
	{
		if(!fputs("Wave rejected buffer attachment.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == WAV_REFUSES_TO_START)
	{
		if(!fputs("Audio device could not be started.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_WAV_HEADER)
	{
		if(!fputs("A wav header was either not found or was invalid.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == INVALID_BITSIZE)
	{
		if(!fputs("The specified bitsize was not valid.\n",stderr))
			return IO_EXCEPTION;
	}
	else if(err == UNEXPECTED_FILE_TYPE)
	{
		if(!fputs("An unexpected file type was encountered during a directory crawl.\n",stderr))
			return IO_EXCEPTION;
	}

	if(!fflush(out))
		return IO_EXCEPTION;

	return SUCCESS;
}

uint32_t is_white_space(uint8_t c)
{
	if(c == ' ' || c == '\t' || c == '\r' || c == '\n')
		return TRUE;

	return FALSE;
}

uint32_t is_zero_decimal(c_string str)
{
	uint32_t i;

	for(i = 0;str[i];i++)
		if(str[i] != '0')
			return FALSE;

	return TRUE;
}

uint32_t is_zero_real(c_string str)
{
	uint32_t i;
	for(i = 0;str[i];i++)
		if(str[i] == '.')
			return str[i + 1] == '\0' || is_zero_decimal((c_string)(str + i + 1));
		else if(str[i] != '0')
			return FALSE;

	return TRUE;
}

uint32_t write_integer(uint32_t i, PFILE out)
{
	uint8_t to_print[4];

	if(out == (PFILE)NULL || out == (PFILE)NULL_POINTER || out == (PFILE)UNDEF_PTR)
		return NULL_POINTER;

	to_print[0] = (uint8_t)(i & 0x000000FF);
	to_print[1] = (uint8_t)((i & 0x0000FF00) >> 8);
	to_print[2] = (uint8_t)((i & 0x00FF0000) >> 16);
	to_print[3] = (uint8_t)((i & 0xFF000000) >> 24);

	for(i = 0;i < 4;i++)
	{
		uint32_t err;
		err = fputc(to_print[i],out);

		if(err != to_print[i] || err == EOF && ferror(out))
			return IO_EXCEPTION;
	}

	return SUCCESS;
}

uint32_t write_short(uint16_t i, PFILE out)
{
	uint8_t to_print[2];

	if(out == (PFILE)NULL || out == (PFILE)NULL_POINTER || out == (PFILE)UNDEF_PTR)
		return NULL_POINTER;

	to_print[0] = (uint8_t)(i & 0x000000FF);
	to_print[1] = (uint8_t)((i & 0x0000FF00) >> 8);

	for(i = 0;i < 2;i++)
	{
		uint32_t err;
		err = fputc(to_print[i],out);

		if(err != to_print[i] || err == EOF && ferror(out))
			return IO_EXCEPTION;
	}

	return SUCCESS;
}

uint32_t index_of(c_string str, uint8_t c, uint32_t start, uint8_t reverse_search)
{
	uint32_t len;
	uint32_t i;

	if(str == (c_string)NULL || str == (c_string)BAD_PTR || str == (c_string)UNDEF_PTR)
		return -1;

	len = strlen(str);

	if(start > len - 1)
		return -1;

	if(reverse_search)
	{
		for(i = start;i != -1;i--)
			if(str[i] == c)
				return i;
	}
	else
		for(i = start;i < len;i++)
			if(str[i] == c)
				return i;

	return -1;
}

double round(double d)
{
	double t;

	/* We don't need to worry about checking bounds because if [d] is not exactly zero then it will be soon */
	if(d == 0.0)
		return 0.0;

	t = fmod(d,1.0);

	if(d > 0.0)
		if(t >= 0.5)
			return ceil(d);
		else
			return floor(d);
	else
		if(t <= -0.5)
			return floor(d);
		else
			return ceil(d);
}

double factorial(uint32_t X)
{
	double ret = 1.0;

	for(;X;X--)
		ret *= (double)X;

	return ret;
}

#ifndef WINDOWS
double HUGE_VAL()
{
	double ret;
	ret = 99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.9;
	ret *= ret;
	ret *= ret;
	return ret;
}

double sin(double theta)
{
	double theta_2;
	double theta_3;
	double theta_5;
	double theta_7;
	double theta_9;
	double theta_11;
	double theta_13;
	double theta_15;
	double theta_17;
	double theta_19;
	double theta_21;
	double theta_23;
	double theta_25;

	while(theta > PI)
		theta -= 2.0 * PI;

	while(theta <= -PI)
		theta += 2.0 * PI;

	theta_2 = theta * theta;
	theta_3 = theta_2 * theta;
	theta_5 = theta_2 * theta_3;
	theta_7 = theta_2 * theta_5;
	theta_9 = theta_2 * theta_7;
	theta_11 = theta_2 * theta_9;
	theta_13 = theta_2 * theta_11;
	theta_15 = theta_2 * theta_13;
	theta_17 = theta_2 * theta_15;
	theta_19 = theta_2 * theta_17;
	theta_21 = theta_2 * theta_19;
	theta_23 = theta_2 * theta_21;
	theta_25 = theta_2 * theta_23;

	return theta - theta_3 / 6.0 + theta_5 / 120.0 - theta_7 / 5040.0 + theta_9 / 362880.0 - theta_11 / 39916800.0 + theta_13 / 6227020800.0 - theta_15 / 1307674368000.0 + theta_17 / 355687428096000.0 - theta_19 / 121645100408832000.0 + theta_21 / 51090942171709440000.0 - theta_23 / 25852016738884976640000.0 + theta_25 / 15511210043330985984000000.0;
}

double cos(double theta)
{
	double theta_2;
	double theta_4;
	double theta_6;
	double theta_8;
	double theta_10;
	double theta_12;
	double theta_14;
	double theta_16;
	double theta_18;
	double theta_20;
	double theta_22;
	double theta_24;

	while(theta > PI)
		theta -= 2.0 * PI;

	while(theta <= -PI)
		theta += 2.0 * PI;

	theta_2 = theta * theta;
	theta_4 = theta_2 * theta_2;
	theta_6 = theta_2 * theta_4;
	theta_8 = theta_2 * theta_6;
	theta_10 = theta_2 * theta_8;
	theta_12 = theta_2 * theta_10;
	theta_14 = theta_2 * theta_12;
	theta_16 = theta_2 * theta_14;
	theta_18 = theta_2 * theta_16;
	theta_20 = theta_2 * theta_18;
	theta_22 = theta_2 * theta_20;
	theta_24 = theta_2 * theta_22;

	return 1 - theta_2 / 2.0 + theta_4 / 24.0 - theta_6 / 720.0 + theta_8 / 40320.0 - theta_10 / 3628800.0 + theta_12 / 479001600.0 - theta_14 / 87178291200.0 + theta_16 / 20922789888000.0 - theta_18 / 6402372705728000.0 + theta_20 / 2432902008176640000.0 - theta_22 / 1124000727777607680000.0 + theta_24 / 620448401733239439360000.0;
}

double fmod(double X, double Y)
{
	double res;
	res = (double)(int32_t)(X / Y);

	return X - res * Y;
}

double floor(double X)
{
	if (X < 0.0)
		return (double)(int32_t)(X / 1.0) - 1.0;

	return (double)(int32_t)(X / 1.0);
}

double ceil(double X)
{
	if(X < 0.0)
		return (double)(int32_t)(X / 1.0);

	return (double)(int32_t)(X / 1.0) + 1.0;
}

double log10(double X)
{
	double ret = 0.0;
	double log_e = 0.43429448190325;
	double X_m_Y = X - 1.0;
	double X2 = X_m_Y * X_m_Y;
	double X3 = X2 * X_m_Y;
	double X4 = X3 * X_m_Y;
	double X5 = X4 * X_m_Y;
	double X6 = X5 * X_m_Y;
	double X7 = X6 * X_m_Y;
	double X8 = X7 * X_m_Y;
	double X9 = X8 * X_m_Y;
	double X10 = X9 * X_m_Y;

	if(X <= 0.0)
		return -HUGE_VAL();

	ret += X_m_Y * log_e;
	ret -= X2 * log_e / 2.0;
	ret += X3 * log_e / 3.0;
	ret -= X4 * log_e / 4.0;
	ret += X5 * log_e / 5.0;
	ret -= X6 * log_e / 6.0;
	ret += X7 * log_e / 7.0;
	ret -= X8 * log_e / 8.0;
	ret += X9 * log_e / 9.0;
	ret -= X10 * log_e / 10.0;

	return ret;
}

double sqrt(double X)
{
	double ret = X;
	int i;

	if(X < 0.0)
		return -1.0;

	for(i = 0;i < 20;i++)
		ret = (((ret * ret) + X) / (2.0 * ret));

	return ret;
}
#endif

#endif