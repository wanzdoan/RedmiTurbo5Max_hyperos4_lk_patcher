00103e78	paciasp
00103e7c	stp x29,x30,[sp, #-0x10]!
00103e80	mov x29,sp
00103e84	mov w1,#0xfff
00103e88	bl 0x00102094
00103e8c	sxtw x0,w0
00103e90	ldp x29,x30,[sp], #0x10
00103e94	autiasp
00103e98	ret
