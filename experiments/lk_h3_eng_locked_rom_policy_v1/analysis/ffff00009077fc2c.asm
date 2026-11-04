ffff00009077fc2c	bti c
ffff00009077fc30	cbz x2,0xffff00009077fc70
ffff00009077fc34	paciasp
ffff00009077fc38	sub x8,x2,#0x1
ffff00009077fc3c	ldrb w10,[x0], #0x1
ffff00009077fc40	ldrb w9,[x1], #0x1
ffff00009077fc44	sub w9,w10,w9
ffff00009077fc48	and w11,w9,#0xff
ffff00009077fc4c	cmp w11,#0x0
ffff00009077fc50	ccmp w10,#0x0,#0x4,eq
ffff00009077fc54	sub x10,x8,#0x1
ffff00009077fc58	ccmp x8,#0x0,#0x4,ne
ffff00009077fc5c	mov x8,x10
ffff00009077fc60	b.ne 0xffff00009077fc3c
ffff00009077fc64	sxtb w0,w9
ffff00009077fc68	autiasp
ffff00009077fc6c	ret
ffff00009077fc70	mov w0,wzr
ffff00009077fc74	ret
