// vmmngr_pde.h
// Defines a bunch of functions that provide data on page directory entries.

#ifndef VMMNGR_PDE_CPP
#define VMMNGR_PDE_CPP

#include <vmmngr_pde.h>

#pragma warning(disable:4100)
#pragma warning(disable:4800)

inline void pd_entry_add_attribute(pd_entry* e, uint32_t attribute)
{
	*e |= attribute;
}

inline void pd_entry_del_attribute(pd_entry* e, uint32_t attribute)
{
	*e &= ~attribute;
}

inline void pd_entry_set_frame(pd_entry* e, physical_addr addr)
{
	*e |= (*e & ~I86_PDE_FRAME) | addr;
}

inline bool pd_entry_is_present(pd_entry e)
{
	return e & I86_PDE_PRESENT;
}

inline bool pd_entry_is_user(pd_entry e)
{
	return e & I86_PDE_USER;
}

inline bool pd_entry_is_4mb(pd_entry e)
{
	return e & I86_PDE_4MB;
}

inline bool pd_entry_is_writable(pd_entry e)
{
	return e & I86_PDE_WRITABLE;
}

inline physical_addr pd_entry_pfn(pd_entry e)
{
	return e & I86_PDE_FRAME;
}

inline void pd_entry_enable_global(pd_entry e)
{}

#endif