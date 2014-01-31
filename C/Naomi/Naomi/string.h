// string.h
// Defines cstring function prototypes.

#ifndef STRING_H
#define STRING_H

#include <stdint.h>
#include <ctype.h>
#include <limits.h>

extern size_t strlen(const char* str);
extern int strcmp(const char* s1, const char* s2);
extern int strcmp_l(const char* s1, const char* s2, uint32_t len);
extern char* strcpy(char* s1, const char* s2);
extern void strrem(char* str, uint32_t len);

extern char* strchr(char* str, char character);
extern char* strchr(char* str, char* str2);
extern char* strchr(char* str, char* start, char character);

extern void* memcpy(void* dest, const void* src, size_t count);
extern void* memset(void* dest, uint8_t val, size_t count);
extern void* memsetw(void* dest, uint16_t val, size_t count);

#endif