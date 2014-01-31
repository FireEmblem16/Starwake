#ifndef _CAPTURE_C
#define _CAPTURE_C

#pragma warning(disable:4996) /* Unsafe function warning */

#include "../Headers/defines.h"
#include "../Headers/util.h"
#include "../Headers/soundconvert.h"
#include "../Headers/wavio.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	WAV_FILE wav;
	uint32_t err;
	uint32_t length;
	uint32_t sps;
	uint32_t nchannels;
	uint32_t bitrate;

	if(argc != 5)
	{
		fputs("This program requires four command line parameters of the following format:\n",stderr);
		fputs("capture [nchan] [sps] [bitrate] [len]\n",stderr);
		fputs("[nchan] should be 2 or 1 for stereo and mono respectively.\n",stderr);
		fputs("[sps] is an integer representing the number of samples per second.\n",stderr);
		fputs("[bitrate] represents the number of bits in a sample and should be 8 or 16.\n",stderr);
		fputs("[len] is an integer representing how long a sample to record in seconds.\n",stderr);

		return EXIT_FAILURE;
	}

	nchannels = atoi(argv[1]);

	/* We don't check for zero because zero is not valid */
	if(!nchannels)
	{
		fputs("The number of channels can only be 1 or 2.\n",stderr);
		return EXIT_FAILURE;
	}

	sps = atoi(argv[2]);

	/* Similarly to the above, zero is not valid */
	if(!sps)
	{
		fputs("The number of samples per second must be a natural number.\n",stderr);
		return EXIT_FAILURE;
	}

	bitrate = atoi(argv[3]);

	if(bitrate != 8 && bitrate != 16 && bitrate != 32)
	{
		fputs("The only available bitrates are 8, 16 and 32.\n",stderr);
		return EXIT_FAILURE;
	}

	length = atoi(argv[4]);

	if(!length)
	{
		fputs("The record time must be a natural number.\n",stderr);
		return EXIT_FAILURE;
	}

	err = record_wav(&wav,(WORD)nchannels,(DWORD)sps,(WORD)bitrate,length);

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

	err = write_wav_file(&wav,stdout);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif