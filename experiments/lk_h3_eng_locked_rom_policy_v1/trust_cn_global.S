// Exact original ENG callback range: payload 0xc5888..0xc595f (216 bytes).
// Keep original 2048-bit key; additionally trust only the stock H3 CN/GL
// 4096-bit moduli by their full SHA256 digests. Never force unlocked state.
.text
.global _start
.set code_base, 0xc5888
_start:
.Lbase:
    paciasp
    stp x29, x30, [sp, #-80]!
    stp x20, x19, [sp, #16]
    mov x19, x5
    mov w0, #2
    cbz x19, done
    cbz x1, done
    strb wzr, [x19]
    cmp x2, #8
    b.lo done
    ldr w8, [x1]
    rev w8, w8
    mov x20, x1
    cmp w8, #2048
    b.eq key_2048
    cmp w8, #4096
    b.ne untrusted
    cmp x2, #1032
    b.ne invalid
    add x0, x20, #8
    mov w1, #512
    add x2, sp, #32
    bl .Lbase + (0xa4b20 - code_base)
    cbnz w0, untrusted
    add x0, sp, #32
    adr x1, .Lbase + (0x68ec0 - code_base)
    mov w2, #32
    bl .Lbase + (0x7f450 - code_base)
    cbz w0, trusted
    add x0, sp, #32
    adr x1, .Lbase + (0x68ee0 - code_base)
    mov w2, #32
    bl .Lbase + (0x7f450 - code_base)
    cbnz w0, untrusted
    b trusted
key_2048:
    cmp x2, #520
    b.ne invalid
    add x0, x20, #8
    adr x1, .Lbase + (0x130728 - code_base)
    mov w2, #256
    bl .Lbase + (0x7f450 - code_base)
    cbnz w0, untrusted
trusted:
    mov w8, #1
    strb w8, [x19]
untrusted:
    mov w0, #0
done:
    ldp x20, x19, [sp, #16]
    ldp x29, x30, [sp], #80
    autiasp
    ret
invalid:
    mov w0, #2
    b done
    .fill (0xd8 - (. - .Lbase)) / 4, 4, 0xd503201f
