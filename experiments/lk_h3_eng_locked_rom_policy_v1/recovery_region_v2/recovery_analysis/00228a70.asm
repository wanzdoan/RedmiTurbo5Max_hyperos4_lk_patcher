00228a70	sub sp,sp,#0x60
00228a74	stp x29,x30,[sp, #0x20]
00228a78	stp x24,x23,[sp, #0x30]
00228a7c	stp x22,x21,[sp, #0x40]
00228a80	stp x20,x19,[sp, #0x50]
00228a84	add x29,sp,#0x20
00228a88	mrs x24,tpidr_el0
00228a8c	mov x21,x0
00228a90	ldr x8,[x24, #0x28]
00228a94	stur x8,[x29, #-0x8]
00228a98	stp xzr,xzr,[x0]
00228a9c	str xzr,[x21, #0x10]!
00228aa0	str x0,[sp, #0x8]
00228aa4	cbz x2,0x00228b00
00228aa8	orr x8,xzr,#-0x5555555555555556
00228aac	mov x19,x0
00228ab0	movk x8,#0xaaab
00228ab4	str xzr,[sp, #0x10]
00228ab8	movk x8,#0xaaa, LSL #48
00228abc	cmp x2,x8
00228ac0	b.cs 0x00228b28
00228ac4	add x8,x2,x2, LSL #0x1
00228ac8	mov x20,x1
00228acc	lsl x23,x8,#0x3
00228ad0	mov x0,x23
00228ad4	bl 0x0041f720
00228ad8	mov x22,x0
00228adc	add x8,x0,x23
00228ae0	stp x0,x0,[x19]
00228ae4	str x8,[x19, #0x10]
00228ae8	add x2,x20,x23
00228aec	mov x0,x21
00228af0	mov x1,x20
00228af4	mov x3,x22
00228af8	bl 0x0022d3b0
00228afc	str x0,[x19, #0x8]
00228b00	ldr x8,[x24, #0x28]
00228b04	ldur x9,[x29, #-0x8]
00228b08	cmp x8,x9
00228b0c	b.ne 0x00228b70
00228b10	ldp x20,x19,[sp, #0x50]
00228b14	ldp x22,x21,[sp, #0x40]
00228b18	ldp x24,x23,[sp, #0x30]
00228b1c	ldp x29,x30,[sp, #0x20]
00228b20	add sp,sp,#0x60
00228b24	ret
00228b28	ldr x8,[x24, #0x28]
00228b2c	ldur x9,[x29, #-0x8]
00228b30	cmp x8,x9
00228b34	b.ne 0x00228b70
00228b38	mov x0,x19
00228b3c	bl 0x00225690
00228b40	mov x20,x0
00228b44	str x22,[x19, #0x8]
00228b48	b 0x00228b50
00228b50	add x0,sp,#0x8
00228b54	bl 0x0022d490
00228b58	ldr x8,[x24, #0x28]
00228b5c	ldur x9,[x29, #-0x8]
00228b60	cmp x8,x9
00228b64	b.ne 0x00228b70
00228b68	mov x0,x20
00228b6c	bl 0x004217d0
00228b70	bl 0x004217e0
