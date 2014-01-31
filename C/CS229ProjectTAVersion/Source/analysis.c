#ifndef _ANALYSIS_C
#define _ANALYSIS_C

#pragma warning(disable:4018)
#pragma warning(disable:4996)
#pragma warning(disable:4244)

#include <stdio.h>
#include <string.h>
#include "../Headers/defines.h"
#include "../Headers/util.h"
#include "../Headers/soundanalysis.h"
#include "../Headers/suio.h"
#include "../Headers/wavio.h"

void printusage();

int main(int argc, char** argv)
{
	void* test_data;
	void* known_data;
	uint32_t test_len;
	uint32_t known_len;
	uint8_t data_bitsize;
	uint32_t min_len;

	c_string test_file;
	c_string test_format;
	uint8_t test_endianness;
	uint8_t test_mono;
	c_string known_file;
	c_string known_format;
	uint8_t known_endianness;
	uint8_t known_mono;

	uint8_t density;

	uint8_t* p;
	uint32_t offset;

	if(argc < 6 || argc > 8)
	{
		printusage();
		return EXIT_FAILURE;
	}

	test_file = argv[1];
	known_file = argv[2];

	test_format = '\0';
	test_endianness = '\0';
	known_format = '\0';
	known_endianness = '\0';
	offset = 0;

	test_format = argv[3];

	for(p = (uint8_t*)test_format;*p;p++)
		*p &= ~0x20;

	/* All format strings are garunteed to be at least two in length */
	if(argv[4][1] == '\0')
	{
		if((argv[4][0] & ~0x20) == 'B')
			test_endianness = 'B';
		else if((argv[4][0] & ~0x20) == 'L')
			test_endianness = 'L';
		else
		{
			printusage();
			return EXIT_FAILURE;
		}

		++offset;
	}
	else
		test_endianness = 'L';

	/* This doesn't matter until after the next paramter but we might as well check it now */
	if(argc < 6 + offset)
	{
		printusage();
		return EXIT_SUCCESS;
	}

	known_format = argv[4 + offset];

	for(p = (uint8_t*)known_format;*p;p++)
		*p &= ~0x20;

	/* All format strings are garunteed to be at least two in length */
	if(argv[5 + offset][1] == '\0')
	{
		if((argv[5 + offset][0] & ~0x20) == 'B')
			known_endianness = 'B';
		else if((argv[5 + offset][0] & ~0x20) == 'L')
			known_endianness = 'L';
		else if(argc == 6 + offset && ((argv[5 + offset][0] & ~0x20) == 'C' || (argv[5 + offset][0] & ~0x20) == 'D'))
		{
			/* We need this catch to ensure that we don't trip up on the last parameter */
			--offset;
			known_endianness = 'L';
		}
		else
		{
			printusage();
			return EXIT_FAILURE;
		}

		++offset;
	}
	else
		known_endianness = 'L';

	/* We check again because offset might have changed */
	if(argc < 6 + offset)
	{
		printusage();
		return EXIT_SUCCESS;
	}

	density = argv[5 + offset][0] & ~0x20;

	if(density != 'C' && density != 'D')
	{
		printusage();
		return EXIT_SUCCESS;
	}

	if(!strcmp(test_format,"SU\0"))
	{
		SU_FILE su;
		PFILE f;
		uint32_t err;

		f = fopen(test_file,"rb");
		err = read_su_file(&su,f);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		if(fclose(f))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

		test_mono = su.header.flags & SU_STEREO ? 1 : 0;
		test_len = su.header.num_samples * (test_mono ? 2 : 1);
		data_bitsize = su.header.flags & SU_8_BIT ? 8 : (su.header.flags & SU_16_BIT ? 16 : 32);
		test_data = (void*)su.samples;

		if(test_endianness == 'B')
		{
			err = flip_endian(&su);

			if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}
		}
	}
	else if(!strcmp(test_format,"WAV\0"))
	{
		WAV_FILE wav;
		PFILE f;
		uint32_t err;

		f = fopen(test_file,"rb");
		err = read_wav_file(&wav,f);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		if(fclose(f))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

		test_mono = wav.fmt.NumChannels - 1;
		test_len = wav.data.SubChunk2Size / (wav.fmt.BitsPerSample >> 3);
		data_bitsize = wav.fmt.BitsPerSample;
		test_data = (void*)wav.data.data;

		if(test_endianness == 'B')
		{
			fputs("Flipping the endianness of wav files is not supported.\n",stderr);
			return EXIT_FAILURE;
		}
	}
	else
	{
		fputs("The value for the test file's format was not supported.\n",stderr);
		return EXIT_FAILURE;
	}

	if(!strcmp(known_format,"SU\0"))
	{
		SU_FILE su;
		PFILE f;
		uint32_t err;

		f = fopen(known_file,"rb");
		err = read_su_file(&su,f);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		if(fclose(f))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

		known_mono = su.header.flags & SU_STEREO ? 1 : 0;
		known_len = su.header.num_samples * (known_mono ? 2 : 1);
		known_data = (void*)su.samples;

		if(data_bitsize != (su.header.flags & SU_8_BIT ? 8 : (su.header.flags & SU_16_BIT ? 16 : 32)))
		{
			fputs("The bitsize of both files must be the same in order to compare them.\n",stderr);
			return EXIT_FAILURE;
		}

		if(known_endianness == 'B')
		{
			err = flip_endian(&su);

			if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}
		}
	}
	else if(!strcmp(known_format,"WAV\0"))
	{
		WAV_FILE wav;
		PFILE f;
		uint32_t err;

		f = fopen(known_file,"rb");
		err = read_wav_file(&wav,f);

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		if(fclose(f))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

		known_mono = wav.fmt.NumChannels - 1;
		known_len = wav.data.SubChunk2Size / (wav.fmt.BitsPerSample >> 3);
		known_data = (void*)wav.data.data;

		if(data_bitsize != wav.fmt.BitsPerSample)
		{
			fputs("The bitsize of both files must be the same in order to compare them.\n",stderr);
			return EXIT_FAILURE;
		}

		if(known_endianness == 'B')
		{
			fputs("Flipping the endianness of wav files is not supported.\n",stderr);
			return EXIT_FAILURE;
		}
	}
	else
	{
		fputs("The value for the known file's format was not supported.\n",stderr);
		return EXIT_FAILURE;
	}

	if(known_mono != test_mono)
	{
		fputs("Both files must be either mono or stereo to compare them.\n",stderr);
		return EXIT_FAILURE;
	}

	if(test_len > known_len)
		min_len = known_len;
	else
		min_len = test_len;

	if(density == 'C')
	{
		uint32_t offset;

		fprintf(stdout,"%d ",known_len - test_len);
		fprintf(stdout,"%d ",s_calc_matching_samples(known_data,test_data,0,min_len,data_bitsize));

		/* We can ignore sign here so don't let it bother you */
		switch(data_bitsize)
		{
		case 8:
			offset = ((uint8_t*)known_data)[0] - ((uint8_t*)test_data)[0];
			break;
		case 16:
			offset = ((uint16_t*)known_data)[0] - ((uint16_t*)test_data)[0];
			break;
		case 32:
			offset = ((uint32_t*)known_data)[0] - ((uint32_t*)test_data)[0];
			break;
		}

		fprintf(stdout,"%d %d ",offset,s_calc_matching_samples(known_data,test_data,offset,min_len,data_bitsize));

		fprintf(stdout,"%d ",known_len);
		fprintf(stdout,"%f ",s_calc_mean(known_data,min_len,data_bitsize));
		fprintf(stdout,"%f ",s_calc_varaince(known_data,min_len,data_bitsize));

		fprintf(stdout,"%d ",test_len);
		fprintf(stdout,"%f ",s_calc_mean(test_data,min_len,data_bitsize));
		fprintf(stdout,"%f ",s_calc_varaince(test_data,min_len,data_bitsize));

		fprintf(stdout,"%f ",s_calc_covariance(known_data,test_data,min_len,data_bitsize));
		fprintf(stdout,"%f ",s_calc_X_minus_Y_variance(known_data,test_data,min_len,data_bitsize));
	}
	else
	{
		uint32_t offset;

		fprintf(stdout,"All calculations are based on the minimum of two file's sample numbers.\n\n");

		fprintf(stdout,"Difference in number of samples in known file and in test file: %d\n",known_len - test_len);
		fprintf(stdout,"Number of matches in known file and test file: %d\n",s_calc_matching_samples(known_data,test_data,0,min_len,data_bitsize));

		/* We can ignore sign here so don't let it bother you */
		switch(data_bitsize)
		{
		case 8:
			offset = ((uint8_t*)known_data)[0] - ((uint8_t*)test_data)[0];
			break;
		case 16:
			offset = ((uint16_t*)known_data)[0] - ((uint16_t*)test_data)[0];
			break;
		case 32:
			offset = ((uint32_t*)known_data)[0] - ((uint32_t*)test_data)[0];
			break;
		}

		fprintf(stdout,"Number of matches in known file and test file with offset %d: %d\n\n",offset,s_calc_matching_samples(known_data,test_data,offset,min_len,data_bitsize));

		fprintf(stdout,"Number of samples in known file: %d\n",known_len);
		fprintf(stdout,"Mean of known file: %f\n",s_calc_mean(known_data,min_len,data_bitsize));
		fprintf(stdout,"Variance of known file: %f\n\n",s_calc_varaince(known_data,min_len,data_bitsize));

		fprintf(stdout,"Number of samples in test file: %d\n",test_len);
		fprintf(stdout,"Mean of test file: %f\n",s_calc_mean(test_data,min_len,data_bitsize));
		fprintf(stdout,"Variance of test file: %f\n\n",s_calc_varaince(test_data,min_len,data_bitsize));

		fprintf(stdout,"Covariance of known file and test file: %f\n",s_calc_covariance(known_data,test_data,min_len,data_bitsize));
		fprintf(stdout,"Variance of known file minus test file: %f\n",s_calc_X_minus_Y_variance(known_data,test_data,min_len,data_bitsize));
	}

	return EXIT_SUCCESS;
}

void printusage()
{
	fputs("This program takes five to seven command line parameters of the form:\n",stderr);
	fputs("analysis [test] [known] [format1 (endianness)] [format2 (endianness)] [c/d]\n\n",stderr);
	fputs("[test] should be the file path to compare to.\n",stderr);
	fputs("[known] should be the file path with correct data.\n\n",stderr);
	fputs("[format1] and [format2] specifies the formats of the data for their files.\n",stderr);
	fputs("Supported formats (case insenstivie) are: SU and WAV\n\n",stderr);
	fputs("Endianness may be provided as 'b' or 'l' but 'l'.\n",stderr);
	fputs("'l' is assumed if absent and the file does not internally sepcify.\n\n",stderr);
	fputs("The final parameter should be c or d, case insensitive.\n",stderr);
	fputs("If c is specified the output will be written as only numbers packed on\n one line with a single space in between each value.\n",stderr);
	fputs("If d is specified the output will be written to be human readable with\n descriptive tags and new lines between data.\n",stderr);

	return;
}

#endif