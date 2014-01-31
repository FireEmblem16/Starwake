#ifndef FSYS_CPP
#define FSYS_CPP

#include <fsys.h>
#include <string.h>

#define DEVICE_MAX 26

PFILESYSTEM _FileSystems[DEVICE_MAX];

FILE volOpenFile(const char* fname)
{
	FILE file;
	file.flags = FS_INVALID;

	if(fname)
	{
		unsigned char device = 'a';
		char* filename = (char*)fname;

		if(fname[1] == ':')
		{
			device = fname[0];
			filename += 3;
		}

		device = tolower(device);
		if(device < 'a' || device > 'z')
			return file;

		if(_FileSystems[device - 'a'])
		{
			file = _FileSystems[device - 'a']->Open(filename);
			file.deviceID = device;
			return file;
		}
	}

	return file;
}

void volReadFile(PFILE file, unsigned char* Buffer, unsigned int Length)
{
	if(file)
		if(_FileSystems[file->deviceID - 'a'])
			_FileSystems[file->deviceID - 'a']->Read(file,Buffer,Length);

	return;
}

void volCloseFile(PFILE file)
{
	if(file)
		if(_FileSystems[file->deviceID - 'a'])
			_FileSystems[file->deviceID - 'a']->Close(file);

	return;
}

void volRegisterFileSystem(PFILESYSTEM fsys, unsigned int deviceID)
{
	static int i = 0;
	
	if(i < DEVICE_MAX)
		if(fsys)
		{
			_FileSystems[deviceID] = fsys;
			i++;
		}

	return;
}

void volUnregisterFileSystem(PFILESYSTEM fsys)
{
	for(int i = 0;i < DEVICE_MAX;i++)
		if(_FileSystems[i] == fsys)
			_FileSystems[i] = 0;

	return;
}

void volUnregisterFileSystemByID(unsigned int deviceID)
{
	if(deviceID < DEVICE_MAX)
		_FileSystems[deviceID] = 0;

	return;
}

#endif