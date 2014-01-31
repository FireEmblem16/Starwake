#ifndef _DISPLAY_H
#define _DISPLAY_H

#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#include "../Headers/defines.h"

GLdouble* data;
GLdouble* altdata = NULL;
uint32_t num_samples;

uint32_t first_sample = 0;
GLdouble stride = 0.1;
GLdouble max_compression; /* We will need samples to be in the range -4 to 4, or rather 0 to 8 since they are unsigned */
GLdouble compression;

void display(void);
void idle(void);
void reshape(int w,int h);
void keyboard(unsigned char key,int x,int y);
void special(int key,int x,int y);
uint32_t initialize_data(uint8_t* raw_data,uint8_t bps,uint32_t size,uint8_t stereo);

void display(void)
{
	uint32_t i;
	
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();

	gluLookAt((GLdouble)first_sample + 4.0,0.0,10.0,(GLdouble)first_sample + 4.0,0.0,0.0,0.0,1.0,0.0);
	
	glBegin(GL_LINES);
	if(num_samples)
	{
		glColor4d(1.0,1.0,1.0,1.0);

		for(i = 0;i < num_samples - 1;i++)
		{
			glVertex3d(i * stride,data[i] / compression,0.0);
			glVertex3d((i + 1) * stride,data[i + 1] / compression,0.0);
		}
	}
	glEnd();

	glutSwapBuffers();
}

void idle(void)
{
	glutPostRedisplay();
	return;
}

void reshape(int w, int h)
{
	/* Prevent a divide by zero, when window is too short */
	if (h == 0)
		h = 1;

	/* Use the Projection Matrix */
	glMatrixMode(GL_PROJECTION);

	/* Reset Matrix */
	glLoadIdentity();

	/* Set the viewport to be the entire window */
	glViewport(0,0,w,h);

	/* Set the correct perspective. */
	gluPerspective(45.0,((GLdouble)w) / h,1.0,100.0);

	/* Get Back to the Modelview */
	glMatrixMode(GL_MODELVIEW);

	return;
}

void keyboard(unsigned char key, int x, int y)
{
	switch(key)
	{
	case 27:
		exit(0);
		break;
	case ' ':
		if(altdata)
		{
#ifndef WINDOWS
			/* I hate pedantic mode so much */
			GLdouble* swap_temp;
			swap_temp = data;
			data = altdata;
			altdata = swap_temp;
#else
			data = (GLdouble*)((PTR)data ^ (PTR)altdata);
			altdata = (GLdouble*)((PTR)data ^ (PTR)altdata);
			data = (GLdouble*)((PTR)data ^ (PTR)altdata);
#endif
		}

		break;
	case ',':
		stride -= 0.005;
		break;
	case '<':
		stride *= 0.5;
		break;
	case '.':
		stride += 0.005;
		break;
	case '>':
		stride *= 2.0;
		break;
	}

	if(stride < 0.001)
		stride = 0.001;

	glutPostRedisplay();
	return;
}

void special(int key, int x, int y)
{
	switch(key)
	{
	case GLUT_KEY_LEFT:
		if(first_sample > 0)
			first_sample -= 1;

		break;
	case GLUT_KEY_RIGHT:
		if(first_sample < (uint32_t)(num_samples * stride) + 1)
			first_sample += 1;		
		
		break;
	case GLUT_KEY_UP:
		compression *= 0.5;
		break;
	case GLUT_KEY_DOWN:
		if(compression <= 0.0) /* Just in case we make the compression so small as to be uninvertable */
			compression = max_compression;
		else
			compression *= 2.0;

		break;
	}

	if(compression > max_compression)
		compression = max_compression;

	glutPostRedisplay();
	return;
}

uint32_t initialize_data(uint8_t* raw_data, uint8_t bps, uint32_t size, uint8_t stereo)
{
	uint32_t i;

	data = (GLdouble*)malloc(sizeof(GLdouble) * size);

	if(data == (GLdouble*)NULL || data == (GLdouble*)BAD_PTR || data == (GLdouble*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	if(stereo)
	{
		altdata = (GLdouble*)malloc(sizeof(GLdouble) * size);

		if(altdata == (GLdouble*)NULL || altdata == (GLdouble*)BAD_PTR || altdata == (GLdouble*)UNDEF_PTR)
		{
			free((void*)data);
			return BUFFER_OVERFLOW;
		}
	}

	switch(bps)
	{
	case 8:
		max_compression = 32.0;

		for(i = 0;i < size;i++)
			if(stereo)
			{
				data[i] = ((GLdouble)raw_data[i << 1]) - 127.5;
				altdata[i] = ((GLdouble)raw_data[(i << 1) + 1]) - 127.5;
			}
			else
				data[i] = ((GLdouble)raw_data[i]) - 127.5;

		break;
	case 16:
		max_compression = 8192.0;

		for(i = 0;i < size;i++)
			if(stereo)
			{
				data[i] = ((GLdouble)((uint16_t*)raw_data)[i << 1]) - 32767.5;
				altdata[i] = ((GLdouble)((uint16_t*)raw_data)[(i << 1) + 1]) - 32767.5;
			}
			else
				data[i] = ((GLdouble)((uint16_t*)raw_data)[i]) - 32767.5;

		break;
	case 32:
		max_compression = 536870912.0;

		for(i = 0;i < size;i++)
			if(stereo)
			{
				data[i] = ((GLdouble)((uint32_t*)raw_data)[i << 1]) - 2147483647.5;
				altdata[i] = ((GLdouble)((uint32_t*)raw_data)[(i << 1) + 1]) - 2147483647.5;
			}
			else
				data[i] = ((GLdouble)((uint32_t*)raw_data)[i]) - 2147483647.5;

		break;
	}

	num_samples = size;
	compression = max_compression;
	return SUCCESS;
}

#endif