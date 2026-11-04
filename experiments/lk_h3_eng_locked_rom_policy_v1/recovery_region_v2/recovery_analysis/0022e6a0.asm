0022e6a0	stp x29,x30,[sp, #-0x20]!
0022e6a4	str x19,[sp, #0x10]
0022e6a8	mov x29,sp
0022e6ac	mov x19,x0
0022e6b0	mov w0,#0x10
0022e6b4	bl 0x0041f720
0022e6b8	ldr x8,[x19, #0x8]
0022e6bc	adrp x9,0x424000
0022e6c0	add x9,x9,#0x138
0022e6c4	stp x9,x8,[x0]
0022e6c8	ldr x19,[sp, #0x10]
0022e6cc	ldp x29,x30,[sp], #0x20
0022e6d0	ret
