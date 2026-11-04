ffff000090709bc8	paciasp
ffff000090709bcc	stp x29,x30,[sp, #-0x30]!
ffff000090709bd0	str x21,[sp, #0x10]
ffff000090709bd4	stp x20,x19,[sp, #0x20]
ffff000090709bd8	mov x29,sp
ffff000090709bdc	mov x20,x0
ffff000090709be0	adrp x21,-0xffff6f7cb000
ffff000090709be4	add x21,x21,#0x168
ffff000090709be8	adrp x19,-0xffff6f829000
ffff000090709bec	add x19,x19,#0x88e
ffff000090709bf0	ldr x21,[x21]
ffff000090709bf4	cbz x21,0xffff000090709c0c
ffff000090709bf8	ldr x1,[x21, #0x8]
ffff000090709bfc	mov x0,x20
ffff000090709c00	bl 0xffff00009077f990
ffff000090709c04	cbnz w0,0xffff000090709bf0
ffff000090709c08	ldr x19,[x21, #0x10]
ffff000090709c0c	mov x0,x19
ffff000090709c10	ldr x21,[sp, #0x10]
ffff000090709c14	ldp x20,x19,[sp, #0x20]
ffff000090709c18	ldp x29,x30,[sp], #0x30
ffff000090709c1c	autiasp
ffff000090709c20	ret
