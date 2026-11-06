.text
.global _start
.set code_base,0xc5978
_start:
.Lbase:
    paciasp
    stp x29,x30,[sp,#-64]!
    stp x20,x19,[sp,#16]
    mov x19,x5
    mov w0,#2
    cbz x19,.Ldone
    cbz x1,.Ldone
    mov x20,x1
    strb wzr,[x19]
    cmp x2,#8
    b.lo .Ldone
    ldr w8,[x1]
    rev w8,w8
    cmp w8,#2048
    b.eq .Leng
    cmp w8,#4096
    b.ne .Luntrusted
    cmp x2,#1032
    b.ne .Linvalid
    mov w1,#512
    b .Lhash
.Leng:
    cmp x2,#520
    b.ne .Linvalid
    mov w1,#256
.Lhash:
    add x0,x20,#8
    add x2,sp,#32
    bl .Lbase+(0xa4810-code_base)
    cbnz w0,.Luntrusted
    adr x20,.Lhashes
.Lloop:
    add x0,sp,#32
    mov x1,x20
    mov w2,#32
    bl .Lbase+(0x7eae8-code_base)
    cbz w0,.Ltrusted
    add x20,x20,#32
    adr x8,.Lhashes_end
    cmp x20,x8
    b.ne .Lloop
.Luntrusted:
    mov w0,#0
    b .Ldone
.Ltrusted:
    mov w8,#1
    strb w8,[x19]
    mov w0,#0
.Ldone:
    ldp x20,x19,[sp,#16]
    ldp x29,x30,[sp],#64
    autiasp
    ret
.Linvalid:
    mov w0,#2
    b .Ldone
.Lhashes:
    .incbin "key_hashes.bin"
.Lhashes_end:
    .org 0x128
.Lhwc:
    add x3,sp,#0xd4
    ldrb w8,[sp]
    cbz w8,.Lformat
    ldr w8,[sp]
    and w8,w8,#0xffffff
    orr w8,w8,#0x20
    orr w8,w8,#0x2000
    mov w9,#0x6e63
    cmp w8,w9
    adr x3,.Lbase+(0xd36ee-code_base)
    adr x9,.Lglobal
    csel x3,x3,x9,eq
.Lformat:
    add x0,sp,#0x110
    mov w1,#200
    adr x2,.Lbase+(0xfcc52-code_base)
    add x4,sp,#0xb0
    add x5,sp,#0xc0
    bl .Lbase+(0x7f6fc-code_base)
    tbnz w0,#31,.Lerror
    add x0,sp,#0x110
    bl .Lbase+(0x19078-code_base)
.Lcontinue:
    add x0,sp,#0x110
    b .Lbase+(0x14170-code_base)
.Lerror:
    adr x0,.Lbase+(0xd7546-code_base)
    adr x1,.Lbase+(0xe261c-code_base)
    mov w2,#0x102
    bl .Lbase+(0x7e11c-code_base)
    b .Lcontinue
.Lglobal:
    .asciz "GLOBAL"
    .byte 0
    .org 0x1a0
