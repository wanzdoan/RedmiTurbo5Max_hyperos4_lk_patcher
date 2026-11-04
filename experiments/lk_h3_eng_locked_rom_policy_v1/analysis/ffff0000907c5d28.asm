ffff0000907c5d28	paciasp
ffff0000907c5d2c	stp x29,x30,[sp, #-0x60]!
ffff0000907c5d30	stp x28,x27,[sp, #0x10]
ffff0000907c5d34	stp x26,x25,[sp, #0x20]
ffff0000907c5d38	stp x24,x23,[sp, #0x30]
ffff0000907c5d3c	stp x22,x21,[sp, #0x40]
ffff0000907c5d40	stp x20,x19,[sp, #0x50]
ffff0000907c5d44	mov x29,sp
ffff0000907c5d48	mov w21,#0x8001
ffff0000907c5d4c	movk w21,#0x10, LSL #16
ffff0000907c5d50	cbz x0,0xffff0000907c5da4
ffff0000907c5d54	mov x22,x2
ffff0000907c5d58	cbz x2,0xffff0000907c5da8
ffff0000907c5d5c	mov x19,x0
ffff0000907c5d60	mov x0,x22
ffff0000907c5d64	mov w20,w1
ffff0000907c5d68	bl 0xffff00009077fb34
ffff0000907c5d6c	mov w28,w20
ffff0000907c5d70	cmp x0,x28
ffff0000907c5d74	b.hi 0xffff0000907c5da8
ffff0000907c5d78	mov x23,x0
ffff0000907c5d7c	mov x0,x19
ffff0000907c5d80	bl 0xffff00009077fb34
ffff0000907c5d84	cmp x23,x0
ffff0000907c5d88	b.hi 0xffff0000907c5da8
ffff0000907c5d8c	mov w25,wzr
ffff0000907c5d90	mov w26,wzr
ffff0000907c5d94	mov w9,wzr
ffff0000907c5d98	mov w27,wzr
ffff0000907c5d9c	mov w8,#0x1
ffff0000907c5da0	b 0xffff0000907c5de4
ffff0000907c5da4	add w21,w21,#0x3
ffff0000907c5da8	mov w0,w21
ffff0000907c5dac	ldp x20,x19,[sp, #0x50]
ffff0000907c5db0	ldp x22,x21,[sp, #0x40]
ffff0000907c5db4	ldp x24,x23,[sp, #0x30]
ffff0000907c5db8	ldp x26,x25,[sp, #0x20]
ffff0000907c5dbc	ldp x28,x27,[sp, #0x10]
ffff0000907c5dc0	ldp x29,x30,[sp], #0x60
ffff0000907c5dc4	autiasp
ffff0000907c5dc8	ret
ffff0000907c5dcc	cbz w8,0xffff0000907c5f0c
ffff0000907c5dd0	mov w9,wzr
ffff0000907c5dd4	mov w8,#0x1
ffff0000907c5dd8	add w27,w27,#0x1
ffff0000907c5ddc	cmp w27,w20
ffff0000907c5de0	b.hi 0xffff0000907c5f04
ffff0000907c5de4	ldrb w10,[x19, w27, UXTW ]
ffff0000907c5de8	cmp w10,#0x20
ffff0000907c5dec	ccmp w20,w27,#0x4,ne
ffff0000907c5df0	b.ne 0xffff0000907c5e34
ffff0000907c5df4	cmp w9,#0x2
ffff0000907c5df8	b.eq 0xffff0000907c5dcc
ffff0000907c5dfc	cmp w9,#0x1
ffff0000907c5e00	b.ne 0xffff0000907c5dd8
ffff0000907c5e04	mov x0,x22
ffff0000907c5e08	sub w23,w27,w26
ffff0000907c5e0c	bl 0xffff00009077fb34
ffff0000907c5e10	cmp x0,x23
ffff0000907c5e14	b.ne 0xffff0000907c5dd0
ffff0000907c5e18	add x24,x19,w26, UXTW
ffff0000907c5e1c	mov x1,x22
ffff0000907c5e20	mov x0,x24
ffff0000907c5e24	mov x2,x23
ffff0000907c5e28	bl 0xffff00009077f450
ffff0000907c5e2c	cbnz w0,0xffff0000907c5dd0
ffff0000907c5e30	b 0xffff0000907c5f6c
ffff0000907c5e34	cmp w10,#0x22
ffff0000907c5e38	b.eq 0xffff0000907c5e8c
ffff0000907c5e3c	cmp w10,#0x3d
ffff0000907c5e40	b.ne 0xffff0000907c5ea4
ffff0000907c5e44	cmp w9,#0x1
ffff0000907c5e48	b.ne 0xffff0000907c5dd8
ffff0000907c5e4c	mov x0,x22
ffff0000907c5e50	sub w25,w27,w26
ffff0000907c5e54	bl 0xffff00009077fb34
ffff0000907c5e58	cmp x0,x25
ffff0000907c5e5c	b.ne 0xffff0000907c5e7c
ffff0000907c5e60	mov x23,x0
ffff0000907c5e64	add x24,x19,w26, UXTW
ffff0000907c5e68	mov x0,x24
ffff0000907c5e6c	mov x1,x22
ffff0000907c5e70	mov x2,x23
ffff0000907c5e74	bl 0xffff00009077f450
ffff0000907c5e78	cbz w0,0xffff0000907c5ed0
ffff0000907c5e7c	mov w8,#0x1
ffff0000907c5e80	add w25,w27,#0x1
ffff0000907c5e84	mov w9,#0x2
ffff0000907c5e88	b 0xffff0000907c5dd8
ffff0000907c5e8c	cmp w9,#0x2
ffff0000907c5e90	b.eq 0xffff0000907c5ec4
ffff0000907c5e94	cmp w9,#0x3
ffff0000907c5e98	b.ne 0xffff0000907c5f64
ffff0000907c5e9c	cbnz w8,0xffff0000907c5dd0
ffff0000907c5ea0	b 0xffff0000907c5fc4
ffff0000907c5ea4	cbnz w9,0xffff0000907c5dd8
ffff0000907c5ea8	adrp x9,-0xffff6f6f2000
ffff0000907c5eac	ldr w9,[x9, #0x538]
ffff0000907c5eb0	cmp w9,#0x10
ffff0000907c5eb4	b.cs 0xffff0000907c6010
ffff0000907c5eb8	mov w9,#0x1
ffff0000907c5ebc	mov w26,w27
ffff0000907c5ec0	b 0xffff0000907c5dd8
ffff0000907c5ec4	mov w9,#0x3
ffff0000907c5ec8	mov w25,w27
ffff0000907c5ecc	b 0xffff0000907c5dd8
ffff0000907c5ed0	cmp w25,#0x40
ffff0000907c5ed4	b.hi 0xffff0000907c5fd4
ffff0000907c5ed8	adrp x8,-0xffff6f6f2000
ffff0000907c5edc	mov w10,#0xc2
ffff0000907c5ee0	adrp x9,-0xffff6f6f3000
ffff0000907c5ee4	add x9,x9,#0x918
ffff0000907c5ee8	mov x1,x24
ffff0000907c5eec	mov x2,x23
ffff0000907c5ef0	ldr w8,[x8, #0x538]
ffff0000907c5ef4	umaddl x0,w8,w10,x9
ffff0000907c5ef8	bl 0xffff00009077f488
ffff0000907c5efc	mov w8,wzr
ffff0000907c5f00	b 0xffff0000907c5e80
ffff0000907c5f04	mov w21,wzr
ffff0000907c5f08	b 0xffff0000907c5da8
ffff0000907c5f0c	sub w2,w27,w25
ffff0000907c5f10	cmp w2,#0x80
ffff0000907c5f14	b.hi 0xffff0000907c5fd4
ffff0000907c5f18	adrp x8,-0xffff6f6f2000
ffff0000907c5f1c	mov w9,#0xc2
ffff0000907c5f20	adrp x10,-0xffff6f6f3000
ffff0000907c5f24	add x10,x10,#0x918
ffff0000907c5f28	add x1,x19,w25, UXTW
ffff0000907c5f2c	ldr w8,[x8, #0x538]
ffff0000907c5f30	umaddl x8,w8,w9,x10
ffff0000907c5f34	add x0,x8,#0x41
ffff0000907c5f38	bl 0xffff00009077f488
ffff0000907c5f3c	cmp w27,w20
ffff0000907c5f40	b.cs 0xffff0000907c5fdc
ffff0000907c5f44	add x8,x19,w27, UXTW
ffff0000907c5f48	ldrb w9,[x8], #0x1
ffff0000907c5f4c	cmp w9,#0x20
ffff0000907c5f50	b.ne 0xffff0000907c5fdc
ffff0000907c5f54	add w27,w27,#0x1
ffff0000907c5f58	cmp w20,w27
ffff0000907c5f5c	b.ne 0xffff0000907c5f48
ffff0000907c5f60	b 0xffff0000907c5fbc
ffff0000907c5f64	add w21,w21,#0x4
ffff0000907c5f68	b 0xffff0000907c5da8
ffff0000907c5f6c	cmp w23,#0x40
ffff0000907c5f70	b.hi 0xffff0000907c5da8
ffff0000907c5f74	adrp x8,-0xffff6f6f2000
ffff0000907c5f78	mov w9,#0xc2
ffff0000907c5f7c	adrp x10,-0xffff6f6f3000
ffff0000907c5f80	add x10,x10,#0x918
ffff0000907c5f84	mov x1,x24
ffff0000907c5f88	mov x2,x23
ffff0000907c5f8c	ldr w8,[x8, #0x538]
ffff0000907c5f90	umaddl x0,w8,w9,x10
ffff0000907c5f94	bl 0xffff00009077f488
ffff0000907c5f98	cmp w27,w20
ffff0000907c5f9c	b.cs 0xffff0000907c6074
ffff0000907c5fa0	add x8,x19,w27, UXTW
ffff0000907c5fa4	ldrb w9,[x8], #0x1
ffff0000907c5fa8	cmp w9,#0x20
ffff0000907c5fac	b.ne 0xffff0000907c6074
ffff0000907c5fb0	add w27,w27,#0x1
ffff0000907c5fb4	cmp w20,w27
ffff0000907c5fb8	b.ne 0xffff0000907c5fa4
ffff0000907c5fbc	mov w8,wzr
ffff0000907c5fc0	b 0xffff0000907c60a4
ffff0000907c5fc4	add w22,w27,#0x1
ffff0000907c5fc8	sub w2,w22,w25
ffff0000907c5fcc	cmp w2,#0x80
ffff0000907c5fd0	b.ls 0xffff0000907c602c
ffff0000907c5fd4	add w21,w21,#0x2
ffff0000907c5fd8	b 0xffff0000907c5da8
ffff0000907c5fdc	subs w8,w20,w27
ffff0000907c5fe0	b.eq 0xffff0000907c60a4
ffff0000907c5fe4	mov x9,x8
ffff0000907c5fe8	mov w10,w26
ffff0000907c5fec	mov w11,w27
ffff0000907c5ff0	add w27,w27,#0x1
ffff0000907c5ff4	subs x9,x9,#0x1
ffff0000907c5ff8	ldrb w12,[x19, x11, LSL ]
ffff0000907c5ffc	strb w12,[x19, w10, UXTW ]
ffff0000907c6000	add w10,w10,#0x1
ffff0000907c6004	strb wzr,[x19, x11, LSL ]
ffff0000907c6008	b.ne 0xffff0000907c5fec
ffff0000907c600c	b 0xffff0000907c60a4
ffff0000907c6010	adrp x0,-0xffff6f830000
ffff0000907c6014	add x0,x0,#0xec3
ffff0000907c6018	adrp x1,-0xffff6f833000
ffff0000907c601c	add x1,x1,#0x84a
ffff0000907c6020	bl 0xffff00009077ea84
ffff0000907c6024	add w21,w21,#0x5
ffff0000907c6028	b 0xffff0000907c5da8
ffff0000907c602c	adrp x8,-0xffff6f6f2000
ffff0000907c6030	mov w9,#0xc2
ffff0000907c6034	adrp x10,-0xffff6f6f3000
ffff0000907c6038	add x10,x10,#0x918
ffff0000907c603c	add x1,x19,w25, UXTW
ffff0000907c6040	ldr w8,[x8, #0x538]
ffff0000907c6044	umaddl x8,w8,w9,x10
ffff0000907c6048	add x0,x8,#0x41
ffff0000907c604c	bl 0xffff00009077f488
ffff0000907c6050	cmp w20,w22
ffff0000907c6054	csel w8,w20,w22,hi
ffff0000907c6058	add w27,w27,#0x1
ffff0000907c605c	cmp w27,w20
ffff0000907c6060	b.cs 0xffff0000907c60b0
ffff0000907c6064	ldrb w9,[x19, w27, UXTW ]
ffff0000907c6068	cmp w9,#0x20
ffff0000907c606c	b.eq 0xffff0000907c6058
ffff0000907c6070	b 0xffff0000907c60b4
ffff0000907c6074	subs w8,w20,w27
ffff0000907c6078	b.eq 0xffff0000907c60a4
ffff0000907c607c	mov x9,x8
ffff0000907c6080	mov w10,w26
ffff0000907c6084	mov w11,w27
ffff0000907c6088	add w27,w27,#0x1
ffff0000907c608c	subs x9,x9,#0x1
ffff0000907c6090	ldrb w12,[x19, x11, LSL ]
ffff0000907c6094	strb w12,[x19, w10, UXTW ]
ffff0000907c6098	add w10,w10,#0x1
ffff0000907c609c	strb wzr,[x19, x11, LSL ]
ffff0000907c60a0	b.ne 0xffff0000907c6084
ffff0000907c60a4	add w8,w8,w26
ffff0000907c60a8	strb wzr,[x19, w8, UXTW ]
ffff0000907c60ac	b 0xffff0000907c60f4
ffff0000907c60b0	mov w27,w8
ffff0000907c60b4	mov w21,w26
ffff0000907c60b8	cmp w27,w20
ffff0000907c60bc	b.ne 0xffff0000907c60c8
ffff0000907c60c0	mov w22,w27
ffff0000907c60c4	b 0xffff0000907c60dc
ffff0000907c60c8	mov w22,w27
ffff0000907c60cc	add x0,x19,x21
ffff0000907c60d0	add x1,x19,x22
ffff0000907c60d4	sub w2,w20,w27
ffff0000907c60d8	bl 0xffff00009077f488
ffff0000907c60dc	add x8,x19,x21
ffff0000907c60e0	sub w2,w27,w26
ffff0000907c60e4	add x8,x8,x28
ffff0000907c60e8	mov w1,wzr
ffff0000907c60ec	sub x0,x8,x22
ffff0000907c60f0	bl 0xffff00009077f7b8
ffff0000907c60f4	adrp x9,-0xffff6f6f2000
ffff0000907c60f8	mov w21,wzr
ffff0000907c60fc	ldr w8,[x9, #0x538]
ffff0000907c6100	add w8,w8,#0x1
ffff0000907c6104	str w8,[x9, #0x538]
ffff0000907c6108	b 0xffff0000907c5da8
