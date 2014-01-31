#ifndef _BATTERY_C
#define _BATTERY_C

#pragma warning(disable:4996)

#define MAX_TEST_SIZE 160000
#define TRY_AGAIN_FLAG 0xA0
#define TRY_ONCE_MORE_FLAG 0x50

#define SU_MONO 0x00
#define SU_32_BIT 0x00

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"

uint32_t tryagain(PSU_FILE dest,PFILE in);
uint32_t tryoncemore(PSU_FILE dest,PFILE in);

/* These four output how many points are lost, not gained */
/* Correct number of samples - 20 points */
/* Lose 3 points if off by one */
/* Correct data - 20 points */
/* Expressed as a percentage of correct data for the number of samples present */
/* Requires at least 50% of the expected number of samples to be present */
double checkone(PSU_FILE s);
double checktwo(PSU_FILE s);
double checkthree(PSU_FILE s);
double checkfour(PSU_FILE s);

int main(int argc, char** argv)
{
	SU_FILE s;
	PFILE f;
	uint32_t err,I;
	double total;

	if(argc < 5)
	{
		fputs("To run this program you must provide 4 parameters.\n",stdout);
		fputs("If the first character in the [i]th parameter is y then gentest[i] will be checked.",stdout);
		fputs("An additional four parameters may be provided to indicate a different file to read than 1.",stdout);

		return EXIT_FAILURE;
	}

	err = 0;
	total = 170.0;

	/* Returns a non-zero value iff the command interpreter is available */
	if(!system((c_string)NULL))
		return EXIT_FAILURE;

	for(I = 1;I < 5;I++)
	{
		uint8_t* gentestcmd;
		uint8_t* Iascstring;

		switch(I)
		{
		case 1:
			gentestcmd = (uint8_t*)"./gentest1 > 1";
			Iascstring = (uint8_t*)"1";
			break;
		case 2:
			gentestcmd = (uint8_t*)"./gentest2 > 1";
			Iascstring = (uint8_t*)"2";
			break;
		case 3:
			gentestcmd = (uint8_t*)"./gentest3 > 1";
			Iascstring = (uint8_t*)"3";
			break;
		case 4:
			gentestcmd = (uint8_t*)"./gentest4 > 1";
			Iascstring = (uint8_t*)"4";
			break;
		}

		if(argv[I][0] == 'y');
		{
			system((c_string)gentestcmd);

			if(argc > 5)
				f = fopen(argv[4 + I],"rb");
			else
				f = fopen("1","rb");
			
			err = read_su_file(&s,f);
			fclose(f);

			if(err)
			{
				total -= 2.5;
				
				if(argc > 5)
					f = fopen(argv[4 + I],"rb");
				else
					f = fopen("1","rb");

				err = tryagain(&s,f);
				fclose(f);
			}

			if(err)
			{
				if(argc > 5)
					f = fopen(argv[4 + I],"rb");
				else
					f = fopen("1","rb");

				err = tryoncemore(&s,f);
				fclose(f);

				fputs("If the score is low for this check the source for data output on ",stdout);
				fputs((c_string)Iascstring,stdout);
				fputs(" as it may deserve more credit than it got.\n",stdout);

				if(err)
					total -= 42.5;
				else
				{
					if(I == 1)
						total -= checkone(&s);
					else if(I == 2)
						total -= checktwo(&s);
					else if(I == 3)
						total -= checkthree(&s);
					else if(I == 4)
						total -= checkfour(&s);
					
					free((void*)s.samples);
				}
			}
			else
			{
				if(I == 1)
					total -= checkone(&s);
				else if(I == 2)
					total -= checktwo(&s);
				else if(I == 3)
					total -= checkthree(&s);
				else if(I == 4)
					total -= checkfour(&s);

				free((void*)s.samples);
			}
		}
	}

	remove("1");
	fprintf(stdout,"Points: %d out of 170. Check for style (30 points).",(uint32_t)round(total));

	return EXIT_SUCCESS;
}

/* Should exit with all memory freed if it errors. */
uint32_t tryagain(PSU_FILE dest, PFILE in)
{
	uint32_t b,q;
	c_string trigger = "ENDHEADER\0";
	b = 1;

	if(in == (PFILE)BAD_PTR || in == (PFILE)NULL || in == (PFILE)UNDEF_PTR || dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)NULL || dest == (PSU_FILE)UNDEF_PTR)
		return FAILURE;

	while(!feof(in) && b)
	{
		uint8_t c;
		
		if(b == 1)
			c = fgetc(in) & ~0x20;
		else
		{
			b = 1;
			c = trigger[0];
		}

		if(c == trigger[0])
		{
			uint8_t i;

			for(i = 0;i < strlen(trigger);i++,c = fgetc(in) & ~0x20)
			{
				if(c != trigger[i])
				{
					if(c == trigger[0])
						b = 2;

					break;
				}
				
				if(i == strlen(trigger) - 1)
				{
					b = 0;
					break; /* Skip the fgetc */
				}
			}
		}
	}

	if(b != 0)
		return FAILURE;

	dest->header.flags = TRY_AGAIN_FLAG;
	dest->samples = (uint8_t*)malloc(MAX_TEST_SIZE);
	q = fgetc(in);

	if(q != '\n')
	{
		dest->header.num_samples = 1;
		dest->samples[0] = q;

		fputs("ENDHEADER does not have a newline after it. Be skeptical of off by one errors.\n",stdout);
	}
	else
		dest->header.num_samples = 0;

	for(;!feof(in) && dest->header.num_samples < MAX_TEST_SIZE;)
		dest->samples[dest->header.num_samples++] = (uint8_t)fgetc(in);
	
	for(;!feof(in);dest->header.num_samples++)
		fgetc(in);

	/* We have to read the end of file character to get the eof flag to be set */
	dest->header.num_samples--;

	return SUCCESS;
}

uint32_t tryoncemore(PSU_FILE dest,PFILE in)
{
	dest->header.flags = TRY_ONCE_MORE_FLAG;
	dest->samples = (uint8_t*)malloc(MAX_TEST_SIZE);
	dest->header.num_samples = 0;

	for(;!feof(in) && dest->header.num_samples < MAX_TEST_SIZE;)
		dest->samples[dest->header.num_samples++] = (uint8_t)fgetc(in);
	
	for(;!feof(in);dest->header.num_samples++)
		fgetc(in);

	/* We have to read the end of file character to get the eof flag to be set */
	dest->header.num_samples--;

	return SUCCESS;
}

void modifysamplenum(PSU_FILE s)
{
	if(s->header.flags != TRY_AGAIN_FLAG && s->header.flags != TRY_ONCE_MORE_FLAG)
	{
		if(s->header.flags & SU_STEREO)
			s->header.num_samples = s->header.num_samples << 1;

		if(s->header.flags & SU_16_BIT)
			s->header.num_samples = s->header.num_samples << 1;
		else if(!(s->header.flags & SU_8_BIT))
			s->header.num_samples = s->header.num_samples << 2;
	}

	return;
}

double checknumsamples(uint32_t num, uint32_t correct)
{
	if(num != correct)
		if(num == correct - 1 || num == correct + 1)
			return 3.0;
		else
			return 20.0;

	return 0.0;
}

double checkheaderdata(PSU_FILE s, uint32_t cbr, uint32_t cf, uint32_t cc)
{
	double ret = 0.0;

	if(s->header.flags != TRY_AGAIN_FLAG && s->header.flags != TRY_ONCE_MORE_FLAG)
	{
		if((s->header.flags & (SU_8_BIT | SU_16_BIT)) != cbr)
			ret += 2.5 / 3.0;

		if(s->header.freq != cf)
			ret += 2.5 / 3.0;

		if((s->header.flags & SU_STEREO) != cc)
			ret += 2.5 / 3.0;
	}

	return ret;
}

double checkone(PSU_FILE s)
{
#define CORRECT_SAMPLE_NUM 10000
#define CORRECT_BIT_RES SU_8_BIT
#define CORRECT_FREQ 8000
#define CORRECT_CHANNEL SU_MONO
	double ret = 0.0;

	modifysamplenum(s);
	ret += checknumsamples(s->header.num_samples,CORRECT_SAMPLE_NUM);

	if(s->header.num_samples < (CORRECT_SAMPLE_NUM >> 1))
		ret += 20.0;
	else
	{
		uint32_t invalids;
		uint32_t i;
		uint32_t min;
		
		if(s->header.num_samples < CORRECT_SAMPLE_NUM)
			min = s->header.num_samples;
		else
			min = CORRECT_SAMPLE_NUM;

		for(i = 0,invalids = 0;i < min;i++)
			if(s->samples[i] != 0)
				invalids++;

		ret += 20.0 * invalids / ((double)min);
	}

	ret += checkheaderdata(s,CORRECT_BIT_RES,CORRECT_FREQ,CORRECT_CHANNEL);

	return ret;
#undef CORRECT_SAMPLE_NUM
#undef CORRECT_BIT_RES
#undef CORRECT_FREQ
#undef CORRECT_CHANNEL
}

double checktwo(PSU_FILE s)
{
#define CORRECT_SAMPLE_NUM 4000
#define CORRECT_BIT_RES SU_8_BIT
#define CORRECT_FREQ 16000
#define CORRECT_CHANNEL SU_STEREO
	double ret = 0.0;

	modifysamplenum(s);
	ret += checknumsamples(s->header.num_samples,CORRECT_SAMPLE_NUM);

	if(s->header.num_samples < (CORRECT_SAMPLE_NUM >> 1))
		ret += 20.0;
	else
	{
		uint32_t invalids;
		uint32_t i;
		uint32_t min;
		
		if(s->header.num_samples < CORRECT_SAMPLE_NUM)
			min = s->header.num_samples;
		else
			min = CORRECT_SAMPLE_NUM;

		for(i = 0,invalids = 0;i < min;i++)
			if(i % 2 == 0 && s->samples[i] != 0)
				invalids++;
			else if(i % 2 == 1 && s->samples[i] != 200)
				invalids++;

		ret += 20.0 * invalids / ((double)min);
	}

	ret += checkheaderdata(s,CORRECT_BIT_RES,CORRECT_FREQ,CORRECT_CHANNEL);

	return ret;
#undef CORRECT_SAMPLE_NUM
#undef CORRECT_BIT_RES
#undef CORRECT_FREQ
#undef CORRECT_CHANNEL
}

double checkthree(PSU_FILE s)
{
#define CORRECT_SAMPLE_NUM 160000
#define CORRECT_BIT_RES SU_32_BIT
#define CORRECT_FREQ 8000
#define CORRECT_CHANNEL SU_MONO
	double ret = 0.0;

	modifysamplenum(s);
	ret += checknumsamples(s->header.num_samples,CORRECT_SAMPLE_NUM);

	if(s->header.num_samples < (CORRECT_SAMPLE_NUM >> 1))
		ret += 20.0;
	else
	{
		uint32_t invalids;
		uint32_t i;
		uint32_t min;
		
		if(s->header.num_samples < CORRECT_SAMPLE_NUM)
			min = s->header.num_samples;
		else
			min = CORRECT_SAMPLE_NUM;

		for(i = 0,invalids = 0;i < min;i++)
		{
			switch(i % 4)
			{
			case 0:
				if(s->samples[i] != 20)
					invalids++;

				break;
			case 1:
			case 2:
			case 3:
				if(s->samples[i] != 0)
					invalids++;

				break;
			}
		}

		ret += 20.0 * invalids / ((double)min);
	}

	ret += checkheaderdata(s,CORRECT_BIT_RES,CORRECT_FREQ,CORRECT_CHANNEL);

	return ret;
#undef CORRECT_SAMPLE_NUM
#undef CORRECT_BIT_RES
#undef CORRECT_FREQ
#undef CORRECT_CHANNEL
}

double checkfour(PSU_FILE s)
{
#define CORRECT_SAMPLE_NUM 80000
#define CORRECT_BIT_RES SU_16_BIT
#define CORRECT_FREQ 44100
#define CORRECT_CHANNEL SU_STEREO
	double ret = 0.0;

	modifysamplenum(s);
	ret += checknumsamples(s->header.num_samples,CORRECT_SAMPLE_NUM);

	if(s->header.num_samples < (CORRECT_SAMPLE_NUM >> 1))
		ret += 20.0;
	else
	{
		uint32_t invalids;
		uint32_t i;
		uint32_t min;
		
		if(s->header.num_samples < CORRECT_SAMPLE_NUM)
			min = s->header.num_samples;
		else
			min = CORRECT_SAMPLE_NUM;

		for(i = 0,invalids = 0;i < min;i++)
		{
			switch(i % 4)
			{
			case 0:
				if(s->samples[i] != 0xE8)
					invalids++;

				break;
			case 1:
				if(s->samples[i] != 0x03)
					invalids++;

				break;
			case 2:
				if(s->samples[i] != 0x92)
					invalids++;

				break;
			case 3:
				if(s->samples[i] != 0x10)
					invalids++;

				break;
			}
		}

		ret += 20.0 * invalids / ((double)min);
	}

	ret += checkheaderdata(s,CORRECT_BIT_RES,CORRECT_FREQ,CORRECT_CHANNEL);

	return ret;
#undef CORRECT_SAMPLE_NUM
#undef CORRECT_BIT_RES
#undef CORRECT_FREQ
#undef CORRECT_CHANNEL
}

#endif