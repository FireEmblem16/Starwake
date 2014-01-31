// cpu.h
// Provides the interface to the computer's processors and registers like gdt and idt.

#ifndef CPU_H
#define CPU_H

#include <stdint.h>
#include <regs.h>

extern int i86_cpu_initialize();
extern void i86_cpu_shutdown();
extern char* i86_cpu_get_vender();
extern void i86_cpu_flush_caches();
extern void i86_cpu_flush_caches_write();
extern void i86_cpu_flush_tlb_entry(uint32_t addr);

#endif