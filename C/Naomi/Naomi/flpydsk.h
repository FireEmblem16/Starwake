#ifndef FLPYDSK_H
#define FLPYDSK_H

#include <_null.h>
#include <stdint.h>
#include <exceptions.h>
#include <hal.h>
#include <naomi.h>

const int DMA_BUFFER = 0x1000;
const int FDC_DMA_CHANNEL = 2;

extern void flpydsk_install(int irq);
extern void flpydsk_set_working_drive(uint8_t drive);
extern void flpydsk_lba_to_chs(int lba, int* head, int* track, int* sector);

extern uint8_t flpydsk_get_working_drive();
extern uint8_t* flpydsk_read_sector(int sectorLBA);

#endif