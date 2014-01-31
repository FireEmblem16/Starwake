#ifndef _PART_1_BATTERY_C
#define _PART_1_BATTERY_C

#pragma warning(disable:4244)
#pragma warning(disable:4996)

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include "../Headers/defines.h"
#include "../Headers/util.h"
#include "../Headers/crawler.h"
#include "../Headers/datastructures.h"

#ifdef WINDOWS
#include <math.h>
#endif

STACK current_path;

void initialize_directory();
void change_directory(c_string path); /* Only accepts .. or a folder to move to next, also creates a new copy of [path] */
c_string get_directory();

/* Returns 0xFFFFFFFF if the file could not be found. */
/* If the file is not found the current directory is reverted. */
uint32_t find_file(c_string fname, uint8_t case_sensitive, uint8_t isdir);
void __sleep(clock_t tics); /* Extra underscores to make sure this function isn't defiend elsewhere */

int main(int argc, char** argv)
{
	PFILE out;
	CRAWLER c;
	C_ENTRY student;
	uint32_t err;
	
	if(!system(NULL))
	{
		fputs("System command processor not available.\n",stderr);
		return EXIT_FAILURE;
	}

#pragma region Test Dependancies
	/* Test all the dependancies for this program */
	out = fopen("known_gensine.exe","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional executable by the name of known_gensine.exe to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("known_info.exe","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional executable by the name of known_info.exe to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("analysis.exe","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional executable by the name of analysis.exe to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("convertsu.exe","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional executable by the name of convertsu.exe to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("corrupt1","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional file by the name of corrupt1 to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("corrupt2","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional file by the name of corrupt2 to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

	out = fopen("corrupt3","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional file by the name of corrupt3 to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}
	out = fopen("corrupt4","rb");

	if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
	{
		fputs("This program requires an additional file by the name of corrupt4 to be in the running directory.\n",stderr);
		return EXIT_FAILURE;
	}

	if(fclose(out))
	{
		write_error(FILE_LEFT_OPEN,stderr);
		return EXIT_FAILURE;
	}

#pragma endregion

#pragma region Generate Test Files
	/* Generate all test files */
	fputs("Generating known files.\n",stdout);

	if(system("bash generate_known.sh"))
	{
		fputs("generate_known.sh could not be executed.\n",stderr);
		return EXIT_FAILURE;
	}
#pragma endregion

	initialize_directory();
	err = crawler_open(&c,get_directory(),"temp.crawl");

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}
	
	student.path = NULL;
	err = crawler_next_entry(&c,&student);
	
	while(student.path)
	{
		STACK s;
		uint8_t cmd[1024];
		uint8_t b;
		uint32_t depth;
		uint32_t i;
		uint32_t j;
		uint32_t* crawl_index;
		b = TRUE;

		if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}

		if(!student.is_dir || !strcmp(student.path,"Debug"))
		{
			err = crawler_next_entry(&c,&student);
			continue;
		}

		strcpy(cmd,student.path);
		strcat(cmd,".txt");

		/* Load the output file */
		out = fopen(cmd,"wb");

		if(out == (PFILE)NULL || out == (PFILE)BAD_PTR || out == (PFILE)UNDEF_PTR)
		{
			write_error(FILE_NOT_FOUND,stderr);
			return EXIT_FAILURE;
		}

#pragma region Initializing Student Resources
		/* Create the header for this student in the output file */
		fputs(student.path,out);
		fputc('\n',out);

		for(i = strlen(student.path);i;i--)
			fputc('-',out);

		fputc('\n',out);

		for(i = strlen(student.path);i;i--)
			fputc('#',out);

		fputc('\n',out);

		for(i = strlen(student.path);i;i--)
			fputc('-',out);

		fputc('\n',out);

		/* We know that even if make succeeds in the current directory is would not be right */
		change_directory((c_string)student.path);

		/* We need to know what index the crawler was at */
		initialize_stack(&s);
		crawl_index = (uint32_t*)malloc(sizeof(uint32_t));
		*crawl_index = 0;
		push_stack(&s,(void*)crawl_index);
#pragma endregion

#pragma region Making the Project
		cmd[0] = '\0';
		strcat(cmd,"make -s -C \"\0");
		strcat(cmd,get_directory()); /* This is garunteed to at least have one character */
		strcat(cmd,"\" > dumpextratext 2> dumpextraerror\0");

		fprintf(stdout,"Attempting to make project for student %s.\n",student.path);

		/* MAKING THE PROJECT - Crawl until a make command is successful */
		while(system(cmd))
		{
			CRAWLER sub_crawler;
			C_ENTRY sub_entry;
			uint32_t* last_crawl_index;

			sub_entry.path = NULL;
			last_crawl_index = (uint32_t*)pop_stack(&s);
			err = crawler_open(&sub_crawler,get_directory(),"temp2.crawler");
			
			if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}

			(*last_crawl_index)++;

			for(i = 0;i < *last_crawl_index;i++)
			{
				err = crawler_next_entry(&sub_crawler,&sub_entry);

				if(err)
				{
					write_error(err,stderr);
					return EXIT_FAILURE;
				}

				/* We only want to crawl through directories */
				if(!sub_entry.is_dir)
					i--;

				if(!sub_entry.path)
					break;
			}

			/* If there's another directory to enter then we should do so */
			if(sub_entry.path)
			{
				uint32_t* new_crawl_index;

				push_stack(&s,(void*)last_crawl_index);

				new_crawl_index = (uint32_t*)malloc(sizeof(uint32_t*));
				(*new_crawl_index) = 0;

				push_stack(&s,(void*)new_crawl_index);

				change_directory((c_string)sub_entry.path);
			} /* If there is not another directory then we need to go to the parent directory */
			else
				change_directory("..\0");

			/* If we are out of data we failed */
			if(!strcmp("\0",get_directory()))
			{
				b = FALSE;
				err = crawler_close(&sub_crawler);
	
				if(err)
				{
					write_error(err,stderr);
					return EXIT_FAILURE;
				}

				break;
			}

			cmd[0] = '\0';
			strcat(cmd,"make -s -C \"\0");
			strcat(cmd,get_directory()); /* This is garunteed to at least have one character */
			strcat(cmd,"\" > dumpextratext 2> dumpextraerror\0");

			err = crawler_close(&sub_crawler);
	
			if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}

			if(sub_entry.path)
				free((void*)sub_entry.path);
		}

		system("rm dumpextratext dumpextraerror");

		/* We were unable to find a makefile */
		if(!b)
		{
			fputs("Unable to fully make project for this student.\nAttempting partial Grading\n",out);
			//err = crawler_next_entry(&c,&student);
			//continue;
		}
#pragma endregion

#pragma region Finding Gensine
		/* FINDING PART A - Crawl until we find gensine or gensine.exe */
		/* Assuming that the student did the sane thing and has the makefile above or in the directory of the executables */
		depth = current_path.size;

		fputs("Searching for gensine.exe.\n\0",stdout);
		err = find_file("gensine.exe",FALSE,FALSE);

		if(err == 0xFFFFFFFF)
		{
			fputs("Unable to find gensine.exe.\nNow searching for gensine.\n",stdout);
			err = find_file("gensine",FALSE,FALSE);

			if(err == 0xFFFFFFFF)
				err = 0;
			else if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}
			else
				err = 2;
		}
		else if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}
		else
			err = 1;

		/* We're using err as a value for if we've found gensine: 1 is gensine, 2 is gensine.exe */
		switch(err)
		{
		case 1:
			fputs("Copying gensine.exe to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/gensine.exe\" gensine.exe");

			if(system(cmd))
			{
				fputs("Unable to copy gensine.exe in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 2:
			fputs("Copying gensine to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/gensine\" gensine.exe");

			if(system(cmd))
			{
				fputs("Unable to copy gensine in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 0:
		default:
			fputs("Unable to find the gensine executable for this student.\ngensine score: 0/340 points.\n",out);
			b = FALSE;

			break;
		}
#pragma endregion

		/* If b true then we found the gensine program */
		/* Scoring - Out of 340 */
		/* 80 points each for three successful runs */
		/* WE HAVE 100 POINTS LEFT FOR ERROR CHECKS */
		if(b)
		{
			PFILE script; /* We will assume for short code that fopen successfully makes and closes a file every time */
			uint8_t pid[10];
			float score = 0.0f;

			fputs("Gensine Tests\n",out);
			fputs("-------------\n\n",out);

#pragma region Gensine Test 1
			fputs("Attempting first gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 440 CS229 2.78 > test.su",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for gensine to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis test.su known1.su su b su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe test.su known1.su su b su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the first gensine test for this student. 0/80 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of first test failed. 0/80 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 80.0f;
						fputs("First gensine test 80/80 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 40 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 20 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 20 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						score += temp;
						fprintf(out,"First gensine test %d/80 points. If zero check for phase shifts\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region Gensine Test 2
			fputs("Attempting second gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 777 CS229 1.75 > test.su",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for gensine to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis test.su known2.su su b su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe test.su known2.su su b su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the second gensine test for this student. 0/80 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of second test failed. 0/80 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 80.0f;
						fputs("Second gensine test 80/80 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 40 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 20 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 20 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						score += temp;
						fprintf(out,"Second gensine test %d/80 points. If zero check for phase shifts\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region Gensine Test 3
			fputs("Attempting third gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 1234 WAV .4123 > test.wav",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for gensine to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis test.wav known3.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe test.wav known3.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the third gensine test for this student. 0/80 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of third test failed. 0/80 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 80.0f;
						fputs("Third gensine test 80/80 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 40 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 20 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 20 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						score += temp;
						fprintf(out,"Third gensine test %d/80 points. If zero check for phase shifts\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region Gensine Test 4
			fputs("Attempting fourth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe CS229 .4123 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Fourth Test - Missing first argument - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 5
			fputs("Attempting fifth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 999 .4123 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Fifth Test - Missing second argument - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 6
			fputs("Attempting sixth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 444 CS229 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Sixth Test - Missing third argument - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 7
			fputs("Attempting seventh gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 500 CS229 0 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			system("gensine 500 CS229 0 > kout");
			system("analysis out kout su b su b d > analysis.out");
#else
			system("./gensine.exe 500 CS229 0 > kout");
			system("./analysis.exe out kout su b su b d > analysis.out");
#endif

			fputs("Gensine Seventh Test - Zero Duration - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Analysis\n",out);
			fputs("--------\n",out);
			
			script = fopen("analysis.out","rb");
			for(i = fgetc(script);!feof(script) && i != 'D';i = fgetc(script));
			fputc('D',out);
			for(i = fgetc(script);!feof(script) && i != '\n';fputc(i,out),i = fgetc(script));
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 8
			fputs("Attempting eigth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Eigth Test - No arguments - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 9
			fputs("Attempting ninth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 440 CS229 1 42 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Ninth Test - Extra Argument - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Gensine Test 10
			fputs("Attempting tenth gensine test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./gensine.exe 440 CS 1 > out 2> err",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for gensine to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Gensine Tenth Test - Invalid Sound Format - 10 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Stdout\n",out);
			fputs("------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Stderr\n",out);
			fputs("------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

			/* I don't particuarly care if we don't remove the files in between tests so we will only do this once */
			if(system("rm test.su test.wav analysis.out out err kout"))
			{
				fputs("Unable to remove gensine test files.",stderr);
				return EXIT_FAILURE;
			}

			if(system("rm gensine.exe temporary_script.sh pid"))
			{
				fputs("Unable to remove temporary files from gensine test.\n",stderr);
				return EXIT_FAILURE;
			}
		} /* We've already printed the zero score for not being able to find gensine */

#pragma region Finding Info
		/* FINDING PART B - Crawl until we find info or info.exe */
		/* Assuming that the student did the sane thing and has the makefile above or in the directory of the executables */
		depth = current_path.size;

		fputs("Searching for info.exe.\n\0",stdout);
		err = find_file("info.exe",FALSE,FALSE);

		if(err == 0xFFFFFFFF)
		{
			fputs("Unable to find info.exe.\nNow searching for info.\n",stdout);
			err = find_file("info",FALSE,FALSE);

			if(err == 0xFFFFFFFF)
				err = 0;
			else if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}
			else
				err = 2;
		}
		else if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}
		else
			err = 1;

		/* We're using err as a value for if we've found info: 1 is info, 2 is info.exe */
		switch(err)
		{
		case 1:
			fputs("Copying info.exe to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/info.exe\" info.exe");

			if(system(cmd))
			{
				fputs("Unable to copy info.exe in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 2:
			fputs("Copying info to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/info\" info.exe");

			if(system(cmd))
			{
				fputs("Unable to copy info in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 0:
		default:
			fputs("Unable to find the info executable for this student.\ninfo score: 0/255 points.\n",out);
			b = FALSE;

			break;
		}
#pragma endregion

		/* If b true then we found the info program */
		/* Scoring - Out of 255 */
		/* 20 points each for nine successful runs */
		/* WE HAVE 75 POINTS LEFT FOR ERROR CHECKS */
		if(b)
		{
			PFILE script; /* We will assume for short code that fopen successfully makes and closes a file every time */
			uint8_t pid[10];

			fputs("Info Tests\n",out);
			fputs("----------\n\n",out);

#pragma region Info Test 1
			fputs("Attempting first info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known4.su > err\n",script);
			fputs("./info.exe < known4.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info First Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 2
			fputs("Attempting second info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known5.su > err\n",script);
			fputs("./info.exe < known5.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Second Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 3
			fputs("Attempting third info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known6.su > err\n",script);
			fputs("./info.exe < known6.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Third Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 4
			fputs("Attempting fourth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known7.su > err\n",script);
			fputs("./info.exe < known7.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Fourth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 5
			fputs("Attempting fifth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known8.su > err\n",script);
			fputs("./info.exe < known8.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Fifth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 6
			fputs("Attempting sixth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known9.su > err\n",script);
			fputs("./info.exe < known9.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Sixth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 7
			fputs("Attempting seventh info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known10.wav > err\n",script);
			fputs("./info.exe < known10.wav > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Seventh Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 8
			fputs("Attempting eigth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known11.wav > err\n",script);
			fputs("./info.exe < known11.wav > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Eigth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 9
			fputs("Attempting ninth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < known12.wav > err\n",script);
			fputs("./info.exe < known12.wav > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for info to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Ninth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Information\n",out);
			fputs("--------------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Information\n",out);
			fputs("-------------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 10
			fputs("Attempting tenth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < corrupt1 > err 2> err2\n",script);
			fputs("./info.exe < corrupt1 > out 2> out2",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for info to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Tenth Test - 15 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Expected Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("err2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("out2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 11
			fputs("Attempting eleventh info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < corrupt2 > err 2> err2\n",script);
			fputs("./info.exe < corrupt2 > out 2> out2",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for info to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Eleventh Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Expected Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("err2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("out2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 12
			fputs("Attempting twelfth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < corrupt3 > err 2> err2\n",script);
			fputs("./info.exe < corrupt3 > out 2> out2",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for info to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Twelfth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Expected Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("err2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("out2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

#pragma region Info Test 13
			fputs("Attempting thirteenth info test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./known_info.exe < corrupt4 > err 2> err2\n",script);
			fputs("./info.exe < corrupt4 > out 2> out2",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 5); /* Five seconds is a very generous time for info to error */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

			fputs("Info Thirteenth Test - 20 points\n",out);
			fputs("--------------------------------------------------------\n",out);

			fputs("Expected Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("err","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Expected Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("err2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stdout\n",out);
			fputs("---------------\n",out);

			script = fopen("out","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);

			fputs("Student Stderr\n",out);
			fputs("---------------\n",out);

			script = fopen("out2","rb");
			for(i = fgetc(script),j = 0;!feof(script) && j < 200;fputc(i,out),i = fgetc(script),j++);
			fclose(script);
			fputs("\n",out);
#pragma endregion

			/* I don't particuarly care if we don't remove the files in between tests so we will only do this once */
			if(system("rm out err out2 err2"))
			{
				fputs("Unable to remove info test files.",stderr);
				return EXIT_FAILURE;
			}

			if(system("rm info.exe temporary_script.sh pid"))
			{
				fputs("Unable to remove temporary files from info test.\n",stderr);
				return EXIT_FAILURE;
			}
		} /* We've already printed the zero score for not being able to find info */

#pragma region Finding cs229towav
		/* FINDING PART C - Crawl until we find info or cs229towav.exe */
		/* Assuming that the student did the sane thing and has the makefile above or in the directory of the executables */
		depth = current_path.size;

		fputs("Searching for cs229towav.exe.\n\0",stdout);
		err = find_file("cs229towav.exe",FALSE,FALSE);

		if(err == 0xFFFFFFFF)
		{
			fputs("Unable to find cs229towav.exe.\nNow searching for cs229towav.\n",stdout);
			err = find_file("cs229towav",FALSE,FALSE);

			if(err == 0xFFFFFFFF)
				err = 0;
			else if(err)
			{
				write_error(err,stderr);
				return EXIT_FAILURE;
			}
			else
				err = 2;
		}
		else if(err)
		{
			write_error(err,stderr);
			return EXIT_FAILURE;
		}
		else
			err = 1;

		/* We're using err as a value for if we've found info: 1 is info, 2 is info.exe */
		switch(err)
		{
		case 1:
			fputs("Copying cs229towav.exe to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/cs229towav.exe\" cs229towav.exe");

			if(system(cmd))
			{
				fputs("Unable to copy cs229towav.exe in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 2:
			fputs("Copying cs229towav to the running directory.\n",stdout);

			cmd[0] = '\0';
			strcat(cmd,"cp \"");
			strcat(cmd,get_directory());
			strcat(cmd,"/cs229towav\" cs229towav.exe");

			if(system(cmd))
			{
				fputs("Unable to copy cs229towav in ",stderr);
				fputs(get_directory(),stderr);
				fputs(" to running directory.\n",stderr);

				return EXIT_FAILURE;
			}

			b = TRUE;
			break;
		case 0:
		default:
			fputs("Unable to find the cs229towav executable for this student.\ncs229towav score: 0/255 points.\n",out);
			b = FALSE;

			break;
		}
#pragma endregion

		/* If b true then we found the cs229towav program */
		/* Scoring - Out of 255 */
		/* 42 points each for six successful runs */
		/* WE HAVE 3 FREE POINTS THAT WE DISTRIBUTE RANDOMLY (We've already done enough error checking) */
		if(b)
		{
			PFILE script; /* We will assume for short code that fopen successfully makes and closes a file every time */
			uint8_t pid[10];
			float score = 0.0f;

			fputs("cs229towav Tests\n",out);
			fputs("----------------\n\n",out);

#pragma region cs229towav Test 1
			fputs("Attempting first cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known4.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known4.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known4.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the first cs229towav test for this student. 0/43 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of first test failed. 0/43 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("First cs229towav test 43/43 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 43.0f / 80.0f;
						score += temp;
						fprintf(out,"First cs229towav test %d/43 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region cs229towav Test 2
			fputs("Attempting second cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known5.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known5.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known5.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the second cs229towav test for this student. 0/42 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of second test failed. 0/42 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("Second cs229towav test 42/42 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 42.0f / 80.0f;
						score += temp;
						fprintf(out,"Second cs229towav test %d/42 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region cs229towav Test 3
			fputs("Attempting third cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known6.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known6.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known6.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the third cs229towav test for this student. 0/42 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of third test failed. 0/42 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("Third cs229towav test 42/42 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 42.0f / 80.0f;
						score += temp;
						fprintf(out,"Third cs229towav test %d/42 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region cs229towav Test 4
			fputs("Attempting fourth cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known7.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known7.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known7.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the fourth cs229towav test for this student. 0/43 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of fourth test failed. 0/43 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("Fourth cs229towav test 43/43 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 43.0f / 80.0f;
						score += temp;
						fprintf(out,"Fourth cs229towav test %d/43 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region cs229towav Test 5
			fputs("Attempting fifth cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known8.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known8.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known8.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the fifth cs229towav test for this student. 0/43 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of fifth test failed. 0/43 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("Fifth cs229towav test 43/43 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 43.0f / 80.0f;
						score += temp;
						fprintf(out,"Fifth cs229towav test %d/43 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

#pragma region cs229towav Test 6
			fputs("Attempting sixth cs229towav test.\n",stdout);

			script = fopen("temporary_script.sh","wb");
			fputs("#!/bin/bash\n",script);
			fputs("./cs229towav.exe < known9.su > out",script);
			fclose(script);

			system("chmod u+x temporary_script.sh");
			system("bash ./run_temporary_script.sh");
			__sleep(CLOCKS_PER_SEC * 10); /* Ten seconds is a very generous time for cs229towav to finish */

			script = fopen("pid","rb");
			if(fscanf(script,"%s",&pid) != 1) /* We are garunteed for this to be what we want */
			{
				fputs("Unable to obtain pid of temporary script.",stderr);
				return EXIT_FAILURE;
			}
			fclose(script);

			cmd[0] = '\0';
			strcat(cmd,"kill ");
			strcat(cmd,pid);

			/* We don't care if this works; in fact we hope it doesn't */
			system(cmd);

#ifdef WINDOWS
			if(system("analysis out known9.su wav l su b c > analysis.out"))
			{
#else
			if(system("./analysis.exe out known9.su wav l su b c > analysis.out"))
			{
#endif
				fputs("Unable to analyize the sixth cs229towav test for this student. 0/42 points.\n",out);
				b = FALSE;
			}
			else
				b = TRUE;

			if(b)
			{
				uint32_t length_difference;
				uint32_t no_offset_matches;
				uint32_t offset;
				uint32_t num_matching_samples;
				uint32_t known_length;
				float known_mean;
				float known_variance;
				uint32_t test_length;
				float test_mean;
				float test_variance;
				float covariance;
				float XmYvariance;

				script = fopen("analysis.out","rb");

				if(script == (PFILE)NULL || script == (PFILE)BAD_PTR || script == (PFILE)UNDEF_PTR)
				{
					fputs("Unable to open the analysis file.\n",stderr);
					return EXIT_FAILURE;
				}

				/* I don't hate myself for this because I'm using my analysis program so output will be good or a total disaster */
				if(fscanf(script,"%d %d %d %d %d %f %f %d %f %f %f %f",&length_difference,&no_offset_matches,&offset,&num_matching_samples,&known_length,&known_mean,&known_variance,&test_length,&test_mean,&test_variance,&covariance,&XmYvariance) != 12)
					fputs("Analysis of sixth test failed. 0/42 points.\n",out);
				else
				{
					if(num_matching_samples == known_length && known_length == test_length)
					{
						score += 30.0f;
						fputs("Sixth cs229towav test 42/42 points.\n",out);
					}
					else
					{
						float temp;

						/* Upto 15 points for X - Y variance, reasonalby in the range 10 to -1 */
						temp = log10(XmYvariance);
						
						if(temp > 0.0f)
							temp = (10.0f - temp) * 3.5f;

						if(temp < 0.0f)
							temp = (1.0f + temp) * 5.0f + 35.0f;

						if(temp > 40.0f)
							temp = 40.0f;

						if(temp < 0.0f)
							temp = 0.0f;

						/* Upto 7.5 points for length similarity */
						if(known_length > test_length)
							temp += 20.0f * (1.0f - (known_length - test_length) / known_length);
						else
							temp += 20.0f * (1.0f - (test_length - known_length) / known_length);

						/* Upto 7.5 points for matching samples */
						temp += 20.0f * num_matching_samples / (known_length > test_length ? test_length : known_length);
						temp *= 80.0f / 67.0f;

						temp *= 42.0f / 80.0f;
						score += temp;
						fprintf(out,"Sixth cs229towav test %d/42 points.\n",(uint32_t)temp);
					}
				}

				if(fclose(script))
				{
					fputs("Unable to close analysis.out.\n",stderr);
					return EXIT_FAILURE;
				}
			} /* Score defaults to zero so we will never need to subtract points for failure */
#pragma endregion

			/* I don't particuarly care if we don't remove the files in between tests so we will only do this once */
			if(system("rm out analysis.out"))
			{
				fputs("Unable to remove cs229towav test files.",stderr);
				return EXIT_FAILURE;
			}

			if(system("rm cs229towav.exe temporary_script.sh pid"))
			{
				fputs("Unable to remove temporary files from cs229towav test.\n",stderr);
				return EXIT_FAILURE;
			}
		} /* We've already printed the zero score for not being able to find cs229towav */

		/* Everywhere that skips this (only after totally failing to make) will already have two but otherwise we only have one */
		fputc('\n',out);

		/* We need to get the directory back to the root */
		while(current_path.size)
			change_directory("..");

		/* Purge the data on [s] */
		destroy_stack(&s);

		if(fclose(out))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

		fprintf(stdout,"Student %s grading complete.\n\n",student.path);
		err = crawler_next_entry(&c,&student);
	} /* Student's path will always be null at the end so we don't need to free it */
	
	err = crawler_close(&c);
	
	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

#pragma region Removing Test Files
	if(system("bash ./remove_known.sh"))
	{
		fputs("remove_known.sh could not be executed.\n",stderr);
		return EXIT_FAILURE;
	}

	system("rm *.stackdump");
#pragma endregion

	return EXIT_SUCCESS;
}

#pragma region Utility Functions
void initialize_directory()
{
	initialize_stack(&current_path);
	return;
}

void change_directory(c_string path)
{
	uint8_t* newstr;

	if(!strcmp(path,"..\0"))
	{
		free(pop_stack(&current_path));
		return;
	}

	newstr = (uint8_t*)malloc(strlen(path) + 1);
	strcpy(newstr,path);

	push_stack(&current_path,(void*)newstr);
	return;
}

c_string get_directory()
{
	uint32_t i;
	uint8_t buff[1024];
	uint8_t* ret;
	buff[0] = '\0';

	if(current_path.size == 0)
		return "\0";

	for(i = current_path.size;i;i--)
	{
		strcat(buff,peek_at_stack(&current_path,i - 1));
		
		if(i != 1)
			strcat(buff,"/\0");
	}

	ret = (uint8_t*)malloc(strlen(buff) + 1);
	strcpy(ret,(c_string)buff);

	return ret;
}

uint32_t find_file(c_string fname, uint8_t case_sensitive, uint8_t isdir)
{
	STACK s;
	CRAWLER c;
	C_ENTRY entry;
	uint32_t depth = current_path.size;
	uint32_t err;
	uint32_t* last_entry_index;
	uint8_t* name;

	initialize_stack(&s);
	entry.path = (uint8_t*)NULL;

	last_entry_index = (uint32_t*)malloc(sizeof(uint32_t));
	*last_entry_index = 0;
	push_stack(&s,(void*)last_entry_index);

	name = (uint8_t*)malloc(strlen(fname) + 1);

	for(err = 0;fname[err];err++)
		if(isalpha(fname[err]))
			name[err] = fname[err] & ~0x20;
		else
			name[err] = fname[err];

	name[err] = '\0';

	while(s.size)
	{
		uint32_t i;

		err = crawler_open(&c,get_directory(),"temp2.crawler");

		if(err)
		{
			while(current_path.size > depth)
				change_directory("..");

			crawler_close(&c);
			destroy_stack(&s);
			//free((void*)last_entry_index);
			return err;
		}

		last_entry_index = (uint32_t*)pop_stack(&s);
		(*last_entry_index)++;

		for(i = 0;i < *last_entry_index;i++)
		{
			err = crawler_next_entry(&c,&entry);

			if(err)
			{
				while(current_path.size > depth)
					change_directory("..");

				crawler_close(&c);
				destroy_stack(&s);
				free((void*)last_entry_index);
				return err;
			}
		}

		if(!entry.path)
			if(s.size) /* We still might have more things to look into */
			{
				change_directory("..");
				crawler_close(&c);
				free((void*)last_entry_index);

				continue;
			}
			else /* We've explored the entire hierarchy and we failed */
			{
				crawler_close(&c);
				free((void*)last_entry_index);
				continue;
			}

		for(i = 0;entry.path[i];i++)
			if(isalpha(entry.path[i]))
				entry.path[i] &= ~0x20;

		if(!strcmp((c_string)name,(c_string)entry.path) && isdir == entry.is_dir)
		{
			crawler_close(&c);
			destroy_stack(&s);
			free((void*)last_entry_index);
			return SUCCESS;
		}

		if(entry.is_dir)
		{
			push_stack(&s,(void*)last_entry_index);

			last_entry_index = (uint32_t*)malloc(sizeof(uint32_t));
			(*last_entry_index) = 0;
			push_stack(&s,(void*)last_entry_index);

			change_directory((c_string)entry.path);
		}
		else
			push_stack(&s,(void*)last_entry_index);

		crawler_close(&c);
	}

	return 0xFFFFFFFF;
}

void __sleep(clock_t tics)
{
	clock_t start = clock();
	while(clock() - start < tics);

	return;
}
#pragma endregion

#endif