// mmngr_stack.h
// Defines functions to manage the stack to avoid stack overflow.

#ifndef MMNGR_STACK_H
#define MMNGR_STACK_H

#include <mmngr_phys.h>
#include <stdint.h>
#include <size_t.h>

extern uint32_t StackSize;
extern uint32_t StackBase;

extern void mmngr_stack_init(physical_addr addr,size_t size);

#endif