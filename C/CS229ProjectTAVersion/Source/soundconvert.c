#ifndef _SOUND_CONVERT_C
#define _SOUND_CONVERT_C

#include "../Headers/soundconvert.h"

uint32_t su_to_wav(PWAV_FILE dest, PSU_FILE src)
{
	uint32_t i;

	if(dest == (PWAV_FILE)BAD_PTR || dest == (PWAV_FILE)NULL || dest == (PWAV_FILE)UNDEF_PTR || src == (PSU_FILE)BAD_PTR || src == (PSU_FILE)NULL || src == (PSU_FILE)UNDEF_PTR)
		return NULL_POINTER;

	dest->data.SubChunk2ID[0] = 'd';
	dest->data.SubChunk2ID[1] = 'a';
	dest->data.SubChunk2ID[2] = 't';
	dest->data.SubChunk2ID[3] = 'a';

	dest->data.SubChunk2Size = src->header.num_samples * (src->header.flags & SU_STEREO ? 2 : 1) * (src->header.flags & SU_8_BIT ? 1 : (src->header.flags & SU_16_BIT ? 2 : 4));
	dest->data.data = (uint8_t*)malloc(dest->data.SubChunk2Size);

	if(dest->data.data == (uint8_t*)NULL || dest->data.data == (uint8_t*)BAD_PTR || dest->data.data == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < dest->data.SubChunk2Size;i++)
		dest->data.data[i] = src->samples[i];

	dest->fmt.SubChunk1ID[0] = 'f';
	dest->fmt.SubChunk1ID[1] = 'm';
	dest->fmt.SubChunk1ID[2] = 't';
	dest->fmt.SubChunk1ID[3] = ' ';

	dest->fmt.SubChunk1Size = 16;
	dest->fmt.AudioFormat = 1;
	dest->fmt.NumChannels = src->header.flags & SU_STEREO ? 2 : 1;
	dest->fmt.SampleRate = src->header.freq;
	dest->fmt.ByteRate = dest->fmt.SampleRate * dest->fmt.NumChannels * (src->header.flags & SU_8_BIT ? 1 : (src->header.flags & SU_16_BIT ? 2 : 4));
	dest->fmt.BlockAlign = dest->fmt.NumChannels * (src->header.flags & SU_8_BIT ? 1 : (src->header.flags & SU_16_BIT ? 2 : 4));
	dest->fmt.BitsPerSample = (src->header.flags & SU_8_BIT ? 8 : (src->header.flags & SU_16_BIT ? 16 : 32));

	dest->riff.ChunkID[0] = 'R';
	dest->riff.ChunkID[1] = 'I';
	dest->riff.ChunkID[2] = 'F';
	dest->riff.ChunkID[3] = 'F';

	dest->riff.ChunkSize = 4 + (8 + dest->fmt.SubChunk1Size) + (8 + dest->data.SubChunk2Size);

	dest->riff.Format[0] = 'W';
	dest->riff.Format[1] = 'A';
	dest->riff.Format[2] = 'V';
	dest->riff.Format[3] = 'E';

	return SUCCESS;
}

uint32_t wav_to_su(PSU_FILE dest, PWAV_FILE src)
{
	uint32_t i;

	if(dest == (PSU_FILE)BAD_PTR || dest == (PSU_FILE)NULL || dest == (PSU_FILE)UNDEF_PTR || src == (PWAV_FILE)BAD_PTR || src == (PWAV_FILE)NULL || src == (PWAV_FILE)UNDEF_PTR)
		return NULL_POINTER;

	dest->header.freq = src->fmt.SampleRate;
	
	dest->header.flags = 0;
	dest->header.flags |= src->fmt.NumChannels == 1 ? 0x0 : SU_STEREO;
	dest->header.flags |= src->fmt.BitsPerSample == 0x8 ? SU_8_BIT : (src->fmt.BitsPerSample == 0x10 ? SU_16_BIT : 0x0);
	
	dest->header.num_samples = (src->data.SubChunk2Size / src->fmt.NumChannels / src->fmt.BitsPerSample) << 3;
	dest->samples = (uint8_t*)malloc(src->data.SubChunk2Size);

	if(dest->samples == (uint8_t*)NULL || dest->samples == (uint8_t*)BAD_PTR || dest->samples == (uint8_t*)UNDEF_PTR)
		return BUFFER_OVERFLOW;

	for(i = 0;i < src->data.SubChunk2Size;i++)
		dest->samples[i] = src->data.data[i];

	return SUCCESS;
}

#endif