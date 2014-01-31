#ifndef _GENSINE_C
#define _GENSINE_C

#pragma warning(disable:4996)

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "../Headers/defines.h"
#include "../Headers/suio.h"
#include "../Headers/util.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	SU_FILE su;
	uint32_t i;
	uint32_t err;
	uint32_t atoi_store;
	uint32_t sine_freq;
	double t;
	double amplitude;
	double duration;
	double increment;
	double omega;
	
	/* Round two for this project allows for three parameters: frequency, [WAV/CS229] and duration */
	/* As we are lazy we will just do some shinanigans */
	if(argc == 4)
	{
		uint8_t format = 0; /* 0 - undef, 1 - su, 2 - wav */
		uint8_t cmd[1024];
		PFILE f;

		if(!(atoi_store = atoi((c_string)argv[1])) && !is_zero_decimal((c_string)argv[1]))
		{
			fputs("You have either entered a non-number or a zero for frequency which must be a positive integer.\n",stderr);
			return EXIT_FAILURE;
		}

		for(i = 0;argv[2][i];i++)
			if(isalpha(argv[2][i]))
				argv[2][i] &= ~0x20;

		if(!strcmp((c_string)argv[2],"WAV"))
			format = 2;
		else if(!strcmp((c_string)argv[2],"CS229"))
			format = 1;
		else
		{
			fputs("Unable to recognize format.\n",stderr);
			return EXIT_FAILURE;
		}

		duration = atof((c_string)argv[3]);

#ifdef WINDOWS
		if(duration == HUGE_VAL)
#else
		if(duration == HUGE_VAL())
#endif
		{
			fputs("You have provided a number well beyond the limits of a double value.\n",stderr);
			return EXIT_FAILURE;
		}
		else if(duration < 0.0)
		{
			fputs("You have provided a negative number for duration which must be a non-negative real number.\n",stderr);
			return EXIT_FAILURE;
		}
		else if(duration == 0.0)
			if(!is_zero_real((c_string)argv[3]))
			{
				fputs("You have provided a non-number for duration which must be a non-negative real number.\n",stderr);
				return EXIT_FAILURE;
			}

		cmd[0] = '\0';
#ifndef WINDOWS
		strcat(cmd,"./");	
#endif
		strcat(cmd,"\"");
		strcat(cmd,argv[0]);
		strcat(cmd,"\"");
		strcat(cmd," MONO ");
		strcat(cmd,argv[1]);
		strcat(cmd," 8 8000 101 ");
		strcat(cmd,argv[3]);
		strcat(cmd," > temp");

		system(cmd);

		if(format == 2)
		{
#ifdef WINDOWS
			system("convertsu < temp > temp2");
#else
			system("./convertsu < temp > temp2");
#endif

			f = fopen("temp2","rb");

			while(!feof(f))
			{
				uint8_t c;
				c = fgetc(f);
				
				if(!feof(f))
					fputc(c,stdout);
			}

			fclose(f);
			system("rm temp temp2");
		}
		else
		{
			f = fopen("temp","rb");

			while(!feof(f))
			{
				uint8_t c;
				c = fgetc(f);
				
				if(!feof(f))
					fputc(c,stdout);
			}

			fclose(f);
			system("rm temp");
		}

		return EXIT_SUCCESS;
	}

	if(argc != 7)
	{
		fputs("This program requires six command line arguments of the form:\n",stderr);
		fputs("gensine <MONO | STEREO> <frequency> <bit-size> <sample rate> <amplitude> <duration>\n",stderr);
		return EXIT_FAILURE;
	}

	/* CAPSLOCK*/
	for(i = 0;argv[1][i];i++)
		argv[1][i] &= ~0x20;

	su.header.flags = 0;

	if(!strcmp("MONO\0",(c_string)argv[1]))
		su.header.flags &= ~SU_STEREO;
	else if(!strcmp("STEREO\0",(c_string)argv[1]))
		su.header.flags |= SU_STEREO;
	else
	{
		fputs("The parameter for channel mode must be either mono or stereo, case insenstive.\n",stderr);
		return EXIT_FAILURE;
	}

	if(!(atoi_store = atoi((c_string)argv[2])))
	{
		fputs("You have either entered a non-number or a zero for frequency which must be a positive integer.\n",stderr);
		return EXIT_FAILURE;
	}

	sine_freq = atoi_store;

	if(!(atoi_store = atoi((c_string)argv[3])))
	{
		fputs("Bit resolution must be 8, 16 or 32.\n",stderr);
		return EXIT_FAILURE;
	}

	if(atoi_store == 8)
		su.header.flags |= SU_8_BIT;
	else if(atoi_store == 16)
		su.header.flags |= SU_16_BIT;
	else if(atoi_store == 32)
		su.header.flags &= ~(SU_8_BIT | SU_16_BIT);
	else
	{
		fputs("Bit resolution must be 8, 16 or 32.\n",stderr);
		return EXIT_FAILURE;
	}

	duration = atof((c_string)argv[6]);

#ifdef WINDOWS
	if(duration == HUGE_VAL)
#else
	if(duration == HUGE_VAL())
#endif
	{
		fputs("You have provided a number well beyond the limits of a double value.\n",stderr);
		return EXIT_FAILURE;
	}
	else if(duration < 0.0)
	{
		fputs("You have provided a negative number for duration which must be a non-negative real number.\n",stderr);
		return EXIT_FAILURE;
	}
	else if(duration == 0.0)
		if(!is_zero_real((c_string)argv[6]))
		{
			fputs("You have provided a non-number for duration which must be a non-negative real number.\n",stderr);
			return EXIT_FAILURE;
		}

	if(!(atoi_store = atoi((c_string)argv[4])))
	{
		fputs("You have either entered a non-number or a zero for sample rate which must be a positive integer.\n",stderr);
		return EXIT_FAILURE;
	}

	su.header.freq = atoi_store;

	if(!(atoi_store = atoi((c_string)argv[5])))
		if(!is_zero_decimal((c_string)argv[5]))
		{
			fputs("You have either entered a non-number for amplitude which must be a non-negative integer.\n",stderr);
			return EXIT_FAILURE;
		}

	amplitude = (double)atoi_store;

	if(amplitude > (su.header.flags & SU_8_BIT ? 0xFF >> 1 : (su.header.flags & SU_16_BIT ? 0xFFFF >> 1 : 0xFFFFFFFF >> 1)))
	{
		fputs("The given amplitude is higher than the given bit resolution will allow.\n",stderr);
		return EXIT_FAILURE;
	}

	su.header.num_samples = (uint32_t)(duration * su.header.freq);
	i = su.header.flags & SU_8_BIT ? su.header.num_samples : (su.header.flags & SU_16_BIT ? su.header.num_samples << 1 : su.header.num_samples << 2);
	i = su.header.flags & SU_STEREO ? i << 1 : i;
	su.samples = (uint8_t*)malloc(i);

	omega = 2.0 * PI * sine_freq; /* u(x,t) = A*sin(omega*t+k*x), x is fixed at zero*/
	increment = 1.0 / (double)su.header.freq; /* When we go one second we should have [su.header.freq] samples already taken*/

	if(su.header.flags & SU_8_BIT)
	{
		double center;
		center = 127.0;

		for(i = 0,t = 0.0;i < su.header.num_samples;i++,t += increment)
			if(su.header.flags & SU_STEREO)
			{
				su.samples[i << 1] = (uint8_t)round(center + amplitude * sin(omega * t));
				su.samples[(i << 1) + 1] = (uint8_t)round(center + amplitude * sin(omega * t));
			}
			else
				su.samples[i] = (uint8_t)round(center + amplitude * sin(omega * t));
	}
	else if(su.header.flags & SU_16_BIT)
	{
		double center;
		center = 32767.0;

		for(i = 0,t = 0.0;i < su.header.num_samples;i++,t += increment)
			if(su.header.flags & SU_STEREO)
			{
				((uint16_t*)su.samples)[i << 1] = (uint16_t)round(center + amplitude * sin(omega * t));
				((uint16_t*)su.samples)[(i << 1) + 1] = (uint16_t)round(center + amplitude * sin(omega * t));
			}
			else
				((uint16_t*)su.samples)[i] = (uint16_t)round(center + amplitude * sin(omega * t));
	}
	else
	{
		double center;
		center = 2147483647.0;

		for(i = 0,t = 0.0;i < su.header.num_samples;i++,t += increment)
			if(su.header.flags & SU_STEREO)
			{
				((uint32_t*)su.samples)[i << 1] = (uint32_t)round(center + amplitude * sin(omega * t));
				((uint32_t*)su.samples)[(i << 1) + 1] = (uint32_t)round(center + amplitude * sin(omega * t));
			}
			else
				((uint32_t*)su.samples)[i] = (uint32_t)round(center + amplitude * sin(omega * t));
	}

#ifdef WINDOWS
	_setmode(_fileno(stdout),_O_BINARY);
#else
	stdout = freopen(NULL,"wb",stdout);
#endif

	err = flip_endian(&su);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = write_su_file(&su,stdout);
	
	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}

#endif