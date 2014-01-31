#ifndef DEBUG_CMD_CPP
#define DEBUG_CMD_CPP

#pragma warning(disable:4127)

#include <Debugcmd.h>
#include <string.h>
#include <stdint.h>
#include <stdio.h>
#include <flpydsk.h>
#include <fsys.h>
#include <naomi.h>
#include <DebugDisplay.h>

void DebugcmdStart();
void DebugcmdPrompt();
bool DebugValidateParsePath(char* path);
void DebugSetCD();
void DebugReadSector();
bool DebugReadFile(char* path);
void DebugGetNextCommand(char* buf,uint32_t len);
bool DebugRunCommand(char* buf);

char currentDir[1024];

void DebugcmdStart()
{
	DebugPuts("Naomi OS debug command prompt - 2010 Omacron-LURYI\n");
	strcpy(currentDir,"a:/");
	currentDir[3] = '\0';

	return;
}

void DebugcmdPrompt()
{
	DebugPuts(currentDir);
	DebugPuts("> ");

	return;
}

bool DebugValidateParsePath(char* path)
{
	char buf[1024];
	strcpy(buf,path);

	if(*(strchr(buf,'\0') - 1) == '/' && *(strchr(buf,'\0') - 2) != ':')
		*(strchr(buf,'\0') - 1) = '\0';

	for(char* p = buf;strchr(p,"..");)
	{
		char* x = strchr(p,"..");
		
		if(*(x-2) == ':')
		{
			strcpy(path,buf);
			return false;
		}

		if(*(x-1) != '/' || *(x+2) != '/' && *(x+3) != '\0')
			continue;

		char* y = strchr(buf,(x-2),'/');
		
		if(*(x+3) == '\0')
			strrem(y + 1,(uint32_t)(x + 1 - y));
		else
			strrem(y,(uint32_t)(x + 2 - y));
	}

	strcpy(path,buf);
	FILE file = volOpenFile(path);

	if(file.flags == FS_DIRECTORY)
		return true;

	if(strchr(path,":/") && strlen(path) == 4)
		return true;

	return false;
}

void DebugSetCD()
{
	char buf[1024];
	DebugPuts("\n>");
	DebugGetNextCommand(buf,1024);

	if(!strchr(buf,':'))
	{
		char buff[1024];
		strcpy(buff,currentDir);
		strcpy((char*)(buff + strlen(buff) - 1),buf);
		strcpy(buf,buff);
	}
	
	if(DebugValidateParsePath(buf))
		strcpy(currentDir,buf);
	else
	{
		DebugPrintf("\n%s is not a valid path.\n",buf);
		return;
	}

	char* temp = strchr(currentDir,'\0');

	if(*(temp - 1) != '/')
	{
		*temp = '/';
		*(temp + 1) = '\0';
	}

	return;
}

void DebugReadSector()
{
	uint32_t sectornum = 0;
	char sectornumbuf[6];
	uint8_t* sector = 0;

	DebugPrintf("\nEnter the sector to be read>");
	DebugGetNextCommand(sectornumbuf,5);
	sectornum = atoi(sectornumbuf);

	DebugPrintf("Sector: %i Contains:\n\n",sectornum);

	sector = flpydsk_read_sector(sectornum);

	if(sector != 0)
	{
		for(int c = 0,i = 0;c < 4;c++,i += 128)
		{
			for(int j = 0;j < 128;j++)
				DebugPrintf("0x%X ",sector[i+j]);

			if(c < 3)
			{
				DebugPuts("\n\nPress any key to continue\n\n");
				while(getchar() == 0);
			}
			else
				DebugPutc('\n');
		}
	}
	else
		DebugPrintf("\nError reading foppy disk.");

	DebugPrintf("\n");

	return;
}

bool DebugReadFile(char* path)
{
	if(!strchr(path,':'))
	{
		char buf[1024];
		strcpy(buf,currentDir);
		strcpy((char*)(buf + strlen(buf) - 1),path);
		path = buf;
	}

	FILE file = volOpenFile(path);

	if(file.flags == FS_INVALID)
		return false;

	if((file.flags & FS_DIRECTORY) == FS_DIRECTORY)
		return false;

	DebugPuts("\n");

	while(!file.eof)
	{
		unsigned char buf[512];

		volReadFile(&file,buf,512);

		for(int i = 0;i < 512;i++)
			DebugPutc(buf[i]);

		if(!file.eof)
		{
			DebugPuts("\n\nPress any key to continue\n\n");
			while(getchar() == 0);
		}
	}

	return true;
}

void DebugGetNextCommand(char* buf, uint32_t len)
{
	KEYCODE key = KEY_UNKNOWN;
	bool BufChar;

	uint32_t i = 0;
	while(i < len)
	{
		key = getkey();
		BufChar = true;

		while(key == KEY_UNKNOWN)
			key = getkey();

		if(key == KEY_RETURN || key == KEY_KP_ENTER)
			break;

		if(key == KEY_BACKSPACE)
		{
			BufChar = false;

			if(i > 0)
			{
				unsigned int x,y;
				DebugGetXY(&x,&y);

				if(x > 0)
					DebugGotoXY(--x,y);
				else
				{
					y--;
					x = DebugGetHorz() - 1;
				}

				DebugPutc(' ');
				DebugGotoXY(x,y);
				i--;
			}
		}

		if(BufChar)
		{
			char c = kybrd_key_to_ascii(key);

			if(c)
			{
				DebugPutc(c);
				buf[i++] = c;
			}
		}
	}

	buf[i] = '\0';
	DebugPutc('\n');

	return;
}

bool DebugRunCommand(char* buf)
{
	if(strcmp(buf,"exit") == 0)
		return true;
	else if(strcmp(buf,"cd") == 0)
		DebugSetCD();
	else if(strcmp(buf,"cls") == 0)
		DebugClrScr(33);
	else if(strcmp(buf,"help") == 0)
	{
		DebugPuts("cd        - Sets the current directory.");
		DebugPuts("\ncls     - Clears the screen of all output.");
		DebugPuts("\nhelp    - Displays the list of all supported commands.");
		DebugPuts("\nreadsec - Reads a sector from the floppy drive and displays it in hex.");
		DebugPuts("\nreset   - Resets the computer.");
		DebugPuts("\nexit    - Exits the command prompt.");
		DebugPuts("\n\n");
	}
	else if(strcmp(buf,"reset") == 0)
		kybrd_reset_system();
	else if(strcmp(buf,"readsec") == 0)
		DebugReadSector();
	else if(DebugReadFile(buf))
		DebugPuts("\n\n---------------EOF---------------\n");
	else
		DebugPuts("No such file was found.\n");

	return false;
}

void DebugRuncmd()
{
	char cmd_buf[1024];
	DebugcmdStart();

	while(true)
	{
		DebugcmdPrompt();
		DebugGetNextCommand(cmd_buf,1022);

		if(DebugRunCommand(cmd_buf))
			break;
	}

	return;
}

#endif