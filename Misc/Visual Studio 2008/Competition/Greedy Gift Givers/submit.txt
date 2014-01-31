/*
ID: fire_em1
PROG: gift1
LANG: C++
*/

#include <fstream>
using namespace::std;

char names[10][14];
char* temp=new char [14];
int k=0;
int l=0;
int z=0;

bool isequal(int n)
{
	bool flag=true;
	z=strlen(temp);

	for(k=0;k<z;k++)
	{
		if((int)temp[k]!=(int)names[n][k])
		{
			flag=false;
			break;
		}
	}

	if(flag)
		return true;
	
	return false;
}

int nametoint()
{
	for(l=0;l<10;l++)
	{
		if(isequal(l))
			return l;
	}
	
	return 10;
}

int main()
{
	ifstream in("gift1.in",ios::in);
	ofstream out("gift1.out",ios::out);

	int a=0;
	int b=0;
	int c=0;
	int d=0;
	int e=0;
	int i=0;
	int j=0;

	int value[10]={0,0,0,0,0,0,0,0,0,0};

	in>>e;

	for(i=0;i<e;i++)
	{
		in>>temp;
		for(j=0;j<14;j++)
			names[i][j]=temp[j];
	}

	while(!in.eof())
	{
		in>>temp;
		in>>a>>b;

		if(temp=="")
			return 0;

		if(b!=0)
		{
			c=a/b;

			d=nametoint();
	
			value[d]=value[d]-a+(a%b);

			for(i=0;i<b;i++)
			{
				in>>temp;
				d=nametoint();
				value[d]+=c;
			}
		}
	}

	for(i=0;i<e;i++)
		out<<names[i]<<" "<<value[i]<<"\n";

	return 0;
}