ffff0000907b6588	paciasp
ffff0000907b658c	stp x29,x30,[sp, #-0x20]!
ffff0000907b6590	str x19,[sp, #0x10]
ffff0000907b6594	mov x29,sp
ffff0000907b6598	mov x19,x0
ffff0000907b659c	add x0,x29,#0x1c
ffff0000907b65a0	str wzr,[x29, #0x1c]
ffff0000907b65a4	bl 0xffff0000907885fc
ffff0000907b65a8	ldr w8,[x29, #0x1c]
ffff0000907b65ac	mov w0,wzr
ffff0000907b65b0	cmp w8,#0x3
ffff0000907b65b4	cset w8,ne
ffff0000907b65b8	str w8,[x19]
ffff0000907b65bc	ldr x19,[sp, #0x10]
ffff0000907b65c0	ldp x29,x30,[sp], #0x20
ffff0000907b65c4	autiasp
ffff0000907b65c8	ret
