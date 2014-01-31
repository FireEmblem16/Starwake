#ifndef FLPYDSK_CPP
#define FLPYDSK_CPP

#pragma warning(disable:4244)

#include <flpydsk.h>

enum FLPYDSK_IO
{
	FLPYDSK_DOR = 0x3F2,
	FLPYDSK_MSR = 0x3F4,
	FLPYDSK_FIFO = 0x3F5,
	FLPYDSK_CTRL = 0x3F7
};

enum FLPYDSK_CMD
{
	FDC_CMD_READ_TRACK = 2,
	FDC_CMD_SPECIFY = 3,
	FDC_CMD_CHECK_STAT = 4,
	FDC_CMD_WRITE_SECT = 5,
	FDC_CMD_READ_SECT = 6,
	FDC_CMD_CALIBRATE = 7,
	FDC_CMD_CHECK_INT = 8,
	FDC_CMD_FORMAT_TRACK = 0xD,
	FDC_CMD_SEEK = 0xF
};

enum FLPYDSK_CMD_EXT
{
	FDC_CMD_EXT_SKIP = 0x20,
	FDC_CMD_EXT_DENSITY = 0x40,
	FDC_CMD_EXT_MULTITRACK = 0x80
};

enum FLPYDSK_DOR_MASK
{
	FLPYDSK_DOR_MASK_DRIVE0 = 0,
	FLPYDSK_DOR_MASK_DRIVE1 = 1,
	FLPYDSK_DOR_MASK_DRIVE2 = 2,
	FLPYDSK_DOR_MASK_DRIVE3 = 3,
	FLPYDSK_DOR_MASK_RESET = 4,
	FLPYDSK_DOR_MASK_DMA = 8,
	FLPYDSK_DOR_MASK_DRIVE0_MOTOR = 16,
	FLPYDSK_DOR_MASK_DRIVE1_MOTOR = 32,
	FLPYDSK_DOR_MASK_DRIVE2_MOTOR = 64,
	FLPYDSK_DOR_MASK_DRIVE3_MOTOR = 128
};

enum FLPYDSK_MSR_MASK
{
	FLPYDSK_MSR_MASK_DRIVE1_POS_MODE = 1,
	FLPYDSK_MSR_MASK_DRIVE2_POS_MODE = 2,
	FLPYDSK_MSR_MASK_DRIVE3_POS_MODE = 4,
	FLPYDSK_MSR_MASK_DRIVE4_POS_MODE = 8,
	FLPYDSK_MSR_MASK_BUSY = 16,
	FLPYDSK_MSR_MASK_DMA = 32,
	FLPYDSK_MSR_MASK_DATAIO = 64,
	FLPYDSK_MSR_MASK_DATAREG = 128
};

enum FLPYDSK_ST0_MASK
{
	FLPYDSK_ST0_MASK_DRIVE0 = 0,
	FLPYDSK_ST0_MASK_DRIVE1 = 1,
	FLPYDSK_ST0_MASK_DRIVE2 = 2,
	FLPYDSK_ST0_MASK_DRIVE3 = 3,
	FLPYDSK_ST0_MASK_HEADACTIVE = 4,
	FLPYDSK_ST0_MASK_NOTREADY = 8,
	FLPYDSK_ST0_MASK_UNITCHECK = 16,
	FLPYDSK_ST0_MASK_SEEKEND = 32,
	FLPYDSK_ST0_MASK_INTCODE = 64
};

enum FLPYDSK_ST0_INTCODE_TYP
{
	FLPYDSK_ST0_TYP_NORMAL = 0,
	FLPYDSK_ST0_TYP_ABNORMAL_ERR = 1,
	FLPYDSK_ST0_TYP_INVALID_ERR = 2,
	FLPYDSK_ST0_TYP_NOTREADY = 3
};

enum FLPYDSK_GAP3_LENGTH
{
	FLPYDSK_GAP3_LENGTH_STD = 42,
	FLPYDSK_GAP3_LENGTH_5_14 = 32,
	FLPYDSK_GAP3_LENGTH_3_5 = 27
};

enum FLPYDSK_SECTOR_DTL
{
	FLPYDSK_SECTOR_DTL_128 = 0,
	FLPYDSK_SECTOR_DTL_256 = 1,
	FLPYDSK_SECTOR_DTL_512 = 2,
	FLPYDSK_SECTOR_DTL_1024 = 4
};

const int FLOPPY_IRQ = 6;
const int FLPY_SECTORS_PER_TRACK = 18;

static uint8_t _CurrentDrive = 0;
static volatile uint8_t _FloppyDiskIRQ = 0;

bool _cdecl dma_initialize_floppy(uint8_t* buffer, unsigned length)
{
	union
	{
		uint8_t byte[4];
		unsigned long l;
	} a,c;

	a.l = (unsigned)buffer;
	c.l = (unsigned)(length - 1);

	if((a.l >> 24) || (c.l >> 16) || (((a.l & 0xFFFF) + c.l) >> 16))
		return false;

	i86_dma_reset(1);
	i86_dma_mask_channel(FDC_DMA_CHANNEL);
	i86_dma_reset_flipflop(1);

	i86_dma_set_address(FDC_DMA_CHANNEL,a.byte[0],a.byte[1]);
	i86_dma_reset_flipflop(1);

	i86_dma_set_count(FDC_DMA_CHANNEL,c.byte[0],c.byte[1]);
	i86_dma_set_read(FDC_DMA_CHANNEL);

	i86_dma_unmask_all(1);

	return true;
}

uint8_t flpydsk_read_status()
{
	return inportb(FLPYDSK_MSR);
}

void flpydsk_write_dor(uint8_t val)
{
	outportb(FLPYDSK_DOR,val);

	return;
}

void flpydsk_send_command(uint8_t cmd)
{
	for(int i = 0;i < 500;i++)
		if(flpydsk_read_status() & FLPYDSK_MSR_MASK_DATAREG)
			return outportb(FLPYDSK_FIFO,cmd);

	return;
}

uint8_t flpydsk_read_data()
{
	for(int i = 0;i < 500;i++)
		if(flpydsk_read_status() & FLPYDSK_MSR_MASK_DATAREG)
			return inportb(FLPYDSK_FIFO);

	return 0;
}

void flpydsk_write_ccr(uint8_t val)
{
	outportb(FLPYDSK_CTRL,val);

	return;
}

inline void flpydsk_wait_irq()
{
	while(_FloppyDiskIRQ == 0);
	_FloppyDiskIRQ = 0;

	return;
}

void _cdecl i86_flpy_irq()
{
	hardintstart();

	_FloppyDiskIRQ = 1;

	interruptdone(FLOPPY_IRQ);
	hardintret();
}

void flpydsk_check_int(uint32_t* st0, uint32_t* cyl)
{
	flpydsk_send_command(FDC_CMD_CHECK_INT);

	*st0 = flpydsk_read_data();
	*cyl = flpydsk_read_data();

	return;
}

void flpydsk_control_motor(bool b)
{
	if(_CurrentDrive > 3)
		return;

	uint32_t motor = 0;

	switch(_CurrentDrive)
	{
	case 0:
		motor = FLPYDSK_DOR_MASK_DRIVE0_MOTOR;
		break;
	case 1:
		motor = FLPYDSK_DOR_MASK_DRIVE1_MOTOR;
		break;
	case 2:
		motor = FLPYDSK_DOR_MASK_DRIVE2_MOTOR;
		break;
	case 3:
		motor = FLPYDSK_DOR_MASK_DRIVE3_MOTOR;
		break;
	}

	if(b)
		flpydsk_write_dor(_CurrentDrive | motor | FLPYDSK_DOR_MASK_RESET | FLPYDSK_DOR_MASK_DMA);
	else
		flpydsk_write_dor(FLPYDSK_DOR_MASK_RESET);

	sleep(20);
	return;
}

void flpydsk_drive_data(uint32_t stepr, uint32_t loadt, uint32_t unloadt, bool dma)
{
	uint32_t data = 0;

	flpydsk_send_command(FDC_CMD_SPECIFY);
	
	data = ((stepr & 0xF) << 4) | (unloadt & 0xF);
	flpydsk_send_command(data);

	data = (loadt << 1) | (dma == false) ? 0 : 1;
	flpydsk_send_command(data);

	return;
}

int flpydsk_calibrate(uint32_t drive)
{
	uint32_t st0, cyl;

	if(drive > 4)
		return -2;

	flpydsk_control_motor(true);

	for(int i = 0;i < 10;i++)
	{
		flpydsk_send_command(FDC_CMD_CALIBRATE);
		flpydsk_send_command(drive);
		flpydsk_wait_irq();
		flpydsk_check_int(&st0,&cyl);

		if(!cyl)
		{
			flpydsk_control_motor(false);
			return 0;
		}
	}

	flpydsk_control_motor(false);
	return -1;
}

void flpydsk_disable_controller()
{
	flpydsk_write_dor(0);

	return;
}

void flpydsk_enable_controller()
{
	flpydsk_write_dor(FLPYDSK_DOR_MASK_RESET | FLPYDSK_DOR_MASK_DMA);

	return;
}

void flpydsk_reset()
{
	uint32_t st0, cyl;

	flpydsk_disable_controller();
	flpydsk_enable_controller();
	flpydsk_wait_irq();

	for(int i = 0;i < 4;i++)
		flpydsk_check_int(&st0,&cyl);

	flpydsk_write_ccr(0);
	flpydsk_drive_data(3,16,240,true);
	flpydsk_calibrate(_CurrentDrive);

	return;
}

void flpydsk_read_sector_imp(uint8_t head, uint8_t track, uint8_t sector)
{
	uint32_t st0, cyl;

	dma_initialize_floppy((uint8_t*)DMA_BUFFER,512);
	i86_dma_set_read(FDC_DMA_CHANNEL);

	flpydsk_send_command(FDC_CMD_READ_SECT | FDC_CMD_EXT_MULTITRACK | FDC_CMD_EXT_SKIP | FDC_CMD_EXT_DENSITY);
	flpydsk_send_command(((head) << 2) | _CurrentDrive);
	flpydsk_send_command(track);
	flpydsk_send_command(head);
	flpydsk_send_command(sector);
	flpydsk_send_command(FLPYDSK_SECTOR_DTL_512);
	flpydsk_send_command(((sector + 1) >= FLPY_SECTORS_PER_TRACK) ? FLPY_SECTORS_PER_TRACK : sector + 1);
	flpydsk_send_command(FLPYDSK_GAP3_LENGTH_3_5);
	flpydsk_send_command(0xFF);

	flpydsk_wait_irq();

	for(int i = 0;i < 7;i++)
		flpydsk_read_data();

	flpydsk_check_int(&st0,&cyl);

	return;
}

int flpydsk_seek(uint32_t cyl, uint32_t head)
{
	uint32_t st0, cyl0;

	if(_CurrentDrive > 3)
		return -2;

	for(int i = 0;i < 10;i++)
	{
		flpydsk_send_command(FDC_CMD_SEEK);
		flpydsk_send_command(((head) << 2) | _CurrentDrive);
		flpydsk_send_command(cyl);

		flpydsk_wait_irq();
		flpydsk_check_int(&st0,&cyl0);

		if(cyl0 == cyl)
			return 0;
	}

	return -1;
}

void flpydsk_lba_to_chs(int lba, int* head, int* track, int* sector)
{
	*head = (lba % (FLPY_SECTORS_PER_TRACK * 2)) / (FLPY_SECTORS_PER_TRACK);
	*track = lba / (FLPY_SECTORS_PER_TRACK * 2);
	*sector = (lba % FLPY_SECTORS_PER_TRACK) + 1;

	return;
}

void flpydsk_install(int irq)
{
	setvect(irq,i86_flpy_irq);
	flpydsk_reset();
	flpydsk_drive_data(13,1,0xF,true);

	return;
}

void flpydsk_set_working_drive(uint8_t drive)
{
	if(drive < 4)
		_CurrentDrive = drive;

	return;
}

uint8_t flpydsk_get_working_drive()
{
	return _CurrentDrive;
}

uint8_t* flpydsk_read_sector(int sectorLBA)
{
	if(_CurrentDrive > 3)
		return NULL;

	int head = 0, track = 0, sector = 1;
	flpydsk_lba_to_chs(sectorLBA,&head,&track,&sector);

	flpydsk_control_motor(true);
	if(flpydsk_seek(track,head) != 0)
		return 0;

	flpydsk_read_sector_imp(head,track,sector);
	flpydsk_control_motor(false);

	return (uint8_t*)DMA_BUFFER;
}

#endif