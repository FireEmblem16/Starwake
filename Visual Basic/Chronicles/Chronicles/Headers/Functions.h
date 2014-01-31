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

#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#pragma region Declarations
#ifndef DARKGDK
#include "DarkGDK.h"
#endif
#ifndef DARKGDK2
#include "DarkGDK2.h"
#endif
#pragma endregion

bool CompareStrings(char* a, char* b)
{
	if(strlen(a)!=strlen(b))
		return false;

	for(int i=0;a[i]==b[i];i++)
		if(i==strlen(a)-1)
			return true;

	return false;
}

void ShowSomeData(bool rawdata, int num)
{
	char* data;

	if(rawdata)
		data="Data = true";
	else
		data="Data = false";

	dbText(dbScreenWidth()-20-dbTextWidth(data),dbScreenHeight()-40-dbTextHeight(data)*num,data);
	
	delete data;

	return;
}

void ShowSomeData(char* data, int num)
{
	dbText(dbScreenWidth()-20-dbTextWidth(data),dbScreenHeight()-40-dbTextHeight(data)*num,data);
	
	return;
}

void ShowSomeData(float data, int num)
{
	char szFPS[256]="";
	strcpy(szFPS,"Data = ");
	strcat(szFPS,dbStr(data));
	dbText(dbScreenWidth()-20-dbTextWidth(szFPS),dbScreenHeight()-40-dbTextHeight(szFPS)*num,szFPS);
	
	return;
}

void ShowSomeData(int data, int num)
{
	char szFPS[256]="";
	strcpy(szFPS,"Data = ");
	strcat(szFPS,dbStr(data));
	dbText(dbScreenWidth()-20-dbTextWidth(szFPS),dbScreenHeight()-40-dbTextHeight(szFPS)*num,szFPS);
	
	return;
}

#endif