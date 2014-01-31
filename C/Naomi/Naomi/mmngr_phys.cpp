// mmngr_phys.cpp
// Defines the functions declared in mmngr_phys.h

#ifndef MMNGR_PHYS_CPP
#define MMNGR_PHYS_CPP

#include <string.h>
#include <mmngr_phys.h>

#pragma warning(disable:4800)

char* strMemoryTypes[] =
{
	{"Available"},
	{"Reserved"},
	{"ACPI Reclaim"},
	{"ACPI NVS Memory"}
};

uint32_t kernelSize = 0;
multiboot_info* bootinfo = 0;

static uint32_t _mmngr_memory_size = 0;
static uint32_t _mmngr_used_blocks = 0;
static uint32_t _mmngr_max_blocks = 0;
static uint32_t* _mmngr_memory_map = 0;

inline void mmap_set(int bit);
inline void mmap_unset(int bit);
inline bool mmap_test(int bit);
int mmap_first_pfree();
int mmap_first_free_s(size_t size);

inline void mmap_set(int bit)
{
	_mmngr_memory_map[bit / 32] |= (1 << (bit % 32));
}

inline void mmap_unset(int bit)
{
	_mmngr_memory_map[bit / 32] &= ~(1 << (bit % 32));
}

inline bool mmap_test(int bit)
{
	return _mmngr_memory_map[bit / 32] & (1 << (bit % 32));
}

int mmap_first_pfree()
{
	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)
		if(_mmngr_memory_map[i] != 0xFFFFFFFF)
			for(int j = 0;j < 32;j++)
			{
				int bit = 1 << j;

				if(!(_mmngr_memory_map[i] & bit))
					return i * 32 + j;
			}

	return -1;
}

int mmap_first_free_s(size_t size)
{
	if(size == 0)
		return -1;

	if(size == 1)
		return mmap_first_pfree();

	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)
		if(_mmngr_memory_map[i] != 0xFFFFFFFF)
			for(int j = 0;j < 32;j++)
			{
				int bit = 1 << j;

				if(!(_mmngr_memory_map[i] & bit))
				{
					int startingBit = i * 32 + bit;
					uint32_t free = 0;

					for(uint32_t count = 0;count <= size;count++)
					{
						if(!mmap_test(startingBit + count))
							free++;

						if(free == size)
							return i * 32 + j;
					}
				}
			}

	return -1;
}

int mmap_first_free_s(size_t size, uint32_t align)
{
	if(size == 0)
		return -1;

	if(size == 1)
		return mmap_first_pfree();

	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)
		if(_mmngr_memory_map[i] != 0xFFFFFFFF)
			for(int j = 0;j < 32;j++)
			{
				int bit = 1 << j;

				if(!(_mmngr_memory_map[i] & bit))
				{
					if((i*32 + j) * PMMNGR_BLOCK_SIZE % align == 0)
					{
						int startingBit = i * 32 + bit;
						uint32_t free = 0;

						for(uint32_t count = 0;count <= size;count++)
						{
							if(!mmap_test(startingBit + count))
								free++;

							if(free == size)
								return i * 32 + j;
						}
					}
				}
			}

	return -1;
}

void pmmngr_init(size_t memSize, physical_addr bitmap)
{
	_mmngr_memory_size = memSize;
	_mmngr_memory_map = (uint32_t*)bitmap;
	_mmngr_max_blocks = (pmmngr_get_memory_size() * 1024) / PMMNGR_BLOCK_SIZE;
	_mmngr_used_blocks = _mmngr_max_blocks;

	memset(_mmngr_memory_map,0xF,pmmngr_get_block_count() / PMMNGR_BLOCKS_PER_BYTE);

	return;
}

void pmmngr_init_region(physical_addr base, size_t size)
{
	int align = base / PMMNGR_BLOCK_SIZE;
	int blocks = size / PMMNGR_BLOCK_SIZE;

	for(;blocks >= 0;blocks--)
	{
		mmap_unset(align++);
		_mmngr_used_blocks--;
	}

	if(!mmap_test(0))
	{
		mmap_set(0);
		_mmngr_used_blocks++;
	}

	return;
}

void pmmngr_deinit_region(physical_addr base, size_t size)
{
	int align = base / PMMNGR_BLOCK_SIZE;
	int blocks = size / PMMNGR_BLOCK_SIZE;

	if(size % PMMNGR_BLOCK_SIZE != 0)
		blocks++;

	for(;blocks >= 0;blocks--)
	{
		mmap_set(align++);
		_mmngr_used_blocks++;
	}

	return;
}

void* pmmngr_alloc_block()
{
	if(pmmngr_get_free_block_count() <= 0)
		return 0;

	int frame = mmap_first_pfree();

	if(frame == -1)
		return 0;

	mmap_set(frame);

	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
	_mmngr_used_blocks++;

	return (void*)addr;
}

void pmmngr_free_block(void* p)
{
	physical_addr addr = (physical_addr)p;
	int frame = addr / PMMNGR_BLOCK_SIZE;

	mmap_unset(frame);
	_mmngr_used_blocks--;

	return;
}

void* pmmngr_alloc_blocks(size_t size)
{
	if(pmmngr_get_free_block_count() <= size)
		return 0;

	int frame = mmap_first_free_s(size);

	if(frame == -1)
		return 0;

	for(uint32_t i = 0;i < size;i++)
		mmap_set(frame + i);

	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
	_mmngr_used_blocks += size;

	return (void*)addr;
}

void* pmmngr_alloc_blocks(size_t size, uint32_t align)
{
	if(pmmngr_get_free_block_count() <= size)
		return 0;

	int frame = mmap_first_free_s(size,align);

	if(frame == -1)
		return 0;

	for(uint32_t i = 0;i < size;i++)
		mmap_set(frame + i);

	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
	_mmngr_used_blocks += size;

	return (void*)addr;
}

void pmmngr_free_blocks(void* p, size_t size)
{
	physical_addr addr = (physical_addr)p;
	int frame = addr / PMMNGR_BLOCK_SIZE;

	for(uint32_t i = 0;i < size;i++)
		mmap_unset(frame + i);

	_mmngr_used_blocks -= size;

	return;
}

size_t pmmngr_get_memory_size()
{
	return _mmngr_memory_size;
}

uint32_t pmmngr_get_block_count()
{
	return _mmngr_max_blocks;
}

uint32_t pmmngr_get_used_block_count()
{
	return _mmngr_used_blocks;
}

uint32_t pmmngr_get_free_block_count()
{
	return _mmngr_max_blocks - _mmngr_used_blocks;
}

uint32_t pmmngr_get_block_size()
{
	return PMMNGR_BLOCK_SIZE;
}

void pmmngr_paging_enable(bool b)
{
#ifdef _MSC_VER
	_asm
	{
		mov eax,cr0
		cmp [b],1
		je enable
		jmp disable
	enable:
		or eax,0x80000000
		mov cr0,eax
		jmp done
	disable:
		and eax,0x7FFFFFFF
		mov cr0,eax
	done:
	}
#endif

	return;
}

bool pmmngr_is_paging()
{
	uint32_t res = 0;

#ifdef _MSC_VER
	_asm
	{
		mov eax,cr0
		mov [res],eax
	}
#endif

	return (res  & 0x80000000) ? false : true;
}

void pmmngr_load_PDBR(physical_addr addr)
{
#ifdef _MSC_VER
	_asm
	{
		mov eax,[addr]
		mov cr3,eax
	}
#endif

	return;
}

physical_addr pmmngr_get_PDBR()
{
#ifdef _MSC_VER
	_asm
	{
		mov eax,cr3
		ret
	}
#endif
}

void* pmalloc(size_t size)
{
	size_t blocks = size / PMMNGR_BLOCK_SIZE;
	if(size % PMMNGR_BLOCK_SIZE != 0)
		blocks++;

	return pmmngr_alloc_blocks(blocks);
}

void pfree(void* p, size_t size)
{
	size_t blocks = size / PMMNGR_BLOCK_SIZE;
	if(size % PMMNGR_BLOCK_SIZE != 0)
		blocks++;

	pmmngr_free_blocks(p,blocks);

	return;
}

#endif