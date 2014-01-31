// exceptions.cpp
// Defines the functions in exceptins.h

#ifndef EXCEPTIONS_CPP
#define EXCEPTIONS_CPP

#include <exceptions.h>
#include <hal.h>

extern void _cdecl kernel_panic(const char* fmt,...);

#pragma warning(disable:4100)
#pragma warning(disable:4702)
#pragma warning(disable:4731)

void install_interrupts()
{
	setvect(0,(void(__cdecl&)(void))divide_by_zero_fault);
	setvect(1,(void(__cdecl&)(void))single_step_trap);
	setvect(2,(void(__cdecl&)(void))nmi_trap);
	setvect(3,(void(__cdecl&)(void))breakpoint_trap);
	setvect(4,(void(__cdecl&)(void))overflow_trap);
	setvect(5,(void(__cdecl&)(void))bounds_check_fault);
	setvect(6,(void(__cdecl&)(void))invalid_opcode_fault);
	setvect(7,(void(__cdecl&)(void))no_device_fault);
	setvect(8,(void(__cdecl&)(void))double_fault_abort);
	setvect(10,(void(__cdecl&)(void))invalid_tss_fault);
	setvect(11,(void(__cdecl&)(void))no_segment_fault);
	setvect(12,(void(__cdecl&)(void))stack_fault);
	setvect(13,(void(__cdecl&)(void))general_protection_fault);
	setvect(14,(void(__cdecl&)(void))page_fault);
	setvect(16,(void(__cdecl&)(void))fpu_fault);
	setvect(17,(void(__cdecl&)(void))alignment_check_fault);
	setvect(18,(void(__cdecl&)(void))machine_check_abort);
	setvect(19,(void(__cdecl&)(void))simd_fpu_fault);

	return;
}

void _cdecl divide_by_zero_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t other)
{
#ifdef _MSC_VER
	_asm
	{
		cli
		add esp,12
		pushad
	}
#endif

	kernel_panic("Divide by 0 at physical address [0x%X:0x%X] EFLAGS [0x%X] other: 0x%X",cs,eip,eflags,other);
	for(;;);
	intret();
}

void _cdecl single_step_trap(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Single step at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl nmi_trap(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("NMI trap at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl breakpoint_trap(uint32_t cs,uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Breakpoint trap at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl overflow_trap(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Overflow trap at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl bounds_check_fault(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Bounds check fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl invalid_opcode_fault(uint32_t eflags, uint32_t cs, uint32_t eip)
{
	intstart();
	kernel_panic("Invalid opcode at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl no_device_fault(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Device not found fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl double_fault_abort(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("Double fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl invalid_tss_fault(uint32_t cs,  uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("Invalid TSS at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl no_segment_fault(uint32_t cs,  uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("Invalid segment at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl stack_fault( uint32_t cs,  uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("Stack fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl general_protection_fault(uint32_t cs,  uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("General Protection Fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void _cdecl page_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();

	int faultAddr = 0;

	_asm
	{
		mov eax,cr2
		mov [faultAddr],eax
	}
	kernel_panic("Page Fault at 0x%X:0x%X refrenced memory at 0x%X",cs,eip,faultAddr);
	
	for(;;);
	intret();
}

void interrupt fpu_fault(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("FPU Fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void interrupt alignment_check_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err)
{
	intstart();
	kernel_panic("Alignment Check at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void interrupt machine_check_abort(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("Machine Check at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

void interrupt simd_fpu_fault(uint32_t cs, uint32_t eip, uint32_t eflags)
{
	intstart();
	kernel_panic("FPU SIMD fault at physical address [0x%X:0x%X] EFLAGS [0x%X]",cs,eip,eflags);
	for(;;);
	intret();
}

#pragma warning(default:4731)
#pragma warning(default:4100)

#endif