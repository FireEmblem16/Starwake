; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\FireEmblem16\Documents\Visual Studio 2008\Projects\Naomi\Naomi\cstd.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	?__xc_a@@3PAP6AXXZA				; __xc_a
PUBLIC	?__xc_z@@3PAP6AXXZA				; __xc_z
PUBLIC	__fltused
_BSS	SEGMENT
_pf_atexitlist DD 01H DUP (?)
_cur_atexitlist_entries DD 01H DUP (?)
_BSS	ENDS
_DATA	SEGMENT
_max_atexitlist_entries DD 020H
__fltused DD	01H
_DATA	ENDS
CRT$XCZ	SEGMENT
?__xc_z@@3PAP6AXXZA DD 00H				; __xc_z
CRT$XCZ	ENDS
CRT$XCA	SEGMENT
?__xc_a@@3PAP6AXXZA DD 00H				; __xc_a
CRT$XCA	ENDS
PUBLIC	?_initterm@@YAXPAP6AXXZ0@Z			; _initterm
; Function compile flags: /Ogtpy
; File c:\users\fireemblem16\documents\visual studio 2008\projects\naomi\naomi\cstd.cpp
;	COMDAT ?_initterm@@YAXPAP6AXXZ0@Z
_TEXT	SEGMENT
_pfbegin$ = 8						; size = 4
_pfend$ = 12						; size = 4
?_initterm@@YAXPAP6AXXZ0@Z PROC				; _initterm, COMDAT

; 59   : {

	push	esi

; 60   : 	// Go through each initializer
; 61   :     while ( pfbegin < pfend )

	mov	esi, DWORD PTR _pfbegin$[esp]
	push	edi
	mov	edi, DWORD PTR _pfend$[esp+4]
	cmp	esi, edi
	jae	SHORT $LN2@initterm
	npad	2
$LL3@initterm:

; 62   :     {
; 63   : 	  // Execute the global initializer
; 64   :       if ( *pfbegin != 0 )

	mov	eax, DWORD PTR [esi]
	test	eax, eax
	je	SHORT $LN1@initterm

; 65   :             (**pfbegin) ();

	call	eax
$LN1@initterm:

; 66   : 
; 67   : 	    // Go to next initializer inside the initializer table
; 68   :         ++pfbegin;

	add	esi, 4
	cmp	esi, edi
	jb	SHORT $LL3@initterm
$LN2@initterm:
	pop	edi
	pop	esi

; 69   :     }
; 70   : }

	ret	0
?_initterm@@YAXPAP6AXXZ0@Z ENDP				; _initterm
_TEXT	ENDS
PUBLIC	?_atexit_init@@YAXXZ				; _atexit_init
; Function compile flags: /Ogtpy
;	COMDAT ?_atexit_init@@YAXXZ
_TEXT	SEGMENT
?_atexit_init@@YAXXZ PROC				; _atexit_init, COMDAT

; 79   :     max_atexitlist_entries = 32;

	mov	DWORD PTR _max_atexitlist_entries, 32	; 00000020H

; 80   : 
; 81   : 	// Warning: Normally, the STDC will dynamically allocate this. Because we have no memory manager, just choose
; 82   : 	// a base address that you will never use for now
; 83   :  //   pf_atexitlist = (_PVFV *)0x500000;
; 84   : 
; 85   : 	pf_atexitlist = (_PVFV *)0x5000;

	mov	DWORD PTR _pf_atexitlist, 20480		; 00005000H

; 86   : }

	ret	0
?_atexit_init@@YAXXZ ENDP				; _atexit_init
_TEXT	ENDS
PUBLIC	_atexit
; Function compile flags: /Ogtpy
;	COMDAT _atexit
_TEXT	SEGMENT
_fn$ = 8						; size = 4
_atexit	PROC						; COMDAT

; 95   : 	// Insure we have enough free space
; 96   : 	if (cur_atexitlist_entries>=max_atexitlist_entries)

	mov	eax, DWORD PTR _cur_atexitlist_entries
	cmp	eax, DWORD PTR _max_atexitlist_entries
	jb	SHORT $LN2@atexit

; 97   : 		return 1;

	mov	eax, 1

; 106  : }

	ret	0
$LN2@atexit:

; 98   : 	else {
; 99   : 
; 100  : 		// Add the exit routine
; 101  : 		*(pf_atexitlist++) = fn;

	mov	eax, DWORD PTR _pf_atexitlist
	mov	ecx, DWORD PTR _fn$[esp-4]

; 102  : 		cur_atexitlist_entries++;

	inc	DWORD PTR _cur_atexitlist_entries
	mov	DWORD PTR [eax], ecx
	add	eax, 4
	mov	DWORD PTR _pf_atexitlist, eax

; 103  : 	}
; 104  : 
; 105  : 	return 0;

	xor	eax, eax

; 106  : }

	ret	0
_atexit	ENDP
_TEXT	ENDS
PUBLIC	?Exit@@YAXXZ					; Exit
; Function compile flags: /Ogtpy
;	COMDAT ?Exit@@YAXXZ
_TEXT	SEGMENT
?Exit@@YAXXZ PROC					; Exit, COMDAT

; 114  : 
; 115  : 	// Go through the list, and execute all global exit routines
; 116  : 	while (cur_atexitlist_entries--) {

	cmp	DWORD PTR _cur_atexitlist_entries, 0
	je	SHORT $LN8@Exit
	npad	7
$LL2@Exit:

; 117  : 
; 118  : 			// execute function
; 119  : 			(*(--pf_atexitlist)) ();

	mov	eax, DWORD PTR _pf_atexitlist
	dec	DWORD PTR _cur_atexitlist_entries
	sub	eax, 4
	mov	DWORD PTR _pf_atexitlist, eax
	mov	eax, DWORD PTR [eax]
	call	eax
	cmp	DWORD PTR _cur_atexitlist_entries, 0
	jne	SHORT $LL2@Exit
$LN8@Exit:

; 114  : 
; 115  : 	// Go through the list, and execute all global exit routines
; 116  : 	while (cur_atexitlist_entries--) {

	dec	DWORD PTR _cur_atexitlist_entries

; 120  : 	}
; 121  : }

	ret	0
?Exit@@YAXXZ ENDP					; Exit
_TEXT	ENDS
PUBLIC	?InitializeConstructors@@YAXXZ			; InitializeConstructors
; Function compile flags: /Ogtpy
;	COMDAT ?InitializeConstructors@@YAXXZ
_TEXT	SEGMENT
?InitializeConstructors@@YAXXZ PROC			; InitializeConstructors, COMDAT

; 129  : {

	push	esi

; 130  :    _atexit_init();
; 131  :    _initterm(__xc_a, __xc_z); 

	mov	esi, OFFSET ?__xc_a@@3PAP6AXXZA		; __xc_a
	mov	eax, esi
	mov	DWORD PTR _max_atexitlist_entries, 32	; 00000020H
	mov	DWORD PTR _pf_atexitlist, 20480		; 00005000H
	cmp	eax, OFFSET ?__xc_z@@3PAP6AXXZA		; __xc_z
	jae	SHORT $LN6@Initialize
$LL7@Initialize:
	mov	eax, DWORD PTR [esi]
	test	eax, eax
	je	SHORT $LN5@Initialize
	call	eax
$LN5@Initialize:
	add	esi, 4
	cmp	esi, OFFSET ?__xc_z@@3PAP6AXXZA		; __xc_z
	jb	SHORT $LL7@Initialize
$LN6@Initialize:
	pop	esi

; 132  : }

	ret	0
?InitializeConstructors@@YAXXZ ENDP			; InitializeConstructors
_TEXT	ENDS
PUBLIC	?_purecall_handler@@YAHXZ			; _purecall_handler
; Function compile flags: /Ogtpy
;	COMDAT ?_purecall_handler@@YAHXZ
_TEXT	SEGMENT
?_purecall_handler@@YAHXZ PROC				; _purecall_handler, COMDAT

; 140  : {

$LL2@purecall_h:

; 141  : 	// for now, halt the system 'til we have a way to display error
; 142  : 	for (;;);

	jmp	SHORT $LL2@purecall_h
?_purecall_handler@@YAHXZ ENDP				; _purecall_handler
_TEXT	ENDS
PUBLIC	__CIcos
; Function compile flags: /Ogtpy
;	COMDAT __CIcos
_TEXT	SEGMENT
__CIcos	PROC						; COMDAT

; 155  :    _asm {
; 156  :       fcos

	fcos

; 157  :       ret

	ret	0
__CIcos	ENDP
_TEXT	ENDS
PUBLIC	__CIsin
; Function compile flags: /Ogtpy
;	COMDAT __CIsin
_TEXT	SEGMENT
__CIsin	PROC						; COMDAT

; 163  :    _asm {
; 164  :       fsin

	fsin

; 165  :       ret

	ret	0
__CIsin	ENDP
_TEXT	ENDS
PUBLIC	__CIsqrt
; Function compile flags: /Ogtpy
;	COMDAT __CIsqrt
_TEXT	SEGMENT
__CIsqrt PROC						; COMDAT

; 171  :    _asm {
; 172  :       fsqrt

	fsqrt

; 173  :       ret

	ret	0
__CIsqrt ENDP
_TEXT	ENDS
PUBLIC	__ftol2_sse
; Function compile flags: /Ogtpy
;	COMDAT __ftol2_sse
_TEXT	SEGMENT
_a$ = -4						; size = 4
__ftol2_sse PROC					; COMDAT

; 179  : 
; 180  : 	int a;
; 181  : 	_asm {
; 182  : 		fistp [a]

	fistp	DWORD PTR _a$[ebp]

; 183  : 		mov	ebx, a

	mov	ebx, DWORD PTR _a$[ebp]

; 184  : 		ret

	ret	0
__ftol2_sse ENDP
_TEXT	ENDS
PUBLIC	??2@YAPAXI@Z					; operator new
; Function compile flags: /Ogtpy
;	COMDAT ??2@YAPAXI@Z
_TEXT	SEGMENT
_size$ = 8						; size = 4
??2@YAPAXI@Z PROC					; operator new, COMDAT

; 202  : 	// Nothing we can do 'til we have a memory manager. Defined here so C++ doesnt mess up
; 203  :     return 0;

	xor	eax, eax

; 204  : }

	ret	0
??2@YAPAXI@Z ENDP					; operator new
_TEXT	ENDS
PUBLIC	??_U@YAPAXI@Z					; operator new[]
; Function compile flags: /Ogtpy
;	COMDAT ??_U@YAPAXI@Z
_TEXT	SEGMENT
_size$ = 8						; size = 4
??_U@YAPAXI@Z PROC					; operator new[], COMDAT

; 208  : 	// see above
; 209  : 	return 0;

	xor	eax, eax

; 210  : }

	ret	0
??_U@YAPAXI@Z ENDP					; operator new[]
_TEXT	ENDS
PUBLIC	??3@YAXPAX@Z					; operator delete
; Function compile flags: /Ogtpy
;	COMDAT ??3@YAXPAX@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
??3@YAXPAX@Z PROC					; operator delete, COMDAT

; 219  : 	// see above
; 220  : }

	ret	0
??3@YAXPAX@Z ENDP					; operator delete
_TEXT	ENDS
PUBLIC	??_V@YAXPAX@Z					; operator delete[]
; Function compile flags: /Ogtpy
;	COMDAT ??_V@YAXPAX@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
??_V@YAXPAX@Z PROC					; operator delete[], COMDAT

; 224  : 	// see above
; 225  : }

	ret	0
??_V@YAXPAX@Z ENDP					; operator delete[]
_TEXT	ENDS
END