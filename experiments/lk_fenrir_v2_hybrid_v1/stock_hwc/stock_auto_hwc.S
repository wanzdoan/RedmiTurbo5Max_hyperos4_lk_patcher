// Preserve original early hardware-cache copy before other bootarg consumers.
// No new frame; late entry runs after the existing countrycode read.
.text
.global _start
.set code_base,0x13a78
_start:
.Lbase:
    add x0,sp,#0xb0
    bl .Lbase + (0x34f8-code_base)
    str wzr,[x29,#0x1c]
    b .Lbase + (0x13b00-code_base)
.Llate:
    add x3,sp,#0xd4
    ldrb w8,[sp]
    cbz w8,.Lformat
    ldr w8,[sp]
    and w8,w8,#0xffffff
    orr w8,w8,#0x20
    orr w8,w8,#0x2000
    mov w9,#0x6e63
    cmp w8,w9
    adr x3,.Lbase + (0xd36b1-code_base)
    adr x9,.Lglobal
    csel x3,x3,x9,eq
.Lformat:
    add x0,sp,#0x110
    mov w1,#200
    adr x2,.Lbase + (0xfcaaa-code_base)
    add x4,sp,#0xb0
    add x5,sp,#0xc0
    bl .Lbase + (0x7f434-code_base)
    tbnz w0,#31,.Lerror
    add x0,sp,#0x110
    bl .Lbase + (0x18f34-code_base)
.Lcontinue:
    add x19,sp,#0xc0
    add x0,sp,#0x110
    b .Lbase + (0x140b4-code_base)
.Lerror:
    adr x0,.Lbase + (0xd74b7-code_base)
    bl .Lbase + (0x7de54-code_base)
    b .Lcontinue
.Lglobal:
    .asciz "GLOBAL"
    .byte 0
    .fill (0x88-(.-.Lbase))/4,4,0xd503201f
