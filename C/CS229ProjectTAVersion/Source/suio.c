#ifndef _SU_C
#define _SU_C

#pragma warning(disable:4047) /* Indirection level difference warning */

#include "../Headers/suio.h"

uint32_t write_su_file(PSU_FILE f, PFILE out)
{
	uint32_t i;
	uint32_t i_loop_end;

	if(out == (PFILE)BAD_PTR || out == (PFILE)NULL || out == (PFILE)UNDEF_PTR || f == (PSU_FILE)BAD_PTR || f == (PSU_FILE)NULL || f == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(ferror(out))
		return IO_EXCEPTION;

	fputs("CS229\nSAMPLE_RATE ",out);
	fprintf(out,"%d",f->header.freq);
	fputs("\nSAMPLES ",out);
	fprintf(out,"%d",f->header.num_samples);
	fputs("\nCHANNELS ",out);
	fputs(f->header.flags & SU_STEREO ? "STEREO" : "MONO",out); /* If the & results in zero then we must be in MONO, else we are in STEREO */
	fputs("\nBITRES ",out);
	fputs(f->header.flags & SU_8_BIT ? "8" : f->header.flags & SU_16_BIT ? "16" : "32",out); /* Similar to STEREO/MONO settings */
	fputs("\nSTARTDATA\n",out);

	/* Determine the length of the data stored in our su structure */
	i_loop_end = f->header.flags & SU_STEREO ? f->header.num_samples << 1 : f->header.num_samples;
	i_loop_end = f->header.flags & SU_8_BIT ? i_loop_end : f->header.flags & SU_16_BIT ? i_loop_end << 1 : i_loop_end << 2;

	for(i = 0;i < i_loop_end;i++)
		if(ferror(out))
			return IO_EXCEPTION;
		else
			fputc(*(char*)(f->samples + i),out);

	/* Flush the buffer in case the user foolishly doesn't close the file */
	if(fflush(out))
		return IO_EXCEPTION;

	return SUCCESS;
}

uint32_t read_su_file(PSU_FILE dest, PFILE in)
{
#define MAX_TAG_LENGTH 12
#define FIRST_TAG_LENGTH 6
#define NUMBER_OF_TAGS 6

	uint8_t b; /* Remember for tag checking that capslock is cruise control for cool */
	uint8_t tags[NUMBER_OF_TAGS][MAX_TAG_LENGTH] = {"CS229\0","SAMPLE_RATE\0","SAMPLES\0","CHANNELS\0","BITRES\0","STARTDATA\n\0"};

	if(in == (PFILE)BAD_PTR || in == (PFILE)NULL || in == (PFILE)UNDEF_PTR || dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)NULL || dest == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(ferror(in))
		return IO_EXCEPTION;

	/* These are the current default values of su sound files, this is the only place they appear so change them here */
	/* Currently only channels defaults to MONO as everything else is defined or implicity defined */
	dest->header.flags = 0;
	dest->header.num_samples = 0;

	b = 0xFF;
	while(b)
	{
		if(b == 0x01 || ((uint8_t)fgetc(in) & ~0x20) == tags[0][0]) /* Our HEADER tag is case insensitive */
		{
			uint8_t buff[MAX_TAG_LENGTH]; /* We already include '\0' in this length */
			uint32_t i;
			uint32_t k;

			/* b = 0x1 was only good once */
			b = 0xFF;

			for(i = 1;i < FIRST_TAG_LENGTH - 1;i++)
			{
				buff[i] = (uint8_t)fgetc(in); /* The new HEADER, CS229, is not all letters */

				if(islower(buff[i])) /* CAPSLOCK IS CRUISE CONTROL FOR COOL */
					buff[i] &= ~0x20;

				if(buff[i] == tags[0][0]) /* If we read in another character that starts the header tag then we failed and should try again */
				{
					b = 0x01;
					break;
				}
			}

			/* fgetc returns EOF if it fails so we can complete the loop without making these checks */
			if(feof(in))
				return NO_HEADER_FOUND; /* We finished the file without finding a header tag so we return this, not UNEXPECTED_END_OF_FILE */
			else if(ferror(in))
				return IO_EXCEPTION;

			if(b == 0x01)
				continue;

			buff[0] = tags[0][0];
			buff[FIRST_TAG_LENGTH - 1] = '\0';

			/* We found our header tag so we should start being picky about what comes through [in] */
			if(!strcmp((c_string)buff,(c_string)tags[0]))
			{
				uint32_t buffer_index;
				uint8_t** buffer;
				uint8_t flags;
				uint32_t bitres;

				/* First, we need to check that header was on a single line alone */
				if((uint8_t)fgetc(in) != '\n')
					return INVALID_HEADER_TAG;

				if(feof(in))
					return UNEXPECTED_END_OF_FILE;
				else if(ferror(in))
					return IO_EXCEPTION;

#define FREQUENCY_BIT 0x1
#define SAMPLE_BIT 0x2
#define CHANNELS_BIT 0x4
#define BITRES_BIT 0x8

				/* We can read upto four states but we are not allowed to read them more than once and some are required */
				flags = 0x00;

				do
				{
					b = 0xFF;
					
					/* We go to MAX_TAG_LENGTH - 1 because we do not expect a '\0' and if we get one the format is wrong anyways */
					/* MAX_TAG_LENGTH garunteed to be greater than 1 since no tag is the empty string so the loop will execute at least once */
					for(i = 0;i < MAX_TAG_LENGTH - 1 && (!i || !is_white_space(buff[i - 1]));i++)
						buff[i] = (uint8_t)fgetc(in);

					/* If we have a whitespace character it means we finished the tag early and if not we read MAX_TAG_LENGTH - 1 characters */
					buff[is_white_space(buff[i - 1]) && buff[i - 1] != '\n' ? i - 1 : i] = '\0';

					if(feof(in))
						return UNEXPECTED_END_OF_FILE;
					else if(ferror(in))
						return IO_EXCEPTION;

					/* Turn on CAPSLOCK! */
					for(k = 0;buff[k];k++)
						if(islower(buff[k]))
							buff[k] &= ~0x20;

					if(!strcmp((c_string)buff,(c_string)tags[NUMBER_OF_TAGS - 1]))
						b = 0x00;
					else
					{
						uint32_t has_white_space;
						has_white_space = buff[i - 1] == '\0';

						for(i = 1;i < NUMBER_OF_TAGS - 1;i++) /* Last tag is START_DATA\n and is a special case above */
							if(!strcmp((c_string)buff,(c_string)tags[i]))
							{
#define MAX_PARAMETER_LENGTH 12
								/* Last character is always '\0' and '\n' should be the last character of every parameter */
								uint8_t parameter_buff[MAX_PARAMETER_LENGTH];
								uint32_t j;
								uint32_t param_val;

								if(!has_white_space)
								{
									parameter_buff[0] = (uint8_t)fgetc(in);

									if(!is_white_space(parameter_buff[0]) || parameter_buff[0] == '\n')
										return INVALID_HEADER;

									if(feof(in))
										return UNEXPECTED_END_OF_FILE;
									if(ferror(in))
										return IO_EXCEPTION;
								}

								parameter_buff[0] = (uint8_t)fgetc(in);

								while(is_white_space(parameter_buff[0]))
								{
									if(parameter_buff[0] == '\n')
										return INVALID_HEADER;
									else if(feof(in))
										return UNEXPECTED_END_OF_FILE;
									else if(ferror(in))
										return IO_EXCEPTION;
									else
										parameter_buff[0] = (uint8_t)fgetc(in);
								}

								for(j = 1;j < MAX_PARAMETER_LENGTH - 1 && (!j || !is_white_space(parameter_buff[j - 1]));j++)
									parameter_buff[j] = (uint8_t)fgetc(in);

								/* C strings are safe so construct parameter_buff to be one */
								parameter_buff[j] = '\0';

								if(feof(in))
									return UNEXPECTED_END_OF_FILE;
								else if(ferror(in))
									return IO_EXCEPTION;
								else if(parameter_buff[j - 1] != '\n') /* Paramters can't have any white space after them */
									return INVALID_HEADER;

								switch(i)
								{
								case 1:
									if(flags & FREQUENCY_BIT)
										return REDECLARATION_OF_TAG;

									flags |= FREQUENCY_BIT;
									param_val = (uint32_t)atoi((c_string)parameter_buff);

									if(!param_val)
										for(j = 0;parameter_buff[j] != '\n';j++)
											if(parameter_buff[j] != '0')
												return INVALID_NUMBER_FORMAT;

									dest->header.freq = param_val;
									i = NUMBER_OF_TAGS - 2;
									break;
								case 2:
									if(flags & SAMPLE_BIT)
										return REDECLARATION_OF_TAG;

									flags |= SAMPLE_BIT;
									param_val = (uint32_t)atoi((c_string)parameter_buff);

									if(!param_val)
										for(j = 0;parameter_buff[j] != '\n';j++)
											if(parameter_buff[j] != '0')
												return INVALID_NUMBER_FORMAT;

									dest->header.num_samples = param_val;
									i = NUMBER_OF_TAGS - 2;
									break;
								case 3:
									if(flags & CHANNELS_BIT)
										return REDECLARATION_OF_TAG;

									flags |= CHANNELS_BIT;

									for(j = 0;j < MAX_PARAMETER_LENGTH;j++)
										parameter_buff[j] &= ~0x20;

									if(!strcmp((c_string)parameter_buff,"STEREO\n"))
										dest->header.flags |= SU_STEREO;
									else if(strcmp((c_string)parameter_buff,"MONO\n"))
										return INVALID_CHANNEL_FORMAT;

									i = NUMBER_OF_TAGS - 2;
									break;
								case 4:
									if(flags & BITRES_BIT)
										return REDECLARATION_OF_TAG;

									flags |= BITRES_BIT;
									param_val = (uint32_t)atoi((c_string)parameter_buff);

									if(param_val != 0x8 && param_val != 0x10 && param_val != 0x20)
										return INVALID_BITRES_FORMAT;

									dest->header.flags |= param_val == 0x8 ? SU_8_BIT : (param_val == 0x10 ? SU_16_BIT : 0x0);
									i = NUMBER_OF_TAGS - 2;
									break;
								default:
									return INVALID_HEADER_TAG; /* This should not be possible */
									break;
								}
#undef MAX_PARAMETER_LENGTH
							}
							else if(i == NUMBER_OF_TAGS - 2)
								return INVALID_HEADER_TAG;
					}
				}
				while(b);

				/* We must read in these tags so check that we have */
				if(!(flags & FREQUENCY_BIT))
					return NO_FREQUENCY_FOUND;
				else if(!(flags & BITRES_BIT))
					return NO_BITRES_FOUND;

				buffer_index = 0x0;
				buffer = NULL;
				bitres = dest->header.flags & SU_8_BIT ? 0x1 : dest->header.flags & SU_16_BIT ? 0x2 : 0x4;
				
				for(i = 0;!(feof(in) || ferror(in));i++)
				{
					if(!i)
					{
						if(!(~(buffer_index & 0x00000FFF) + 1)) /* Check that we have a multiple of 0x1000, WITH WRAP AROUND! */
						{
							uint8_t** temp;
							temp = (uint8_t**)malloc((sizeof(PTR) << 12) * (buffer_index + 1));

							if(temp == (uint8_t**)NULL || temp == (uint8_t**)BAD_PTR || temp == (uint8_t**)UNDEF_PTR)
							{
								/* We don't go to [buffer_index] + 1 because we did not yet request that memory */
								for(k = 0;k < buffer_index;k++)
									if(buffer)
										free((void*)buffer[k]);

								if(buffer)
									free((void*)buffer);

								return BUFFER_OVERFLOW;
							}

							if(buffer) /* Starts as NULL so there's a need to check this */
							{
								uint32_t j;

								for(j = 0;j < buffer_index;j++)
									temp[j] = buffer[j];
							}

#ifndef WINDOWS
							/* I hate pedantic mode so much */
							{
							uint8_t** swap_temp;
							swap_temp = buffer;
							buffer = temp;
							temp = swap_temp;
							}
#else
							buffer = (uint8_t**)((PTR)buffer ^ (PTR)temp);
							temp = (uint8_t**)((PTR)buffer ^ (PTR)temp);
							buffer = (uint8_t**)((PTR)buffer ^ (PTR)temp);
#endif

							if(temp) /* May have inherited NULL from buffer */
								free((void*)temp);
						}

						buffer[buffer_index] = (uint8_t*)malloc(0x1000);

						if(buffer[buffer_index] == (uint8_t*)NULL || buffer[buffer_index] == (uint8_t*)BAD_PTR || buffer[buffer_index] == (uint8_t*)UNDEF_PTR)
						{
							/* We do not go to k < [buffer_index] + 1 because we never got that memory */
							for(k = 0;k < buffer_index;k++)
								if(buffer)
									free((void*)buffer[k]);

							if(buffer)
								free((void*)buffer);

							return BUFFER_OVERFLOW;
						}
					}

					buffer[buffer_index][i] = (uint8_t)fgetc(in);

					if(!(~i & 0x00000FFF))
					{
						i = 0xFFFFFFFF;
						buffer_index++;
					}
				}
				
				/* This is the total used buffer */
				/* We get an extra one from the last time through the for loop unless [i] is zero because we already set [i] to be one low */
				i = (buffer_index << 12) + i - (i ? 1 : 0);

				if(dest->header.flags & SU_STEREO)
					i = i >> 1;
				
				if(!dest->header.num_samples)
					dest->header.num_samples = i / bitres;
				else if(dest->header.num_samples * bitres != i)
				{
					for(k = 0;k < buffer_index + 1;k++)
						if(buffer)
							free((void*)buffer[k]);

					if(buffer)
						free((void*)buffer);

					return SAMPLE_SIZE_MISMATCH;
				}

				/* [i] is garunteed to be even when the stereo flag is given if the data is valid */
				if(dest->header.flags & SU_STEREO)
					i = i << 1;

				dest->samples = (uint8_t*)malloc(i);

				for(k = 0;k < i;k++)
				{
					uint32_t x,y;
					x = k >> 12;
					y = k & 0x00000FFF;

					dest->samples[k] = buffer[x][y];
				}

				/* For some reason the program doesn't like us cleaning up in the above loop */
				for(k = 0;k < buffer_index + 1;k++)
					if(buffer) /* If sample size is zero we might not even have a buffer */
						free((void*)buffer[k]);

				if(buffer)
					free((void*)buffer);

#undef FREQUENCY_BIT
#undef SAMPLE_BIT
#undef CHANNELS_BIT
#undef BITRES_BIT

				/* We finished with header data so we should leave the while loop */
				b = 0x00;
			}
		}

		if(b && feof(in))
			return UNEXPECTED_END_OF_FILE;
		else if(b && ferror(in))
			return IO_EXCEPTION;
	}
	
#undef MAX_TAG_LENGTH
#undef FIRST_TAG_LENGTH
#undef NUMBER_OF_TAGS

	/* We should be at the end of the file if we successfully read in the su sound file */
	if(!feof(in))
	{
		free((void*)dest->samples);
		return UNEXPECTED_CONTINUATION_OF_FILE;
	}
	else if(ferror(in))
	{
		free((void*)dest->samples);
		return IO_EXCEPTION;
	}

	return SUCCESS;
}

uint32_t flip_endian(PSU_FILE f)
{
	uint32_t i;
	uint32_t bound;
	uint32_t size;

	if(f == (PSU_FILE)BAD_PTR || f == (PSU_FILE)NULL || f == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(f->samples == (uint8_t*)BAD_PTR || f->samples == (uint8_t*)NULL || f->samples == (uint8_t*)UNDEF_PTR)
		return NULL_POINTER;

	if(f->header.flags & SU_8_BIT)
		return SUCCESS;
	else if(f->header.flags & SU_16_BIT)
		size = 0;
	else
		size = 1;

	bound = f->header.num_samples << (f->header.flags & SU_STEREO ? 1 : 0);

	if(size)
		for(i = 0;i < bound;i++)
		{
			/* Swap msb and lsb */
			f->samples[i << 2] ^= f->samples[(i << 2) + 3];
			f->samples[(i << 2) + 3] ^= f->samples[i << 2];
			f->samples[i << 2] ^= f->samples[(i << 2) + 3];

			/* Swap middle bytes */
			f->samples[(i << 2) + 1] ^= f->samples[(i << 2) + 2];
			f->samples[(i << 2) + 2] ^= f->samples[(i << 2) + 1];
			f->samples[(i << 2) + 1] ^= f->samples[(i << 2) + 2];
		}
	else
		for(i = 0;i < bound;i++)
		{
			/* Swap msb and lsb */
			f->samples[i << 1] ^= f->samples[(i << 1) + 1];
			f->samples[(i << 1) + 1] ^= f->samples[i << 1];
			f->samples[i << 1] ^= f->samples[(i << 1) + 1];
		}

	return SUCCESS;
}

uint32_t mix_su_file(PSU_FILE dest, PSU_FILE files, double* file_intensities, uint32_t num_files)
{
	uint32_t i;
	uint32_t j;
	uint32_t c;
	double dmax; /* Reminds me of DDR and now it does for YOU TOO */
	double* mix_samples;

	if(dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)NULL || dest == (PSU_FILE)UNDEF_PTR || files == (PSU_FILE)BAD_PTR || files == (PSU_FILE)NULL || files == (PSU_FILE)UNDEF_PTR || file_intensities == (double*)BAD_PTR || file_intensities == (double*)NULL || file_intensities == (double*)UNDEF_PTR)
		return NULL_POINTER;

	for(i = 0;i < num_files - 1;i++)
		if(files[i].header.flags != files[i + 1].header.flags || files[i].header.freq != files[i + 1].header.freq)
			return INCOMPATABLE_FILE_TYPES;

	dest->header.flags = files[0].header.flags;
	dest->header.freq = files[0].header.freq;

	c = 0x0;

	for(i = 0;i < num_files;i++)
		if(files[i].header.num_samples > c)
			c = files[i].header.num_samples;

	/* At some point this will need to be in existence at the same time as the double array so might as well make it now */
	dest->header.num_samples = c;
	if(dest->header.flags & SU_STEREO)
		dest->samples = (uint8_t*)malloc(dest->header.flags & SU_8_BIT ? dest->header.num_samples << 1 : (dest->header.flags & SU_16_BIT ? dest->header.num_samples << 2 : dest->header.num_samples << 3));
	else
		dest->samples = (uint8_t*)malloc(dest->header.flags & SU_8_BIT ? dest->header.num_samples : (dest->header.flags & SU_16_BIT ? dest->header.num_samples << 1 : dest->header.num_samples << 2));

	if(dest->samples == (uint8_t*)BAD_PTR || dest->samples == (uint8_t*)NULL || dest->samples == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	/* As a note, we are throwing low bit significance to the wind here, and if we get that significance, well great */
	mix_samples = (double*)malloc(sizeof(double) * dest->header.num_samples);

	if(mix_samples == (double*)BAD_PTR || mix_samples == (double*)NULL || mix_samples == (double*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < dest->header.num_samples;i++)
		mix_samples[i] = 0.0;

	for(i = 0;i < num_files;i++)
		if(files[i].header.flags & SU_8_BIT)
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)files[i].samples[j];
		else if(files[i].header.flags & SU_16_BIT)
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)((uint16_t*)files[i].samples)[j];
		else
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)((uint32_t*)files[i].samples)[j];

	/* We might have negative numbers */
#ifdef WINDOWS
	dmax = -HUGE_VAL;
#else
	dmax = -HUGE_VAL();
#endif

	/* For some reason we need the braces here, no clue why */
	for(i = 0;i < dest->header.num_samples;i++)
	{
		if(mix_samples[i] > dmax)
			dmax = mix_samples[i];
	}

	/* We need to scale the biggest value to the max value and scale all other values by the same factor */
	if(dest->header.flags & SU_8_BIT)
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++) /* We round because truncation will probably cause us to never get the largest value */
				dest->samples[i] = (uint8_t)round(mix_samples[i] * 255.0 / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				dest->samples[i] = 0;
	else if(dest->header.flags & SU_16_BIT)
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++)
				((uint16_t*)dest->samples)[i] = (uint16_t)round(mix_samples[i] * 65535.0 / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				((uint16_t*)dest->samples)[i] = 0;
	else
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++)
				((uint32_t*)dest->samples)[i] = (uint32_t)round(mix_samples[i] * 4294967295.0 / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				((uint32_t*)dest->samples)[i] = 0;

	return SUCCESS;
}

uint32_t mix_su_file_no_volume_gain(PSU_FILE dest, PSU_FILE files, double* file_intensities, uint32_t num_files)
{
	uint32_t i;
	uint32_t j;
	uint32_t c;
	uint32_t max;
	double dmax; /* Reminds me of DDR and now it does for YOU TOO */
	double* mix_samples;

	if(dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)NULL || dest == (PSU_FILE)UNDEF_PTR || files == (PSU_FILE)BAD_PTR || files == (PSU_FILE)NULL || files == (PSU_FILE)UNDEF_PTR || file_intensities == (double*)BAD_PTR || file_intensities == (double*)NULL || file_intensities == (double*)UNDEF_PTR)
		return NULL_POINTER;

	for(i = 0;i < num_files - 1;i++)
		if(files[i].header.flags != files[i + 1].header.flags || files[i].header.freq != files[i + 1].header.freq)
			return INCOMPATABLE_FILE_TYPES;

	dest->header.flags = files[0].header.flags;
	dest->header.freq = files[0].header.freq;

	c = 0x0;
	max = 0x0;

	for(i = 0;i < num_files;i++)
	{
		if(files[i].header.num_samples > max)
			c = files[i].header.num_samples;

		for(j = 0;j < files[i].header.num_samples;j++)
			if(files[i].header.flags & SU_8_BIT)
			{
				if(files[i].samples[j] > max)
					max = files[i].samples[j];
			}
			else if(files[i].header.flags & SU_16_BIT)
			{
				if(((uint16_t*)files[i].samples)[j] > max)
					max = ((uint16_t*)files[i].samples)[j];
			}
			else if(((uint32_t*)files[i].samples)[j] > max)
					max = ((uint32_t*)files[i].samples)[j];
	}

	/* At some point this will need to be in existence at the same time as the double array so might as well make it now */
	dest->header.num_samples = c;
	if(dest->header.flags & SU_STEREO)
		dest->samples = (uint8_t*)malloc(dest->header.flags & SU_8_BIT ? dest->header.num_samples << 1 : (dest->header.flags & SU_16_BIT ? dest->header.num_samples << 2 : dest->header.num_samples << 3));
	else
		dest->samples = (uint8_t*)malloc(dest->header.flags & SU_8_BIT ? dest->header.num_samples : (dest->header.flags & SU_16_BIT ? dest->header.num_samples << 1 : dest->header.num_samples << 2));

	if(dest->samples == (uint8_t*)BAD_PTR || dest->samples == (uint8_t*)NULL || dest->samples == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	/* As a note, we are throwing low bit significance to the wind here, and if we get that significance, well great */
	mix_samples = (double*)malloc(sizeof(double) * dest->header.num_samples);

	if(mix_samples == (double*)BAD_PTR || mix_samples == (double*)NULL || mix_samples == (double*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < dest->header.num_samples;i++)
		mix_samples[i] = 0.0;

	for(i = 0;i < num_files;i++)
		if(files[i].header.flags & SU_8_BIT)
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)files[i].samples[j];
		else if(files[i].header.flags & SU_16_BIT)
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)((uint16_t*)files[i].samples)[j];
		else
			for(j = 0;j < files[i].header.num_samples;j++)
				mix_samples[j] += file_intensities[i] * (double)((uint32_t*)files[i].samples)[j];

	/* We might have negative numbers */
#ifdef WINDOWS
	dmax = -HUGE_VAL;
#else
	dmax = -HUGE_VAL();
#endif

	/* For some reason we need the braces here, no clue why */
	for(i = 0;i < dest->header.num_samples;i++)
	{
		if(mix_samples[i] > dmax)
			dmax = mix_samples[i];
	}

	/* We need to scale the biggest value to the max value and scale all other values by the same factor */
	if(dest->header.flags & SU_8_BIT)
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++) /* We round because truncation will probably cause us to never get the largest value */
				dest->samples[i] = (uint8_t)round(mix_samples[i] * max / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				dest->samples[i] = 0;
	else if(dest->header.flags & SU_16_BIT)
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++)
				((uint16_t*)dest->samples)[i] = (uint16_t)round(mix_samples[i] * max / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				((uint16_t*)dest->samples)[i] = 0;
	else
		if(dmax != 0.0)
			for(i = 0;i < dest->header.num_samples;i++)
				((uint32_t*)dest->samples)[i] = (uint32_t)round(mix_samples[i] * max / dmax);
		else
			for(i = 0;i < dest->header.num_samples;i++)
				((uint32_t*)dest->samples)[i] = 0;

	return SUCCESS;
}

uint32_t join_su_file(PSU_FILE dest, PSU_FILE first, PSU_FILE second)
{
	uint32_t i;
	uint32_t j;

	if(dest == (PSU_FILE)NULL || dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)UNDEF_PTR || first == (PSU_FILE)NULL || first == (PSU_FILE)BAD_PTR || first == (PSU_FILE)UNDEF_PTR || second == (PSU_FILE)NULL || second == (PSU_FILE)BAD_PTR || second == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(first->header.flags != second->header.flags)
		return INCOMPATABLE_FILE_TYPES;

	if(first->header.freq != second->header.freq)
		return INCOMPATABLE_FILE_TYPES;

	dest->samples = (uint8_t*)malloc((first->header.num_samples + second->header.num_samples) * (first->header.flags & SU_STEREO ? 2 : 1) * (first->header.flags & SU_8_BIT ? 1 : (first->header.flags & SU_16_BIT ? 2 : 4)));

	if(dest->samples == (uint8_t*)NULL || dest->samples == (uint8_t*)BAD_PTR || dest->samples == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	dest->header.flags = first->header.flags;
	dest->header.freq = first->header.freq;
	dest->header.num_samples = first->header.num_samples + second->header.num_samples;

	for(i = 0,j = 0;i < first->header.num_samples;i++,j++)
		dest->samples[j] = first->samples[i];

	for(i = 0;i < second->header.num_samples;i++,j++)
		dest->samples[j] = second->samples[i];

	return SUCCESS;
}

uint32_t fourier_transform(PCOMPLEX dest, PSU_FILE source, uint32_t N, uint32_t k, uint32_t lc)
{
	uint32_t n;

	if(dest == (PCOMPLEX)NULL || dest == (PCOMPLEX)BAD_PTR || dest == (PCOMPLEX)UNDEF_PTR || source == (PSU_FILE)NULL || source == (PSU_FILE)BAD_PTR || source == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(k >= (N >> 1))
		return INVALID_FREQUENCY_COMPONENT;

	if(N >= source->header.num_samples)
		return WINDOW_TOO_BIG;

	dest->real = 0.0;
	dest->imag = 0.0;

	if(source->header.flags & SU_STEREO)
		if(lc)
			for(n = 0;n < N;n++)
			{
				double sample;
				
				if(source->header.flags & SU_8_BIT)
					sample = (double)(uint8_t)source->samples[n << 1];
				else if(source->header.flags & SU_16_BIT)
					sample = (double)(uint16_t)((uint16_t*)source->samples)[n << 1];
				else
					sample = (double)(uint32_t)((uint32_t*)source->samples)[n << 1];

				dest->real += sample * cos(2.0 * PI * n * k / N);
				dest->imag += sample * sin(2.0 * PI * n * k / N);
			}
		else
			for(n = 0;n < N;n++)
			{
				double sample;
				
				if(source->header.flags & SU_8_BIT)
					sample = (double)(uint8_t)source->samples[(n << 1) + 1];
				else if(source->header.flags & SU_16_BIT)
					sample = (double)(uint16_t)((uint16_t*)source->samples)[(n << 1) + 1];
				else
					sample = (double)(uint32_t)((uint32_t*)source->samples)[(n << 1) + 1];

				dest->real += sample * cos(2.0 * PI * n * k / N);
				dest->imag += sample * sin(2.0 * PI * n * k / N);
			}
	else
		for(n = 0;n < N;n++)
		{
			double sample;
			
			if(source->header.flags & SU_8_BIT)
				sample = (double)(uint8_t)source->samples[n];
			else if(source->header.flags & SU_16_BIT)
				sample = (double)(uint16_t)((uint16_t*)source->samples)[n];
			else
				sample = (double)(uint32_t)((uint32_t*)source->samples)[n];

			dest->real += sample * cos(2.0 * PI * n * k / N);
			dest->imag += sample * sin(2.0 * PI * n * k / N);
		}

	return SUCCESS;
}

double su_rms(PSU_FILE f)
{
	double ret = 0.0;
	uint32_t i;
	uint32_t i_loop_end;

	if(f == (PSU_FILE)BAD_PTR || f == (PSU_FILE)NULL || f == (PSU_FILE)UNDEF_PTR)
	{
#ifdef WINDOWS
		return -HUGE_VAL;
#else
		return -HUGE_VAL();
#endif
	}

	/* Determine the length of the data stored in our su structure */
	i_loop_end = f->header.flags & SU_STEREO ? f->header.num_samples << 1 : f->header.num_samples;
	i_loop_end = f->header.flags & SU_8_BIT ? i_loop_end : f->header.flags & SU_16_BIT ? i_loop_end << 1 : i_loop_end << 2;

	for(i = 0;i < i_loop_end;i++)
	{
		double sample;
		
		if(f->header.flags & SU_8_BIT)
			sample = (double)(uint8_t)f->samples[i];
		else if(f->header.flags & SU_16_BIT)
			sample = (double)(uint16_t)((uint16_t*)f->samples)[i];
		else
			sample = (double)(uint32_t)((uint32_t*)f->samples)[i];

		ret += sample * sample / (double)i_loop_end;
	}

	return sqrt(ret);
}

uint32_t scale_su_file(PSU_FILE f, double factor)
{
	uint32_t i;
	uint32_t i_loop_end;

	if(f == (PSU_FILE)BAD_PTR || f == (PSU_FILE)NULL || f == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	/* Determine the length of the data stored in our su structure */
	i_loop_end = f->header.flags & SU_STEREO ? f->header.num_samples << 1 : f->header.num_samples;
	i_loop_end = f->header.flags & SU_8_BIT ? i_loop_end : f->header.flags & SU_16_BIT ? i_loop_end << 1 : i_loop_end << 2;

	for(i = 0;i < i_loop_end;i++)
		if(f->header.flags & SU_8_BIT)
			f->samples[i] = (uint32_t)((double)f->samples[i] * factor);
		else if(f->header.flags & SU_16_BIT)
			((uint16_t*)f->samples)[i] = (uint32_t)((double)((uint16_t*)f->samples)[i] * factor);
		else
			((uint32_t*)f->samples)[i] = (uint32_t)((double)((uint32_t*)f->samples)[i] * factor);

	return SUCCESS;
}

#endif