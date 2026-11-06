// H4 AVB callback. Keep stored lock policy; trust CN4096 or H3 ENG2048.
.text
.global _start
.set code_base,0xc5978
_start:
.Lbase:
    paciasp
    stp x29,x30,[sp,#-32]!
    stp x20,x19,[sp,#16]
    mov x19,x5
    mov w0,#2
    cbz x19,.Ldone
    cbz x1,.Ldone
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
    add x0,x1,#8
    adr x1,.Lbase + (0x138658-code_base)
    mov w2,#512
    b .Lcompare
.Leng:
    cmp x2,#520
    b.ne .Linvalid
    add x0,x1,#8
    adr x1,.Leng_modulus
    mov w2,#256
.Lcompare:
    bl .Lbase + (0x7eae8-code_base)
    cbnz w0,.Luntrusted
    mov w8,#1
    strb w8,[x19]
.Luntrusted:
    mov w0,#0
.Ldone:
    ldp x20,x19,[sp,#16]
    ldp x29,x30,[sp],#32
    autiasp
    ret
.Linvalid:
    mov w0,#2
    b .Ldone
.Leng_modulus:
    .incbin "eng_modulus.bin"
    .fill (0x1a0-(.-.Lbase))/4,4,0xd503201f
