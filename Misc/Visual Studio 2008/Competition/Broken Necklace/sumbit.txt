/*
ID: fire_em1
PROG: beads
LANG: C++
*/

#include <fstream>
using namespace::std;

char* necklace=new char [350];
int i=0;
int cursor=0;
int temp=0;
int len=0;
int maxall=0;
int maxn=0;
int last=0;
int now=0;
int hold;
int fbuff=0;
int ebuff=0;
int wbuff=0;
bool flag=true;
bool eflag=true;
bool fflag=true;

char findfirst()
{
	flag=true;

	for(i=0;flag;i++)
		if(necklace[i]!='w')
			return necklace[i];
		else if(necklace[i+1]!='w')
			return necklace[i+1];
		else if(i==len-2)
			flag=false;

	flag=true;
	return 'w';
}

char findlast()
{
	flag=true;

	for(i=len-1;flag;i--)
		if(necklace[i]!='w')
			return necklace[i];
		else if(necklace[i-1]!='w')
			return necklace[i-1];
		else if(i==1)
			flag=false;

	flag=true;
	return 'w';
}
int fcount(int k,char current=' ')
{
	for(i=k;flag;i++)
	{
		if((necklace[i]=='r'||necklace[i]=='b')&&current==' ')
			current=necklace[i];
		if(necklace[i]!=current&&necklace[i]!='w')
		{
			flag=false;
			i--;
		}
		else if(necklace[i+1]!=current&&necklace[i+1]!='w')
			flag=false;
	}
	flag=true;

	return i-k;
}

int bcount(int k,char current=' ')
{
	for(i=k;flag;i--)
	{
		if((necklace[i]=='r'||necklace[i]=='b')&&current==' ')
			current=necklace[i];
		if(necklace[i]!=current&&necklace[i]!='w')
		{
			flag=false;
			i++;
		}
		else if(necklace[i-1]!=current&&necklace[i-1]!='w')
			flag=false;
	}
	flag=true;

	return k-i;
}


int bwocount(int k,char current=' ')
{
	for(i=k;flag;i--)
	{
		if((necklace[i]=='r'||necklace[i]=='b')&&current==' ')
			current=necklace[i];
		if(necklace[i]!=current&&necklace[i]!='w')
		{
			flag=false;
			i++;
		}
		else if(necklace[i-1]!=current&&necklace[i-1]!='w')
			flag=false;
	}
	flag=true;

	return k-i;
}


int backtrack(int k,char current=' ')
{
	for(i=k;flag;i--)
		if(necklace[i-1]!='w'&&necklace[i-1]!=current)
		{
			flag=false;
			i++;
		}
	flag=true;

	return k-i;
}

int fwbuff()
{
	int zzz=0;

	for(i=0;necklace[i]=='w'&&i<len;i++)
		zzz++;

	return zzz;
}
int ewbuff()
{
	int zzz=0;

	for(i=len-1;necklace[i]=='w'&&i>=0;i--)
		zzz++;

	return zzz;
}

int analize()
{
	char current=findfirst();
	char last=findlast();

	if(current=='w'||last=='w')
		return len;

	ebuff=ewbuff();
	fbuff=fwbuff();
	wbuff=fbuff+ebuff;
	cursor=fwbuff();
	
	cursor=fcount(0,current);
	maxn=cursor+bcount(len-1,current);

	if(current=='b')
	{
		if(bcount(len-1-maxn+cursor,'r')+maxn>len)
			return len;
		maxn+=fcount(cursor,'r');
	}
	else
	{
		if(bcount(len-1-maxn+cursor,'b')+maxn>len)
			return len;
		maxn+=fcount(cursor,'b');
	}

	current=necklace[cursor];

	if(cursor==len)
		return len;

	while(true)
	{
		cursor-=backtrack(cursor,current);

		now=fcount(cursor,current);
		cursor+=now;

		if(cursor==len)
			fflag=false;

		if(!fflag)
		{
			cursor=0;
			hold=fcount(cursor,current);
			now+=hold;
			cursor+=hold;

			if(current=='b')
				current='r';
			else
				current='b';

			now+=fcount(cursor,current);

			if(now>maxn)
				maxn=now;

			break;
		}

		if(current=='b')
			current='r';
		else
			current='b';
		
		hold=fcount(cursor,current);
		now+=hold;
		cursor+=hold;

		if(cursor==len)
			eflag=false;

		if(!eflag)
		{
			temp=cursor;

			cursor=0;
			now+=fcount(cursor,current);

			if(now>maxn)
				maxn=now;

			cursor=temp;
			eflag=true;
		}

		cursor-=hold;

		if(now>maxn)
			maxn=now;
	}

	return maxn;
}

int main()
{
	ifstream in("beads.in",ios::in);
	ofstream out("beads.out",ios::out);

	in>>len>>necklace;

	maxall=analize();

	out<<maxall<<"\n";

	return 0;
}