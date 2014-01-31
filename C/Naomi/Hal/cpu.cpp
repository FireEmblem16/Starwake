// cpu.cpp
// Defines the functions found in cpu.h

#ifndef CPU_CPP
#define CPU_CPP

#include <cpu.h>
#include <gdt.h>
#include <idt.h>

int i86_cpu_initialize()
{
	i86_gdt_initialize();
	i86_idt_initialize(0x8);

	return 0;
}

void i86_cpu_shutdown()
{
	return;
}

char* i86_cpu_get_vender()
{
	static char vender[13] = {0};

#ifdef _MSC_VER
	_asm
	{
		mov eax,0
		cpuid
		mov dword ptr [vender],ebx
		mov dword ptr [vender+4],edx
		mov dword ptr [vender+8],ecx
	}
#endif

	return vender;
}

void i86_cpu_flush_caches()
{
#ifdef _MSC_VER
	_asm
	{
		cli
		invd
		sti
	}
#endif

	return;
}

void i86_cpu_flush_caches_write()
{
#ifdef _MSC_VER
	_asm
	{
		cli
		wbinvd
		sti
	}
#endif

	return;
}

void i86_cpu_flush_tlb_entry(uint32_t addr)
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

#endif