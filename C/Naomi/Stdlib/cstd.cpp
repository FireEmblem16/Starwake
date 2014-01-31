// cstd.cpp
// Defines a lot of data to start the c runtime data.

#ifndef CSTD_CPP
#define CSTD_CPP

#ifndef _MSC_VER
#error "MOS2 Kernel C++ Runtime requires Microsoft Visual C++ 2005 or later."
#endif

#pragma warning(disable:4702)

#include <stdint.h>
#include <mmngr_phys.h>

typedef void (__cdecl* _PVFV)(void);

#pragma data_seg(".CRT$XCA")
_PVFV __xc_a[] = { 0 };

#pragma data_seg(".CRT$XCZ")
_PVFV __xc_z[] = { 0 };

#pragma data_seg()

#pragma comment(linker,"/merge:.CRT=.data")

static _PVFV* pf_atexitlist = 0;

static unsigned max_atexitlist_entries = 32;

static unsigned cur_atexitlist_entries = 0;

void __cdecl _initterm(_PVFV* pfbegin, _PVFV* pfend)
{
    while(pfbegin < pfend)
    {
      if(*pfbegin != 0)
            (**pfbegin)();

        ++pfbegin;
    }
}

void __cdecl _atexit_init(void)
{
    max_atexitlist_entries = 32;
	pf_atexitlist = (_PVFV*)0x50000;
}

int __cdecl atexit(_PVFV fn)
{
	if (cur_atexitlist_entries >= max_atexitlist_entries)
		return 1;
	else
	{
		*(pf_atexitlist++) = fn;
		cur_atexitlist_entries++;
	}

	return 0;
}

void _cdecl Exit()
{
	while(cur_atexitlist_entries--)
	{
		(*(--pf_atexitlist))();
	}
}

void _cdecl InitializeConstructors()
{
	_atexit_init();
	_initterm(__xc_a,__xc_z); 
}

int __cdecl _purecall_handler()
{
	for (;;);

	return 0;
}

#pragma warning (disable:4100)

extern "C"
{
	float __declspec(naked) _CIcos()
	{
	   _asm
	   {
		  fcos
		  ret
	   };
	};

	float __declspec(naked) _CIsin()
	{
	   _asm
	   {
		  fsin
		  ret
	   };
	};

	float __declspec(naked) _CIsqrt()
	{
	   _asm
	   {
		  fsqrt
		  ret
	   };
	};

	long __declspec (naked) _ftol2_sse()
	{
		int a;
		_asm
		{
			fistp [a]
			mov	ebx,a
			ret
		}
	}

	uint64_t _declspec(naked) _aullshr()
	{
		_asm
		{
			cmp cl,64
			jae invalid

			// Handle shifts between 0 and 31 bits.
			cmp cl,32
			jae more32
			shrd eax,edx,cl
			shr edx,cl
			ret

			// Handle shifts of 32-63 bits.
		more32:
			mov eax,edx
			xor edx,edx
			and cl,31
			shr eax,cl
			ret

			// Invalid number, return 0.
		invalid:
			xor eax,eax
			xor edx,edx
			ret
		}
	}

	int _fltused = 1;
};

void* __cdecl::operator new(uint32_t size)
{
    return pmalloc((size_t)size);
}

void* __cdecl operator new[](uint32_t size)
{
	return pmalloc((size_t)size);
}

void __cdecl::operator delete(void* p)
{
	

	return;
}

void __cdecl operator delete[](void* p)
{
	

	return;
}

#pragma warning (default:4100)

#endif