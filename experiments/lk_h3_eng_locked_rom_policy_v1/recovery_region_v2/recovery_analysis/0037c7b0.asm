0037c7b0	sub sp,sp,#0x50
0037c7b4	stp x29,x30,[sp, #0x10]
0037c7b8	str x23,[sp, #0x20]
0037c7bc	stp x22,x21,[sp, #0x30]
0037c7c0	stp x20,x19,[sp, #0x40]
0037c7c4	add x29,sp,#0x10
0037c7c8	mrs x22,tpidr_el0
0037c7cc	mov x19,x0
0037c7d0	ldr x8,[x22, #0x28]
0037c7d4	adrp x0,0x150000
0037c7d8	add x0,x0,#0x15d
0037c7dc	mov x21,x1
0037c7e0	str x8,[sp, #0x8]
0037c7e4	bl 0x0041fbd0
0037c7e8	mov w0,#0x14
0037c7ec	bl 0x0041feb0
0037c7f0	cbz x0,0x0037c844
0037c7f4	mov x20,x0
0037c7f8	ldp x0,x1,[x21]
0037c7fc	mov x2,x20
0037c800	bl 0x00421050
0037c804	mov x23,xzr
0037c808	mov x21,x19
0037c80c	nop
0037c810	ldrb w3,[x20, x23, LSL ]
0037c814	mov x0,x21
0037c818	mov x1,#-0x1
0037c81c	bl 0x0037cd30
0037c820	add x23,x23,#0x1
0037c824	add x21,x21,#0x2
0037c828	cmp x23,#0x14
0037c82c	b.ne 0x0037c810
0037c830	mov x0,x20
0037c834	strb wzr,[x19, #0x28]
0037c838	bl 0x0041f920
0037c83c	mov w0,wzr
0037c840	b 0x0037c858
0037c844	mov w0,#0x4
0037c848	mov x1,xzr
0037c84c	bl 0x0040d370
0037c850	tbnz w0,#0x0,0x0037c880
0037c854	mov w0,#0xffffffff
0037c858	ldr x8,[x22, #0x28]
0037c85c	ldr x9,[sp, #0x8]
0037c860	cmp x8,x9
0037c864	b.ne 0x0037c920
0037c868	ldp x20,x19,[sp, #0x40]
0037c86c	ldp x22,x21,[sp, #0x30]
0037c870	ldp x29,x30,[sp, #0x10]
0037c874	ldr x23,[sp, #0x20]
0037c878	add sp,sp,#0x50
0037c87c	ret
0037c880	bl 0x0041f880
0037c884	mov x19,x0
0037c888	ldr w21,[x0]
0037c88c	adrp x1,0x153000
0037c890	add x1,x1,#0x790
0037c894	mov x0,sp
0037c898	mov w2,#0xe5
0037c89c	mov w3,#0x4
0037c8a0	mov x4,xzr
0037c8a4	mov w5,#0xffffffff
0037c8a8	bl 0x0040d500
0037c8ac	mov x0,sp
0037c8b0	bl 0x0040d3e0
0037c8b4	adrp x1,0x14d000
0037c8b8	add x1,x1,#0x8ee
0037c8bc	mov w2,#0x26
0037c8c0	bl 0x00226b10
0037c8c4	mov w1,#0x14
0037c8c8	bl 0x0041f740
0037c8cc	adrp x1,0x156000
0037c8d0	add x1,x1,#0x1d2
0037c8d4	mov w2,#0x6
0037c8d8	bl 0x00226b10
0037c8dc	mov x0,sp
0037c8e0	bl 0x0040d610
0037c8e4	mov w0,#0xffffffff
0037c8e8	str w21,[x19]
0037c8ec	b 0x0037c858
0037c920	bl 0x004217e0
