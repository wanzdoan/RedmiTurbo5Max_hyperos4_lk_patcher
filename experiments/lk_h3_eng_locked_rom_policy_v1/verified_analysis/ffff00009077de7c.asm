ffff00009077de7c	paciasp
ffff00009077de80	stp x29,x30,[sp, #-0x20]!
ffff00009077de84	str x19,[sp, #0x10]
ffff00009077de88	mov x29,sp
ffff00009077de8c	nop
ffff00009077de90	adr x8,-0xffff6f7d5720
ffff00009077de94	mov x19,x0
ffff00009077de98	ldr x9,[x8, #0x8]
ffff00009077de9c	cmp x9,x8
ffff00009077dea0	b.ne 0xffff00009077debc
ffff00009077dea4	mov x0,x19
ffff00009077dea8	ldr x19,[sp, #0x10]
ffff00009077deac	mov w1,wzr
ffff00009077deb0	ldp x29,x30,[sp], #0x20
ffff00009077deb4	autiasp
ffff00009077deb8	b 0xffff0000907c2e34
ffff00009077debc	bl 0xffff00009077ddb0
ffff00009077dec0	b 0xffff00009077dea4
