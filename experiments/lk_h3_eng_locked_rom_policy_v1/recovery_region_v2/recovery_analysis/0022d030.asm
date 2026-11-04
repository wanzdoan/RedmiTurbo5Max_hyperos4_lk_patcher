0022d030	sub sp,sp,#0x80
0022d034	stp x29,x30,[sp, #0x40]
0022d038	stp x24,x23,[sp, #0x50]
0022d03c	stp x22,x21,[sp, #0x60]
0022d040	stp x20,x19,[sp, #0x70]
0022d044	add x29,sp,#0x40
0022d048	mrs x23,tpidr_el0
0022d04c	mov w19,w0
0022d050	ldr x8,[x23, #0x28]
0022d054	stur x8,[x29, #-0x8]
0022d058	ldp x20,x24,[x1]
0022d05c	stp xzr,xzr,[sp, #0x20]
0022d060	str xzr,[sp, #0x30]
0022d064	cmp x20,x24
0022d068	b.eq 0x0022d104
0022d06c	adrp x21,0x15a000
0022d070	add x21,x21,#0x7db
0022d074	b 0x0022d09c
0022d080	add x0,sp,#0x20
0022d084	mov x1,x20
0022d088	bl 0x0022daf0
0022d08c	str x0,[sp, #0x28]
0022d090	add x20,x20,#0x18
0022d094	cmp x20,x24
0022d098	b.eq 0x0022d104
0022d09c	ldp x10,x9,[x20, #0x8]
0022d0a0	ldrb w8,[x20]
0022d0a4	tst w8,#0x1
0022d0a8	lsr x8,x8,#0x1
0022d0ac	csinc x0,x9,x20,ne
0022d0b0	csel x1,x8,x10,eq
0022d0b4	mov x2,x21
0022d0b8	mov w3,#0xd
0022d0bc	bl 0x00412360
0022d0c0	tbnz w0,#0x0,0x0022d090
0022d0c4	ldp x22,x8,[sp, #0x28]
0022d0c8	cmp x22,x8
0022d0cc	b.cs 0x0022d080
0022d0d0	ldrb w8,[x20]
0022d0d4	tbnz w8,#0x0,0x0022d0f0
0022d0d8	ldr x8,[x20, #0x10]
0022d0dc	add x0,x22,#0x18
0022d0e0	ldr q0,[x20]
0022d0e4	str x8,[x22, #0x10]
0022d0e8	str q0,[x22]
0022d0ec	b 0x0022d08c
0022d0f0	ldp x2,x1,[x20, #0x8]
0022d0f4	mov x0,x22
0022d0f8	bl 0x00225710
0022d0fc	add x0,x22,#0x18
0022d100	b 0x0022d08c
0022d104	adrp x0,0x143000
0022d108	add x0,x0,#0x67
0022d10c	add x8,sp,#0x8
0022d110	mov w1,w19
0022d114	bl 0x00410bc0
0022d118	ldp x8,x9,[sp, #0x28]
0022d11c	cmp x8,x9
0022d120	b.cs 0x0022d140
0022d124	ldr x9,[sp, #0x18]
0022d128	add x10,x8,#0x18
0022d12c	ldur q0,[sp, #0x8]
0022d130	str x9,[x8, #0x10]
0022d134	str q0,[x8]
0022d138	str x10,[sp, #0x28]
0022d13c	b 0x0022d160
0022d140	add x0,sp,#0x20
0022d144	add x1,sp,#0x8
0022d148	bl 0x00225ed0
0022d14c	ldrb w8,[sp, #0x8]
0022d150	str x0,[sp, #0x28]
0022d154	tbz w8,#0x0,0x0022d160
0022d158	ldr x0,[sp, #0x18]
0022d15c	bl 0x0041f730
0022d160	stp xzr,xzr,[sp, #0x8]
0022d164	str xzr,[sp, #0x18]
0022d168	add x0,sp,#0x20
0022d16c	add x1,sp,#0x8
0022d170	bl 0x0041f9c0
0022d174	tbnz w0,#0x0,0x0022d188
0022d178	mov w0,#0x4
0022d17c	mov x1,xzr
0022d180	bl 0x0040d370
0022d184	tbnz w0,#0x0,0x0022d214
0022d188	ldrb w8,[sp, #0x8]
0022d18c	tbz w8,#0x0,0x0022d198
0022d190	ldr x0,[sp, #0x18]
0022d194	bl 0x0041f730
0022d198	ldr x19,[sp, #0x20]
0022d19c	cbz x19,0x0022d1ec
0022d1a0	ldr x8,[sp, #0x28]
0022d1a4	mov x0,x19
0022d1a8	cmp x8,x19
0022d1ac	b.eq 0x0022d1e4
0022d1b0	mov x20,x8
0022d1b4	b 0x0022d1cc
0022d1c0	mov x8,x20
0022d1c4	cmp x20,x19
0022d1c8	b.eq 0x0022d1e0
0022d1cc	ldrb w9,[x20, #-0x18]!
0022d1d0	tbz w9,#0x0,0x0022d1c0
0022d1d4	ldur x0,[x8, #-0x8]
0022d1d8	bl 0x0041f730
0022d1dc	b 0x0022d1c0
0022d1e0	ldr x0,[sp, #0x20]
0022d1e4	str x19,[sp, #0x28]
0022d1e8	bl 0x0041f730
0022d1ec	ldr x8,[x23, #0x28]
0022d1f0	ldur x9,[x29, #-0x8]
0022d1f4	cmp x8,x9
0022d1f8	b.ne 0x0022d2e8
0022d1fc	ldp x20,x19,[sp, #0x70]
0022d200	ldp x22,x21,[sp, #0x60]
0022d204	ldp x24,x23,[sp, #0x50]
0022d208	ldp x29,x30,[sp, #0x40]
0022d20c	add sp,sp,#0x80
0022d210	ret
0022d214	bl 0x0041f880
0022d218	mov x20,x0
0022d21c	ldr w21,[x0]
0022d220	adrp x1,0x147000
0022d224	add x1,x1,#0x470
0022d228	mov x0,sp
0022d22c	mov w2,#0x314
0022d230	mov w3,#0x4
0022d234	mov x4,xzr
0022d238	mov w5,#0xffffffff
0022d23c	bl 0x0040d500
0022d240	mov x0,sp
0022d244	bl 0x0040d3e0
0022d248	add x1,sp,#0x8
0022d24c	bl 0x00224bb0
0022d250	mov x0,sp
0022d254	bl 0x0040d610
0022d258	str w21,[x20]
0022d25c	ldrb w8,[sp, #0x8]
0022d260	tbnz w8,#0x0,0x0022d190
0022d264	b 0x0022d198
0022d2e8	bl 0x004217e0
