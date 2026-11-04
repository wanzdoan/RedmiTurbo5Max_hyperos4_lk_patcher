00224bb0	ldp x10,x9,[x1, #0x8]
00224bb4	ldrb w8,[x1]
00224bb8	tst w8,#0x1
00224bbc	lsr x8,x8,#0x1
00224bc0	csinc x1,x9,x1,ne
00224bc4	csel x2,x8,x10,eq
00224bc8	b 0x00226b10
