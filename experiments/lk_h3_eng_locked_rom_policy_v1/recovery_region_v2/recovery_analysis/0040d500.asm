0040d500	stp x29,x30,[sp, #-0x60]!
0040d504	stp x28,x27,[sp, #0x10]
0040d508	stp x26,x25,[sp, #0x20]
0040d50c	stp x24,x23,[sp, #0x30]
0040d510	stp x22,x21,[sp, #0x40]
0040d514	stp x20,x19,[sp, #0x50]
0040d518	mov x29,sp
0040d51c	mov x21,x0
0040d520	mov w0,#0x128
0040d524	mov w19,w5
0040d528	mov x20,x4
0040d52c	mov w22,w3
0040d530	mov w24,w2
0040d534	mov x23,x1
0040d538	bl 0x0041f720
0040d53c	adrp x27,0x429000
0040d540	ldr x27,[x27, #0xb80]
0040d544	adrp x8,0x429000
0040d548	ldr x8,[x8, #0xb88]
0040d54c	mov x25,x0
0040d550	add x1,x0,#0x8
0040d554	add x28,x27,#0x40
0040d558	ldp x9,x8,[x8, #0x8]
0040d55c	str x28,[x0, #0x70]
0040d560	str x9,[x0]
0040d564	ldur x9,[x9, #-0x18]
0040d568	str x8,[x0, x9, LSL #0x0]
0040d56c	ldr x8,[x0]
0040d570	ldur x8,[x8, #-0x18]
0040d574	add x26,x0,x8
0040d578	mov x0,x26
0040d57c	bl 0x0041fa10
0040d580	mov w8,#0xffffffff
0040d584	add x9,x27,#0x18
0040d588	add x0,x25,#0x8
0040d58c	str xzr,[x26, #0x88]
0040d590	str w8,[x26, #0x90]
0040d594	str x9,[x25]
0040d598	str x28,[x25, #0x70]
0040d59c	bl 0x0041fcd0
0040d5a0	adrp x8,0x429000
0040d5a4	ldr x8,[x8, #0xb90]
0040d5a8	movi v0.2D,#0x0
0040d5ac	mov w9,#0x10
0040d5b0	mov x0,x23
0040d5b4	mov w1,#0x2f
0040d5b8	add x8,x8,#0x10
0040d5bc	str w9,[x25, #0x68]
0040d5c0	stur q0,[x25, #0x48]
0040d5c4	stur q0,[x25, #0x58]
0040d5c8	str x8,[x25, #0x8]
0040d5cc	bl 0x00421320
0040d5d0	cmp x0,#0x0
0040d5d4	str w24,[x25, #0x110]
0040d5d8	csinc x8,x23,x0,eq
0040d5dc	str w22,[x25, #0x114]
0040d5e0	str x20,[x25, #0x118]
0040d5e4	str w19,[x25, #0x120]
0040d5e8	str x8,[x25, #0x108]
0040d5ec	str x25,[x21]
0040d5f0	ldp x20,x19,[sp, #0x50]
0040d5f4	ldp x22,x21,[sp, #0x40]
0040d5f8	ldp x24,x23,[sp, #0x30]
0040d5fc	ldp x26,x25,[sp, #0x20]
0040d600	ldp x28,x27,[sp, #0x10]
0040d604	ldp x29,x30,[sp], #0x60
0040d608	ret
