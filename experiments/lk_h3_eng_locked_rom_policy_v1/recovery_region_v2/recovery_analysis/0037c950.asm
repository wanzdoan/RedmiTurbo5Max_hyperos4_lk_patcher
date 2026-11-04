0037c950	stp x29,x30,[sp, #-0x40]!
0037c954	stp x28,x23,[sp, #0x10]
0037c958	stp x22,x21,[sp, #0x20]
0037c95c	stp x20,x19,[sp, #0x30]
0037c960	mov x29,sp
0037c964	sub sp,sp,#0x340
0037c968	mrs x22,tpidr_el0
0037c96c	ldr x8,[x22, #0x28]
0037c970	stur x8,[x29, #-0x8]
0037c974	cbz x0,0x0037cb04
0037c978	mov x19,x1
0037c97c	add x1,sp,#0x10
0037c980	str xzr,[sp, #0x10]
0037c984	bl 0x0037c390
0037c988	tbnz w0,#0x1f,0x0037cb18
0037c98c	adrp x0,0x159000
0037c990	add x0,x0,#0x6df
0037c994	sub x20,x29,#0xd0
0037c998	add x21,sp,#0x180
0037c99c	add x23,sp,#0x90
0037c9a0	bl 0x0041fbd0
0037c9a4	movi v0.2D,#0x0
0037c9a8	mov w9,#0x31
0037c9ac	adrp x1,0x147000
0037c9b0	add x1,x1,#0xcf5
0037c9b4	sub x2,x29,#0x70
0037c9b8	add x3,sp,#0x210
0037c9bc	add x4,sp,#0x180
0037c9c0	add x5,sp,#0x1e
0037c9c4	stur q0,[x20, #0xac]
0037c9c8	add x6,sp,#0x20
0037c9cc	stp q0,q0,[x20, #0x10]
0037c9d0	stp q0,q0,[x20, #0x70]
0037c9d4	stp q0,q0,[x20, #0x90]
0037c9d8	str q0,[x20, #0x60]
0037c9dc	stur q0,[x20, #0x4c]
0037c9e0	stp q0,q0,[x20, #0x30]
0037c9e4	str q0,[x20]
0037c9e8	ldr x20,[sp, #0x10]
0037c9ec	strh w9,[sp, #0x1e]
0037c9f0	stur q0,[x21, #0xdd]
0037c9f4	mov x0,x20
0037c9f8	stp q0,q0,[x21, #0x40]
0037c9fc	stp q0,q0,[x21, #0xa0]
0037ca00	stp q0,q0,[x21, #0xc0]
0037ca04	str q0,[x21, #0x90]
0037ca08	stur q0,[x21, #0x7d]
0037ca0c	stp q0,q0,[x21, #0x60]
0037ca10	str q0,[x21, #0x30]
0037ca14	stur q0,[x21, #0x19]
0037ca18	stp q0,q0,[x21]
0037ca1c	stur q0,[x23, #0xd9]
0037ca20	strh w9,[sp, #0x1c]
0037ca24	stur q0,[x23, #0xac]
0037ca28	stp q0,q0,[x23, #0xc0]
0037ca2c	str q0,[x23, #0x60]
0037ca30	stur q0,[x23, #0x4c]
0037ca34	stp q0,q0,[x23, #0x10]
0037ca38	stp q0,q0,[x23, #0x30]
0037ca3c	stp q0,q0,[x23, #0x70]
0037ca40	stp q0,q0,[x23, #0x90]
0037ca44	str q0,[x23]
0037ca48	str wzr,[sp, #0x80]
0037ca4c	stp q0,q0,[sp, #0x20]
0037ca50	stp q0,q0,[sp, #0x40]
0037ca54	stp q0,q0,[sp, #0x60]
0037ca58	bl 0x0041fc10
0037ca5c	mov x0,x20
0037ca60	bl 0x0041f920
0037ca64	sub x0,x29,#0xd0
0037ca68	bl 0x0037c6c0
0037ca6c	tbnz w0,#0x1f,0x0037cb0c
0037ca70	adrp x0,0x152000
0037ca74	add x0,x0,#0x960
0037ca78	bl 0x0041fbd0
0037ca7c	adrp x0,0x15a000
0037ca80	add x0,x0,#0x8d2
0037ca84	add x1,sp,#0x1b0
0037ca88	adrp x2,0x148000
0037ca8c	add x2,x2,#0x27f
0037ca90	bl 0x002a8870
0037ca94	mov x9,#0x6e75
0037ca98	ldr x8,[sp, #0x1b0]
0037ca9c	movk x9,#0x6e6b, LSL #16
0037caa0	movk x9,#0x776f, LSL #32
0037caa4	movk x9,#0x6e, LSL #48
0037caa8	cmp x8,x9
0037caac	b.eq 0x0037cb0c
0037cab0	sub x20,x29,#0x70
0037cab4	sub x21,x29,#0xd0
0037cab8	adrp x0,0x143000
0037cabc	add x0,x0,#0x7c7
0037cac0	add x1,sp,#0x210
0037cac4	add x2,sp,#0x1b0
0037cac8	add x5,sp,#0x1c
0037cacc	mov x3,x20
0037cad0	mov x4,x21
0037cad4	bl 0x0041f6a0
0037cad8	mov x0,x20
0037cadc	mov x1,x21
0037cae0	bl 0x0041f6b0
0037cae4	cbnz w0,0x0037caf8
0037cae8	add x0,sp,#0x210
0037caec	add x1,sp,#0x1b0
0037caf0	bl 0x0041f6b0
0037caf4	cbz w0,0x0037cb50
0037caf8	adrp x0,0x14a000
0037cafc	add x0,x0,#0x4a0
0037cb00	b 0x0037cb20
0037cb04	mov w0,#0x1
0037cb08	b 0x0037cb28
0037cb0c	adrp x0,0x152000
0037cb10	add x0,x0,#0x977
0037cb14	b 0x0037cb20
0037cb18	adrp x0,0x157000
0037cb1c	add x0,x0,#0xfae
0037cb20	bl 0x0041fbd0
0037cb24	mov w0,#0xffffffff
0037cb28	ldr x8,[x22, #0x28]
0037cb2c	ldur x9,[x29, #-0x8]
0037cb30	cmp x8,x9
0037cb34	b.ne 0x0037cd2c
0037cb38	add sp,sp,#0x340
0037cb3c	ldp x20,x19,[sp, #0x30]
0037cb40	ldp x22,x21,[sp, #0x20]
0037cb44	ldp x28,x23,[sp, #0x10]
0037cb48	ldp x29,x30,[sp], #0x40
0037cb4c	ret
0037cb50	mov x10,#0x227b
0037cb54	ldr x8,[sp, #0x20]
0037cb58	movk x10,#0x6556, LSL #16
0037cb5c	ldrh w9,[sp, #0x28]
0037cb60	movk x10,#0x6972, LSL #32
0037cb64	movk x10,#0x7966, LSL #48
0037cb68	cmp x8,x10
0037cb6c	mov w8,#0x3a22
0037cb70	ccmp w9,w8,#0x0,eq
0037cb74	b.eq 0x0037cba8
0037cb78	mov x9,#0x227b
0037cb7c	ldr x8,[sp, #0x20]
0037cb80	movk x9,#0x6f5a, LSL #16
0037cb84	movk x9,#0x656e, LSL #32
0037cb88	movk x9,#0x3a22, LSL #48
0037cb8c	cmp x8,x9
0037cb90	b.eq 0x0037cbc4
0037cb94	ldrb w8,[sp, #0x20]
0037cb98	cbz w8,0x0037cbd8
0037cb9c	adrp x0,0x15b000
0037cba0	add x0,x0,#0xdfd
0037cba4	b 0x0037cb20
0037cba8	add x0,sp,#0x20
0037cbac	add x2,sp,#0x1c
0037cbb0	add x3,sp,#0xf0
0037cbb4	adrp x1,0x142000
0037cbb8	add x1,x1,#0x99b
0037cbbc	bl 0x0041fc10
0037cbc0	b 0x0037cbd8
0037cbc4	add x0,sp,#0x20
0037cbc8	add x2,sp,#0xf0
0037cbcc	adrp x1,0x14a000
0037cbd0	add x1,x1,#0x491
0037cbd4	bl 0x0041fc10
0037cbd8	add x0,sp,#0x1c
0037cbdc	bl 0x0041f6c0
0037cbe0	cbz w0,0x0037cb28
0037cbe4	mov w21,w0
0037cbe8	add x0,sp,#0x90
0037cbec	bl 0x0037c660
0037cbf0	tbz w0,#0x1f,0x0037cc08
0037cbf4	ldrb w8,[sp, #0xf0]
0037cbf8	cbz w8,0x0037cc08
0037cbfc	adrp x0,0x153000
0037cc00	add x0,x0,#0x7bd
0037cc04	b 0x0037cb20
0037cc08	cbz w0,0x0037cc88
0037cc0c	adrp x0,0x57b000
0037cc10	add x0,x0,#0xab8
0037cc14	bl 0x004207b0
0037cc18	cbz w0,0x0037cca4
0037cc1c	mov x1,xzr
0037cc20	mov w0,#0x4
0037cc24	bl 0x0040d370
0037cc28	tbz w0,#0x0,0x0037cb24
0037cc2c	bl 0x0041f880
0037cc30	ldr w19,[x0]
0037cc34	mov x20,x0
0037cc38	mov x4,xzr
0037cc3c	add x0,sp,#0x8
0037cc40	adrp x1,0x153000
0037cc44	add x1,x1,#0x790
0037cc48	mov w2,#0x17b
0037cc4c	mov w3,#0x4
0037cc50	mov w5,#0xffffffff
0037cc54	bl 0x0040d500
0037cc58	add x0,sp,#0x8
0037cc5c	bl 0x0040d3e0
0037cc60	mov w8,#0x41
0037cc64	adrp x1,0x15b000
0037cc68	add x1,x1,#0x33
0037cc6c	mov w2,w8
0037cc70	bl 0x00226b10
0037cc74	add x0,sp,#0x8
0037cc78	bl 0x0040d610
0037cc7c	mov w0,#0xffffffff
0037cc80	str w19,[x20]
0037cc84	b 0x0037cb28
0037cc88	add x0,sp,#0x90
0037cc8c	add x1,sp,#0xf0
0037cc90	bl 0x0041f6b0
0037cc94	cbz w0,0x0037cc0c
0037cc98	adrp x0,0x147000
0037cc9c	add x0,x0,#0xd36
0037cca0	b 0x0037cb20
0037cca4	add x0,sp,#0x150
0037cca8	mov x1,x19
0037ccac	bl 0x0037c7b0
0037ccb0	tbnz w0,#0x1f,0x0037ccec
0037ccb4	add x19,sp,#0x180
0037ccb8	add x20,sp,#0x150
0037ccbc	adrp x0,0x144000
0037ccc0	add x0,x0,#0x4c4
0037ccc4	mov x1,x19
0037ccc8	mov x2,x20
0037cccc	bl 0x0041f6a0
0037ccd0	mov x0,x19
0037ccd4	mov x1,x20
0037ccd8	bl 0x0041f6b0
0037ccdc	cbz w0,0x0037ccf8
0037cce0	adrp x0,0x14a000
0037cce4	add x0,x0,#0x4a0
0037cce8	b 0x0037cb20
0037ccec	adrp x0,0x142000
0037ccf0	add x0,x0,#0x9d5
0037ccf4	b 0x0037cb20
0037ccf8	mov w0,w21
0037ccfc	b 0x0037cb28
0037cd2c	bl 0x004217e0
