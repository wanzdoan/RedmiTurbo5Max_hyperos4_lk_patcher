ffff000090721db0	paciasp
ffff000090721db4	sub sp,sp,#0xc0
ffff000090721db8	stp x29,x30,[sp, #0x90]
ffff000090721dbc	str x21,[sp, #0xa0]
ffff000090721dc0	stp x20,x19,[sp, #0xb0]
ffff000090721dc4	add x29,sp,#0x90
ffff000090721dc8	adrp x8,-0xffff6f7a0000
ffff000090721dcc	ldrb w8,[x8, #0x5f8]
ffff000090721dd0	tbz w8,#0x0,0xffff000090721f98
ffff000090721dd4	adrp x8,-0xffff6f7a0000
ffff000090721dd8	ldrb w8,[x8, #0x5fc]
ffff000090721ddc	cmp w8,#0x1
ffff000090721de0	b.ne 0xffff000090721f68
ffff000090721de4	adrp x0,-0xffff6f810000
ffff000090721de8	add x0,x0,#0xb80
ffff000090721dec	bl 0xffff00009078850c
ffff000090721df0	cbz w0,0xffff000090721f7c
ffff000090721df4	add x0,sp,#0x48
ffff000090721df8	mov w1,wzr
ffff000090721dfc	mov w2,#0x41
ffff000090721e00	strb wzr,[sp, #0x88]
ffff000090721e04	stp xzr,xzr,[sp, #0x78]
ffff000090721e08	add x21,sp,#0x48
ffff000090721e0c	stp xzr,xzr,[sp, #0x68]
ffff000090721e10	stp xzr,xzr,[sp, #0x58]
ffff000090721e14	stp xzr,xzr,[sp, #0x48]
ffff000090721e18	stp xzr,xzr,[sp]
ffff000090721e1c	stp xzr,xzr,[sp, #0x10]
ffff000090721e20	stp xzr,xzr,[sp, #0x20]
ffff000090721e24	stp xzr,xzr,[sp, #0x30]
ffff000090721e28	strb wzr,[sp, #0x40]
ffff000090721e2c	bl 0xffff00009077f7b8
ffff000090721e30	mov x8,xzr
ffff000090721e34	adrp x19,-0xffff6f7a0000
ffff000090721e38	add x19,x19,#0x634
ffff000090721e3c	adrp x20,-0xffff6f80d000
ffff000090721e40	add x20,x20,#0xf15
ffff000090721e44	ldrb w9,[x19, x8, LSL ]
ffff000090721e48	add x8,x8,#0x1
ffff000090721e4c	cmp x8,#0x20
ffff000090721e50	lsr x10,x9,#0x4
ffff000090721e54	and x9,x9,#0xf
ffff000090721e58	ldrb w10,[x20, x10, LSL ]
ffff000090721e5c	ldrb w9,[x20, x9, LSL ]
ffff000090721e60	strb w10,[x21]
ffff000090721e64	strb w9,[x21, #0x1]
ffff000090721e68	add x21,x21,#0x2
ffff000090721e6c	b.ne 0xffff000090721e44
ffff000090721e70	mov x0,sp
ffff000090721e74	mov w1,wzr
ffff000090721e78	mov w2,#0x41
ffff000090721e7c	mov x21,sp
ffff000090721e80	bl 0xffff00009077f7b8
ffff000090721e84	mov x8,xzr
ffff000090721e88	adrp x9,-0xffff6f7a0000
ffff000090721e8c	add x9,x9,#0x654
ffff000090721e90	ldrb w10,[x9, x8, LSL ]
ffff000090721e94	add x8,x8,#0x1
ffff000090721e98	cmp x8,#0x20
ffff000090721e9c	lsr x11,x10,#0x4
ffff000090721ea0	and x10,x10,#0xf
ffff000090721ea4	ldrb w11,[x20, x11, LSL ]
ffff000090721ea8	ldrb w10,[x20, x10, LSL ]
ffff000090721eac	strb w11,[x21]
ffff000090721eb0	strb w10,[x21, #0x1]
ffff000090721eb4	add x21,x21,#0x2
ffff000090721eb8	b.ne 0xffff000090721e90
ffff000090721ebc	adrp x0,-0xffff6f826000
ffff000090721ec0	add x0,x0,#0xff1
ffff000090721ec4	adrp x1,-0xffff6f831000
ffff000090721ec8	add x1,x1,#0x9f4
ffff000090721ecc	add x3,sp,#0x48
ffff000090721ed0	mov x4,sp
ffff000090721ed4	mov w2,#0x10e
ffff000090721ed8	bl 0xffff00009077ea84
ffff000090721edc	mov x8,xzr
ffff000090721ee0	ldrb w9,[x19, x8, LSL ]
ffff000090721ee4	cbnz w9,0xffff000090721f10
ffff000090721ee8	add x8,x8,#0x1
ffff000090721eec	cmp x8,#0x20
ffff000090721ef0	b.ne 0xffff000090721ee0
ffff000090721ef4	adrp x0,-0xffff6f812000
ffff000090721ef8	add x0,x0,#0xda1
ffff000090721efc	adrp x1,-0xffff6f831000
ffff000090721f00	add x1,x1,#0x9f4
ffff000090721f04	mov w2,#0x111
ffff000090721f08	bl 0xffff00009077ea84
ffff000090721f0c	b 0xffff000090721f68
ffff000090721f10	adrp x0,-0xffff6f7a0000
ffff000090721f14	add x0,x0,#0x634
ffff000090721f18	adrp x1,-0xffff6f7a0000
ffff000090721f1c	add x1,x1,#0x654
ffff000090721f20	mov w2,#0x20
ffff000090721f24	bl 0xffff00009077f450
ffff000090721f28	cbz w0,0xffff000090721f7c
ffff000090721f2c	adrp x0,-0xffff6f81a000
ffff000090721f30	add x0,x0,#0x9f3
ffff000090721f34	adrp x1,-0xffff6f831000
ffff000090721f38	add x1,x1,#0x9f4
ffff000090721f3c	mov w2,#0x115
ffff000090721f40	bl 0xffff00009077ea84
ffff000090721f44	add x1,x29,#0x1c
ffff000090721f48	mov w0,wzr
ffff000090721f4c	str wzr,[x29, #0x1c]
ffff000090721f50	bl 0xffff00009074b510
ffff000090721f54	cbnz w0,0xffff000090721fb0
ffff000090721f58	ldrb w8,[x29, #0x1d]
ffff000090721f5c	lsl w8,w8,#0x8
ffff000090721f60	cmp w8,#0xf, LSL #12
ffff000090721f64	b.ne 0xffff000090721f70
ffff000090721f68	mov w0,wzr
ffff000090721f6c	b 0xffff000090721f80
ffff000090721f70	adrp x0,-0xffff6f81d000
ffff000090721f74	add x0,x0,#0x37e
ffff000090721f78	bl 0xffff00009077ea84
ffff000090721f7c	mov w0,#0x1
ffff000090721f80	ldp x20,x19,[sp, #0xb0]
ffff000090721f84	ldp x29,x30,[sp, #0x90]
ffff000090721f88	ldr x21,[sp, #0xa0]
ffff000090721f8c	add sp,sp,#0xc0
ffff000090721f90	autiasp
ffff000090721f94	ret
ffff000090721f98	adrp x0,-0xffff6f809000
ffff000090721f9c	add x0,x0,#0xb40
ffff000090721fa0	adrp x2,-0xffff6f80e000
ffff000090721fa4	add x2,x2,#0x483
ffff000090721fa8	mov w1,#0x106
ffff000090721fac	bl 0xffff00009077d934
ffff000090721fb0	mov w1,w0
ffff000090721fb4	adrp x0,-0xffff6f831000
ffff000090721fb8	add x0,x0,#0xa08
ffff000090721fbc	bl 0xffff00009077ea84
ffff000090721fc0	adrp x0,-0xffff6f809000
ffff000090721fc4	add x0,x0,#0xb40
ffff000090721fc8	adrp x2,-0xffff6f836000
ffff000090721fcc	add x2,x2,#0x5f8
ffff000090721fd0	mov w1,#0x121
ffff000090721fd4	bl 0xffff00009077d934
