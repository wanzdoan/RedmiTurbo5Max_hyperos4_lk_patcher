ffff0000907add80	paciasp
ffff0000907add84	stp x29,x30,[sp, #-0x20]!
ffff0000907add88	str x19,[sp, #0x10]
ffff0000907add8c	mov x29,sp
ffff0000907add90	adrp x8,-0xffff6f7cf000
ffff0000907add94	ldr x8,[x8, #0x818]
ffff0000907add98	ldr w8,[x8]
ffff0000907add9c	cmp w8,#0x22
ffff0000907adda0	b.eq 0xffff0000907addd0
ffff0000907adda4	cmp w8,#0x11
ffff0000907adda8	b.eq 0xffff0000907addf8
ffff0000907addac	cbnz w8,0xffff0000907ade2c
ffff0000907addb0	adrp x8,-0xffff6f6f4000
ffff0000907addb4	ldrb w9,[x8, #0x520]
ffff0000907addb8	cbnz w9,0xffff0000907ade18
ffff0000907addbc	mov w9,#0x1
ffff0000907addc0	mov w0,#0x3
ffff0000907addc4	strb w9,[x8, #0x520]
ffff0000907addc8	bl 0xffff000090788640
ffff0000907addcc	b 0xffff0000907ade18
ffff0000907addd0	mov w19,w0
ffff0000907addd4	bl 0xffff0000907aa614
ffff0000907addd8	cmp w0,#0x1
ffff0000907adddc	b.ne 0xffff0000907ade00
ffff0000907adde0	tbz w19,#0x0,0xffff0000907addf8
ffff0000907adde4	adrp x0,-0xffff6f814000
ffff0000907adde8	add x0,x0,#0x84e
ffff0000907addec	adrp x1,-0xffff6f816000
ffff0000907addf0	add x1,x1,#0xe49
ffff0000907addf4	bl 0xffff00009077ea84
ffff0000907addf8	mov w0,#0x1
ffff0000907addfc	b 0xffff0000907ade1c
ffff0000907ade00	tbz w19,#0x0,0xffff0000907ade18
ffff0000907ade04	adrp x0,-0xffff6f824000
ffff0000907ade08	add x0,x0,#0x4fe
ffff0000907ade0c	adrp x1,-0xffff6f816000
ffff0000907ade10	add x1,x1,#0xe49
ffff0000907ade14	bl 0xffff00009077ea84
ffff0000907ade18	mov w0,wzr
ffff0000907ade1c	ldr x19,[sp, #0x10]
ffff0000907ade20	ldp x29,x30,[sp], #0x20
ffff0000907ade24	autiasp
ffff0000907ade28	ret
ffff0000907ade2c	adrp x8,-0xffff6f7cf000
ffff0000907ade30	adrp x0,-0xffff6f812000
ffff0000907ade34	add x0,x0,#0x5f0
ffff0000907ade38	adrp x1,-0xffff6f816000
ffff0000907ade3c	add x1,x1,#0xe49
ffff0000907ade40	ldr x8,[x8, #0x808]
ffff0000907ade44	ldr w2,[x8, #0xe8]
ffff0000907ade48	bl 0xffff00009077ea84
ffff0000907ade4c	adrp x0,-0xffff6f828000
ffff0000907ade50	add x0,x0,#0xd36
ffff0000907ade54	adrp x2,-0xffff6f836000
ffff0000907ade58	add x2,x2,#0x5f8
ffff0000907ade5c	mov w1,#0x117
ffff0000907ade60	bl 0xffff00009077d934
