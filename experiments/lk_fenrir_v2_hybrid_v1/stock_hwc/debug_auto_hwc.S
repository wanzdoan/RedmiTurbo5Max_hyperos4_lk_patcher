// Preserve original early hardware-cache copy before other bootarg consumers.
// No new frame; late entry runs after the existing countrycode read.
.text
.global _start
.set code_base,0x13364
_start:
.Lbase:
    add x0,sp,#0x120
    bl .Lbase + (0x3530-code_base)
    add x0,sp,#0x180
    mov w1,wzr
    b .Lbase + (0x133ec-code_base)
.Llate:
    add x3,sp,#0x144
    ldrb w8,[sp]
    cbz w8,.Lformat
    ldr w8,[sp]
    and w8,w8,#0xffffff
    orr w8,w8,#0x20
    orr w8,w8,#0x2000
    mov w9,#0x6e63
    cmp w8,w9
    adr x3,.Lbase + (0xf34d4-code_base)
    adr x9,.Lglobal
    csel x3,x3,x9,eq
.Lformat:
    add x0,sp,#0x180
    mov w1,#200
    adr x2,.Lbase + (0xf5a3e-code_base)
    add x4,sp,#0x120
    add x5,sp,#0x130
    bl .Lbase + (0x7ffe0-code_base)
    tbnz w0,#31,.Lerror
    add x0,sp,#0x180
    bl .Lbase + (0x18ab8-code_base)
.Lcontinue:
    add x19,sp,#0x130
    add x0,sp,#0x180
    b .Lbase + (0x139c4-code_base)
.Lerror:
    adr x0,.Lbase + (0xcf78f-code_base)
    bl .Lbase + (0x7ea84-code_base)
    b .Lcontinue
.Lglobal:
    .asciz "GLOBAL"
    .byte 0
    .fill (0x88-(.-.Lbase))/4,4,0xd503201f
