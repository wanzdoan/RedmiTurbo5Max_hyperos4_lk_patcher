ffff0000907c4818	paciasp
ffff0000907c481c	sub sp,sp,#0x80
ffff0000907c4820	stp x29,x30,[sp, #0x20]
ffff0000907c4824	stp x28,x27,[sp, #0x30]
ffff0000907c4828	stp x26,x25,[sp, #0x40]
ffff0000907c482c	stp x24,x23,[sp, #0x50]
ffff0000907c4830	stp x22,x21,[sp, #0x60]
ffff0000907c4834	stp x20,x19,[sp, #0x70]
ffff0000907c4838	add x29,sp,#0x20
ffff0000907c483c	mov x19,x0
ffff0000907c4840	adrp x23,-0xffff6f6f3000
ffff0000907c4844	ldr x8,[x0, #0x10]
ffff0000907c4848	adrp x21,-0xffff6f6f3000
ffff0000907c484c	adrp x28,-0xffff6f6f3000
ffff0000907c4850	str xzr,[sp, #0x8]
ffff0000907c4854	cbz x8,0xffff0000907c49f8
ffff0000907c4858	adrp x27,-0xffff6f821000
ffff0000907c485c	add x27,x27,#0xac5
ffff0000907c4860	mov x20,xzr
ffff0000907c4864	adrp x22,-0xffff6f821000
ffff0000907c4868	add x22,x22,#0xaa2
ffff0000907c486c	adrp x25,-0xffff6f80d000
ffff0000907c4870	add x25,x25,#0xd88
ffff0000907c4874	mov x24,x27
ffff0000907c4878	b 0xffff0000907c4894
ffff0000907c487c	mov x27,x24
ffff0000907c4880	str xzr,[sp, #0x8]
ffff0000907c4884	ldr x8,[x19, #0x10]
ffff0000907c4888	add w20,w20,#0x1
ffff0000907c488c	cmp x8,x20
ffff0000907c4890	b.ls 0xffff0000907c49f8
ffff0000907c4894	ldr x8,[x23, #0x8b0]
ffff0000907c4898	cbz x8,0xffff0000907c48b0
ffff0000907c489c	ldr x8,[x21, #0x8b8]
ffff0000907c48a0	cbz x8,0xffff0000907c48fc
ffff0000907c48a4	ldr x8,[x28, #0x8c0]
ffff0000907c48a8	cbnz x8,0xffff0000907c49a4
ffff0000907c48ac	b 0xffff0000907c4954
ffff0000907c48b0	ldr x8,[x19, #0x8]
ffff0000907c48b4	add x4,sp,#0x8
ffff0000907c48b8	mov x2,x27
ffff0000907c48bc	mov x3,xzr
ffff0000907c48c0	add x8,x8,x20, LSL #0x5
ffff0000907c48c4	ldp x0,x1,[x8, #0x8]
ffff0000907c48c8	bl 0xffff0000907bd9a4
ffff0000907c48cc	ldr x8,[sp, #0x8]
ffff0000907c48d0	cbz x8,0xffff0000907c48f0
ffff0000907c48d4	mov x26,x0
ffff0000907c48d8	mov x0,x22
ffff0000907c48dc	mov x1,x27
ffff0000907c48e0	mov w2,w20
ffff0000907c48e4	mov x3,x26
ffff0000907c48e8	bl 0xffff00009077ea84
ffff0000907c48ec	str x26,[x23, #0x8b0]
ffff0000907c48f0	str xzr,[sp, #0x8]
ffff0000907c48f4	ldr x8,[x21, #0x8b8]
ffff0000907c48f8	cbnz x8,0xffff0000907c48a4
ffff0000907c48fc	ldr x8,[x19, #0x8]
ffff0000907c4900	nop
ffff0000907c4904	adr x27,-0xffff6f837752
ffff0000907c4908	add x4,sp,#0x8
ffff0000907c490c	mov x2,x27
ffff0000907c4910	mov x3,xzr
ffff0000907c4914	add x8,x8,x20, LSL #0x5
ffff0000907c4918	ldp x0,x1,[x8, #0x8]
ffff0000907c491c	bl 0xffff0000907bd9a4
ffff0000907c4920	ldr x8,[sp, #0x8]
ffff0000907c4924	cbz x8,0xffff0000907c4944
ffff0000907c4928	mov x26,x0
ffff0000907c492c	mov x0,x22
ffff0000907c4930	mov x1,x27
ffff0000907c4934	mov w2,w20
ffff0000907c4938	mov x3,x26
ffff0000907c493c	bl 0xffff00009077ea84
ffff0000907c4940	str x26,[x21, #0x8b8]
ffff0000907c4944	mov x27,x24
ffff0000907c4948	str xzr,[sp, #0x8]
ffff0000907c494c	ldr x8,[x28, #0x8c0]
ffff0000907c4950	cbnz x8,0xffff0000907c49a4
ffff0000907c4954	ldr x8,[x19, #0x8]
ffff0000907c4958	adrp x27,-0xffff6f80d000
ffff0000907c495c	add x27,x27,#0xd60
ffff0000907c4960	add x4,sp,#0x8
ffff0000907c4964	mov x2,x27
ffff0000907c4968	mov x3,xzr
ffff0000907c496c	add x8,x8,x20, LSL #0x5
ffff0000907c4970	ldp x0,x1,[x8, #0x8]
ffff0000907c4974	bl 0xffff0000907bd9a4
ffff0000907c4978	ldr x8,[sp, #0x8]
ffff0000907c497c	cbz x8,0xffff0000907c499c
ffff0000907c4980	mov x26,x0
ffff0000907c4984	mov x0,x22
ffff0000907c4988	mov x1,x27
ffff0000907c498c	mov w2,w20
ffff0000907c4990	mov x3,x26
ffff0000907c4994	bl 0xffff00009077ea84
ffff0000907c4998	str x26,[x28, #0x8c0]
ffff0000907c499c	mov x27,x24
ffff0000907c49a0	str xzr,[sp, #0x8]
ffff0000907c49a4	adrp x8,-0xffff6f6f3000
ffff0000907c49a8	ldr x8,[x8, #0x8c8]
ffff0000907c49ac	cbnz x8,0xffff0000907c4884
ffff0000907c49b0	ldr x8,[x19, #0x8]
ffff0000907c49b4	add x4,sp,#0x8
ffff0000907c49b8	mov x2,x25
ffff0000907c49bc	mov x3,xzr
ffff0000907c49c0	add x8,x8,x20, LSL #0x5
ffff0000907c49c4	ldp x0,x1,[x8, #0x8]
ffff0000907c49c8	bl 0xffff0000907bd9a4
ffff0000907c49cc	ldr x8,[sp, #0x8]
ffff0000907c49d0	cbz x8,0xffff0000907c487c
ffff0000907c49d4	mov x26,x0
ffff0000907c49d8	mov x0,x22
ffff0000907c49dc	mov x1,x25
ffff0000907c49e0	mov w2,w20
ffff0000907c49e4	mov x3,x26
ffff0000907c49e8	bl 0xffff00009077ea84
ffff0000907c49ec	adrp x8,-0xffff6f6f3000
ffff0000907c49f0	str x26,[x8, #0x8c8]
ffff0000907c49f4	b 0xffff0000907c487c
ffff0000907c49f8	ldr x20,[x23, #0x8b0]
ffff0000907c49fc	cbnz x20,0xffff0000907c4a0c
ffff0000907c4a00	adrp x20,-0xffff6f817000
ffff0000907c4a04	add x20,x20,#0x6ae
ffff0000907c4a08	str x20,[x23, #0x8b0]
ffff0000907c4a0c	mov x0,x20
ffff0000907c4a10	bl 0xffff00009077fb34
ffff0000907c4a14	str wzr,[sp, #0x18]
ffff0000907c4a18	str xzr,[sp, #0x10]
ffff0000907c4a1c	cbz w0,0xffff0000907c4aec
ffff0000907c4a20	cmp w0,#0x1
ffff0000907c4a24	b.ne 0xffff0000907c4a48
ffff0000907c4a28	ldrb w10,[x20]
ffff0000907c4a2c	sub w8,w10,#0x4d
ffff0000907c4a30	cmp w8,#0xd
ffff0000907c4a34	b.hi 0xffff0000907c4a48
ffff0000907c4a38	mov w8,wzr
ffff0000907c4a3c	mov w9,wzr
ffff0000907c4a40	sub w10,w10,#0x47
ffff0000907c4a44	b 0xffff0000907c4ac0
ffff0000907c4a48	mov w8,wzr
ffff0000907c4a4c	and x9,x0,#0xffffffff
ffff0000907c4a50	add x10,sp,#0x10
ffff0000907c4a54	mov w11,#0xa
ffff0000907c4a58	b 0xffff0000907c4a84
ffff0000907c4a5c	add w8,w8,#0x1
ffff0000907c4a60	cmp w8,#0x3
ffff0000907c4a64	b.eq 0xffff0000907c4ab8
ffff0000907c4a68	ubfiz x13,x8,#0x2,#0x20
ffff0000907c4a6c	subs x9,x9,#0x1
ffff0000907c4a70	ldr w14,[x10, x13, LSL #0x0]
ffff0000907c4a74	madd w12,w14,w11,w12
ffff0000907c4a78	sub w12,w12,#0x30
ffff0000907c4a7c	str w12,[x10, x13, LSL #0x0]
ffff0000907c4a80	b.eq 0xffff0000907c4ab8
ffff0000907c4a84	ldrb w12,[x20], #0x1
ffff0000907c4a88	cmp w12,#0x2e
ffff0000907c4a8c	b.eq 0xffff0000907c4a5c
ffff0000907c4a90	sub w13,w12,#0x3a
ffff0000907c4a94	cmn w13,#0xb
ffff0000907c4a98	b.hi 0xffff0000907c4a68
ffff0000907c4a9c	adrp x0,-0xffff6f821000
ffff0000907c4aa0	add x0,x0,#0xae9
ffff0000907c4aa4	adrp x1,-0xffff6f833000
ffff0000907c4aa8	add x1,x1,#0x812
ffff0000907c4aac	bl 0xffff00009077ea84
ffff0000907c4ab0	mov w0,wzr
ffff0000907c4ab4	b 0xffff0000907c4aec
ffff0000907c4ab8	ldp w10,w9,[sp, #0x10]
ffff0000907c4abc	ldr w8,[sp, #0x18]
ffff0000907c4ac0	cmp w10,#0x63
ffff0000907c4ac4	mov w11,#0x63
ffff0000907c4ac8	csel w10,w10,w11,cc
ffff0000907c4acc	cmp w9,#0x63
ffff0000907c4ad0	csel w9,w9,w11,cc
ffff0000907c4ad4	cmp w8,#0x63
ffff0000907c4ad8	mov w12,#0x64
ffff0000907c4adc	csel w8,w8,w11,cc
ffff0000907c4ae0	mov w11,#0x2710
ffff0000907c4ae4	madd w8,w9,w12,w8
ffff0000907c4ae8	madd w0,w10,w11,w8
ffff0000907c4aec	adrp x8,-0xffff6f6f3000
ffff0000907c4af0	ldr x20,[x21, #0x8b8]
ffff0000907c4af4	str w0,[x8, #0x8d0]
ffff0000907c4af8	cbz x20,0xffff0000907c4b1c
ffff0000907c4afc	mov x0,x20
ffff0000907c4b00	bl 0xffff00009077fb34
ffff0000907c4b04	mov x2,x0
ffff0000907c4b08	mov w0,#0x1
ffff0000907c4b0c	mov x1,x20
ffff0000907c4b10	bl 0xffff0000907c4bac
ffff0000907c4b14	adrp x8,-0xffff6f6f3000
ffff0000907c4b18	str w0,[x8, #0x8d4]
ffff0000907c4b1c	ldr x20,[x28, #0x8c0]
ffff0000907c4b20	cbz x20,0xffff0000907c4b44
ffff0000907c4b24	mov x0,x20
ffff0000907c4b28	bl 0xffff00009077fb34
ffff0000907c4b2c	mov x2,x0
ffff0000907c4b30	mov w0,#0x2
ffff0000907c4b34	mov x1,x20
ffff0000907c4b38	bl 0xffff0000907c4bac
ffff0000907c4b3c	adrp x8,-0xffff6f6f3000
ffff0000907c4b40	str w0,[x8, #0x8d8]
ffff0000907c4b44	adrp x8,-0xffff6f6f3000
ffff0000907c4b48	ldr x20,[x8, #0x8c8]
ffff0000907c4b4c	cbz x20,0xffff0000907c4b70
ffff0000907c4b50	mov x0,x20
ffff0000907c4b54	bl 0xffff00009077fb34
ffff0000907c4b58	mov x2,x0
ffff0000907c4b5c	mov w0,#0x3
ffff0000907c4b60	mov x1,x20
ffff0000907c4b64	bl 0xffff0000907c4bac
ffff0000907c4b68	adrp x8,-0xffff6f6f3000
ffff0000907c4b6c	str w0,[x8, #0x8dc]
ffff0000907c4b70	adrp x2,-0xffff6f6f3000
ffff0000907c4b74	add x2,x2,#0x8e0
ffff0000907c4b78	mov x0,x19
ffff0000907c4b7c	mov w1,wzr
ffff0000907c4b80	bl 0xffff0000907c0030
ffff0000907c4b84	ldp x20,x19,[sp, #0x70]
ffff0000907c4b88	mov w0,wzr
ffff0000907c4b8c	ldp x22,x21,[sp, #0x60]
ffff0000907c4b90	ldp x24,x23,[sp, #0x50]
ffff0000907c4b94	ldp x26,x25,[sp, #0x40]
ffff0000907c4b98	ldp x28,x27,[sp, #0x30]
ffff0000907c4b9c	ldp x29,x30,[sp, #0x20]
ffff0000907c4ba0	add sp,sp,#0x80
ffff0000907c4ba4	autiasp
ffff0000907c4ba8	ret
