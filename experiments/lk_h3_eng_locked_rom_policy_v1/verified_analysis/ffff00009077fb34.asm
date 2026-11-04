ffff00009077fb34	paciasp
ffff00009077fb38	mov x8,xzr
ffff00009077fb3c	ldrb w9,[x0, x8, LSL ]
ffff00009077fb40	add x8,x8,#0x1
ffff00009077fb44	cbnz w9,0xffff00009077fb3c
ffff00009077fb48	sub x0,x8,#0x1
ffff00009077fb4c	autiasp
ffff00009077fb50	ret
