0026fd20	sub sp,sp,#0x50
0026fd24	stp x29,x30,[sp, #0x20]
0026fd28	stp x22,x21,[sp, #0x30]
0026fd2c	stp x20,x19,[sp, #0x40]
0026fd30	add x29,sp,#0x20
0026fd34	mrs x22,tpidr_el0
0026fd38	mov w10,#0x632f
0026fd3c	mov x20,x1
0026fd40	mov x19,x0
0026fd44	ldr x8,[x22, #0x28]
0026fd48	mov w9,#0xc
0026fd4c	movk w10,#0x6361, LSL #16
0026fd50	mov w11,#0x6568
0026fd54	stur x8,[x29, #-0x8]
0026fd58	strb w9,[sp]
0026fd5c	stur w10,[sp, #0x1]
0026fd60	sturh w11,[sp, #0x5]
0026fd64	strb wzr,[sp, #0x7]
0026fd68	mov x0,sp
0026fd6c	bl 0x00393de0
0026fd70	mov x21,x0
0026fd74	ldrb w8,[sp]
0026fd78	tbz w8,#0x0,0x0026fd84
0026fd7c	ldr x0,[sp, #0x10]
0026fd80	bl 0x0041f730
0026fd84	cbz x21,0x0026fe2c
0026fd88	ldr x0,[x20, #0x20]
0026fd8c	cbz x0,0x0026fda0
0026fd90	ldr x8,[x0]
0026fd94	ldr x8,[x8, #0x30]
0026fd98	blr x8
0026fd9c	tbz w0,#0x0,0x0026fe44
0026fda0	ldr x8,[x19]
0026fda4	adrp x1,0x148000
0026fda8	add x1,x1,#0x671
0026fdac	mov x0,x19
0026fdb0	ldr x8,[x8, #0x158]
0026fdb4	blr x8
0026fdb8	ldr x8,[x19]
0026fdbc	mov x0,x19
0026fdc0	mov w1,#0x3
0026fdc4	ldr x8,[x8, #0x118]
0026fdc8	blr x8
0026fdcc	ldr x8,[x19]
0026fdd0	mov x0,x19
0026fdd4	mov w1,#0x1
0026fdd8	ldr x8,[x8, #0x128]
0026fddc	blr x8
0026fde0	adrp x0,0x14a000
0026fde4	add x0,x0,#0x9a8
0026fde8	mov x1,x19
0026fdec	mov w2,wzr
0026fdf0	bl 0x0026f440
0026fdf4	ldr x8,[x19]
0026fdf8	adrp x9,0x145000
0026fdfc	add x9,x9,#0xcf2
0026fe00	adrp x10,0x142000
0026fe04	add x10,x10,#0x63c
0026fe08	tst w0,#0x1
0026fe0c	mov w20,w0
0026fe10	csel x2,x10,x9,ne
0026fe14	ldr x8,[x8, #0x158]
0026fe18	adrp x1,0x146000
0026fe1c	add x1,x1,#0xaf5
0026fe20	mov x0,x19
0026fe24	blr x8
0026fe28	b 0x0026fe48
0026fe2c	ldr x8,[x19]
0026fe30	adrp x1,0x158000
0026fe34	add x1,x1,#0x620
0026fe38	mov x0,x19
0026fe3c	ldr x8,[x8, #0x158]
0026fe40	blr x8
0026fe44	mov w20,wzr
0026fe48	ldr x8,[x22, #0x28]
0026fe4c	ldur x9,[x29, #-0x8]
0026fe50	cmp x8,x9
0026fe54	b.ne 0x0026fe9c
0026fe58	and w0,w20,#0x1
0026fe5c	ldp x20,x19,[sp, #0x40]
0026fe60	ldp x22,x21,[sp, #0x30]
0026fe64	ldp x29,x30,[sp, #0x20]
0026fe68	add sp,sp,#0x50
0026fe6c	ret
0026fe9c	bl 0x004217e0
