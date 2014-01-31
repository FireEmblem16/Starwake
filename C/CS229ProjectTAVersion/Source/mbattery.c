#ifndef _M_BATTERY_C
#define _M_BATTERY_C

#pragma warning(disable:4996)
#pragma warning(disable:4101)

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
double check(PSU_FILE m,c_string expected,c_string staticexpected,uint32_t correctsamplenum,uint32_t testnum);
double* totalfailarray;

int main(int argc, char** argv)
{
	SU_FILE m;
	PFILE f1;
	uint32_t err;
	uint32_t I;
	double total;

	totalfailarray = (double*)malloc(sizeof(double) * 5);
	totalfailarray[0] = 20.0;
	totalfailarray[1] = 60.0;
	totalfailarray[2] = 30.0;
	totalfailarray[3] = 30.0;
	totalfailarray[4] = 30.0;

	if(argc < 6)
	{
		fputs("To run this program you must provide 5 parameters.\n",stdout);
		fputs("If the first character in the [i]th parameter is y then the [i]th test will be checked.",stdout);
		fputs("An additional five parameters may be provided to indicate a different file to read than 1.",stdout);

		return EXIT_FAILURE;
	}

	err = 0;
	total = 170.0;

	/* Returns a non-zero value iff the command interpreter is available */
	if(!system((c_string)NULL))
		return EXIT_FAILURE;

	for(I = 1;I < 6;I++)
	{
		uint8_t* cmd1;
		uint8_t* Iascstring;

		switch(I)
		{
		case 1:
			cmd1 = (uint8_t*)"./mix a 1.0 b 1.0 > 1";
			Iascstring = (uint8_t*)"1";
			break;
		case 2:
			cmd1 = (uint8_t*)"./mix b 1.0 c 2.0 > 1";
			Iascstring = (uint8_t*)"2";
			break;
		case 3:
			cmd1 = (uint8_t*)"./mix b 1.0 c 1.5 d 1.0 > 1";
			Iascstring = (uint8_t*)"3";
			break;
		case 4:
			cmd1 = (uint8_t*)"./mix b 0.05 c 3.0 d 1.1 e 3.5 > 1";
			Iascstring = (uint8_t*)"4";
			break;
		case 5:
			cmd1 = (uint8_t*)"./mix f 4.0 g 5.0 > 1";
			Iascstring = (uint8_t*)"5";
			break;
		}

		if(argv[I][0] == 'y');
		{
			system((c_string)cmd1);

			if(argc > 6)
				f1 = fopen(argv[5 + I],"rb");
			else
				f1 = fopen("1","rb");
			
			err = read_su_file(&m,f1);
			fclose(f1);

			if(err && I != 1) /* If I is 1 we are expecting an error and to not error is a problem */
			{
				if(argc > 6)
					f1 = fopen(argv[5 + I],"rb");
				else
					f1 = fopen("1","rb");

				err = tryagain(&m,f1);
				fclose(f1);

				if(err)
				{
					if(argc > 6)
						f1 = fopen(argv[5 + I],"rb");
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
					total -= totalfailarray[0];
				else if(I == 2)
					total -= check(&m,"e2","s2",8000,2);
				else if(I == 3)
					total -= check(&m,"e3","s3",8000,3);
				else if(I == 4)
					total -= check(&m,"e4","s4",8000,4);
				else if(I == 5)
					total -= check(&m,"e5","s5",8000,5);
				
				free((void*)m.samples);
			}
			else if(I != 1)
				total -= totalfailarray[I - 1];

			fprintf(stdout,"Score after test %d: %d\n",I,(uint32_t)total);
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

double check(PSU_FILE m, c_string expected, c_string staticexpected, uint32_t correctsamplenum, uint32_t testnum)
{
	double ret = 0.0;
	modifysamplenum(m);

	if(m->header.num_samples < (correctsamplenum >> 1))
		ret += totalfailarray[testnum - 1];
	else
	{
		uint32_t invalids,invalids1,invalids2,invalids3,invalids4;
		uint32_t i;
		uint32_t err;
		uint32_t min1,min2;
		uint32_t offset1,offset2;
		SU_FILE em,esm;
		PFILE f1,f2;
		double mod,mod1,mod2,mod3,mod4;

		if(m->header.num_samples < correctsamplenum)
			min1 = m->header.num_samples;
		else
			min1 = correctsamplenum;

		if(m->header.num_samples < correctsamplenum)
			min2 = m->header.num_samples;
		else
			min2 = correctsamplenum;

		f1 = fopen(expected,"r");
		f2 = fopen(staticexpected,"r");

		err = read_su_file(&em,f1);
		err += read_su_file(&esm,f2);

		fclose(f1);
		fclose(f2);

		if(err)
		{
			fprintf(stdout,"Test %d failed to read in the expected files. Fix this now.\n",testnum);
			return totalfailarray[testnum - 1];
		}

		/* If is resonable for the purposes of this project to allow the values to be moved up or down */
		offset1 = m->samples[0] - em.samples[0];
		offset2 = m->samples[0] - esm.samples[0];

		/* We will also be tolerant of off by one errors (including the first sample error in offset) giving THREE (FIVE) values that are allowed */
		for(i = 0,invalids1 = 0;i < min1;i++)
			if(!inrange(m->samples[i],em.samples[i] + offset1,2))
				invalids1++;

		for(i = 0,invalids2 = 0;i < min2;i++)
			if(!inrange(m->samples[i],esm.samples[i] + offset2,2))
				invalids2++;

		/* This should be unnecessary as offset should always be zero in this case but in case the first value of the file was messed up check anyways */
		/* If someone gets extrodinarily lucky, well whatever */
		for(i = 0,invalids3 = 0;i < min1;i++)
			if(!inrange(m->samples[i],em.samples[i],1))
				invalids3++;

		for(i = 0,invalids4 = 0;i < min2;i++)
			if(!inrange(m->samples[i],esm.samples[i],1))
				invalids4++;

		mod1 = invalids1 / ((double)min1);
		mod2 = invalids2 / ((double)min2);
		mod3 = invalids3 / ((double)min1);
		mod4 = invalids4 / ((double)min2);

		mod = mod1;

		if(mod > mod2)
			mod = mod2;

		if(mod > mod3)
			mod = mod3;
		
		if(mod > mod4)
			mod = mod4;

		fprintf(stdout,"%d: %.3f %d: %.3f %d: %.3f %d: %.3f mod: %.3f\n",1,mod1,2,mod2,3,mod3,4,mod4,mod);
		ret += totalfailarray[testnum - 1] * mod;
	}

	return ret;
}

#endif