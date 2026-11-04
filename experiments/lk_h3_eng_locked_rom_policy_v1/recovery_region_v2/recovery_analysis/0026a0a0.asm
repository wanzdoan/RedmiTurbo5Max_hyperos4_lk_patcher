0026a0a0	sub sp,sp,#0x60
0026a0a4	stp x29,x30,[sp, #0x20]
0026a0a8	stp x24,x23,[sp, #0x30]
0026a0ac	stp x22,x21,[sp, #0x40]
0026a0b0	stp x20,x19,[sp, #0x50]
0026a0b4	add x29,sp,#0x20
0026a0b8	mrs x24,tpidr_el0
0026a0bc	mov x22,x0
0026a0c0	ldr x9,[x24, #0x28]
0026a0c4	mov w0,#0x28
0026a0c8	mov x21,x1
0026a0cc	mov x19,x8
0026a0d0	stur x9,[x29, #-0x8]
0026a0d4	bl 0x0041f720
0026a0d8	movi v0.2D,#0x0
0026a0dc	mov x20,x0
0026a0e0	str xzr,[x0, #0x20]
0026a0e4	str x0,[sp, #0x8]
0026a0e8	stp q0,q0,[x0]
0026a0ec	mov x1,x22
0026a0f0	bl 0x00404390
0026a0f4	tbz w0,#0x0,0x0026a13c
0026a0f8	mov w0,#0x90
0026a0fc	bl 0x0041f720
0026a100	mov x23,x0
0026a104	stp xzr,x20,[sp, #0x8]
0026a108	add x2,sp,#0x10
0026a10c	mov x1,x22
0026a110	mov x3,x21
0026a114	bl 0x0026a600
0026a118	ldr x20,[sp, #0x10]
0026a11c	str xzr,[sp, #0x10]
0026a120	cbz x20,0x0026a134
0026a124	mov x0,x20
0026a128	bl 0x00404760
0026a12c	mov x0,x20
0026a130	bl 0x0041f730
0026a134	str x23,[x19]
0026a138	b 0x0026a160
0026a13c	mov w0,#0x4
0026a140	mov x1,xzr
0026a144	bl 0x0040d370
0026a148	tbnz w0,#0x0,0x0026a188
0026a14c	mov x0,x20
0026a150	str xzr,[x19]
0026a154	bl 0x00404760
0026a158	mov x0,x20
0026a15c	bl 0x0041f730
0026a160	ldr x8,[x24, #0x28]
0026a164	ldur x9,[x29, #-0x8]
0026a168	cmp x8,x9
0026a16c	b.ne 0x0026a240
0026a170	ldp x20,x19,[sp, #0x50]
0026a174	ldp x22,x21,[sp, #0x40]
0026a178	ldp x24,x23,[sp, #0x30]
0026a17c	ldp x29,x30,[sp, #0x20]
0026a180	add sp,sp,#0x60
0026a184	ret
0026a188	bl 0x0041f880
0026a18c	mov x21,x0
0026a190	ldr w22,[x0]
0026a194	adrp x1,0x14b000
0026a198	add x1,x1,#0xc9b
0026a19c	add x0,sp,#0x10
0026a1a0	mov w2,#0x7f
0026a1a4	mov w3,#0x4
0026a1a8	mov x4,xzr
0026a1ac	mov w5,#0xffffffff
0026a1b0	bl 0x0040d500
0026a1b4	add x0,sp,#0x10
0026a1b8	bl 0x0040d3e0
0026a1bc	adrp x1,0x14e000
0026a1c0	add x1,x1,#0x1ce
0026a1c4	mov w2,#0x12
0026a1c8	bl 0x00226b10
0026a1cc	add x0,sp,#0x10
0026a1d0	bl 0x0040d610
0026a1d4	str w22,[x21]
0026a1d8	b 0x0026a14c
0026a240	bl 0x004217e0
