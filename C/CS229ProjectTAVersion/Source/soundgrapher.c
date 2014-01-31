#ifndef _SOUND_GRAPHER_C
#define _SOUND_GRAPHER_C

#pragma warning(disable:4996)

#include <stdlib.h>
#include <stdio.h>
#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#include "../Headers/display.h"
#include "../Headers/defines.h"
#include "../Headers/suio.h"
#include "../Headers/wavio.h"
#include "../Headers/util.h"

#ifdef WINDOWS
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char** argv)
{
	PFILE f;
	SU_FILE su;
	WAV_FILE wav;
	uint32_t err;
	uint32_t type;
	uint8_t* raw_data;
	uint8_t bps; /* bits per sample */
	uint32_t size;
	uint8_t stereo;

	if(argc == 2)
#ifdef WINDOWS
	{
		_setmode(_fileno(stdin),_O_BINARY);
		f = stdin;
	}
#else
		f = freopen(NULL,"rb",stdin);
#endif
	else if(argc == 3)
	{
		f = fopen((c_string)argv[2],"rb");

		if(f == (PFILE)NULL || f == (PFILE)BAD_PTR || f == (PFILE)UNDEF_PTR)
		{
			write_error(FILE_NOT_FOUND,stderr);
			return EXIT_FAILURE;
		}
	}
	else
	{
		fputs("This program only supports the use of two command line arguments.\n",stderr);
		fputs("The first parameter should be the type of file being displayed: WAV or SU (case insensitive)\n",stderr);
		fputs("The second parameter is optional but if provided should be the path to the file.\n",stderr);
		fputs("This program will read from standard in by default.\n",stderr);
		return EXIT_FAILURE;
	}

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowPosition(0,0);
	glutInitWindowSize(500,500);

	glutCreateWindow("Sound Grapher");

	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(special);
	
	for(err = 0;argv[err];err++)
		argv[1][err] &= ~0x20;

	if(!strcmp(argv[1],"WAV\0"))
	{
		type = 0;
		err = read_wav_file(&wav,f);
		raw_data = wav.data.data;
		bps = wav.fmt.BitsPerSample;
		stereo = wav.fmt.NumChannels - 1;
		size = wav.data.SubChunk2Size / wav.fmt.NumChannels;
	}
	else if(!strcmp(argv[1],"SU\0"))
	{
		type = 1;
		err = read_su_file(&su,f);
		raw_data = su.samples;
		bps = su.header.flags & SU_8_BIT ? 8 : (su.header.flags & SU_16_BIT ? 16 : 32);
		stereo = su.header.flags & SU_STEREO;
		size = su.header.num_samples;
		flip_endian(&su);
	}
	else
	{
		fputs("An unsupported sound format was specificed.\n",stderr);
		return EXIT_FAILURE;
	}

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	err = initialize_data(raw_data,bps,size,stereo);

	if(err)
	{
		write_error(err,stderr);
		return EXIT_FAILURE;
	}

	free((void*)raw_data);

	if(argc == 3)
		if(fclose(f))
		{
			write_error(FILE_LEFT_OPEN,stderr);
			return EXIT_FAILURE;
		}

	glutMainLoop();
	return EXIT_SUCCESS;
}

#endif