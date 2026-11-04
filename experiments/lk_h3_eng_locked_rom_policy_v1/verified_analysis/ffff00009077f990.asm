ffff00009077f990	paciasp
ffff00009077f994	ldrb w8,[x0], #0x1
ffff00009077f998	ldrb w9,[x1], #0x1
ffff00009077f99c	cbz w8,0xffff00009077f9a8
ffff00009077f9a0	cmp w8,w9
ffff00009077f9a4	b.eq 0xffff00009077f994
ffff00009077f9a8	sub w8,w8,w9
ffff00009077f9ac	sxtb w0,w8
ffff00009077f9b0	autiasp
ffff00009077f9b4	ret
