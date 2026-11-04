ffff000090789db0	paciasp
ffff000090789db4	sub sp,sp,#0x140
ffff000090789db8	stp x29,x30,[sp, #0x110]
ffff000090789dbc	stp x28,x21,[sp, #0x120]
ffff000090789dc0	stp x20,x19,[sp, #0x130]
ffff000090789dc4	add x29,sp,#0x110
ffff000090789dc8	mov w20,w2
ffff000090789dcc	mov x19,x1
ffff000090789dd0	mov w21,w0
ffff000090789dd4	stur wzr,[x29, #-0x4]
ffff000090789dd8	stp xzr,xzr,[sp, #0xf8]
ffff000090789ddc	stp xzr,xzr,[sp, #0xe8]
ffff000090789de0	stp xzr,xzr,[sp, #0xd8]
ffff000090789de4	stp xzr,xzr,[sp, #0xc8]
ffff000090789de8	stp xzr,xzr,[sp, #0xb8]
ffff000090789dec	stp xzr,xzr,[sp, #0xa8]
ffff000090789df0	stp xzr,xzr,[sp, #0x98]
ffff000090789df4	stp xzr,xzr,[sp, #0x88]
ffff000090789df8	stp xzr,xzr,[sp, #0x78]
ffff000090789dfc	stp xzr,xzr,[sp, #0x68]
ffff000090789e00	stp xzr,xzr,[sp, #0x58]
ffff000090789e04	stp xzr,xzr,[sp, #0x48]
ffff000090789e08	stp xzr,xzr,[sp, #0x38]
ffff000090789e0c	stp xzr,xzr,[sp, #0x28]
ffff000090789e10	stp xzr,xzr,[sp, #0x18]
ffff000090789e14	stp xzr,xzr,[sp, #0x8]
ffff000090789e18	bl 0xffff00009073585c
ffff000090789e1c	cmp w0,#0x2
ffff000090789e20	b.ne 0xffff000090789f84
ffff000090789e24	add x2,sp,#0x8
ffff000090789e28	sub x4,x29,#0x4
ffff000090789e2c	mov w0,wzr
ffff000090789e30	mov w1,w21
ffff000090789e34	mov w3,#0x100
ffff000090789e38	bl 0xffff000090795cec
ffff000090789e3c	mov w1,w0
ffff000090789e40	cmn w0,#0x6
ffff000090789e44	b.ne 0xffff000090789f6c
ffff000090789e48	mov w0,#0x86a0
ffff000090789e4c	movk w0,#0x1, LSL #16
ffff000090789e50	bl 0xffff00009077d808
ffff000090789e54	bl 0xffff00009073585c
ffff000090789e58	cmp w0,#0x2
ffff000090789e5c	b.ne 0xffff000090789f84
ffff000090789e60	add x2,sp,#0x8
ffff000090789e64	sub x4,x29,#0x4
ffff000090789e68	mov w0,wzr
ffff000090789e6c	mov w1,w21
ffff000090789e70	mov w3,#0x100
ffff000090789e74	bl 0xffff000090795cec
ffff000090789e78	mov w1,w0
ffff000090789e7c	cmn w0,#0x6
ffff000090789e80	b.ne 0xffff000090789f6c
ffff000090789e84	mov w0,#0x86a0
ffff000090789e88	movk w0,#0x1, LSL #16
ffff000090789e8c	bl 0xffff00009077d808
ffff000090789e90	bl 0xffff00009073585c
ffff000090789e94	cmp w0,#0x2
ffff000090789e98	b.ne 0xffff000090789f84
ffff000090789e9c	add x2,sp,#0x8
ffff000090789ea0	sub x4,x29,#0x4
ffff000090789ea4	mov w0,wzr
ffff000090789ea8	mov w1,w21
ffff000090789eac	mov w3,#0x100
ffff000090789eb0	bl 0xffff000090795cec
ffff000090789eb4	mov w1,w0
ffff000090789eb8	cmn w0,#0x6
ffff000090789ebc	b.ne 0xffff000090789f6c
ffff000090789ec0	mov w0,#0x86a0
ffff000090789ec4	movk w0,#0x1, LSL #16
ffff000090789ec8	bl 0xffff00009077d808
ffff000090789ecc	bl 0xffff00009073585c
ffff000090789ed0	cmp w0,#0x2
ffff000090789ed4	b.ne 0xffff000090789f84
ffff000090789ed8	add x2,sp,#0x8
ffff000090789edc	sub x4,x29,#0x4
ffff000090789ee0	mov w0,wzr
ffff000090789ee4	mov w1,w21
ffff000090789ee8	mov w3,#0x100
ffff000090789eec	bl 0xffff000090795cec
ffff000090789ef0	mov w1,w0
ffff000090789ef4	cmn w0,#0x6
ffff000090789ef8	b.ne 0xffff000090789f6c
ffff000090789efc	mov w0,#0x86a0
ffff000090789f00	movk w0,#0x1, LSL #16
ffff000090789f04	bl 0xffff00009077d808
ffff000090789f08	bl 0xffff00009073585c
ffff000090789f0c	cmp w0,#0x2
ffff000090789f10	b.ne 0xffff000090789f84
ffff000090789f14	add x2,sp,#0x8
ffff000090789f18	sub x4,x29,#0x4
ffff000090789f1c	mov w0,wzr
ffff000090789f20	mov w1,w21
ffff000090789f24	mov w3,#0x100
ffff000090789f28	bl 0xffff000090795cec
ffff000090789f2c	mov w1,w0
ffff000090789f30	cmn w0,#0x6
ffff000090789f34	b.ne 0xffff000090789f6c
ffff000090789f38	mov w0,#0x86a0
ffff000090789f3c	movk w0,#0x1, LSL #16
ffff000090789f40	bl 0xffff00009077d808
ffff000090789f44	bl 0xffff00009073585c
ffff000090789f48	cmp w0,#0x2
ffff000090789f4c	b.ne 0xffff000090789f84
ffff000090789f50	add x2,sp,#0x8
ffff000090789f54	sub x4,x29,#0x4
ffff000090789f58	mov w0,wzr
ffff000090789f5c	mov w1,w21
ffff000090789f60	mov w3,#0x100
ffff000090789f64	bl 0xffff000090795cec
ffff000090789f68	mov w1,w0
ffff000090789f6c	cbz w1,0xffff000090789fa0
ffff000090789f70	ldur w8,[x29, #-0x4]
ffff000090789f74	cmp w8,#0x7
ffff000090789f78	b.ne 0xffff000090789fa0
ffff000090789f7c	mov w0,#0x6
ffff000090789f80	b 0xffff000090789f88
ffff000090789f84	mov w0,#0x5
ffff000090789f88	ldp x20,x19,[sp, #0x130]
ffff000090789f8c	ldp x28,x21,[sp, #0x120]
ffff000090789f90	ldp x29,x30,[sp, #0x110]
ffff000090789f94	add sp,sp,#0x140
ffff000090789f98	autiasp
ffff000090789f9c	ret
ffff000090789fa0	cbz w1,0xffff000090789fb8
ffff000090789fa4	adrp x0,-0xffff6f81e000
ffff000090789fa8	add x0,x0,#0xabb
ffff000090789fac	bl 0xffff00009077ea84
ffff000090789fb0	mov w0,#0x4
ffff000090789fb4	b 0xffff000090789f88
ffff000090789fb8	mov w2,w20
ffff000090789fbc	add x1,sp,#0x8
ffff000090789fc0	mov x0,x19
ffff000090789fc4	bl 0xffff00009077f488
ffff000090789fc8	mov w0,wzr
ffff000090789fcc	b 0xffff000090789f88
