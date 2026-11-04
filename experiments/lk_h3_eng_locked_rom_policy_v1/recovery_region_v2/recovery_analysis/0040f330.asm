0040f330	sub sp,sp,#0x50
0040f334	stp x29,x30,[sp, #0x20]
0040f338	stp x22,x21,[sp, #0x30]
0040f33c	stp x20,x19,[sp, #0x40]
0040f340	add x29,sp,#0x20
0040f344	mrs x21,tpidr_el0
0040f348	mov x19,x8
0040f34c	ldr x8,[x21, #0x28]
0040f350	mov x20,x1
0040f354	stur x8,[x29, #-0x8]
0040f358	ldrb w8,[x0]
0040f35c	stp xzr,xzr,[sp]
0040f360	ldr x9,[x0, #0x10]
0040f364	str xzr,[sp, #0x10]
0040f368	tst w8,#0x1
0040f36c	csinc x0,x9,x0,ne
0040f370	bl 0x0041ff90
0040f374	cbz x0,0x0040f3c8
0040f378	nop
0040f37c	adr x1,0x410a30
0040f380	mov x2,sp
0040f384	mov x22,sp
0040f388	bl 0x0041ffa0
0040f38c	ldrb w8,[sp]
0040f390	ldr x9,[sp, #0x8]
0040f394	lsr x10,x8,#0x1
0040f398	tst w8,#0x1
0040f39c	csel x11,x10,x9,eq
0040f3a0	cmp x11,#0x0
0040f3a4	csel x10,x20,x22,eq
0040f3a8	ldrb w12,[x10]
0040f3ac	tbnz w12,#0x0,0x0040f3f4
0040f3b0	ldr q0,[x10]
0040f3b4	ldr x9,[x10, #0x10]
0040f3b8	str q0,[x19]
0040f3bc	str x9,[x19, #0x10]
0040f3c0	tbnz w8,#0x0,0x0040f420
0040f3c4	b 0x0040f428
0040f3c8	ldrb w8,[x20]
0040f3cc	tbnz w8,#0x0,0x0040f44c
0040f3d0	ldr x8,[x21, #0x28]
0040f3d4	ldur x9,[x29, #-0x8]
0040f3d8	cmp x8,x9
0040f3dc	b.ne 0x0040f478
0040f3e0	ldr q0,[x20]
0040f3e4	ldr x8,[x20, #0x10]
0040f3e8	str q0,[x19]
0040f3ec	str x8,[x19, #0x10]
0040f3f0	b 0x0040f438
0040f3f4	add x8,x20,#0x10
0040f3f8	add x10,x22,#0x10
0040f3fc	cmp x11,#0x0
0040f400	mov x0,x19
0040f404	csel x8,x8,x10,eq
0040f408	ldr x10,[x20, #0x8]
0040f40c	ldr x1,[x8]
0040f410	csel x2,x10,x9,eq
0040f414	bl 0x00225710
0040f418	ldrb w8,[sp]
0040f41c	tbz w8,#0x0,0x0040f428
0040f420	ldr x0,[sp, #0x10]
0040f424	bl 0x0041f730
0040f428	ldr x8,[x21, #0x28]
0040f42c	ldur x9,[x29, #-0x8]
0040f430	cmp x8,x9
0040f434	b.ne 0x0040f478
0040f438	ldp x20,x19,[sp, #0x40]
0040f43c	ldp x22,x21,[sp, #0x30]
0040f440	ldp x29,x30,[sp, #0x20]
0040f444	add sp,sp,#0x50
0040f448	ret
0040f44c	ldp x2,x1,[x20, #0x8]
0040f450	ldr x8,[x21, #0x28]
0040f454	ldur x9,[x29, #-0x8]
0040f458	cmp x8,x9
0040f45c	b.ne 0x0040f478
0040f460	mov x0,x19
0040f464	ldp x20,x19,[sp, #0x40]
0040f468	ldp x22,x21,[sp, #0x30]
0040f46c	ldp x29,x30,[sp, #0x20]
0040f470	add sp,sp,#0x50
0040f474	b 0x00225710
0040f478	bl 0x004217e0
