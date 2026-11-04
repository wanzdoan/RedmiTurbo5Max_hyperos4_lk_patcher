00394790	sub sp,sp,#0x40
00394794	stp x29,x30,[sp, #0x20]
00394798	stp x20,x19,[sp, #0x30]
0039479c	add x29,sp,#0x20
003947a0	mrs x20,tpidr_el0
003947a4	mov x1,x0
003947a8	ldr x8,[x20, #0x28]
003947ac	stur x8,[x29, #-0x8]
003947b0	strh wzr,[sp]
003947b4	adrp x0,0x57b000
003947b8	add x0,x0,#0xcf0
003947bc	mov x2,sp
003947c0	bl 0x00420ef0
003947c4	mov w19,w0
003947c8	ldrb w8,[sp]
003947cc	tbz w8,#0x0,0x003947d8
003947d0	ldr x0,[sp, #0x10]
003947d4	bl 0x0041f730
003947d8	ldr x8,[x20, #0x28]
003947dc	ldur x9,[x29, #-0x8]
003947e0	cmp x8,x9
003947e4	b.ne 0x0039482c
003947e8	and w8,w19,#0x1
003947ec	sub w0,w8,#0x1
003947f0	ldp x20,x19,[sp, #0x30]
003947f4	ldp x29,x30,[sp, #0x20]
003947f8	add sp,sp,#0x40
003947fc	ret
0039482c	bl 0x004217e0
