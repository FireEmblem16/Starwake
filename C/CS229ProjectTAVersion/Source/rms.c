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
	double rms;

	if(argc != 1)
	{
		fputs("This program takes no arguments.\n",stderr);
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

	rms = su_rms(&su);

#ifdef WINDOWS
	if(rms == -HUGE_VAL)
#else
	if(rms == -HUGE_VAL())
#endif
	{
		fputs("Unable to calculate rms for the provided file.\n",stderr);
		return EXIT_FAILURE;
	}
	
#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif

	fprintf(stdout,"%lf",rms);
	return EXIT_SUCCESS;
}

#endif