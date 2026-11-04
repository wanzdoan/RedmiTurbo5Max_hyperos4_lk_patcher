0022cf30	stp x29,x30,[sp, #-0x40]!
0022cf34	stp x24,x23,[sp, #0x10]
0022cf38	stp x22,x21,[sp, #0x20]
0022cf3c	stp x20,x19,[sp, #0x30]
0022cf40	mov x29,sp
0022cf44	ldp x10,x9,[x0, #0x8]
0022cf48	mov x21,x2
0022cf4c	mov x19,x1
0022cf50	ldrb w8,[x0]
0022cf54	tst w8,#0x1
0022cf58	lsr x8,x8,#0x1
0022cf5c	csinc x23,x9,x0,ne
0022cf60	mov x0,x1
0022cf64	csel x22,x8,x10,eq
0022cf68	bl 0x0041f860
0022cf6c	cmp x22,x21
0022cf70	b.cs 0x0022cf7c
0022cf74	mov x21,#-0x1
0022cf78	b 0x0022cfa8
0022cf7c	mov x20,x0
0022cf80	cbz x0,0x0022cfa8
0022cf84	add x0,x23,x21
0022cf88	add x24,x23,x22
0022cf8c	sub x8,x24,x0
0022cf90	cmp x8,x20
0022cf94	b.ge 0x0022cfc0
0022cf98	mov x22,x24
0022cf9c	sub x8,x22,x23
0022cfa0	cmp x22,x24
0022cfa4	csinv x21,x8,xzr,ne
0022cfa8	mov x0,x21
0022cfac	ldp x20,x19,[sp, #0x30]
0022cfb0	ldp x22,x21,[sp, #0x20]
0022cfb4	ldp x24,x23,[sp, #0x10]
0022cfb8	ldp x29,x30,[sp], #0x40
0022cfbc	ret
0022cfc0	ldrb w21,[x19]
0022cfc4	sub x8,x8,x20
0022cfc8	adds x2,x8,#0x1
0022cfcc	b.cs 0x0022cf98
0022cfd0	mov w1,w21
0022cfd4	bl 0x0041f9d0
0022cfd8	cbz x0,0x0022cf98
0022cfdc	mov x1,x19
0022cfe0	mov x2,x20
0022cfe4	mov x22,x0
0022cfe8	bl 0x0041fad0
0022cfec	cbz w0,0x0022cf9c
0022cff0	add x0,x22,#0x1
0022cff4	sub x8,x24,x0
0022cff8	cmp x8,x20
0022cffc	b.ge 0x0022cfc4
0022d000	b 0x0022cf98
