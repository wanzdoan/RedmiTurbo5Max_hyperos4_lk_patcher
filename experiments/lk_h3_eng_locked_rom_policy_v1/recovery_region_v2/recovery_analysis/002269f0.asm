002269f0	stp x29,x30,[sp, #-0x30]!
002269f4	stp x22,x21,[sp, #0x10]
002269f8	stp x20,x19,[sp, #0x20]
002269fc	mov x29,sp
00226a00	ldp x10,x11,[x0, #0x8]
00226a04	ldrb w9,[x0]
00226a08	tst w9,#0x1
00226a0c	lsr x9,x9,#0x1
00226a10	csel x10,x9,x10,eq
00226a14	csinc x11,x11,x0,ne
00226a18	cbz x10,0x00226a84
00226a1c	neg x9,x10
00226a20	mov x19,x11
00226a24	ldrb w12,[x19]
00226a28	sub w13,w12,#0xe
00226a2c	cmp w12,#0x20
00226a30	ccmn w13,#0x5,#0x2,ne
00226a34	b.cc 0x00226a48
00226a38	add x19,x19,#0x1
00226a3c	adds x9,x9,#0x1
00226a40	b.cc 0x00226a24
00226a44	b 0x00226a84
00226a48	add x10,x10,x11
00226a4c	mov x12,xzr
00226a50	sub x10,x10,#0x1
00226a54	add x11,x9,#0x1
00226a58	nop
00226a5c	nop
00226a60	ldrb w13,[x10, x12, LSL ]
00226a64	sub w14,w13,#0xe
00226a68	cmp w13,#0x20
00226a6c	ccmn w14,#0x5,#0x2,ne
00226a70	b.cc 0x00226aa0
00226a74	sub x13,x12,#0x1
00226a78	cmp x11,x12
00226a7c	mov x12,x13
00226a80	b.ne 0x00226a60
00226a84	mov x20,xzr
00226a88	strb wzr,[x8], #0x1
00226a8c	strb wzr,[x8, x20, LSL ]
00226a90	ldp x20,x19,[sp, #0x20]
00226a94	ldp x22,x21,[sp, #0x10]
00226a98	ldp x29,x30,[sp], #0x30
00226a9c	ret
00226aa0	sub x20,x12,x9
00226aa4	cmn x20,#0x10
00226aa8	b.cs 0x00226afc
00226aac	cmp x20,#0x17
00226ab0	b.cs 0x00226ac0
00226ab4	lsl w9,w20,#0x1
00226ab8	strb w9,[x8], #0x1
00226abc	b 0x00226ae0
00226ac0	orr x21,x20,#0xf
00226ac4	mov x22,x8
00226ac8	add x0,x21,#0x1
00226acc	bl 0x0041f720
00226ad0	add x8,x21,#0x2
00226ad4	stp x20,x0,[x22, #0x8]
00226ad8	str x8,[x22]
00226adc	mov x8,x0
00226ae0	mov x0,x8
00226ae4	mov x1,x19
00226ae8	mov x2,x20
00226aec	mov x19,x8
00226af0	bl 0x004217a0
00226af4	mov x8,x19
00226af8	b 0x00226a8c
00226afc	mov x0,x8
00226b00	bl 0x00225510
00226b04	udf 0x0
