#ifndef LL_H
#define LL_H

#include <stdint.h>

typedef struct node
{
	struct node* next;
	void* data;
} NODE, *PNODE;

typedef struct dnode
{
	struct dnode* next;
	struct dnode* prev;
	void* data;
} DNODE, *PDNODE;

typedef struct LinkedList
{
	struct LinkedList* (*create)();
	void (*destroy)();
	uint32_t (*add)(void* data);
	uint32_t (*addat)(void* data, uint32_t index);
	void* (*remove)(uint32_t index);
	void* (*get)(uint32_t index);
	void* (*getnode)(uint32_t index);
	uint32_t (*contains)(void* data, uint32_t datasize);
	
	void* head;
	void* tail;

	uint32_t size;
} LINKEDLIST, *PLINKEDLIST;



extern PLINKEDLIST CreateSinglyLinkedList();
extern PLINKEDLIST CreateDoubleLinkedList();

#endif