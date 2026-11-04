0025c660	stp x29,x30,[sp, #-0x20]!
0025c664	stp x20,x19,[sp, #0x10]
0025c668	mov x29,sp
0025c66c	mov x19,x0
0025c670	add x0,x0,#0x9dc
0025c674	bl 0x00420000
0025c678	ldrb w8,[x19, #0xd18]
0025c67c	cbz w8,0x0025c6cc
0025c680	ldr w8,[x19, #0xbd0]
0025c684	strb wzr,[x19, #0xd18]
0025c688	str xzr,[x19, #0xbb0]
0025c68c	str wzr,[x19, #0xbb8]
0025c690	cmp w8,#0x1
0025c694	str xzr,[x19, #0xbc8]
0025c698	str xzr,[x19, #0xbc0]
0025c69c	b.lt 0x0025c6b8
0025c6a0	add x9,x19,#0xbe8
0025c6a4	subs x8,x8,#0x1
0025c6a8	stur xzr,[x9, #-0x10]
0025c6ac	stur wzr,[x9, #-0x8]
0025c6b0	stp xzr,xzr,[x9], #0x20
0025c6b4	b.ne 0x0025c6a4
0025c6b8	ldr x8,[x19]
0025c6bc	str wzr,[x19, #0xbd0]
0025c6c0	ldr x8,[x8, #0x248]
0025c6c4	mov x0,x19
0025c6c8	blr x8
0025c6cc	add x0,x19,#0x9dc
0025c6d0	ldp x20,x19,[sp, #0x10]
0025c6d4	ldp x29,x30,[sp], #0x20
0025c6d8	b 0x00420010
