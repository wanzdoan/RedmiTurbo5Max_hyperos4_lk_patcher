00101d9c	paciasp
00101da0	sub sp,sp,#0xf0
00101da4	stp x29,x30,[sp, #0xa0]
00101da8	str x25,[sp, #0xb0]
00101dac	stp x24,x23,[sp, #0xc0]
00101db0	stp x22,x21,[sp, #0xd0]
00101db4	stp x20,x19,[sp, #0xe0]
00101db8	add x29,sp,#0xa0
00101dbc	mrs x8,sp_el0
00101dc0	adrp x20,0x10c000
00101dc4	ldr x8,[x8, #0x620]
00101dc8	stur x8,[x29, #-0x8]
00101dcc	ldr x1,[x20, #0xd78]
00101dd0	stp xzr,xzr,[x29, #-0x18]
00101dd4	stp xzr,xzr,[x29, #-0x28]
00101dd8	stp xzr,xzr,[x29, #-0x38]
00101ddc	stp xzr,xzr,[x29, #-0x48]
00101de0	str wzr,[sp, #0x50]
00101de4	stp xzr,xzr,[sp, #0x40]
00101de8	stp xzr,xzr,[sp, #0x30]
00101dec	stp xzr,xzr,[sp, #0x20]
00101df0	stp xzr,xzr,[sp, #0x10]
00101df4	str xzr,[sp, #0x8]
00101df8	cbz x1,0x00101ea8
00101dfc	adrp x21,0x10c000
00101e00	ldr w8,[x21, #0xd80]
00101e04	cbz w8,0x00101ea0
00101e08	mov w22,wzr
00101e0c	add x23,sp,#0x8
00101e10	adrp x24,0x10c000
00101e14	adrp x19,0x110000
00101e18	add x19,x19,#0x3ff
00101e1c	add x0,sp,#0x8
00101e20	mov w2,#0x4c
00101e24	bl 0x00116000
00101e28	ldr w25,[x24, #0xd84]
00101e2c	sub x0,x29,#0x48
00101e30	add x3,sp,#0x8
00101e34	mov w1,#0x40
00101e38	mov x2,x19
00101e3c	bl 0x00116008
00101e40	cmp w25,#0x1
00101e44	add x10,x23,#0x40
00101e48	add x11,x23,#0x10
00101e4c	add x8,x23,#0x44
00101e50	add x9,x23,#0x14
00101e54	csel x10,x10,x11,hi
00101e58	csel x8,x8,x9,hi
00101e5c	add x9,x23,#0x48
00101e60	add x12,x23,#0x18
00101e64	ldr x11,[x20, #0xd78]
00101e68	ldr w10,[x10]
00101e6c	csel x9,x9,x12,hi
00101e70	ldr w2,[x8]
00101e74	ldr w25,[x9]
00101e78	sub x0,x29,#0x48
00101e7c	add x1,x11,x10
00101e80	mov w3,wzr
00101e84	bl 0x00108c08
00101e88	ldr w8,[x21, #0xd80]
00101e8c	ldr x9,[x20, #0xd78]
00101e90	add w22,w22,#0x1
00101e94	cmp w22,w8
00101e98	add x1,x9,x25
00101e9c	b.cc 0x00101e1c
00101ea0	mov w0,wzr
00101ea4	b 0x00101ee4
00101ea8	adrp x19,0x110000
00101eac	add x19,x19,#0x1a4
00101eb0	adrp x20,0x112000
00101eb4	add x20,x20,#0x487
00101eb8	mov w0,wzr
00101ebc	mov w1,#0x1
00101ec0	mov x2,x19
00101ec4	mov x3,x20
00101ec8	bl 0x00104638
00101ecc	mov w0,#0x2
00101ed0	mov w1,#0x1
00101ed4	mov x2,x19
00101ed8	mov x3,x20
00101edc	bl 0x00104638
00101ee0	mov w0,#0xffffffff
00101ee4	mrs x8,sp_el0
00101ee8	ldr x8,[x8, #0x620]
00101eec	ldur x9,[x29, #-0x8]
00101ef0	cmp x8,x9
00101ef4	b.ne 0x00101f18
00101ef8	ldp x20,x19,[sp, #0xe0]
00101efc	ldr x25,[sp, #0xb0]
00101f00	ldp x22,x21,[sp, #0xd0]
00101f04	ldp x24,x23,[sp, #0xc0]
00101f08	ldp x29,x30,[sp, #0xa0]
00101f0c	add sp,sp,#0xf0
00101f10	autiasp
00101f14	ret
00101f18	bl 0x00116010
