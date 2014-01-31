// hal.h
// The Hardware Abstraction Layer. Provides interfaces to the hardware through software.

#ifndef HAL_H
#define HAL_H

#ifndef ARCH_X86
#pragma error "HAL not implimented for this platform"
#endif

#include <stdint.h>

#ifdef _MSC_VER
#define interrupt __declspec(naked)
#else
#define interrupt
#endif

#define far
#define near

extern int _cdecl hal_initialize();
extern int _cdecl hal_shutdown();

extern void _cdecl geninterrupt(int n);
extern void _cdecl enable_interrupts();
extern void _cdecl disable_interrupts();
extern void _cdecl interruptdone(unsigned int intno);
extern void _cdecl setvect(int intno, void (_cdecl far &vect)());
extern void (_cdecl far* _cdecl getvect(int intno))();

extern unsigned char _cdecl inportb(unsigned short portid);
extern void _cdecl outportb(unsigned short portid, unsigned char value);

extern void _cdecl sound(unsigned frequency);

extern const char* _cdecl get_cpu_vender();
extern int _cdecl get_tick_count();

extern void i86_dma_set_mode(uint8_t channel, uint8_t mode);
extern void i86_dma_set_read(uint8_t channel);
extern void i86_dma_set_write(uint8_t channel);

extern void i86_dma_set_address(uint8_t channel, uint8_t low, uint8_t high);
extern void i86_dma_set_count(uint8_t channel, uint8_t low, uint8_t high);

extern void i86_dma_mask_channel(uint8_t channel);
extern void i86_dma_unmask_channel(uint8_t channel);
extern void i86_dma_unmask_all(int dma);

extern void i86_dma_reset_flipflop(int dma);
extern void i86_dma_enable(uint8_t ctrl, bool e);
extern void i86_dma_reset(int dma);
extern void i86_dma_set_external_page_register(uint8_t reg, uint8_t val);

#endif