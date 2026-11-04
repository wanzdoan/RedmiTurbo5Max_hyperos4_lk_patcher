0026bef0	sub sp,sp,#0x60
0026bef4	stp x29,x30,[sp, #0x30]
0026bef8	stp x22,x21,[sp, #0x40]
0026befc	stp x20,x19,[sp, #0x50]
0026bf00	add x29,sp,#0x30
0026bf04	adrp x10,0x158000
0026bf08	add x10,x10,#0x3fa
0026bf0c	mrs x21,tpidr_el0
0026bf10	mov w8,#0x2a
0026bf14	ldr x9,[x21, #0x28]
0026bf18	ldr q0,[x10]
0026bf1c	ldur x10,[x10, #0xd]
0026bf20	stur x9,[x29, #-0x8]
0026bf24	strb w8,[sp, #0x10]
0026bf28	stur q0,[sp, #0x11]
0026bf2c	stur x10,[sp, #0x1e]
0026bf30	strb wzr,[sp, #0x26]
0026bf34	add x0,sp,#0x10
0026bf38	mov w1,wzr
0026bf3c	bl 0x0040fe90
0026bf40	mov w19,w0
0026bf44	ldrb w8,[sp, #0x10]
0026bf48	tbz w8,#0x0,0x0026bf54
0026bf4c	ldr x0,[sp, #0x20]
0026bf50	bl 0x0041f730
0026bf54	tbz w19,#0x0,0x0026bf80
0026bf58	add x8,sp,#0x10
0026bf5c	mov x0,xzr
0026bf60	str xzr,[sp, #0x10]
0026bf64	bl 0x002a8e60
0026bf68	ldr x0,[sp, #0x10]
0026bf6c	cbz x0,0x0026bf88
0026bf70	bl 0x002c1630
0026bf74	cmp w0,#0x0
0026bf78	cset w0,ne
0026bf7c	b 0x0026bf9c
0026bf80	mov w0,#0x1
0026bf84	b 0x0026bfc0
0026bf88	mov w0,#0x3
0026bf8c	mov x1,xzr
0026bf90	bl 0x0040d370
0026bf94	tbnz w0,#0x0,0x0026bfe4
0026bf98	mov w0,#0x1
0026bf9c	ldr x19,[sp, #0x10]
0026bfa0	str xzr,[sp, #0x10]
0026bfa4	cbz x19,0x0026bfc0
0026bfa8	mov w20,w0
0026bfac	mov x0,x19
0026bfb0	bl 0x002a8c30
0026bfb4	mov x0,x19
0026bfb8	bl 0x0041f730
0026bfbc	mov w0,w20
0026bfc0	ldr x8,[x21, #0x28]
0026bfc4	ldur x9,[x29, #-0x8]
0026bfc8	cmp x8,x9
0026bfcc	b.ne 0x0026c09c
0026bfd0	ldp x20,x19,[sp, #0x50]
0026bfd4	ldp x22,x21,[sp, #0x40]
0026bfd8	ldp x29,x30,[sp, #0x30]
0026bfdc	add sp,sp,#0x60
0026bfe0	ret
0026bfe4	bl 0x0041f880
0026bfe8	mov x19,x0
0026bfec	ldr w22,[x0]
0026bff0	adrp x1,0x14d000
0026bff4	add x1,x1,#0x457
0026bff8	add x0,sp,#0x8
0026bffc	mov w2,#0x55
0026c000	mov w3,#0x3
0026c004	mov x4,xzr
0026c008	mov w5,#0xffffffff
0026c00c	bl 0x0040d500
0026c010	add x0,sp,#0x8
0026c014	bl 0x0040d3e0
0026c018	adrp x1,0x15b000
0026c01c	add x1,x1,#0x7f6
0026c020	mov w2,#0x20
0026c024	bl 0x00226b10
0026c028	add x0,sp,#0x8
0026c02c	bl 0x0040d610
0026c030	mov w0,#0x1
0026c034	str w22,[x19]
0026c038	b 0x0026bf9c
0026c09c	bl 0x004217e0
