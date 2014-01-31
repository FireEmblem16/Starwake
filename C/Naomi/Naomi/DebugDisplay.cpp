// DebugDisplay.cpp
// Defines the functions in DebugDisplay.h

#ifndef DEBUGDISPLAY_CPP
#define DEBUGDISPLAY_CPP

#include <stdarg.h>
#include <stdint.h>
#include <string.h>
#include <hal.h>
#include <DebugDisplay.h>
#include <mmngr_phys.h>

uint16_t* video_memory = (uint16_t*)0xB8000;
uint8_t cursor_x = 0;
uint8_t cursor_y = 0;
uint8_t	_color=0;

#ifdef _MSC_VER
#pragma warning (disable:4244)
#endif

void DebugUpdateCur(int x, int y)
{
	uint16_t cursorLocation = x + 80 * y;

	outportb(0x3D4,14);
	outportb(0x3D5,cursorLocation >> 8);
	outportb(0x3D4,15);
	outportb(0x3D5,cursorLocation);

	return;
}

void Scroll()
{
	uint16_t attribute = _color << 8;

	for(uint32_t i = 0;i < 24 * 80;i++)
		video_memory[i] = video_memory[i+80];

	for(uint32_t i = 24 * 80;i < 25 * 80;i++)
		video_memory[i] = ' ' | attribute;

	return;
}

void DebugPutc(unsigned char c)
{
	uint16_t attribute = _color << 8;

	if(c == 0x08 && cursor_x)
		cursor_x--;
	else if(c == 0x09)
		cursor_x = (cursor_x+8) & ~(8-1);
	else if(c == '\t')
	{
		for(uint16_t i = 0;i < 3;i++)
			DebugPutc(' ');
	}
	else if(c == '\r')
		cursor_x = 0;
	else if(c == '\n')
	{
		cursor_x = 0;
		cursor_y++;
	}
	else if(c >= ' ')
	{
        uint16_t* location = video_memory + (cursor_y * 80 + cursor_x);
		*location = c | attribute;
		cursor_x++;
	}

	if (cursor_x >= 80)
	{
		cursor_x = 0;
		cursor_y++;
	}

	if(cursor_y > 24)
	{
		cursor_y = 24;
		Scroll();
	}

	DebugUpdateCur(cursor_x,cursor_y);
}

char tbuf[32];
char bchars[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

void itoa(unsigned i, unsigned base, char* buf)
{
	int pos = 0;
	int opos = 0;
	int top = 0;

	if(i == 0 || base > 16)
		{
		buf[0] = '0';
		buf[1] = '\0';
		return;
	}

	while(i != 0)
	{
		tbuf[pos++] = bchars[i % base];
		i /= base;
	}
   
	top = pos--;

	for(opos = 0;opos < top;pos--,opos++)
		buf[opos] = tbuf[pos];

	buf[opos] = 0;

	return;
}

void itoa_s(int i, unsigned base, char* buf)
{
	if(base > 16)
		return;

	if(i < 0)
	{
		*buf++ = '-';
		i *= -1;
	}
   
	itoa(i,base,buf);
	
	return;
}

unsigned DebugSetColor(const unsigned c)
{
	unsigned t = _color;
	_color = c;
	return t;
}

void DebugGotoXY(unsigned x, unsigned y)
{
	if(cursor_x <= 80)
		cursor_x = x;

	if(cursor_y <= 25)
		cursor_y = y;

	DebugUpdateCur(cursor_x,cursor_y);
}

void DebugGetXY(unsigned* x, unsigned* y)
{
	if(x == 0 || y == 0)
		return;

	*x = cursor_x;
	*y = cursor_y;

	return;
}

int DebugGetHorz()
{
	return 80;
}

int DebugGetVert()
{
	return 25;
}

void DebugClrScr(const unsigned short c)
{
	for(int i = 0;i < 80 * 25;i++)
		video_memory[i] = ' ' | (c << 8);

	DebugGotoXY(0,0);
}

void DebugPuts(char* str)
{
	if(!str)
		return;

	for(unsigned int i = 0;i < strlen(str);i++)
		DebugPutc(str[i]);
}

int DebugPrintf(const char* str, ...)
{
	if(!str)
		return 0;

	va_list	args;
	va_start(args,str);

	size_t i;

	for(i = 0;i<strlen(str);i++)
	{
		switch(str[i])
		{
		case '%':
			switch(str[i+1])
			{
			/*** Character ***/
			case 'c':
			{
				char c = va_arg(args,char);
				DebugPutc(c);
				i++;
				break;
			}

			/*** String ***/
			case 's':
			{
				char* c = va_arg(args,char*);
				DebugPuts(c);
				i++;
				break;
			}

			/*** Address of Data ***/
			case 'a':
			{
				int c = (int&)va_arg(args,char);
				char str[32] = {0};
				itoa(c,16,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Unsigned Binary ***/
			case 'B':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa(c,2,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Signed Binary ***/
			case 'b':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa_s(c,2,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Unsigned Integers ***/
			case 'D':
			case 'I':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa(c,10,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Signed Integers ***/
			case 'd':
			case 'i':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa_s(c,10,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Integers in Unsigned Hexadecimal ***/
			case 'X':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa(c,16,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Integers in Signed Hexadecimal ***/
			case 'x':
			{
				int c = va_arg(args,int);
				char str[32] = {0};
				itoa_s(c,16,str);
				DebugPuts(str);
				i++;
				break;
			}

			/*** Print the % Sign ***/
			case '%':
			{
				DebugPutc('%');
				i++;
				break;
			}

			/*** Bad Formating ***/
			default:
			{
				va_end(args);
				return 1;
			}
			}
			
			break;
		default:
			DebugPutc(str[i]);
			break;
		}
	}

	va_end(args);
	return i;
}

#endif