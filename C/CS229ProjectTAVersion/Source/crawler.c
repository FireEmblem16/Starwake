#ifndef _CRAWLER_C
#define _CRAWLER_C

#pragma warning(disable:4996)

#include "../Headers/crawler.h"

uint32_t crawler_open(PCRAWLER c, c_string path, c_string temp_path)
{
	uint8_t* cmd_buff;
	uint32_t len;

	if(c == (PCRAWLER)NULL || c == (PCRAWLER)BAD_PTR || c == (PCRAWLER)UNDEF_PTR || path == (c_string)NULL || path == (c_string)BAD_PTR || path == (c_string)UNDEF_PTR || temp_path == (c_string)NULL || temp_path == (c_string)BAD_PTR || temp_path == (c_string)UNDEF_PTR)
		return NULL_POINTER;

	c->valid = FALSE;
	c->cd = (uint8_t*)malloc(strlen(path) + 1);

	if(c->cd == (uint8_t*)NULL || c->cd == (uint8_t*)BAD_PTR || c->cd == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	strcpy(c->cd,path);

	c->temp = (uint8_t*)malloc(strlen(temp_path) + 1);

	if(c->temp == (uint8_t*)NULL || c->temp == (uint8_t*)BAD_PTR || c->temp == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	strcpy(c->temp,temp_path);

	/* If [c->cd] is not an empty string then we will put quotes around it */
	len = strlen(c->temp) + strlen(c->cd) + 16 + (c->cd[0] ? 2 : 0);
	cmd_buff = (uint8_t*)malloc(len);

	if(cmd_buff == (uint8_t*)NULL || cmd_buff == (uint8_t*)BAD_PTR || cmd_buff == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	cmd_buff[0] = '\0';

	strcat(cmd_buff,"ls -g -o -F \0");

	if(c->cd[0] && c->cd[0] != '\"')
		strcat(cmd_buff,"\"\0");

	strcat(cmd_buff,c->cd);

	if(c->cd[0] && c->cd[0] != '\"')
		strcat(cmd_buff,"\"\0");

	strcat(cmd_buff," > \0");
	strcat(cmd_buff,c->temp);

	if(system((c_string)cmd_buff))
		return IO_EXCEPTION;

	free((void*)cmd_buff);

	c->tempf = fopen(temp_path,"rb");

	if(c->tempf == (PFILE)NULL || c->tempf == (PFILE)BAD_PTR || c->tempf == (PFILE)UNDEF_PTR)
		return IO_EXCEPTION;

	/* We need to read past "total ___" */
	while(fgetc(c->tempf) != '\n');

	c->valid = TRUE;
	return SUCCESS;
}

uint32_t crawler_close(PCRAWLER c)
{
	uint32_t i;
	uint32_t len;
	uint8_t* cmd_buff;

	if(c == (PCRAWLER)NULL || c == (PCRAWLER)BAD_PTR || c == (PCRAWLER)UNDEF_PTR)
		return NULL_POINTER;

	if(!c->valid)
		return NULL_POINTER;

	c->valid = FALSE;

	if(fclose(c->tempf))
		return FILE_LEFT_OPEN;

	len = strlen(c->temp) + 4;
	cmd_buff = (uint8_t*)malloc(len);

	if(cmd_buff == (uint8_t*)NULL || cmd_buff == (uint8_t*)BAD_PTR || cmd_buff == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < 3;i++)
		cmd_buff[i] = "rm "[i];

	for(;c->temp[i - 3];i++)
		cmd_buff[i] = c->temp[i - 3];

	cmd_buff[i] = '\0';

	if(system(cmd_buff))
		return IO_EXCEPTION;

	free((void*)c->cd);
	free((void*)c->temp);

	return SUCCESS;
}

uint32_t crawler_reopen(PCRAWLER c)
{
	if(c == (PCRAWLER)NULL || c == (PCRAWLER)BAD_PTR || c == (PCRAWLER)UNDEF_PTR)
		return NULL_POINTER;

	if(!c->valid)
		return NULL_POINTER;

	if(fclose(c->tempf))
		return IO_EXCEPTION;

	c->valid = FALSE;

	if(fopen(c->temp,"rb"))
		return IO_EXCEPTION;

	/* We need to read past "total ___" */
	while(fgetc(c->tempf) != '\n');

	c->valid = TRUE;
	return SUCCESS;
}

uint32_t crawler_next_entry(PCRAWLER c, PC_ENTRY dest)
{
	/* File names have a max limit so this is more than enough space */
	uint8_t buff[1024];
	uint32_t i;
	uint32_t offset;

	if(c == (PCRAWLER)NULL || c == (PCRAWLER)BAD_PTR || c == (PCRAWLER)UNDEF_PTR || dest == (PC_ENTRY)NULL || dest == (PC_ENTRY)BAD_PTR || dest == (PC_ENTRY)UNDEF_PTR)
		return NULL_POINTER;

	if(!c->valid)
		return NULL_POINTER;

	/* Read until the end of the line */
	for(i = 0;!feof(c->tempf) && (buff[i] = fgetc(c->tempf)) != '\n';i++);

	if(ferror(c->tempf))
		return IO_EXCEPTION;

	/* Need to null data if we passed the end of the file */
	if(feof(c->tempf))
	{
		if(dest->path)
			free((void*)dest->path);

		dest->path = NULL;
		return SUCCESS;
	}

	/* This will remove the newline */
	buff[i] = '\0';

	if(dest->path)
		free((void*)dest->path);

	/* We have to find the filename so we read through the other parts */
	for(i = 0;buff[i] != ' ';i++); /* End of premission bits */
	for(;buff[i] == ' ';i++); /* Get to unknown item */
	for(;buff[i] != ' ';i++); /* End of unknown item */
	for(;buff[i] == ' ';i++); /* Get to size */
	for(;buff[i] != ' ';i++); /* End of size */
	for(;buff[i] == ' ';i++); /* Get to month */
	for(;buff[i] != ' ';i++); /* End of month */
	for(;buff[i] == ' ';i++); /* Get to day */
	for(;buff[i] != ' ';i++); /* End of day */
	for(;buff[i] == ' ';i++); /* Get to time or year */
	for(;buff[i] != ' ';i++); /* End of time or year */
	for(;buff[i] == ' ';i++); /* Get to file name */

	dest->path = (uint8_t*)malloc(strlen((c_string)(buff + i)));

	for(offset = i;buff[i];i++)
		dest->path[i - offset] = buff[i];

	if(dest->path[i - offset - 1] == '*')
		dest->is_dir = FALSE;
	else if(dest->path[i - offset - 1] == '/')
		dest->is_dir = TRUE;
	else /* Apparently not all files have the '*' extension, like object files which have no extension */
		dest->is_dir = FALSE;/*return UNEXPECTED_FILE_TYPE;*/

	/* This will replace the ls marker for file or directory */
	dest->path[i - offset - 1] = '\0';

	return SUCCESS;
}

#endif