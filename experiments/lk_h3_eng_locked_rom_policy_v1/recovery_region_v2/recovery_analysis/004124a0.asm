004124a0	cmp x1,x3
004124a4	b.ne 0x004124d0
004124a8	stp x29,x30,[sp, #-0x10]!
004124ac	mov x29,sp
004124b0	mov x8,x1
004124b4	mov x1,x2
004124b8	mov x2,x8
004124bc	bl 0x004213c0
004124c0	cmp w0,#0x0
004124c4	cset w0,eq
004124c8	ldp x29,x30,[sp], #0x10
004124cc	ret
004124d0	mov w0,wzr
004124d4	ret
