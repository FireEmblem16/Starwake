#ifndef _DATA_STRUCTURES_C
#define _DATA_STRUCTURES_C

#include "../Headers/datastructures.h"

void initialize_stack(PSTACK s)
{
	if(s == (PSTACK)NULL || s == (PSTACK)BAD_PTR || s == (PSTACK)UNDEF_PTR)
		return;

	s->head = NULL;
	s->size = 0;

	return;
}

void destroy_stack(PSTACK s)
{
	PNODE n;

	if(s == (PSTACK)NULL || s == (PSTACK)BAD_PTR || s == (PSTACK)UNDEF_PTR)
		return;

	n = s->head;

	while(n)
	{
		PNODE n2 = n->next;

		free(n->data);
		free((void*)n);

		n = n2;
	}

	s->head = NULL;
	s->size = 0;

	return;
}

void push_stack(PSTACK s, void* data)
{
	PNODE n;

	if(s == (PSTACK)NULL || s == (PSTACK)BAD_PTR || s == (PSTACK)UNDEF_PTR)
		return;

	n = (PNODE)malloc(sizeof(NODE));
	n->data = data;
	n->next = s->head;
	s->head = n;
	s->size++;

	return;
}

void* pop_stack(PSTACK s)
{
	PNODE n;
	void* ret;

	if(s == (PSTACK)NULL || s == (PSTACK)BAD_PTR || s == (PSTACK)UNDEF_PTR)
		return NULL;

	if(!s->head)
		return NULL;

	n = s->head;
	ret = n->data;
	s->head = n->next;
	s->size--;
	free((void*)n);

	return ret;
}

void* peek_at_stack(PSTACK s, uint32_t index)
{
	PNODE n;

	if(s == (PSTACK)NULL || s == (PSTACK)BAD_PTR || s == (PSTACK)UNDEF_PTR)
		return NULL;

	if(index >= s->size)
		return NULL;

	n = s->head;

	for(;index;index--)
		n = n->next;

	return n->data;
}

#endif