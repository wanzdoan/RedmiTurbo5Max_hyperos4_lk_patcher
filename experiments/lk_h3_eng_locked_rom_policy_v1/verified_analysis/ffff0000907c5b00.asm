ffff0000907c5b00	paciasp
ffff0000907c5b04	stp x29,x30,[sp, #-0x20]!
ffff0000907c5b08	str x19,[sp, #0x10]
ffff0000907c5b0c	mov x29,sp
ffff0000907c5b10	adrp x19,-0xffff6f6f3000
ffff0000907c5b14	mov w8,#0x8c00000
ffff0000907c5b18	adrp x5,-0xffff6f807000
ffff0000907c5b1c	add x5,x5,#0xd6
ffff0000907c5b20	mov w0,#0x8c00000
ffff0000907c5b24	mov w1,#0x10000
ffff0000907c5b28	mov x2,#-0x1
ffff0000907c5b2c	mov x3,xzr
ffff0000907c5b30	mov w4,wzr
ffff0000907c5b34	str x8,[x19, #0x908]
ffff0000907c5b38	bl 0xffff00009071931c
ffff0000907c5b3c	cbz x0,0xffff0000907c5b88
ffff0000907c5b40	mov x5,x0
ffff0000907c5b44	adrp x0,-0xffff6f7cf000
ffff0000907c5b48	ldr x2,[x19, #0x908]
ffff0000907c5b4c	adrp x1,-0xffff6f819000
ffff0000907c5b50	add x1,x1,#0xd82
ffff0000907c5b54	adrp x3,-0xffff6f6f3000
ffff0000907c5b58	add x3,x3,#0x910
ffff0000907c5b5c	ldr x0,[x0, #0x678]
ffff0000907c5b60	mov w4,#0xc
ffff0000907c5b64	mov w6,wzr
ffff0000907c5b68	mov w7,wzr
ffff0000907c5b6c	bl 0xffff00009077c2e4
ffff0000907c5b70	cbz w0,0xffff0000907c5b98
ffff0000907c5b74	mov w1,w0
ffff0000907c5b78	adrp x0,-0xffff6f81a000
ffff0000907c5b7c	add x0,x0,#0x4aa
ffff0000907c5b80	bl 0xffff00009077ea84
ffff0000907c5b84	b 0xffff0000907c5b94
ffff0000907c5b88	adrp x0,-0xffff6f831000
ffff0000907c5b8c	add x0,x0,#0x3cc
ffff0000907c5b90	bl 0xffff00009077ea84
ffff0000907c5b94	mov w0,#0x1
ffff0000907c5b98	ldr x19,[sp, #0x10]
ffff0000907c5b9c	ldp x29,x30,[sp], #0x20
ffff0000907c5ba0	autiasp
ffff0000907c5ba4	ret
