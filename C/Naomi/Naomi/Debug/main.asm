; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\The Science Guy\Documents\Visual Studio 2008\Projects\Naomi\Naomi\main.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	?test@@YAXXZ					; test
EXTRN	?DebugRuncmd@@YAXXZ:PROC			; DebugRuncmd
EXTRN	?DebugGotoXY@@YAXII@Z:PROC			; DebugGotoXY
EXTRN	?DebugSetColor@@YAII@Z:PROC			; DebugSetColor
EXTRN	?DebugClrScr@@YAXG@Z:PROC			; DebugClrScr
; Function compile flags: /Ogtpy
; File c:\users\the science guy\documents\visual studio 2008\projects\naomi\naomi\main.cpp
;	COMDAT ?test@@YAXXZ
_TEXT	SEGMENT
?test@@YAXXZ PROC					; test, COMDAT

; 24   : #ifdef _DEBUG
; 25   : 	DebugClrScr(33);

	push	33					; 00000021H
	call	?DebugClrScr@@YAXG@Z			; DebugClrScr

; 26   : 	DebugSetColor(33);

	push	33					; 00000021H
	call	?DebugSetColor@@YAII@Z			; DebugSetColor

; 27   : 	DebugGotoXY(0,0);

	push	0
	push	0
	call	?DebugGotoXY@@YAXII@Z			; DebugGotoXY
	add	esp, 16					; 00000010H

; 28   : 	
; 29   : 	DebugRuncmd();

	jmp	?DebugRuncmd@@YAXXZ			; DebugRuncmd
?test@@YAXXZ ENDP					; test
_TEXT	ENDS
PUBLIC	_main
; Function compile flags: /Ogtpy
;	COMDAT _main
_TEXT	SEGMENT
_main	PROC						; COMDAT

; 17   : 	test();

	push	33					; 00000021H
	call	?DebugClrScr@@YAXG@Z			; DebugClrScr
	push	33					; 00000021H
	call	?DebugSetColor@@YAII@Z			; DebugSetColor
	push	0
	push	0
	call	?DebugGotoXY@@YAXII@Z			; DebugGotoXY
	add	esp, 16					; 00000010H
	call	?DebugRuncmd@@YAXXZ			; DebugRuncmd

; 18   : 	
; 19   : 	return 1;

	mov	eax, 1

; 20   : }

	ret	0
_main	ENDP
_TEXT	ENDS
END
