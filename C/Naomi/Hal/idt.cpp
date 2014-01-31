// idt.cpp
// Defines the functions from idt.h

#ifndef IDT_CPP
#define IDT_CPP

#include <idt.h>
#include <string.h>
#include <hal.h>
#include <exceptions.h>
#ifdef _DEBUG
#include <DebugDisplay.h>
#endif

#ifdef _MSC_VER
#pragma pack(push, 1)
#endif

struct idtr
{
	uint16_t limit;
	uint32_t base;
};

#ifdef _MSC_VER
#pragma pack(pop, 1)
#endif

static struct idt_descriptor _idt[I86_MAX_INTERRUPTS];
static struct idtr _idtr;

static void idt_install();
static void i86_default_handler();

static void idt_install()
{
#ifdef _MSC_VER
	_asm lidt [_idtr]
#endif

	return;
}

static void _cdecl i86_default_handler()
{
	intstart();

#ifdef _DEBUG
	DebugClrScr(33);
	DebugGotoXY(0,0);
	DebugSetColor(33);
	DebugPuts("*** [i86 HAL] i86_default_handler: Unhandled Exception");
#endif

	for(;;);
}

idt_descriptor* i86_get_ir(uint32_t i)
{
	if(i>I86_MAX_INTERRUPTS)
		return 0;

	return &_idt[i];
}

int i86_install_ir(uint32_t i, uint16_t flags, uint16_t sel, I86_IRQ_HANDLER irq)
{
	if(i>I86_MAX_INTERRUPTS)
		return 0;

	if(!irq)
		return 0;

	uint64_t uiBase = (uint64_t)&(*irq);

	_idt[i].baseLo = uint16_t(uiBase & 0xffff);
	_idt[i].baseHi = uint16_t((uiBase >> 16) & 0xffff);
	_idt[i].reserved = 0;
	_idt[i].flags = uint8_t(flags);
	_idt[i].sel = sel;

	return	0;
}

int i86_idt_initialize(uint16_t codeSel)
{
	_idtr.limit = sizeof(struct idt_descriptor) * I86_MAX_INTERRUPTS - 1;
	_idtr.base = (uint32_t)&_idt[0];

	memset((void*)&_idt[0],0,sizeof(idt_descriptor) * I86_MAX_INTERRUPTS - 1);

	for(int i=0;i < I86_MAX_INTERRUPTS;i++)
		i86_install_ir(i,I86_IDT_DESC_PRESENT | I86_IDT_DESC_BIT32,codeSel,(I86_IRQ_HANDLER)i86_default_handler);

	idt_install();

	return 0;
}

#endif