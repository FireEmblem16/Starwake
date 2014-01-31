#ifndef _SU_C
#define _SU_C

#pragma warning(disable:4047) /* Indirection level difference warning */
#pragma warning(disable:4996)

#include "../Headers/wavio.h"

/* Changes [data] between unsigned centered at the middle value to signed centered at zero. */
void flip_signedness(void* data, uint8_t bitsize, uint32_t length);

uint32_t write_wav_file(PWAV_FILE f, PFILE out)
{
	uint32_t i;

	if(out == (PFILE)BAD_PTR || out == (PFILE)NULL || out == (PFILE)UNDEF_PTR || f == (PWAV_FILE)BAD_PTR || f == (PWAV_FILE)NULL || f == (PWAV_FILE)UNDEF_PTR)
		return NULL_POINTER;

	if(ferror(out))
		return IO_EXCEPTION;

	for(i = 0;i < 4;i++)
		fputc(f->riff.ChunkID[i],out);

	if(f->fmt.BitsPerSample == 8 && f->fmt.NumChannels & 0x1 && f->data.SubChunk2Size & 0x1)
		write_integer(f->riff.ChunkSize + 1,out);
	else
		write_integer(f->riff.ChunkSize,out);

	for(i = 0;i < 4;i++)
		fputc(f->riff.Format[i],out);

	for(i = 0;i < 4;i++)
		fputc(f->fmt.SubChunk1ID[i],out);

	write_integer(f->fmt.SubChunk1Size,out);
	write_short(f->fmt.AudioFormat,out);
	write_short(f->fmt.NumChannels,out);
	write_integer(f->fmt.SampleRate,out);
	write_integer(f->fmt.ByteRate,out);
	write_short(f->fmt.BlockAlign,out);
	write_short(f->fmt.BitsPerSample,out);

	for(i = 0;i < 4;i++)
		fputc(f->data.SubChunk2ID[i],out);

	if(f->fmt.BitsPerSample == 8 && f->fmt.NumChannels & 0x1 && f->data.SubChunk2Size & 0x1)
		write_integer(f->data.SubChunk2Size + 1,out);
	else
		write_integer(f->data.SubChunk2Size,out);

	/* Wave files expect signed data for bit sizes greater than eight */
	flip_signedness((void*)f->data.data,(uint8_t)f->fmt.BitsPerSample,f->data.SubChunk2Size / (f->fmt.BitsPerSample >> 3));
	
	for(i = 0;i < f->data.SubChunk2Size;i++)
		if(ferror(out))
			return IO_EXCEPTION;
		else
			fputc(f->data.data[i],out);
	
	/* Preserve our data integrety */
	flip_signedness((void*)f->data.data,(uint8_t)f->fmt.BitsPerSample,f->data.SubChunk2Size / (f->fmt.BitsPerSample >> 3));

	/* Sample data must end on an even byte boundary */
	if(f->fmt.BitsPerSample == 8 && f->fmt.NumChannels & 0x1 && i & 0x1)
		fputc(0,out);

	/* Flush the buffer in case the user foolishly doesn't close the file */
	if(fflush(out))
		return IO_EXCEPTION;

	return SUCCESS;
}

/* Reads a wav sound file from [in] into [dest]. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [dest] or [in] is not valid. */
/* Returns IO_EXCEPTION if an error occurs during use of the file stream [in] or if [in] is already errored. */
/* Returns INVALID_WAV_HEADER if a valid wav header is not found. */
/* Returns UNEXPECTED_END_OF_FILE if [in] is valid until an early termination is detected. */
/* Returns UNEXPECTED_CONTINUATION_OF_FILE if [in] has more data than we could read. This is highly unlikely. */
/* Returns BUFFER_OVERFLOW if [in] is too long. */
/* User is responsible for opening and closing [in]. */
uint32_t read_wav_file(PWAV_FILE dest,PFILE in)
{
	const uint8_t tags[4][4] = {"RIFF","WAVE","fmt ","data"};
	uint8_t buff[4];
	uint32_t i;

	if(in == (PFILE)BAD_PTR || in == (PFILE)NULL || in == (PFILE)UNDEF_PTR || dest == (PWAV_FILE)BAD_PTR || dest == (PWAV_FILE)NULL || dest == (PWAV_FILE)UNDEF_PTR)
		return NULL_POINTER;

	/* We won't check these errors during header reading becuase we don't really need to and they take up a lot of lines */
	if(feof(in))
		return UNEXPECTED_END_OF_FILE;

	if(ferror(in))
		return IO_EXCEPTION;

	/* Load RIFF */
	for(i = 0;i < 4;i++)
		dest->riff.ChunkID[i] = (uint8_t)fgetc(in);

	if(strncmp((c_string)tags[0],(c_string)dest->riff.ChunkID,(size_t)4))
		return INVALID_WAV_HEADER;

	/* This should be 36 + dest->data.SubChunk2Size and is the riff chunk's Chunksize */
	for(i = 0;i < 4;i++)
		buff[i] = (uint8_t)fgetc(in);
	
	dest->riff.ChunkSize = *((uint32_t*)buff);

	/* Load WAVE */
	for(i = 0;i < 4;i++)
		dest->riff.Format[i] = (uint8_t)fgetc(in);

	if(strncmp((c_string)tags[1],(c_string)dest->riff.Format,(size_t)4))
		return INVALID_WAV_HEADER;

	/* Load fmt */
	for(i = 0;i < 4;i++)
		dest->fmt.SubChunk1ID[i] = (uint8_t)fgetc(in);

	if(strncmp((c_string)tags[2],(c_string)dest->fmt.SubChunk1ID,(size_t)4))
		return INVALID_WAV_HEADER;

	/* Load Subchunk1Size, should be 16 */
	for(i = 0;i < 4;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.SubChunk1Size = *((uint32_t*)buff);

	if(dest->fmt.SubChunk1Size != 16)
		return INVALID_WAV_HEADER;

	/* Load AudioFormat, Should be 1 */
	for(i = 0;i < 2;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.AudioFormat = *((uint16_t*)buff);

	if(dest->fmt.AudioFormat != 1)
		return INVALID_WAV_HEADER;

	/* Load NumChannels */
	for(i = 0;i < 2;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.NumChannels = *((uint16_t*)buff);

	/* Load the sample rate */
	for(i = 0;i < 4;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.SampleRate = *((uint32_t*)buff);

	/* Load the byte rate */
	for(i = 0;i < 4;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.ByteRate = *((uint32_t*)buff);

	/* Load the block align */
	for(i = 0;i < 2;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.BlockAlign = *((uint16_t*)buff);

	/* Load bits per sample */
	for(i = 0;i < 2;i++)
		buff[i] = (uint8_t)fgetc(in);

	dest->fmt.BitsPerSample = *((uint16_t*)buff);

	if(dest->fmt.BlockAlign != (dest->fmt.NumChannels * dest->fmt.BitsPerSample) >> 3)
		return INVALID_WAV_HEADER;

	if(dest->fmt.ByteRate != dest->fmt.BlockAlign * dest->fmt.SampleRate)
		return INVALID_WAV_HEADER;

	/* Load data */
	for(i = 0;i < 4;i++)
		dest->data.SubChunk2ID[i] = (uint8_t)fgetc(in);

	if(strncmp((c_string)tags[3],(c_string)dest->data.SubChunk2ID,(size_t)4))
		return INVALID_WAV_HEADER;

	/* This should be fmt->BlockAlign * NumSamples */
	for(i = 0;i < 4;i++)
		buff[i] = (uint8_t)fgetc(in);
	
	dest->data.SubChunk2Size = *((uint32_t*)buff);

	if(dest->riff.ChunkSize != 36 + dest->data.SubChunk2Size)
		return INVALID_WAV_HEADER;

	/* We will resume checking file io errors here */
	if(feof(in))
		return UNEXPECTED_END_OF_FILE;

	if(ferror(in))
		return IO_EXCEPTION;

	dest->data.data = (uint8_t*)malloc((size_t)dest->data.SubChunk2Size);

	if(dest->data.data == (uint8_t*)NULL || dest->data.data == (uint8_t*)BAD_PTR || dest->data.data == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < dest->data.SubChunk2Size;i++)
		dest->data.data[i] = (uint8_t)fgetc(in);

	/* We should have read the last byte already so let's raise the eof flag */
	fgetc(in);

	/* We should be at the end of the file if we successfully read in the wav sound file */
	if(!feof(in))
	{
		free((void*)dest->data.data);
		return UNEXPECTED_CONTINUATION_OF_FILE;
	}
	else if(ferror(in))
	{
		free((void*)dest->data.data);
		return IO_EXCEPTION;
	}

	/* Change the data to a useful form */
	flip_signedness((void*)dest->data.data,(uint8_t)dest->fmt.BitsPerSample,dest->data.SubChunk2Size / (dest->fmt.BitsPerSample << 3));
	return SUCCESS;
}

#ifdef WINDOWS
void InitWavFmt(PWAVEFORMATEX g_WavFmt, WORD nchannels, DWORD sps, WORD bitrate)
{
	g_WavFmt->wFormatTag = WAVE_FORMAT_PCM;
	g_WavFmt->nChannels = nchannels;
	g_WavFmt->nSamplesPerSec = sps;
	g_WavFmt->wBitsPerSample = bitrate;
	g_WavFmt->nBlockAlign = g_WavFmt->nChannels * g_WavFmt->wBitsPerSample / 8;
	g_WavFmt->nAvgBytesPerSec = g_WavFmt->nChannels * g_WavFmt->wBitsPerSample / 8 * g_WavFmt->nSamplesPerSec;
	g_WavFmt->cbSize = 0;

	return;
}

uint32_t record_wav(PWAV_FILE dest, WORD nchannels, DWORD sps, WORD bitrate, uint32_t length)
{
	WAVEFORMATEX g_WavFmt;
	HWAVEIN hWavIn = NULL;
	WAVEHDR WavHdr;

	uint8_t* pBuffer = NULL;
	uint8_t* pWavBuffer = NULL;

	PFILE fpPCMFile = NULL;
	PFILE fpWavFile = NULL;

	MMTIME MMTime;
	MMRESULT ret = NULL;
	
	if(dest == NULL || dest == BAD_PTR || dest == UNDEF_PTR)
		return NULL_POINTER;

	InitWavFmt(&g_WavFmt,nchannels,sps,bitrate);
	MMTime.wType = TIME_BYTES;

	if(!waveInGetNumDevs())
		return AUDIO_DEVICE_NOT_FOUND;

	ret = waveInOpen(&hWavIn,WAVE_MAPPER,&g_WavFmt,0,0,WAVE_FORMAT_QUERY);

	if(ret)
		return UNSUPPORTED_WAV_FORMAT;

	ret = waveInOpen(&hWavIn,WAVE_MAPPER,&g_WavFmt,0,0,CALLBACK_NULL);

	if(ret)
		return UNSUPPORTED_WAV_FORMAT;

	pBuffer = (uint8_t*)malloc(WAVBUFFERLENGTH(nchannels,sps,bitrate,length));

	if(pBuffer)
		memset(pBuffer,0,WAVBUFFERLENGTH(nchannels,sps,bitrate,length));
	else
		return BUFFER_OVERFLOW;

	WavHdr.lpData = pBuffer;
	WavHdr.dwBufferLength = WAVBUFFERLENGTH(nchannels,sps,bitrate,length);
	WavHdr.dwBytesRecorded = 0;
	WavHdr.dwUser = 0;
	WavHdr.dwFlags = 0;
	WavHdr.dwLoops = 1;
	WavHdr.lpNext = 0;
	WavHdr.reserved = 0;

	ret = waveInPrepareHeader(hWavIn,&WavHdr,sizeof(WAVEHDR));

	if(ret)
	{
		free(pBuffer);
		return WAV_HEADER_CANNOT_BE_PREPARED;
	}

    ret = waveInAddBuffer(hWavIn,&WavHdr,sizeof(WAVEHDR));

	if(ret)
	{
		free(pBuffer);
		return WAV_REJECTS_BUFFER;
	}

	ret = waveInStart(hWavIn);

	if(ret)
	{
		free(pBuffer);
		return WAV_REFUSES_TO_START;
	}

	Sleep((DWORD)(length * 1000));

	waveInGetPosition(hWavIn,&MMTime,sizeof(MMTime));
	waveInReset(hWavIn);

	waveInUnprepareHeader(hWavIn,&WavHdr,sizeof(WAVEHDR));
	waveInClose(hWavIn);

	dest->data.SubChunk2ID[0] = 'd';
	dest->data.SubChunk2ID[1] = 'a';
	dest->data.SubChunk2ID[2] = 't';
	dest->data.SubChunk2ID[3] = 'a';

	dest->data.SubChunk2Size = WavHdr.dwBufferLength;
	dest->data.data = pBuffer;

	dest->fmt.SubChunk1ID[0] = 'f';
	dest->fmt.SubChunk1ID[1] = 'm';
	dest->fmt.SubChunk1ID[2] = 't';
	dest->fmt.SubChunk1ID[3] = ' ';

	dest->fmt.SubChunk1Size = 16;
	dest->fmt.AudioFormat = 1;
	dest->fmt.NumChannels = (uint16_t)nchannels;
	dest->fmt.SampleRate = (uint32_t)sps;
	dest->fmt.ByteRate = dest->fmt.SampleRate * dest->fmt.NumChannels * (bitrate >> 3);
	dest->fmt.BlockAlign = dest->fmt.NumChannels * (bitrate >> 3);
	dest->fmt.BitsPerSample = (uint16_t)bitrate;

	dest->riff.ChunkID[0] = 'R';
	dest->riff.ChunkID[1] = 'I';
	dest->riff.ChunkID[2] = 'F';
	dest->riff.ChunkID[3] = 'F';

	dest->riff.ChunkSize = 4 + (8 + dest->fmt.SubChunk1Size) + (8 + dest->data.SubChunk2Size);

	dest->riff.Format[0] = 'W';
	dest->riff.Format[1] = 'A';
	dest->riff.Format[2] = 'V';
	dest->riff.Format[3] = 'E';

	flip_signedness((void*)dest->data.data,(uint8_t)dest->fmt.BitsPerSample,dest->data.SubChunk2Size * dest->fmt.NumChannels);
	return SUCCESS;
}
#endif

void flip_signedness(void* data, uint8_t bitsize, uint32_t length)
{
	uint16_t* data16;
	uint32_t* data32;
	uint32_t i;

	switch(bitsize)
	{
	case 8:
		break;
	case 16:
		data16 = (uint16_t*)data;

		for(i = 0;i < length;i++)
			data16[i] ^= 0x8000;

		break;
	case 32:
		data32 = (uint32_t*)data;

		for(i = 0;i < length;i++)
			data32[i] ^= 0x80000000;

		break;
	default:
		break;
	}

	return;
}

#endif