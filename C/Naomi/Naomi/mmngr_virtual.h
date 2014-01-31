// mmngr_virtual.h
// Manages virtual memory usage.

#ifndef MMNGR_VIRTUAL_H
#define MMNGR_VIRTUAL_H

#include <stdint.h>
#include <vmmngr_pte.h>
#include <vmmngr_pde.h>

typedef uint32_t virtual_addr;

#define PAGES_PER_TABLE 1024
#define TABLES_PER_DIRECTORY 1024

struct ptable
{
	pt_entry m_entries[PAGES_PER_TABLE];
};

struct pdirectory
{
	pd_entry m_entries[TABLES_PER_DIRECTORY];
};

extern void vmmngr_initialize();
extern bool vmmngr_alloc_page(pt_entry* e);
extern void vmmngr_free_page(pt_entry* e);
extern bool vmmngr_switch_pdirectory(pdirectory* dir);
extern pdirectory* vmmngr_get_directory();
extern void vmmngr_flush_tlb_entry(virtual_addr addr);
extern void vmmngr_ptable_clear(ptable* p);
extern uint32_t vmmngr_ptable_virt_to_index(virtual_addr addr);
extern pt_entry* vmmngr_ptable_lookup_entry(ptable* p, virtual_addr addr);
extern uint32_t vmmngr_pdirectory_virt_to_index(virtual_addr addr);
extern pd_entry* vmmngr_pdirectory_lookup_entry(pdirectory* dir, virtual_addr addr);
extern void vmmngr_directory_clear(pdirectory* dir);

#endif