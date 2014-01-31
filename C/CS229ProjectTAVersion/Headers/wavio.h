#ifndef _WAV_H
#define _WAV_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "defines.h"
#include "util.h"

#ifdef WINDOWS
#include <windows.h>
#include <mmsystem.h>
#include <aviriff.h>
#include <conio.h>
#endif

/* Wave buf len (channel * samples per second * bitrate / 8 * length in seconds) */
#define WAVBUFFERLENGTH(nchannels,sps,bitrate,len) (nchannels * sps * (bitrate / 8) * len)

/* Wave header length */
#define HEADERLENGTH (sizeof(RIFFLIST) + sizeof(RIFFCHUNK) + sizeof(WAVEFORMATEX) + sizeof(RIFFCHUNK))

typedef struct wav_riff_chunk
{
	uint8_t ChunkID[4];
	uint32_t ChunkSize;
	uint8_t Format[4];
} WAV_RIFF_CHUNK, *PWAV_RIFF_CHUNK;

typedef struct wav_fmt_chunk
{
	uint8_t SubChunk1ID[4];
	uint32_t SubChunk1Size;
	uint16_t AudioFormat;
	uint16_t NumChannels;
	uint32_t SampleRate;
	uint32_t ByteRate;
	uint16_t BlockAlign;
	uint16_t BitsPerSample;
} WAV_FMT_CHUNK, *PWAV_FMT_CHUNK;

typedef struct wav_data_chunk
{
	uint8_t SubChunk2ID[4];
	uint32_t SubChunk2Size;
	uint8_t* data;
} WAV_DATA_CHUNK, *PWAV_DATA_CHUNK;

typedef struct wav_file
{
	WAV_RIFF_CHUNK riff;
	WAV_FMT_CHUNK fmt;
	WAV_DATA_CHUNK data;
} WAV_FILE, *PWAV_FILE;

/* Writes the wav sound file [f] to the file stream [out] in the appropriate format. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [f] or [out] are not valid. */
/* Returns IO_EXCEPTION if an error occurs during use of the file stream [out] or if [out] is already errored. */
/* User is responsible for opening the file stream [out] as well as closing the file stream after use. */
uint32_t write_wav_file(PWAV_FILE f,PFILE out);

/* Reads a wav sound file from [in] into [dest]. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [dest] or [in] is not valid. */
/* Returns IO_EXCEPTION if an error occurs during use of the file stream [in] or if [in] is already errored. */
/* Returns INVALID_WAV_HEADER if a valid wav header is not found. */
/* Returns UNEXPECTED_END_OF_FILE if [in] is valid until an early termination is detected. */
/* Returns UNEXPECTED_CONTINUATION_OF_FILE if [in] has more data than we could read. This is highly unlikely. */
/* Returns BUFFER_OVERFLOW if [in] is too long. */
/* User is responsible for opening and closing [in]. */
uint32_t read_wav_file(PWAV_FILE dest,PFILE in);

#ifdef WINDOWS
/* Reads a wave file into [dest] from the available audio device which is usualy a microphone. */
/* Returns SUCCESS if no problems are encountered. */
/* Returns NULL_POINTER if [dest] is not valid. */
/* Returns AUDIO_DEVICE_NOT_FOUND if no sound capture device could be found. */
/* Returns UNSUPPORTED_WAV_FORMAT if wave files could not be made from raw data. */
/* Returns BUFFER_OVERFLOW if there is not enough memory for the amount of sound data desired. */
/* Returns WAV_HEADER_CANNOT_BE_PREPARED if the wave header could not be created. */
/* Returns WAV_REJECTS_BUFFER if a buffer could not be attached to the pending wave file. */
/* Returns WAV_REFUSES_TO_START is the audio device could not be started. */
uint32_t record_wav(PWAV_FILE dest,WORD nchannels,DWORD sps,WORD bitrate,uint32_t length);
#endif

#endif