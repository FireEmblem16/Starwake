#ifndef KYBRD_H
#define KYBRD_H

#include <_null.h>
#include <stdint.h>
#include <ctype.h>
#include <limits.h>
#include <exceptions.h>
#include <hal.h>

enum KEYCODE
{
	KEY_SPACE = ' ',
	
	KEY_0 = '0',
	KEY_1 = '1',
	KEY_2 = '2',
	KEY_3 = '3',
	KEY_4 = '4',
	KEY_5 = '5',
	KEY_6 = '6',
	KEY_7 = '7',
	KEY_8 = '8',
	KEY_9 = '9',

	KEY_A = 'a',
	KEY_B = 'b',
	KEY_C = 'c',
	KEY_D = 'd',
	KEY_E = 'e',
	KEY_F = 'f',
	KEY_G = 'g',
	KEY_H = 'h',
	KEY_I = 'i',
	KEY_J = 'j',
	KEY_K = 'k',
	KEY_L = 'l',
	KEY_M = 'm',
	KEY_N = 'n',
	KEY_O = 'o',
	KEY_P = 'p',
	KEY_Q = 'q',
	KEY_R = 'r',
	KEY_S = 's',
	KEY_T = 't',
	KEY_U = 'u',
	KEY_V = 'v',
	KEY_W = 'w',
	KEY_X = 'x',
	KEY_Y = 'y',
	KEY_Z = 'z',
	
	KEY_RETURN = '\r',
	KEY_ESCAPE = 0x1001,
	KEY_BACKSPACE = '\b',

	KEY_UP = 0x1100,
	KEY_DOWN = 0x1101,
	KEY_LEFT = 0x1102,
	KEY_RIGHT = 0x1103,

	KEY_F1 = 0x1201,
	KEY_F2 = 0x1202,
	KEY_F3 = 0x1203,
	KEY_F4 = 0x1204,
	KEY_F5 = 0x1205,
	KEY_F6 = 0x1206,
	KEY_F7 = 0x1207,
	KEY_F8 = 0x1208,
	KEY_F9 = 0x1209,
	KEY_F10 = 0x120A,
	KEY_F11 = 0x120B,
	KEY_F12 = 0x120C,
	KEY_F13 = 0x120D,
	KEY_F14 = 0x120E,
	KEY_F15 = 0x120F,

	KEY_DOT = '.',
	KEY_COMMA = ',',
	KEY_COLON = ':',
	KEY_SEMICOLON = ';',
	KEY_SLASH = '/',
	KEY_BACKSLASH = '\\',
	KEY_PLUS = '+',
	KEY_MINUS = '-',
	KEY_ASTERISK = '*',
	KEY_EXCLAMATION = '!',
	KEY_QUESTION = '?',
	KEY_QUOTEDOUBLE = '\"',
	KEY_QUOTE = '\'',
	KEY_EQUAL = '=',
	KEY_HASH = '#',
	KEY_PERCENT = '%',
	KEY_AMPERSAND = '&',
	KEY_UNDERSCORE = '_',
	KEY_LEFTPARENTHESIS = '(',
	KEY_RIGHTPARENTHESIS = ')',
	KEY_LEFTBRACKET = '[',
	KEY_RIGHTBRACKET = ']',
	KEY_LEFTCURL = '{',
	KEY_RIGHTCURL = '}',
	KEY_DOLLAR = '$',
	KEY_POUND = '�',
	KEY_EURO = '$',
	KEY_LESS = '<',
	KEY_GREATER = '>',
	KEY_BAR = '|',
	KEY_GRAVE = '`',
	KEY_TILDE = '~',
	KEY_AT = '@',
	KEY_CARRET = '^',

	KEY_KP_0 = '0',
	KEY_KP_1 = '1',
	KEY_KP_2 = '2',
	KEY_KP_3 = '3',
	KEY_KP_4 = '4',
	KEY_KP_5 = '5',
	KEY_KP_6 = '6',
	KEY_KP_7 = '7',
	KEY_KP_8 = '8',
	KEY_KP_9 = '9',
	KEY_KP_PLUS = '+',
	KEY_KP_MINUS = '-',
	KEY_KP_DECIMAL = '.',
	KEY_KP_DIVIDE = '/',
	KEY_KP_ASTERISK = '*',
	KEY_KP_NUMLOCK = 0x300F,
	KEY_KP_ENTER = 0x3010,

	KEY_TAB = 0x4000,
	KEY_CAPSLOCK = 0x4001,

	KEY_LSHIFT = 0x4002,
	KEY_LCTRL = 0x4003,
	KEY_LALT = 0x4004,
	KEY_LWIN = 0x4005,
	KEY_RSHIFT = 0x4006,
	KEY_RCTRL = 0x4007,
	KEY_RALT = 0x4008,
	KEY_RWIN = 0x4009,

	KEY_INSERT = 0x400A,
	KEY_DELETE = 0x400B,
	KEY_HOME = 0x400C,
	KEY_END = 0x400D,
	KEY_PAGEUP = 0x400E,
	KEY_PAGEDOWN = 0x400F,
	KEY_SCROLLLOCK = 0x4010,
	KEY_PAUSE = 0x4011,

	KEY_PRINT_SCRN,
	KEY_APPS,
	
	KEY_POWER,
	KEY_SLEEP,
	KEY_WAKE,

	KEY_NEXT_TRACK,
	KEY_PREVIOUS_TRACK,
	KEY_STOP,
	KEY_PLAY_PAUSE,
	KEY_MUTE,
	KEY_VOLUME_UP,
	KEY_VOLUME_DOWN,
	KEY_MEDIA_SELECT,
	KEY_EMAIL,
	KEY_CALC,
	KEY_MY_COMP,
	KEY_WWW_SEARCH,
	KEY_WWW_HOME,
	KEY_WWW_BACK,
	KEY_WWW_FORWARD,
	KEY_WWW_STOP,
	KEY_WWW_REFRESH,
	KEY_WWW_FAVORITES,

	KEY_UNKNOWN,
	KEY_EXTENDED = 0,
	KEY_NUMKEYCODES
};

extern bool kybrd_get_scroll_lock();
extern bool kybrd_get_numlock();
extern bool kybrd_get_capslock();

extern bool kybrd_get_alt();
extern bool kybrd_get_ctrl();
extern bool kybrd_get_shift();

extern void kybrd_ignore_resend();
extern bool kybrd_check_resend();

extern bool kybrd_get_diagnostic_res();
extern bool kybrd_get_bat_res();
extern bool kybrd_self_test();

extern uint16_t kybrd_get_last_scan();
extern KEYCODE kybrd_get_last_key();
extern void kybrd_discard_last_key();

extern void kybrd_set_scancode_set(uint8_t set);
extern void kybrd_set_LEDs(bool num,bool caps,bool scroll);

extern char kybrd_key_to_ascii(KEYCODE code);

extern void kybrd_disable();
extern void kybrd_enable();
extern bool kybrd_is_enabled();

extern void kybrd_reset_system();

extern void kybrd_init(int irv);

#endif