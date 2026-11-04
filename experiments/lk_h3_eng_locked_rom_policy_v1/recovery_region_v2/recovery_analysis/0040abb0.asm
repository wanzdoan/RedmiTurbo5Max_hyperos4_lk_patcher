0040abb0	sub sp,sp,#0x60
0040abb4	stp x29,x30,[sp, #0x10]
0040abb8	str x25,[sp, #0x20]
0040abbc	stp x24,x23,[sp, #0x30]
0040abc0	stp x22,x21,[sp, #0x40]
0040abc4	stp x20,x19,[sp, #0x50]
0040abc8	add x29,sp,#0x10
0040abcc	mov w9,#0x241
0040abd0	mrs x23,tpidr_el0
0040abd4	movk w9,#0x8, LSL #16
0040abd8	tst w2,#0x1
0040abdc	orr w10,w9,#0x8000
0040abe0	mov x19,x1
0040abe4	mov x20,x0
0040abe8	ldr x8,[x23, #0x28]
0040abec	csel w21,w9,w10,ne
0040abf0	add x24,x1,#0x1
0040abf4	str x8,[sp, #0x8]
0040abf8	nop
0040abfc	nop
0040ac00	ldrb w8,[x19]
0040ac04	mov w1,w21
0040ac08	ldr x9,[x19, #0x10]
0040ac0c	mov w2,#0x1b6
0040ac10	tst w8,#0x1
0040ac14	csel x0,x24,x9,eq
0040ac18	bl 0x004200d0
0040ac1c	mov w22,w0
0040ac20	cmn w0,#0x1
0040ac24	b.ne 0x0040ac38
0040ac28	bl 0x0041f880
0040ac2c	ldr w8,[x0]
0040ac30	cmp w8,#0x4
0040ac34	b.eq 0x0040ac00
0040ac38	mov w8,#0xffffffff
0040ac3c	add x0,sp,#0x4
0040ac40	mov w1,w22
0040ac44	mov x2,xzr
0040ac48	str w8,[sp, #0x4]
0040ac4c	bl 0x00225890
0040ac50	ldr w21,[sp, #0x4]
0040ac54	cmn w21,#0x1
0040ac58	b.eq 0x0040ad2c
0040ac5c	ldrb w8,[x20]
0040ac60	ldr x9,[x20, #0x8]
0040ac64	lsr x10,x8,#0x1
0040ac68	tst w8,#0x1
0040ac6c	csel x22,x10,x9,eq
0040ac70	cbz x22,0x0040acc0
0040ac74	ldr x9,[x20, #0x10]
0040ac78	tst w8,#0x1
0040ac7c	csinc x20,x9,x20,ne
0040ac80	mov w0,w21
0040ac84	mov x1,x20
0040ac88	mov x2,x22
0040ac8c	mov x3,#-0x1
0040ac90	bl 0x00420050
0040ac94	cmn x0,#0x1
0040ac98	b.ne 0x0040acb0
0040ac9c	bl 0x0041f880
0040aca0	ldr w25,[x0]
0040aca4	cmp w25,#0x4
0040aca8	b.eq 0x0040ac80
0040acac	b 0x0040acc8
0040acb0	subs x22,x22,x0
0040acb4	b.cc 0x0040ad80
0040acb8	add x20,x20,x0
0040acbc	cbnz x22,0x0040ac80
0040acc0	mov w19,#0x1
0040acc4	b 0x0040acec
0040acc8	ldrb w8,[x19]
0040accc	mov x20,x0
0040acd0	ldr x9,[x19, #0x10]
0040acd4	tst w8,#0x1
0040acd8	csel x8,x24,x9,eq
0040acdc	mov x0,x8
0040ace0	bl 0x0041fbf0
0040ace4	mov w19,wzr
0040ace8	str w25,[x20]
0040acec	ldr w21,[sp, #0x4]
0040acf0	bl 0x0041f880
0040acf4	mov x20,x0
0040acf8	ldr w22,[x0]
0040acfc	cmn w21,#0x1
0040ad00	b.eq 0x0040ad48
0040ad04	adrp x8,0x429000
0040ad08	ldr x8,[x8, #0xae8]
0040ad0c	cbz x8,0x0040ad40
0040ad10	add x1,sp,#0x4
0040ad14	mov w0,#0x3
0040ad18	bl 0x0041f960
0040ad1c	mov x1,x0
0040ad20	mov w0,w21
0040ad24	bl 0x0041f950
0040ad28	b 0x0040ad48
0040ad2c	bl 0x0041f880
0040ad30	ldr w22,[x0]
0040ad34	mov x20,x0
0040ad38	mov w19,wzr
0040ad3c	b 0x0040ad48
0040ad40	mov w0,w21
0040ad44	bl 0x0041f970
0040ad48	str w22,[x20]
0040ad4c	ldr x8,[x23, #0x28]
0040ad50	ldr x9,[sp, #0x8]
0040ad54	cmp x8,x9
0040ad58	b.ne 0x0040ad7c
0040ad5c	mov w0,w19
0040ad60	ldp x20,x19,[sp, #0x50]
0040ad64	ldp x22,x21,[sp, #0x40]
0040ad68	ldp x24,x23,[sp, #0x30]
0040ad6c	ldp x29,x30,[sp, #0x10]
0040ad70	ldr x25,[sp, #0x20]
0040ad74	add sp,sp,#0x60
0040ad78	ret
0040ad7c	bl 0x004217e0
0040ad80	bl 0x00220254
