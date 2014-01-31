// mmngr_stack.h
// Provides many functions to manage the stack.

#ifndef MMNGR_STACK_CPP
#define MMNGR_STACK_CPP

#include <mmngr_stack.h>
#include <size_t.h>
#include <string.h>

physical_addr StackBase = 0;
size_t StackSize = 0;
physical_addr old_StackBase = 0;
size_t old_StackSize = 0;

void mmngr_stack_init(physical_addr addr, size_t size)
{
	old_StackBase = addr;
	old_StackSize = size;

	pmmngr_deinit_region(addr - size,size);

	return;
}

#endif