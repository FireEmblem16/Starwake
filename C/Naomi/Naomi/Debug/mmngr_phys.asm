; Listing generated by Microsoft (R) Optimizing Compiler Version 15.00.21022.08 

	TITLE	c:\Users\The Science Guy\Documents\Visual Studio 2008\Projects\Naomi\Naomi\mmngr_phys.cpp
	.686P
	.XMM
	include listing.inc
	.model	flat

INCLUDELIB LIBCMT
INCLUDELIB OLDNAMES

PUBLIC	??_C@_0BA@BJOBDCJI@ACPI?5NVS?5Memory?$AA@	; `string'
PUBLIC	??_C@_0N@EHGNOCNM@ACPI?5Reclaim?$AA@		; `string'
PUBLIC	??_C@_08NKCLJDKG@Reserved?$AA@			; `string'
PUBLIC	??_C@_09PPNHOLKJ@Available?$AA@			; `string'
PUBLIC	?strMemoryTypes@@3PAPADA			; strMemoryTypes
PUBLIC	?kernelSize@@3IA				; kernelSize
PUBLIC	?bootinfo@@3PAUmultiboot_info@@A		; bootinfo
_BSS	SEGMENT
?kernelSize@@3IA DD 01H DUP (?)				; kernelSize
?bootinfo@@3PAUmultiboot_info@@A DD 01H DUP (?)		; bootinfo
__mmngr_memory_size DD 01H DUP (?)
__mmngr_used_blocks DD 01H DUP (?)
__mmngr_max_blocks DD 01H DUP (?)
__mmngr_memory_map DD 01H DUP (?)
_BSS	ENDS
_DATA	SEGMENT
?strMemoryTypes@@3PAPADA DD FLAT:??_C@_09PPNHOLKJ@Available?$AA@ ; strMemoryTypes
	DD	FLAT:??_C@_08NKCLJDKG@Reserved?$AA@
	DD	FLAT:??_C@_0N@EHGNOCNM@ACPI?5Reclaim?$AA@
	DD	FLAT:??_C@_0BA@BJOBDCJI@ACPI?5NVS?5Memory?$AA@
_DATA	ENDS
;	COMDAT ??_C@_0BA@BJOBDCJI@ACPI?5NVS?5Memory?$AA@
CONST	SEGMENT
??_C@_0BA@BJOBDCJI@ACPI?5NVS?5Memory?$AA@ DB 'ACPI NVS Memory', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0N@EHGNOCNM@ACPI?5Reclaim?$AA@
CONST	SEGMENT
??_C@_0N@EHGNOCNM@ACPI?5Reclaim?$AA@ DB 'ACPI Reclaim', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_08NKCLJDKG@Reserved?$AA@
CONST	SEGMENT
??_C@_08NKCLJDKG@Reserved?$AA@ DB 'Reserved', 00H	; `string'
CONST	ENDS
;	COMDAT ??_C@_09PPNHOLKJ@Available?$AA@
CONST	SEGMENT
??_C@_09PPNHOLKJ@Available?$AA@ DB 'Available', 00H	; `string'
CONST	ENDS
PUBLIC	?mmap_set@@YAXH@Z				; mmap_set
; Function compile flags: /Ogtpy
; File c:\users\the science guy\documents\visual studio 2008\projects\naomi\naomi\mmngr_phys.cpp
;	COMDAT ?mmap_set@@YAXH@Z
_TEXT	SEGMENT
_bit$ = 8						; size = 4
?mmap_set@@YAXH@Z PROC					; mmap_set, COMDAT

; 36   : 	_mmngr_memory_map[bit / 32] |= (1 << (bit % 32));

	mov	ecx, DWORD PTR _bit$[esp-4]
	mov	eax, ecx
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	mov	edx, DWORD PTR __mmngr_memory_map
	sar	eax, 5
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [edx+eax*4]
	jns	SHORT $LN3@mmap_set
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN3@mmap_set:
	mov	edx, 1
	shl	edx, cl
	or	DWORD PTR [eax], edx

; 37   : }

	ret	0
?mmap_set@@YAXH@Z ENDP					; mmap_set
_TEXT	ENDS
PUBLIC	?mmap_unset@@YAXH@Z				; mmap_unset
; Function compile flags: /Ogtpy
;	COMDAT ?mmap_unset@@YAXH@Z
_TEXT	SEGMENT
_bit$ = 8						; size = 4
?mmap_unset@@YAXH@Z PROC				; mmap_unset, COMDAT

; 41   : 	_mmngr_memory_map[bit / 32] &= ~(1 << (bit % 32));

	mov	ecx, DWORD PTR _bit$[esp-4]
	mov	eax, ecx
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	mov	edx, DWORD PTR __mmngr_memory_map
	sar	eax, 5
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [edx+eax*4]
	jns	SHORT $LN3@mmap_unset
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN3@mmap_unset:
	mov	edx, 1
	shl	edx, cl
	not	edx
	and	DWORD PTR [eax], edx

; 42   : }

	ret	0
?mmap_unset@@YAXH@Z ENDP				; mmap_unset
_TEXT	ENDS
PUBLIC	?mmap_test@@YA_NH@Z				; mmap_test
; Function compile flags: /Ogtpy
;	COMDAT ?mmap_test@@YA_NH@Z
_TEXT	SEGMENT
_bit$ = 8						; size = 4
?mmap_test@@YA_NH@Z PROC				; mmap_test, COMDAT

; 46   : 	return _mmngr_memory_map[bit / 32] & (1 << (bit % 32));

	mov	eax, DWORD PTR _bit$[esp-4]
	mov	ecx, eax
	and	ecx, -2147483617			; 8000001fH
	push	esi
	jns	SHORT $LN3@mmap_test
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN3@mmap_test:
	cdq
	mov	esi, 1
	shl	esi, cl
	mov	ecx, DWORD PTR __mmngr_memory_map
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	test	esi, DWORD PTR [ecx+eax*4]
	pop	esi
	setne	al

; 47   : }

	ret	0
?mmap_test@@YA_NH@Z ENDP				; mmap_test
_TEXT	ENDS
PUBLIC	?pmmngr_init_region@@YAXII@Z			; pmmngr_init_region
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_init_region@@YAXII@Z
_TEXT	SEGMENT
_base$ = 8						; size = 4
_size$ = 12						; size = 4
?pmmngr_init_region@@YAXII@Z PROC			; pmmngr_init_region, COMDAT

; 146  : {

	push	ebx

; 147  : 	int align = base / PMMNGR_BLOCK_SIZE;
; 148  : 	int blocks = size / PMMNGR_BLOCK_SIZE;
; 149  : 
; 150  : 	for(;blocks >= 0;blocks--)

	mov	ebx, DWORD PTR __mmngr_memory_map
	push	esi
	mov	esi, DWORD PTR _base$[esp+4]
	push	edi
	mov	edi, DWORD PTR _size$[esp+8]
	shr	edi, 12					; 0000000cH
	shr	esi, 12					; 0000000cH
	test	edi, edi
	jl	SHORT $LN15@pmmngr_ini
	npad	5
$LL4@pmmngr_ini:

; 151  : 	{
; 152  : 		mmap_unset(align++);

	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [ebx+eax*4]
	jns	SHORT $LN16@pmmngr_ini
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN16@pmmngr_ini:

; 153  : 		_mmngr_used_blocks--;

	dec	DWORD PTR __mmngr_used_blocks
	mov	edx, 1
	shl	edx, cl
	inc	esi
	not	edx
	and	DWORD PTR [eax], edx
	sub	edi, 1
	jns	SHORT $LL4@pmmngr_ini
$LN15@pmmngr_ini:

; 154  : 	}
; 155  : 
; 156  : 	if(!mmap_test(0))

	test	BYTE PTR [ebx], 1
	jne	SHORT $LN1@pmmngr_ini

; 157  : 	{
; 158  : 		mmap_set(0);

	or	DWORD PTR [ebx], 1

; 159  : 		_mmngr_used_blocks++;

	inc	DWORD PTR __mmngr_used_blocks
$LN1@pmmngr_ini:
	pop	edi
	pop	esi
	pop	ebx

; 160  : 	}
; 161  : 
; 162  : 	return;
; 163  : }

	ret	0
?pmmngr_init_region@@YAXII@Z ENDP			; pmmngr_init_region
_TEXT	ENDS
PUBLIC	?pmmngr_deinit_region@@YAXII@Z			; pmmngr_deinit_region
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_deinit_region@@YAXII@Z
_TEXT	SEGMENT
_base$ = 8						; size = 4
_size$ = 12						; size = 4
?pmmngr_deinit_region@@YAXII@Z PROC			; pmmngr_deinit_region, COMDAT

; 167  : 	int align = base / PMMNGR_BLOCK_SIZE;
; 168  : 	int blocks = size / PMMNGR_BLOCK_SIZE;

	mov	eax, DWORD PTR _size$[esp-4]
	push	esi
	mov	esi, DWORD PTR _base$[esp]
	push	edi
	mov	edi, eax
	shr	esi, 12					; 0000000cH
	shr	edi, 12					; 0000000cH

; 169  : 
; 170  : 	if(size % PMMNGR_BLOCK_SIZE != 0)

	test	eax, 4095				; 00000fffH
	je	SHORT $LN4@pmmngr_dei

; 171  : 		blocks++;

	inc	edi
$LN4@pmmngr_dei:

; 172  : 
; 173  : 	for(;blocks >= 0;blocks--)

	test	edi, edi
	jl	SHORT $LN1@pmmngr_dei
	mov	eax, DWORD PTR __mmngr_used_blocks
	lea	ecx, DWORD PTR [eax+edi+1]
	push	ebx
	mov	ebx, DWORD PTR __mmngr_memory_map
	mov	DWORD PTR __mmngr_used_blocks, ecx
$LL3@pmmngr_dei:

; 174  : 	{
; 175  : 		mmap_set(align++);

	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [ebx+eax*4]
	jns	SHORT $LN11@pmmngr_dei
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN11@pmmngr_dei:
	mov	edx, 1
	shl	edx, cl
	inc	esi
	or	DWORD PTR [eax], edx
	sub	edi, 1
	jns	SHORT $LL3@pmmngr_dei
	pop	ebx
$LN1@pmmngr_dei:
	pop	edi
	pop	esi

; 176  : 		_mmngr_used_blocks++;
; 177  : 	}
; 178  : 
; 179  : 	return;
; 180  : }

	ret	0
?pmmngr_deinit_region@@YAXII@Z ENDP			; pmmngr_deinit_region
_TEXT	ENDS
PUBLIC	?pmmngr_free_block@@YAXPAX@Z			; pmmngr_free_block
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_free_block@@YAXPAX@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
?pmmngr_free_block@@YAXPAX@Z PROC			; pmmngr_free_block, COMDAT

; 202  : 	physical_addr addr = (physical_addr)p;
; 203  : 	int frame = addr / PMMNGR_BLOCK_SIZE;

	mov	ecx, DWORD PTR _p$[esp-4]
	shr	ecx, 12					; 0000000cH

; 204  : 
; 205  : 	mmap_unset(frame);

	mov	eax, ecx
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	mov	edx, DWORD PTR __mmngr_memory_map
	sar	eax, 5
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [edx+eax*4]
	jns	SHORT $LN5@pmmngr_fre
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN5@pmmngr_fre:

; 206  : 	_mmngr_used_blocks--;

	dec	DWORD PTR __mmngr_used_blocks
	mov	edx, 1
	shl	edx, cl
	not	edx
	and	DWORD PTR [eax], edx

; 207  : 
; 208  : 	return;
; 209  : }

	ret	0
?pmmngr_free_block@@YAXPAX@Z ENDP			; pmmngr_free_block
_TEXT	ENDS
PUBLIC	?pmmngr_free_blocks@@YAXPAXI@Z			; pmmngr_free_blocks
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_free_blocks@@YAXPAXI@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
_size$ = 12						; size = 4
?pmmngr_free_blocks@@YAXPAXI@Z PROC			; pmmngr_free_blocks, COMDAT

; 250  : {

	push	ebp

; 251  : 	physical_addr addr = (physical_addr)p;
; 252  : 	int frame = addr / PMMNGR_BLOCK_SIZE;
; 253  : 
; 254  : 	for(uint32_t i = 0;i < size;i++)

	mov	ebp, DWORD PTR _size$[esp]
	push	esi
	mov	esi, DWORD PTR _p$[esp+4]
	shr	esi, 12					; 0000000cH
	test	ebp, ebp
	jbe	SHORT $LN10@pmmngr_fre@2
	push	ebx
	mov	ebx, DWORD PTR __mmngr_memory_map
	push	edi
	mov	edi, ebp
	npad	5
$LL3@pmmngr_fre@2:

; 255  : 		mmap_unset(frame + i);

	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [ebx+eax*4]
	jns	SHORT $LN11@pmmngr_fre@2
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN11@pmmngr_fre@2:
	mov	edx, 1
	shl	edx, cl
	inc	esi
	not	edx
	and	DWORD PTR [eax], edx
	sub	edi, 1
	jne	SHORT $LL3@pmmngr_fre@2
	pop	edi
	pop	ebx
$LN10@pmmngr_fre@2:

; 256  : 
; 257  : 	_mmngr_used_blocks -= size;

	sub	DWORD PTR __mmngr_used_blocks, ebp
	pop	esi
	pop	ebp

; 258  : 
; 259  : 	return;
; 260  : }

	ret	0
?pmmngr_free_blocks@@YAXPAXI@Z ENDP			; pmmngr_free_blocks
_TEXT	ENDS
PUBLIC	?pmmngr_get_memory_size@@YAIXZ			; pmmngr_get_memory_size
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_memory_size@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_memory_size@@YAIXZ PROC			; pmmngr_get_memory_size, COMDAT

; 264  : 	return _mmngr_memory_size;

	mov	eax, DWORD PTR __mmngr_memory_size

; 265  : }

	ret	0
?pmmngr_get_memory_size@@YAIXZ ENDP			; pmmngr_get_memory_size
_TEXT	ENDS
PUBLIC	?pmmngr_get_block_count@@YAIXZ			; pmmngr_get_block_count
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_block_count@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_block_count@@YAIXZ PROC			; pmmngr_get_block_count, COMDAT

; 269  : 	return _mmngr_max_blocks;

	mov	eax, DWORD PTR __mmngr_max_blocks

; 270  : }

	ret	0
?pmmngr_get_block_count@@YAIXZ ENDP			; pmmngr_get_block_count
_TEXT	ENDS
PUBLIC	?pmmngr_get_used_block_count@@YAIXZ		; pmmngr_get_used_block_count
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_used_block_count@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_used_block_count@@YAIXZ PROC		; pmmngr_get_used_block_count, COMDAT

; 274  : 	return _mmngr_used_blocks;

	mov	eax, DWORD PTR __mmngr_used_blocks

; 275  : }

	ret	0
?pmmngr_get_used_block_count@@YAIXZ ENDP		; pmmngr_get_used_block_count
_TEXT	ENDS
PUBLIC	?pmmngr_get_free_block_count@@YAIXZ		; pmmngr_get_free_block_count
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_free_block_count@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_free_block_count@@YAIXZ PROC		; pmmngr_get_free_block_count, COMDAT

; 279  : 	return _mmngr_max_blocks - _mmngr_used_blocks;

	mov	eax, DWORD PTR __mmngr_max_blocks
	sub	eax, DWORD PTR __mmngr_used_blocks

; 280  : }

	ret	0
?pmmngr_get_free_block_count@@YAIXZ ENDP		; pmmngr_get_free_block_count
_TEXT	ENDS
PUBLIC	?pmmngr_get_block_size@@YAIXZ			; pmmngr_get_block_size
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_block_size@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_block_size@@YAIXZ PROC			; pmmngr_get_block_size, COMDAT

; 284  : 	return PMMNGR_BLOCK_SIZE;

	mov	eax, 4096				; 00001000H

; 285  : }

	ret	0
?pmmngr_get_block_size@@YAIXZ ENDP			; pmmngr_get_block_size
_TEXT	ENDS
PUBLIC	?pmmngr_paging_enable@@YAX_N@Z			; pmmngr_paging_enable
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_paging_enable@@YAX_N@Z
_TEXT	SEGMENT
_b$ = 8							; size = 1
?pmmngr_paging_enable@@YAX_N@Z PROC			; pmmngr_paging_enable, COMDAT

; 288  : {

	push	ebp
	mov	ebp, esp

; 289  : #ifdef _MSC_VER
; 290  : 	_asm
; 291  : 	{
; 292  : 		mov eax,cr0

	mov	eax, cr0

; 293  : 		cmp [b],1

	cmp	BYTE PTR _b$[ebp], 1

; 294  : 		je enable

	je	SHORT $enable$2868

; 295  : 		jmp disable

	jmp	SHORT $disable$2869
$enable$2868:

; 296  : 	enable:
; 297  : 		or eax,0x80000000

	or	eax, -2147483648			; 80000000H

; 298  : 		mov cr0,eax

	mov	cr0, eax

; 299  : 		jmp done

	jmp	SHORT $done$2870
$disable$2869:

; 300  : 	disable:
; 301  : 		and eax,0x7FFFFFFF

	and	eax, 2147483647				; 7fffffffH

; 302  : 		mov cr0,eax

	mov	cr0, eax
$done$2870:

; 303  : 	done:
; 304  : 	}
; 305  : #endif
; 306  : 
; 307  : 	return;
; 308  : }

	pop	ebp
	ret	0
?pmmngr_paging_enable@@YAX_N@Z ENDP			; pmmngr_paging_enable
_TEXT	ENDS
PUBLIC	?pmmngr_is_paging@@YA_NXZ			; pmmngr_is_paging
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_is_paging@@YA_NXZ
_TEXT	SEGMENT
_res$ = -4						; size = 4
?pmmngr_is_paging@@YA_NXZ PROC				; pmmngr_is_paging, COMDAT

; 311  : {

	push	ecx

; 312  : 	uint32_t res = 0;

	mov	DWORD PTR _res$[esp+4], 0

; 313  : 
; 314  : #ifdef _MSC_VER
; 315  : 	_asm
; 316  : 	{
; 317  : 		mov eax,cr0

	mov	eax, cr0

; 318  : 		mov [res],eax

	mov	DWORD PTR _res$[esp+4], eax

; 319  : 	}
; 320  : #endif
; 321  : 
; 322  : 	return (res  & 0x80000000) ? false : true;

	mov	eax, DWORD PTR _res$[esp+4]
	shr	eax, 31					; 0000001fH
	not	al
	and	al, 1

; 323  : }

	pop	ecx
	ret	0
?pmmngr_is_paging@@YA_NXZ ENDP				; pmmngr_is_paging
_TEXT	ENDS
PUBLIC	?pmmngr_load_PDBR@@YAXI@Z			; pmmngr_load_PDBR
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_load_PDBR@@YAXI@Z
_TEXT	SEGMENT
_addr$ = 8						; size = 4
?pmmngr_load_PDBR@@YAXI@Z PROC				; pmmngr_load_PDBR, COMDAT

; 327  : #ifdef _MSC_VER
; 328  : 	_asm
; 329  : 	{
; 330  : 		mov eax,[addr]

	mov	eax, DWORD PTR _addr$[esp-4]

; 331  : 		mov cr3,eax

	mov	cr3, eax

; 332  : 	}
; 333  : #endif
; 334  : 
; 335  : 	return;
; 336  : }

	ret	0
?pmmngr_load_PDBR@@YAXI@Z ENDP				; pmmngr_load_PDBR
_TEXT	ENDS
PUBLIC	?pmmngr_get_PDBR@@YAIXZ				; pmmngr_get_PDBR
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_get_PDBR@@YAIXZ
_TEXT	SEGMENT
?pmmngr_get_PDBR@@YAIXZ PROC				; pmmngr_get_PDBR, COMDAT

; 340  : #ifdef _MSC_VER
; 341  : 	_asm
; 342  : 	{
; 343  : 		mov eax,cr3

	mov	eax, cr3

; 344  : 		ret

	ret	0

; 345  : 	}
; 346  : #endif
; 347  : }

	ret	0
?pmmngr_get_PDBR@@YAIXZ ENDP				; pmmngr_get_PDBR
_TEXT	ENDS
PUBLIC	?pfree@@YAXPAXI@Z				; pfree
; Function compile flags: /Ogtpy
;	COMDAT ?pfree@@YAXPAXI@Z
_TEXT	SEGMENT
_p$ = 8							; size = 4
_size$ = 12						; size = 4
?pfree@@YAXPAXI@Z PROC					; pfree, COMDAT

; 360  : 	size_t blocks = size / PMMNGR_BLOCK_SIZE;

	mov	ecx, DWORD PTR _size$[esp-4]
	mov	eax, ecx
	shr	eax, 12					; 0000000cH

; 361  : 	if(size % PMMNGR_BLOCK_SIZE != 0)

	test	ecx, 4095				; 00000fffH
	je	SHORT $LN1@pfree

; 362  : 		blocks++;

	inc	eax
$LN1@pfree:

; 363  : 
; 364  : 	pmmngr_free_blocks(p,blocks);

	mov	DWORD PTR _size$[esp-4], eax
	jmp	?pmmngr_free_blocks@@YAXPAXI@Z		; pmmngr_free_blocks
?pfree@@YAXPAXI@Z ENDP					; pfree
_TEXT	ENDS
PUBLIC	?mmap_first_pfree@@YAHXZ			; mmap_first_pfree
; Function compile flags: /Ogtpy
;	COMDAT ?mmap_first_pfree@@YAHXZ
_TEXT	SEGMENT
?mmap_first_pfree@@YAHXZ PROC				; mmap_first_pfree, COMDAT

; 50   : {

	push	ebx
	push	esi

; 51   : 	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)

	mov	esi, DWORD PTR __mmngr_max_blocks
	shr	esi, 5
	xor	eax, eax
	push	edi
	test	esi, esi
	jbe	SHORT $LN6@mmap_first
	mov	edi, DWORD PTR __mmngr_memory_map
$LL8@mmap_first:

; 52   : 		if(_mmngr_memory_map[i] != 0xFFFFFFFF)

	mov	edx, DWORD PTR [edi+eax*4]
	cmp	edx, -1
	je	SHORT $LN7@mmap_first

; 53   : 			for(int j = 0;j < 32;j++)

	xor	ecx, ecx
$LL4@mmap_first:

; 54   : 			{
; 55   : 				int bit = 1 << j;

	mov	ebx, 1
	shl	ebx, cl

; 56   : 
; 57   : 				if(!(_mmngr_memory_map[i] & bit))

	test	ebx, edx
	je	SHORT $LN15@mmap_first
	inc	ecx
	cmp	ecx, 32					; 00000020H
	jl	SHORT $LL4@mmap_first
$LN7@mmap_first:

; 51   : 	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)

	inc	eax
	cmp	eax, esi
	jb	SHORT $LL8@mmap_first
$LN6@mmap_first:
	pop	edi
	pop	esi

; 59   : 			}
; 60   : 
; 61   : 	return -1;

	or	eax, -1
	pop	ebx

; 62   : }

	ret	0
$LN15@mmap_first:
	pop	edi

; 58   : 					return i * 32 + j;

	shl	eax, 5
	pop	esi
	add	eax, ecx
	pop	ebx

; 62   : }

	ret	0
?mmap_first_pfree@@YAHXZ ENDP				; mmap_first_pfree
_TEXT	ENDS
PUBLIC	?mmap_first_free_s@@YAHI@Z			; mmap_first_free_s
; Function compile flags: /Ogtpy
;	COMDAT ?mmap_first_free_s@@YAHI@Z
_TEXT	SEGMENT
_i$2735 = -20						; size = 4
_j$2740 = -16						; size = 4
tv250 = -12						; size = 4
$T3022 = -8						; size = 4
tv168 = -4						; size = 4
tv245 = 8						; size = 4
_size$ = 8						; size = 4
?mmap_first_free_s@@YAHI@Z PROC				; mmap_first_free_s, COMDAT

; 65   : {

	sub	esp, 20					; 00000014H
	push	ebp

; 66   : 	if(size == 0)

	mov	ebp, DWORD PTR _size$[esp+20]
	xor	eax, eax
	cmp	ebp, eax
	jne	SHORT $LN15@mmap_first@2

; 67   : 		return -1;

	or	eax, -1
	pop	ebp

; 95   : }

	add	esp, 20					; 00000014H
	ret	0
$LN15@mmap_first@2:

; 68   : 
; 69   : 	if(size == 1)

	cmp	ebp, 1
	jne	SHORT $LN14@mmap_first@2
	pop	ebp

; 95   : }

	add	esp, 20					; 00000014H

; 70   : 		return mmap_first_pfree();

	jmp	?mmap_first_pfree@@YAHXZ		; mmap_first_pfree
$LN14@mmap_first@2:
	push	ebx
	push	esi

; 71   : 
; 72   : 	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)

	mov	esi, DWORD PTR __mmngr_max_blocks
	shr	esi, 5
	push	edi
	mov	DWORD PTR _i$2735[esp+36], eax
	mov	DWORD PTR $T3022[esp+36], esi
	cmp	esi, eax
	jbe	$LN31@mmap_first@2
	mov	DWORD PTR tv245[esp+32], eax
	npad	12
$LL33@mmap_first@2:

; 73   : 		if(_mmngr_memory_map[i] != 0xFFFFFFFF)

	mov	ecx, DWORD PTR __mmngr_memory_map
	mov	edx, DWORD PTR [ecx+eax*4]
	mov	DWORD PTR tv168[esp+36], edx
	cmp	edx, -1
	je	SHORT $LN12@mmap_first@2

; 74   : 			for(int j = 0;j < 32;j++)

	xor	ecx, ecx
	mov	DWORD PTR _j$2740[esp+36], ecx
	npad	8
$LL32@mmap_first@2:

; 75   : 			{
; 76   : 				int bit = 1 << j;

	mov	eax, 1
	shl	eax, cl

; 77   : 
; 78   : 				if(!(_mmngr_memory_map[i] & bit))

	test	edx, eax
	jne	SHORT $LN8@mmap_first@2
	mov	edx, DWORD PTR tv245[esp+32]

; 79   : 				{
; 80   : 					int startingBit = i * 32 + bit;
; 81   : 					uint32_t free = 0;

	xor	ebx, ebx
	lea	esi, DWORD PTR [edx+eax]

; 82   : 
; 83   : 					for(uint32_t count = 0;count <= size;count++)

	xor	edi, edi
$LL29@mmap_first@2:

; 84   : 					{
; 85   : 						if(!mmap_test(startingBit + count))

	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	jns	SHORT $LN38@mmap_first@2
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN38@mmap_first@2:
	mov	eax, 1
	shl	eax, cl
	mov	ecx, DWORD PTR __mmngr_memory_map
	mov	DWORD PTR tv250[esp+36], eax
	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	mov	edx, DWORD PTR tv250[esp+36]
	sar	eax, 5
	test	edx, DWORD PTR [ecx+eax*4]
	jne	SHORT $LN30@mmap_first@2

; 86   : 							free++;

	inc	ebx
$LN30@mmap_first@2:

; 87   : 
; 88   : 						if(free == size)

	cmp	ebx, ebp
	je	SHORT $LN25@mmap_first@2
	inc	edi
	inc	esi
	cmp	edi, ebp
	jbe	SHORT $LL29@mmap_first@2
	mov	ecx, DWORD PTR _j$2740[esp+36]
	mov	esi, DWORD PTR $T3022[esp+36]
	mov	edx, DWORD PTR tv168[esp+36]
$LN8@mmap_first@2:
	inc	ecx
	cmp	ecx, 32					; 00000020H
	mov	DWORD PTR _j$2740[esp+36], ecx
	jl	SHORT $LL32@mmap_first@2

; 74   : 			for(int j = 0;j < 32;j++)

	mov	eax, DWORD PTR _i$2735[esp+36]
$LN12@mmap_first@2:

; 71   : 
; 72   : 	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)

	add	DWORD PTR tv245[esp+32], 32		; 00000020H
	inc	eax
	mov	DWORD PTR _i$2735[esp+36], eax
	cmp	eax, esi
	jb	$LL33@mmap_first@2
$LN31@mmap_first@2:
	pop	edi
	pop	esi
	pop	ebx

; 90   : 					}
; 91   : 				}
; 92   : 			}
; 93   : 
; 94   : 	return -1;

	or	eax, -1
	pop	ebp

; 95   : }

	add	esp, 20					; 00000014H
	ret	0
$LN25@mmap_first@2:

; 89   : 							return i * 32 + j;

	mov	eax, DWORD PTR _i$2735[esp+36]
	pop	edi
	pop	esi
	shl	eax, 5
	add	eax, DWORD PTR _j$2740[esp+28]
	pop	ebx
	pop	ebp

; 95   : }

	add	esp, 20					; 00000014H
	ret	0
?mmap_first_free_s@@YAHI@Z ENDP				; mmap_first_free_s
_TEXT	ENDS
PUBLIC	?mmap_first_free_s@@YAHII@Z			; mmap_first_free_s
; Function compile flags: /Ogtpy
;	COMDAT ?mmap_first_free_s@@YAHII@Z
_TEXT	SEGMENT
_i$2761 = -20						; size = 4
tv282 = -16						; size = 4
tv294 = -12						; size = 4
tv200 = -8						; size = 4
$T3050 = -4						; size = 4
_size$ = 8						; size = 4
_align$ = 12						; size = 4
?mmap_first_free_s@@YAHII@Z PROC			; mmap_first_free_s, COMDAT

; 99   : 	if(size == 0)

	mov	eax, DWORD PTR _size$[esp-4]
	sub	esp, 20					; 00000014H
	test	eax, eax
	jne	SHORT $LN16@mmap_first@3

; 100  : 		return -1;

	or	eax, -1

; 131  : }

	add	esp, 20					; 00000014H
	ret	0
$LN16@mmap_first@3:

; 101  : 
; 102  : 	if(size == 1)

	cmp	eax, 1
	jne	SHORT $LN15@mmap_first@3

; 131  : }

	add	esp, 20					; 00000014H

; 103  : 		return mmap_first_pfree();

	jmp	?mmap_first_pfree@@YAHXZ		; mmap_first_pfree
$LN15@mmap_first@3:

; 104  : 
; 105  : 	for(uint32_t i = 0;i < pmmngr_get_block_count() / 32;i++)

	mov	ecx, DWORD PTR __mmngr_max_blocks
	push	ebx
	push	ebp
	shr	ecx, 5
	xor	eax, eax
	push	esi
	push	edi
	mov	DWORD PTR _i$2761[esp+36], eax
	mov	DWORD PTR $T3050[esp+36], ecx
	test	ecx, ecx
	jbe	$LN31@mmap_first@3
	xor	edi, edi
	mov	DWORD PTR tv282[esp+36], edi
	npad	12
$LL33@mmap_first@3:

; 106  : 		if(_mmngr_memory_map[i] != 0xFFFFFFFF)

	mov	edx, DWORD PTR __mmngr_memory_map
	mov	ebx, DWORD PTR [edx+eax*4]
	mov	DWORD PTR tv200[esp+36], ebx
	cmp	ebx, -1
	je	$LN13@mmap_first@3

; 107  : 			for(int j = 0;j < 32;j++)

	xor	ebp, ebp
	npad	8
$LL10@mmap_first@3:

; 108  : 			{
; 109  : 				int bit = 1 << j;

	mov	esi, 1
	mov	ecx, ebp
	shl	esi, cl

; 110  : 
; 111  : 				if(!(_mmngr_memory_map[i] & bit))

	test	ebx, esi
	jne	SHORT $LN9@mmap_first@3

; 112  : 				{
; 113  : 					if((i*32 + j) * PMMNGR_BLOCK_SIZE % align == 0)

	lea	eax, DWORD PTR [edi+ebp]
	shl	eax, 12					; 0000000cH
	xor	edx, edx
	div	DWORD PTR _align$[esp+32]
	test	edx, edx
	jne	SHORT $LN9@mmap_first@3
	add	esi, edi

; 114  : 					{
; 115  : 						int startingBit = i * 32 + bit;
; 116  : 						uint32_t free = 0;

	xor	ebx, ebx

; 117  : 
; 118  : 						for(uint32_t count = 0;count <= size;count++)

	xor	edi, edi
$LL5@mmap_first@3:

; 119  : 						{
; 120  : 							if(!mmap_test(startingBit + count))

	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	jns	SHORT $LN39@mmap_first@3
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN39@mmap_first@3:
	mov	eax, 1
	shl	eax, cl
	mov	ecx, DWORD PTR __mmngr_memory_map
	mov	DWORD PTR tv294[esp+36], eax
	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	mov	edx, DWORD PTR tv294[esp+36]
	sar	eax, 5
	test	edx, DWORD PTR [ecx+eax*4]
	jne	SHORT $LN30@mmap_first@3

; 121  : 								free++;

	inc	ebx
$LN30@mmap_first@3:

; 122  : 
; 123  : 							if(free == size)

	mov	eax, DWORD PTR _size$[esp+32]
	cmp	ebx, eax
	je	SHORT $LN26@mmap_first@3
	inc	edi
	inc	esi
	cmp	edi, eax
	jbe	SHORT $LL5@mmap_first@3
	mov	ebx, DWORD PTR tv200[esp+36]
	mov	edi, DWORD PTR tv282[esp+36]
$LN9@mmap_first@3:
	inc	ebp
	cmp	ebp, 32					; 00000020H
	jl	SHORT $LL10@mmap_first@3
	mov	eax, DWORD PTR _i$2761[esp+36]
	mov	ecx, DWORD PTR $T3050[esp+36]
$LN13@mmap_first@3:
	inc	eax
	add	edi, 32					; 00000020H
	mov	DWORD PTR _i$2761[esp+36], eax
	mov	DWORD PTR tv282[esp+36], edi
	cmp	eax, ecx
	jb	$LL33@mmap_first@3
$LN31@mmap_first@3:
	pop	edi
	pop	esi
	pop	ebp

; 125  : 						}
; 126  : 					}
; 127  : 				}
; 128  : 			}
; 129  : 
; 130  : 	return -1;

	or	eax, -1
	pop	ebx

; 131  : }

	add	esp, 20					; 00000014H
	ret	0
$LN26@mmap_first@3:

; 124  : 								return i * 32 + j;

	mov	eax, DWORD PTR _i$2761[esp+36]
	pop	edi
	shl	eax, 5
	pop	esi
	add	eax, ebp
	pop	ebp
	pop	ebx

; 131  : }

	add	esp, 20					; 00000014H
	ret	0
?mmap_first_free_s@@YAHII@Z ENDP			; mmap_first_free_s
_TEXT	ENDS
PUBLIC	?pmmngr_init@@YAXII@Z				; pmmngr_init
EXTRN	?memset@@YAPAXPAXEI@Z:PROC			; memset
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_init@@YAXII@Z
_TEXT	SEGMENT
_memSize$ = 8						; size = 4
_bitmap$ = 12						; size = 4
?pmmngr_init@@YAXII@Z PROC				; pmmngr_init, COMDAT

; 135  : 	_mmngr_memory_size = memSize;

	mov	eax, DWORD PTR _memSize$[esp-4]

; 136  : 	_mmngr_memory_map = (uint32_t*)bitmap;

	mov	ecx, DWORD PTR _bitmap$[esp-4]
	mov	DWORD PTR __mmngr_memory_size, eax

; 137  : 	_mmngr_max_blocks = (pmmngr_get_memory_size() * 1024) / PMMNGR_BLOCK_SIZE;

	shl	eax, 10					; 0000000aH
	shr	eax, 12					; 0000000cH
	mov	DWORD PTR __mmngr_max_blocks, eax

; 138  : 	_mmngr_used_blocks = _mmngr_max_blocks;

	mov	DWORD PTR __mmngr_used_blocks, eax

; 139  : 
; 140  : 	memset(_mmngr_memory_map,0xF,pmmngr_get_block_count() / PMMNGR_BLOCKS_PER_BYTE);

	shr	eax, 3
	push	eax
	push	15					; 0000000fH
	push	ecx
	mov	DWORD PTR __mmngr_memory_map, ecx
	call	?memset@@YAPAXPAXEI@Z			; memset
	add	esp, 12					; 0000000cH

; 141  : 
; 142  : 	return;
; 143  : }

	ret	0
?pmmngr_init@@YAXII@Z ENDP				; pmmngr_init
_TEXT	ENDS
PUBLIC	?pmmngr_alloc_block@@YAPAXXZ			; pmmngr_alloc_block
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_alloc_block@@YAPAXXZ
_TEXT	SEGMENT
?pmmngr_alloc_block@@YAPAXXZ PROC			; pmmngr_alloc_block, COMDAT

; 184  : 	if(pmmngr_get_free_block_count() <= 0)

	mov	eax, DWORD PTR __mmngr_max_blocks
	sub	eax, DWORD PTR __mmngr_used_blocks
	jne	SHORT $LN2@pmmngr_all

; 196  : 
; 197  : 	return (void*)addr;
; 198  : }

	ret	0
$LN2@pmmngr_all:
	push	esi

; 185  : 		return 0;
; 186  : 
; 187  : 	int frame = mmap_first_pfree();

	call	?mmap_first_pfree@@YAHXZ		; mmap_first_pfree
	mov	esi, eax

; 188  : 
; 189  : 	if(frame == -1)

	cmp	esi, -1
	jne	SHORT $LN1@pmmngr_all

; 190  : 		return 0;

	xor	eax, eax
	pop	esi

; 196  : 
; 197  : 	return (void*)addr;
; 198  : }

	ret	0
$LN1@pmmngr_all:

; 191  : 
; 192  : 	mmap_set(frame);

	mov	ecx, DWORD PTR __mmngr_memory_map
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	lea	eax, DWORD PTR [ecx+eax*4]
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	jns	SHORT $LN10@pmmngr_all
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN10@pmmngr_all:

; 193  : 
; 194  : 	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
; 195  : 	_mmngr_used_blocks++;

	inc	DWORD PTR __mmngr_used_blocks
	mov	edx, 1
	shl	edx, cl
	or	DWORD PTR [eax], edx
	mov	eax, esi
	shl	eax, 12					; 0000000cH
	pop	esi

; 196  : 
; 197  : 	return (void*)addr;
; 198  : }

	ret	0
?pmmngr_alloc_block@@YAPAXXZ ENDP			; pmmngr_alloc_block
_TEXT	ENDS
PUBLIC	?pmmngr_alloc_blocks@@YAPAXI@Z			; pmmngr_alloc_blocks
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_alloc_blocks@@YAPAXI@Z
_TEXT	SEGMENT
_frame$ = 8						; size = 4
_size$ = 8						; size = 4
?pmmngr_alloc_blocks@@YAPAXI@Z PROC			; pmmngr_alloc_blocks, COMDAT

; 213  : 	if(pmmngr_get_free_block_count() <= size)

	mov	eax, DWORD PTR __mmngr_max_blocks
	sub	eax, DWORD PTR __mmngr_used_blocks
	push	ebp
	mov	ebp, DWORD PTR _size$[esp]
	cmp	eax, ebp
	ja	SHORT $LN5@pmmngr_all@2
$LN15@pmmngr_all@2:

; 214  : 		return 0;

	xor	eax, eax
	pop	ebp

; 226  : 
; 227  : 	return (void*)addr;
; 228  : }

	ret	0
$LN5@pmmngr_all@2:

; 215  : 
; 216  : 	int frame = mmap_first_free_s(size);

	push	ebp
	call	?mmap_first_free_s@@YAHI@Z		; mmap_first_free_s
	add	esp, 4
	mov	DWORD PTR _frame$[esp], eax

; 217  : 
; 218  : 	if(frame == -1)

	cmp	eax, -1

; 219  : 		return 0;

	je	SHORT $LN15@pmmngr_all@2

; 220  : 
; 221  : 	for(uint32_t i = 0;i < size;i++)

	test	ebp, ebp
	jbe	SHORT $LN1@pmmngr_all@2
	push	ebx
	mov	ebx, DWORD PTR __mmngr_memory_map
	push	esi
	push	edi
	mov	esi, eax
	mov	edi, ebp
	npad	5
$LL3@pmmngr_all@2:

; 222  : 		mmap_set(frame + i);

	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [ebx+eax*4]
	jns	SHORT $LN14@pmmngr_all@2
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN14@pmmngr_all@2:
	mov	edx, 1
	shl	edx, cl
	inc	esi
	or	DWORD PTR [eax], edx
	sub	edi, 1
	jne	SHORT $LL3@pmmngr_all@2
	mov	eax, DWORD PTR _frame$[esp+12]
	pop	edi
	pop	esi
	pop	ebx
$LN1@pmmngr_all@2:

; 223  : 
; 224  : 	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
; 225  : 	_mmngr_used_blocks += size;

	add	DWORD PTR __mmngr_used_blocks, ebp
	shl	eax, 12					; 0000000cH
	pop	ebp

; 226  : 
; 227  : 	return (void*)addr;
; 228  : }

	ret	0
?pmmngr_alloc_blocks@@YAPAXI@Z ENDP			; pmmngr_alloc_blocks
_TEXT	ENDS
PUBLIC	?pmmngr_alloc_blocks@@YAPAXII@Z			; pmmngr_alloc_blocks
; Function compile flags: /Ogtpy
;	COMDAT ?pmmngr_alloc_blocks@@YAPAXII@Z
_TEXT	SEGMENT
_frame$ = 8						; size = 4
_size$ = 8						; size = 4
_align$ = 12						; size = 4
?pmmngr_alloc_blocks@@YAPAXII@Z PROC			; pmmngr_alloc_blocks, COMDAT

; 232  : 	if(pmmngr_get_free_block_count() <= size)

	mov	eax, DWORD PTR __mmngr_max_blocks
	sub	eax, DWORD PTR __mmngr_used_blocks
	push	ebp
	mov	ebp, DWORD PTR _size$[esp]
	cmp	eax, ebp
	ja	SHORT $LN5@pmmngr_all@3
$LN15@pmmngr_all@3:

; 233  : 		return 0;

	xor	eax, eax
	pop	ebp

; 245  : 
; 246  : 	return (void*)addr;
; 247  : }

	ret	0
$LN5@pmmngr_all@3:

; 234  : 
; 235  : 	int frame = mmap_first_free_s(size,align);

	mov	ecx, DWORD PTR _align$[esp]
	push	ecx
	push	ebp
	call	?mmap_first_free_s@@YAHII@Z		; mmap_first_free_s
	add	esp, 8
	mov	DWORD PTR _frame$[esp], eax

; 236  : 
; 237  : 	if(frame == -1)

	cmp	eax, -1

; 238  : 		return 0;

	je	SHORT $LN15@pmmngr_all@3

; 239  : 
; 240  : 	for(uint32_t i = 0;i < size;i++)

	test	ebp, ebp
	jbe	SHORT $LN1@pmmngr_all@3
	push	ebx
	mov	ebx, DWORD PTR __mmngr_memory_map
	push	esi
	push	edi
	mov	esi, eax
	mov	edi, ebp
$LL3@pmmngr_all@3:

; 241  : 		mmap_set(frame + i);

	mov	eax, esi
	cdq
	and	edx, 31					; 0000001fH
	add	eax, edx
	sar	eax, 5
	mov	ecx, esi
	and	ecx, -2147483617			; 8000001fH
	lea	eax, DWORD PTR [ebx+eax*4]
	jns	SHORT $LN14@pmmngr_all@3
	dec	ecx
	or	ecx, -32				; ffffffe0H
	inc	ecx
$LN14@pmmngr_all@3:
	mov	edx, 1
	shl	edx, cl
	inc	esi
	or	DWORD PTR [eax], edx
	sub	edi, 1
	jne	SHORT $LL3@pmmngr_all@3
	mov	eax, DWORD PTR _frame$[esp+12]
	pop	edi
	pop	esi
	pop	ebx
$LN1@pmmngr_all@3:

; 242  : 
; 243  : 	physical_addr addr = frame * PMMNGR_BLOCK_SIZE;
; 244  : 	_mmngr_used_blocks += size;

	add	DWORD PTR __mmngr_used_blocks, ebp
	shl	eax, 12					; 0000000cH
	pop	ebp

; 245  : 
; 246  : 	return (void*)addr;
; 247  : }

	ret	0
?pmmngr_alloc_blocks@@YAPAXII@Z ENDP			; pmmngr_alloc_blocks
_TEXT	ENDS
PUBLIC	?pmalloc@@YAPAXI@Z				; pmalloc
; Function compile flags: /Ogtpy
;	COMDAT ?pmalloc@@YAPAXI@Z
_TEXT	SEGMENT
_size$ = 8						; size = 4
?pmalloc@@YAPAXI@Z PROC					; pmalloc, COMDAT

; 351  : 	size_t blocks = size / PMMNGR_BLOCK_SIZE;

	mov	ecx, DWORD PTR _size$[esp-4]
	mov	eax, ecx
	shr	eax, 12					; 0000000cH

; 352  : 	if(size % PMMNGR_BLOCK_SIZE != 0)

	test	ecx, 4095				; 00000fffH
	je	SHORT $LN1@pmalloc

; 353  : 		blocks++;

	inc	eax
$LN1@pmalloc:

; 354  : 
; 355  : 	return pmmngr_alloc_blocks(blocks);

	mov	DWORD PTR _size$[esp-4], eax
	jmp	?pmmngr_alloc_blocks@@YAPAXI@Z		; pmmngr_alloc_blocks
?pmalloc@@YAPAXI@Z ENDP					; pmalloc
_TEXT	ENDS
END
