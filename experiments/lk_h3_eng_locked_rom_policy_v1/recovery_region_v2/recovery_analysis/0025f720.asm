0025f720	sub sp,sp,#0x70
0025f724	stp x29,x30,[sp, #0x40]
0025f728	stp x22,x21,[sp, #0x50]
0025f72c	stp x20,x19,[sp, #0x60]
0025f730	add x29,sp,#0x40
0025f734	mrs x21,tpidr_el0
0025f738	mov w0,#0x30
0025f73c	ldr x8,[x21, #0x28]
0025f740	stur x8,[x29, #-0x8]
0025f744	bl 0x0041f720
0025f748	adrp x9,0x158000
0025f74c	add x9,x9,#0x381
0025f750	mov w22,#0x31
0025f754	adrp x1,0x57b000
0025f758	add x1,x1,#0x3a8
0025f75c	mov w8,#0x26
0025f760	ldp q0,q1,[x9]
0025f764	strb wzr,[x0, #0x26]
0025f768	adrp x19,0x42c000
0025f76c	add x19,x19,#0xe00
0025f770	stp x22,x8,[x1]
0025f774	str x0,[x1, #0x10]
0025f778	mov x2,x19
0025f77c	ldur x8,[x9, #0x1e]
0025f780	stp q0,q1,[x0]
0025f784	stur x8,[x0, #0x1e]
0025f788	adrp x20,0x429000
0025f78c	ldr x20,[x20, #0xac0]
0025f790	mov x0,x20
0025f794	bl 0x0041f690
0025f798	mov w0,#0x30
0025f79c	bl 0x0041f720
0025f7a0	adrp x9,0x14e000
0025f7a4	add x9,x9,#0xb74
0025f7a8	mov w8,#0x25
0025f7ac	adrp x1,0x57b000
0025f7b0	add x1,x1,#0x3c0
0025f7b4	strb wzr,[x0, #0x25]
0025f7b8	ldp q0,q1,[x9]
0025f7bc	mov x2,x19
0025f7c0	stp x8,x0,[x1, #0x8]
0025f7c4	str x22,[x1]
0025f7c8	ldur x8,[x9, #0x1d]
0025f7cc	stp q0,q1,[x0]
0025f7d0	stur x8,[x0, #0x1d]
0025f7d4	mov x0,x20
0025f7d8	bl 0x0041f690
0025f7dc	mov w0,#0x30
0025f7e0	bl 0x0041f720
0025f7e4	adrp x9,0x14e000
0025f7e8	add x9,x9,#0xb9a
0025f7ec	mov w8,#0x27
0025f7f0	adrp x1,0x57b000
0025f7f4	add x1,x1,#0x3d8
0025f7f8	strb wzr,[x0, #0x27]
0025f7fc	ldp q0,q1,[x9]
0025f800	mov x2,x19
0025f804	stp x8,x0,[x1, #0x8]
0025f808	str x22,[x1]
0025f80c	ldur x8,[x9, #0x1f]
0025f810	stp q0,q1,[x0]
0025f814	stur x8,[x0, #0x1f]
0025f818	mov x0,x20
0025f81c	bl 0x0041f690
0025f820	adrp x10,0x153000
0025f824	add x10,x10,#0x18a
0025f828	mov w9,#0x682e
0025f82c	mov w8,#0x16
0025f830	movk w9,#0x6377, LSL #16
0025f834	strb wzr,[sp, #0x2c]
0025f838	ldr x10,[x10]
0025f83c	strh wzr,[sp, #0x8]
0025f840	strb w8,[sp, #0x20]
0025f844	str w9,[sp, #0x28]
0025f848	stur x10,[sp, #0x21]
0025f84c	adrp x8,0x57b000
0025f850	add x8,x8,#0x460
0025f854	add x0,sp,#0x20
0025f858	add x1,sp,#0x8
0025f85c	bl 0x0040f330
0025f860	ldrb w8,[sp, #0x8]
0025f864	tbz w8,#0x0,0x0025f870
0025f868	ldr x0,[sp, #0x18]
0025f86c	bl 0x0041f730
0025f870	ldrb w8,[sp, #0x20]
0025f874	tbz w8,#0x0,0x0025f880
0025f878	ldr x0,[sp, #0x30]
0025f87c	bl 0x0041f730
0025f880	adrp x0,0x429000
0025f884	ldr x0,[x0, #0xac0]
0025f888	adrp x1,0x57b000
0025f88c	add x1,x1,#0x460
0025f890	adrp x2,0x42c000
0025f894	add x2,x2,#0xe00
0025f898	bl 0x0041f690
0025f89c	adrp x9,0x14d000
0025f8a0	add x9,x9,#0x22f
0025f8a4	mov w8,#0x22
0025f8a8	strh wzr,[sp, #0x8]
0025f8ac	ldr q0,[x9]
0025f8b0	mov w9,#0x65
0025f8b4	strb w8,[sp, #0x20]
0025f8b8	stur q0,[sp, #0x21]
0025f8bc	sturh w9,[sp, #0x31]
0025f8c0	adrp x8,0x57b000
0025f8c4	add x8,x8,#0x478
0025f8c8	add x0,sp,#0x20
0025f8cc	add x1,sp,#0x8
0025f8d0	bl 0x0040f330
0025f8d4	ldrb w8,[sp, #0x8]
0025f8d8	tbz w8,#0x0,0x0025f8e4
0025f8dc	ldr x0,[sp, #0x18]
0025f8e0	bl 0x0041f730
0025f8e4	ldrb w8,[sp, #0x20]
0025f8e8	tbz w8,#0x0,0x0025f8f4
0025f8ec	ldr x0,[sp, #0x30]
0025f8f0	bl 0x0041f730
0025f8f4	adrp x0,0x429000
0025f8f8	ldr x0,[x0, #0xac0]
0025f8fc	adrp x1,0x57b000
0025f900	add x1,x1,#0x478
0025f904	adrp x2,0x42c000
0025f908	add x2,x2,#0xe00
0025f90c	bl 0x0041f690
0025f910	ldr x8,[x21, #0x28]
0025f914	ldur x9,[x29, #-0x8]
0025f918	cmp x8,x9
0025f91c	b.ne 0x0025f974
0025f920	ldp x20,x19,[sp, #0x60]
0025f924	ldp x22,x21,[sp, #0x50]
0025f928	ldp x29,x30,[sp, #0x40]
0025f92c	add sp,sp,#0x70
0025f930	ret
0025f974	bl 0x004217e0
