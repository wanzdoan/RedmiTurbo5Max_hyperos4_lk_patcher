0022ea30	sub sp,sp,#0xa0
0022ea34	stp x29,x30,[sp, #0x60]
0022ea38	stp x24,x23,[sp, #0x70]
0022ea3c	stp x22,x21,[sp, #0x80]
0022ea40	stp x20,x19,[sp, #0x90]
0022ea44	add x29,sp,#0x60
0022ea48	mrs x23,tpidr_el0
0022ea4c	mov x19,x0
0022ea50	ldr x8,[x23, #0x28]
0022ea54	mov x0,x1
0022ea58	mov x20,x1
0022ea5c	stur x8,[x29, #-0x8]
0022ea60	stp xzr,xzr,[x29, #-0x20]
0022ea64	stur xzr,[x29, #-0x10]
0022ea68	bl 0x0041f860
0022ea6c	cmn x0,#0x10
0022ea70	b.cs 0x0022ec0c
0022ea74	mov x21,x0
0022ea78	cmp x0,#0x17
0022ea7c	b.cs 0x0022ea98
0022ea80	sub x9,x29,#0x20
0022ea84	lsl w8,w21,#0x1
0022ea88	orr x22,x9,#0x1
0022ea8c	sturb w8,[x29, #-0x20]
0022ea90	cbnz x21,0x0022eab4
0022ea94	b 0x0022eac4
0022ea98	orr x24,x21,#0xf
0022ea9c	add x0,x24,#0x1
0022eaa0	bl 0x0041f720
0022eaa4	add x8,x24,#0x2
0022eaa8	mov x22,x0
0022eaac	stp x21,x0,[x29, #-0x18]
0022eab0	stur x8,[x29, #-0x20]
0022eab4	mov x0,x22
0022eab8	mov x1,x20
0022eabc	mov x2,x21
0022eac0	bl 0x004217b0
0022eac4	strb wzr,[x22, x21, LSL ]
0022eac8	ldr x8,[x19]
0022eacc	ldr w0,[x8]
0022ead0	cmp w0,#0x1
0022ead4	b.lt 0x0022eb58
0022ead8	add x8,sp,#0x8
0022eadc	bl 0x0041f7d0
0022eae0	adrp x2,0x148000
0022eae4	add x2,x2,#0x2c2
0022eae8	add x0,sp,#0x8
0022eaec	mov x1,xzr
0022eaf0	mov w3,#0x1
0022eaf4	bl 0x0041fb60
0022eaf8	ldr x8,[x0, #0x10]
0022eafc	add x10,sp,#0x20
0022eb00	ldr q0,[x0]
0022eb04	orr x10,x10,#0x1
0022eb08	str x8,[sp, #0x30]
0022eb0c	str q0,[sp, #0x20]
0022eb10	stp xzr,xzr,[x0, #0x8]
0022eb14	str xzr,[x0]
0022eb18	ldp x11,x9,[sp, #0x28]
0022eb1c	ldrb w8,[sp, #0x20]
0022eb20	tst w8,#0x1
0022eb24	lsr x8,x8,#0x1
0022eb28	csel x1,x10,x9,eq
0022eb2c	csel x2,x8,x11,eq
0022eb30	sub x0,x29,#0x20
0022eb34	bl 0x0041fd00
0022eb38	ldrb w8,[sp, #0x20]
0022eb3c	tbz w8,#0x0,0x0022eb48
0022eb40	ldr x0,[sp, #0x30]
0022eb44	bl 0x0041f730
0022eb48	ldrb w8,[sp, #0x8]
0022eb4c	tbz w8,#0x0,0x0022eb58
0022eb50	ldr x0,[sp, #0x18]
0022eb54	bl 0x0041f730
0022eb58	sub x0,x29,#0x20
0022eb5c	bl 0x00394790
0022eb60	cbnz w0,0x0022eb88
0022eb64	ldurb w8,[x29, #-0x20]
0022eb68	sub x10,x29,#0x20
0022eb6c	ldur x9,[x29, #-0x10]
0022eb70	orr x10,x10,#0x1
0022eb74	mov w1,#0x4
0022eb78	tst w8,#0x1
0022eb7c	csel x0,x10,x9,eq
0022eb80	bl 0x0041fd10
0022eb84	cbz w0,0x0022ebc0
0022eb88	ldurb w8,[x29, #-0x20]
0022eb8c	tbz w8,#0x0,0x0022eb98
0022eb90	ldur x0,[x29, #-0x10]
0022eb94	bl 0x0041f730
0022eb98	ldr x8,[x23, #0x28]
0022eb9c	ldur x9,[x29, #-0x8]
0022eba0	cmp x8,x9
0022eba4	b.ne 0x0022ec60
0022eba8	ldp x20,x19,[sp, #0x90]
0022ebac	ldp x22,x21,[sp, #0x80]
0022ebb0	ldp x24,x23,[sp, #0x70]
0022ebb4	ldp x29,x30,[sp, #0x60]
0022ebb8	add sp,sp,#0xa0
0022ebbc	ret
0022ebc0	ldr x19,[x19, #0x8]
0022ebc4	ldp x8,x9,[x19, #0x8]
0022ebc8	cmp x8,x9
0022ebcc	b.cs 0x0022ebf0
0022ebd0	ldur x9,[x29, #-0x10]
0022ebd4	add x0,x8,#0x18
0022ebd8	ldur q0,[x29, #-0x20]
0022ebdc	str x9,[x8, #0x10]
0022ebe0	str q0,[x8]
0022ebe4	stp xzr,xzr,[x29, #-0x18]
0022ebe8	stur xzr,[x29, #-0x20]
0022ebec	b 0x0022ebfc
0022ebf0	sub x1,x29,#0x20
0022ebf4	mov x0,x19
0022ebf8	bl 0x00225ed0
0022ebfc	str x0,[x19, #0x8]
0022ec00	ldurb w8,[x29, #-0x20]
0022ec04	tbnz w8,#0x0,0x0022eb90
0022ec08	b 0x0022eb98
0022ec0c	ldr x8,[x23, #0x28]
0022ec10	ldur x9,[x29, #-0x8]
0022ec14	cmp x8,x9
0022ec18	b.ne 0x0022ec60
0022ec1c	sub x0,x29,#0x20
0022ec20	bl 0x00225510
0022ec24	ldrb w8,[sp, #0x20]
0022ec28	mov x19,x0
0022ec2c	tbnz w8,#0x0,0x0022ec64
0022ec30	ldrb w8,[sp, #0x8]
0022ec34	tbnz w8,#0x0,0x0022ec84
0022ec38	ldurb w8,[x29, #-0x20]
0022ec3c	tbz w8,#0x0,0x0022ec48
0022ec40	ldur x0,[x29, #-0x10]
0022ec44	bl 0x0041f730
0022ec48	ldr x8,[x23, #0x28]
0022ec4c	ldur x9,[x29, #-0x8]
0022ec50	cmp x8,x9
0022ec54	b.ne 0x0022ec60
0022ec58	mov x0,x19
0022ec5c	bl 0x004217d0
0022ec60	bl 0x004217e0
0022ec64	ldr x0,[sp, #0x30]
0022ec68	bl 0x0041f730
0022ec6c	ldrb w8,[sp, #0x8]
0022ec70	tbz w8,#0x0,0x0022ec38
0022ec74	b 0x0022ec84
0022ec84	ldr x0,[sp, #0x18]
0022ec88	bl 0x0041f730
0022ec8c	ldurb w8,[x29, #-0x20]
0022ec90	tbnz w8,#0x0,0x0022ec40
0022ec94	b 0x0022ec48
