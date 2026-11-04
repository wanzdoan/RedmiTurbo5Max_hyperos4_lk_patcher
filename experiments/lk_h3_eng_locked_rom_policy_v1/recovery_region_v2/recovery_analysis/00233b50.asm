00233b50	sub sp,sp,#0x50
00233b54	stp x29,x30,[sp, #0x30]
00233b58	str x19,[sp, #0x40]
00233b5c	add x29,sp,#0x30
00233b60	mrs x19,tpidr_el0
00233b64	mov x9,sp
00233b68	ldr x8,[x19, #0x28]
00233b6c	movi v0.2D,#0x0
00233b70	stur x8,[x29, #-0x8]
00233b74	adrp x8,0x42c000
00233b78	str x0,[x8, #0xfc0]
00233b7c	add x0,x9,#0x10
00233b80	stp q0,q0,[sp]
00233b84	bl 0x0041ff40
00233b88	mov w8,#0x4
00233b8c	nop
00233b90	adr x9,0x2332d0
00233b94	movk w8,#0x1800, LSL #16
00233b98	mov x1,sp
00233b9c	mov w0,#0x6
00233ba0	mov x2,xzr
00233ba4	str x9,[sp, #0x8]
00233ba8	str w8,[sp]
00233bac	bl 0x0041ff50
00233bb0	mov x1,sp
00233bb4	mov w0,#0x7
00233bb8	mov x2,xzr
00233bbc	bl 0x0041ff50
00233bc0	mov x1,sp
00233bc4	mov w0,#0x8
00233bc8	mov x2,xzr
00233bcc	bl 0x0041ff50
00233bd0	mov x1,sp
00233bd4	mov w0,#0x4
00233bd8	mov x2,xzr
00233bdc	bl 0x0041ff50
00233be0	mov x1,sp
00233be4	mov w0,#0xd
00233be8	mov x2,xzr
00233bec	bl 0x0041ff50
00233bf0	mov x1,sp
00233bf4	mov w0,#0xb
00233bf8	mov x2,xzr
00233bfc	bl 0x0041ff50
00233c00	mov x1,sp
00233c04	mov w0,#0x10
00233c08	mov x2,xzr
00233c0c	bl 0x0041ff50
00233c10	mov x1,sp
00233c14	mov w0,#0x5
00233c18	mov x2,xzr
00233c1c	bl 0x0041ff50
00233c20	ldr x8,[x19, #0x28]
00233c24	ldur x9,[x29, #-0x8]
00233c28	cmp x8,x9
00233c2c	b.ne 0x00233c40
00233c30	ldp x29,x30,[sp, #0x30]
00233c34	ldr x19,[sp, #0x40]
00233c38	add sp,sp,#0x50
00233c3c	ret
00233c40	bl 0x004217e0
