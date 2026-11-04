00238ca0	stp x29,x30,[sp, #-0x20]!
00238ca4	stp x20,x19,[sp, #0x10]
00238ca8	mov x29,sp
00238cac	mov x20,x0
00238cb0	mov x0,x1
00238cb4	mov x19,x1
00238cb8	bl 0x0041f860
00238cbc	mov x2,x0
00238cc0	and x0,x20,#0xffffffff
00238cc4	mov x1,x19
00238cc8	ldp x20,x19,[sp, #0x10]
00238ccc	ldp x29,x30,[sp], #0x20
00238cd0	b 0x00238970
