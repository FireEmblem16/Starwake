#ifndef _G_BATTERY_C
#define _G_BATTERY_C

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
/* Error when a mono and a stereo file are given together - 10 points */
/* Successful run when two files are given that do not overflow - 60 points */
/* Successful run when three files are given that do not overflow - 15 points */
/* Successful run when four files are given that do not overflow - 15 points */
/* Successful run when two files are given that overflow on at least one sample - 30 points */
double check(PSU_FILE m,c_string expected,uint32_t correctsamplenum,uint32_t testnum);
double* totalfailarray;

int main(int argc, char** argv)
{
	SU_FILE m;
	PFILE f1;
	uint32_t err;
	uint32_t I;
	double total;

	totalfailarray = (double*)malloc(sizeof(double) * 4);
	totalfailarray[0] = 100.0;
	totalfailarray[1] = 20.0;
	totalfailarray[2] = 20.0;
	totalfailarray[3] = 20.0;

	if(argc < 5)
	{
		fputs("To run this program you must provide four parameters.\n",stdout);
		fputs("If the first character in the [i]th parameter is y then the [i]th test will be checked.",stdout);
		fputs("An additional four parameters may be provided to indicate a different file to read than 1.",stdout);

		return EXIT_FAILURE;
	}

	err = 0;
	total = 160.0;

	/* Returns a non-zero value iff the command interpreter is available */
	if(!system((c_string)NULL))
		return EXIT_FAILURE;

	for(I = 1;I < 5;I++)
	{
		uint8_t* cmd1;
		uint8_t* Iascstring;

		switch(I)
		{
		case 1:
			cmd1 = (uint8_t*)"./gensine MONO 777 8 4200 50 1.3 > 1";
			Iascstring = (uint8_t*)"1";
			break;
		case 2:
			cmd1 = (uint8_t*)"./gensine STEREO 923 8 6100 100 0.9 > 1";
			Iascstring = (uint8_t*)"2";
			break;
		case 3:
			cmd1 = (uint8_t*)"./gensine MONO 1123 16 20000 29 0.5 > 1";
			Iascstring = (uint8_t*)"3";
			break;
		case 4:
			cmd1 = (uint8_t*)"./gensine MONO 400 32 8000 90 0.65 > 1";
			Iascstring = (uint8_t*)"4";
			break;
		}

		if(argv[I][0] == 'y');
		{
			system((c_string)cmd1);

			if(argc > 5)
				f1 = fopen(argv[4 + I],"rb");
			else
				f1 = fopen("1","rb");
			
			err = read_su_file(&m,f1);
			fclose(f1);

			if(err)
			{
				if(argc > 5)
					f1 = fopen(argv[4 + I],"rb");
				else
					f1 = fopen("1","rb");

				err = tryagain(&m,f1);
				fclose(f1);

				if(err)
				{
					if(argc > 5)
						f1 = fopen(argv[4 + I],"rb");
					else
						f1 = fopen("1","rb");

					err = tryoncemore(&m,f1);
					fclose(f1);

					fputs("If the score is low for this check the source for data output on ",stdout);
					fputs((c_string)Iascstring,stdout);
					fputs(" as it may deserve more credit than it got.\n",stdout);
				}
			}

			if(!err)
			{
				if(I == 1)
					total -= check(&m,"e1",5460,1);
				else if(I == 2)
					total -= check(&m,"e2",5490,2);
				else if(I == 3)
					total -= check(&m,"e3",20000,3);
				else if(I == 4)
					total -= check(&m,"e4",5200,4);
				
				free((void*)m.samples);
			}

			fprintf(stdout,"Score after test %d: %d\n",I,(uint32_t)total);
		}
	}

	remove("1");
	fprintf(stdout,"Points: %d out of 160. Check for style (30 points). Check for bad amplitude catch (10 points).",(uint32_t)round(total));

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

uint32_t inrange(uint32_t val, uint32_t base, uint32_t range)
{
	uint32_t low,high;

	low = base - range;
	high = base + range;

	if(low > base)
		low = 0;

	if(high < base)
		base = 0xFFFFFFFF;

	if(val > high || val < low)
		return FALSE;

	return TRUE;
}

double check(PSU_FILE m, c_string expected, uint32_t correctsamplenum, uint32_t testnum)
{
	double ret = 0.0;
	
	if(m->header.num_samples < (correctsamplenum >> 1))
		ret += totalfailarray[testnum - 1];
	else
	{
		uint32_t invalids1,invalids3;
		uint32_t i;
		uint32_t err;
		uint32_t min1;
		uint32_t offset1;
		SU_FILE em;
		PFILE f1;
		double mod,mod1,mod3;

		if(m->header.num_samples < correctsamplenum)
			min1 = m->header.num_samples;
		else
			min1 = correctsamplenum;

		f1 = fopen(expected,"r");
		err = read_su_file(&em,f1);
		fclose(f1);

		if(err)
		{
			fprintf(stdout,"Test %d failed to read in the expected file. Fix this now.\n",testnum);
			return totalfailarray[testnum - 1];
		}

		if(m->header.flags & TRY_AGAIN_FLAG || m->header.flags & TRY_ONCE_MORE_FLAG)
			if(em.header.flags & SU_16_BIT)
				m->header.num_samples = m->header.num_samples << 1;
			else if(!(em.header.flags & SU_8_BIT))
				m->header.num_samples = m->header.num_samples << 2;

		/* Dude I am so generous with the range of acceptable value here, you have no idea */
		if(em.header.flags & SU_8_BIT)
		{
			offset1 = m->samples[0] - em.samples[0];

			for(i = 0,invalids1 = 0;i < min1;i++)
				if(!inrange(m->samples[i],em.samples[i] + offset1,8))
					invalids1++;

			for(i = 0,invalids3 = 0;i < min1;i++)
				if(!inrange(m->samples[i],em.samples[i],4))
					invalids3++;
		}
		else if(em.header.flags & SU_16_BIT)
		{
			offset1 = ((uint16_t*)m->samples)[0] - ((uint16_t*)em.samples)[0];

			for(i = 0,invalids1 = 0;i < min1;i++)
				if(!inrange(((uint16_t*)m->samples)[i],((uint16_t*)em.samples)[i] + offset1,8))
					invalids1++;

			for(i = 0,invalids3 = 0;i < min1;i++)
				if(!inrange(((uint16_t*)m->samples)[i],((uint16_t*)em.samples)[i],4))
					invalids3++;
		}
		else
		{
			offset1 = ((uint32_t*)m->samples)[0] - ((uint32_t*)em.samples)[0];

			for(i = 0,invalids1 = 0;i < min1;i++)
				if(!inrange(((uint32_t*)m->samples)[i],((uint32_t*)em.samples)[i] + offset1,8))
					invalids1++;

			for(i = 0,invalids3 = 0;i < min1;i++)
				if(!inrange(((uint32_t*)m->samples)[i],((uint32_t*)em.samples)[i],4))
					invalids3++;
		}

		mod1 = invalids1 / ((double)min1);
		mod3 = invalids3 / ((double)min1);

		mod = mod1;

		if(mod > mod3)
			mod = mod3;

		ret += totalfailarray[testnum - 1] * mod;
	}

	return ret;
}

#endif