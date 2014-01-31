#ifndef BMP_FUNCTIONS_CPP
#define BMP_FUNCTIONS_CPP

#include "bmp_functions.h"

PBMP load_bmp(char* filename)
{
	PFILE file = fopen(filename,"r");

	if(!file)
		return NULL;

	PBMP ret = new BMP();
	uint8_t buf[4];

	// Load magic number
	fscanf(file,"%c%c",&ret->header.magic_number[0],&ret->header.magic_number[1]);
	
	// Load size of image
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->header.size = *((uint32_t*)buf);

	// Load reserved values
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->header.reserved_1 = *((uint16_t*)buf);
	ret->header.reserved_2 = *((uint16_t*)(buf + 2));

	// Load data offset
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->header.data_offset = *((uint32_t*)buf);

	// Load header size
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.header_size = *((uint32_t*)buf);

	// Load image width
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.width = *((int32_t*)buf);

	// Load image height
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.height = *((int32_t*)buf);

	// Load image color pane and bpp data
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.num_color_planes = *((uint16_t*)buf);
	ret->dib.bits_per_pixel = *((uint16_t*)(buf + 2));

	// Load compression method
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.compression_method = *((uint32_t*)buf);

	// Load image size
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.image_size = *((uint32_t*)buf);

	// Load the horizontal resolution
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.h_res = *((int32_t*)buf);

	// Load the vertical resolution
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.v_res = *((int32_t*)buf);

	// Load the number of colors
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.num_colors = *((uint32_t*)buf);

	// Load the number of important colors
	fscanf(file,"%c%c%c%c",buf,buf + 1,buf + 2,buf + 3);
	ret->dib.num_important_colors = *((uint32_t*)buf);

	ret->data = new uint8_t[ret->dib.image_size];

	// Load all data
	for(uint32_t i = 0;i < ret->dib.image_size;i++)
		fscanf(file,"%c",ret->data + i);

	// Reverse the data so it will be useful
	for(uint32_t i = 0;i < ret->dib.image_size / 2;i++)
	{
		*(ret->data + i) ^= *(ret->dib.image_size + ret->data - 1 - i);
		*(ret->dib.image_size + ret->data - 1 - i) ^= *(ret->data + i);
		*(ret->data + i) ^= *(ret->dib.image_size + ret->data - 1 - i);
	}

	if(file)
		fclose(file);

	return ret;
}

uint32_t write_bmp(char* filename, PBMP img)
{
	PFILE file = fopen(filename,"w");

	if(!file)
		return FILE_NOT_FOUND;

	uint8_t buf[4];

	// Store magic number
	fprintf(file,"%c%c",img->header.magic_number[0],img->header.magic_number[1]);
	
	// Store size of image
	*((uint32_t*)buf) = img->header.size;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));
	
	// Store reserved values
	*((uint16_t*)buf) = img->header.reserved_1;
	*((uint16_t*)(buf + 2)) = img->header.reserved_2;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store data offset
	*((uint32_t*)buf) = img->header.data_offset;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store header size
	*((uint32_t*)buf) = img->dib.header_size;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store image width
	*((int32_t*)buf) = img->dib.width;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store image height
	*((int32_t*)buf) = img->dib.height;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store image color pane and bpp data
	*((uint16_t*)buf) = img->dib.num_color_planes;
	*((uint16_t*)(buf + 2)) = img->dib.bits_per_pixel;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store compression method
	*((uint32_t*)buf) = img->dib.compression_method;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store image size
	*((uint32_t*)buf) = img->dib.image_size;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store the horizontal resolution
	*((int32_t*)buf) = img->dib.h_res;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store the vertical resolution
	*((int32_t*)buf) = img->dib.v_res;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store the number of colors
	*((uint32_t*)buf) = img->dib.num_colors;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Store the number of important colors
	*((uint32_t*)buf) = img->dib.num_important_colors;
	fprintf(file,"%c%c%c%c",*buf,*(buf + 1),*(buf + 2),*(buf + 3));

	// Reverse the data so it will be stored properly
	for(uint32_t i = 0;i < img->dib.image_size / 2;i++)
	{
		*(img->data + i) ^= *(img->dib.image_size + img->data - 1 - i);
		*(img->dib.image_size + img->data - 1 - i) ^= *(img->data + i);
		*(img->data + i) ^= *(img->dib.image_size + img->data - 1 - i);
	}

	// Store all data
	for(uint32_t i = 0;i < img->dib.image_size;i++)
		fputc(*(img->data + i),file);

	if(file)
		fclose(file);

	return SUCCESS;
}

uint32_t* delete_bmp(PBMP img)
{
	delete[] img->data;
	delete img;

	return SUCCESS;
}

uint32_t get_pixel(PBMP img, uint8_t* store, int32_t x, int32_t y)
{
	if(x > img->dib.height || y > img->dib.width || x < 1 || y < 1)
		return PIXEL_OUT_OF_BOUNDS;

	uint32_t row_size = (uint32_t)ceil(img->dib.bits_per_pixel * img->dib.width / 32.0) << 2;
	uint32_t depth = img->dib.height - y;
	uint32_t offset = depth * row_size + ((img->dib.bits_per_pixel * x) >> 3);

	store[0] = img->data[offset];
	store[1] = img->data[offset + 1];
	store[2] = img->data[offset + 2];

	return SUCCESS;
}

uint32_t set_pixel(PBMP img, uint8_t* data, int32_t x, int32_t y)
{
	if(x > img->dib.height || y > img->dib.width || x < 1 || y < 1)
		return PIXEL_OUT_OF_BOUNDS;

	uint32_t row_size = (uint32_t)ceil(img->dib.bits_per_pixel * img->dib.width / 32.0) << 2;
	uint32_t depth = img->dib.height - y;
	uint32_t offset = depth * row_size + ((img->dib.bits_per_pixel * x) >> 3);

	img->data[offset] = data[0];
	img->data[offset + 1] = data[1];
	img->data[offset + 2] = data[2];

	return SUCCESS;
}

#endif