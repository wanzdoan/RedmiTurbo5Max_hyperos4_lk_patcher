003d63e0	sub sp,sp,#0x80
003d63e4	stp x29,x30,[sp, #0x40]
003d63e8	str x23,[sp, #0x50]
003d63ec	stp x22,x21,[sp, #0x60]
003d63f0	stp x20,x19,[sp, #0x70]
003d63f4	add x29,sp,#0x40
003d63f8	mrs x21,tpidr_el0
003d63fc	mov w9,#0x1e
003d6400	ldr x8,[x21, #0x28]
003d6404	mov x19,x0
003d6408	stur x8,[x29, #-0x8]
003d640c	adrp x8,0x15d000
003d6410	add x8,x8,#0x458
003d6414	strb w9,[sp]
003d6418	stp xzr,xzr,[sp, #0x20]
003d641c	str xzr,[sp, #0x30]
003d6420	ldr x9,[x8]
003d6424	strb wzr,[sp, #0x10]
003d6428	ldur x8,[x8, #0x7]
003d642c	stur x9,[sp, #0x1]
003d6430	str x8,[sp, #0x8]
003d6434	add x8,sp,#0x20
003d6438	mov x0,sp
003d643c	bl 0x003f4be0
003d6440	ldrb w8,[sp]
003d6444	tbz w8,#0x0,0x003d6450
003d6448	ldr x0,[sp, #0x10]
003d644c	bl 0x0041f730
003d6450	ldp x8,x9,[sp, #0x20]
003d6454	cmp x8,x9
003d6458	b.eq 0x003d6484
003d645c	ldrb w8,[x19, #0x9c0]
003d6460	stp xzr,xzr,[sp]
003d6464	str xzr,[sp, #0x10]
003d6468	tbnz w8,#0x0,0x003d64a0
003d646c	add x8,x19,#0x9c0
003d6470	ldr q0,[x8]
003d6474	ldr x8,[x8, #0x10]
003d6478	str q0,[sp]
003d647c	str x8,[sp, #0x10]
003d6480	b 0x003d64b0
003d6484	ldr x8,[x19]
003d6488	ldr x8,[x8, #0x158]
003d648c	adrp x1,0x158000
003d6490	add x1,x1,#0xcc9
003d6494	mov x0,x19
003d6498	blr x8
003d649c	b 0x003d65bc
003d64a0	ldr x1,[x19, #0x9d0]
003d64a4	ldr x2,[x19, #0x9c8]
003d64a8	mov x0,sp
003d64ac	bl 0x00225710
003d64b0	add x1,sp,#0x20
003d64b4	mov x0,x19
003d64b8	mov x2,xzr
003d64bc	bl 0x003d5780
003d64c0	ldr x8,[x19]
003d64c4	ldr x8,[x8, #0x1a8]
003d64c8	mov x0,x19
003d64cc	blr x8
003d64d0	orr x22,xzr,#-0x5555555555555556
003d64d4	mov x20,xzr
003d64d8	movk x22,#0xaaab
003d64dc	mov x23,#-0x1
003d64e0	ldr x8,[x19]
003d64e4	ldr x8,[x8, #0x170]
003d64e8	mov x0,x19
003d64ec	blr x8
003d64f0	cmp w0,#0x6b
003d64f4	b.le 0x003d6540
003d64f8	cmp w0,#0x72
003d64fc	b.gt 0x003d6560
003d6500	cmp w0,#0x6c
003d6504	b.eq 0x003d6510
003d6508	cmp w0,#0x72
003d650c	b.ne 0x003d64e0
003d6510	ldp x9,x8,[sp, #0x20]
003d6514	sub x8,x8,x9
003d6518	asr x8,x8,#0x3
003d651c	madd x8,x8,x22,x23
003d6520	cmp x20,x8
003d6524	csinc x20,xzr,x20,eq
003d6528	add x1,sp,#0x20
003d652c	mov x0,x19
003d6530	mov x2,x20
003d6534	bl 0x003d5780
003d6538	b 0x003d64e0
003d6540	cmn w0,#0x2
003d6544	ccmp w0,#0x1c,#0x4,ne
003d6548	b.eq 0x003d65a0
003d654c	cmp w0,#0x67
003d6550	b.ne 0x003d64e0
003d6554	b 0x003d6574
003d6560	cmp w0,#0x73
003d6564	b.eq 0x003d6574
003d6568	cmp w0,#0x74
003d656c	b.ne 0x003d64e0
003d6570	b 0x003d65a0
003d6574	cbnz x20,0x003d6588
003d6578	ldp x9,x8,[sp, #0x20]
003d657c	sub x8,x8,x9
003d6580	asr x8,x8,#0x3
003d6584	mul x20,x8,x22
003d6588	sub x20,x20,#0x1
003d658c	add x1,sp,#0x20
003d6590	mov x0,x19
003d6594	mov x2,x20
003d6598	bl 0x003d5780
003d659c	b 0x003d64e0
003d65a0	mov x1,sp
003d65a4	mov x0,x19
003d65a8	bl 0x003d5f40
003d65ac	ldrb w8,[sp]
003d65b0	tbz w8,#0x0,0x003d65bc
003d65b4	ldr x0,[sp, #0x10]
003d65b8	bl 0x0041f730
003d65bc	ldr x19,[sp, #0x20]
003d65c0	cbz x19,0x003d660c
003d65c4	ldr x8,[sp, #0x28]
003d65c8	mov x0,x19
003d65cc	cmp x8,x19
003d65d0	b.eq 0x003d6604
003d65d4	mov x20,x8
003d65d8	b 0x003d65ec
003d65e0	mov x8,x20
003d65e4	cmp x20,x19
003d65e8	b.eq 0x003d6600
003d65ec	ldrb w9,[x20, #-0x18]!
003d65f0	tbz w9,#0x0,0x003d65e0
003d65f4	ldur x0,[x8, #-0x8]
003d65f8	bl 0x0041f730
003d65fc	b 0x003d65e0
003d6600	ldr x0,[sp, #0x20]
003d6604	str x19,[sp, #0x28]
003d6608	bl 0x0041f730
003d660c	ldr x8,[x21, #0x28]
003d6610	ldur x9,[x29, #-0x8]
003d6614	cmp x8,x9
003d6618	b.ne 0x003d6698
003d661c	ldp x20,x19,[sp, #0x70]
003d6620	ldp x22,x21,[sp, #0x60]
003d6624	ldp x29,x30,[sp, #0x40]
003d6628	ldr x23,[sp, #0x50]
003d662c	add sp,sp,#0x80
003d6630	ret
003d6698	bl 0x004217e0
