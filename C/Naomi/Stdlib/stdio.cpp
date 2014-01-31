// stdio.cpp
// Defines the functions created in stdio.h

#ifndef STDIO_CPP
#define STDIO_CPP

#pragma warning(disable:4146)

#include <size_t.h>
#include <stdarg.h>
#include <va_list.h>
#include <string.h>

extern void itoa(unsigned int i, unsigned int base, char* buf);
extern void itoa_s(int i, unsigned int base, char* buf);

int vsprintf(char* str, const char* format, va_list ap)
{
	if(!str)
		return 0;

	if(!format)
		return 0;

	size_t loc = 0;
	size_t i;

	for(i = 0;i <= strlen(format);i++,loc++)
	{
		switch(format[i])
		{
		case '%':
			switch(format[i+1])
			{
			/*** Character ***/
			case 'C':	
			case 'c':
			{
				char c = va_arg(ap,char);
				str[loc] = c;
				i++;
				break;
			}

			/*** String ***/
			case 'S':
			case 's':
			{
				int c = (int&)va_arg(ap,char);
				char s[32] = {0};
				strcpy(s,(const char*)c);						
				strcpy(&str[loc],s);
				i++;
				loc += strlen(s) - 2;
				break;
			}

			/*** Address of Data ***/
			case 'A':
			case 'a':
			{
				int c = (int&)va_arg(ap,char);
				char s[32] = {0};
				itoa(c,16,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Unsigned Binary ***/
			case 'B':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa(c,2,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Signed Binary ***/
			case 'b':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa_s(c,2,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Unsigned Integers ***/
			case 'D':
			case 'I':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa(c,10,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Signed Integers ***/
			case 'd':
			case 'i':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa_s(c,10,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Integers in Unsigned Hexadecimal ***/
			case 'X':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa(c,16,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Integers in Signed Hexadecimal ***/
			case 'x':
			{
				int c = va_arg(ap,int);
				char s[32] = {0};
				itoa_s(c,16,s);
				strcpy(&str[loc],s);
				loc += strlen(s) - 2;
				i++;
				break;
			}

			/*** Print the % Sign ***/
			case '%':
			{
				char c = '%';
				str[loc] = c;
				i++;
				break;
			}

			/*** Bad Formating ***/
			default:
				str[loc] = format[i];
				break;
			}
			break;
		default:
			str[loc] = format[i];
			break;
		}
	}

	return i;
}

long strtol(const char* nptr, char** endptr, int base)
{
	const char* s = nptr;
	unsigned long acc;
	int c;
	unsigned long cutoff;
	int neg = 0, any, cutlim;

	do
	{
		c = *s++;
	}
	while(isspace(c));

	if(c == '-')
	{
		neg = 1;
		c = *s++;
	}
	else if(c == '+')
		c = *s++;

	if((base == 0 || base == 16) && c == '0' && (*s == 'x' || *s == 'X'))
	{
		c = s[1];
		s += 2;
		base = 16;
	}
	else if((base == 0 || base == 2) && c == '0' && (*s == 'b' || *s == 'B'))
	{
		c = s[1];
		s += 2;
		base = 2;
	}

	if(base == 0)
		base = (c == '0' ? 8 : 10);

	cutoff = neg ? -(unsigned long)LONG_MIN : LONG_MAX;
	cutlim = cutoff % (unsigned long)base;
	cutoff /= (unsigned long)base;

	for(acc = 0, any = 0;;c = *s++)
	{
		if(isdigit(c))
			c -= '0';
		else if(isalpha(c))
			c -= isupper(c) ? 'A' - 10 : 'a' - 10;
		else
			break;

		if(c >= base)
			break;

		if(any < 0 || acc > cutoff || acc == cutoff && c > cutlim)
			any = -1;
		else
		{
			any = 1;
			acc *= base;
			acc += c;
		}
	}

	if(any < 0)
		acc = neg ? LONG_MIN : LONG_MAX;
	else if(neg)
		acc = -acc;

	if(endptr != 0)
		*endptr = (char*)(any ? s - 1 : nptr);

	return (acc);
}

unsigned long strtoul(const char* nptr, char** endptr, int base)
{
	const char* s = nptr;
	unsigned long acc;
	int c;
	unsigned long cutoff;
	int neg = 0, any, cutlim;

	do
	{
		c = *s++;
	}
	while(isspace(c));

	if(c == '-')
	{
		neg = 1;
		c = *s++;
	}
	else if(c == '+')
		c = *s++;

	if((base == 0 || base == 16) && c == '0' && (*s == 'x' || *s == 'X'))
	{
		c = s[1];
		s += 2;
		base = 16;
	}
	else if((base == 0 || base == 2) && c == '0' && (*s == 'b' || *s == 'B'))
	{
		c = s[1];
		s += 2;
		base = 2;
	}

	if (base == 0)
		base = c == '0' ? 8 : 10;

	cutoff = (unsigned long)ULONG_MAX / (unsigned long)base;
	cutlim = (unsigned long)ULONG_MAX % (unsigned long)base;
	
	for(acc = 0, any = 0;;c = *s++)
	{
		if(isdigit(c))
			c -= '0';
		else if(isalpha(c))
			c -= isupper(c) ? 'A' - 10 : 'a' - 10;
		else
			break;

		if(c >= base)
			break;

		if(any < 0 || acc > cutoff || acc == cutoff && c > cutlim)
			any = -1;
		else
		{
			any = 1;
			acc *= base;
			acc += c;
		}
	}

	if(any < 0)
	{
		acc = ULONG_MAX;
	}
	else if(neg)
		acc = -acc;

	if(endptr != 0)
		*endptr = (char*)(any ? s - 1 : nptr);

	return (acc);
}

int atoi(const char* str)
{
	return (int)strtol(str,0,10);
}

#endif