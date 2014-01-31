// mmngr_phys.h
// Declares many functions that work to manage physical memory.

#ifndef MMNGR_PHYS_H
#define MMNGR_PHYS_H

#include <stdint.h>
#include <bootinfo.h>

typedef uint32_t physical_addr;

#define PMMNGR_BLOCKS_PER_BYTE 8
#define PMMNGR_BLOCK_SIZE 4096
#define PMMNGR_BLOCK_ALIGN PMMNGR_BLOCK_SIZE

extern char* strMemoryTypes[];
extern uint32_t kernelSize;
extern multiboot_info* bootinfo;

extern void pmmngr_init(size_t memSize,physical_addr bitmap);
extern void pmmngr_init_region(physical_addr base, size_t size);
extern void pmmngr_deinit_region(physical_addr base, size_t size);
extern void* pmmngr_alloc_block();
extern void pmmngr_free_block(void* p);
extern void* pmmngr_alloc_blocks(size_t size);
extern void* pmmngr_alloc_blocks(size_t size, uint32_t align);
extern void pmmngr_free_blocks(void* p, size_t size);
extern size_t pmmngr_get_memory_size();
extern uint32_t pmmngr_get_used_block_count();
extern uint32_t pmmngr_get_free_block_count();
extern uint32_t pmmngr_get_block_count();
extern uint32_t pmmngr_get_block_size();
extern void pmmngr_paging_enable(bool b);
extern bool pmmngr_is_paging();
extern void pmmngr_load_PDBR(physical_addr addr);
extern physical_addr pmmngr_get_PDBR();

extern void* pmalloc(size_t size);
extern void pfree(void* p, size_t size);

#endif