// entry.cpp
// The entry point for the kernel.

#include <bootinfo.h>
#include <hal.h>
#include <kybrd.h>
#include <flpydsk.h>
#include <fat12.h>
#include <exceptions.h>
#include <mmngr_phys.h>
#include <mmngr_stack.h>
#include <mmngr_virtual.h>

extern int _cdecl main();
extern void _cdecl InitializeConstructors();
extern void _cdecl Exit();

void setup();

void _cdecl kernel_entry(multiboot_info* mBootInfo)
{
#ifdef _MSC_VER
	_asm mov kernelSize,edx
#endif

#ifdef ARCH_X86
	_asm
	{
		cli
		mov ax,10h
		mov ds,ax
		mov es,ax
		mov fs,ax
		mov gs,ax
	}
#endif

	bootinfo = mBootInfo;

	setup();
	InitializeConstructors();
	main();
	Exit();

	// Add code here to turn the computer off.

#ifdef ARCH_X86
	_asm cli
	_asm hlt
#endif

	for(;;);
}

void setup()
{
	uint32_t memSize = 1024 + bootinfo->m_memoryLo + bootinfo->m_memoryHi*64;

	// All data past the kernel is free to use
	pmmngr_init(memSize,0x100000 + kernelSize * 512);

	memory_region* region = (memory_region*)0x1000;

	for(int i = 0;region[i].startLo != 0 || i == 0;i++)
	{
		if(region[i].type > 4)
			region[i].type = 1;

		if(region[i].type == 1)
			pmmngr_init_region(region[i].startLo,region[i].sizeLo);
	}

	// Kernal location
	pmmngr_deinit_region(0x100000,kernelSize * 512 + pmmngr_get_block_count() / PMMNGR_BLOCKS_PER_BYTE);

	// c++ at exit runtime function pointers
	pmmngr_deinit_region(0x50000,sizeof(unsigned) * 32);
	
	// Boot data retained in bootinfo
	pmmngr_deinit_region((physical_addr)bootinfo,sizeof(multiboot_info));
	
	// Initialize DMA buffer here using deinit, not pmalloc()
	pmmngr_deinit_region((physical_addr)DMA_BUFFER,0x200);

	// Don't corrupt the stack
	mmngr_stack_init(0x90000,0x16000);

	hal_initialize();
	kybrd_init(0x21);
	install_interrupts();
	vmmngr_initialize();
	enable_interrupts();

	flpydsk_set_working_drive(0);
	flpydsk_install(0x26);

	fsysFatInitialize();

	return;
}