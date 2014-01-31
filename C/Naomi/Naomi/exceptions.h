// exceptions.h
// Handles interrupts.

#ifndef EXCEPTIONS_H
#define EXCEPTIONS_H

#include <stdint.h>

#ifdef _MSC_VER
#define intstart() \
	_asm cli \
	_asm sub ebp,4
#define intret() \
	_asm add ebp,4 \
	_asm sti \
	_asm iretd
#define hardintstart() \
	_asm cli \
	_asm add esp,12 \
	_asm pushad 
#define hardintret() \
	_asm popad \
	_asm sti \
	_asm iretd
#else
#define intstart()
#define intret()
#define hardintstart()
#define hardintret()
#endif

extern void install_interrupts();
extern void _cdecl divide_by_zero_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t other);
extern void _cdecl single_step_trap(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl nmi_trap(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl breakpoint_trap(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl overflow_trap(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl bounds_check_fault(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl invalid_opcode_fault(uint32_t eflags, uint32_t cs, uint32_t eip);
extern void _cdecl no_device_fault(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl double_fault_abort(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl invalid_tss_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl no_segment_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl stack_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl general_protection_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl page_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl fpu_fault(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl alignment_check_fault(uint32_t cs, uint32_t eip, uint32_t eflags, uint32_t err);
extern void _cdecl machine_check_abort(uint32_t cs, uint32_t eip, uint32_t eflags);
extern void _cdecl simd_fpu_fault(uint32_t cs, uint32_t eip, uint32_t eflags);

#endif