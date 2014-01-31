/*
ID: fire_em1
PROG: ride
LANG: C++
*/

#include <fstream>
using std::ifstream;
using std::ofstream;
using std::ios;
#include <string>
using std::string;

int main()
{
	ifstream fin("ride.in");
	ofstream fout("ride.out");

	string comet("");
	string group("");
	int ca=1;
	int ga=1;
	int cl=0;
	int gl=0;

	fin>>comet>>group;
	
	cl=comet.length();
	gl=group.length();

	for(int i=0;i<cl;i++)
		ca*=(int)comet[i]-64;

	for(int i=0;i<gl;i++)
		ga*=(int)group[i]-64;

	if(ca%47==ga%47)
		fout<<"GO"<<"\n";
	else
		fout<<"STAY"<<"\n";

	return 0;
}