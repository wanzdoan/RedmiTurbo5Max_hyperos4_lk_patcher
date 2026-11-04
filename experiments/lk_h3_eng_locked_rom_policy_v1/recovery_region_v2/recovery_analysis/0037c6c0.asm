0037c6c0	sub sp,sp,#0x90
0037c6c4	stp x29,x30,[sp, #0x70]
0037c6c8	stp x20,x19,[sp, #0x80]
0037c6cc	add x29,sp,#0x70
0037c6d0	mrs x20,tpidr_el0
0037c6d4	mov x19,x0
0037c6d8	movi v0.2D,#0x0
0037c6dc	ldr x8,[x20, #0x28]
0037c6e0	adrp x0,0x14d000
0037c6e4	add x0,x0,#0x22f
0037c6e8	adrp x2,0x145000
0037c6ec	add x2,x2,#0x9ad
0037c6f0	mov x1,sp
0037c6f4	stur x8,[x29, #-0x8]
0037c6f8	stur q0,[sp, #0x4c]
0037c6fc	stp q0,q0,[sp, #0x10]
0037c700	stp q0,q0,[sp, #0x30]
0037c704	str q0,[sp]
0037c708	bl 0x002a8870
0037c70c	ldr w8,[sp]
0037c710	mov w10,#0x6d6f
0037c714	ldrb w9,[sp, #0x4]
0037c718	movk w10,#0x6765, LSL #16
0037c71c	cmp w8,w10
0037c720	mov w8,#0x61
0037c724	ccmp w9,w8,#0x0,eq
0037c728	b.eq 0x0037c764
0037c72c	ldr w8,[sp]
0037c730	mov w10,#0x696e
0037c734	ldrb w9,[sp, #0x4]
0037c738	movk w10,#0x656b, LSL #16
0037c73c	cmp w8,w10
0037c740	mov w8,#0x6c
0037c744	ccmp w9,w8,#0x0,eq
0037c748	b.eq 0x0037c764
0037c74c	mov x0,x19
0037c750	bl 0x00374090
0037c754	tbz w0,#0x1f,0x0037c770
0037c758	adrp x0,0x14f000
0037c75c	add x0,x0,#0xa30
0037c760	b 0x0037c780
0037c764	mov x0,x19
0037c768	bl 0x00374170
0037c76c	tbnz w0,#0x1f,0x0037c778
0037c770	mov w0,wzr
0037c774	b 0x0037c788
0037c778	adrp x0,0x150000
0037c77c	add x0,x0,#0x679
0037c780	bl 0x0041fbd0
0037c784	mov w0,#0xffffffff
0037c788	ldr x8,[x20, #0x28]
0037c78c	ldur x9,[x29, #-0x8]
0037c790	cmp x8,x9
0037c794	b.ne 0x0037c7a8
0037c798	ldp x20,x19,[sp, #0x80]
0037c79c	ldp x29,x30,[sp, #0x70]
0037c7a0	add sp,sp,#0x90
0037c7a4	ret
0037c7a8	bl 0x004217e0
