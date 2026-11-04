00377b70	sub sp,sp,#0x1a0
00377b74	stp x29,x30,[sp, #0x140]
00377b78	stp x28,x27,[sp, #0x150]
00377b7c	stp x26,x25,[sp, #0x160]
00377b80	stp x24,x23,[sp, #0x170]
00377b84	stp x22,x21,[sp, #0x180]
00377b88	stp x20,x19,[sp, #0x190]
00377b8c	add x29,sp,#0x140
00377b90	mrs x22,tpidr_el0
00377b94	mov x21,x0
00377b98	ldr x8,[x22, #0x28]
00377b9c	mov x19,x1
00377ba0	stur x8,[x29, #-0x10]
00377ba4	ldrb w8,[x0]
00377ba8	ldr x9,[x0, #0x10]
00377bac	tst w8,#0x1
00377bb0	csinc x0,x9,x0,ne
00377bb4	bl 0x00420210
00377bb8	mov x20,x0
00377bbc	cbz x0,0x00377f40
00377bc0	sub x8,x29,#0x90
00377bc4	add x10,x21,#0x1
00377bc8	orr x8,x8,#0x1
00377bcc	add x9,x19,#0x1
00377bd0	adrp x24,0x148000
00377bd4	add x24,x24,#0x2c2
00377bd8	mov w28,#0x2f
00377bdc	str x8,[sp, #0x28]
00377be0	add x8,sp,#0x90
00377be4	orr x8,x8,#0x1
00377be8	stp x22,x9,[sp, #0x8]
00377bec	stp x10,x8,[sp, #0x18]
00377bf0	add x8,sp,#0x70
00377bf4	orr x23,x8,#0x1
00377bf8	nop
00377bfc	nop
00377c00	mov x0,x20
00377c04	bl 0x00420230
00377c08	cbz x0,0x00377f30
00377c0c	mov x25,x0
00377c10	add x0,x0,#0x13
00377c14	mov x1,x24
00377c18	bl 0x0041f6b0
00377c1c	cbz w0,0x00377c00
00377c20	add x0,x25,#0x13
00377c24	adrp x1,0x14e000
00377c28	add x1,x1,#0x75
00377c2c	bl 0x0041f6b0
00377c30	cbz w0,0x00377c00
00377c34	ldrb w22,[x21]
00377c38	stp xzr,xzr,[sp, #0x90]
00377c3c	ldr x8,[x21, #0x8]
00377c40	str xzr,[sp, #0xa0]
00377c44	stp xzr,xzr,[x29, #-0x88]
00377c48	lsr x9,x22,#0x1
00377c4c	tst w22,#0x1
00377c50	csel x26,x9,x8,eq
00377c54	stur xzr,[x29, #-0x90]
00377c58	add x24,x26,#0x1
00377c5c	cmn x24,#0x10
00377c60	b.cs 0x00377f78
00377c64	cmp x24,#0x17
00377c68	b.cs 0x00377c80
00377c6c	lsl w8,w24,#0x1
00377c70	ldr x27,[sp, #0x28]
00377c74	sturb w8,[x29, #-0x90]
00377c78	cbnz x26,0x00377ca0
00377c7c	b 0x00377cbc
00377c80	orr x28,x24,#0xf
00377c84	add x0,x28,#0x1
00377c88	bl 0x0041f720
00377c8c	add x8,x28,#0x2
00377c90	mov x27,x0
00377c94	mov w28,#0x2f
00377c98	stp x24,x0,[x29, #-0x88]
00377c9c	stur x8,[x29, #-0x90]
00377ca0	ldr x8,[x21, #0x10]
00377ca4	tst w22,#0x1
00377ca8	ldr x9,[sp, #0x18]
00377cac	mov x0,x27
00377cb0	mov x2,x26
00377cb4	csel x1,x9,x8,eq
00377cb8	bl 0x004217a0
00377cbc	add x0,x25,#0x13
00377cc0	strh w28,[x27, x26, LSL #0x0]
00377cc4	bl 0x0041f860
00377cc8	mov x2,x0
00377ccc	sub x0,x29,#0x90
00377cd0	add x1,x25,#0x13
00377cd4	bl 0x0041fd00
00377cd8	ldr x8,[x0, #0x10]
00377cdc	ldr q0,[x0]
00377ce0	str x8,[sp, #0xa0]
00377ce4	str q0,[sp, #0x90]
00377ce8	stp xzr,xzr,[x0, #0x8]
00377cec	str xzr,[x0]
00377cf0	ldurb w8,[x29, #-0x90]
00377cf4	tbz w8,#0x0,0x00377d00
00377cf8	ldur x0,[x29, #-0x80]
00377cfc	bl 0x0041f730
00377d00	ldrb w22,[x19]
00377d04	stp xzr,xzr,[sp, #0x70]
00377d08	ldr x8,[x19, #0x8]
00377d0c	str xzr,[sp, #0x80]
00377d10	stp xzr,xzr,[x29, #-0x88]
00377d14	lsr x9,x22,#0x1
00377d18	tst w22,#0x1
00377d1c	csel x26,x9,x8,eq
00377d20	stur xzr,[x29, #-0x90]
00377d24	add x24,x26,#0x1
00377d28	cmn x24,#0x10
00377d2c	b.cs 0x00377f94
00377d30	cmp x24,#0x17
00377d34	b.cs 0x00377d4c
00377d38	lsl w8,w24,#0x1
00377d3c	ldr x27,[sp, #0x28]
00377d40	sturb w8,[x29, #-0x90]
00377d44	cbnz x26,0x00377d70
00377d48	b 0x00377d8c
00377d4c	orr x8,x24,#0xf
00377d50	add x28,x8,#0x1
00377d54	mov x0,x28
00377d58	bl 0x0041f720
00377d5c	orr x8,x28,#0x1
00377d60	mov x27,x0
00377d64	stp x24,x0,[x29, #-0x88]
00377d68	mov w28,#0x2f
00377d6c	stur x8,[x29, #-0x90]
00377d70	ldr x8,[x19, #0x10]
00377d74	tst w22,#0x1
00377d78	ldr x9,[sp, #0x10]
00377d7c	mov x0,x27
00377d80	mov x2,x26
00377d84	csel x1,x9,x8,eq
00377d88	bl 0x004217a0
00377d8c	add x0,x25,#0x13
00377d90	strh w28,[x27, x26, LSL #0x0]
00377d94	bl 0x0041f860
00377d98	mov x2,x0
00377d9c	sub x0,x29,#0x90
00377da0	add x1,x25,#0x13
00377da4	bl 0x0041fd00
00377da8	ldr x8,[x0, #0x10]
00377dac	adrp x24,0x148000
00377db0	add x24,x24,#0x2c2
00377db4	ldr q0,[x0]
00377db8	str x8,[sp, #0x80]
00377dbc	str q0,[sp, #0x70]
00377dc0	stp xzr,xzr,[x0, #0x8]
00377dc4	str xzr,[x0]
00377dc8	ldurb w8,[x29, #-0x90]
00377dcc	tbz w8,#0x0,0x00377dd8
00377dd0	ldur x0,[x29, #-0x80]
00377dd4	bl 0x0041f730
00377dd8	ldrb w8,[sp, #0x90]
00377ddc	adrp x1,0x154000
00377de0	add x1,x1,#0x57a
00377de4	ldr x9,[sp, #0xa0]
00377de8	tst w8,#0x1
00377dec	ldr x8,[sp, #0x20]
00377df0	csel x0,x8,x9,eq
00377df4	bl 0x0041f8d0
00377df8	movi v0.2D,#0x0
00377dfc	mov x25,x0
00377e00	stp q0,q0,[x29, #-0x90]
00377e04	stp q0,q0,[x29, #-0x70]
00377e08	stp q0,q0,[x29, #-0x50]
00377e0c	stp q0,q0,[x29, #-0x30]
00377e10	bl 0x0041fd70
00377e14	sub x1,x29,#0x90
00377e18	bl 0x00420a70
00377e1c	ldur w8,[x29, #-0x80]
00377e20	mov x0,x25
00377e24	and w22,w8,#0xf000
00377e28	bl 0x0041f910
00377e2c	cmp w22,#0x4, LSL #12
00377e30	b.ne 0x00377e9c
00377e34	ldrb w8,[sp, #0x70]
00377e38	mov w1,#0x1fd
00377e3c	ldr x9,[sp, #0x80]
00377e40	tst w8,#0x1
00377e44	csel x0,x23,x9,eq
00377e48	bl 0x00420140
00377e4c	ldrb w8,[sp, #0x70]
00377e50	mov w1,#0x3e8
00377e54	ldr x9,[sp, #0x80]
00377e58	mov w2,#0x3e8
00377e5c	tst w8,#0x1
00377e60	csel x0,x23,x9,eq
00377e64	bl 0x0041fc50
00377e68	ldrb w8,[sp, #0x70]
00377e6c	mov w1,#0x1f9
00377e70	ldr x9,[sp, #0x80]
00377e74	tst w8,#0x1
00377e78	csel x0,x23,x9,eq
00377e7c	bl 0x0041fc60
00377e80	ldrb w8,[sp, #0x90]
00377e84	tbnz w8,#0x0,0x00377eac
00377e88	ldr q0,[sp, #0x90]
00377e8c	ldr x8,[sp, #0xa0]
00377e90	str q0,[sp, #0x50]
00377e94	str x8,[sp, #0x60]
00377e98	b 0x00377eb8
00377e9c	add x0,sp,#0x90
00377ea0	add x1,sp,#0x70
00377ea4	bl 0x00377440
00377ea8	b 0x00377f0c
00377eac	ldp x2,x1,[sp, #0x98]
00377eb0	add x0,sp,#0x50
00377eb4	bl 0x00225710
00377eb8	ldrb w8,[sp, #0x70]
00377ebc	tbnz w8,#0x0,0x00377ed4
00377ec0	ldr q0,[sp, #0x70]
00377ec4	ldr x8,[sp, #0x80]
00377ec8	str q0,[sp, #0x30]
00377ecc	str x8,[sp, #0x40]
00377ed0	b 0x00377ee0
00377ed4	ldp x2,x1,[sp, #0x78]
00377ed8	add x0,sp,#0x30
00377edc	bl 0x00225710
00377ee0	add x0,sp,#0x50
00377ee4	add x1,sp,#0x30
00377ee8	bl 0x00377b70
00377eec	ldrb w8,[sp, #0x30]
00377ef0	tbz w8,#0x0,0x00377efc
00377ef4	ldr x0,[sp, #0x40]
00377ef8	bl 0x0041f730
00377efc	ldrb w8,[sp, #0x50]
00377f00	tbz w8,#0x0,0x00377f0c
00377f04	ldr x0,[sp, #0x60]
00377f08	bl 0x0041f730
00377f0c	ldrb w8,[sp, #0x70]
00377f10	tbz w8,#0x0,0x00377f1c
00377f14	ldr x0,[sp, #0x80]
00377f18	bl 0x0041f730
00377f1c	ldrb w8,[sp, #0x90]
00377f20	tbz w8,#0x0,0x00377c00
00377f24	ldr x0,[sp, #0xa0]
00377f28	bl 0x0041f730
00377f2c	b 0x00377c00
00377f30	mov x0,x20
00377f34	bl 0x00420220
00377f38	bl 0x0041fc00
00377f3c	ldr x22,[sp, #0x8]
00377f40	ldr x8,[x22, #0x28]
00377f44	cmp x20,#0x0
00377f48	ldur x9,[x29, #-0x10]
00377f4c	cset w0,ne
00377f50	cmp x8,x9
00377f54	b.ne 0x0037807c
00377f58	ldp x20,x19,[sp, #0x190]
00377f5c	ldp x22,x21,[sp, #0x180]
00377f60	ldp x24,x23,[sp, #0x170]
00377f64	ldp x26,x25,[sp, #0x160]
00377f68	ldp x28,x27,[sp, #0x150]
00377f6c	ldp x29,x30,[sp, #0x140]
00377f70	add sp,sp,#0x1a0
00377f74	ret
00377f78	ldr x8,[sp, #0x8]
00377f7c	ldr x8,[x8, #0x28]
00377f80	ldur x9,[x29, #-0x10]
00377f84	cmp x8,x9
00377f88	b.ne 0x0037807c
00377f8c	sub x0,x29,#0x90
00377f90	bl 0x00225510
00377f94	ldr x8,[sp, #0x8]
00377f98	ldr x8,[x8, #0x28]
00377f9c	ldur x9,[x29, #-0x10]
00377fa0	cmp x8,x9
00377fa4	b.ne 0x0037807c
00377fa8	sub x0,x29,#0x90
00377fac	bl 0x00225510
00377fb0	mov x19,x0
00377fb4	ldrb w8,[sp, #0x50]
00377fb8	tbz w8,#0x0,0x00377fdc
00377fbc	b 0x00377ff8
00377fdc	ldrb w8,[sp, #0x70]
00377fe0	tbnz w8,#0x0,0x00378018
00377fe4	b 0x00378050
00377ff8	ldr x0,[sp, #0x60]
00377ffc	bl 0x0041f730
00378000	ldrb w8,[sp, #0x70]
00378004	tbnz w8,#0x0,0x00378018
00378008	b 0x00378050
00378018	ldr x0,[sp, #0x80]
0037801c	b 0x0037804c
0037804c	bl 0x0041f730
00378050	ldrb w8,[sp, #0x90]
00378054	tbz w8,#0x0,0x00378060
00378058	ldr x0,[sp, #0xa0]
0037805c	bl 0x0041f730
00378060	ldr x8,[sp, #0x8]
00378064	ldr x8,[x8, #0x28]
00378068	ldur x9,[x29, #-0x10]
0037806c	cmp x8,x9
00378070	b.ne 0x0037807c
00378074	mov x0,x19
00378078	bl 0x004217d0
0037807c	bl 0x004217e0
