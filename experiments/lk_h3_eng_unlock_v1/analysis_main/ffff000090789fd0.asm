ffff000090789fd0	paciasp
ffff000090789fd4	sub sp,sp,#0x140
ffff000090789fd8	stp x29,x30,[sp, #0x110]
ffff000090789fdc	stp x28,x21,[sp, #0x120]
ffff000090789fe0	stp x20,x19,[sp, #0x130]
ffff000090789fe4	add x29,sp,#0x110
ffff000090789fe8	mov w20,w2
ffff000090789fec	mov x21,x1
ffff000090789ff0	mov w19,w0
ffff000090789ff4	cmp w2,#0x101
ffff000090789ff8	stur wzr,[x29, #-0x4]
ffff000090789ffc	stp xzr,xzr,[sp, #0xf8]
ffff00009078a000	stp xzr,xzr,[sp, #0xe8]
ffff00009078a004	stp xzr,xzr,[sp, #0xd8]
ffff00009078a008	stp xzr,xzr,[sp, #0xc8]
ffff00009078a00c	stp xzr,xzr,[sp, #0xb8]
ffff00009078a010	stp xzr,xzr,[sp, #0xa8]
ffff00009078a014	stp xzr,xzr,[sp, #0x98]
ffff00009078a018	stp xzr,xzr,[sp, #0x88]
ffff00009078a01c	stp xzr,xzr,[sp, #0x78]
ffff00009078a020	stp xzr,xzr,[sp, #0x68]
ffff00009078a024	stp xzr,xzr,[sp, #0x58]
ffff00009078a028	stp xzr,xzr,[sp, #0x48]
ffff00009078a02c	stp xzr,xzr,[sp, #0x38]
ffff00009078a030	stp xzr,xzr,[sp, #0x28]
ffff00009078a034	stp xzr,xzr,[sp, #0x18]
ffff00009078a038	stp xzr,xzr,[sp, #0x8]
ffff00009078a03c	b.cc 0xffff00009078a060
ffff00009078a040	and w8,w20,#0xff
ffff00009078a044	cbz w8,0xffff00009078a060
ffff00009078a048	adrp x0,-0xffff6f80d000
ffff00009078a04c	add x0,x0,#0x79e
ffff00009078a050	mov w1,w20
ffff00009078a054	bl 0xffff00009077ea84
ffff00009078a058	mov w0,#0x1
ffff00009078a05c	b 0xffff00009078a0e8
ffff00009078a060	add x1,sp,#0x8
ffff00009078a064	mov w0,w19
ffff00009078a068	mov w2,#0x100
ffff00009078a06c	bl 0xffff000090789db0
ffff00009078a070	cbnz w0,0xffff00009078a0e8
ffff00009078a074	mov w2,w20
ffff00009078a078	add x0,sp,#0x8
ffff00009078a07c	mov x1,x21
ffff00009078a080	bl 0xffff00009077f488
ffff00009078a084	bl 0xffff00009073585c
ffff00009078a088	cmp w0,#0x2
ffff00009078a08c	b.ne 0xffff00009078a0b8
ffff00009078a090	add x2,sp,#0x8
ffff00009078a094	sub x4,x29,#0x4
ffff00009078a098	mov w0,wzr
ffff00009078a09c	mov w1,w19
ffff00009078a0a0	mov w3,#0x100
ffff00009078a0a4	bl 0xffff0000907961ac
ffff00009078a0a8	cmp w0,#0x7
ffff00009078a0ac	b.ne 0xffff00009078a0c0
ffff00009078a0b0	mov w0,#0x6
ffff00009078a0b4	b 0xffff00009078a0e8
ffff00009078a0b8	mov w0,#0x5
ffff00009078a0bc	b 0xffff00009078a0e8
ffff00009078a0c0	ldur w8,[x29, #-0x4]
ffff00009078a0c4	mov w1,w0
ffff00009078a0c8	orr w8,w0,w8
ffff00009078a0cc	cbz w8,0xffff00009078a0e4
ffff00009078a0d0	adrp x0,-0xffff6f81b000
ffff00009078a0d4	add x0,x0,#0xed4
ffff00009078a0d8	bl 0xffff00009077ea84
ffff00009078a0dc	mov w0,#0x4
ffff00009078a0e0	b 0xffff00009078a0e8
ffff00009078a0e4	mov w0,wzr
ffff00009078a0e8	ldp x20,x19,[sp, #0x130]
ffff00009078a0ec	ldp x28,x21,[sp, #0x120]
ffff00009078a0f0	ldp x29,x30,[sp, #0x110]
ffff00009078a0f4	add sp,sp,#0x140
ffff00009078a0f8	autiasp
ffff00009078a0fc	ret
