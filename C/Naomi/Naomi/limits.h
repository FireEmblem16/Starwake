#ifndef LIMITS_H
#define LIMITS_H

#include <float.h>

#define	CHAR_BIT 8
#define	SCHAR_MIN (-128)
#define	SCHAR_MAX 127
#define	UCHAR_MAX 255

#define	MB_LEN_MAX 5

#define	CHAR_MIN SCHAR_MIN
#define	CHAR_MAX SCHAR_MAX

#define	SHRT_MIN (-32768)
#define	SHRT_MAX 32767
#define	USHRT_MAX 65535
#define	INT_MIN (-2147483647-1)
#define	INT_MAX 2147483647
#define	UINT_MAX 4294967295U
#define	LONG_MIN (-2147483647L - 1L)
#define	LONG_MAX 2147483647L
#define	ULONG_MAX 4294967295UL

#if defined(__EXTENSIONS__) || __STDC__ - 0 == 0 || defined(_POSIX_C_SOURCE) || defined(_XOPEN_SOURCE)

#define	SSIZE_MAX INT_MAX

#define	ARG_MAX 1048320

#define	LINK_MAX 32767

#ifndef MAX_CANON
#define	MAX_CANON 256
#endif

#ifndef MAX_INPUT
#define	MAX_INPUT 512
#endif

#define	NGROUPS_MAX 16

#ifndef PATH_MAX
#define	PATH_MAX 1024
#endif

#define	PIPE_BUF 5120

#ifndef TMP_MAX
#define	TMP_MAX 17576
#endif

#define	_POSIX_AIO_LISTIO_MAX 2
#define	_POSIX_AIO_MAX 1
#define	_POSIX_ARG_MAX 4096
#define	_POSIX_CHILD_MAX 6
#define	_POSIX_DELAYTIMER_MAX 32
#define	_POSIX_LINK_MAX 8
#define	_POSIX_MAX_CANON 255
#define	_POSIX_MAX_INPUT 255
#define	_POSIX_MQ_OPEN_MAX 8
#define	_POSIX_MQ_PRIO_MAX 32
#define	_POSIX_NAME_MAX 14
#define	_POSIX_NGROUPS_MAX 0
#define	_POSIX_OPEN_MAX 16
#define	_POSIX_PATH_MAX 255
#define	_POSIX_PIPE_BUF 512
#define	_POSIX_RTSIG_MAX 8
#define	_POSIX_SEM_NSEMS_MAX 256
#define	_POSIX_SEM_VALUE_MAX 32767
#define	_POSIX_SIGQUEUE_MAX 32
#define	_POSIX_SSIZE_MAX 32767
#define	_POSIX_STREAM_MAX 8
#define	_POSIX_TIMER_MAX 32
#define	_POSIX_TZNAME_MAX 3

#define	_POSIX_LOGIN_NAME_MAX 9
#define	_POSIX_THREAD_DESTRUCTOR_INTERATION 4
#define	_POSIX_THREAD_KEYS_MAX 128
#define	_POSIX_THREAD_THREADS_MAX 64
#define	_POSIX_TTY_NAME_MAX 9

#define	_POSIX2_BC_BASE_MAX 99
#define	_POSIX2_BC_DIM_MAX 2048
#define	_POSIX2_BC_SCALE_MAX 99
#define	_POSIX2_BC_STRING_MAX 1000
#define	_POSIX2_COLL_WEIGHTS_MAX 2
#define	_POSIX2_EXPR_NEST_MAX 32
#define	_POSIX2_LINE_MAX 2048
#define	_POSIX2_RE_DUP_MAX 255

#define	BC_BASE_MAX _POSIX2_BC_BASE_MAX
#define	BC_DIM_MAX _POSIX2_BC_DIM_MAX
#define	BC_SCALE_MAX _POSIX2_BC_SCALE_MAX
#define	BC_STRING_MAX _POSIX2_BC_STRING_MAX
#define	COLL_WEIGHTS_MAX _POSIX2_COLL_WEIGHTS_MAX
#define	EXPR_NEST_MAX _POSIX2_EXPR_NEST_MAX
#define	LINE_MAX _POSIX2_LINE_MAX
#define	RE_DUP_MAX _POSIX2_RE_DUP_MAX

#endif

#if defined(__EXTENSIONS__) || (__STDC__ - 0 == 0 && !defined(_POSIX_C_SOURCE)) || defined(_XOPEN_SOURCE)

#define	PASS_MAX 8

#define	CHARCLASS_NAME_MAX 14
#define	NL_ARGMAX 9

#define	NL_LANGMAX 14
#define	NL_MSGMAX 32767
#define	NL_NMAX 1
#define	NL_SETMAX 255
#define	NL_TEXTMAX 2048
#define	NZERO 20

#define	WORD_BIT 32
#define	LONG_BIT 32

#define DBL_DIG 15
//#define DBL_MAX 1.7976931348623157E+308
//#define DBL_MIN 2.2250738585072014E-308

#define	FLT_DIG 6
//#define FLT_MAX 3.402823466E+38F
//#define FLT_MIN 1.175494351E-38F

#endif

#if defined(__EXTENSIONS__) || (__STDC__ - 0 == 0 && !defined(_POSIX_C_SOURCE) && !defined(_XOPEN_SOURCE))

#define	FCHR_MAX 1048576
#define	PID_MAX 30000

#define	CHILD_MAX 25
#ifndef OPEN_MAX
#define	OPEN_MAX 64
#endif

#define	PIPE_MAX 5120

#define	STD_BLK 1024
#define	UID_MAX 60002
#define	USI_MAX 4294967295
#define	SYSPID_MAX 1

#ifndef SYS_NMLN
#define	SYS_NMLN
#endif

#ifndef CLK_TCK
extern long _sysconf(int);
#define	CLK_TCK	_sysconf(3)
#endif

#define	LOGNAME_MAX	8
#define	TTYNAME_MAX	128

#define	LLONG_MIN (-9223372036854775807LL - 1LL)
#define	LLONG_MAX 9223372036854775807LL
#define	ULLONG_MAX 18446744073709551615ULL

#endif

#if	defined(__EXTENSIONS__) || (_POSIX_C_SOURCE >= 199506L)
#include <sys/unistd.h>
extern long _sysconf(int);
#define	PTHREAD_STACK_MIN _sysconf(_SC_THREAD_STACK_MIN)
#endif

#endif