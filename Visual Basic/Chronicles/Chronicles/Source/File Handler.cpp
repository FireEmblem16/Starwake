#pragma region Title
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// Chronacles of Mu Alpha Theta ///////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////This game is completely fictional...mostly/////////////////////////
/////////////////////////////////Produced by Omacron Games//////////////////////////////////
//////////////////This game is not available for redistributing or copying//////////////////
/////This game is not open source so if you are reading this you better have permission/////
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
#pragma endregion

#ifndef FILE_HANDLER_CPP
#define FILE_HANDLER_CPP

#include "File Handler.h"

#pragma region SaveFile
SaveFile::SaveFile(char* Name, BYTE EncryptionKey)
{
	this->Key=EncryptionKey;
	if(dbFileExist(Name)==1)
		;//Do fancy stuff here for selecting whether or not to overwrite using user input
	this->FileName=Name;//change this so Save Data\\ is added to the name automatically
	this->FileSize=500;//filesize doesn't seem to work

	return;
}
SaveFile::~SaveFile()
{
	delete this->FileName;
	
	return;
}
void SaveFile::ReadData(char* DataStorage, int Position, int Length)
{
	DataStorage="";
	
	for(int i=0;i<Length&&(Position+i)<this->FileSize;i++)
		strcat(DataStorage,(char*)(dbReadByteFromFile(this->FileName,Position+i)^this->Key));//strcat doesn't seem to work
		//Seems that it won't dynamically locate space thus arrays must be used
	return;
}
void SaveFile::WriteData(char* Data, int Position)
{
	for(int i=0;i<strlen(Data)&&(Position+i)<this->FileSize;i++)
		dbWriteByteToFile(this->FileName,Position+i,((int)Data[i])^this->Key);

	return;
}
#pragma endregion

#endif