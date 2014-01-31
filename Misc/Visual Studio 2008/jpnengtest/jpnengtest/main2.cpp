#ifndef NAOMI_TEST_FILE
#define NAOMI_TEST_FILE

#include "windows.h"
#include <fstream>
using namespace::std;

bool CompareCharPointers(char*,char*);
void main();
#

bool CompareCharPointers(char* string1, char* string2)
{
	int l1=strlen(string1);
	int l2=strlen(string2);
	
	if(l1!=l2)
		return false;

	for(int i=0;i<l1;i++)
		if(string1[i]!=string2[i])
			return false;

	return true;
}

void main()
{
	//istream File("main.cpp",ios::in);
	//ostream Temp("temp.txt",ios::out);
int a[2];
	char Parameters[]="bool hi,int yes";
	char* ParametersTemp;
	char ParametersShort[1000];
	char temp;
	bool seek=true;
	bool flag=false;
	bool cflag=true;
	int l=strlen(Parameters);
	int j=0;

	/*for(int i=0;i<l;i++)
	{
		if(seek)
			if(Parameters[i]==' ')
				seek=false;
			else
				ParametersShort[j++]=Parameters[i];
		else
			if(Parameters[i]==',')
			{
				ParametersShort[j++]=Parameters[i];
				seek=true;
			}
		if(i==l-1)
			ParametersShort[j++]='\0';
	}

	while(!flag)
	{
		File.get(temp);
		if(temp=='#')
			if(File.peek()=='\n')
				flag=true;
			else
				Temp.put(temp);
		else if(temp=='(')
		{
			Temp.put(temp);
			for(int i=0;File.peek()!=')';i++)
			{
				File.get(temp);
				Temp.put(temp);
				ParametersTemp[i]=temp;
			}
			if(CompareCharPointers(ParametersTemp,ParametersShort))
			{
				cflag=false;
				flag=true;
			}
		}
		else
			Temp.put(temp);
	}

	flag=false;

	bool is=CompareCharPointers(ParametersShort,ParametersTemp);
*/}

#endif