#ifndef BMP_FUNCTIONS_H
#define BMP_FUNCTIONS_H

#include <math.h>

#include "Defines.h"

#define PIXEL_OUT_OF_BOUNDS 0x1

typedef struct bmp_header
{
	uint8_t magic_number[2];
	uint32_t size;
	uint16_t reserved_1;
	uint16_t reserved_2;
	uint32_t data_offset;
} BMPHEADER, *PBMPHEADER;

typedef struct dib_header
{
	uint32_t header_size;
	int32_t width;
	int32_t height;
	uint16_t num_color_planes;
	uint16_t bits_per_pixel;
	uint32_t compression_method;
	uint32_t image_size; // Not file size
	int32_t h_res;
	int32_t v_res;
	uint32_t num_colors;
	uint32_t num_important_colors;
} DIBHEADER, *PDIBHEADER;

typedef struct bmp
{
	BMPHEADER header;
	DIBHEADER dib;
	uint8_t* data;
} BMP, *PBMP;

PBMP load_bmp(char* filename);
uint32_t write_bmp(char* filename, PBMP img);
uint32_t* delete_bmp(PBMP img);

// Pixels are one indexed
uint32_t get_pixel(PBMP img, uint8_t* store, int32_t x, int32_t y);
uint32_t set_pixel(PBMP img, uint8_t* data, int32_t x, int32_t y);

#endif