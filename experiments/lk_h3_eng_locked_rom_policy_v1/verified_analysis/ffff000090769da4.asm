ffff000090769da4	paciasp
ffff000090769da8	adrp x8,-0xffff6f7e7000
ffff000090769dac	ldrsw x8,[x8, #0x968]
ffff000090769db0	cmp w8,#0x3
ffff000090769db4	b.hi 0xffff000090769dd0
ffff000090769db8	nop
ffff000090769dbc	adr x9,-0xffff6f7f8b0c
ffff000090769dc0	ldrsw x8,[x9, x8, LSL #0x2]
ffff000090769dc4	add x0,x9,x8
ffff000090769dc8	autiasp
ffff000090769dcc	b 0xffff000090718ab8
ffff000090769dd0	autiasp
ffff000090769dd4	ret
