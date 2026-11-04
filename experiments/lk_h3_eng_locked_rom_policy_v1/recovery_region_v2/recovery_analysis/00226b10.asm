00226b10	sub sp,sp,#0x70
00226b14	stp x29,x30,[sp, #0x20]
00226b18	stp x26,x25,[sp, #0x30]
00226b1c	stp x24,x23,[sp, #0x40]
00226b20	stp x22,x21,[sp, #0x50]
00226b24	stp x20,x19,[sp, #0x60]
00226b28	add x29,sp,#0x20
00226b2c	mrs x24,tpidr_el0
00226b30	mov x21,x2
00226b34	mov x20,x1
00226b38	mov x19,x0
00226b3c	ldr x8,[x24, #0x28]
00226b40	stur x8,[x29, #-0x8]
00226b44	stp xzr,xzr,[sp]
00226b48	mov x0,sp
00226b4c	mov x1,x19
00226b50	bl 0x0041fb20
00226b54	ldrb w8,[sp]
00226b58	cbz w8,0x00226c04
00226b5c	ldr x8,[x19]
00226b60	ldur x8,[x8, #-0x18]
00226b64	add x4,x19,x8
00226b68	ldr w5,[x4, #0x90]
00226b6c	ldr x22,[x4, #0x28]
00226b70	ldr w25,[x4, #0x8]
00226b74	cmn w5,#0x1
00226b78	b.ne 0x00226bc4
00226b7c	add x8,sp,#0x10
00226b80	mov x0,x4
00226b84	mov x26,x4
00226b88	bl 0x0041fa50
00226b8c	adrp x1,0x429000
00226b90	ldr x1,[x1, #0xb40]
00226b94	add x0,sp,#0x10
00226b98	bl 0x0041faa0
00226b9c	ldr x8,[x0]
00226ba0	ldr x8,[x8, #0x38]
00226ba4	mov w1,#0x20
00226ba8	blr x8
00226bac	mov w23,w0
00226bb0	add x0,sp,#0x10
00226bb4	bl 0x0041fa60
00226bb8	and w5,w23,#0xff
00226bbc	mov x4,x26
00226bc0	str w5,[x26, #0x90]
00226bc4	mov w8,#0xb0
00226bc8	add x3,x20,x21
00226bcc	and w8,w25,w8
00226bd0	cmp w8,#0x20
00226bd4	csel x2,x3,x20,eq
00226bd8	mov x0,x22
00226bdc	mov x1,x20
00226be0	bl 0x00226cb0
00226be4	cbnz x0,0x00226c04
00226be8	ldr x8,[x19]
00226bec	mov w9,#0x5
00226bf0	ldur x8,[x8, #-0x18]
00226bf4	add x0,x19,x8
00226bf8	ldr w8,[x0, #0x20]
00226bfc	orr w1,w8,w9
00226c00	bl 0x0041fa30
00226c04	mov x0,sp
00226c08	bl 0x0041fb30
00226c0c	ldr x8,[x24, #0x28]
00226c10	ldur x9,[x29, #-0x8]
00226c14	cmp x8,x9
00226c18	b.ne 0x00226ca4
00226c1c	mov x0,x19
00226c20	ldp x20,x19,[sp, #0x60]
00226c24	ldp x22,x21,[sp, #0x50]
00226c28	ldp x24,x23,[sp, #0x40]
00226c2c	ldp x26,x25,[sp, #0x30]
00226c30	ldp x29,x30,[sp, #0x20]
00226c34	add sp,sp,#0x70
00226c38	ret
00226ca4	bl 0x004217e0
