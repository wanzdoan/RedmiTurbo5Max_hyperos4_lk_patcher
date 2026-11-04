00404c40	stp x29,x30,[sp, #-0x40]!
00404c44	stp x24,x23,[sp, #0x10]
00404c48	stp x22,x21,[sp, #0x20]
00404c4c	stp x20,x19,[sp, #0x30]
00404c50	mov x29,sp
00404c54	ldp x24,x23,[x0]
00404c58	mov x19,x8
00404c5c	stp xzr,xzr,[x19, #0x8]
00404c60	subs x8,x23,x24
00404c64	str xzr,[x19]
00404c68	b.eq 0x00404ce4
00404c6c	orr x9,xzr,#-0x5555555555555556
00404c70	asr x8,x8,#0x3
00404c74	movk x9,#0xaaab
00404c78	mul x8,x8,x9
00404c7c	lsr x9,x8,#0x3d
00404c80	cbnz x9,0x00404da0
00404c84	lsl x21,x8,#0x3
00404c88	mov x0,x21
00404c8c	bl 0x0041f720
00404c90	add x22,x0,x21
00404c94	mov w1,wzr
00404c98	mov x2,x21
00404c9c	mov x20,x0
00404ca0	str x0,[x19]
00404ca4	str x22,[x19, #0x10]
00404ca8	bl 0x004217f0
00404cac	mov x8,x20
00404cb0	str x22,[x19, #0x8]
00404cb4	ldrb w9,[x24]
00404cb8	ldr x10,[x24, #0x10]
00404cbc	tst w9,#0x1
00404cc0	csinc x9,x10,x24,ne
00404cc4	add x24,x24,#0x18
00404cc8	cmp x24,x23
00404ccc	str x9,[x8], #0x8
00404cd0	b.ne 0x00404cb4
00404cd4	cmp x22,x22
00404cd8	b.cs 0x00404cf4
00404cdc	str xzr,[x22], #0x8
00404ce0	b 0x00404d88
00404ce4	mov x22,xzr
00404ce8	mov x21,xzr
00404cec	mov x20,xzr
00404cf0	b 0x00404cf8
00404cf4	mov x21,x22
00404cf8	sub x8,x21,x20
00404cfc	asr x23,x8,#0x3
00404d00	add x8,x23,#0x1
00404d04	lsr x9,x8,#0x3d
00404d08	cbnz x9,0x00404da0
00404d0c	sub x9,x22,x20
00404d10	orr x11,xzr,#0x7ffffffffffffff8
00404d14	asr x10,x9,#0x2
00404d18	cmp x10,x8
00404d1c	csel x8,x10,x8,hi
00404d20	cmp x9,x11
00404d24	mov x9,#0x1fffffffffffffff
00404d28	csel x24,x8,x9,cc
00404d2c	cbz x24,0x00404d44
00404d30	lsr x8,x24,#0x3d
00404d34	cbnz x8,0x00404da8
00404d38	lsl x0,x24,#0x3
00404d3c	bl 0x0041f720
00404d40	b 0x00404d48
00404d44	mov x0,xzr
00404d48	add x8,x0,x23, LSL #0x3
00404d4c	add x9,x0,x24, LSL #0x3
00404d50	mov x22,x8
00404d54	cmp x21,x20
00404d58	str xzr,[x22], #0x8
00404d5c	b.eq 0x00404d74
00404d60	ldr x10,[x21, #-0x8]!
00404d64	cmp x21,x20
00404d68	str x10,[x8, #-0x8]!
00404d6c	b.ne 0x00404d60
00404d70	ldr x21,[x19]
00404d74	stp x8,x22,[x19]
00404d78	str x9,[x19, #0x10]
00404d7c	cbz x21,0x00404d88
00404d80	mov x0,x21
00404d84	bl 0x0041f730
00404d88	str x22,[x19, #0x8]
00404d8c	ldp x20,x19,[sp, #0x30]
00404d90	ldp x22,x21,[sp, #0x20]
00404d94	ldp x24,x23,[sp, #0x10]
00404d98	ldp x29,x30,[sp], #0x40
00404d9c	ret
00404da0	mov x0,x19
00404da4	bl 0x002382c0
00404da8	bl 0x00234da0
