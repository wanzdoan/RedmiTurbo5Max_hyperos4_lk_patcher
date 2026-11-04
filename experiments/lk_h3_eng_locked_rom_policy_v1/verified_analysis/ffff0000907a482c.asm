ffff0000907a482c	paciasp
ffff0000907a4830	stp x29,x30,[sp, #-0x30]!
ffff0000907a4834	str x21,[sp, #0x10]
ffff0000907a4838	stp x20,x19,[sp, #0x20]
ffff0000907a483c	mov x29,sp
ffff0000907a4840	cbz x0,0xffff0000907a4864
ffff0000907a4844	mov x19,x1
ffff0000907a4848	cbz x1,0xffff0000907a486c
ffff0000907a484c	ldr x8,[x0, #0x48]
ffff0000907a4850	mov x20,x0
ffff0000907a4854	cmp x8,#0x3f
ffff0000907a4858	b.ls 0xffff0000907a4874
ffff0000907a485c	mov w0,#0xfffffffc
ffff0000907a4860	b 0xffff0000907a4b0c
ffff0000907a4864	mov w0,#0xfffffffe
ffff0000907a4868	b 0xffff0000907a4b0c
ffff0000907a486c	mov w0,#0xfffffffd
ffff0000907a4870	b 0xffff0000907a4b0c
ffff0000907a4874	ldr x9,[x20]
ffff0000907a4878	add x10,x8,#0x1
ffff0000907a487c	add x21,x20,#0x58
ffff0000907a4880	mov w11,#0x80
ffff0000907a4884	add x9,x9,x8, LSL #0x3
ffff0000907a4888	str x10,[x20, #0x48]
ffff0000907a488c	str x9,[x20]
ffff0000907a4890	strb w11,[x21, x8, LSL ]
ffff0000907a4894	ldr x8,[x20, #0x48]
ffff0000907a4898	cmp x8,#0x39
ffff0000907a489c	b.cc 0xffff0000907a4964
ffff0000907a48a0	cmp x8,#0x3f
ffff0000907a48a4	b.hi 0xffff0000907a4948
ffff0000907a48a8	add x9,x8,#0x1
ffff0000907a48ac	add x10,x20,x8
ffff0000907a48b0	cmp x9,#0x40
ffff0000907a48b4	str x9,[x20, #0x48]
ffff0000907a48b8	strb wzr,[x10, #0x58]
ffff0000907a48bc	b.eq 0xffff0000907a4948
ffff0000907a48c0	add x10,x8,#0x2
ffff0000907a48c4	add x9,x20,x9
ffff0000907a48c8	cmp x10,#0x40
ffff0000907a48cc	str x10,[x20, #0x48]
ffff0000907a48d0	strb wzr,[x9, #0x58]
ffff0000907a48d4	b.eq 0xffff0000907a4948
ffff0000907a48d8	add x9,x8,#0x3
ffff0000907a48dc	add x10,x20,x10
ffff0000907a48e0	cmp x9,#0x40
ffff0000907a48e4	str x9,[x20, #0x48]
ffff0000907a48e8	strb wzr,[x10, #0x58]
ffff0000907a48ec	b.eq 0xffff0000907a4948
ffff0000907a48f0	add x10,x8,#0x4
ffff0000907a48f4	add x9,x20,x9
ffff0000907a48f8	cmp x10,#0x40
ffff0000907a48fc	str x10,[x20, #0x48]
ffff0000907a4900	strb wzr,[x9, #0x58]
ffff0000907a4904	b.eq 0xffff0000907a4948
ffff0000907a4908	add x9,x8,#0x5
ffff0000907a490c	add x10,x20,x10
ffff0000907a4910	cmp x9,#0x40
ffff0000907a4914	str x9,[x20, #0x48]
ffff0000907a4918	strb wzr,[x10, #0x58]
ffff0000907a491c	b.eq 0xffff0000907a4948
ffff0000907a4920	add x10,x8,#0x6
ffff0000907a4924	add x9,x20,x9
ffff0000907a4928	cmp x10,#0x40
ffff0000907a492c	str x10,[x20, #0x48]
ffff0000907a4930	strb wzr,[x9, #0x58]
ffff0000907a4934	b.eq 0xffff0000907a4948
ffff0000907a4938	add x8,x8,#0x7
ffff0000907a493c	add x9,x20,x10
ffff0000907a4940	str x8,[x20, #0x48]
ffff0000907a4944	strb wzr,[x9, #0x58]
ffff0000907a4948	mov w8,#0x40
ffff0000907a494c	mov x0,x20
ffff0000907a4950	mov x1,x21
ffff0000907a4954	str x8,[x20, #0x50]
ffff0000907a4958	bl 0xffff00009079c180
ffff0000907a495c	mov x8,xzr
ffff0000907a4960	b 0xffff0000907a496c
ffff0000907a4964	cmp x8,#0x38
ffff0000907a4968	b.eq 0xffff0000907a4988
ffff0000907a496c	add x9,x20,#0x58
ffff0000907a4970	add x10,x8,#0x1
ffff0000907a4974	cmp x10,#0x38
ffff0000907a4978	str x10,[x20, #0x48]
ffff0000907a497c	strb wzr,[x9, x8, LSL ]
ffff0000907a4980	mov x8,x10
ffff0000907a4984	b.ne 0xffff0000907a4970
ffff0000907a4988	ldr x8,[x20]
ffff0000907a498c	mov w9,#0x40
ffff0000907a4990	mov x0,x20
ffff0000907a4994	mov x1,x21
ffff0000907a4998	rev x8,x8
ffff0000907a499c	str x9,[x20, #0x50]
ffff0000907a49a0	str x8,[x20, #0x90]
ffff0000907a49a4	bl 0xffff00009079c180
ffff0000907a49a8	ldr x8,[x20, #0x8]
ffff0000907a49ac	mov w0,wzr
ffff0000907a49b0	lsr x8,x8,#0x18
ffff0000907a49b4	strb w8,[x19]
ffff0000907a49b8	ldr x8,[x20, #0x8]
ffff0000907a49bc	lsr x8,x8,#0x10
ffff0000907a49c0	strb w8,[x19, #0x1]
ffff0000907a49c4	ldr x8,[x20, #0x8]
ffff0000907a49c8	lsr x8,x8,#0x8
ffff0000907a49cc	strb w8,[x19, #0x2]
ffff0000907a49d0	ldr x8,[x20, #0x8]
ffff0000907a49d4	strb w8,[x19, #0x3]
ffff0000907a49d8	ldr x8,[x20, #0x10]
ffff0000907a49dc	lsr x8,x8,#0x18
ffff0000907a49e0	strb w8,[x19, #0x4]
ffff0000907a49e4	ldr x8,[x20, #0x10]
ffff0000907a49e8	lsr x8,x8,#0x10
ffff0000907a49ec	strb w8,[x19, #0x5]
ffff0000907a49f0	ldr x8,[x20, #0x10]
ffff0000907a49f4	lsr x8,x8,#0x8
ffff0000907a49f8	strb w8,[x19, #0x6]
ffff0000907a49fc	ldr x8,[x20, #0x10]
ffff0000907a4a00	strb w8,[x19, #0x7]
ffff0000907a4a04	ldr x8,[x20, #0x18]
ffff0000907a4a08	lsr x8,x8,#0x18
ffff0000907a4a0c	strb w8,[x19, #0x8]
ffff0000907a4a10	ldr x8,[x20, #0x18]
ffff0000907a4a14	lsr x8,x8,#0x10
ffff0000907a4a18	strb w8,[x19, #0x9]
ffff0000907a4a1c	ldr x8,[x20, #0x18]
ffff0000907a4a20	lsr x8,x8,#0x8
ffff0000907a4a24	strb w8,[x19, #0xa]
ffff0000907a4a28	ldr x8,[x20, #0x18]
ffff0000907a4a2c	strb w8,[x19, #0xb]
ffff0000907a4a30	ldr x8,[x20, #0x20]
ffff0000907a4a34	lsr x8,x8,#0x18
ffff0000907a4a38	strb w8,[x19, #0xc]
ffff0000907a4a3c	ldr x8,[x20, #0x20]
ffff0000907a4a40	lsr x8,x8,#0x10
ffff0000907a4a44	strb w8,[x19, #0xd]
ffff0000907a4a48	ldr x8,[x20, #0x20]
ffff0000907a4a4c	lsr x8,x8,#0x8
ffff0000907a4a50	strb w8,[x19, #0xe]
ffff0000907a4a54	ldr x8,[x20, #0x20]
ffff0000907a4a58	strb w8,[x19, #0xf]
ffff0000907a4a5c	ldr x8,[x20, #0x28]
ffff0000907a4a60	lsr x8,x8,#0x18
ffff0000907a4a64	strb w8,[x19, #0x10]
ffff0000907a4a68	ldr x8,[x20, #0x28]
ffff0000907a4a6c	lsr x8,x8,#0x10
ffff0000907a4a70	strb w8,[x19, #0x11]
ffff0000907a4a74	ldr x8,[x20, #0x28]
ffff0000907a4a78	lsr x8,x8,#0x8
ffff0000907a4a7c	strb w8,[x19, #0x12]
ffff0000907a4a80	ldr x8,[x20, #0x28]
ffff0000907a4a84	strb w8,[x19, #0x13]
ffff0000907a4a88	ldr x8,[x20, #0x30]
ffff0000907a4a8c	lsr x8,x8,#0x18
ffff0000907a4a90	strb w8,[x19, #0x14]
ffff0000907a4a94	ldr x8,[x20, #0x30]
ffff0000907a4a98	lsr x8,x8,#0x10
ffff0000907a4a9c	strb w8,[x19, #0x15]
ffff0000907a4aa0	ldr x8,[x20, #0x30]
ffff0000907a4aa4	lsr x8,x8,#0x8
ffff0000907a4aa8	strb w8,[x19, #0x16]
ffff0000907a4aac	ldr x8,[x20, #0x30]
ffff0000907a4ab0	strb w8,[x19, #0x17]
ffff0000907a4ab4	ldr x8,[x20, #0x38]
ffff0000907a4ab8	lsr x8,x8,#0x18
ffff0000907a4abc	strb w8,[x19, #0x18]
ffff0000907a4ac0	ldr x8,[x20, #0x38]
ffff0000907a4ac4	lsr x8,x8,#0x10
ffff0000907a4ac8	strb w8,[x19, #0x19]
ffff0000907a4acc	ldr x8,[x20, #0x38]
ffff0000907a4ad0	lsr x8,x8,#0x8
ffff0000907a4ad4	strb w8,[x19, #0x1a]
ffff0000907a4ad8	ldr x8,[x20, #0x38]
ffff0000907a4adc	strb w8,[x19, #0x1b]
ffff0000907a4ae0	ldr x8,[x20, #0x40]
ffff0000907a4ae4	lsr x8,x8,#0x18
ffff0000907a4ae8	strb w8,[x19, #0x1c]
ffff0000907a4aec	ldr x8,[x20, #0x40]
ffff0000907a4af0	lsr x8,x8,#0x10
ffff0000907a4af4	strb w8,[x19, #0x1d]
ffff0000907a4af8	ldr x8,[x20, #0x40]
ffff0000907a4afc	lsr x8,x8,#0x8
ffff0000907a4b00	strb w8,[x19, #0x1e]
ffff0000907a4b04	ldr x8,[x20, #0x40]
ffff0000907a4b08	strb w8,[x19, #0x1f]
ffff0000907a4b0c	ldp x20,x19,[sp, #0x20]
ffff0000907a4b10	ldr x21,[sp, #0x10]
ffff0000907a4b14	ldp x29,x30,[sp], #0x30
ffff0000907a4b18	autiasp
ffff0000907a4b1c	ret
