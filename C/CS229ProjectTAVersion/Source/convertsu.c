#ifndef _CONVERT_SU_C
#define _CONVERT_SU_C

#pragma warning(disable:4996) /* Unsafe function warning */

#include "../Headers/defines.h"
#include "../Headers/util.h"
#include "../Headers/soundconvert.h"
#include "../Headers/suio.h"
#include "../Headers/wavio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	PFILE f;
	SU_FILE su_in;
	WAV_FILE wav_out;
	uint32_t err;

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

	err = read_su_file(&su_in,f);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = flip_endian(&su_in);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = su_to_wav(&wav_out,&su_in);

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

	err = write_wav_file(&wav_out,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif