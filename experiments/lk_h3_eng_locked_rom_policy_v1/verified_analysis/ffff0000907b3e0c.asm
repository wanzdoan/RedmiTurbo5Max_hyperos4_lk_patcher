ffff0000907b3e0c	paciasp
ffff0000907b3e10	sub sp,sp,#0x60
ffff0000907b3e14	stp x29,x30,[sp, #0x20]
ffff0000907b3e18	str x23,[sp, #0x30]
ffff0000907b3e1c	stp x22,x21,[sp, #0x40]
ffff0000907b3e20	stp x20,x19,[sp, #0x50]
ffff0000907b3e24	add x29,sp,#0x20
ffff0000907b3e28	stp xzr,xzr,[sp, #0x10]
ffff0000907b3e2c	stp xzr,xzr,[sp]
ffff0000907b3e30	cbz x0,0xffff0000907b3f5c
ffff0000907b3e34	mov x19,x0
ffff0000907b3e38	adrp x0,-0xffff6f6f3000
ffff0000907b3e3c	add x0,x0,#0x6c0
ffff0000907b3e40	mov x2,sp
ffff0000907b3e44	mov w1,#0x1c
ffff0000907b3e48	bl 0xffff0000907a4b20
ffff0000907b3e4c	cbz w0,0xffff0000907b3e6c
ffff0000907b3e50	mov w2,w0
ffff0000907b3e54	adrp x0,-0xffff6f80d000
ffff0000907b3e58	add x0,x0,#0xbdc
ffff0000907b3e5c	adrp x1,-0xffff6f804000
ffff0000907b3e60	add x1,x1,#0x53b
ffff0000907b3e64	bl 0xffff00009077ea84
ffff0000907b3e68	b 0xffff0000907b3f5c
ffff0000907b3e6c	mov x1,sp
ffff0000907b3e70	mov x0,x19
ffff0000907b3e74	mov w2,#0x20
ffff0000907b3e78	mov x22,sp
ffff0000907b3e7c	bl 0xffff00009077f450
ffff0000907b3e80	cbz w0,0xffff0000907b3f5c
ffff0000907b3e84	adrp x20,-0xffff6f804000
ffff0000907b3e88	add x20,x20,#0x53b
ffff0000907b3e8c	adrp x0,-0xffff6f81f000
ffff0000907b3e90	add x0,x0,#0x2df
ffff0000907b3e94	mov x1,x20
ffff0000907b3e98	bl 0xffff00009077ea84
ffff0000907b3e9c	adrp x0,-0xffff6f82e000
ffff0000907b3ea0	add x0,x0,#0x6ab
ffff0000907b3ea4	mov x1,x20
ffff0000907b3ea8	bl 0xffff00009077ea84
ffff0000907b3eac	mov x23,xzr
ffff0000907b3eb0	adrp x20,-0xffff6f834000
ffff0000907b3eb4	add x20,x20,#0xf39
ffff0000907b3eb8	adrp x21,-0xffff6f80f000
ffff0000907b3ebc	add x21,x21,#0x4e
ffff0000907b3ec0	b 0xffff0000907b3ecc
ffff0000907b3ec4	cmp x23,#0x20
ffff0000907b3ec8	b.eq 0xffff0000907b3ef0
ffff0000907b3ecc	ldrb w1,[x22, x23, LSL ]
ffff0000907b3ed0	mov x0,x20
ffff0000907b3ed4	bl 0xffff00009077ea84
ffff0000907b3ed8	add x23,x23,#0x1
ffff0000907b3edc	tst x23,#0xf
ffff0000907b3ee0	b.ne 0xffff0000907b3ec4
ffff0000907b3ee4	mov x0,x21
ffff0000907b3ee8	bl 0xffff00009077ea84
ffff0000907b3eec	b 0xffff0000907b3ec4
ffff0000907b3ef0	adrp x20,-0xffff6f80f000
ffff0000907b3ef4	add x20,x20,#0x4e
ffff0000907b3ef8	mov x0,x20
ffff0000907b3efc	bl 0xffff00009077ea84
ffff0000907b3f00	adrp x0,-0xffff6f821000
ffff0000907b3f04	add x0,x0,#0x99e
ffff0000907b3f08	adrp x1,-0xffff6f804000
ffff0000907b3f0c	add x1,x1,#0x53b
ffff0000907b3f10	bl 0xffff00009077ea84
ffff0000907b3f14	mov x22,xzr
ffff0000907b3f18	adrp x21,-0xffff6f834000
ffff0000907b3f1c	add x21,x21,#0xf39
ffff0000907b3f20	b 0xffff0000907b3f2c
ffff0000907b3f24	cmp x22,#0x20
ffff0000907b3f28	b.eq 0xffff0000907b3f50
ffff0000907b3f2c	ldrb w1,[x19, x22, LSL ]
ffff0000907b3f30	mov x0,x21
ffff0000907b3f34	bl 0xffff00009077ea84
ffff0000907b3f38	add x22,x22,#0x1
ffff0000907b3f3c	tst x22,#0xf
ffff0000907b3f40	b.ne 0xffff0000907b3f24
ffff0000907b3f44	mov x0,x20
ffff0000907b3f48	bl 0xffff00009077ea84
ffff0000907b3f4c	b 0xffff0000907b3f24
ffff0000907b3f50	adrp x0,-0xffff6f80f000
ffff0000907b3f54	add x0,x0,#0x4e
ffff0000907b3f58	bl 0xffff00009077ea84
ffff0000907b3f5c	ldp x20,x19,[sp, #0x50]
ffff0000907b3f60	ldp x22,x21,[sp, #0x40]
ffff0000907b3f64	ldp x29,x30,[sp, #0x20]
ffff0000907b3f68	ldr x23,[sp, #0x30]
ffff0000907b3f6c	add sp,sp,#0x60
ffff0000907b3f70	autiasp
ffff0000907b3f74	ret
