0022cbe0	sub sp,sp,#0x80
0022cbe4	stp x29,x30,[sp, #0x30]
0022cbe8	stp x26,x25,[sp, #0x40]
0022cbec	stp x24,x23,[sp, #0x50]
0022cbf0	stp x22,x21,[sp, #0x60]
0022cbf4	stp x20,x19,[sp, #0x70]
0022cbf8	add x29,sp,#0x30
0022cbfc	mrs x22,tpidr_el0
0022cc00	mov x8,x0
0022cc04	ldr x9,[x22, #0x28]
0022cc08	mov x21,x2
0022cc0c	mov x20,x0
0022cc10	mov x19,x1
0022cc14	stur x9,[x29, #-0x8]
0022cc18	ldr x23,[x0, #0x8]
0022cc1c	ldr x9,[x8, #0x10]!
0022cc20	cmp x23,x9
0022cc24	b.cs 0x0022ccc4
0022cc28	subs x8,x19,x23
0022cc2c	b.eq 0x0022cd60
0022cc30	add x9,x19,#0x18
0022cc34	sub x10,x23,#0x18
0022cc38	mov x11,x23
0022cc3c	cmp x10,x23
0022cc40	b.cs 0x0022cc60
0022cc44	ldr x11,[x10, #0x10]
0022cc48	ldr q0,[x10]
0022cc4c	str x11,[x23, #0x10]
0022cc50	add x11,x23,#0x18
0022cc54	str q0,[x23]
0022cc58	stp xzr,xzr,[x10, #0x8]
0022cc5c	str xzr,[x10]
0022cc60	cmp x23,x9
0022cc64	str x11,[x20, #0x8]
0022cc68	b.eq 0x0022cd38
0022cc6c	mov x20,xzr
0022cc70	add x24,x8,#0x18
0022cc74	b 0x0022cca8
0022cc80	sub x8,x25,#0x30
0022cc84	sub x20,x20,#0x18
0022cc88	cmp x24,x20
0022cc8c	ldr x9,[x8, #0x10]
0022cc90	ldr q0,[x8]
0022cc94	str x9,[x26, #0x10]
0022cc98	str q0,[x26]
0022cc9c	strb wzr,[x8]
0022cca0	sturb wzr,[x25, #-0x2f]
0022cca4	b.eq 0x0022cd38
0022cca8	add x25,x23,x20
0022ccac	mov x26,x25
0022ccb0	ldrb w8,[x26, #-0x18]!
0022ccb4	tbz w8,#0x0,0x0022cc80
0022ccb8	ldur x0,[x25, #-0x8]
0022ccbc	bl 0x0041f730
0022ccc0	b 0x0022cc80
0022ccc4	ldr x24,[x20]
0022ccc8	mov x11,#0x1
0022cccc	sub x10,x23,x24
0022ccd0	orr x23,xzr,#-0x5555555555555556
0022ccd4	asr x10,x10,#0x3
0022ccd8	movk x23,#0xaaab
0022ccdc	madd x11,x10,x23,x11
0022cce0	orr x10,xzr,#-0x5555555555555556
0022cce4	movk x10,#0xaaa, LSL #48
0022cce8	cmp x11,x10
0022ccec	b.hi 0x0022ced8
0022ccf0	sub x9,x9,x24
0022ccf4	orr x13,xzr,#0x5555555555555555
0022ccf8	asr x9,x9,#0x3
0022ccfc	movk x13,#0x555, LSL #48
0022cd00	str x8,[sp, #0x20]
0022cd04	mul x9,x9,x23
0022cd08	lsl x12,x9,#0x1
0022cd0c	cmp x12,x11
0022cd10	csel x11,x12,x11,hi
0022cd14	cmp x9,x13
0022cd18	csel x25,x11,x10,cc
0022cd1c	cbz x25,0x0022cd84
0022cd20	cmp x25,x10
0022cd24	b.hi 0x0022cef0
0022cd28	add x8,x25,x25, LSL #0x1
0022cd2c	lsl x0,x8,#0x3
0022cd30	bl 0x0041f720
0022cd34	b 0x0022cd88
0022cd38	ldrb w8,[x19]
0022cd3c	tbz w8,#0x0,0x0022cd48
0022cd40	ldr x0,[x19, #0x10]
0022cd44	bl 0x0041f730
0022cd48	ldr x8,[x21, #0x10]
0022cd4c	ldr q0,[x21]
0022cd50	str x8,[x19, #0x10]
0022cd54	str q0,[x19]
0022cd58	strh wzr,[x21]
0022cd5c	b 0x0022ce70
0022cd60	ldr x8,[x21, #0x10]
0022cd64	ldr q0,[x21]
0022cd68	str x8,[x19, #0x10]
0022cd6c	add x8,x19,#0x18
0022cd70	str q0,[x19]
0022cd74	stp xzr,xzr,[x21, #0x8]
0022cd78	str xzr,[x21]
0022cd7c	str x8,[x20, #0x8]
0022cd80	b 0x0022ce70
0022cd84	mov x0,xzr
0022cd88	sub x8,x19,x24
0022cd8c	mov w9,#0x18
0022cd90	asr x8,x8,#0x3
0022cd94	mul x8,x8,x23
0022cd98	madd x8,x8,x9,x0
0022cd9c	madd x9,x25,x9,x0
0022cda0	stp x0,x8,[sp]
0022cda4	stp x8,x9,[sp, #0x10]
0022cda8	mov x0,sp
0022cdac	mov x1,x21
0022cdb0	bl 0x0022d550
0022cdb4	ldr x21,[sp, #0x8]
0022cdb8	ldr x8,[x20]
0022cdbc	mov x9,x21
0022cdc0	cmp x8,x19
0022cdc4	b.eq 0x0022ce00
0022cdc8	mov x11,x21
0022cdcc	mov x10,x19
0022cdd0	ldur x9,[x10, #-0x8]
0022cdd4	sub x12,x10,#0x18
0022cdd8	ldur q0,[x10, #-0x18]
0022cddc	cmp x12,x8
0022cde0	stur x9,[x11, #-0x8]
0022cde4	sub x9,x11,#0x18
0022cde8	stur q0,[x11, #-0x18]
0022cdec	mov x11,x9
0022cdf0	stp xzr,xzr,[x10, #-0x10]
0022cdf4	stur xzr,[x10, #-0x18]
0022cdf8	mov x10,x12
0022cdfc	b.ne 0x0022cdd0
0022ce00	ldr x10,[x20, #0x8]
0022ce04	str x9,[sp, #0x8]
0022ce08	ldr x8,[sp, #0x10]
0022ce0c	cmp x10,x19
0022ce10	b.eq 0x0022ce3c
0022ce14	ldr x9,[x19, #0x10]
0022ce18	ldr q0,[x19]
0022ce1c	str x9,[x8, #0x10]
0022ce20	str q0,[x8], #0x18
0022ce24	stp xzr,xzr,[x19, #0x8]
0022ce28	str xzr,[x19], #0x18
0022ce2c	cmp x19,x10
0022ce30	b.ne 0x0022ce14
0022ce34	ldr x9,[sp, #0x8]
0022ce38	ldr x19,[x20, #0x8]
0022ce3c	ldr x23,[x20]
0022ce40	stp x9,x8,[x20]
0022ce44	ldr x10,[sp, #0x18]
0022ce48	ldr x8,[x20, #0x10]
0022ce4c	cmp x19,x23
0022ce50	stp x23,x23,[sp]
0022ce54	str x10,[x20, #0x10]
0022ce58	stp x19,x8,[sp, #0x10]
0022ce5c	b.ne 0x0022ceac
0022ce60	cbz x19,0x0022ce6c
0022ce64	mov x0,x19
0022ce68	bl 0x0041f730
0022ce6c	mov x19,x21
0022ce70	ldr x8,[x22, #0x28]
0022ce74	ldur x9,[x29, #-0x8]
0022ce78	cmp x8,x9
0022ce7c	b.ne 0x0022cf28
0022ce80	mov x0,x19
0022ce84	ldp x20,x19,[sp, #0x70]
0022ce88	ldp x22,x21,[sp, #0x60]
0022ce8c	ldp x24,x23,[sp, #0x50]
0022ce90	ldp x26,x25,[sp, #0x40]
0022ce94	ldp x29,x30,[sp, #0x30]
0022ce98	add sp,sp,#0x80
0022ce9c	ret
0022cea0	mov x19,x8
0022cea4	cmp x8,x23
0022cea8	b.eq 0x0022cecc
0022ceac	sub x8,x19,#0x18
0022ceb0	str x8,[sp, #0x10]
0022ceb4	ldurb w9,[x19, #-0x18]
0022ceb8	tbz w9,#0x0,0x0022cea0
0022cebc	ldur x0,[x19, #-0x8]
0022cec0	bl 0x0041f730
0022cec4	ldr x8,[sp, #0x10]
0022cec8	b 0x0022cea0
0022cecc	ldr x19,[sp]
0022ced0	cbnz x19,0x0022ce64
0022ced4	b 0x0022ce6c
0022ced8	ldr x8,[x22, #0x28]
0022cedc	ldur x9,[x29, #-0x8]
0022cee0	cmp x8,x9
0022cee4	b.ne 0x0022cf28
0022cee8	mov x0,x20
0022ceec	bl 0x00225690
0022cef0	ldr x8,[x22, #0x28]
0022cef4	ldur x9,[x29, #-0x8]
0022cef8	cmp x8,x9
0022cefc	b.ne 0x0022cf28
0022cf00	bl 0x002255b0
0022cf28	bl 0x004217e0
