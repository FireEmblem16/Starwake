// vmmngr_pte.h
// Defines many functions relevant to page table entries.

#ifndef VMMNGR_PTE_H
#define VMMNGR_PTE_H

#include <stdint.h>
#include <mmngr_phys.h>

enum PAGE_PTE_FLAGS
{
	I86_PTE_PRESENT = 1,
	I86_PTE_WRITABLE = 2,
	I86_PTE_USER = 4,
	I86_PTE_WRITETHROUGH = 8,
	I86_PTE_NOT_CATCHABLE = 16,
	I86_PTE_ACCESSED = 32,
	I86_PTE_DIRTY = 64,
	I86_PTE_PAT = 128,
	I86_PTE_CPU_GLOBAL = 256,
	I86_PTE_LV4_GLOBAL = 512,
	I86_PTE_FRAME = 0x7FFFF000
};

typedef uint32_t pt_entry;

extern void pt_entry_add_attribute(pt_entry* e, uint32_t attribute);
extern void pt_entry_del_attribute(pt_entry* e, uint32_t attribute);
extern void pt_entry_set_frame(pt_entry* e, physical_addr addr);
extern bool pt_entry_is_present(pt_entry e);
extern bool pt_entry_is_writable(pt_entry e);
extern physical_addr pt_entry_pfn(pt_entry e);

#endif