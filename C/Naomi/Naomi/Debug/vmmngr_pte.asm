; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\The Science Guy\Documents\Visual Studio 2008\Projects\Naomi\Naomi\vmmngr_pte.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	?pt_entry_add_attribute@@YAXPAII@Z		; pt_entry_add_attribute
; Function compile flags: /Ogtpy
; File c:\users\the science guy\documents\visual studio 2008\projects\naomi\naomi\vmmngr_pte.cpp
;	COMDAT ?pt_entry_add_attribute@@YAXPAII@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
_attribute$ = 12					; size = 4
?pt_entry_add_attribute@@YAXPAII@Z PROC			; pt_entry_add_attribute, COMDAT

; 13   : 	*e |= attribute;

	mov	eax, DWORD PTR _e$[esp-4]
	mov	ecx, DWORD PTR _attribute$[esp-4]
	or	DWORD PTR [eax], ecx

; 14   : }

	ret	0
?pt_entry_add_attribute@@YAXPAII@Z ENDP			; pt_entry_add_attribute
_TEXT	ENDS
PUBLIC	?pt_entry_del_attribute@@YAXPAII@Z		; pt_entry_del_attribute
; Function compile flags: /Ogtpy
;	COMDAT ?pt_entry_del_attribute@@YAXPAII@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
_attribute$ = 12					; size = 4
?pt_entry_del_attribute@@YAXPAII@Z PROC			; pt_entry_del_attribute, COMDAT

; 18   : 	*e &= ~attribute;

	mov	ecx, DWORD PTR _attribute$[esp-4]
	mov	eax, DWORD PTR _e$[esp-4]
	not	ecx
	and	DWORD PTR [eax], ecx

; 19   : }

	ret	0
?pt_entry_del_attribute@@YAXPAII@Z ENDP			; pt_entry_del_attribute
_TEXT	ENDS
PUBLIC	?pt_entry_set_frame@@YAXPAII@Z			; pt_entry_set_frame
; Function compile flags: /Ogtpy
;	COMDAT ?pt_entry_set_frame@@YAXPAII@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
_addr$ = 12						; size = 4
?pt_entry_set_frame@@YAXPAII@Z PROC			; pt_entry_set_frame, COMDAT

; 23   : 	*e = (*e & ~I86_PTE_FRAME) | addr;

	mov	eax, DWORD PTR _e$[esp-4]
	mov	ecx, DWORD PTR [eax]
	and	ecx, -2147479553			; 80000fffH
	or	ecx, DWORD PTR _addr$[esp-4]
	mov	DWORD PTR [eax], ecx

; 24   : }

	ret	0
?pt_entry_set_frame@@YAXPAII@Z ENDP			; pt_entry_set_frame
_TEXT	ENDS
PUBLIC	?pt_entry_is_present@@YA_NI@Z			; pt_entry_is_present
; Function compile flags: /Ogtpy
;	COMDAT ?pt_entry_is_present@@YA_NI@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
?pt_entry_is_present@@YA_NI@Z PROC			; pt_entry_is_present, COMDAT

; 28   : 	return e & I86_PTE_PRESENT;

	mov	eax, DWORD PTR _e$[esp-4]
	and	eax, 1

; 29   : }

	ret	0
?pt_entry_is_present@@YA_NI@Z ENDP			; pt_entry_is_present
_TEXT	ENDS
PUBLIC	?pt_entry_is_writable@@YA_NI@Z			; pt_entry_is_writable
; Function compile flags: /Ogtpy
;	COMDAT ?pt_entry_is_writable@@YA_NI@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
?pt_entry_is_writable@@YA_NI@Z PROC			; pt_entry_is_writable, COMDAT

; 33   : 	return e & I86_PTE_WRITABLE;

	mov	eax, DWORD PTR _e$[esp-4]
	shr	eax, 1
	and	al, 1

; 34   : }

	ret	0
?pt_entry_is_writable@@YA_NI@Z ENDP			; pt_entry_is_writable
_TEXT	ENDS
PUBLIC	?pt_entry_pfn@@YAII@Z				; pt_entry_pfn
; Function compile flags: /Ogtpy
;	COMDAT ?pt_entry_pfn@@YAII@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
?pt_entry_pfn@@YAII@Z PROC				; pt_entry_pfn, COMDAT

; 38   : 	return e & I86_PTE_FRAME;

	mov	eax, DWORD PTR _e$[esp-4]
	and	eax, 2147479552				; 7ffff000H

; 39   : }

	ret	0
?pt_entry_pfn@@YAII@Z ENDP				; pt_entry_pfn
_TEXT	ENDS
END
