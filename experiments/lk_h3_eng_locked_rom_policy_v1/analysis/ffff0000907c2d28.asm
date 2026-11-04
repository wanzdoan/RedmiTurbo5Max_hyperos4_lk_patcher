ffff0000907c2d28	paciasp
ffff0000907c2d2c	sub sp,sp,#0x140
ffff0000907c2d30	stp x29,x30,[sp, #0x120]
ffff0000907c2d34	stp x28,x19,[sp, #0x130]
ffff0000907c2d38	add x29,sp,#0x120
ffff0000907c2d3c	mov x19,x2
ffff0000907c2d40	add x2,sp,#0x20
ffff0000907c2d44	mov x3,sp
ffff0000907c2d48	mov x4,xzr
ffff0000907c2d4c	stp xzr,xzr,[sp, #0x110]
ffff0000907c2d50	stp xzr,xzr,[sp, #0x100]
ffff0000907c2d54	stp xzr,xzr,[sp, #0xf0]
ffff0000907c2d58	stp xzr,xzr,[sp, #0xe0]
ffff0000907c2d5c	stp xzr,xzr,[sp, #0xd0]
ffff0000907c2d60	stp xzr,xzr,[sp, #0xc0]
ffff0000907c2d64	stp xzr,xzr,[sp, #0xb0]
ffff0000907c2d68	stp xzr,xzr,[sp, #0xa0]
ffff0000907c2d6c	stp xzr,xzr,[sp, #0x90]
ffff0000907c2d70	stp xzr,xzr,[sp, #0x80]
ffff0000907c2d74	stp xzr,xzr,[sp, #0x70]
ffff0000907c2d78	stp xzr,xzr,[sp, #0x60]
ffff0000907c2d7c	stp xzr,xzr,[sp, #0x50]
ffff0000907c2d80	stp xzr,xzr,[sp, #0x40]
ffff0000907c2d84	stp xzr,xzr,[sp, #0x30]
ffff0000907c2d88	stp xzr,xzr,[sp, #0x20]
ffff0000907c2d8c	stp xzr,xzr,[sp, #0x10]
ffff0000907c2d90	stp xzr,xzr,[sp]
ffff0000907c2d94	bl 0xffff0000907c2abc
ffff0000907c2d98	tbz w0,#0x0,0xffff0000907c2df4
ffff0000907c2d9c	adrp x1,-0xffff6f824000
ffff0000907c2da0	add x1,x1,#0x5ac
ffff0000907c2da4	add x0,sp,#0x20
ffff0000907c2da8	mov w2,#0x4
ffff0000907c2dac	bl 0xffff0000907c0e5c
ffff0000907c2db0	cbz w0,0xffff0000907c2e0c
ffff0000907c2db4	adrp x0,-0xffff6f838000
ffff0000907c2db8	add x0,x0,#0x87d
ffff0000907c2dbc	bl 0xffff0000907c119c
ffff0000907c2dc0	adrp x1,-0xffff6f81c000
ffff0000907c2dc4	add x1,x1,#0xabf
ffff0000907c2dc8	adrp x2,-0xffff6f82b000
ffff0000907c2dcc	add x2,x2,#0xb7f
ffff0000907c2dd0	adrp x3,-0xffff6f828000
ffff0000907c2dd4	add x3,x3,#0xdec
ffff0000907c2dd8	adrp x4,-0xffff6f824000
ffff0000907c2ddc	add x4,x4,#0x63d
ffff0000907c2de0	nop
ffff0000907c2de4	adr x6,-0xffff6f8377aa
ffff0000907c2de8	mov x5,sp
ffff0000907c2dec	mov x7,xzr
ffff0000907c2df0	bl 0xffff0000907c0ea8
ffff0000907c2df4	mov w0,wzr
ffff0000907c2df8	ldp x28,x19,[sp, #0x130]
ffff0000907c2dfc	ldp x29,x30,[sp, #0x120]
ffff0000907c2e00	add sp,sp,#0x140
ffff0000907c2e04	autiasp
ffff0000907c2e08	ret
ffff0000907c2e0c	ldr w0,[sp, #0x98]
ffff0000907c2e10	bl 0xffff0000907c1010
ffff0000907c2e14	cbz x19,0xffff0000907c2e2c
ffff0000907c2e18	tst w0,#0x2
ffff0000907c2e1c	mov w0,#0x1
ffff0000907c2e20	cset w8,eq
ffff0000907c2e24	strb w8,[x19]
ffff0000907c2e28	b 0xffff0000907c2df8
ffff0000907c2e2c	mov w0,#0x1
ffff0000907c2e30	b 0xffff0000907c2df8
