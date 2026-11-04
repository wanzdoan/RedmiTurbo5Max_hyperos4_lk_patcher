00411dc0	sub sp,sp,#0xa0
00411dc4	stp x29,x30,[sp, #0x40]
00411dc8	stp x28,x27,[sp, #0x50]
00411dcc	stp x26,x25,[sp, #0x60]
00411dd0	stp x24,x23,[sp, #0x70]
00411dd4	stp x22,x21,[sp, #0x80]
00411dd8	stp x20,x19,[sp, #0x90]
00411ddc	add x29,sp,#0x40
00411de0	mrs x9,tpidr_el0
00411de4	str x9,[sp, #0x8]
00411de8	ldr x9,[x9, #0x28]
00411dec	stur x9,[x29, #-0x8]
00411df0	ldrb w9,[x1]
00411df4	ldr x10,[x1, #0x8]
00411df8	lsr x11,x9,#0x1
00411dfc	tst w9,#0x1
00411e00	csel x9,x11,x10,eq
00411e04	cbz x9,0x00411fdc
00411e08	mov x21,x8
00411e0c	add x8,sp,#0x20
00411e10	mov x19,x1
00411e14	mov x20,x0
00411e18	mov x28,xzr
00411e1c	add x25,x0,#0x1
00411e20	add x26,x1,#0x1
00411e24	orr x8,x8,#0x1
00411e28	stp xzr,xzr,[x21]
00411e2c	str xzr,[x21, #0x10]
00411e30	stp x8,x25,[sp, #0x10]
00411e34	b 0x00411e60
00411e40	ldr x9,[sp, #0x30]
00411e44	add x10,x8,#0x18
00411e48	ldr q0,[sp, #0x20]
00411e4c	str x9,[x8, #0x10]
00411e50	str q0,[x8]
00411e54	str x10,[x21, #0x8]
00411e58	adds x28,x27,#0x1
00411e5c	b.cs 0x00411f94
00411e60	ldrb w8,[x20]
00411e64	mov x27,#-0x1
00411e68	ldp x10,x9,[x20, #0x8]
00411e6c	tst w8,#0x1
00411e70	lsr x8,x8,#0x1
00411e74	ldrb w11,[x19]
00411e78	csel x8,x8,x10,eq
00411e7c	ldp x10,x12,[x19, #0x8]
00411e80	csel x24,x25,x9,eq
00411e84	tst w11,#0x1
00411e88	lsr x11,x11,#0x1
00411e8c	csel x10,x11,x10,eq
00411e90	csel x9,x26,x12,eq
00411e94	cmp x8,x28
00411e98	b.ls 0x00411eec
00411e9c	cbz x10,0x00411eec
00411ea0	add x11,x24,x8
00411ea4	add x12,x24,x28
00411ea8	ldrb w13,[x12]
00411eac	mov x14,x10
00411eb0	mov x15,x9
00411eb4	ldrb w16,[x15]
00411eb8	cmp w13,w16
00411ebc	b.eq 0x00411ee0
00411ec0	add x15,x15,#0x1
00411ec4	subs x14,x14,#0x1
00411ec8	b.ne 0x00411eb4
00411ecc	add x12,x12,#0x1
00411ed0	cmp x12,x11
00411ed4	b.ne 0x00411ea8
00411ed8	mov x12,x11
00411edc	nop
00411ee0	sub x9,x12,x24
00411ee4	cmp x12,x11
00411ee8	csinv x27,x9,xzr,ne
00411eec	subs x9,x27,x28
00411ef0	b.cc 0x00411fc8
00411ef4	subs x8,x8,x28
00411ef8	b.cc 0x00411fcc
00411efc	cmp x8,x9
00411f00	csel x22,x8,x9,cc
00411f04	cmn x22,#0x10
00411f08	b.cs 0x00411fd4
00411f0c	cmp x22,#0x17
00411f10	b.cs 0x00411f30
00411f14	lsl w8,w22,#0x1
00411f18	ldr x23,[sp, #0x10]
00411f1c	strb w8,[sp, #0x20]
00411f20	cbnz x22,0x00411f50
00411f24	b 0x00411f60
00411f30	orr x25,x22,#0xf
00411f34	add x0,x25,#0x1
00411f38	bl 0x0041f720
00411f3c	add x8,x25,#0x2
00411f40	ldr x25,[sp, #0x18]
00411f44	mov x23,x0
00411f48	stp x22,x0,[sp, #0x28]
00411f4c	str x8,[sp, #0x20]
00411f50	add x1,x24,x28
00411f54	mov x0,x23
00411f58	mov x2,x22
00411f5c	bl 0x004217a0
00411f60	ldp x8,x9,[x21, #0x8]
00411f64	strb wzr,[x23, x22, LSL ]
00411f68	cmp x8,x9
00411f6c	b.cc 0x00411e40
00411f70	add x1,sp,#0x20
00411f74	mov x0,x21
00411f78	bl 0x00225ed0
00411f7c	ldrb w8,[sp, #0x20]
00411f80	str x0,[x21, #0x8]
00411f84	tbz w8,#0x0,0x00411e58
00411f88	ldr x0,[sp, #0x30]
00411f8c	bl 0x0041f730
00411f90	b 0x00411e58
00411f94	ldr x8,[sp, #0x8]
00411f98	ldr x8,[x8, #0x28]
00411f9c	ldur x9,[x29, #-0x8]
00411fa0	cmp x8,x9
00411fa4	b.ne 0x00411fe0
00411fa8	ldp x20,x19,[sp, #0x90]
00411fac	ldp x22,x21,[sp, #0x80]
00411fb0	ldp x24,x23,[sp, #0x70]
00411fb4	ldp x26,x25,[sp, #0x60]
00411fb8	ldp x28,x27,[sp, #0x50]
00411fbc	ldp x29,x30,[sp, #0x40]
00411fc0	add sp,sp,#0xa0
00411fc4	ret
00411fc8	bl 0x00220254
00411fcc	add x0,sp,#0x20
00411fd0	bl 0x00239420
00411fd4	add x0,sp,#0x20
00411fd8	bl 0x00234d60
00411fdc	bl 0x00421080
00411fe0	bl 0x004217e0
