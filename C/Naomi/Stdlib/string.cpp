// string.cpp
// Defines the functions in string.h

#ifndef STRING_CPP
#define STRING_CPP

#include <string.h>

#pragma warning (disable:4706)

size_t strlen(const char* str)
{
	size_t len=0;
	while(str[len++]);
	return len;
}

int strcmp(const char* s1, const char* s2)
{
	int res = 0;

	while(!(res = *(unsigned char*)s1 - *(unsigned char*)s2) && *s1 && *s2)
		++s1,++s2;

	if(res < 0)
		res = -1;
	if(res > 0)
		res = 1;

	return res;
}

int strcmp_l(const char* s1, const char* s2, uint32_t len)
{
	int res = 0;

	while(len-- > 0 && !(res = *(unsigned char*)s1 - *(unsigned char*)s2) && *s1 && *s2)
		++s1,++s2;

	if(res < 0)
		res = -1;
	if(res > 0)
		res = 1;

	return res;
}

char* strcpy(char* s1, const char* s2)
{
    char* s1_p = s1;
    while(*s1++ = *s2++);
    return s1_p;
}

void strrem(char* str, uint32_t len)
{
	if(strlen(str) < len)
	{
		*str = '\0';
		return;
	}

	strcpy(str,(const char*)(str + len));
	return;
}

char* strchr(char* str, char character)
{
	do
	{
		if(*str == character)
			return str;
	}
	while(*str++);

	return 0;
}

char* strchr(char* str, char* str2)
{
	do
	{
		if(*str == *str2)
			for(char* i = str, * j = str2, * k = i;j < (str2 + strlen(str2) + 1) && i < (str + strlen(str) + 1);i++,j++)
				if(*i != *j)
					break;
				else
					if(*(j + 1) == 0)
						return k;
	}
	while(*str++);

	return 0;
}

char* strchr(char* str, char* start, char character)
{
	if(start < str)
		return 0;

	do
	{
		if(*start == character)
			return start;
	}
	while(start-- != str);

	return 0;
}

void* memcpy(void* dest, const void* src, size_t count)
{
    const char* sp = (const char*)src;
    char* dp = (char*)dest;
    for(;count != 0;count--)
		*dp++ = *sp++;

    return dest;
}

void* memset(void* dest, uint8_t val, size_t count)
{
    unsigned char* temp = (unsigned char*)dest;
	for(;count != 0;count--,temp[count] = val);
	return dest;
}

void* memsetw(void* dest, uint16_t val, size_t count)
{
    unsigned short* temp = (unsigned short*)dest;
    for(;count != 0;count--,*temp++ = val);
    return dest;
}

#endif