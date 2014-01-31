[BITS 16]      ; 16 bit code generation
[ORG 0x7C00]   ; Origin location

; Main program
main:		; Label for the start of the main program

mov eax,10000h
add eax,40000h
sub eax,20000h

; End Matter
times 510-($-$$) db 0	; Fill the rest with zeros
dw 0xAA55		; Boot loader signature
