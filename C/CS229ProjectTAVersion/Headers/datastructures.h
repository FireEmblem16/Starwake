#ifndef _DATA_STRUCTURES_H
#define _DATA_STRUCTURES_H

#include <stdlib.h>
#include "defines.h"

typedef struct node
{
	struct node* next;
	void* data;

} NODE, *PNODE;

typedef struct stack
{
	PNODE head;
	uint32_t size;
} STACK, *PSTACK;

/* Initializes [s]. */
/* All data will be lost so beware memory leaks. */
void initialize_stack(PSTACK s);

/* Frees all used memory in [s]. */
void destroy_stack(PSTACK s);

/* Adds [data] to the stack [s]. */
/* Will not create new memory for [data]. */
void push_stack(PSTACK s, void* data);

/* Returns the top element in [s] or NULL if empty. */
/* Will not create new memory for the returned data. */
void* pop_stack(PSTACK s);

/* Returns the data at [index] in the stack [s]. */
/* Returns NULL if [index] is out of bounds or [s] is not valid. */
void* peek_at_stack(PSTACK s, uint32_t index);

#endif