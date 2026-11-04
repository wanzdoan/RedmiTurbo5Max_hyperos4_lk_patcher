ffff0000907b393c	paciasp
ffff0000907b3940	stp x29,x30,[sp, #-0x30]!
ffff0000907b3944	stp x22,x21,[sp, #0x10]
ffff0000907b3948	stp x20,x19,[sp, #0x20]
ffff0000907b394c	mov x29,sp
ffff0000907b3950	adrp x0,-0xffff6f812000
ffff0000907b3954	add x0,x0,#0x66e
ffff0000907b3958	adrp x1,-0xffff6f804000
ffff0000907b395c	add x1,x1,#0x53b
ffff0000907b3960	bl 0xffff00009077ea84
ffff0000907b3964	mov x22,xzr
ffff0000907b3968	adrp x21,-0xffff6f6f3000
ffff0000907b396c	add x21,x21,#0x6c0
ffff0000907b3970	adrp x19,-0xffff6f834000
ffff0000907b3974	add x19,x19,#0xf39
ffff0000907b3978	adrp x20,-0xffff6f80f000
ffff0000907b397c	add x20,x20,#0x4e
ffff0000907b3980	b 0xffff0000907b398c
ffff0000907b3984	cmp x22,#0x3c
ffff0000907b3988	b.eq 0xffff0000907b39b0
ffff0000907b398c	ldrb w1,[x21, x22, LSL ]
ffff0000907b3990	mov x0,x19
ffff0000907b3994	bl 0xffff00009077ea84
ffff0000907b3998	add x22,x22,#0x1
ffff0000907b399c	tst x22,#0xf
ffff0000907b39a0	b.ne 0xffff0000907b3984
ffff0000907b39a4	mov x0,x20
ffff0000907b39a8	bl 0xffff00009077ea84
ffff0000907b39ac	b 0xffff0000907b3984
ffff0000907b39b0	adrp x19,-0xffff6f80f000
ffff0000907b39b4	add x19,x19,#0x4e
ffff0000907b39b8	mov x0,x19
ffff0000907b39bc	bl 0xffff00009077ea84
ffff0000907b39c0	adrp x20,-0xffff6f804000
ffff0000907b39c4	add x20,x20,#0x53b
ffff0000907b39c8	adrp x0,-0xffff6f823000
ffff0000907b39cc	add x0,x0,#0xf83
ffff0000907b39d0	mov x1,x20
ffff0000907b39d4	bl 0xffff00009077ea84
ffff0000907b39d8	ldr w2,[x21]
ffff0000907b39dc	adrp x0,-0xffff6f812000
ffff0000907b39e0	add x0,x0,#0x690
ffff0000907b39e4	mov x1,x20
ffff0000907b39e8	bl 0xffff00009077ea84
ffff0000907b39ec	ldr w2,[x21, #0x4]
ffff0000907b39f0	adrp x0,-0xffff6f81f000
ffff0000907b39f4	add x0,x0,#0x303
ffff0000907b39f8	mov x1,x20
ffff0000907b39fc	bl 0xffff00009077ea84
ffff0000907b3a00	ldr w2,[x21, #0x8]
ffff0000907b3a04	adrp x0,-0xffff6f814000
ffff0000907b3a08	add x0,x0,#0x936
ffff0000907b3a0c	mov x1,x20
ffff0000907b3a10	bl 0xffff00009077ea84
ffff0000907b3a14	ldr w2,[x21, #0xc]
ffff0000907b3a18	adrp x0,-0xffff6f821000
ffff0000907b3a1c	add x0,x0,#0x9ae
ffff0000907b3a20	mov x1,x20
ffff0000907b3a24	bl 0xffff00009077ea84
ffff0000907b3a28	ldr w2,[x21, #0x18]
ffff0000907b3a2c	adrp x0,-0xffff6f830000
ffff0000907b3a30	add x0,x0,#0xd92
ffff0000907b3a34	mov x1,x20
ffff0000907b3a38	bl 0xffff00009077ea84
ffff0000907b3a3c	adrp x0,-0xffff6f833000
ffff0000907b3a40	add x0,x0,#0x61c
ffff0000907b3a44	mov x1,x20
ffff0000907b3a48	bl 0xffff00009077ea84
ffff0000907b3a4c	mov x22,xzr
ffff0000907b3a50	adrp x20,-0xffff6f834000
ffff0000907b3a54	add x20,x20,#0xf39
ffff0000907b3a58	b 0xffff0000907b3a64
ffff0000907b3a5c	cmp x22,#0x20
ffff0000907b3a60	b.eq 0xffff0000907b3a8c
ffff0000907b3a64	add x8,x21,x22
ffff0000907b3a68	mov x0,x20
ffff0000907b3a6c	ldrb w1,[x8, #0x1c]
ffff0000907b3a70	bl 0xffff00009077ea84
ffff0000907b3a74	add x22,x22,#0x1
ffff0000907b3a78	tst x22,#0xf
ffff0000907b3a7c	b.ne 0xffff0000907b3a5c
ffff0000907b3a80	mov x0,x19
ffff0000907b3a84	bl 0xffff00009077ea84
ffff0000907b3a88	b 0xffff0000907b3a5c
ffff0000907b3a8c	adrp x19,-0xffff6f80f000
ffff0000907b3a90	add x19,x19,#0x4e
ffff0000907b3a94	mov x0,x19
ffff0000907b3a98	bl 0xffff00009077ea84
ffff0000907b3a9c	adrp x0,-0xffff6f808000
ffff0000907b3aa0	add x0,x0,#0xfce
ffff0000907b3aa4	adrp x1,-0xffff6f804000
ffff0000907b3aa8	add x1,x1,#0x53b
ffff0000907b3aac	bl 0xffff00009077ea84
ffff0000907b3ab0	mov x22,xzr
ffff0000907b3ab4	adrp x21,-0xffff6f6f3000
ffff0000907b3ab8	add x21,x21,#0x700
ffff0000907b3abc	adrp x20,-0xffff6f834000
ffff0000907b3ac0	add x20,x20,#0xf39
ffff0000907b3ac4	b 0xffff0000907b3ad0
ffff0000907b3ac8	cmp x22,#0x3c
ffff0000907b3acc	b.eq 0xffff0000907b3af4
ffff0000907b3ad0	ldrb w1,[x21, x22, LSL ]
ffff0000907b3ad4	mov x0,x20
ffff0000907b3ad8	bl 0xffff00009077ea84
ffff0000907b3adc	add x22,x22,#0x1
ffff0000907b3ae0	tst x22,#0xf
ffff0000907b3ae4	b.ne 0xffff0000907b3ac8
ffff0000907b3ae8	mov x0,x19
ffff0000907b3aec	bl 0xffff00009077ea84
ffff0000907b3af0	b 0xffff0000907b3ac8
ffff0000907b3af4	adrp x19,-0xffff6f80f000
ffff0000907b3af8	add x19,x19,#0x4e
ffff0000907b3afc	mov x0,x19
ffff0000907b3b00	bl 0xffff00009077ea84
ffff0000907b3b04	adrp x20,-0xffff6f804000
ffff0000907b3b08	add x20,x20,#0x53b
ffff0000907b3b0c	adrp x0,-0xffff6f829000
ffff0000907b3b10	add x0,x0,#0x60b
ffff0000907b3b14	mov x1,x20
ffff0000907b3b18	bl 0xffff00009077ea84
ffff0000907b3b1c	ldr w2,[x21]
ffff0000907b3b20	adrp x0,-0xffff6f812000
ffff0000907b3b24	add x0,x0,#0x690
ffff0000907b3b28	mov x1,x20
ffff0000907b3b2c	bl 0xffff00009077ea84
ffff0000907b3b30	ldr w2,[x21, #0x4]
ffff0000907b3b34	adrp x0,-0xffff6f81f000
ffff0000907b3b38	add x0,x0,#0x303
ffff0000907b3b3c	mov x1,x20
ffff0000907b3b40	bl 0xffff00009077ea84
ffff0000907b3b44	ldr w2,[x21, #0x8]
ffff0000907b3b48	adrp x0,-0xffff6f814000
ffff0000907b3b4c	add x0,x0,#0x936
ffff0000907b3b50	mov x1,x20
ffff0000907b3b54	bl 0xffff00009077ea84
ffff0000907b3b58	ldr w2,[x21, #0xc]
ffff0000907b3b5c	adrp x0,-0xffff6f821000
ffff0000907b3b60	add x0,x0,#0x9ae
ffff0000907b3b64	mov x1,x20
ffff0000907b3b68	bl 0xffff00009077ea84
ffff0000907b3b6c	ldr w2,[x21, #0x18]
ffff0000907b3b70	adrp x0,-0xffff6f830000
ffff0000907b3b74	add x0,x0,#0xd92
ffff0000907b3b78	mov x1,x20
ffff0000907b3b7c	bl 0xffff00009077ea84
ffff0000907b3b80	adrp x0,-0xffff6f833000
ffff0000907b3b84	add x0,x0,#0x61c
ffff0000907b3b88	mov x1,x20
ffff0000907b3b8c	bl 0xffff00009077ea84
ffff0000907b3b90	mov x22,xzr
ffff0000907b3b94	adrp x20,-0xffff6f834000
ffff0000907b3b98	add x20,x20,#0xf39
ffff0000907b3b9c	b 0xffff0000907b3ba8
ffff0000907b3ba0	cmp x22,#0x20
ffff0000907b3ba4	b.eq 0xffff0000907b3bd0
ffff0000907b3ba8	add x8,x21,x22
ffff0000907b3bac	mov x0,x20
ffff0000907b3bb0	ldrb w1,[x8, #0x1c]
ffff0000907b3bb4	bl 0xffff00009077ea84
ffff0000907b3bb8	add x22,x22,#0x1
ffff0000907b3bbc	tst x22,#0xf
ffff0000907b3bc0	b.ne 0xffff0000907b3ba0
ffff0000907b3bc4	mov x0,x19
ffff0000907b3bc8	bl 0xffff00009077ea84
ffff0000907b3bcc	b 0xffff0000907b3ba0
ffff0000907b3bd0	ldp x20,x19,[sp, #0x20]
ffff0000907b3bd4	adrp x0,-0xffff6f80f000
ffff0000907b3bd8	add x0,x0,#0x4e
ffff0000907b3bdc	ldp x22,x21,[sp, #0x10]
ffff0000907b3be0	ldp x29,x30,[sp], #0x30
ffff0000907b3be4	autiasp
ffff0000907b3be8	b 0xffff00009077ea84
