// pit.cpp
// Defines the functions found in pit.h

#ifndef PIT_CPP
#define PIT_CPP

#include <idt.h>
#include <pit.h>
#include <pic.h>
#include <hal.h>
#include <exceptions.h>

#define I86_PIT_REG_COUNTER0 0x40
#define I86_PIT_REG_COUNTER1 0x41
#define I86_PIT_REG_COUNTER2 0x42
#define I86_PIT_REG_COMMAND 0x43

static volatile uint32_t _pit_ticks = 0;
static bool _pit_bIsInit = false;

void _cdecl i86_pit_irq();

void _cdecl i86_pit_irq()
{
	hardintstart();

	_pit_ticks++;

	interruptdone(0);
	hardintret();
}

uint32_t i86_pit_set_tick_count(uint32_t i)
{
	uint32_t ret = _pit_ticks;
	_pit_ticks = i;
	return ret;
}

uint32_t i86_pit_get_tick_count()
{
	return _pit_ticks;
}

void i86_pit_send_command(uint8_t cmd)
{
	outportb(I86_PIT_REG_COMMAND,cmd);

	return;
}

void i86_pit_send_data(uint16_t data, uint8_t counter)
{
	uint8_t port = (counter == I86_PIT_OCW_COUNTER_0) ? I86_PIT_REG_COUNTER0 : 
		((counter == I86_PIT_OCW_COUNTER_1) ? I86_PIT_REG_COUNTER1 : I86_PIT_REG_COUNTER2);

	outportb(port,(uint8_t)data);

	return;
}

uint8_t i86_pit_read_data(uint16_t counter)
{
	uint8_t	port = (counter == I86_PIT_OCW_COUNTER_0) ? I86_PIT_REG_COUNTER0 :
		((counter == I86_PIT_OCW_COUNTER_1) ? I86_PIT_REG_COUNTER1 : I86_PIT_REG_COUNTER2);

	return inportb(port);
}

void i86_pit_start_counter(uint32_t freq, uint8_t counter, uint8_t mode)
{
	if(freq == 0)
		return;

	uint16_t divisor = uint16_t(1193181 / (uint16_t)freq);

	uint8_t ocw = 0;
	ocw = (ocw & ~I86_PIT_OCW_MASK_MODE) | mode;
	ocw = (ocw & ~I86_PIT_OCW_MASK_RL) | I86_PIT_OCW_RL_DATA;
	ocw = (ocw & ~I86_PIT_OCW_MASK_COUNTER) | counter;
	i86_pit_send_command(ocw);

	i86_pit_send_data(divisor & 0xFF,0);
	i86_pit_send_data((divisor >> 8) & 0xFF,0);

	_pit_ticks = 0;

	return;
}

void _cdecl i86_pit_initialize(int irv)
{
	setvect(irv,i86_pit_irq);
	_pit_bIsInit = true;
}

bool _cdecl i86_pit_is_initialized()
{
	return _pit_bIsInit;
}

#endif