0022dd20	sub sp,sp,#0x140
0022dd24	stp x29,x30,[sp, #0xf0]
0022dd28	stp x28,x25,[sp, #0x100]
0022dd2c	stp x24,x23,[sp, #0x110]
0022dd30	stp x22,x21,[sp, #0x120]
0022dd34	stp x20,x19,[sp, #0x130]
0022dd38	add x29,sp,#0xf0
0022dd3c	adrp x10,0x157000
0022dd40	add x10,x10,#0x71b
0022dd44	mrs x23,tpidr_el0
0022dd48	mov w20,w1
0022dd4c	mov x19,x0
0022dd50	ldr x8,[x23, #0x28]
0022dd54	ldr q0,[x10]
0022dd58	mov w10,#0x6666
0022dd5c	mov w9,#0x26
0022dd60	movk w10,#0x7869, LSL #16
0022dd64	sub x24,x29,#0x70
0022dd68	stur x8,[x29, #-0x8]
0022dd6c	sturb wzr,[x29, #-0x5c]
0022dd70	sturb w9,[x29, #-0x70]
0022dd74	stur w10,[x29, #-0x60]
0022dd78	stur q0,[x24, #0x1]
0022dd7c	strh wzr,[sp, #0x48]
0022dd80	sub x8,x29,#0x50
0022dd84	sub x0,x29,#0x70
0022dd88	add x1,sp,#0x48
0022dd8c	bl 0x0040f330
0022dd90	ldurb w8,[x29, #-0x50]
0022dd94	ldur x9,[x29, #-0x48]
0022dd98	lsr x10,x8,#0x1
0022dd9c	tst w8,#0x1
0022dda0	csel x22,x10,x9,eq
0022dda4	tbnz w8,#0x0,0x0022de74
0022dda8	ldrb w8,[sp, #0x48]
0022ddac	tbnz w8,#0x0,0x0022de84
0022ddb0	ldurb w8,[x29, #-0x70]
0022ddb4	tbz w8,#0x0,0x0022ddc0
0022ddb8	ldur x0,[x29, #-0x60]
0022ddbc	bl 0x0041f730
0022ddc0	adrp x9,0x158000
0022ddc4	add x9,x9,#0x3fa
0022ddc8	mov w8,#0x2a
0022ddcc	sturb wzr,[x29, #-0x3a]
0022ddd0	ldr q0,[x9]
0022ddd4	ldur x9,[x9, #0xd]
0022ddd8	sturb w8,[x29, #-0x50]
0022dddc	stur q0,[x24, #0x21]
0022dde0	stur x9,[x24, #0x2e]
0022dde4	sub x0,x29,#0x50
0022dde8	mov w1,wzr
0022ddec	bl 0x0040fe90
0022ddf0	mov w21,w0
0022ddf4	ldurb w8,[x29, #-0x50]
0022ddf8	tbz w8,#0x0,0x0022de04
0022ddfc	ldur x0,[x29, #-0x40]
0022de00	bl 0x0041f730
0022de04	cmp x22,#0x0
0022de08	cset w8,eq
0022de0c	orr w8,w8,w21
0022de10	tbz w8,#0x0,0x0022de98
0022de14	cmp w20,#0x8
0022de18	stp xzr,xzr,[sp, #0x48]
0022de1c	str xzr,[sp, #0x58]
0022de20	stp xzr,xzr,[sp, #0x30]
0022de24	str xzr,[sp, #0x40]
0022de28	b.ne 0x0022dea0
0022de2c	mov w9,#0x66
0022de30	mov w8,#0x12
0022de34	adrp x10,0x147000
0022de38	add x10,x10,#0x4b3
0022de3c	mov w11,#0x14
0022de40	mov w21,#0x9
0022de44	sturh w9,[sp, #0x51]
0022de48	adrp x9,0x156000
0022de4c	add x9,x9,#0x983
0022de50	ldr x10,[x10]
0022de54	strb w8,[sp, #0x48]
0022de58	mov w8,#0x6666
0022de5c	strb w11,[sp, #0x30]
0022de60	ldr x9,[x9]
0022de64	stur x10,[sp, #0x49]
0022de68	sturh w8,[sp, #0x39]
0022de6c	stur x9,[sp, #0x31]
0022de70	b 0x0022deb4
0022de74	ldur x0,[x29, #-0x40]
0022de78	bl 0x0041f730
0022de7c	ldrb w8,[sp, #0x48]
0022de80	tbz w8,#0x0,0x0022ddb0
0022de84	ldr x0,[sp, #0x58]
0022de88	bl 0x0041f730
0022de8c	ldurb w8,[x29, #-0x70]
0022de90	tbnz w8,#0x0,0x0022ddb8
0022de94	b 0x0022ddc0
0022de98	mov w19,#0x1
0022de9c	b 0x0022e34c
0022dea0	mov w0,#0x6
0022dea4	mov x1,xzr
0022dea8	bl 0x0040d370
0022deac	tbnz w0,#0x0,0x0022e37c
0022deb0	mov x21,xzr
0022deb4	stp xzr,xzr,[sp, #0x18]
0022deb8	str xzr,[sp, #0x28]
0022debc	mov w0,#0x30
0022dec0	bl 0x0041f720
0022dec4	adrp x8,0x149000
0022dec8	add x8,x8,#0xa48
0022decc	mov w9,#0x31
0022ded0	mov w10,#0x2a
0022ded4	stur x0,[x29, #-0x40]
0022ded8	ldp q0,q1,[x8]
0022dedc	stp x9,x10,[x29, #-0x50]
0022dee0	strb wzr,[x0, #0x2a]
0022dee4	stp q0,q1,[x0]
0022dee8	ldur q0,[x8, #0x1a]
0022deec	sub x8,x29,#0x50
0022def0	add x25,x8,#0x18
0022def4	stur q0,[x0, #0x1a]
0022def8	mov w0,#0x30
0022defc	bl 0x0041f720
0022df00	adrp x10,0x14c000
0022df04	add x10,x10,#0x669
0022df08	mov w8,#0x26
0022df0c	mov w9,#0x31
0022df10	orr x8,x21,x8
0022df14	add x22,x0,#0x26
0022df18	ldp q0,q1,[x10]
0022df1c	cmp w20,#0x8
0022df20	str x9,[sp]
0022df24	stp x8,x0,[sp, #0x8]
0022df28	ldur x8,[x10, #0x1e]
0022df2c	stp q0,q1,[x0]
0022df30	stur x8,[x0, #0x1e]
0022df34	b.ne 0x0022df4c
0022df38	add x8,sp,#0x48
0022df3c	mov x0,x22
0022df40	orr x1,x8,#0x1
0022df44	mov x2,x21
0022df48	bl 0x004217b0
0022df4c	strb wzr,[x22, x21, LSL ]
0022df50	adrp x1,0x153000
0022df54	add x1,x1,#0xc89
0022df58	mov x0,sp
0022df5c	mov w2,#0x5
0022df60	bl 0x0041fd00
0022df64	ldr x8,[x0, #0x10]
0022df68	adrp x9,0x14d000
0022df6c	add x9,x9,#0xfcf
0022df70	ldr q0,[x0]
0022df74	add x22,sp,#0x18
0022df78	str x8,[x25, #0x10]
0022df7c	mov w8,#0x22
0022df80	str q0,[x25]
0022df84	ldr q0,[x9]
0022df88	stp xzr,xzr,[x0, #0x8]
0022df8c	mov w9,#0x3f
0022df90	str xzr,[x0]
0022df94	sturb w8,[x29, #-0x20]
0022df98	sub x8,x29,#0x50
0022df9c	add x21,x8,#0x30
0022dfa0	stur q0,[x24, #0x51]
0022dfa4	sturh w9,[x24, #0x61]
0022dfa8	stp xzr,xzr,[sp, #0x20]
0022dfac	str xzr,[sp, #0x18]
0022dfb0	stp x22,xzr,[sp, #0x70]
0022dfb4	mov w0,#0x48
0022dfb8	bl 0x0041f720
0022dfbc	add x8,x0,#0x48
0022dfc0	mov x20,x0
0022dfc4	add x9,x22,#0x10
0022dfc8	add x10,sp,#0x60
0022dfcc	ldurb w11,[x29, #-0x50]
0022dfd0	stp x0,x0,[sp, #0x18]
0022dfd4	str x8,[sp, #0x28]
0022dfd8	add x8,sp,#0x68
0022dfdc	stp x0,x0,[sp, #0x60]
0022dfe0	stp x9,x10,[x29, #-0x70]
0022dfe4	stp x8,xzr,[x29, #-0x60]
0022dfe8	tbnz w11,#0x0,0x0022e004
0022dfec	ldur q0,[x29, #-0x50]
0022dff0	mov x9,x20
0022dff4	ldur x8,[x29, #-0x40]
0022dff8	str q0,[x20]
0022dffc	str x8,[x20, #0x10]
0022e000	b 0x0022e014
0022e004	ldp x2,x1,[x29, #-0x48]
0022e008	mov x0,x20
0022e00c	bl 0x00225710
0022e010	ldr x9,[sp, #0x68]
0022e014	add x8,x9,#0x18
0022e018	ldurb w10,[x29, #-0x38]
0022e01c	str x8,[sp, #0x68]
0022e020	tbnz w10,#0x0,0x0022e03c
0022e024	ldr x10,[x25, #0x10]
0022e028	add x0,x9,#0x30
0022e02c	ldr q0,[x25]
0022e030	str x10,[x8, #0x10]
0022e034	str q0,[x8]
0022e038	b 0x0022e05c
0022e03c	ldp x2,x1,[x29, #-0x30]
0022e040	mov x0,x8
0022e044	bl 0x00225710
0022e048	ldr x8,[sp, #0x68]
0022e04c	ldurb w9,[x29, #-0x20]
0022e050	add x0,x8,#0x18
0022e054	str x0,[sp, #0x68]
0022e058	tbnz w9,#0x0,0x0022e070
0022e05c	ldr x8,[x21, #0x10]
0022e060	ldr q0,[x21]
0022e064	str x8,[x0, #0x10]
0022e068	str q0,[x0]
0022e06c	b 0x0022e07c
0022e070	ldp x2,x1,[x29, #-0x18]
0022e074	bl 0x00225710
0022e078	ldr x0,[sp, #0x68]
0022e07c	add x8,x0,#0x18
0022e080	ldurb w9,[x29, #-0x20]
0022e084	str x8,[sp, #0x20]
0022e088	tbnz w9,#0x0,0x0022e0f8
0022e08c	ldurb w8,[x29, #-0x38]
0022e090	tbnz w8,#0x0,0x0022e108
0022e094	ldurb w8,[x29, #-0x50]
0022e098	tbnz w8,#0x0,0x0022e118
0022e09c	ldrb w8,[sp]
0022e0a0	tbz w8,#0x0,0x0022e0ac
0022e0a4	ldr x0,[sp, #0x10]
0022e0a8	bl 0x0041f730
0022e0ac	mov w9,#0x4320
0022e0b0	mov w10,#0x636e
0022e0b4	mov w8,#0xe
0022e0b8	movk w9,#0x6e61, LSL #16
0022e0bc	movk w10,#0x6c65, LSL #16
0022e0c0	ldrb w11,[sp, #0x30]
0022e0c4	sub x21,x29,#0x50
0022e0c8	stp xzr,xzr,[sp]
0022e0cc	str xzr,[sp, #0x10]
0022e0d0	sturb w8,[x29, #-0x50]
0022e0d4	stur w9,[x24, #0x21]
0022e0d8	stur w10,[x29, #-0x4c]
0022e0dc	sturb wzr,[x29, #-0x48]
0022e0e0	tbnz w11,#0x0,0x0022e12c
0022e0e4	ldr q0,[sp, #0x30]
0022e0e8	ldr x8,[sp, #0x40]
0022e0ec	stur q0,[x21, #0x18]
0022e0f0	stur x8,[x21, #0x28]
0022e0f4	b 0x0022e138
0022e0f8	ldur x0,[x29, #-0x10]
0022e0fc	bl 0x0041f730
0022e100	ldurb w8,[x29, #-0x38]
0022e104	tbz w8,#0x0,0x0022e094
0022e108	ldur x0,[x29, #-0x28]
0022e10c	bl 0x0041f730
0022e110	ldurb w8,[x29, #-0x50]
0022e114	tbz w8,#0x0,0x0022e09c
0022e118	ldur x0,[x29, #-0x40]
0022e11c	bl 0x0041f730
0022e120	ldrb w8,[sp]
0022e124	tbnz w8,#0x0,0x0022e0a4
0022e128	b 0x0022e0ac
0022e12c	ldp x2,x1,[sp, #0x38]
0022e130	add x0,x21,#0x18
0022e134	bl 0x00225710
0022e138	mov x22,sp
0022e13c	stp xzr,xzr,[sp]
0022e140	str xzr,[sp, #0x10]
0022e144	stp x22,xzr,[sp, #0x70]
0022e148	mov w0,#0x30
0022e14c	bl 0x0041f720
0022e150	add x8,x0,#0x30
0022e154	mov x20,x0
0022e158	add x9,x22,#0x10
0022e15c	add x10,sp,#0x60
0022e160	ldurb w11,[x29, #-0x50]
0022e164	stp x0,x0,[sp]
0022e168	str x8,[sp, #0x10]
0022e16c	add x8,sp,#0x68
0022e170	stp x0,x0,[sp, #0x60]
0022e174	stp x9,x10,[x29, #-0x70]
0022e178	stp x8,xzr,[x29, #-0x60]
0022e17c	tbnz w11,#0x0,0x0022e198
0022e180	ldur q0,[x29, #-0x50]
0022e184	mov x8,x20
0022e188	ldur x9,[x29, #-0x40]
0022e18c	str q0,[x20]
0022e190	str x9,[x20, #0x10]
0022e194	b 0x0022e1a8
0022e198	ldp x2,x1,[x29, #-0x48]
0022e19c	mov x0,x20
0022e1a0	bl 0x00225710
0022e1a4	ldr x8,[sp, #0x68]
0022e1a8	add x0,x8,#0x18
0022e1ac	ldurb w8,[x29, #-0x38]
0022e1b0	str x0,[sp, #0x68]
0022e1b4	tbnz w8,#0x0,0x0022e1cc
0022e1b8	ldur x8,[x21, #0x28]
0022e1bc	ldur q0,[x21, #0x18]
0022e1c0	str x8,[x0, #0x10]
0022e1c4	str q0,[x0]
0022e1c8	b 0x0022e1d8
0022e1cc	ldp x2,x1,[x29, #-0x30]
0022e1d0	bl 0x00225710
0022e1d4	ldr x0,[sp, #0x68]
0022e1d8	add x8,x0,#0x18
0022e1dc	ldurb w9,[x29, #-0x38]
0022e1e0	str x8,[sp, #0x8]
0022e1e4	tbz w9,#0x0,0x0022e1f0
0022e1e8	ldur x0,[x29, #-0x28]
0022e1ec	bl 0x0041f730
0022e1f0	ldurb w8,[x29, #-0x50]
0022e1f4	tbz w8,#0x0,0x0022e200
0022e1f8	ldur x0,[x29, #-0x40]
0022e1fc	bl 0x0041f730
0022e200	ldr x8,[x19]
0022e204	ldr x8,[x8, #0x10]
0022e208	mov x0,x19
0022e20c	blr x8
0022e210	adrp x8,0x424000
0022e214	add x8,x8,#0x908
0022e218	mov w9,#0x1
0022e21c	mov w10,#0x40
0022e220	sub x20,x29,#0x50
0022e224	add x8,x8,#0x10
0022e228	stp x9,x19,[x29, #-0x40]
0022e22c	stur x20,[x29, #-0x30]
0022e230	stp x8,x10,[x29, #-0x50]
0022e234	ldr x8,[x0]
0022e238	ldr x8,[x8, #0x1d0]
0022e23c	add x1,sp,#0x18
0022e240	mov x2,sp
0022e244	sub x5,x29,#0x50
0022e248	mov x3,xzr
0022e24c	mov w4,#0x1
0022e250	blr x8
0022e254	mov x19,x0
0022e258	ldur x0,[x29, #-0x30]
0022e25c	cmp x0,x20
0022e260	b.eq 0x0022e270
0022e264	cbz x0,0x0022e284
0022e268	mov w8,#0x5
0022e26c	b 0x0022e278
0022e270	mov w8,#0x4
0022e274	sub x0,x29,#0x50
0022e278	ldr x9,[x0]
0022e27c	ldr x8,[x9, x8, LSL #0x3]
0022e280	blr x8
0022e284	cmp x19,#0x1
0022e288	ldr x20,[sp]
0022e28c	cset w19,eq
0022e290	cbz x20,0x0022e2dc
0022e294	ldr x8,[sp, #0x8]
0022e298	mov x0,x20
0022e29c	cmp x8,x20
0022e2a0	b.eq 0x0022e2d4
0022e2a4	mov x21,x8
0022e2a8	b 0x0022e2bc
0022e2b0	mov x8,x21
0022e2b4	cmp x21,x20
0022e2b8	b.eq 0x0022e2d0
0022e2bc	ldrb w9,[x21, #-0x18]!
0022e2c0	tbz w9,#0x0,0x0022e2b0
0022e2c4	ldur x0,[x8, #-0x8]
0022e2c8	bl 0x0041f730
0022e2cc	b 0x0022e2b0
0022e2d0	ldr x0,[sp]
0022e2d4	str x20,[sp, #0x8]
0022e2d8	bl 0x0041f730
0022e2dc	ldr x20,[sp, #0x18]
0022e2e0	cbz x20,0x0022e32c
0022e2e4	ldr x8,[sp, #0x20]
0022e2e8	mov x0,x20
0022e2ec	cmp x8,x20
0022e2f0	b.eq 0x0022e324
0022e2f4	mov x21,x8
0022e2f8	b 0x0022e30c
0022e300	mov x8,x21
0022e304	cmp x21,x20
0022e308	b.eq 0x0022e320
0022e30c	ldrb w9,[x21, #-0x18]!
0022e310	tbz w9,#0x0,0x0022e300
0022e314	ldur x0,[x8, #-0x8]
0022e318	bl 0x0041f730
0022e31c	b 0x0022e300
0022e320	ldr x0,[sp, #0x18]
0022e324	str x20,[sp, #0x20]
0022e328	bl 0x0041f730
0022e32c	ldrb w8,[sp, #0x30]
0022e330	tbz w8,#0x0,0x0022e33c
0022e334	ldr x0,[sp, #0x40]
0022e338	bl 0x0041f730
0022e33c	ldrb w8,[sp, #0x48]
0022e340	tbz w8,#0x0,0x0022e34c
0022e344	ldr x0,[sp, #0x58]
0022e348	bl 0x0041f730
0022e34c	ldr x8,[x23, #0x28]
0022e350	ldur x9,[x29, #-0x8]
0022e354	cmp x8,x9
0022e358	b.ne 0x0022e590
0022e35c	mov w0,w19
0022e360	ldp x20,x19,[sp, #0x130]
0022e364	ldp x22,x21,[sp, #0x120]
0022e368	ldp x24,x23,[sp, #0x110]
0022e36c	ldp x28,x25,[sp, #0x100]
0022e370	ldp x29,x30,[sp, #0xf0]
0022e374	add sp,sp,#0x140
0022e378	ret
0022e37c	bl 0x0041f880
0022e380	mov x21,x0
0022e384	ldr w22,[x0]
0022e388	adrp x1,0x147000
0022e38c	add x1,x1,#0x470
0022e390	sub x0,x29,#0x50
0022e394	mov w2,#0x1e2
0022e398	mov w3,#0x6
0022e39c	mov x4,xzr
0022e3a0	mov w5,#0xffffffff
0022e3a4	bl 0x0040d500
0022e3a8	sub x0,x29,#0x50
0022e3ac	mov x25,x21
0022e3b0	bl 0x0040d3e0
0022e3b4	adrp x1,0x147000
0022e3b8	add x1,x1,#0x4bd
0022e3bc	mov w2,#0x16
0022e3c0	bl 0x00226b10
0022e3c4	mov w1,w20
0022e3c8	bl 0x0041f740
0022e3cc	sub x0,x29,#0x50
0022e3d0	bl 0x0040d610
0022e3d4	mov x21,xzr
0022e3d8	str w22,[x25]
0022e3dc	b 0x0022deb4
0022e590	bl 0x004217e0
