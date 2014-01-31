// vmmngr_pde.h
// Defines many functions relevant to page directories.

#ifndef VMMNGR_PDE_H
#define VMMNGR_PDE_H

#include <stdint.h>
#include <mmngr_phys.h>

enum PAGE_PDE_FLAGS
{
	I86_PDE_PRESENT = 1,
	I86_PDE_WRITABLE = 2,
	I86_PDE_USER = 4,
	I86_PDE_PWT = 8,
	I86_PDE_PCD = 16,
	I86_PDE_ACCESSED = 32,
	I86_PDE_DIRTY = 64,
	I86_PDE_4MB = 128,
	I86_PDE_CPU_GLOBAL = 256,
	I86_PDE_LV4_GLOBAL = 512,
	I86_PDE_FRAME = 0x7FFFF000
};

typedef uint32_t pd_entry;

extern void pd_entry_add_attribute(pd_entry* e, uint32_t attribute);
extern void pd_entry_del_attribute(pd_entry* e, uint32_t attribute);
extern void pd_entry_set_frame(pd_entry* e, physical_addr addr);
extern bool pd_entry_is_present(pd_entry e);
extern bool pd_entry_is_user(pd_entry e);
extern bool pd_entry_is_4mb(pd_entry e);
extern bool pd_entry_is_writable(pd_entry e);
extern physical_addr pd_entry_pfn(pd_entry e);
extern void pd_entry_enable_global(pd_entry e);

#endif