.text
.global _start
.set code_base,0x13b70
_start:
.Lbase:
    add x0,sp,#0x100
    adr x1,.Lbase+(0x1121d7-code_base)
    mov w2,#256
    bl .Lbase+(0x7eae8-code_base)
    cbz w0,.Ltrusted
    add x0,sp,#0x100
    adr x1,.Lbase+(0x1122d7-code_base)
    mov w2,#256
    bl .Lbase+(0x7eae8-code_base)
    cmp w0,#0
    csetm w20,ne
    b .Lbase+(0x88854-code_base)
.Ltrusted:
    mov w20,#0
    b .Lbase+(0x88854-code_base)
    .fill (0x54-(.-.Lbase))/4,4,0xd503201f
