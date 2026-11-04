ffff00009070f894	paciasp
ffff00009070f898	adrp x8,-0xffff6f838000
ffff00009070f89c	add x8,x8,#0xac4
ffff00009070f8a0	cmp w0,#0x0
ffff00009070f8a4	adrp x9,-0xffff6f822000
ffff00009070f8a8	add x9,x9,#0x253
ffff00009070f8ac	csel x8,x8,xzr,eq
ffff00009070f8b0	cmp w0,#0x1
ffff00009070f8b4	csel x0,x9,x8,eq
ffff00009070f8b8	autiasp
ffff00009070f8bc	ret
