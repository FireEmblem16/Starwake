#ifndef _MIX_C
#define _MIX_C

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
	PSU_FILE files;
	SU_FILE dest;
	uint32_t num_files;
	uint32_t i;
	uint32_t err;
	double atof_store;
	double* file_intensities;
	
	if(!(argc & 0x1) || argc < 3)
	{
		fputs("This program requires an even number of command line arguments, at least two, of the form:\n",stderr);
		fputs("mix <file1> <relative volume 1> [<file2> <relative volume 2> ...]\n",stderr);
		return EXIT_FAILURE;
	}

	num_files = (argc - 1) >> 1;
	files = (PSU_FILE)malloc(sizeof(SU_FILE) * num_files);
	file_intensities = (double*)malloc(sizeof(double) * num_files);

	for(i = 0;i < num_files;i++)
	{
		/* We check [in] for errors in read_su_file */
		PFILE in = fopen(argv[(i << 1) + 1],"rb");
		err = read_su_file(&files[i],in);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		err = flip_endian(&files[i]);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		atof_store = atof((c_string)argv[(i + 1) << 1]);

#ifdef WINDOWS
		if(atof_store == HUGE_VAL)
#else
		if(atof_store == HUGE_VAL())
#endif
		{
			fputs("You have provided a number well beyond the limits of a double value.\n",stderr);
			return EXIT_FAILURE;
		}
		else if(atof_store == 0.0)
			if(!is_zero_real((c_string)argv[(i + 1) << 1]))
			{
				fputs("You have provided a non-number for duration which must be a real number.\n",stderr);
				return EXIT_FAILURE;
			}

		file_intensities[i] = atof_store;
	}

	err = mix_su_file(&dest,files,file_intensities,num_files);
	
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

	err = flip_endian(&dest);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = write_su_file(&dest,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif