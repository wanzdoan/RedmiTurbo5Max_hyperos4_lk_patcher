003d2c60	sub sp,sp,#0x40
003d2c64	stp x29,x30,[sp, #0x20]
003d2c68	stp x20,x19,[sp, #0x30]
003d2c6c	add x29,sp,#0x20
003d2c70	mrs x19,tpidr_el0
003d2c74	ldr x9,[x19, #0x28]
003d2c78	stur x9,[x29, #-0x8]
003d2c7c	ldr x9,[x0, #0x10]
003d2c80	cbz x9,0x003d2ca4
003d2c84	mov x10,x9
003d2c88	ldrb w11,[x10, #0x18]!
003d2c8c	tbnz w11,#0x0,0x003d2cb0
003d2c90	ldr q0,[x10]
003d2c94	ldr x9,[x10, #0x10]
003d2c98	str q0,[sp]
003d2c9c	str x9,[sp, #0x10]
003d2ca0	b 0x003d2cc4
003d2ca4	strb wzr,[x8]
003d2ca8	strb wzr,[x8, #0x18]
003d2cac	b 0x003d2cdc
003d2cb0	ldp x2,x1,[x9, #0x20]
003d2cb4	mov x0,sp
003d2cb8	mov x20,x8
003d2cbc	bl 0x00225710
003d2cc0	mov x8,x20
003d2cc4	ldr q0,[sp]
003d2cc8	mov w10,#0x1
003d2ccc	ldr x9,[sp, #0x10]
003d2cd0	str q0,[x8]
003d2cd4	str x9,[x8, #0x10]
003d2cd8	strb w10,[x8, #0x18]
003d2cdc	ldr x8,[x19, #0x28]
003d2ce0	ldur x9,[x29, #-0x8]
003d2ce4	cmp x8,x9
003d2ce8	b.ne 0x003d2cfc
003d2cec	ldp x20,x19,[sp, #0x30]
003d2cf0	ldp x29,x30,[sp, #0x20]
003d2cf4	add sp,sp,#0x40
003d2cf8	ret
003d2cfc	bl 0x004217e0
