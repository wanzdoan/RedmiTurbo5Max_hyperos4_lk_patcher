003ff3c0	sub sp,sp,#0x150
003ff3c4	stp x29,x30,[sp, #0xf0]
003ff3c8	stp x28,x27,[sp, #0x100]
003ff3cc	stp x26,x25,[sp, #0x110]
003ff3d0	stp x24,x23,[sp, #0x120]
003ff3d4	stp x22,x21,[sp, #0x130]
003ff3d8	stp x20,x19,[sp, #0x140]
003ff3dc	add x29,sp,#0xf0
003ff3e0	mrs x25,tpidr_el0
003ff3e4	ldr x8,[x25, #0x28]
003ff3e8	stur x8,[x29, #-0x18]
003ff3ec	ldrb w9,[x0]
003ff3f0	ldr x8,[x0, #0x8]
003ff3f4	lsr x10,x9,#0x1
003ff3f8	tst w9,#0x1
003ff3fc	csel x10,x10,x8,eq
003ff400	cbz x10,0x003ff660
003ff404	mov w22,w1
003ff408	mov x20,x3
003ff40c	stp xzr,xzr,[sp, #0x30]
003ff410	str xzr,[sp, #0x40]
003ff414	tbnz w9,#0x0,0x003ff678
003ff418	ldr q0,[x0]
003ff41c	ldr x8,[x0, #0x10]
003ff420	str q0,[sp, #0x30]
003ff424	str x8,[sp, #0x40]
003ff428	tbnz w2,#0x0,0x003ff694
003ff42c	add x0,sp,#0x30
003ff430	mov w1,#0x2f
003ff434	bl 0x0041fa70
003ff438	ldrb w8,[sp, #0x30]
003ff43c	add x10,sp,#0x30
003ff440	ldr x9,[sp, #0x40]
003ff444	orr x27,x10,#0x1
003ff448	movi v0.2D,#0x0
003ff44c	add x11,sp,#0x50
003ff450	tst w8,#0x1
003ff454	add x1,sp,#0x50
003ff458	csel x0,x27,x9,eq
003ff45c	stp q0,q0,[x11, #0x60]
003ff460	str q0,[x11, #0x50]
003ff464	stp q0,q0,[sp, #0x60]
003ff468	stp q0,q0,[sp, #0x80]
003ff46c	str q0,[sp, #0x50]
003ff470	bl 0x0041fe00
003ff474	cbz w0,0x003ff708
003ff478	bl 0x0041f880
003ff47c	ldr w8,[x0]
003ff480	cmp w8,#0x2
003ff484	b.ne 0x003ff734
003ff488	add x8,sp,#0x18
003ff48c	mov x21,x0
003ff490	mov x19,xzr
003ff494	orr x24,x8,#0x1
003ff498	str w22,[sp, #0xc]
003ff49c	str x20,[sp, #0x10]
003ff4a0	b 0x003ff4b0
003ff4a4	ldr x0,[sp, #0x28]
003ff4a8	bl 0x0041f730
003ff4ac	tbz w22,#0x0,0x003ff734
003ff4b0	ldrb w8,[sp, #0x30]
003ff4b4	ldr x9,[sp, #0x38]
003ff4b8	lsr x10,x8,#0x1
003ff4bc	tst w8,#0x1
003ff4c0	csel x23,x10,x9,eq
003ff4c4	cmp x19,x23
003ff4c8	b.cs 0x003ff718
003ff4cc	add x9,x19,#0x1
003ff4d0	subs x2,x23,x9
003ff4d4	b.ls 0x003ff718
003ff4d8	ldr x10,[sp, #0x40]
003ff4dc	tst w8,#0x1
003ff4e0	mov w1,#0x2f
003ff4e4	csel x22,x27,x10,eq
003ff4e8	add x0,x22,x9
003ff4ec	bl 0x0041f9d0
003ff4f0	sub x28,x0,x22
003ff4f4	cmp x0,#0x0
003ff4f8	ccmn x28,#0x1,#0x4,ne
003ff4fc	b.eq 0x003ff718
003ff500	cmp x23,x28
003ff504	stp xzr,xzr,[sp, #0x18]
003ff508	csel x23,x23,x28,cc
003ff50c	str xzr,[sp, #0x28]
003ff510	cmn x23,#0x10
003ff514	b.cs 0x003ff7d0
003ff518	cmp x23,#0x17
003ff51c	b.cs 0x003ff534
003ff520	lsl w8,w23,#0x1
003ff524	mov x26,x24
003ff528	strb w8,[sp, #0x18]
003ff52c	cbnz x28,0x003ff550
003ff530	b 0x003ff560
003ff534	orr x20,x23,#0xf
003ff538	add x0,x20,#0x1
003ff53c	bl 0x0041f720
003ff540	add x8,x20,#0x2
003ff544	mov x26,x0
003ff548	stp x23,x0,[sp, #0x20]
003ff54c	str x8,[sp, #0x18]
003ff550	mov x0,x26
003ff554	mov x1,x22
003ff558	mov x2,x23
003ff55c	bl 0x004217a0
003ff560	movi v0.2D,#0x0
003ff564	add x9,sp,#0x50
003ff568	strb wzr,[x26, x23, LSL ]
003ff56c	add x1,sp,#0x50
003ff570	ldrb w8,[sp, #0x18]
003ff574	stp q0,q0,[x9, #0x60]
003ff578	str q0,[x9, #0x50]
003ff57c	ldr x9,[sp, #0x28]
003ff580	tst w8,#0x1
003ff584	stp q0,q0,[sp, #0x60]
003ff588	stp q0,q0,[sp, #0x80]
003ff58c	csel x0,x24,x9,eq
003ff590	str q0,[sp, #0x50]
003ff594	bl 0x0041fe00
003ff598	cbz w0,0x003ff624
003ff59c	ldr w8,[x21]
003ff5a0	cmp w8,#0x2
003ff5a4	b.ne 0x003ff614
003ff5a8	ldr x0,[sp, #0x10]
003ff5ac	str xzr,[sp, #0x50]
003ff5b0	ldr w20,[sp, #0xc]
003ff5b4	cbz x0,0x003ff5dc
003ff5b8	ldrb w8,[sp, #0x18]
003ff5bc	add x1,sp,#0x50
003ff5c0	ldr x9,[sp, #0x28]
003ff5c4	mov w3,w20
003ff5c8	tst w8,#0x1
003ff5cc	csel x2,x24,x9,eq
003ff5d0	bl 0x00421590
003ff5d4	ldr x0,[sp, #0x50]
003ff5d8	bl 0x004215a0
003ff5dc	ldrb w8,[sp, #0x18]
003ff5e0	mov w1,w20
003ff5e4	ldr x9,[sp, #0x28]
003ff5e8	tst w8,#0x1
003ff5ec	csel x0,x24,x9,eq
003ff5f0	bl 0x00420140
003ff5f4	ldr x8,[sp, #0x50]
003ff5f8	mov w22,w0
003ff5fc	cbz x8,0x003ff610
003ff600	mov x0,x8
003ff604	bl 0x004215b0
003ff608	mov x0,xzr
003ff60c	bl 0x004215a0
003ff610	cbz w22,0x003ff634
003ff614	mov w22,wzr
003ff618	ldrb w8,[sp, #0x18]
003ff61c	tbz w8,#0x0,0x003ff4ac
003ff620	b 0x003ff4a4
003ff624	ldr w8,[sp, #0x60]
003ff628	and w8,w8,#0xf000
003ff62c	cmp w8,#0x4, LSL #12
003ff630	b.ne 0x003ff648
003ff634	mov w22,#0x1
003ff638	mov x19,x28
003ff63c	ldrb w8,[sp, #0x18]
003ff640	tbz w8,#0x0,0x003ff4ac
003ff644	b 0x003ff4a4
003ff648	mov w8,#0x14
003ff64c	mov w22,wzr
003ff650	str w8,[x21]
003ff654	ldrb w8,[sp, #0x18]
003ff658	tbz w8,#0x0,0x003ff4ac
003ff65c	b 0x003ff4a4
003ff660	bl 0x0041f880
003ff664	mov x8,x0
003ff668	mov w9,#0x2
003ff66c	mov w0,#0xffffffff
003ff670	str w9,[x8]
003ff674	b 0x003ff754
003ff678	ldr x1,[x0, #0x10]
003ff67c	add x0,sp,#0x30
003ff680	mov w19,w2
003ff684	mov x2,x8
003ff688	bl 0x00225710
003ff68c	mov w2,w19
003ff690	tbz w2,#0x0,0x003ff42c
003ff694	ldp x8,x10,[sp, #0x38]
003ff698	add x9,sp,#0x30
003ff69c	ldrb w11,[sp, #0x30]
003ff6a0	orr x12,x9,#0x1
003ff6a4	tst w11,#0x1
003ff6a8	lsr x13,x11,#0x1
003ff6ac	csel x9,x13,x8,eq
003ff6b0	csel x13,x12,x10,eq
003ff6b4	cbz x9,0x003ff6e8
003ff6b8	add x13,x9,x13
003ff6bc	mov x8,xzr
003ff6c0	sub x13,x13,#0x1
003ff6c4	cmn x9,x8
003ff6c8	b.eq 0x003ff6e8
003ff6cc	ldrb w14,[x13, x8, LSL ]
003ff6d0	sub x8,x8,#0x1
003ff6d4	cmp w14,#0x2f
003ff6d8	b.ne 0x003ff6c4
003ff6dc	add x13,x9,x8
003ff6e0	cmn x13,#0x1
003ff6e4	b.cc 0x003ff784
003ff6e8	bl 0x0041f880
003ff6ec	mov x8,x0
003ff6f0	mov w9,#0x2
003ff6f4	mov w0,#0xffffffff
003ff6f8	str w9,[x8]
003ff6fc	ldrb w8,[sp, #0x30]
003ff700	tbz w8,#0x0,0x003ff754
003ff704	b 0x003ff740
003ff708	ldr w8,[sp, #0x60]
003ff70c	and w8,w8,#0xf000
003ff710	cmp w8,#0x4, LSL #12
003ff714	b.ne 0x003ff728
003ff718	mov w0,wzr
003ff71c	ldrb w8,[sp, #0x30]
003ff720	tbz w8,#0x0,0x003ff754
003ff724	b 0x003ff740
003ff728	bl 0x0041f880
003ff72c	mov w8,#0x14
003ff730	str w8,[x0]
003ff734	mov w0,#0xffffffff
003ff738	ldrb w8,[sp, #0x30]
003ff73c	tbz w8,#0x0,0x003ff754
003ff740	ldr x8,[sp, #0x40]
003ff744	mov w19,w0
003ff748	mov x0,x8
003ff74c	bl 0x0041f730
003ff750	mov w0,w19
003ff754	ldr x8,[x25, #0x28]
003ff758	ldur x9,[x29, #-0x18]
003ff75c	cmp x8,x9
003ff760	b.ne 0x003ff7cc
003ff764	ldp x20,x19,[sp, #0x140]
003ff768	ldp x22,x21,[sp, #0x130]
003ff76c	ldp x24,x23,[sp, #0x120]
003ff770	ldp x26,x25,[sp, #0x110]
003ff774	ldp x28,x27,[sp, #0x100]
003ff778	ldp x29,x30,[sp, #0xf0]
003ff77c	add sp,sp,#0x150
003ff780	ret
003ff784	cmp x9,x13
003ff788	b.ls 0x003ff7a4
003ff78c	add x13,x13,#0x1
003ff790	tbnz w11,#0x0,0x003ff7b8
003ff794	lsl w11,w13,#0x1
003ff798	mov x10,x12
003ff79c	strb w11,[sp, #0x30]
003ff7a0	b 0x003ff7bc
003ff7a4	add x0,sp,#0x30
003ff7a8	add x1,x8,#0x1
003ff7ac	mov w2,wzr
003ff7b0	bl 0x00420030
003ff7b4	b 0x003ff438
003ff7b8	str x13,[sp, #0x38]
003ff7bc	add x9,x10,x9
003ff7c0	add x8,x9,x8
003ff7c4	strb wzr,[x8, #0x1]
003ff7c8	b 0x003ff438
003ff7cc	bl 0x004217e0
003ff7d0	add x0,sp,#0x18
003ff7d4	bl 0x00234d60
