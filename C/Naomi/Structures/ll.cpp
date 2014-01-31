#ifndef LL_C
#define LL_C

#include <ll.h>
#include <_null.h>
#include <mmngr_phys.h>

PLINKEDLIST CreateSinglyLinkedList()
{
	PLINKEDLIST list = (PLINKEDLIST)pmalloc((size_t)sizeof(struct LinkedList));

	list->head = NULL;
	list->tail = NULL;
	list->size = 0;

	list->create = &CreateSinglyLinkedList;

	return list;
}

PLINKEDLIST CreateDoublyLinkedList()
{
	PLINKEDLIST list = (PLINKEDLIST)pmalloc((size_t)sizeof(struct LinkedList));

	list->head = NULL;
	list->tail = NULL;
	list->size = 0;
	
	list->create = &CreateDoublyLinkedList;

	return list;
}

#endif