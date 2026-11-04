0037c070	stp x29,x30,[sp, #-0x40]!
0037c074	stp x28,x23,[sp, #0x10]
0037c078	stp x22,x21,[sp, #0x20]
0037c07c	stp x20,x19,[sp, #0x30]
0037c080	mov x29,sp
0037c084	sub sp,sp,#0x330
0037c088	mrs x21,tpidr_el0
0037c08c	mov x19,x1
0037c090	ldr x8,[x21, #0x28]
0037c094	mov x1,sp
0037c098	stur x8,[x29, #-0x8]
0037c09c	str xzr,[sp]
0037c0a0	bl 0x0037c390
0037c0a4	tbnz w0,#0x1f,0x0037c1e4
0037c0a8	adrp x0,0x146000
0037c0ac	add x0,x0,#0x282
0037c0b0	sub x20,x29,#0xd0
0037c0b4	add x22,sp,#0x170
0037c0b8	add x23,sp,#0x80
0037c0bc	bl 0x0041fbd0
0037c0c0	movi v0.2D,#0x0
0037c0c4	mov w8,#0x31
0037c0c8	adrp x1,0x147000
0037c0cc	add x1,x1,#0xcf5
0037c0d0	sub x2,x29,#0x70
0037c0d4	add x3,sp,#0x200
0037c0d8	add x4,sp,#0x170
0037c0dc	add x5,sp,#0xe
0037c0e0	stur q0,[x20, #0xac]
0037c0e4	add x6,sp,#0x10
0037c0e8	stur q0,[x20, #0x4c]
0037c0ec	ldr x20,[sp]
0037c0f0	stur q0,[x29, #-0x70]
0037c0f4	stp q0,q0,[x29, #-0xc0]
0037c0f8	mov x0,x20
0037c0fc	stp q0,q0,[x29, #-0xa0]
0037c100	stp q0,q0,[x29, #-0x60]
0037c104	stp q0,q0,[x29, #-0x40]
0037c108	stur q0,[x29, #-0xd0]
0037c10c	stur q0,[x22, #0xdd]
0037c110	stp q0,q0,[sp, #0x1b0]
0037c114	stp q0,q0,[sp, #0x210]
0037c118	str q0,[sp, #0x200]
0037c11c	stur q0,[x22, #0x7d]
0037c120	stp q0,q0,[sp, #0x230]
0037c124	str q0,[sp, #0x1a0]
0037c128	stur q0,[x22, #0x19]
0037c12c	stp q0,q0,[sp, #0x1d0]
0037c130	stur q0,[x23, #0xd9]
0037c134	stp q0,q0,[sp, #0x170]
0037c138	strh w8,[sp, #0xc]
0037c13c	stur q0,[x23, #0xac]
0037c140	stp q0,q0,[sp, #0x140]
0037c144	strh w8,[sp, #0xe]
0037c148	str q0,[sp, #0xe0]
0037c14c	str q0,[sp, #0x80]
0037c150	str wzr,[sp, #0x70]
0037c154	stp q0,q0,[sp, #0x10]
0037c158	stp q0,q0,[sp, #0x30]
0037c15c	stp q0,q0,[sp, #0x50]
0037c160	stp q0,q0,[sp, #0x90]
0037c164	stp q0,q0,[sp, #0xf0]
0037c168	stp q0,q0,[sp, #0x110]
0037c16c	stur q0,[x23, #0x4c]
0037c170	stp q0,q0,[sp, #0xb0]
0037c174	bl 0x0041fc10
0037c178	mov x0,x20
0037c17c	bl 0x0041f920
0037c180	add x0,sp,#0xe
0037c184	bl 0x0041f6c0
0037c188	mov x10,#0x227b
0037c18c	ldr x8,[sp, #0x10]
0037c190	movk x10,#0x6556, LSL #16
0037c194	ldrh w9,[sp, #0x18]
0037c198	movk x10,#0x6972, LSL #32
0037c19c	mov w20,w0
0037c1a0	movk x10,#0x7966, LSL #48
0037c1a4	cmp x8,x10
0037c1a8	mov w8,#0x3a22
0037c1ac	ccmp w9,w8,#0x0,eq
0037c1b0	b.eq 0x0037c1f0
0037c1b4	mov x9,#0x227b
0037c1b8	ldr x8,[sp, #0x10]
0037c1bc	movk x9,#0x6f5a, LSL #16
0037c1c0	movk x9,#0x656e, LSL #32
0037c1c4	movk x9,#0x3a22, LSL #48
0037c1c8	cmp x8,x9
0037c1cc	b.eq 0x0037c20c
0037c1d0	ldrb w8,[sp, #0x10]
0037c1d4	cbz w8,0x0037c220
0037c1d8	adrp x0,0x15b000
0037c1dc	add x0,x0,#0xdfd
0037c1e0	b 0x0037c34c
0037c1e4	adrp x0,0x157000
0037c1e8	add x0,x0,#0xfae
0037c1ec	b 0x0037c34c
0037c1f0	adrp x1,0x142000
0037c1f4	add x1,x1,#0x99b
0037c1f8	add x0,sp,#0x10
0037c1fc	add x2,sp,#0xc
0037c200	add x3,sp,#0xe0
0037c204	bl 0x0041fc10
0037c208	b 0x0037c220
0037c20c	adrp x1,0x14a000
0037c210	add x1,x1,#0x491
0037c214	add x0,sp,#0x10
0037c218	add x2,sp,#0xe0
0037c21c	bl 0x0041fc10
0037c220	mov x22,#0x6e75
0037c224	adrp x0,0x146000
0037c228	add x0,x0,#0x297
0037c22c	movk x22,#0x6e6b, LSL #16
0037c230	movk x22,#0x776f, LSL #32
0037c234	movk x22,#0x6e, LSL #48
0037c238	bl 0x0041fbd0
0037c23c	adrp x0,0x15a000
0037c240	add x0,x0,#0x910
0037c244	adrp x2,0x148000
0037c248	add x2,x2,#0x27f
0037c24c	add x1,sp,#0x80
0037c250	bl 0x002a8870
0037c254	ldr x8,[sp, #0x80]
0037c258	cmp x8,x22
0037c25c	cset w8,ne
0037c260	b.ne 0x0037c278
0037c264	ldrb w9,[sp, #0xe0]
0037c268	cbz w9,0x0037c278
0037c26c	adrp x0,0x153000
0037c270	add x0,x0,#0x7bd
0037c274	b 0x0037c34c
0037c278	cbz w8,0x0037c298
0037c27c	add x0,sp,#0x80
0037c280	add x1,sp,#0xe0
0037c284	bl 0x0041f6b0
0037c288	cbz w0,0x0037c298
0037c28c	adrp x0,0x147000
0037c290	add x0,x0,#0xd36
0037c294	b 0x0037c34c
0037c298	sub x0,x29,#0xd0
0037c29c	bl 0x0037c6c0
0037c2a0	tbnz w0,#0x1f,0x0037c344
0037c2a4	adrp x0,0x152000
0037c2a8	add x0,x0,#0x960
0037c2ac	bl 0x0041fbd0
0037c2b0	adrp x0,0x15a000
0037c2b4	add x0,x0,#0x8d2
0037c2b8	adrp x2,0x148000
0037c2bc	add x2,x2,#0x27f
0037c2c0	add x1,sp,#0x1a0
0037c2c4	bl 0x002a8870
0037c2c8	ldr x8,[sp, #0x1a0]
0037c2cc	cmp x8,x22
0037c2d0	b.eq 0x0037c344
0037c2d4	add x0,sp,#0x140
0037c2d8	mov x1,x19
0037c2dc	bl 0x0037c7b0
0037c2e0	tbnz w0,#0x1f,0x0037c344
0037c2e4	adrp x0,0x14b000
0037c2e8	add x0,x0,#0x38b
0037c2ec	add x1,sp,#0x200
0037c2f0	add x2,sp,#0x1a0
0037c2f4	sub x3,x29,#0x70
0037c2f8	sub x4,x29,#0xd0
0037c2fc	add x5,sp,#0x170
0037c300	add x6,sp,#0x140
0037c304	bl 0x0041f6a0
0037c308	add x0,sp,#0x200
0037c30c	add x1,sp,#0x1a0
0037c310	bl 0x0041f6b0
0037c314	cbnz w0,0x0037c338
0037c318	sub x0,x29,#0x70
0037c31c	sub x1,x29,#0xd0
0037c320	bl 0x0041f6b0
0037c324	cbnz w0,0x0037c338
0037c328	add x0,sp,#0x170
0037c32c	add x1,sp,#0x140
0037c330	bl 0x0041f6b0
0037c334	cbz w0,0x0037c37c
0037c338	adrp x0,0x14a000
0037c33c	add x0,x0,#0x4a0
0037c340	b 0x0037c34c
0037c344	adrp x0,0x150000
0037c348	add x0,x0,#0xcb3
0037c34c	bl 0x0041fbd0
0037c350	mov w0,#0xffffffff
0037c354	ldr x8,[x21, #0x28]
0037c358	ldur x9,[x29, #-0x8]
0037c35c	cmp x8,x9
0037c360	b.ne 0x0037c384
0037c364	add sp,sp,#0x330
0037c368	ldp x20,x19,[sp, #0x30]
0037c36c	ldp x22,x21,[sp, #0x20]
0037c370	ldp x28,x23,[sp, #0x10]
0037c374	ldp x29,x30,[sp], #0x40
0037c378	ret
0037c37c	mov w0,w20
0037c380	b 0x0037c354
0037c384	bl 0x004217e0
