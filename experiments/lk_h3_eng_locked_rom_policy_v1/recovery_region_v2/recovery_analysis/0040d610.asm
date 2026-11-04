0040d610	sub sp,sp,#0x70
0040d614	stp x29,x30,[sp, #0x30]
0040d618	stp x24,x23,[sp, #0x40]
0040d61c	stp x22,x21,[sp, #0x50]
0040d620	stp x20,x19,[sp, #0x60]
0040d624	add x29,sp,#0x30
0040d628	mrs x23,tpidr_el0
0040d62c	mov x19,x0
0040d630	ldr x8,[x23, #0x28]
0040d634	mov w1,wzr
0040d638	mov w2,wzr
0040d63c	stur x8,[x29, #-0x8]
0040d640	ldr x8,[x0]
0040d644	mov w0,#0x1e
0040d648	ldr w20,[x8, #0x114]
0040d64c	bl 0x002204cc
0040d650	cbz w0,0x0040d678
0040d654	add w8,w20,#0x2
0040d658	cmp w20,#0x5
0040d65c	mov w9,#0x7
0040d660	mov x1,xzr
0040d664	csel w0,w8,w9,cc
0040d668	mov w2,#0x4
0040d66c	bl 0x003d0c50
0040d670	cbnz w0,0x0040d688
0040d674	b 0x0040d824
0040d678	adrp x8,0x42c000
0040d67c	ldr w8,[x8, #0xcf8]
0040d680	cmp w8,w20
0040d684	b.gt 0x0040d824
0040d688	ldr x0,[x19]
0040d68c	ldr w8,[x0, #0x120]
0040d690	cmn w8,#0x1
0040d694	b.eq 0x0040d6d4
0040d698	adrp x1,0x159000
0040d69c	add x1,x1,#0x87
0040d6a0	mov w2,#0x2
0040d6a4	bl 0x00235720
0040d6a8	ldr x8,[x19]
0040d6ac	mov x20,x0
0040d6b0	ldr w0,[x8, #0x120]
0040d6b4	bl 0x0041fc40
0040d6b8	mov x21,x0
0040d6bc	bl 0x0041f860
0040d6c0	mov x2,x0
0040d6c4	mov x0,x20
0040d6c8	mov x1,x21
0040d6cc	bl 0x00235720
0040d6d0	ldr x0,[x19]
0040d6d4	ldr w8,[x0, #0x68]
0040d6d8	stp xzr,xzr,[sp, #0x8]
0040d6dc	str xzr,[sp, #0x18]
0040d6e0	tbnz w8,#0x4,0x0040d6f8
0040d6e4	tbnz w8,#0x3,0x0040d718
0040d6e8	add x8,sp,#0x8
0040d6ec	mov x20,xzr
0040d6f0	orr x21,x8,#0x1
0040d6f4	b 0x0040d77c
0040d6f8	ldr x8,[x0, #0x60]
0040d6fc	ldr x9,[x0, #0x38]
0040d700	cmp x8,x9
0040d704	b.cs 0x0040d710
0040d708	mov x8,x9
0040d70c	str x9,[x0, #0x60]
0040d710	add x9,x0,#0x30
0040d714	b 0x0040d720
0040d718	add x9,x0,#0x18
0040d71c	ldr x8,[x0, #0x28]
0040d720	ldr x22,[x9]
0040d724	sub x20,x8,x22
0040d728	cmn x20,#0x10
0040d72c	b.cs 0x0040d910
0040d730	cmp x20,#0x17
0040d734	b.cs 0x0040d750
0040d738	add x9,sp,#0x8
0040d73c	lsl w8,w20,#0x1
0040d740	orr x21,x9,#0x1
0040d744	strb w8,[sp, #0x8]
0040d748	cbnz x20,0x0040d76c
0040d74c	b 0x0040d77c
0040d750	orr x24,x20,#0xf
0040d754	add x0,x24,#0x1
0040d758	bl 0x0041f720
0040d75c	add x8,x24,#0x2
0040d760	mov x21,x0
0040d764	stp x20,x0,[sp, #0x10]
0040d768	str x8,[sp, #0x8]
0040d76c	mov x0,x21
0040d770	mov x1,x22
0040d774	mov x2,x20
0040d778	bl 0x004217a0
0040d77c	strb wzr,[x21, x20, LSL ]
0040d780	ldr x8,[x19]
0040d784	ldr w2,[x8, #0x114]
0040d788	cmp w2,#0x6
0040d78c	b.ne 0x0040d7b4
0040d790	ldrb w8,[sp, #0x8]
0040d794	add x10,sp,#0x8
0040d798	ldr x9,[sp, #0x18]
0040d79c	orr x10,x10,#0x1
0040d7a0	tst w8,#0x1
0040d7a4	csel x0,x10,x9,eq
0040d7a8	bl 0x00421280
0040d7ac	ldr x8,[x19]
0040d7b0	ldr w2,[x8, #0x114]
0040d7b4	ldrb w9,[sp, #0x8]
0040d7b8	add x11,sp,#0x8
0040d7bc	ldr x10,[sp, #0x18]
0040d7c0	orr x20,x11,#0x1
0040d7c4	ldr x0,[x8, #0x108]
0040d7c8	tst w9,#0x1
0040d7cc	ldr w1,[x8, #0x110]
0040d7d0	ldr x3,[x8, #0x118]
0040d7d4	csel x4,x20,x10,eq
0040d7d8	bl 0x0040d970
0040d7dc	ldr x8,[x19]
0040d7e0	ldr w8,[x8, #0x114]
0040d7e4	cmp w8,#0x6
0040d7e8	b.ne 0x0040d814
0040d7ec	mov w0,#0x1e
0040d7f0	mov w1,wzr
0040d7f4	mov w2,wzr
0040d7f8	bl 0x002204cc
0040d7fc	cbz w0,0x0040d8bc
0040d800	ldrb w8,[sp, #0x8]
0040d804	ldr x9,[sp, #0x18]
0040d808	tst w8,#0x1
0040d80c	csel x0,x20,x9,eq
0040d810	bl 0x003cf450
0040d814	ldrb w8,[sp, #0x8]
0040d818	tbz w8,#0x0,0x0040d824
0040d81c	ldr x0,[sp, #0x18]
0040d820	bl 0x0041f730
0040d824	ldr x20,[x19]
0040d828	str xzr,[x19]
0040d82c	cbz x20,0x0040d894
0040d830	adrp x21,0x429000
0040d834	ldr x21,[x21, #0xb88]
0040d838	adrp x9,0x429000
0040d83c	ldr x9,[x9, #0xb90]
0040d840	mov x19,x20
0040d844	ldr x8,[x21]
0040d848	ldr x10,[x21, #0x18]
0040d84c	add x9,x9,#0x10
0040d850	str x8,[x20]
0040d854	ldur x8,[x8, #-0x18]
0040d858	str x10,[x20, x8, LSL #0x0]
0040d85c	str x9,[x19, #0x8]!
0040d860	ldrb w8,[x19, #0x40]
0040d864	tbz w8,#0x0,0x0040d870
0040d868	ldr x0,[x20, #0x58]
0040d86c	bl 0x0041f730
0040d870	mov x0,x19
0040d874	bl 0x0041fce0
0040d878	add x1,x21,#0x8
0040d87c	mov x0,x20
0040d880	bl 0x0041fcc0
0040d884	add x0,x20,#0x70
0040d888	bl 0x0041fa00
0040d88c	mov x0,x20
0040d890	bl 0x0041f730
0040d894	ldr x8,[x23, #0x28]
0040d898	ldur x9,[x29, #-0x8]
0040d89c	cmp x8,x9
0040d8a0	b.ne 0x0040d90c
0040d8a4	ldp x20,x19,[sp, #0x60]
0040d8a8	ldp x22,x21,[sp, #0x50]
0040d8ac	ldp x24,x23,[sp, #0x40]
0040d8b0	ldp x29,x30,[sp, #0x30]
0040d8b4	add sp,sp,#0x70
0040d8b8	ret
0040d8bc	adrp x8,0x57d000
0040d8c0	add x8,x8,#0x770
0040d8c4	adrp x21,0x57d000
0040d8c8	ldaprb w8,[x8]
0040d8cc	tbz w8,#0x0,0x0040d918
0040d8d0	ldrb w9,[sp, #0x8]
0040d8d4	ldr x8,[x21, #0x768]
0040d8d8	ldr x10,[sp, #0x18]
0040d8dc	tst w9,#0x1
0040d8e0	ldr x0,[x8, #0x20]
0040d8e4	csel x9,x20,x10,eq
0040d8e8	stur x9,[x29, #-0x10]
0040d8ec	cbz x0,0x0040d960
0040d8f0	ldr x8,[x0]
0040d8f4	sub x1,x29,#0x10
0040d8f8	ldr x8,[x8, #0x30]
0040d8fc	blr x8
0040d900	ldrb w8,[sp, #0x8]
0040d904	tbnz w8,#0x0,0x0040d81c
0040d908	b 0x0040d824
0040d90c	bl 0x004217e0
0040d910	add x0,sp,#0x8
0040d914	bl 0x00234d60
0040d918	adrp x0,0x57d000
0040d91c	add x0,x0,#0x770
0040d920	bl 0x0041fc90
0040d924	cbz w0,0x0040d8d0
0040d928	mov w0,#0x30
0040d92c	bl 0x0041f720
0040d930	nop
0040d934	adr x8,0x427bb0
0040d938	nop
0040d93c	adr x9,0x40c410
0040d940	str x0,[x0, #0x20]
0040d944	add x8,x8,#0x10
0040d948	str x0,[x21, #0x768]
0040d94c	stp x8,x9,[x0]
0040d950	adrp x0,0x57d000
0040d954	add x0,x0,#0x770
0040d958	bl 0x0041fcb0
0040d95c	b 0x0040d8d0
0040d960	bl 0x00238530
0040d964	udf 0x0
