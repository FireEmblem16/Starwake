// mmngr_virtual.cpp
// Does the actual managing of virtual memory usage.

#ifndef MMNGR_VIRTUAL_CPP
#define MMNGR_VIRTUAL_CPP

#include <string.h>
#include <mmngr_virtual.h>
#include <mmngr_phys.h>

#pragma warning(disable:4740)

#define PTABLE_ADDR_SPACE_SIZE 0x400000
#define DTABLE_ADDR_SPACE_SIZE 0x100000000
#define PAGE_SIZE 4096

pdirectory* _cur_directory = 0;
physical_addr _cur_pdbr = 0;

inline uint32_t vmmngr_ptable_virt_to_index(virtual_addr addr)
{
	return (addr >= PTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;
}

inline pt_entry* vmmngr_ptable_lookup_entry(ptable* p, virtual_addr addr)
{
	if(p)
		return &p->m_entries[vmmngr_ptable_virt_to_index(addr)];

	return 0;
}

inline void vmmngr_ptable_clear(ptable* p)
{
	if(p)
		memset(p,0,sizeof(ptable));
}

inline void vmmngr_pdirectory_clear(pdirectory* dir)
{
	if(dir)
		memset(dir,0,sizeof(pdirectory));
}

inline uint32_t vmmngr_pdirectory_virt_to_index(virtual_addr addr)
{
	return (addr >= DTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;
}

inline pd_entry* vmmngr_pdirectory_lookup_entry(pdirectory* dir, virtual_addr addr)
{
	if(dir)
		return &dir->m_entries[vmmngr_pdirectory_virt_to_index(addr)];

	return 0;
}

inline bool vmmngr_switch_pdirectory(pdirectory* dir)
{
	if(!dir)
		return false;

	_cur_directory = dir;
	pmmngr_load_PDBR(_cur_pdbr);
	return true;
}

void vmmngr_flush_tlb_entry(virtual_addr addr)
{
#ifdef _MSC_VER
	_asm
	{
		cli
		invlpg addr
		sti
	}
#endif

	return;
}

pdirectory* vmmngr_get_directory()
{
	return _cur_directory;
}

bool vmmngr_alloc_page(pt_entry* e)
{
	void* p = pmalloc(PAGE_SIZE);
	if(p)
		return false;

	pt_entry_set_frame(e,(physical_addr)p);
	pt_entry_add_attribute(e,I86_PTE_PRESENT);

	return true;
}

void vmmngr_free_page(pt_entry* e)
{
	void* p = (void*)pt_entry_pfn(*e);
	if(p)
		pfree(p,PAGE_SIZE);

	pt_entry_del_attribute(e,I86_PTE_PRESENT);

	return;
}

void vmmngr_initialize()
{
	pdirectory* dir = (pdirectory*)pmalloc(sizeof(pdirectory));
	
	if(!dir)
		return;

	vmmngr_pdirectory_clear(dir);

	// Identity Map all memory
	// for(uint32_t j = 0;j < TABLES_PER_DIRECTORY;j++)

	// Identity Map only the first 4MB of memory.
	for(uint32_t j = 0;j < 1;j++)
	{
		ptable* table = (ptable*)pmalloc(sizeof(ptable));

		if(!table)
			return;

		vmmngr_ptable_clear(table);

		for(int i = 0, frame = j * PTABLE_ADDR_SPACE_SIZE ;i < PAGES_PER_TABLE;i++,frame += PAGE_SIZE)
		{
			pt_entry page = 0;
			pt_entry_add_attribute(&page,I86_PTE_PRESENT);
			pt_entry_set_frame(&page,frame);

			table->m_entries[vmmngr_ptable_virt_to_index(frame)] = page;
		}

		pd_entry* entry = vmmngr_pdirectory_lookup_entry(dir,j * PTABLE_ADDR_SPACE_SIZE);
		pd_entry_add_attribute(entry,I86_PDE_PRESENT);
		pd_entry_add_attribute(entry,I86_PDE_WRITABLE);
		pd_entry_set_frame(entry,(physical_addr)table);
	}

	_cur_pdbr = (physical_addr)&dir->m_entries;

	if(!vmmngr_switch_pdirectory(dir))
		return;

	pmmngr_paging_enable(true);

	return;
}

#endif