#ifndef FAT12_CPP
#define FAT12_CPP

#pragma warning(disable:4701)
#pragma warning(disable:4100)

#include <fat12.h>
#include <string.h>
#include <flpydsk.h>
#include <bpb.h>
#include <ctype.h>

#define SECTOR_SIZE 512

FILESYSTEM _FSysFat;
MOUNT_INFO _MountInfo;
uint8_t FAT[SECTOR_SIZE * 2];

void ToDosFileName(const char* filename, char* fname, unsigned int FNameLength)
{
	unsigned int i = 0;

	if(FNameLength > 11)
		return;

	if(!fname || !filename)
		return;

	memset(fname,' ',FNameLength);

	for(i = 0;i < strlen(filename) - 1 && i < FNameLength;i++)
	{
		if(filename[i] == '.' || i == 8)
			break;

		fname[i] = toupper(filename[i]);
	}

	if(filename[i] == '.')
	{
		for(int k = 0;k < 3;k++)
		{
			i++;

			if(filename[i])
				fname[k + 8] = filename[i];
		}
	}

	for(i = 0;i < 3;i++)
		fname[i + 8] = toupper(fname[i + 8]);

	return;
}

FILE fsysFatDirectory(const char* DirectoryName)
{
	FILE file;
	unsigned char* buf;
	PDIRECTORY directory;

	char DosFileName[12];
	ToDosFileName(DirectoryName,DosFileName,11);
	DosFileName[11] = 0;

	for(uint32_t sector = 0;sector < _MountInfo.rootSize; sector++)
	{
		buf = (unsigned char*)flpydsk_read_sector(_MountInfo.rootOffset + sector);
		directory = (PDIRECTORY)buf;

		for(uint32_t i = 0;i < _MountInfo.numRootEntries / _MountInfo.rootSize;i++)
		{
			char name[12];
			memcpy(name,directory->Filename,11);
			name[11] = 0;

			if(strcmp(DosFileName,name) == 0)
			{
				strcpy(file.name,DirectoryName);
				file.id = 0;
				file.currentCluster = directory->FirstCluster;
				file.fileLength = directory->FileSize;
				file.eof = 0;

				if(directory->Attrib == 0x10)
					file.flags = FS_DIRECTORY;
				else
					file.flags = FS_FILE;

				return file;
			}

			directory++;
		}
	}

	file.flags = FS_INVALID;
	return file;
}

void fsysFatRead(PFILE file, unsigned char* Buffer, unsigned int Length)
{
	if(file)
	{
		unsigned int physSector = _MountInfo.rootOffset + _MountInfo.rootSize + file->currentCluster - 2;
		unsigned char* sector = (unsigned char*)flpydsk_read_sector(physSector);
		
		memcpy(Buffer,sector,512);

		unsigned int FAT_Offset = file->currentCluster + (file->currentCluster / 2);
		unsigned int FAT_Sector = _MountInfo.reservedSectors + (FAT_Offset / SECTOR_SIZE);
		unsigned int entryOffset = FAT_Offset % SECTOR_SIZE;

		sector = (unsigned char*)flpydsk_read_sector(FAT_Sector);
		memcpy(FAT,sector,512);

		sector = (unsigned char*)flpydsk_read_sector(FAT_Sector + 1);
		memcpy(FAT + SECTOR_SIZE,sector,512);

		uint16_t nextCluster = *(uint16_t*)&FAT[entryOffset];

		if(file->currentCluster & 0x0001)
			nextCluster >>= 4;
		else
			nextCluster &= 0x0FFF;

		if(nextCluster >= 0xFF8)
		{
			file->eof = 1;
			return;
		}

		if(nextCluster == 0)
		{
			file->eof = 1;
			return;
		}

		file->currentCluster = nextCluster;
	}

	return;
}

void fsysFatClose(PFILE file)
{
	if(file)
		file->flags = FS_INVALID;

	return;
}

FILE fsysFatOpenSubDir(FILE kFile, const char* filename)
{
	FILE file;

	char DosFileName[12];
	ToDosFileName(filename,DosFileName,11);
	DosFileName[11] = 0;

	while(!kFile.eof)
	{
		unsigned char buf[512];
		fsysFatRead(&file,buf,512);

		PDIRECTORY pkDir = (PDIRECTORY)buf;
		
		for(unsigned int i = 0;i < _MountInfo.numRootEntries / _MountInfo.rootSize;i++)
		{
			char name[12];
			memcpy(name,pkDir->Filename,11);
			name[11] = 0;

			if(strcmp(name,DosFileName) == 0)
			{
				strcpy(file.name,filename);
				file.id = 0;
				file.currentCluster = pkDir->FirstCluster;
				file.fileLength = pkDir->FileSize;
				file.eof = 0;

				if(pkDir->Attrib == 0x10)
					file.flags = FS_DIRECTORY;
				else
					file.flags = FS_FILE;

				return file;
			}

			pkDir++;
		}
	}

	file.flags = FS_INVALID;
	return file;
}

FILE fsysFatOpen(const char* FileName)
{
	FILE curDirectory;
	char* p = (char*)FileName;
	bool rootDir = true;
	char* path = (char*)FileName;

	if(!strchr(path,'/'))
	{
		curDirectory = fsysFatDirectory(path);

		if(curDirectory.flags == FS_FILE || curDirectory.flags == FS_DIRECTORY)
			return curDirectory;

		FILE ret;
		ret.flags = FS_INVALID;
		return ret;
	}

	while(p)
	{
		char pathname[12];
		int i = 0;
		
		for(i = 0;i < 12;i++)
		{
			if(p[i] == '/' || p[i] == '\0')
				break;

			pathname[i] = p[i];
		}

		pathname[i] = 0;

		if(rootDir)
		{
			curDirectory = fsysFatDirectory(pathname);
			rootDir = false;
		}
		else
			curDirectory = fsysFatOpenSubDir(curDirectory,pathname);

		if(curDirectory.flags == FS_INVALID)
			break;

		if(curDirectory.flags == FS_FILE)
			return curDirectory;

		p = strchr(p,'/');

		if(p)
			p++;
	}

	FILE ret;
	ret.flags = FS_INVALID;
	return ret;
}

void fsysFatMount()
{
	PBOOTSECTOR bootsector;

	bootsector = (PBOOTSECTOR)flpydsk_read_sector(0);

	_MountInfo.numSectors = bootsector->Bpb.NumSectors;
	_MountInfo.fatOffset = bootsector->Bpb.ReservedSectors;
	_MountInfo.fatSize = bootsector->Bpb.SectorsPerFat;
	_MountInfo.fatEntrySize = bootsector->Bpb.SectorsPerFat;
	_MountInfo.numRootEntries = bootsector->Bpb.NumDirEntries;
	_MountInfo.rootOffset = (bootsector->Bpb.NumberOfFats * bootsector->Bpb.SectorsPerFat) + bootsector->Bpb.ReservedSectors;
	_MountInfo.rootSize = (bootsector->Bpb.NumDirEntries * 32) / bootsector->Bpb.BytesPerSector;
	_MountInfo.reservedSectors = bootsector->Bpb.ReservedSectors;

	return;
}

void fsysFatInitialize()
{
	strcpy(_FSysFat.Name,"FAT12");
	_FSysFat.Directory = fsysFatDirectory;
	_FSysFat.Mount = fsysFatMount;
	_FSysFat.Open = fsysFatOpen;
	_FSysFat.Read = fsysFatRead;
	_FSysFat.Close = fsysFatClose;

	volRegisterFileSystem(&_FSysFat,0);
	fsysFatMount();

	return;

	return;
}

#endif