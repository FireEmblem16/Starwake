#ifndef _CONVERT_WAV_C
#define _CONVERT_WAV_C

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
	WAV_FILE wav_in;
	SU_FILE su_out;
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

	err = read_wav_file(&wav_in,f);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = wav_to_su(&su_out,&wav_in);

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