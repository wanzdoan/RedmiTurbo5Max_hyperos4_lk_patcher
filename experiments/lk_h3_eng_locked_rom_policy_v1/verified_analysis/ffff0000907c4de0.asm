ffff0000907c4de0	paciasp
ffff0000907c4de4	sub sp,sp,#0x170
ffff0000907c4de8	stp x29,x30,[sp, #0x150]
ffff0000907c4dec	stp x28,x19,[sp, #0x160]
ffff0000907c4df0	add x29,sp,#0x150
ffff0000907c4df4	mov w8,#0x1
ffff0000907c4df8	adrp x0,-0xffff6f82e000
ffff0000907c4dfc	add x0,x0,#0x811
ffff0000907c4e00	stp xzr,xzr,[sp, #0x140]
ffff0000907c4e04	stp xzr,xzr,[sp, #0x130]
ffff0000907c4e08	stp xzr,xzr,[sp, #0x120]
ffff0000907c4e0c	stp xzr,xzr,[sp, #0x110]
ffff0000907c4e10	stp xzr,xzr,[sp, #0x100]
ffff0000907c4e14	stp xzr,xzr,[sp, #0xf0]
ffff0000907c4e18	stp xzr,xzr,[sp, #0xe0]
ffff0000907c4e1c	stp xzr,xzr,[sp, #0xd0]
ffff0000907c4e20	stp xzr,xzr,[sp, #0xc0]
ffff0000907c4e24	stp xzr,xzr,[sp, #0xb0]
ffff0000907c4e28	stp xzr,xzr,[sp, #0xa0]
ffff0000907c4e2c	stp xzr,xzr,[sp, #0x90]
ffff0000907c4e30	stp xzr,xzr,[sp, #0x80]
ffff0000907c4e34	stp xzr,xzr,[sp, #0x70]
ffff0000907c4e38	stp xzr,xzr,[sp, #0x60]
ffff0000907c4e3c	stp xzr,xzr,[sp, #0x50]
ffff0000907c4e40	stp xzr,xzr,[sp, #0x40]
ffff0000907c4e44	stp xzr,xzr,[sp, #0x30]
ffff0000907c4e48	str w8,[sp, #0x2c]
ffff0000907c4e4c	stp xzr,xzr,[sp, #0x18]
ffff0000907c4e50	stp xzr,xzr,[sp, #0x8]
ffff0000907c4e54	bl 0xffff00009077ea84
ffff0000907c4e58	add x0,sp,#0x50
ffff0000907c4e5c	mov w1,#0x100
ffff0000907c4e60	bl 0xffff000090788578
ffff0000907c4e64	cbz w0,0xffff0000907c4e74
ffff0000907c4e68	adrp x0,-0xffff6f814000
ffff0000907c4e6c	add x0,x0,#0xa53
ffff0000907c4e70	bl 0xffff00009077ea84
ffff0000907c4e74	add x0,sp,#0x50
ffff0000907c4e78	add x2,sp,#0x30
ffff0000907c4e7c	mov w1,#0x100
ffff0000907c4e80	bl 0xffff00009078847c
ffff0000907c4e84	cbz w0,0xffff0000907c4e94
ffff0000907c4e88	adrp x0,-0xffff6f817000
ffff0000907c4e8c	add x0,x0,#0x6b1
ffff0000907c4e90	bl 0xffff00009077ea84
ffff0000907c4e94	add x0,sp,#0x2c
ffff0000907c4e98	bl 0xffff00009078856c
ffff0000907c4e9c	cbz w0,0xffff0000907c4eac
ffff0000907c4ea0	adrp x0,-0xffff6f807000
ffff0000907c4ea4	add x0,x0,#0x9f
ffff0000907c4ea8	bl 0xffff00009077ea84
ffff0000907c4eac	ldr w8,[sp, #0x2c]
ffff0000907c4eb0	cbz w8,0xffff0000907c4ecc
ffff0000907c4eb4	ldp x1,x2,[sp, #0x30]
ffff0000907c4eb8	mov w0,#0x120
ffff0000907c4ebc	add x8,sp,#0x8
ffff0000907c4ec0	ldp x3,x4,[sp, #0x40]
ffff0000907c4ec4	movk w0,#0xc200, LSL #16
ffff0000907c4ec8	b 0xffff0000907c4ee8
ffff0000907c4ecc	mov w0,#0x120
ffff0000907c4ed0	add x8,sp,#0x8
ffff0000907c4ed4	movk w0,#0xc200, LSL #16
ffff0000907c4ed8	mov x1,xzr
ffff0000907c4edc	mov x2,xzr
ffff0000907c4ee0	mov x3,xzr
ffff0000907c4ee4	mov x4,xzr
ffff0000907c4ee8	mov x5,xzr
ffff0000907c4eec	mov x6,xzr
ffff0000907c4ef0	mov x7,xzr
ffff0000907c4ef4	str x8,[sp]
ffff0000907c4ef8	bl 0xffff00009071e714
ffff0000907c4efc	adrp x8,-0xffff6f6f3000
ffff0000907c4f00	adrp x9,-0xffff6f6f3000
ffff0000907c4f04	adrp x10,-0xffff6f6f3000
ffff0000907c4f08	mov w0,#0x120
ffff0000907c4f0c	ldrsw x1,[sp, #0x2c]
ffff0000907c4f10	add x19,sp,#0x8
ffff0000907c4f14	ldr w2,[x8, #0x900]
ffff0000907c4f18	movk w0,#0xc200, LSL #16
ffff0000907c4f1c	ldr w3,[x9, #0x8d0]
ffff0000907c4f20	mov x5,xzr
ffff0000907c4f24	ldr w4,[x10, #0x8d4]
ffff0000907c4f28	mov x6,xzr
ffff0000907c4f2c	mov x7,xzr
ffff0000907c4f30	str x19,[sp]
ffff0000907c4f34	bl 0xffff00009071e714
ffff0000907c4f38	adrp x8,-0xffff6f6f3000
ffff0000907c4f3c	add x8,x8,#0x8e0
ffff0000907c4f40	mov w0,#0x120
ffff0000907c4f44	mov x5,xzr
ffff0000907c4f48	movk w0,#0xc200, LSL #16
ffff0000907c4f4c	mov x6,xzr
ffff0000907c4f50	ldp x1,x2,[x8]
ffff0000907c4f54	mov x7,xzr
ffff0000907c4f58	str x19,[sp]
ffff0000907c4f5c	ldp x3,x4,[x8, #0x10]
ffff0000907c4f60	bl 0xffff00009071e714
ffff0000907c4f64	adrp x8,-0xffff6f6f3000
ffff0000907c4f68	adrp x9,-0xffff6f6f3000
ffff0000907c4f6c	mov w0,#0x120
ffff0000907c4f70	mov x3,xzr
ffff0000907c4f74	movk w0,#0xc200, LSL #16
ffff0000907c4f78	mov x4,xzr
ffff0000907c4f7c	ldr w1,[x8, #0x8d8]
ffff0000907c4f80	mov x5,xzr
ffff0000907c4f84	ldr w2,[x9, #0x8dc]
ffff0000907c4f88	mov x6,xzr
ffff0000907c4f8c	mov x7,xzr
ffff0000907c4f90	str x19,[sp]
ffff0000907c4f94	bl 0xffff00009071e714
ffff0000907c4f98	ldr x1,[sp, #0x8]
ffff0000907c4f9c	cbz x1,0xffff0000907c4fac
ffff0000907c4fa0	adrp x0,-0xffff6f81a000
ffff0000907c4fa4	add x0,x0,#0x480
ffff0000907c4fa8	bl 0xffff00009077ea84
ffff0000907c4fac	mov w0,#0x120
ffff0000907c4fb0	mov x1,xzr
ffff0000907c4fb4	movk w0,#0xc200, LSL #16
ffff0000907c4fb8	mov x2,xzr
ffff0000907c4fbc	mov x3,xzr
ffff0000907c4fc0	mov x4,xzr
ffff0000907c4fc4	mov x5,xzr
ffff0000907c4fc8	mov x6,xzr
ffff0000907c4fcc	mov x7,xzr
ffff0000907c4fd0	str x19,[sp]
ffff0000907c4fd4	bl 0xffff00009071e714
ffff0000907c4fd8	ldr x8,[sp, #0x8]
ffff0000907c4fdc	cbnz x8,0xffff0000907c4ff0
ffff0000907c4fe0	adrp x0,-0xffff6f831000
ffff0000907c4fe4	add x0,x0,#0x395
ffff0000907c4fe8	mov x1,xzr
ffff0000907c4fec	bl 0xffff00009077ea84
ffff0000907c4ff0	ldp x28,x19,[sp, #0x160]
ffff0000907c4ff4	ldp x29,x30,[sp, #0x150]
ffff0000907c4ff8	add sp,sp,#0x170
ffff0000907c4ffc	autiasp
ffff0000907c5000	ret
