#ifndef _COMBINE_C
#define _COMBINE_C

#pragma warning(disable:4047) /* Indirection level warning */
#pragma warning(disable:4996) /* Unsafe function warning */

#include <stdio.h>
#include <stdlib.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	PFILE f;
	SU_FILE su;
	SU_FILE su_out;
	uint32_t err;
	uint32_t i;

	if(argc > 2)
	{
		fputs("This program supports supplying a file as the only argument with no extra characters added.\n",stderr);
		fputs("Quotations should be placed around file names with whitespace in them.\n",stderr);
		fputs("File path may be absolute or relative.\n",stderr);
		fputs("Reads from stdin by default.\n",stderr);

		return EXIT_FAILURE;
	}
	else if(argc == 2)
	{
		f = fopen((c_string)argv[1],"rb");

		if(f == (PFILE)NULL || f == (PFILE)BAD_PTR || f == (PFILE)UNDEF_PTR)
		{
			write_error(FILE_NOT_FOUND,stderr);
			return EXIT_FAILURE;
		}
	}
	else
#ifdef WINDOWS
	{
		_setmode(_fileno(stdin),_O_BINARY);
		f = stdin;
	}
#else
		f = freopen(NULL,"rb",stdin);
#endif

	err = read_su_file(&su,f);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	if(!(su.header.flags & SU_STEREO))
	{
		fputs("A mono su sound file was given when a stereo file was expected.\n",stderr);
		return EXIT_FAILURE;
	}

	err = flip_endian(&su);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	su_out.header.flags = su.header.flags & ~SU_STEREO;
	su_out.header.freq = su.header.freq;
	su_out.header.num_samples = su.header.num_samples;
	
	su_out.samples = (uint8_t*)malloc(su_out.header.flags & SU_8_BIT ? su_out.header.num_samples : (su_out.header.flags & SU_16_BIT ? su_out.header.num_samples << 1 : su_out.header.num_samples << 2));

	if(su_out.header.flags & SU_8_BIT)
		for(i = 0;i < su_out.header.num_samples;i++) /* If both numbers are odd our division by two will be one short so add it back in */
			su_out.samples[i] = su.samples[i << 1] / 2 + su.samples[(i << 1) + 1] / 2 + ((su.samples[i << 1] & 0x1) && (su.samples[(i << 1 ) + 1] & 0x1) ? 1 : 0);
	else if(su_out.header.flags & SU_16_BIT)
		for(i = 0;i < su_out.header.num_samples;i++)
			((uint16_t*)su_out.samples)[i] = ((uint16_t*)su.samples)[i << 1] / 2 + ((uint16_t*)su.samples)[(i << 1) + 1] / 2 + ((((uint16_t*)su.samples)[i << 1] & 0x1) && (((uint16_t*)su.samples)[(i << 1 ) + 1] & 0x1) ? 1 : 0);
	else
		for(i = 0;i < su_out.header.num_samples;i++)
			((uint32_t*)su_out.samples)[i] = ((uint32_t*)su.samples)[i << 1] / 2 + ((uint32_t*)su.samples)[(i << 1) + 1] / 2 + ((((uint32_t*)su.samples)[i << 1] & 0x1) && (((uint32_t*)su.samples)[(i << 1 ) + 1] & 0x1) ? 1 : 0);

#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif

	err = flip_endian(&su_out);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = write_su_file(&su_out,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif