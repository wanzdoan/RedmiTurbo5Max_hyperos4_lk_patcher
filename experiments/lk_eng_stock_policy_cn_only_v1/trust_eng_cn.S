// Replace only original ENG AVB key callback0xc5888..0xc595f.
// Preserve ENG2048; add China4096 only. No Global fingerprint or HWC patch.
.text
.global _start
.set code_base,0xc5888
_start:
.Lbase:
    paciasp
    stp x29,x30,[sp,#-64]!
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
    mov x20,x1
    cmp w8,#2048
    b.eq .Leng
    cmp w8,#4096
    b.ne .Luntrusted
    cmp x2,#1032
    b.ne .Linvalid
    add x0,x20,#8
    mov w1,#512
    add x2,sp,#32
    bl .Lbase + (0xa4b20-code_base)
    cbnz w0,.Luntrusted
    add x0,sp,#32
    adr x1,.Lcn_digest
    mov w2,#32
    b .Lcompare
.Leng:
    cmp x2,#520
    b.ne .Linvalid
    add x0,x20,#8
    adr x1,.Lbase + (0x130728-code_base)
    mov w2,#256
.Lcompare:
    bl .Lbase + (0x7f450-code_base)
    cbnz w0,.Luntrusted
    mov w8,#1
    strb w8,[x19]
.Luntrusted:
    mov w0,#0
.Ldone:
    ldp x20,x19,[sp,#16]
    ldp x29,x30,[sp],#64
    autiasp
    ret
.Linvalid:
    mov w0,#2
    b .Ldone
.Lcn_digest:
    .byte 0x8a,0x0e,0xca,0xf4,0x6f,0x05,0x71,0x82
    .byte 0xee,0xbf,0xd3,0xce,0x7f,0x7f,0xf0,0x4c
    .byte 0x7e,0x97,0xd9,0x89,0x8e,0x54,0xa0,0xdf
    .byte 0x20,0xed,0x3d,0x9a,0x0f,0x9b,0xef,0x1e
    .fill (0xd8-(.-.Lbase))/4,4,0xd503201f
