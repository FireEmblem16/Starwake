#ifndef _RMS_C
#define _RMS_C

#pragma warning(disable:4996)

#include <stdlib.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	SU_FILE su;
	uint32_t i;
	uint32_t err;
	double scale;

	if(argc != 2)
	{
		fputs("This program requires a single command line argument for the scale factor.\n",stderr);
		return EXIT_FAILURE;
	}

	scale = atof((c_string)argv[1]);

#ifdef WINDOWS
	if(scale == HUGE_VAL)
#else
	if(scale == HUGE_VAL())
#endif
	{
		fputs("You have provided a scale well beyond the limits of a double value.\n",stderr);
		return EXIT_FAILURE;
	}
	else if(scale == 0.0)
		if(!is_zero_real((c_string)argv[1]))
		{
			fputs("You have provided a non-number for scale which must be a real number.\n",stderr);
			return EXIT_FAILURE;
		}

#ifdef WINDOWS
	_setmode(_fileno(stdin),_O_BINARY);
#else
	stdin = freopen(NULL,"rb",stdin);
#endif

	err = read_su_file(&su,stdin);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = flip_endian(&su);
	
	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = scale_su_file(&su,scale);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = flip_endian(&su);
	
	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif
	
	err = write_su_file(&su,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif