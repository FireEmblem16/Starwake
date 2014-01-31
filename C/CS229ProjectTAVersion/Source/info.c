#ifndef _INFO_C
#define _INFO_C

#pragma warning(disable:4047) /* Indirection level warning */
#pragma warning(disable:4996) /* Unsafe function warning */

#include <stdio.h>
#include <stdlib.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"
#include "../Headers/wavio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	PFILE f;
	PFILE temp;
	SU_FILE su;
	WAV_FILE wav;
	uint32_t err;
	uint32_t err2;
	uint32_t iswav;

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

	temp = fopen("temp","wb");
	
	while(1)
	{
		int c = fgetc(f);

		if(feof(f) || ferror(f))
			break;

		fputc(c,temp);
	}

	fclose(temp);

	temp = fopen("temp","rb");
	err = read_su_file(&su,temp);
	fclose(temp);

	if(err)
	{
		temp = fopen("temp","rb");
		err2 = read_wav_file(&wav,temp);
		fclose(temp);

		remove("temp");

		if(err2)
		{
			write_error(err,stderr);
			write_error(err2,stderr);
			return EXIT_FAILURE;
		}
		else
			iswav = TRUE;
	}
	else
	{
		iswav = FALSE;
		remove("temp");
	}

	fputs("Reading from ",stdout);

	if(argc == 1)
		fputs("stdin",stdout);
	else
		fputs((c_string)argv[1],stdout);

	if(iswav)
	{
		fputs(".\nSample Frequency: ",stdout);
		fprintf(stdout,"%u",wav.fmt.SampleRate);
		fputs("\nThe file is in ",stdout);
		fputs(wav.fmt.NumChannels == 2 ? "stereo" : "mono",stdout);
		fputs(".\nThe bit resolution is at ",stdout);
		fputs(wav.fmt.BitsPerSample == 8 ? "8" : (wav.fmt.BitsPerSample == 16 ? "16" : "32"),stdout);
		fputs(" bits to the sample.\nThe file contains ",stdout);
		fprintf(stdout,"%u",wav.data.SubChunk2Size / (wav.fmt.BitsPerSample >> 3) / wav.fmt.NumChannels);
		fputs(" samples.\n",stdout);
	}
	else
	{
		fputs(".\nSample Frequency: ",stdout);
		fprintf(stdout,"%u",su.header.freq);
		fputs("\nThe file is in ",stdout);
		fputs(su.header.flags & SU_STEREO ? "stereo" : "mono",stdout);
		fputs(".\nThe bit resolution is at ",stdout);
		fputs(su.header.flags & SU_8_BIT ? "8" : (su.header.flags & SU_16_BIT ? "16" : "32"),stdout);
		fputs(" bits to the sample.\nThe file contains ",stdout);
		fprintf(stdout,"%u",su.header.num_samples);
		fputs(" samples.\n",stdout);
	}

	if(argc == 2 && fclose(f))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif