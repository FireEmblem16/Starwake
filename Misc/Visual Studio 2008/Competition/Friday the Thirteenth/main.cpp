/*
ID: fire_em1
PROG: friday
LANG: C++
*/

#include <fstream>
using namespace::std;

int i=0;
int n=0;
int nf=0;
int nt=0;
int m=0;
int days[7]={0,0,0,0,0,0,0};
int months[12]={1,4,4,0,2,5,0,3,6,1,4,6};

void checkmonth(int ntn)
{
	ntn+=13+months[m];
	
	if(m<2)
		if(n%400==0||n%4==0&&n%100!=0)
			ntn-=1;

	if(n%400>=0&&n%400<100)
		ntn+=6;
	else if(n%400>=100&&n%400<200)
		ntn+=4;
	else if(n%400>=200&&n%400<300)
		ntn+=2;
	else if(n%400>=300&&n%400<400)
		ntn+=0;

	days[ntn%7]+=1;
}

void checkyear()
{
	nt=n;

	for(nt;nt>=100;nt-=100);

	nt=(nt-nt%4)/4+nt;

	for(m=0;m<12;m++)
		checkmonth(nt);
}

int main()
{
	ofstream out("friday.out",ios::out);
	ifstream in("friday.in",ios::in);

	in>>n;

	nf=1900+n;
	n=1900;

	for(n;n<nf;n++)
		checkyear();

	for(i=0;i<6;i++)
		out<<days[i]<<" ";
	out<<days[6]<<"\n";

	return 0;
}