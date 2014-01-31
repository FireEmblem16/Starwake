#ifndef _GENTEST1_C
#define _GENTEST1_C

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

	if(argc > 1)
	{
		fputs("This program does not support command line arguments.\n",stderr);
		return EXIT_FAILURE;
	}

	su.samples = (uint8_t*)malloc(10000);
	su.header.flags |= SU_8_BIT;
	su.header.flags &= ~SU_STEREO;
	su.header.freq = 8000;
	su.header.num_samples = 10000;
	
	for(i = 0;i < 10000;i++)
		su.samples[i] = 0;
	
#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif

	/* No need to flip endian since everything is zero and because we have an 8-bit resolution */
	err = write_su_file(&su,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif