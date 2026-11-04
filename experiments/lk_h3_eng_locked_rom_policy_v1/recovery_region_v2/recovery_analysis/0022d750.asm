0022d750	sub sp,sp,#0x170
0022d754	stp x29,x30,[sp, #0x110]
0022d758	stp x28,x27,[sp, #0x120]
0022d75c	stp x26,x25,[sp, #0x130]
0022d760	stp x24,x23,[sp, #0x140]
0022d764	stp x22,x21,[sp, #0x150]
0022d768	stp x20,x19,[sp, #0x160]
0022d76c	add x29,sp,#0x110
0022d770	mrs x23,tpidr_el0
0022d774	mov x19,x8
0022d778	ldr x9,[x23, #0x28]
0022d77c	stur x9,[x29, #-0x8]
0022d780	ldp x9,x10,[x0]
0022d784	cmp x9,x10
0022d788	b.eq 0x0022d8f0
0022d78c	movi v0.2D,#0x0
0022d790	mov x24,sp
0022d794	mov x20,x1
0022d798	mov x21,x0
0022d79c	stur q0,[sp, #0x68]
0022d7a0	stur q0,[x24, #0xf8]
0022d7a4	stur q0,[x24, #0xe8]
0022d7a8	stur q0,[x24, #0xd8]
0022d7ac	stur q0,[x24, #0xc8]
0022d7b0	stur q0,[x24, #0xb8]
0022d7b4	stur q0,[x24, #0xa8]
0022d7b8	stur q0,[x24, #0x98]
0022d7bc	stur q0,[x24, #0x88]
0022d7c0	stur q0,[sp, #0x78]
0022d7c4	stur q0,[sp, #0x58]
0022d7c8	adrp x26,0x429000
0022d7cc	ldr x26,[x26, #0xb80]
0022d7d0	adrp x25,0x429000
0022d7d4	ldr x25,[x25, #0xb88]
0022d7d8	stur q0,[sp, #0x48]
0022d7dc	stur q0,[sp, #0x38]
0022d7e0	add x27,x26,#0x40
0022d7e4	stur q0,[sp, #0x28]
0022d7e8	ldp x8,x9,[x25, #0x8]
0022d7ec	stur q0,[sp, #0x18]
0022d7f0	stur q0,[sp, #0x8]
0022d7f4	str x27,[sp, #0x70]
0022d7f8	str x8,[sp]
0022d7fc	ldur x8,[x8, #-0x18]
0022d800	str x9,[x24, x8, LSL #0x0]
0022d804	ldr x8,[sp]
0022d808	ldur x8,[x8, #-0x18]
0022d80c	add x22,x24,x8
0022d810	add x1,x24,#0x8
0022d814	mov x0,x22
0022d818	bl 0x0041fa10
0022d81c	mov w8,#0xffffffff
0022d820	add x9,x26,#0x18
0022d824	str xzr,[x22, #0x88]
0022d828	str w8,[x22, #0x90]
0022d82c	str x9,[sp]
0022d830	str x27,[sp, #0x70]
0022d834	add x0,x24,#0x8
0022d838	bl 0x0041fcd0
0022d83c	adrp x26,0x429000
0022d840	ldr x26,[x26, #0xb90]
0022d844	mov w8,#0x10
0022d848	movi v0.2D,#0x0
0022d84c	add x9,x26,#0x10
0022d850	str w8,[sp, #0x68]
0022d854	ldr x8,[x21]
0022d858	stur q0,[sp, #0x48]
0022d85c	stur q0,[sp, #0x58]
0022d860	str x9,[sp, #0x8]
0022d864	ldp x11,x10,[x8, #0x8]
0022d868	ldrb w9,[x8]
0022d86c	tst w9,#0x1
0022d870	lsr x9,x9,#0x1
0022d874	csinc x1,x10,x8,ne
0022d878	csel x2,x9,x11,eq
0022d87c	mov x0,sp
0022d880	bl 0x00226b10
0022d884	ldr x8,[x21]
0022d888	add x22,x8,#0x18
0022d88c	nop
0022d890	ldr x8,[x21, #0x8]
0022d894	cmp x22,x8
0022d898	b.eq 0x0022d8d8
0022d89c	mov x0,x20
0022d8a0	bl 0x0041f860
0022d8a4	mov x2,x0
0022d8a8	mov x0,sp
0022d8ac	mov x1,x20
0022d8b0	bl 0x00226b10
0022d8b4	ldp x10,x9,[x22, #0x8]
0022d8b8	ldrb w8,[x22]
0022d8bc	tst w8,#0x1
0022d8c0	lsr x8,x8,#0x1
0022d8c4	csinc x1,x9,x22,ne
0022d8c8	add x22,x22,#0x18
0022d8cc	csel x2,x8,x10,eq
0022d8d0	bl 0x00226b10
0022d8d4	b 0x0022d890
0022d8d8	ldr w8,[sp, #0x68]
0022d8dc	tbnz w8,#0x4,0x0022d8f8
0022d8e0	tbnz w8,#0x3,0x0022d91c
0022d8e4	mov x20,xzr
0022d8e8	strb wzr,[x19], #0x1
0022d8ec	b 0x0022d980
0022d8f0	strh wzr,[x19]
0022d8f4	b 0x0022d9d0
0022d8f8	ldr x8,[sp, #0x60]
0022d8fc	ldr x9,[sp, #0x38]
0022d900	cmp x8,x9
0022d904	b.cs 0x0022d910
0022d908	mov x8,x9
0022d90c	str x9,[sp, #0x60]
0022d910	mov x9,sp
0022d914	add x9,x9,#0x30
0022d918	b 0x0022d928
0022d91c	mov x8,sp
0022d920	add x9,x8,#0x18
0022d924	ldr x8,[sp, #0x28]
0022d928	ldr x21,[x9]
0022d92c	sub x20,x8,x21
0022d930	cmn x20,#0x10
0022d934	b.cs 0x0022da00
0022d938	cmp x20,#0x17
0022d93c	b.cs 0x0022d950
0022d940	lsl w8,w20,#0x1
0022d944	strb w8,[x19], #0x1
0022d948	cbnz x20,0x0022d970
0022d94c	b 0x0022d980
0022d950	orr x8,x20,#0xf
0022d954	add x22,x8,#0x1
0022d958	mov x0,x22
0022d95c	bl 0x0041f720
0022d960	orr x8,x22,#0x1
0022d964	stp x20,x0,[x19, #0x8]
0022d968	str x8,[x19]
0022d96c	mov x19,x0
0022d970	mov x0,x19
0022d974	mov x1,x21
0022d978	mov x2,x20
0022d97c	bl 0x004217a0
0022d980	ldr x8,[x25]
0022d984	strb wzr,[x19, x20, LSL ]
0022d988	ldr x9,[x25, #0x18]
0022d98c	mov x10,sp
0022d990	str x8,[sp]
0022d994	ldur x8,[x8, #-0x18]
0022d998	str x9,[x10, x8, LSL #0x0]
0022d99c	add x8,x26,#0x10
0022d9a0	ldrb w9,[sp, #0x48]
0022d9a4	str x8,[sp, #0x8]
0022d9a8	tbz w9,#0x0,0x0022d9b4
0022d9ac	ldr x0,[sp, #0x58]
0022d9b0	bl 0x0041f730
0022d9b4	add x0,x24,#0x8
0022d9b8	bl 0x0041fce0
0022d9bc	mov x0,sp
0022d9c0	add x1,x25,#0x8
0022d9c4	bl 0x0041fcc0
0022d9c8	add x0,x24,#0x70
0022d9cc	bl 0x0041fa00
0022d9d0	ldr x8,[x23, #0x28]
0022d9d4	ldur x9,[x29, #-0x8]
0022d9d8	cmp x8,x9
0022d9dc	b.ne 0x0022da70
0022d9e0	ldp x20,x19,[sp, #0x160]
0022d9e4	ldp x22,x21,[sp, #0x150]
0022d9e8	ldp x24,x23,[sp, #0x140]
0022d9ec	ldp x26,x25,[sp, #0x130]
0022d9f0	ldp x28,x27,[sp, #0x120]
0022d9f4	ldp x29,x30,[sp, #0x110]
0022d9f8	add sp,sp,#0x170
0022d9fc	ret
0022da00	ldr x8,[x23, #0x28]
0022da04	ldur x9,[x29, #-0x8]
0022da08	cmp x8,x9
0022da0c	b.ne 0x0022da70
0022da10	mov x0,x19
0022da14	bl 0x00225510
0022da18	b 0x0022da3c
0022da3c	mov x19,x0
0022da40	adrp x1,0x429000
0022da44	ldr x1,[x1, #0xb88]
0022da48	mov x0,sp
0022da4c	bl 0x0022da80
0022da50	add x0,x24,#0x70
0022da54	bl 0x0041fa00
0022da58	ldr x8,[x23, #0x28]
0022da5c	ldur x9,[x29, #-0x8]
0022da60	cmp x8,x9
0022da64	b.ne 0x0022da70
0022da68	mov x0,x19
0022da6c	bl 0x004217d0
0022da70	bl 0x004217e0
