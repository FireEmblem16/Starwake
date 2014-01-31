#ifndef INPUTHANDLE_H
#define INPUTHANDLE_H

#include <windows.h>
#include <time.h>
#include <string>
using std::string;

//key return order 0:unpushed -127:pushed 1:unpushed but toggled -128: pushed

BYTE keyset[192]={0x01,0x02,0x03,0x04,0x05,0x06,0x08,0x09,0x0C,0x0D,0x10,0x11,0x12,0x13,0x14,0x15,0x17,0x18,0x19,0x1B,0x1C,0x1D,0x1E,0x1F,
				  0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F,0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,
				  0x38,0x39,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x53,0x54,0x55,0x56,
				  0x57,0x58,0x59,0x5A,0x5B,0x5C,0x5D,0x5F,0x60,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,
				  0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x7B,0x7C,0x7D,0x7E,0x7F,0x80,0x81,0x82,0x83,0x84,0x85,0x86,0x87,
				  0x90,0x91,0x92,0x93,0x94,0x95,0x96,0xA0,0xA1,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xAB,0xAC,0xAD,0xAE,0xAF,0xB0,
				  0xB1,0xB2,0xB3,0xB4,0xB5,0xB6,0xB7,0xBA,0xBB,0xBC,0xBD,0xBE,0xBF,0xC0,0xDB,0xDC,0xDD,0xDE,0xDF,0xE1,0xE2,0xE3,0xE4,0xE5,
				  0xE6,0xE7,0xE9,0xEA,0xEB,0xEC,0xED,0xEE,0xEF,0xF0,0xF1,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,0xFA,0xFB,0xFC,0xFD,0xFE};
string chars[192]={"leftmouse","rightmouse","cancel","middlemouse","x1mouse","x2mouse","backspace","tab","clear","enter","shift","ctrl",
				  "alt","pause","capslock","kana","junja","final","hanja","escape","IMEconvert","IMEnonconvert","IMEaccept","IMEmodechange",
				  "spacebar","pageup","pagedown","end","home","left","up","right","down","select","print","execute","printscreen","insert",
				  "delete","help","0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p",
				  "q","r","s","t","u","v","w","x","y","z","leftwindowskey","rightwindowskey","applicationskey","sleepkey","numpad0","numpad1",
				  "numpad2","numpad3","numpad4","numpad5","numpad6","numpad7","numpad8","numpad9","numpad*","numpad+","seperator","numpad-",
				  "numpad.","numpad/","f1","f2","f3","f4","f5","f6","f7","f8","f9","f10","f11","f12","f13","f14","f15","f16","f17","f18","f19",
				  "f20","f21","f22","f23","f24","numlock","scrolllock","OEM","OEM","OEM","OEM","OEM","leftshift","rightshift","leftctrl",
				  "rightctrl","leftmenu","rightmenu","browserbackkey","browserforwardkey","browserrefreshkey","browserstopkey","browsersearchkey",
				  "browserfavoriteskey","browserhomekey","mute","volumedown","volumeup","nexttrack","previoustrack","stop","play","startmailkey",
				  "selectmediakey","startappkey1","startappkey2",";","=",",","-",".","/","`","[","\\","]","\'","OEM","OEM","processkey",
				  "OEM","packet","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","OEM","attn","crsel","exsel","eof","Play",
				  "zoom","noname","pa1","Clear"};
SHORT state[192];
SHORT oldstate[192];
clock_t newclock=clock();
clock_t oldclock=clock();
bool flag=false;

void getstate();
int stringtoint(string);
char* searchforkey();
bool ispressedchanged(int);
bool ispressedorheld(int, int);
bool ispressed(int);
bool handlekeys(string, int, int);
bool handlekeys(int, int, int);

void getstate()
{
	for(int j=0;j<192;j++)
			oldstate[j]=state[j];
	for(int j=0;j<192;j++)
		state[j]=GetKeyState(keyset[j]);
}

int stringtoint(string key)
{
	int i=0;

	while(i<192)
	{
		if(key==chars[i])
			return i;

		i++;
	}

	return 0;
}

char* searchforkey()
{
	int i=0;

	while(i<192)
	{
		if(ispressed(i))
		{
			char* toreturn=(char*)chars[i].c_str();
			return toreturn;
		}

		i++;
	}
	return "fail";
}

//type 1: if pressed but not held 2: if pressed or held 3: if pressed 4: call searchforkey() to get current key pressed
bool handlekeys(string key, int type=1, int delay=200)
{	
	newclock=clock();

	int keyval=stringtoint(key);

	if(keyval<=192)
	{
		switch(type)
		{
		case 1:
			return ispressedchanged(keyval);
			break;
		case 2:
			return ispressedorheld(keyval,delay);
			break;
		case 3:
			return ispressed(keyval);
			break;
		default:
			return false;
			break;
		}
	}
	return false;
}

bool handlekeys(int key, int type=1, int delay=200)
{	
	newclock=clock();

	if(key<=192)
	{
		switch(type)
		{
		case 1:
			return ispressedchanged(key);
			break;
		case 2:
			return ispressedorheld(key,delay);
			break;
		case 3:
			return ispressed(key);
			break;
		default:
			return false;
			break;
		}
	}
	return false;
}

bool ispressedchanged(int key)
{
	if((state[key]<0&&state[key]!=oldstate[key]))
	{
		oldclock=newclock;
		return true;
	}
	return false;
}

bool ispressedorheld(int key, int delay)
{
	if(ispressedchanged(key))
		return true;

	if(flag==false)
	{
		if((newclock-oldclock)>delay&&state[key]<0)
		{
			oldclock=newclock;
			flag=true;
			return true;
		}
	}
	else if(flag==true)
	{
		if(state[key]<0)
		{
			oldclock=newclock;
			return true;
		}
		else
			flag=false;
	}

	return false;
}

bool ispressed(int key)
{
	if(state[key]<0)
	{
		oldclock=newclock;
		return true;
	}
	return false;
}

#endif