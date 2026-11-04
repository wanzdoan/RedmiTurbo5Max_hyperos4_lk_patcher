0025c250	sub sp,sp,#0x60
0025c254	str d10,[sp, #0x20]
0025c258	stp d9,d8,[sp, #0x30]
0025c25c	stp x29,x30,[sp, #0x40]
0025c260	stp x20,x19,[sp, #0x50]
0025c264	add x29,sp,#0x40
0025c268	mrs x20,tpidr_el0
0025c26c	mov x19,x0
0025c270	ldr x8,[x20, #0x28]
0025c274	add x0,x0,#0x9dc
0025c278	fmov s8,s1
0025c27c	fmov s9,s0
0025c280	str x8,[sp, #0x18]
0025c284	bl 0x00420000
0025c288	mov w8,#0x3
0025c28c	mov x10,#0x470
0025c290	mov x11,#0xdc
0025c294	movi d10,#0x0
0025c298	movi d0,#0x0
0025c29c	add x9,x19,#0xbb0
0025c2a0	str w8,[x19, #0xbd0]
0025c2a4	add x8,x19,#0xe78
0025c2a8	movk x10,#0x4, LSL #32
0025c2ac	movk x11,#0x4, LSL #32
0025c2b0	fcmp s9,#0.0
0025c2b4	str xzr,[x19, #0xbb0]
0025c2b8	str x8,[x19, #0xc08]
0025c2bc	add x8,x19,#0xe80
0025c2c0	stur x10,[x9, #0x2c]
0025c2c4	stur x11,[x9, #0x4c]
0025c2c8	str x8,[x19, #0xc10]
0025c2cc	mov x8,#0x400000000
0025c2d0	str xzr,[x19, #0xbc8]
0025c2d4	str xzr,[x19, #0xbc0]
0025c2d8	str xzr,[x19, #0xbf0]
0025c2dc	str xzr,[x19, #0xbe8]
0025c2e0	stur x8,[x9, #0x6c]
0025c2e4	str xzr,[x19, #0xc30]
0025c2e8	str xzr,[x19, #0xc28]
0025c2ec	b.mi 0x0025c304
0025c2f0	fmov s0,0x3f800000
0025c2f4	fcmp s9,s0
0025c2f8	fmov s0,s9
0025c2fc	b.le 0x0025c304
0025c300	fmov s0,0x3f800000
0025c304	add x0,sp,#0x8
0025c308	mov x1,xzr
0025c30c	str s0,[x19, #0xbb8]
0025c310	stp xzr,xzr,[sp, #0x8]
0025c314	bl 0x00420780
0025c318	ldr d0,[sp, #0x10]
0025c31c	mov x8,#0x848000000000
0025c320	movk x8,#0x412e, LSL #48
0025c324	fcvt d2,s8
0025c328	fcmp s8,#0.0
0025c32c	scvtf d0,d0
0025c330	fmov d1,x8
0025c334	str d2,[x19, #0xbc8]
0025c338	fdiv d0,d0,d1
0025c33c	ldr d1,[sp, #0x8]
0025c340	scvtf d1,d1
0025c344	fadd d1,d0,d1
0025c348	ldr s0,[x19, #0xbb4]
0025c34c	str d1,[x19, #0xbc0]
0025c350	b.ne 0x0025c360
0025c354	ldr s1,[x19, #0xbb0]
0025c358	fcmp s0,s1
0025c35c	b.ls 0x0025c378
0025c360	fcmp s0,#0.0
0025c364	str s0,[x19, #0xbb0]
0025c368	b.mi 0x0025c378
0025c36c	fmov s10,0x3f800000
0025c370	fcmp s0,s10
0025c374	b.le 0x0025c37c
0025c378	str s10,[x19, #0xbb0]
0025c37c	mov w8,#0x1
0025c380	strb wzr,[x19, #0xba0]
0025c384	strb w8,[x19, #0xd18]
0025c388	ldr x8,[x20, #0x28]
0025c38c	ldr x9,[sp, #0x18]
0025c390	cmp x8,x9
0025c394	b.ne 0x0025c3b4
0025c398	add x0,x19,#0x9dc
0025c39c	ldp x20,x19,[sp, #0x50]
0025c3a0	ldp x29,x30,[sp, #0x40]
0025c3a4	ldp d9,d8,[sp, #0x30]
0025c3a8	ldr d10,[sp, #0x20]
0025c3ac	add sp,sp,#0x60
0025c3b0	b 0x00420010
0025c3b4	bl 0x004217e0
