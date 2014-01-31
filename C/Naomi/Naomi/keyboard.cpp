#ifndef KEYBOARD_CPP
#define KEYBOARD_CPP

#include <naomi.h>

char getchar()
{
	KEYCODE key = getkey();

	if(key == KEY_UNKNOWN)
		return 0;

	return kybrd_key_to_ascii(key);
}

KEYCODE getkey()
{
	KEYCODE key = kybrd_get_last_key();
	kybrd_discard_last_key();
	return key;
}

#endif