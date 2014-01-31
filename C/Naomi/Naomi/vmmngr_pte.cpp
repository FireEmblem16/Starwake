// vmmngr_pte.cpp
// Defines the functions from vmmngr_pte.h that provide data about page table entries.

#ifndef VMMNGR_PTE_CPP
#define VMMNGR_PTE_CPP

#include <vmmngr_pte.h>

#pragma warning(disable:4800)

inline void pt_entry_add_attribute(pt_entry* e, uint32_t attribute)
{
	*e |= attribute;
}

inline void pt_entry_del_attribute(pt_entry* e, uint32_t attribute)
{
	*e &= ~attribute;
}

inline void pt_entry_set_frame(pt_entry* e, uint32_t addr)
{
	*e = (*e & ~I86_PTE_FRAME) | addr;
}

inline bool pt_entry_is_present(pt_entry e)
{
	return e & I86_PTE_PRESENT;
}

inline bool pt_entry_is_writable(pt_entry e)
{
	return e & I86_PTE_WRITABLE;
}

inline physical_addr pt_entry_pfn(pt_entry e)
{
	return e & I86_PTE_FRAME;
}

#endif