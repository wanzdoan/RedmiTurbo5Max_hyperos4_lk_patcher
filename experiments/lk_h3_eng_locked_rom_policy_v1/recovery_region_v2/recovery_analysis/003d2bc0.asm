003d2bc0	sub sp,sp,#0x40
003d2bc4	stp x29,x30,[sp, #0x20]
003d2bc8	stp x20,x19,[sp, #0x30]
003d2bcc	add x29,sp,#0x20
003d2bd0	mrs x19,tpidr_el0
003d2bd4	ldr x9,[x19, #0x28]
003d2bd8	stur x9,[x29, #-0x8]
003d2bdc	ldr x9,[x0, #0x10]
003d2be0	cbz x9,0x003d2c00
003d2be4	ldrb w10,[x9]
003d2be8	tbnz w10,#0x0,0x003d2c0c
003d2bec	ldr x10,[x9, #0x10]
003d2bf0	ldr q0,[x9]
003d2bf4	str x10,[sp, #0x10]
003d2bf8	str q0,[sp]
003d2bfc	b 0x003d2c20
003d2c00	strb wzr,[x8]
003d2c04	strb wzr,[x8, #0x18]
003d2c08	b 0x003d2c38
003d2c0c	ldp x2,x1,[x9, #0x8]
003d2c10	mov x0,sp
003d2c14	mov x20,x8
003d2c18	bl 0x00225710
003d2c1c	mov x8,x20
003d2c20	ldr q0,[sp]
003d2c24	mov w10,#0x1
003d2c28	ldr x9,[sp, #0x10]
003d2c2c	str q0,[x8]
003d2c30	str x9,[x8, #0x10]
003d2c34	strb w10,[x8, #0x18]
003d2c38	ldr x8,[x19, #0x28]
003d2c3c	ldur x9,[x29, #-0x8]
003d2c40	cmp x8,x9
003d2c44	b.ne 0x003d2c58
003d2c48	ldp x20,x19,[sp, #0x30]
003d2c4c	ldp x29,x30,[sp, #0x20]
003d2c50	add sp,sp,#0x40
003d2c54	ret
003d2c58	bl 0x004217e0
