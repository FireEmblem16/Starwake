// pic.h
// Provides a driver for the Programmable Interrupt Controller.

#ifndef PIC_H
#define PIC_H

#ifndef ARCH_X86
#error "[pic.h] platform not implimented. Define ARCH_X86 for HAL."
#endif

#include <stdint.h>

// On PIC 1
#define I86_PIC_IRQ_TIMER 0
#define I86_PIC_IRQ_KEYBOARD 1
// Channel 2 is connected to PIC 2
#define I86_PIC_IRQ_SERIAL2 3
#define I86_PIC_IRQ_SERIAL1 4
#define I86_PIC_IRQ_PARALLEL2 5
#define I86_PIC_IRQ_DISKETTE 6
#define I86_PIC_IRQ_PARALLEL1 7

// On PIC 2
#define I86_PIC_IRQ_CMOSTIMER 0
#define I86_PIC_IRQ_CGARETRACE 1
#define I86_PIC_IRQ_AUXILIARY 4
#define I86_PIC_IRQ_FPU 5
#define I86_PIC_IRQ_HDC 6

// First Command Words
#define I86_PIC_OCW1_MASK_1 1
#define I86_PIC_OCW1_MASK_2 2
#define I86_PIC_OCW1_MASK_3 4
#define I86_PIC_OCW1_MASK_4 8
#define I86_PIC_OCW1_MASK_5 16
#define I86_PIC_OCW1_MASK_6 32
#define I86_PIC_OCW1_MASK_7 64
#define I86_PIC_OCW1_MASK_8 128

// Second Command Words
#define I86_PIC_OCW2_MASK_L1 1
#define I86_PIC_OCW2_MASK_L2 2
#define I86_PIC_OCW2_MASK_L3 4
#define I86_PIC_OCW2_MASK_EOI 0x20
#define I86_PIC_OCW2_MASK_SL 0x40
#define I86_PIC_OCW2_MASK_ROTATE 0x80

// Third Command Words
#define I86_PIC_OCW3_MASK_RIS 1
#define I86_PIC_OCW3_MASK_RIR 2
#define I86_PIC_OCW3_MASK_MODE 4
#define I86_PIC_OCW3_MASK_SMM 0x20
#define I86_PIC_OCW3_MASK_ESMM 0x40
#define I86_PIC_OCW3_MASK_D7 0x80

extern uint8_t i86_pic_read_data(uint8_t picNum);
extern void i86_pic_send_data(uint8_t data, uint8_t picNum);
extern void i86_pic_send_command(uint8_t cmd, uint8_t picNum);
extern bool i86_pic_mask_irq(uint8_t irqmask, uint8_t picNum);
extern bool i86_pic_unmask_irq(uint8_t irqmask, uint8_t picNum);
extern void i86_pic_initialize(uint8_t base0, uint8_t base1);

#endif