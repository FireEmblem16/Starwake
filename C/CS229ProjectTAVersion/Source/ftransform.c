#ifndef F_TRANSFORM_C
#define F_TRANSFORM_C

#pragma warning(disable:4996) /* Unsafe function warning */

#include "../Headers/defines.h"
#include "../Headers/util.h"
#include "../Headers/suio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	COMPLEX ft;
	PFILE in;
	SU_FILE sf;
	uint32_t err;

	if(argc > 2)
	{
		fputs("This program requires one command line argument specifying the file to read or for the file to be passing in from stdin.\n",stderr);
		return EXIT_FAILURE;
	}
	else if(argc == 2)
		in = fopen(argv[1],"rb");
	else
#ifdef WINDOWS
	{
		_setmode(_fileno(stdin),_O_BINARY);
		in = stdin;
	}
#else
		in = freopen(NULL,"rb",stdin);
#endif
	
	err = read_su_file(&sf,in);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = flip_endian(&sf);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = fourier_transform(&ft,&sf,256,0,1);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	if(sf.header.flags & SU_STEREO)
		fprintf(stdout,"Left channel ");

	fprintf(stdout,"Fourier Transform at k = 0: %.15f%s%.15f%s",ft.real," + ",ft.imag,"i\n");

	err = fourier_transform(&ft,&sf,256,127,1);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	if(sf.header.flags & SU_STEREO)
			fprintf(stdout,"Left channel ");

	fprintf(stdout,"Fourier Transform at k = 127: %.15f%s%.15f%s",ft.real," + ",ft.imag,"i\n");

	if(sf.header.flags & SU_STEREO)
	{
		err = fourier_transform(&ft,&sf,256,0,0);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		fprintf(stdout,"Right channel Fourier Transform at k = 0: %.15f%s%.15f%s",ft.real," + ",ft.imag,"i\n");

		err = fourier_transform(&ft,&sf,256,127,0);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		fprintf(stdout,"Right channel Fourier Transform at k = 127: %.15f%s%.15f%s",ft.real," + ",ft.imag,"i\n");
	}

	return EXIT_SUCCESS;
}

#endif