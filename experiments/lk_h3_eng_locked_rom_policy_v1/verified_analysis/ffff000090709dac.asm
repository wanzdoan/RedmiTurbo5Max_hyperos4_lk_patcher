ffff000090709dac	paciasp
ffff000090709db0	stp x29,x30,[sp, #-0x20]!
ffff000090709db4	str x19,[sp, #0x10]
ffff000090709db8	mov x29,sp
ffff000090709dbc	mov x19,x0
ffff000090709dc0	adrp x0,-0xffff6f830000
ffff000090709dc4	add x0,x0,#0xfe3
ffff000090709dc8	mov x1,x19
ffff000090709dcc	bl 0xffff000090709c24
ffff000090709dd0	mov x1,x19
ffff000090709dd4	ldr x19,[sp, #0x10]
ffff000090709dd8	adrp x0,-0xffff6f817000
ffff000090709ddc	add x0,x0,#0x7f1
ffff000090709de0	ldp x29,x30,[sp], #0x20
ffff000090709de4	autiasp
ffff000090709de8	b 0xffff00009077ea84
