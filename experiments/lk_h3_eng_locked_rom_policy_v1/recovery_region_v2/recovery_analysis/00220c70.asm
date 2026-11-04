00220c70	stp x29,x30,[sp, #-0x30]!
00220c74	stp x22,x21,[sp, #0x10]
00220c78	stp x20,x19,[sp, #0x20]
00220c7c	mov x29,sp
00220c80	mov x19,x0
00220c84	mov x0,x1
00220c88	mov x21,x1
00220c8c	bl 0x0041f860
00220c90	cmn x0,#0x10
00220c94	b.cs 0x00220cf4
00220c98	mov x20,x0
00220c9c	cmp x0,#0x17
00220ca0	b.cs 0x00220cb4
00220ca4	lsl w8,w20,#0x1
00220ca8	strb w8,[x19], #0x1
00220cac	cbnz x20,0x00220cd0
00220cb0	b 0x00220ce0
00220cb4	orr x22,x20,#0xf
00220cb8	add x0,x22,#0x1
00220cbc	bl 0x0041f720
00220cc0	add x8,x22,#0x2
00220cc4	stp x20,x0,[x19, #0x8]
00220cc8	str x8,[x19]
00220ccc	mov x19,x0
00220cd0	mov x0,x19
00220cd4	mov x1,x21
00220cd8	mov x2,x20
00220cdc	bl 0x004217a0
00220ce0	strb wzr,[x19, x20, LSL ]
00220ce4	ldp x20,x19,[sp, #0x20]
00220ce8	ldp x22,x21,[sp, #0x10]
00220cec	ldp x29,x30,[sp], #0x30
00220cf0	ret
00220cf4	mov x0,x19
00220cf8	bl 0x00225510
00220cfc	udf 0x0
