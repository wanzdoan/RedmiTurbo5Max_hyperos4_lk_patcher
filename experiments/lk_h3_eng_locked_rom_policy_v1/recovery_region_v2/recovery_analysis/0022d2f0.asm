0022d2f0	sub sp,sp,#0x40
0022d2f4	stp x29,x30,[sp, #0x20]
0022d2f8	stp x20,x19,[sp, #0x30]
0022d2fc	add x29,sp,#0x20
0022d300	adrp x10,0x143000
0022d304	add x10,x10,#0x21
0022d308	mrs x20,tpidr_el0
0022d30c	mov w8,#0x1a
0022d310	ldr x9,[x20, #0x28]
0022d314	ldr x11,[x10]
0022d318	ldur x10,[x10, #0x5]
0022d31c	stur x9,[x29, #-0x8]
0022d320	strb w8,[sp]
0022d324	stur x11,[sp, #0x1]
0022d328	stur x10,[sp, #0x6]
0022d32c	strb wzr,[sp, #0xe]
0022d330	mov x0,sp
0022d334	mov w1,wzr
0022d338	bl 0x0040fe90
0022d33c	mov w19,w0
0022d340	ldrb w8,[sp]
0022d344	tbz w8,#0x0,0x0022d350
0022d348	ldr x0,[sp, #0x10]
0022d34c	bl 0x0041f730
0022d350	ldr x8,[x20, #0x28]
0022d354	ldur x9,[x29, #-0x8]
0022d358	cmp x8,x9
0022d35c	b.ne 0x0022d3a0
0022d360	and w0,w19,#0x1
0022d364	ldp x20,x19,[sp, #0x30]
0022d368	ldp x29,x30,[sp, #0x20]
0022d36c	add sp,sp,#0x40
0022d370	ret
0022d3a0	bl 0x004217e0
