; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\The Science Guy\Documents\Visual Studio 2008\Projects\Naomi\Naomi\keyboard.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	?getkey@@YA?AW4KEYCODE@@XZ			; getkey
EXTRN	?kybrd_discard_last_key@@YAXXZ:PROC		; kybrd_discard_last_key
EXTRN	?kybrd_get_last_key@@YA?AW4KEYCODE@@XZ:PROC	; kybrd_get_last_key
; Function compile flags: /Ogtpy
; File c:\users\the science guy\documents\visual studio 2008\projects\naomi\naomi\keyboard.cpp
;	COMDAT ?getkey@@YA?AW4KEYCODE@@XZ
_TEXT	SEGMENT
?getkey@@YA?AW4KEYCODE@@XZ PROC				; getkey, COMDAT

; 17   : {

	push	esi

; 18   : 	KEYCODE key = kybrd_get_last_key();

	call	?kybrd_get_last_key@@YA?AW4KEYCODE@@XZ	; kybrd_get_last_key
	mov	esi, eax

; 19   : 	kybrd_discard_last_key();

	call	?kybrd_discard_last_key@@YAXXZ		; kybrd_discard_last_key

; 20   : 	return key;

	mov	eax, esi
	pop	esi

; 21   : }

	ret	0
?getkey@@YA?AW4KEYCODE@@XZ ENDP				; getkey
_TEXT	ENDS
PUBLIC	?getchar@@YADXZ					; getchar
EXTRN	?kybrd_key_to_ascii@@YADW4KEYCODE@@@Z:PROC	; kybrd_key_to_ascii
; Function compile flags: /Ogtpy
;	COMDAT ?getchar@@YADXZ
_TEXT	SEGMENT
?getchar@@YADXZ PROC					; getchar, COMDAT

; 7    : {

	push	esi

; 8    : 	KEYCODE key = getkey();

	call	?kybrd_get_last_key@@YA?AW4KEYCODE@@XZ	; kybrd_get_last_key
	mov	esi, eax
	call	?kybrd_discard_last_key@@YAXXZ		; kybrd_discard_last_key

; 9    : 
; 10   : 	if(key == KEY_UNKNOWN)

	cmp	esi, 16425				; 00004029H
	jne	SHORT $LN1@getchar

; 11   : 		return 0;

	xor	al, al
	pop	esi

; 14   : }

	ret	0
$LN1@getchar:

; 12   : 
; 13   : 	return kybrd_key_to_ascii(key);

	push	esi
	call	?kybrd_key_to_ascii@@YADW4KEYCODE@@@Z	; kybrd_key_to_ascii
	add	esp, 4
	pop	esi

; 14   : }

	ret	0
?getchar@@YADXZ ENDP					; getchar
_TEXT	ENDS
END
