0040ff40	stp x29,x30,[sp, #-0x10]!
0040ff44	mov x29,sp
0040ff48	ldrb w8,[x0]
0040ff4c	ldr x9,[x0, #0x10]
0040ff50	ldrb w10,[x1]
0040ff54	ldr x11,[x1, #0x10]
0040ff58	tst w8,#0x1
0040ff5c	csinc x0,x9,x0,ne
0040ff60	tst w10,#0x1
0040ff64	csinc x1,x11,x1,ne
0040ff68	bl 0x004209d0
0040ff6c	cmp w0,#0x0
0040ff70	cset w0,eq
0040ff74	ldp x29,x30,[sp], #0x10
0040ff78	ret
