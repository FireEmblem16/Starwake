#ifndef KYBRD_CPP
#define KYBRD_CPP

#pragma warning(disable:4305)
#pragma warning(disable:4309)

#include <kybrd.h>

#pragma warning(disable:4127)

enum KYBRD_ENCODER_IO
{
	KYBRD_ENC_INPUT_BUF = 0x60,
	KYBRD_ENC_CMD_REG = 0x60
};

enum KYBRD_ENC_CMDS
{
	KYBRD_ENC_CMD_SET_LED = 0xED,
	KYBRD_ENC_CMD_ECHO = 0xEE,
	KYBRD_ENC_CMD_SCAN_CODE_SET = 0xF0,
	KYBRD_ENC_CMD_ID = 0xF2,
	KYBRD_ENC_CMD_AUTODELAY = 0xF3,
	KYBRD_ENC_CMD_ENABLE = 0xF4,
	KYBRD_ENC_CMD_RESETWAIT = 0xF5,
	KYBRD_ENC_CMD_RESETSCAN = 0xF6,
	KYBRD_ENC_CMD_ALL_AUTO = 0xF7,
	KYBRD_ENC_CMD_ALL_MAKEBREAK = 0xF8,
	KYBRD_ENC_CMD_ALL_MAKEONLY = 0xF9,
	KYBRD_ENC_CMD_ALL_MAKEBREAK_AUTO = 0xFA,
	KYBRD_ENC_CMD_SINGLE_AUTOREPEAT = 0xFB,
	KYBRD_ENC_CMD_SINGLE_MAKEBREAK = 0xFC,
	KYBRD_ENC_CMD_SINGLE_BREAKONLY = 0xFD,
	KYBRD_ENC_CMD_RESEND = 0xFE,
	KYBRD_ENC_CMD_RESET = 0xFF,
};

enum KYBRD_CTRL_IO
{
	KYBRD_CTRL_STATS_REG = 0x64,
	KYBRD_CTRL_CMD_REG = 0x64
};

enum KYBRD_CTRL_STATS_MASK
{
	KYBRD_CTRL_STATS_MASK_OUT_BUF = 0x1,
	KYBRD_CTRL_STATS_MASK_IN_BUF = 0x2,
	KYBRD_CTRL_STATS_MASK_SYSTEM = 0x4,
	KYBRD_CTRL_STATS_MASK_CMD_DATA = 0x8,
	KYBRD_CTRL_STATS_MASK_LOCKED = 0x10,
	KYBRD_CTRL_STATS_MASK_AUX_BUF = 0x20,
	KYBRD_CTRL_STATS_MASK_TIMEOUT = 0x40,
	KYBRD_CTRL_STATS_MASK_PARITY = 0x80
};

enum KYBRD_CTRL_CMDS
{
	KYBRD_CTRL_CMD_READ = 0x20,
	KYBRD_CTRL_CMD_WRITE = 0x60,
	KYBRD_CTRL_CMD_SELF_TEST = 0xAA,
	KYBRD_CTRL_CMD_INTERFACE_TEST = 0xAB,
	KYBRD_CTRL_CMD_DISABLE = 0xAD,
	KYBRD_CTRL_CMD_ENABLE = 0xAE,
	KYBRD_CTRL_CMD_READ_IN_PORT = 0xC0,
	KYBRD_CTRL_CMD_READ_OUT_PORT = 0xD0,
	KYBRD_CTRL_CMD_WRITE_OUT_PORT = 0xD1,
	KYBRD_CTRL_CMD_READ_TEST_INPUTS = 0xE0,
	KYBRD_CTRL_CMD_SYSTEM_RESET = 0xFE,
	KYBRD_CTRL_CMD_MOUSE_DISABLE = 0xA7,
	KYBRD_CTRL_CMD_MOUSE_ENABLE = 0xA8,
	KYBRD_CTRL_CMD_MOUSE_PORT_TEST = 0xA9,
	KYBRD_CTRL_CMD_MOUSE_WRITE = 0xD4
};

enum KYBRD_ERROR
{
	KYBRD_ERR_BUF_OVERRUN = 0,
	KYBRD_ERR_ID_RET = 0x83AB,
	KYBRD_ERR_BAT = 0xAA,
	KYBRD_ERR_ECHO_RET = 0xEE,
	KYBRD_ERR_ACK = 0xFA,
	KYBRD_ERR_BAT_FAILED = 0xFC,
	KYBRD_ERR_DIAG_FAILED = 0xFD,
	KYBRD_ERR_RESEND_CMD = 0xFE,
	KYBRD_ERR_KEY = 0xFF
};

enum SCAN_CODES
{
	KYBRD_CODE_ORG_XT = 0x1,
	KYBRD_CODE_2 = 0x2,
	KYBRD_CODE_3 = 0x4
};

short _scancode[256];
short _extended_scancode[256];

volatile unsigned char _current_scancode = 0;
volatile unsigned char _latest_scancode = 0;

static bool _numlock, _scrolllock, _capslock;
static bool _shift, _alt, _ctrl;

static int _kybrd_error = 0;
static bool _kybrd_bat_res = false;
static bool _kybrd_diag_res = false;
static bool _kybrd_resend_res = false;
static bool _kybrd_disable = false;

static int _kybrd_scancode_org_xt[] =
{
	KEY_UNKNOWN,		//00
	KEY_ESCAPE,			//01
	KEY_1,				//02
	KEY_2,				//03
	KEY_3,				//04
	KEY_4,				//05
	KEY_5,				//06
	KEY_6,				//07
	KEY_7,				//08
	KEY_8,				//09
	KEY_9,				//0a
	KEY_0,				//0b
	KEY_MINUS,			//0c
	KEY_EQUAL,			//0d
	KEY_BACKSPACE,		//0e
	KEY_TAB,			//0f
	KEY_Q,				//10
	KEY_W,				//11
	KEY_E,				//12
	KEY_R,				//13
	KEY_T,				//14
	KEY_Y,				//15
	KEY_U,				//16
	KEY_I,				//17
	KEY_O,				//18
	KEY_P,				//19
	KEY_LEFTBRACKET,	//1a
	KEY_RIGHTBRACKET,	//1b
	KEY_RETURN,			//1c
	KEY_LCTRL,			//1d
	KEY_A,				//1e
	KEY_S,				//1f
	KEY_D,				//20
	KEY_F,				//21
	KEY_G,				//22
	KEY_H,				//23
	KEY_J,				//24
	KEY_K,				//25
	KEY_L,				//26
	KEY_SEMICOLON,		//27
	KEY_QUOTE,			//28
	KEY_GRAVE,			//29
	KEY_LSHIFT,			//2a
	KEY_BACKSLASH,		//2b
	KEY_Z,				//2c
	KEY_X,				//2d
	KEY_C,				//2e
	KEY_V,				//2f
	KEY_B,				//30
	KEY_N,				//31
	KEY_M,				//32
	KEY_COMMA,			//33
	KEY_DOT,			//34
	KEY_SLASH,			//35
	KEY_RSHIFT,			//36
	KEY_KP_ASTERISK,	//37
	KEY_RALT,			//38
	KEY_SPACE,			//39
	KEY_CAPSLOCK,		//3a
	KEY_F1,				//3b
	KEY_F2,				//3c
	KEY_F3,				//3d
	KEY_F4,				//3e
	KEY_F5,				//3f
	KEY_F6,				//40
	KEY_F7,				//41
	KEY_F8,				//42
	KEY_F9,				//43
	KEY_F10,			//44
	KEY_KP_NUMLOCK,		//45
	KEY_SCROLLLOCK,		//46
	KEY_KP_7,			//47
	KEY_KP_8,			//48
	KEY_KP_9,			//49
	KEY_KP_MINUS,		//4a
	KEY_KP_4,			//4b
	KEY_KP_5,			//4c
	KEY_KP_6,			//4d
	KEY_KP_PLUS,		//4e
	KEY_KP_1,			//4f
	KEY_KP_2,			//50
	KEY_KP_3,			//51
	KEY_KP_0,			//52
	KEY_KP_DECIMAL,		//53
	KEY_UNKNOWN,		//54
	KEY_UNKNOWN,		//55
	KEY_UNKNOWN,		//56
	KEY_F11,			//57
	KEY_F12,			//58
	KEY_UNKNOWN,		//59
	KEY_UNKNOWN,		//5a
	KEY_UNKNOWN,		//5b
	KEY_UNKNOWN,		//5c
	KEY_UNKNOWN,		//5d
	KEY_UNKNOWN,		//5e
	KEY_UNKNOWN,		//5f
	KEY_UNKNOWN,		//60
	KEY_UNKNOWN,		//61
	KEY_UNKNOWN,		//62
	KEY_UNKNOWN,		//63
	KEY_UNKNOWN,		//64
	KEY_UNKNOWN,		//65
	KEY_UNKNOWN,		//66
	KEY_UNKNOWN,		//67
	KEY_UNKNOWN,		//68
	KEY_UNKNOWN,		//69
	KEY_UNKNOWN,		//6a
	KEY_UNKNOWN,		//6b
	KEY_UNKNOWN,		//6c
	KEY_UNKNOWN			//6d
};

static int _kybrd_scancode_org_xt_ext[] =
{
	KEY_UNKNOWN,		//00
	KEY_UNKNOWN,		//01
	KEY_UNKNOWN,		//02
	KEY_UNKNOWN,		//03
	KEY_UNKNOWN,		//04
	KEY_UNKNOWN,		//05
	KEY_UNKNOWN,		//06
	KEY_UNKNOWN,		//07
	KEY_UNKNOWN,		//08
	KEY_UNKNOWN,		//09
	KEY_UNKNOWN,		//0a
	KEY_UNKNOWN,		//0b
	KEY_UNKNOWN,		//0c
	KEY_UNKNOWN,		//0d
	KEY_UNKNOWN,		//0e
	KEY_UNKNOWN,		//0f
	KEY_PREVIOUS_TRACK,	//10
	KEY_UNKNOWN,		//11
	KEY_UNKNOWN,		//12
	KEY_UNKNOWN,		//13
	KEY_UNKNOWN,		//14
	KEY_UNKNOWN,		//15
	KEY_UNKNOWN,		//16
	KEY_UNKNOWN,		//17
	KEY_UNKNOWN,		//18
	KEY_NEXT_TRACK,		//19
	KEY_UNKNOWN,		//1a
	KEY_UNKNOWN,		//1b
	KEY_KP_ENTER,		//1c
	KEY_RCTRL,			//1d
	KEY_UNKNOWN,		//1e
	KEY_UNKNOWN,		//1f
	KEY_MUTE,			//20
	KEY_CALC,			//21
	KEY_PLAY_PAUSE,		//22
	KEY_UNKNOWN,		//23
	KEY_STOP,			//24
	KEY_UNKNOWN,		//25
	KEY_UNKNOWN,		//26
	KEY_UNKNOWN,		//27
	KEY_UNKNOWN,		//28
	KEY_UNKNOWN,		//29
	KEY_PRINT_SCRN,		//2a
	KEY_UNKNOWN,		//2b
	KEY_UNKNOWN,		//2c
	KEY_UNKNOWN,		//2d
	KEY_VOLUME_DOWN,	//2e
	KEY_UNKNOWN,		//2f
	KEY_VOLUME_UP,		//30
	KEY_UNKNOWN,		//31
	KEY_WWW_HOME,		//32
	KEY_UNKNOWN,		//33
	KEY_UNKNOWN,		//34
	KEY_KP_DIVIDE,		//35
	KEY_UNKNOWN,		//36
	KEY_UNKNOWN,		//37 (Print Screen second make code)
	KEY_UNKNOWN,		//38
	KEY_UNKNOWN,		//39
	KEY_UNKNOWN,		//3a
	KEY_UNKNOWN,		//3b
	KEY_UNKNOWN,		//3c
	KEY_RALT,			//3d
	KEY_UNKNOWN,		//3e
	KEY_UNKNOWN,		//3f
	KEY_UNKNOWN,		//40
	KEY_UNKNOWN,		//41
	KEY_UNKNOWN,		//42
	KEY_UNKNOWN,		//43
	KEY_UNKNOWN,		//44
	KEY_PAUSE,			//45
	KEY_UNKNOWN,		//46
	KEY_HOME,			//47
	KEY_UP,				//48
	KEY_PAGEUP,			//49
	KEY_UNKNOWN,		//4a
	KEY_LEFT,			//4b
	KEY_UNKNOWN,		//4c
	KEY_RIGHT,			//4d
	KEY_UNKNOWN,		//4e
	KEY_END,			//4f
	KEY_DOWN,			//50
	KEY_PAGEDOWN,		//51
	KEY_INSERT,			//52
	KEY_DELETE,			//53
	KEY_UNKNOWN,		//54
	KEY_UNKNOWN,		//55
	KEY_UNKNOWN,		//56
	KEY_UNKNOWN,		//57
	KEY_UNKNOWN,		//58
	KEY_UNKNOWN,		//59
	KEY_UNKNOWN,		//5a
	KEY_LWIN,			//5b
	KEY_RWIN,			//5c
	KEY_APPS,			//5d
	KEY_POWER,			//5e
	KEY_SLEEP,			//5f
	KEY_UNKNOWN,		//60
	KEY_UNKNOWN,		//61
	KEY_UNKNOWN,		//62
	KEY_WAKE,			//63
	KEY_UNKNOWN,		//64
	KEY_WWW_SEARCH,		//65
	KEY_WWW_FAVORITES,	//66
	KEY_WWW_REFRESH,	//67
	KEY_WWW_STOP,		//68
	KEY_WWW_FORWARD,	//69
	KEY_WWW_BACK,		//6a
	KEY_MY_COMP,		//6b
	KEY_EMAIL,			//6c
	KEY_MEDIA_SELECT	//6d
};

static int* _kybrd_makecode_org_xt = _kybrd_scancode_org_xt;
static int* _kybrd_makecode_org_xt_ext = _kybrd_scancode_org_xt_ext;

static bool _extended = false;
static bool _xextended = false;
static bool _break = false;

const int INVALID_SCANCODE = 0;

uint8_t kybrd_ctrl_read_status();
void kybrd_ctrl_send_cmd(uint8_t);
uint8_t kybrd_enc_read_buf();
void kybrd_enc_send_cmd(uint8_t);
void _cdecl i86_kybrd_irq();

uint8_t kybrd_ctrl_read_status()
{
	return inportb(KYBRD_CTRL_STATS_REG);
}

void kybrd_ctrl_send_cmd(uint8_t cmd)
{
	while(true)
		if((kybrd_ctrl_read_status() & KYBRD_CTRL_STATS_MASK_IN_BUF) == 0)
			break;

	outportb(KYBRD_CTRL_CMD_REG,cmd);
}

uint8_t kybrd_enc_read_buf()
{
	return inportb(KYBRD_ENC_INPUT_BUF);
}

void kybrd_enc_send_cmd(uint8_t cmd)
{
	while(true)
		if((kybrd_ctrl_read_status() & KYBRD_CTRL_STATS_MASK_IN_BUF) == 0)
			break;

	outportb(KYBRD_ENC_CMD_REG,cmd);
}

void kybrd_add_scancode(short _code, bool _ext)
{
	if(_code > 0x6d)
		return;

	_scancode[_latest_scancode] = _code;
	_extended_scancode[_latest_scancode] = _ext;

	_latest_scancode++;

	if(_latest_scancode == _current_scancode)
		kybrd_discard_last_key();

	return;
}

void _cdecl i86_kybrd_irq()
{
	hardintstart();

	int code = 0;

	if(kybrd_ctrl_read_status() & KYBRD_CTRL_STATS_MASK_OUT_BUF)
	{
		code = kybrd_enc_read_buf();

		if(code == 0xE0 || _xextended)
		{
			_extended = true;
			_xextended = false;
		}
		else if(code == 0xE1)
		{
			_xextended = true;
		}
		else if(code > 0x6D)
		{
			_break = true;

			if(code & 0x80)
			{
				code -= 0x80;
				int key = NULL;

				if(_extended)
					key = _kybrd_scancode_org_xt_ext[code];
				else
					key = _kybrd_scancode_org_xt[code];

				switch(key)
				{
				case KEY_LCTRL:
				case KEY_RCTRL:
					_ctrl = false;
					break;
				case KEY_LSHIFT:
				case KEY_RSHIFT:
					_shift = false;
					break;
				case KEY_LALT:
				case KEY_RALT:
					_alt = false;
					break;
				}
			}

			_xextended = false;
			_extended = false;
			_break = false;
		}
		else 
		{
			kybrd_add_scancode(code,_extended);
			int key = NULL;

			if(_extended)
				key = _kybrd_scancode_org_xt_ext[code];
			else
			{
				key = _kybrd_scancode_org_xt[code];

				if(!_numlock)
					switch(_scancode[_latest_scancode - 1])
					{
					case 0x53:
					case 0x52:
					case 0x4F:
					case 0x50:
					case 0x51:
					case 0x4B:
					case 0x4C:
					case 0x4D:
					case 0x47:
					case 0x48:
					case 0x49:
						_extended_scancode[_latest_scancode - 1] = true;
						break;
					}
			}

			switch(key)
			{
			case KEY_LCTRL:
			case KEY_RCTRL:
				_ctrl = true;
				break;
			case KEY_LSHIFT:
			case KEY_RSHIFT:
				_shift = true;
				break;
			case KEY_LALT:
			case KEY_RALT:
				_alt = true;
				break;
			case KEY_CAPSLOCK:
				_capslock = _capslock ? false : true;
				kybrd_set_LEDs(_numlock,_capslock,_scrolllock);
				break;
			case KEY_KP_NUMLOCK:
				_numlock = _numlock ? false : true;
				kybrd_set_LEDs(_numlock,_capslock,_scrolllock);
				break;
			case KEY_SCROLLLOCK:
				_scrolllock = _scrolllock ? false : true;
				kybrd_set_LEDs(_numlock,_capslock,_scrolllock);
				break;
			}

			_extended = false;
			_break = false;
		}

		switch(code)
		{
		case KYBRD_ERR_BAT_FAILED:
			_kybrd_bat_res = false;
			break;
		case KYBRD_ERR_DIAG_FAILED:
			_kybrd_diag_res = false;
			break;
		case KYBRD_ERR_RESEND_CMD:
			_kybrd_resend_res = true;
			break;
		}
	}

	interruptdone(1);
	hardintret();
}

bool kybrd_get_scroll_lock()
{
	return _scrolllock;
}

bool kybrd_get_numlock()
{
	return _numlock;
}

bool kybrd_get_capslock()
{
	return _capslock;
}

bool kybrd_get_ctrl()
{
	return _ctrl;
}

bool kybrd_get_alt()
{
	return _alt;
}

bool kybrd_get_shift()
{
	return _shift;
}

void kybrd_ignore_resend()
{
	_kybrd_resend_res = false;
}

bool kybrd_check_resend()
{
	return _kybrd_resend_res;
}

bool kybrd_get_diagnostic_res()
{
	return _kybrd_diag_res;
}

bool kybrd_get_bat_res()
{
	return _kybrd_bat_res;
}

uint16_t kybrd_get_last_scan()
{
	return _scancode[_latest_scancode];
}

KEYCODE kybrd_get_last_key()
{
	if(_extended_scancode[_current_scancode])
		return (KEYCODE)_kybrd_scancode_org_xt_ext[_scancode[_current_scancode]];
	else
		return (KEYCODE)_kybrd_scancode_org_xt[_scancode[_current_scancode]];
}

void kybrd_discard_last_key()
{
	_current_scancode++;

	if(_current_scancode == _latest_scancode)
	{
		_current_scancode--;
		_scancode[_current_scancode] = KEY_UNKNOWN;
	}

	return;
}

void kybrd_set_LEDs(bool num, bool caps, bool scroll)
{
	uint8_t data = 0;

	data = scroll ? data | 1 : data & ~1;
	data = num ? data | 2 : data & ~2;
	data = caps ? data | 4 : data & ~4;

	kybrd_enc_send_cmd(KYBRD_ENC_CMD_SET_LED);
	kybrd_enc_send_cmd(data);

	return;
}

void kybrd_set_scancode_set(uint8_t set)
{
	if(set != KYBRD_CODE_ORG_XT || set != KYBRD_CODE_2 || set != KYBRD_CODE_3)
		return;

	kybrd_enc_send_cmd(KYBRD_ENC_CMD_SCAN_CODE_SET);
	kybrd_enc_send_cmd(set);

	return;
}

char kybrd_key_to_ascii(KEYCODE code)
{
	if(code > UCHAR_MAX)
	{
		int key = code;

		if(key == KEY_KP_ENTER)
			return '\n';

		return KEY_EXTENDED;
	}

	uint8_t key = code;

	if(isascii(key))
	{
		if(_shift || _capslock && !(_shift && _capslock))
			if(key >= 'a' && key <= 'z')
				key -=32;

		if(_shift)
		{
			if(key >= '0' && key <= '9')
			{
				switch(key)
				{
				case '0':
					key = KEY_RIGHTPARENTHESIS;
					break;
				case '1':
					key = KEY_EXCLAMATION;
					break;
				case '2':
					key = KEY_AT;
					break;
				case '3':
					key = KEY_HASH;
					break;
				case '4':
					key = KEY_DOLLAR;
					break;
				case '5':
					key = KEY_PERCENT;
					break;
				case '6':
					key = KEY_CARRET;
					break;
				case '7':
					key = KEY_AMPERSAND;
					break;
				case '8':
					key = KEY_ASTERISK;
					break;
				case '9':
					key = KEY_LEFTPARENTHESIS;
					break;
				}
			}
			else
			{
				switch(key)
				{
				case KEY_COMMA:
					key = KEY_LESS;
					break;
				case KEY_DOT:
					key = KEY_GREATER;
					break;
				case KEY_SLASH:
					key = KEY_QUESTION;
					break;
				case KEY_SEMICOLON:
					key = KEY_COLON;
					break;
				case KEY_QUOTE:
					key = KEY_QUOTEDOUBLE;
					break;
				case KEY_LEFTBRACKET:
					key = KEY_LEFTCURL;
					break;
				case KEY_RIGHTBRACKET:
					key = KEY_RIGHTCURL;
					break;
				case KEY_GRAVE:
					key = KEY_TILDE;
					break;
				case KEY_MINUS:
					key = KEY_UNDERSCORE;
					break;
				case KEY_EQUAL:
					key = KEY_PLUS;
					break;
				case KEY_BACKSLASH:
					key = KEY_BAR;
					break;
				}
			}
		}

		return key;
	}

	return 0;
}

void kybrd_disable()
{
	kybrd_ctrl_send_cmd(KYBRD_CTRL_CMD_DISABLE);
	_kybrd_disable = true;
}

void kybrd_enable()
{
	kybrd_ctrl_send_cmd(KYBRD_CTRL_CMD_ENABLE);
	_kybrd_disable = false;
}

bool kybrd_is_disabled()
{
	return _kybrd_disable;
}

void kybrd_reset_system()
{
	kybrd_ctrl_send_cmd(KYBRD_CTRL_CMD_WRITE_OUT_PORT);
	kybrd_enc_send_cmd(0xFE);
}

bool kybrd_self_test()
{
	kybrd_ctrl_send_cmd(KYBRD_CTRL_CMD_SELF_TEST);

	while(true)
		if(kybrd_ctrl_read_status() & KYBRD_CTRL_STATS_MASK_OUT_BUF)
			break;

	return (kybrd_enc_read_buf() == 0x55) ? true : false;
}

void kybrd_init(int irv)
{
	setvect(irv,i86_kybrd_irq);

	_kybrd_bat_res = true;

	_numlock = _scrolllock = _capslock = false;
	kybrd_set_scancode_set(KYBRD_CODE_ORG_XT);
	kybrd_set_LEDs(false,false,false);

	_shift = _alt = _ctrl = false;

	for(uint16_t i = 0;i < 256;i++)
	{
		_scancode[i] = 0;
		_extended_scancode[i] = false;
	}

	return;
}

#endif