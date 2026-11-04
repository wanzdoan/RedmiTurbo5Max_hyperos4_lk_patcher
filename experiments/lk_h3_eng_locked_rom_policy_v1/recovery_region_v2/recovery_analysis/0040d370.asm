0040d370	stp x29,x30,[sp, #-0x20]!
0040d374	stp x20,x19,[sp, #0x10]
0040d378	mov x29,sp
0040d37c	mov x19,x1
0040d380	mov w20,w0
0040d384	mov w0,#0x1e
0040d388	mov w1,wzr
0040d38c	mov w2,wzr
0040d390	bl 0x002204cc
0040d394	cbz w0,0x0040d3c0
0040d398	add w8,w20,#0x2
0040d39c	cmp w20,#0x5
0040d3a0	mov w9,#0x7
0040d3a4	mov x1,x19
0040d3a8	csel w0,w8,w9,cc
0040d3ac	mov w2,#0x4
0040d3b0	bl 0x003d0c50
0040d3b4	cmp w0,#0x0
0040d3b8	cset w0,ne
0040d3bc	b 0x0040d3d0
0040d3c0	adrp x8,0x42c000
0040d3c4	ldr w8,[x8, #0xcf8]
0040d3c8	cmp w8,w20
0040d3cc	cset w0,le
0040d3d0	ldp x20,x19,[sp, #0x10]
0040d3d4	ldp x29,x30,[sp], #0x20
0040d3d8	ret
