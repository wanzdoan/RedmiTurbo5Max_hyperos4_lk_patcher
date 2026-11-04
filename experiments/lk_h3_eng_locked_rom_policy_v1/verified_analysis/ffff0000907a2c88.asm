ffff0000907a2c88	paciasp
ffff0000907a2c8c	sub w8,w3,#0x1
ffff0000907a2c90	cmp w8,#0x2
ffff0000907a2c94	b.cs 0xffff0000907a2ca0
ffff0000907a2c98	autiasp
ffff0000907a2c9c	b 0xffff0000907a5c10
ffff0000907a2ca0	cbnz w3,0xffff0000907a2cac
ffff0000907a2ca4	autiasp
ffff0000907a2ca8	b 0xffff0000907a4b20
ffff0000907a2cac	mov w0,#0x1023
ffff0000907a2cb0	movk w0,#0x10, LSL #16
ffff0000907a2cb4	autiasp
ffff0000907a2cb8	ret
