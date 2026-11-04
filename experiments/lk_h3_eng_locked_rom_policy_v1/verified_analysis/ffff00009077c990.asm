ffff00009077c990	paciasp
ffff00009077c994	stp x29,x30,[sp, #-0x30]!
ffff00009077c998	str x21,[sp, #0x10]
ffff00009077c99c	stp x20,x19,[sp, #0x20]
ffff00009077c9a0	mov x29,sp
ffff00009077c9a4	nop
ffff00009077c9a8	adr x21,-0xffff6f7d5768
ffff00009077c9ac	mov x20,x0
ffff00009077c9b0	add x0,x21,#0x10
ffff00009077c9b4	mov w1,#0xffffffff
ffff00009077c9b8	bl 0xffff000090716f9c
ffff00009077c9bc	ldr x19,[x21, #0x8]
ffff00009077c9c0	b 0xffff00009077c9c8
ffff00009077c9c4	ldr x19,[x19, #0x8]
ffff00009077c9c8	cmp x19,x21
ffff00009077c9cc	b.eq 0xffff00009077c9fc
ffff00009077c9d0	ldr x0,[x19, #0x40]
ffff00009077c9d4	cbz x0,0xffff00009077c9c4
ffff00009077c9d8	mov x1,x20
ffff00009077c9dc	bl 0xffff00009077f990
ffff00009077c9e0	cbnz w0,0xffff00009077c9c4
ffff00009077c9e4	add x8,x19,#0x10
ffff00009077c9e8	ldxr w9,[x8]
ffff00009077c9ec	add w9,w9,#0x1
ffff00009077c9f0	stxr w10,w9,[x8]
ffff00009077c9f4	cbnz w10,0xffff00009077c9e8
ffff00009077c9f8	b 0xffff00009077ca00
ffff00009077c9fc	mov x19,xzr
ffff00009077ca00	adrp x0,-0xffff6f7d6000
ffff00009077ca04	add x0,x0,#0x8a8
ffff00009077ca08	bl 0xffff000090717060
ffff00009077ca0c	mov x0,x19
ffff00009077ca10	ldr x21,[sp, #0x10]
ffff00009077ca14	ldp x20,x19,[sp, #0x20]
ffff00009077ca18	ldp x29,x30,[sp], #0x30
ffff00009077ca1c	autiasp
ffff00009077ca20	ret
