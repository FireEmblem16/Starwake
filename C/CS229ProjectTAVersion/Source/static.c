#ifndef _STATIC_C
#define _STATIC_C

#pragma warning(disable:4996)

#include <stdlib.h>
#include <time.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"
#include "../Headers/util.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	SU_FILE su;
	uint32_t i;
	uint32_t err;
	uint32_t atoi_store;
	
	if(argc != 3)
	{
		fputs("This program requires two command line arguments of the form:\n",stderr);
		fputs("static <bit-size> <number of samples>\n",stderr);
		return EXIT_FAILURE;
	}

	if(!(atoi_store = atoi((c_string)argv[1])))
	{
		fputs("You have either entered a non-number or a zero for bit-size which must be 8, 16 or 32.\n",stderr);
		return EXIT_FAILURE;
	}

	/* We always output in mono format */
	su.header.flags = 0;

	if(atoi_store == 8)
		su.header.flags |= SU_8_BIT;
	else if(atoi_store == 16)
		su.header.flags |= SU_16_BIT;
	else if(atoi_store == 32)
		su.header.flags &= ~(SU_8_BIT | SU_16_BIT);
	else
	{
		fputs("Bit resolution must be 8, 16 or 32.\n",stderr);
		return EXIT_FAILURE;
	}

	if(!(atoi_store = atoi((c_string)argv[2])) && !is_zero_decimal((c_string)argv[2]))
	{
		fputs("You have either entered a non-number or a zero for the number of samples which must be a non-negative integer.\n",stderr);
		return EXIT_FAILURE;
	}

	su.header.num_samples = atoi_store;
	su.header.freq = 8000; /* We were not given a sample rate so we might as well go with 8kHz */
	
	i = su.header.flags & SU_8_BIT ? su.header.num_samples : (su.header.flags & SU_16_BIT ? su.header.num_samples << 1 : su.header.num_samples << 2);
	su.samples = (uint8_t*)malloc(i);

	srand((uint32_t)time((time_t*)NULL));

	if(su.header.flags & SU_8_BIT)
		for(i = 0;i < su.header.num_samples;i++)
			su.samples[i] = (uint8_t)rand();
	else if(su.header.flags & SU_16_BIT)
		for(i = 0;i < su.header.num_samples;i++)
			((uint16_t*)su.samples)[i] = (uint16_t)rand();
	else
		for(i = 0;i < su.header.num_samples;i++)
			((uint32_t*)su.samples)[i] = (uint32_t)rand();
	
#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif

	/* We're outputting garbage anyways so there's no reason for us to flip endian */
	err = write_su_file(&su,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif