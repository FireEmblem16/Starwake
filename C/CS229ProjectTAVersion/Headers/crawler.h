#ifndef _CRAWLER_H
#define _CRAWLER_H

#include <stdlib.h>
#include <string.h>
#include "defines.h"

typedef struct c_entry
{
	uint8_t* path;
	uint8_t is_dir;
} C_ENTRY, *PC_ENTRY;

typedef struct crawler
{
	PFILE tempf;
	uint8_t* cd;
	uint8_t* temp;

	uint8_t valid; /* DO NOT MODIFY, Functions avoid errors by checking against this */
} CRAWLER, *PCRAWLER;

/* Opens the directory [path] in [c] and uses [temp_path] as a temporary file. */
/* Returns SUCCESS if no problems were encountered. */
/* Returns NULL_POINTER if [c], [path] or [temp_path] is not valid. */
/* Returns BUFFER_OVERFLOW if room for the current directory or temp_path could not be made. */
/* Returns IO_EXCEPTION if the crawler was not opened successfully. */
uint32_t crawler_open(PCRAWLER c, c_string path, c_string temp_path);

/* Closes the directory open in [c]. */
/* Returns SUCCESS if no problems were encountered. */
/* Returns NULL_POINTER if [c]is not valid. */
/* Returns FILE_LEFT_OPEN if unable to close the file opened in [c]. */
uint32_t crawler_close(PCRAWLER c);

/* Reopens the [c] in its current directory at the beginning of the file list. */
/* Note that if the directory listing has changed a refresh is still required. */
/* Returns SUCCESS if no problems were encountered. */
/* Returns NULL_POINTER if [c] is not valid. */
/* Returns IO_EXCEPTION if unable to reopen the directory contents. */
uint32_t crawler_reopen(PCRAWLER c);

/* Puts the next entry in [dest] or NULLs the data in [dest] if no more entries are avialable. */
/* Note that [path] in [dest] will be freed if it is not NULL. */
/* Returns SUCCESS if no problems were encountered. */
/* Returns NULL_POINTER if [c] or [dest] is not valid. */
/* Returns IO_EXCEPTION if unable to read from the temporary file buffer. */
/* Returns UNEXPECTED_FILE_TYPE if a file type other than file or directory is encountered. */
uint32_t crawler_next_entry(PCRAWLER c, PC_ENTRY dest);

#endif