#ifndef _APPEND_C
#define _APPEND_C

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
	PFILE in;
	SU_FILE dest;
	SU_FILE first;
	SU_FILE second;
	uint32_t err;
	
	if(argc != 3)
	{
		fputs("This program requires two command line arguments of the form: append <file1> <file2>\n",stderr);
		return EXIT_FAILURE;
	}

	in = fopen(argv[1],"rb");
	err = read_su_file(&first,in);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	in = fopen(argv[2],"rb");
	err = read_su_file(&second,in);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = join_su_file(&dest,&first,&second);
	
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

	err = write_su_file(&dest,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif