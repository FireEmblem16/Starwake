// HAL.cpp
// Impliments the function defined in hal.h

#ifndef HAL_CPP
#define HAL_CPP

#ifndef ARCH_X86
#error "[HAL.cpp for i86] requires i86 architecture. Define ARCH_X86"
#endif

#include <hal.h>
#include <cpu.h>
#include <idt.h>
#include <pic.h>
#include <pit.h>

int _cdecl hal_initialize()
{
	i86_cpu_initialize();
	i86_pic_initialize(0x20,0x28);
	i86_pit_initialize(0x20);
	i86_pit_start_counter(2000,I86_PIT_OCW_COUNTER_0,I86_PIT_OCW_MODE_SQUAREWAVEGEN);

	return 0;
}

int _cdecl hal_shutdown()
{
	i86_cpu_shutdown();
	return 0;
}

void _cdecl geninterrupt(int n)
{
#ifdef _MSC_VER
	_asm
	{
		mov al,byte ptr[n]
		mov byte ptr[genint+1],al
		mov al,byte ptr[n]
		jmp genint
	genint:
		int 0
	}
#endif
}

void _cdecl enable_interrupts()
{
#ifdef _MSC_VER
	_asm sti
#endif
}

void _cdecl disable_interrupts()
{
#ifdef _MSC_VER
	_asm cli
#endif
}

unsigned char inportb(unsigned short portid)
{
#ifdef _MSC_VER
	_asm
	{
		mov dx,word ptr [portid]
		in al,dx
		mov byte ptr [portid],al
	}
#endif
	return (unsigned char)portid;
}

void outportb(unsigned short portid, unsigned char value)
{
#ifdef _MSC_VER
	_asm
	{
		mov al,byte ptr [value]
		mov dx,word ptr [portid]
		out dx,al
	}
#endif

	return;
}

inline void _cdecl interruptdone(unsigned int intno)
{
	if(intno > 16)
		return;

	if(intno > 7)
		i86_pic_send_command(I86_PIC_OCW2_MASK_EOI,1);

	i86_pic_send_command(I86_PIC_OCW2_MASK_EOI,0);
}

void _cdecl setvect(int intno, void (_cdecl far& vect)())
{
	i86_install_ir(intno,I86_IDT_DESC_PRESENT | I86_IDT_DESC_BIT32,0x8,vect);
}

void (_cdecl far* _cdecl getvect(int intno))()
{
	idt_descriptor* desc = i86_get_ir(intno);

	if(!desc)
		return 0;

	uint32_t addr = desc->baseLo | (desc->baseHi << 16);
	I86_IRQ_HANDLER irq = (I86_IRQ_HANDLER)addr;

	return irq;
}

void _cdecl sound(unsigned int frequency)
{
	outportb(0x61,3 | unsigned char(frequency << 2));
}

const char* get_cpu_vender()
{
	return i86_cpu_get_vender();
}

int get_tick_count()
{
	return i86_pit_get_tick_count();
}

#endif