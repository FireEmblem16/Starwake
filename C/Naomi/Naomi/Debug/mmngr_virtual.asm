; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\The Science Guy\Documents\Visual Studio 2008\Projects\Naomi\Naomi\mmngr_virtual.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	?_cur_directory@@3PAUpdirectory@@A		; _cur_directory
PUBLIC	?_cur_pdbr@@3IA					; _cur_pdbr
_BSS	SEGMENT
?_cur_directory@@3PAUpdirectory@@A DD 01H DUP (?)	; _cur_directory
?_cur_pdbr@@3IA DD 01H DUP (?)				; _cur_pdbr
_BSS	ENDS
PUBLIC	?vmmngr_ptable_virt_to_index@@YAII@Z		; vmmngr_ptable_virt_to_index
; Function compile flags: /Ogtpy
; File c:\users\the science guy\documents\visual studio 2008\projects\naomi\naomi\mmngr_virtual.cpp
;	COMDAT ?vmmngr_ptable_virt_to_index@@YAII@Z
_TEXT	SEGMENT
_addr$ = 8						; size = 4
?vmmngr_ptable_virt_to_index@@YAII@Z PROC		; vmmngr_ptable_virt_to_index, COMDAT

; 22   : 	return (addr >= PTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;

	mov	eax, DWORD PTR _addr$[esp-4]
	cmp	eax, 4194304				; 00400000H
	jb	SHORT $LN3@vmmngr_pta
	xor	eax, eax

; 23   : }

	ret	0
$LN3@vmmngr_pta:

; 22   : 	return (addr >= PTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;

	shr	eax, 12					; 0000000cH

; 23   : }

	ret	0
?vmmngr_ptable_virt_to_index@@YAII@Z ENDP		; vmmngr_ptable_virt_to_index
_TEXT	ENDS
PUBLIC	?vmmngr_ptable_lookup_entry@@YAPAIPAUptable@@I@Z ; vmmngr_ptable_lookup_entry
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_ptable_lookup_entry@@YAPAIPAUptable@@I@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
_addr$ = 12						; size = 4
?vmmngr_ptable_lookup_entry@@YAPAIPAUptable@@I@Z PROC	; vmmngr_ptable_lookup_entry, COMDAT

; 27   : 	if(p)

	mov	ecx, DWORD PTR _p$[esp-4]
	test	ecx, ecx
	je	SHORT $LN1@vmmngr_pta@2

; 28   : 		return &p->m_entries[vmmngr_ptable_virt_to_index(addr)];

	mov	eax, DWORD PTR _addr$[esp-4]
	cmp	eax, 4194304				; 00400000H
	jb	SHORT $LN6@vmmngr_pta@2
	xor	eax, eax
	lea	eax, DWORD PTR [ecx+eax*4]

; 31   : }

	ret	0

; 28   : 		return &p->m_entries[vmmngr_ptable_virt_to_index(addr)];

$LN6@vmmngr_pta@2:
	shr	eax, 12					; 0000000cH
	lea	eax, DWORD PTR [ecx+eax*4]

; 31   : }

	ret	0
$LN1@vmmngr_pta@2:

; 29   : 
; 30   : 	return 0;

	xor	eax, eax

; 31   : }

	ret	0
?vmmngr_ptable_lookup_entry@@YAPAIPAUptable@@I@Z ENDP	; vmmngr_ptable_lookup_entry
_TEXT	ENDS
PUBLIC	?vmmngr_ptable_clear@@YAXPAUptable@@@Z		; vmmngr_ptable_clear
EXTRN	?memset@@YAPAXPAXEI@Z:PROC			; memset
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_ptable_clear@@YAXPAUptable@@@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
?vmmngr_ptable_clear@@YAXPAUptable@@@Z PROC		; vmmngr_ptable_clear, COMDAT

; 35   : 	if(p)

	mov	eax, DWORD PTR _p$[esp-4]
	test	eax, eax
	je	SHORT $LN1@vmmngr_pta@3

; 36   : 		memset(p,0,sizeof(ptable));

	push	4096					; 00001000H
	push	0
	push	eax
	call	?memset@@YAPAXPAXEI@Z			; memset
	add	esp, 12					; 0000000cH
$LN1@vmmngr_pta@3:

; 37   : }

	ret	0
?vmmngr_ptable_clear@@YAXPAUptable@@@Z ENDP		; vmmngr_ptable_clear
_TEXT	ENDS
PUBLIC	?vmmngr_pdirectory_clear@@YAXPAUpdirectory@@@Z	; vmmngr_pdirectory_clear
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_pdirectory_clear@@YAXPAUpdirectory@@@Z
_TEXT	SEGMENT
_dir$ = 8						; size = 4
?vmmngr_pdirectory_clear@@YAXPAUpdirectory@@@Z PROC	; vmmngr_pdirectory_clear, COMDAT

; 41   : 	if(dir)

	mov	eax, DWORD PTR _dir$[esp-4]
	test	eax, eax
	je	SHORT $LN1@vmmngr_pdi

; 42   : 		memset(dir,0,sizeof(pdirectory));

	push	4096					; 00001000H
	push	0
	push	eax
	call	?memset@@YAPAXPAXEI@Z			; memset
	add	esp, 12					; 0000000cH
$LN1@vmmngr_pdi:

; 43   : }

	ret	0
?vmmngr_pdirectory_clear@@YAXPAUpdirectory@@@Z ENDP	; vmmngr_pdirectory_clear
_TEXT	ENDS
PUBLIC	?vmmngr_pdirectory_virt_to_index@@YAII@Z	; vmmngr_pdirectory_virt_to_index
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_pdirectory_virt_to_index@@YAII@Z
_TEXT	SEGMENT
_addr$ = 8						; size = 4
?vmmngr_pdirectory_virt_to_index@@YAII@Z PROC		; vmmngr_pdirectory_virt_to_index, COMDAT

; 47   : 	return (addr >= DTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;

	mov	eax, DWORD PTR _addr$[esp-4]
	xor	ecx, ecx
	cmp	ecx, 1
	jl	SHORT $LN3@vmmngr_pdi@2
	jg	SHORT $LN5@vmmngr_pdi@2
	test	eax, eax
	jb	SHORT $LN3@vmmngr_pdi@2
$LN5@vmmngr_pdi@2:
	xor	eax, eax

; 48   : }

	ret	0
$LN3@vmmngr_pdi@2:

; 47   : 	return (addr >= DTABLE_ADDR_SPACE_SIZE) ? 0 : addr / PAGE_SIZE;

	shr	eax, 12					; 0000000cH

; 48   : }

	ret	0
?vmmngr_pdirectory_virt_to_index@@YAII@Z ENDP		; vmmngr_pdirectory_virt_to_index
_TEXT	ENDS
PUBLIC	?vmmngr_pdirectory_lookup_entry@@YAPAIPAUpdirectory@@I@Z ; vmmngr_pdirectory_lookup_entry
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_pdirectory_lookup_entry@@YAPAIPAUpdirectory@@I@Z
_TEXT	SEGMENT
_dir$ = 8						; size = 4
_addr$ = 12						; size = 4
?vmmngr_pdirectory_lookup_entry@@YAPAIPAUpdirectory@@I@Z PROC ; vmmngr_pdirectory_lookup_entry, COMDAT

; 52   : 	if(dir)

	mov	edx, DWORD PTR _dir$[esp-4]
	test	edx, edx
	je	SHORT $LN1@vmmngr_pdi@3

; 53   : 		return &dir->m_entries[vmmngr_pdirectory_virt_to_index(addr)];

	mov	eax, DWORD PTR _addr$[esp-4]
	xor	ecx, ecx
	cmp	ecx, 1
	jl	SHORT $LN6@vmmngr_pdi@3
	jg	SHORT $LN8@vmmngr_pdi@3
	test	eax, eax
	jb	SHORT $LN6@vmmngr_pdi@3
$LN8@vmmngr_pdi@3:
	xor	eax, eax
	lea	eax, DWORD PTR [edx+eax*4]

; 56   : }

	ret	0

; 53   : 		return &dir->m_entries[vmmngr_pdirectory_virt_to_index(addr)];

$LN6@vmmngr_pdi@3:
	shr	eax, 12					; 0000000cH
	lea	eax, DWORD PTR [edx+eax*4]

; 56   : }

	ret	0
$LN1@vmmngr_pdi@3:

; 54   : 
; 55   : 	return 0;

	xor	eax, eax

; 56   : }

	ret	0
?vmmngr_pdirectory_lookup_entry@@YAPAIPAUpdirectory@@I@Z ENDP ; vmmngr_pdirectory_lookup_entry
_TEXT	ENDS
PUBLIC	?vmmngr_switch_pdirectory@@YA_NPAUpdirectory@@@Z ; vmmngr_switch_pdirectory
EXTRN	?pmmngr_load_PDBR@@YAXI@Z:PROC			; pmmngr_load_PDBR
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_switch_pdirectory@@YA_NPAUpdirectory@@@Z
_TEXT	SEGMENT
_dir$ = 8						; size = 4
?vmmngr_switch_pdirectory@@YA_NPAUpdirectory@@@Z PROC	; vmmngr_switch_pdirectory, COMDAT

; 60   : 	if(!dir)

	mov	eax, DWORD PTR _dir$[esp-4]
	test	eax, eax
	jne	SHORT $LN1@vmmngr_swi

; 61   : 		return false;

	xor	al, al

; 66   : }

	ret	0
$LN1@vmmngr_swi:

; 62   : 
; 63   : 	_cur_directory = dir;

	mov	DWORD PTR ?_cur_directory@@3PAUpdirectory@@A, eax ; _cur_directory

; 64   : 	pmmngr_load_PDBR(_cur_pdbr);

	mov	eax, DWORD PTR ?_cur_pdbr@@3IA		; _cur_pdbr
	push	eax
	call	?pmmngr_load_PDBR@@YAXI@Z		; pmmngr_load_PDBR
	add	esp, 4

; 65   : 	return true;

	mov	al, 1

; 66   : }

	ret	0
?vmmngr_switch_pdirectory@@YA_NPAUpdirectory@@@Z ENDP	; vmmngr_switch_pdirectory
_TEXT	ENDS
PUBLIC	?vmmngr_flush_tlb_entry@@YAXI@Z			; vmmngr_flush_tlb_entry
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_flush_tlb_entry@@YAXI@Z
_TEXT	SEGMENT
_addr$ = 8						; size = 4
?vmmngr_flush_tlb_entry@@YAXI@Z PROC			; vmmngr_flush_tlb_entry, COMDAT

; 70   : #ifdef _MSC_VER
; 71   : 	_asm
; 72   : 	{
; 73   : 		cli

	cli

; 74   : 		invlpg addr

	invlpg	DWORD PTR _addr$[esp-4]

; 75   : 		sti

	sti

; 76   : 	}
; 77   : #endif
; 78   : 
; 79   : 	return;
; 80   : }

	ret	0
?vmmngr_flush_tlb_entry@@YAXI@Z ENDP			; vmmngr_flush_tlb_entry
_TEXT	ENDS
PUBLIC	?vmmngr_get_directory@@YAPAUpdirectory@@XZ	; vmmngr_get_directory
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_get_directory@@YAPAUpdirectory@@XZ
_TEXT	SEGMENT
?vmmngr_get_directory@@YAPAUpdirectory@@XZ PROC		; vmmngr_get_directory, COMDAT

; 84   : 	return _cur_directory;

	mov	eax, DWORD PTR ?_cur_directory@@3PAUpdirectory@@A ; _cur_directory

; 85   : }

	ret	0
?vmmngr_get_directory@@YAPAUpdirectory@@XZ ENDP		; vmmngr_get_directory
_TEXT	ENDS
PUBLIC	?vmmngr_alloc_page@@YA_NPAI@Z			; vmmngr_alloc_page
EXTRN	?pt_entry_add_attribute@@YAXPAII@Z:PROC		; pt_entry_add_attribute
EXTRN	?pt_entry_set_frame@@YAXPAII@Z:PROC		; pt_entry_set_frame
EXTRN	?pmalloc@@YAPAXI@Z:PROC				; pmalloc
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_alloc_page@@YA_NPAI@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
?vmmngr_alloc_page@@YA_NPAI@Z PROC			; vmmngr_alloc_page, COMDAT

; 89   : 	void* p = pmalloc(PAGE_SIZE);

	push	4096					; 00001000H
	call	?pmalloc@@YAPAXI@Z			; pmalloc
	add	esp, 4

; 90   : 	if(p)

	test	eax, eax
	je	SHORT $LN1@vmmngr_all

; 91   : 		return false;

	xor	al, al

; 97   : }

	ret	0
$LN1@vmmngr_all:
	push	esi

; 92   : 
; 93   : 	pt_entry_set_frame(e,(physical_addr)p);

	mov	esi, DWORD PTR _e$[esp]
	push	0
	push	esi
	call	?pt_entry_set_frame@@YAXPAII@Z		; pt_entry_set_frame

; 94   : 	pt_entry_add_attribute(e,I86_PTE_PRESENT);

	push	1
	push	esi
	call	?pt_entry_add_attribute@@YAXPAII@Z	; pt_entry_add_attribute
	add	esp, 16					; 00000010H

; 95   : 
; 96   : 	return true;

	mov	al, 1
	pop	esi

; 97   : }

	ret	0
?vmmngr_alloc_page@@YA_NPAI@Z ENDP			; vmmngr_alloc_page
_TEXT	ENDS
PUBLIC	?vmmngr_free_page@@YAXPAI@Z			; vmmngr_free_page
EXTRN	?pt_entry_del_attribute@@YAXPAII@Z:PROC		; pt_entry_del_attribute
EXTRN	?pfree@@YAXPAXI@Z:PROC				; pfree
EXTRN	?pt_entry_pfn@@YAII@Z:PROC			; pt_entry_pfn
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_free_page@@YAXPAI@Z
_TEXT	SEGMENT
_e$ = 8							; size = 4
?vmmngr_free_page@@YAXPAI@Z PROC			; vmmngr_free_page, COMDAT

; 100  : {

	push	esi

; 101  : 	void* p = (void*)pt_entry_pfn(*e);

	mov	esi, DWORD PTR _e$[esp]
	mov	eax, DWORD PTR [esi]
	push	eax
	call	?pt_entry_pfn@@YAII@Z			; pt_entry_pfn
	add	esp, 4

; 102  : 	if(p)

	test	eax, eax
	je	SHORT $LN1@vmmngr_fre

; 103  : 		pfree(p,PAGE_SIZE);

	push	4096					; 00001000H
	push	eax
	call	?pfree@@YAXPAXI@Z			; pfree
	add	esp, 8
$LN1@vmmngr_fre:

; 104  : 
; 105  : 	pt_entry_del_attribute(e,I86_PTE_PRESENT);

	push	1
	push	esi
	call	?pt_entry_del_attribute@@YAXPAII@Z	; pt_entry_del_attribute
	add	esp, 8
	pop	esi

; 106  : 
; 107  : 	return;
; 108  : }

	ret	0
?vmmngr_free_page@@YAXPAI@Z ENDP			; vmmngr_free_page
_TEXT	ENDS
PUBLIC	?vmmngr_initialize@@YAXXZ			; vmmngr_initialize
EXTRN	?pmmngr_paging_enable@@YAX_N@Z:PROC		; pmmngr_paging_enable
EXTRN	?pd_entry_set_frame@@YAXPAII@Z:PROC		; pd_entry_set_frame
EXTRN	?pd_entry_add_attribute@@YAXPAII@Z:PROC		; pd_entry_add_attribute
; Function compile flags: /Ogtpy
;	COMDAT ?vmmngr_initialize@@YAXXZ
_TEXT	SEGMENT
_page$2865 = -8						; size = 4
_dir$ = -4						; size = 4
?vmmngr_initialize@@YAXXZ PROC				; vmmngr_initialize, COMDAT

; 111  : {

	sub	esp, 8

; 112  : 	pdirectory* dir = (pdirectory*)pmalloc(sizeof(pdirectory));

	push	4096					; 00001000H
	call	?pmalloc@@YAPAXI@Z			; pmalloc
	add	esp, 4
	mov	DWORD PTR _dir$[esp+8], eax

; 113  : 	
; 114  : 	if(!dir)

	test	eax, eax
	je	$LN10@vmmngr_ini
	push	ebp

; 115  : 		return;
; 116  : 
; 117  : 	vmmngr_pdirectory_clear(dir);

	push	4096					; 00001000H
	push	0
	push	eax
	call	?memset@@YAPAXPAXEI@Z			; memset
	add	esp, 12					; 0000000cH
	push	ebx
	push	esi
	xor	ebp, ebp
	push	edi
$LL41@vmmngr_ini:

; 118  : 
; 119  : 	// Identity Map all memory
; 120  : 	// for(uint32_t j = 0;j < TABLES_PER_DIRECTORY;j++)
; 121  : 
; 122  : 	// Identity Map only the first 4MB of memory.
; 123  : 	for(uint32_t j = 0;j < 1;j++)
; 124  : 	{
; 125  : 		ptable* table = (ptable*)pmalloc(sizeof(ptable));

	push	4096					; 00001000H
	call	?pmalloc@@YAPAXI@Z			; pmalloc
	mov	edi, eax
	add	esp, 4

; 126  : 
; 127  : 		if(!table)

	test	edi, edi
	je	$LN43@vmmngr_ini

; 128  : 			return;
; 129  : 
; 130  : 		vmmngr_ptable_clear(table);

	push	4096					; 00001000H
	push	0
	push	edi
	call	?memset@@YAPAXPAXEI@Z			; memset
	add	esp, 12					; 0000000cH

; 131  : 
; 132  : 		for(int i = 0, frame = j * PTABLE_ADDR_SPACE_SIZE ;i < PAGES_PER_TABLE;i++,frame += PAGE_SIZE)

	mov	esi, ebp
	mov	ebx, 1024				; 00000400H
$LL4@vmmngr_ini:

; 133  : 		{
; 134  : 			pt_entry page = 0;
; 135  : 			pt_entry_add_attribute(&page,I86_PTE_PRESENT);

	lea	eax, DWORD PTR _page$2865[esp+24]
	push	1
	push	eax
	mov	DWORD PTR _page$2865[esp+32], 0
	call	?pt_entry_add_attribute@@YAXPAII@Z	; pt_entry_add_attribute

; 136  : 			pt_entry_set_frame(&page,frame);

	lea	ecx, DWORD PTR _page$2865[esp+32]
	push	esi
	push	ecx
	call	?pt_entry_set_frame@@YAXPAII@Z		; pt_entry_set_frame
	add	esp, 16					; 00000010H

; 137  : 
; 138  : 			table->m_entries[vmmngr_ptable_virt_to_index(frame)] = page;

	cmp	esi, 4194304				; 00400000H
	jb	SHORT $LN20@vmmngr_ini
	xor	eax, eax
	jmp	SHORT $LN21@vmmngr_ini
$LN20@vmmngr_ini:
	mov	eax, esi
	shr	eax, 12					; 0000000cH
$LN21@vmmngr_ini:
	mov	edx, DWORD PTR _page$2865[esp+24]
	add	esi, 4096				; 00001000H
	sub	ebx, 1
	mov	DWORD PTR [edi+eax*4], edx
	jne	SHORT $LL4@vmmngr_ini

; 139  : 		}
; 140  : 
; 141  : 		pd_entry* entry = vmmngr_pdirectory_lookup_entry(dir,j * PTABLE_ADDR_SPACE_SIZE);

	xor	eax, eax
	cmp	eax, 1
	jl	SHORT $LN27@vmmngr_ini
	jg	SHORT $LN40@vmmngr_ini
	test	ebp, ebp
	jb	SHORT $LN27@vmmngr_ini
$LN40@vmmngr_ini:
	xor	eax, eax
	jmp	SHORT $LN28@vmmngr_ini
$LN27@vmmngr_ini:
	mov	eax, ebp
	shr	eax, 12					; 0000000cH
$LN28@vmmngr_ini:
	mov	ecx, DWORD PTR _dir$[esp+24]
	lea	esi, DWORD PTR [ecx+eax*4]

; 142  : 		pd_entry_add_attribute(entry,I86_PDE_PRESENT);

	push	1
	push	esi
	call	?pd_entry_add_attribute@@YAXPAII@Z	; pd_entry_add_attribute

; 143  : 		pd_entry_add_attribute(entry,I86_PDE_WRITABLE);

	push	2
	push	esi
	call	?pd_entry_add_attribute@@YAXPAII@Z	; pd_entry_add_attribute

; 144  : 		pd_entry_set_frame(entry,(physical_addr)table);

	push	edi
	push	esi
	call	?pd_entry_set_frame@@YAXPAII@Z		; pd_entry_set_frame
	add	ebp, 4194304				; 00400000H
	add	esp, 24					; 00000018H
	cmp	ebp, 4194304				; 00400000H
	jb	$LL41@vmmngr_ini

; 145  : 	}
; 146  : 
; 147  : 	_cur_pdbr = (physical_addr)&dir->m_entries;

	mov	eax, DWORD PTR _dir$[esp+24]

; 148  : 
; 149  : 	if(!vmmngr_switch_pdirectory(dir))

	push	eax
	mov	DWORD PTR ?_cur_pdbr@@3IA, eax		; _cur_pdbr
	mov	DWORD PTR ?_cur_directory@@3PAUpdirectory@@A, eax ; _cur_directory
	call	?pmmngr_load_PDBR@@YAXI@Z		; pmmngr_load_PDBR

; 150  : 		return;
; 151  : 
; 152  : 	pmmngr_paging_enable(true);

	push	1
	call	?pmmngr_paging_enable@@YAX_N@Z		; pmmngr_paging_enable
	add	esp, 8
$LN43@vmmngr_ini:
	pop	edi
	pop	esi
	pop	ebx
	pop	ebp
$LN10@vmmngr_ini:

; 153  : 
; 154  : 	return;
; 155  : }

	add	esp, 8
	ret	0
?vmmngr_initialize@@YAXXZ ENDP				; vmmngr_initialize
_TEXT	ENDS
END