00376d40	sub sp,sp,#0x60
00376d44	stp x29,x30,[sp, #0x40]
00376d48	stp x20,x19,[sp, #0x50]
00376d4c	add x29,sp,#0x40
00376d50	mrs x20,tpidr_el0
00376d54	ldr x8,[x20, #0x28]
00376d58	stur x8,[x29, #-0x8]
00376d5c	bl 0x00376e50
00376d60	and w8,w0,#0xff
00376d64	cmp w8,#0x1
00376d68	b.ne 0x00376d74
00376d6c	mov w19,#0x1
00376d70	b 0x00376df8
00376d74	mov w8,#0x3002
00376d78	strb wzr,[sp, #0xa]
00376d7c	strh w8,[sp, #0x8]
00376d80	adrp x0,0x1d0000
00376d84	add x0,x0,#0xa58
00376d88	add x8,sp,#0x20
00376d8c	add x1,sp,#0x8
00376d90	bl 0x0040f330
00376d94	ldrb w8,[sp, #0x20]
00376d98	ldr x9,[sp, #0x28]
00376d9c	lsr x10,x8,#0x1
00376da0	tst w8,#0x1
00376da4	csel x9,x10,x9,eq
00376da8	cmp x9,#0x1
00376dac	b.ne 0x00376dd8
00376db0	ldr x9,[sp, #0x30]
00376db4	add x10,sp,#0x20
00376db8	orr x10,x10,#0x1
00376dbc	tst w8,#0x1
00376dc0	csel x9,x10,x9,eq
00376dc4	ldrb w9,[x9]
00376dc8	cmp w9,#0x31
00376dcc	cset w19,eq
00376dd0	tbnz w8,#0x0,0x00376de0
00376dd4	b 0x00376de8
00376dd8	mov w19,wzr
00376ddc	tbz w8,#0x0,0x00376de8
00376de0	ldr x0,[sp, #0x30]
00376de4	bl 0x0041f730
00376de8	ldrb w8,[sp, #0x8]
00376dec	tbz w8,#0x0,0x00376df8
00376df0	ldr x0,[sp, #0x18]
00376df4	bl 0x0041f730
00376df8	ldr x8,[x20, #0x28]
00376dfc	ldur x9,[x29, #-0x8]
00376e00	cmp x8,x9
00376e04	b.ne 0x00376e48
00376e08	mov w0,w19
00376e0c	ldp x20,x19,[sp, #0x50]
00376e10	ldp x29,x30,[sp, #0x40]
00376e14	add sp,sp,#0x60
00376e18	ret
00376e48	bl 0x004217e0
