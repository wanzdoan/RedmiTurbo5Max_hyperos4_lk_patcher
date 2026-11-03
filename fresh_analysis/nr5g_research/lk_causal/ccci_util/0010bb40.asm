0010bb40	paciasp
0010bb44	sub sp,sp,#0xb0
0010bb48	stp x29,x30,[sp, #0x50]
0010bb4c	stp x28,x27,[sp, #0x60]
0010bb50	stp x26,x25,[sp, #0x70]
0010bb54	stp x24,x23,[sp, #0x80]
0010bb58	stp x22,x21,[sp, #0x90]
0010bb5c	stp x20,x19,[sp, #0xa0]
0010bb60	add x29,sp,#0x50
0010bb64	mrs x8,sp_el0
0010bb68	adrp x2,0x111000
0010bb6c	add x2,x2,#0x97b
0010bb70	ldr x8,[x8, #0x620]
0010bb74	mov x0,xzr
0010bb78	mov x1,xzr
0010bb7c	stur x8,[x29, #-0x8]
0010bb80	bl 0x00116038
0010bb84	cbz x0,0x0010bc94
0010bb88	adrp x1,0x10f000
0010bb8c	add x1,x1,#0xe39
0010bb90	mov x2,xzr
0010bb94	mov x19,x0
0010bb98	bl 0x00116050
0010bb9c	adrp x26,0x10c000
0010bba0	adrp x23,0x10c000
0010bba4	adrp x28,0x10c000
0010bba8	adrp x27,0x10c000
0010bbac	cbz x0,0x0010bcc0
0010bbb0	ldr x19,[x0]
0010bbb4	adrp x22,0x110000
0010bbb8	add x22,x22,#0x9d2
0010bbbc	ldp w21,w24,[x0, #0x8]
0010bbc0	ldp w23,w20,[x0, #0x10]
0010bbc4	ldr w25,[x0, #0x1c]
0010bbc8	mov w0,wzr
0010bbcc	mov w1,#0x1
0010bbd0	mov x2,x22
0010bbd4	mov x3,x19
0010bbd8	bl 0x00104638
0010bbdc	mov w0,#0x2
0010bbe0	mov w1,#0x1
0010bbe4	mov x2,x22
0010bbe8	mov x3,x19
0010bbec	bl 0x00104638
0010bbf0	adrp x22,0x10e000
0010bbf4	add x22,x22,#0xa30
0010bbf8	mov w0,wzr
0010bbfc	mov w1,#0x1
0010bc00	mov x2,x22
0010bc04	mov w3,w21
0010bc08	bl 0x00104638
0010bc0c	mov w0,#0x2
0010bc10	mov w1,#0x1
0010bc14	mov x2,x22
0010bc18	mov w3,w21
0010bc1c	bl 0x00104638
0010bc20	adrp x22,0x110000
0010bc24	add x22,x22,#0x5fd
0010bc28	mov w0,wzr
0010bc2c	mov w1,#0x1
0010bc30	mov x2,x22
0010bc34	mov w3,w20
0010bc38	bl 0x00104638
0010bc3c	mov w0,#0x2
0010bc40	mov w1,#0x1
0010bc44	mov x2,x22
0010bc48	mov w3,w20
0010bc4c	bl 0x00104638
0010bc50	adrp x8,0x10c000
0010bc54	str w21,[x27, #0xe44]
0010bc58	str w24,[x8, #0xd98]
0010bc5c	str w25,[x26, #0xd9c]
0010bc60	cbnz x19,0x0010bc68
0010bc64	cbz w24,0x0010cbb4
0010bc68	cbz x19,0x0010cbec
0010bc6c	cmp w21,#0x10, LSL #12
0010bc70	b.hi 0x0010cc04
0010bc74	adrp x8,0x10c000
0010bc78	mov x0,x19
0010bc7c	mov w1,#0x10000
0010bc80	str w23,[x28, #0xd84]
0010bc84	str w20,[x8, #0xd80]
0010bc88	bl 0x00101fb4
0010bc8c	adrp x23,0x10c000
0010bc90	b 0x0010bdc0
0010bc94	adrp x19,0x112000
0010bc98	add x19,x19,#0x219
0010bc9c	mov w1,#0x1
0010bca0	mov x2,x19
0010bca4	bl 0x00104638
0010bca8	mov w0,#0x2
0010bcac	mov w1,#0x1
0010bcb0	mov x2,x19
0010bcb4	bl 0x00104638
0010bcb8	mov w0,#0xffffffff
0010bcbc	b 0x0010ca90
0010bcc0	adrp x20,0x10f000
0010bcc4	add x20,x20,#0x1c8
0010bcc8	mov w1,#0x1
0010bccc	mov x2,x20
0010bcd0	bl 0x00104638
0010bcd4	mov w0,#0x2
0010bcd8	mov w1,#0x1
0010bcdc	mov x2,x20
0010bce0	bl 0x00104638
0010bce4	adrp x1,0x111000
0010bce8	add x1,x1,#0x272
0010bcec	mov x0,x19
0010bcf0	mov x2,xzr
0010bcf4	bl 0x00116050
0010bcf8	cbz x0,0x0010c66c
0010bcfc	ldr x19,[x0]
0010bd00	adrp x22,0x110000
0010bd04	add x22,x22,#0x9d2
0010bd08	ldp w21,w20,[x0, #0x8]
0010bd0c	mov w0,wzr
0010bd10	mov w1,#0x1
0010bd14	mov x2,x22
0010bd18	mov x3,x19
0010bd1c	bl 0x00104638
0010bd20	mov w0,#0x2
0010bd24	mov w1,#0x1
0010bd28	mov x2,x22
0010bd2c	mov x3,x19
0010bd30	bl 0x00104638
0010bd34	adrp x22,0x10e000
0010bd38	add x22,x22,#0xa30
0010bd3c	mov w0,wzr
0010bd40	mov w1,#0x1
0010bd44	mov x2,x22
0010bd48	mov w3,w21
0010bd4c	bl 0x00104638
0010bd50	mov w0,#0x2
0010bd54	mov w1,#0x1
0010bd58	mov x2,x22
0010bd5c	mov w3,w21
0010bd60	bl 0x00104638
0010bd64	adrp x22,0x110000
0010bd68	add x22,x22,#0x5fd
0010bd6c	mov w0,wzr
0010bd70	mov w1,#0x1
0010bd74	mov x2,x22
0010bd78	mov w3,w20
0010bd7c	bl 0x00104638
0010bd80	mov w0,#0x2
0010bd84	mov w1,#0x1
0010bd88	mov x2,x22
0010bd8c	mov w3,w20
0010bd90	bl 0x00104638
0010bd94	str w21,[x27, #0xe44]
0010bd98	cbz x19,0x0010ccc8
0010bd9c	cmp w21,#0x10, LSL #12
0010bda0	b.hi 0x0010cce0
0010bda4	mov w8,#0x1
0010bda8	adrp x9,0x10c000
0010bdac	mov x0,x19
0010bdb0	mov w1,#0x10000
0010bdb4	str w8,[x28, #0xd84]
0010bdb8	str w20,[x9, #0xd80]
0010bdbc	bl 0x00101fb4
0010bdc0	str x0,[x23, #0xd78]
0010bdc4	cbz x0,0x0010c8b4
0010bdc8	adrp x28,0x10c000
0010bdcc	mov w8,#0x1
0010bdd0	strb w8,[x28, #0xe2c]
0010bdd4	bl 0x00101d9c
0010bdd8	bl 0x0010b1e0
0010bddc	bl 0x00109ff8
0010bde0	adrp x0,0x10e000
0010bde4	add x0,x0,#0xf3b
0010bde8	add x1,sp,#0x10
0010bdec	mov w2,#0x4
0010bdf0	str wzr,[sp, #0x10]
0010bdf4	stp xzr,xzr,[sp, #0x20]
0010bdf8	str xzr,[sp, #0x30]
0010bdfc	bl 0x001089ec
0010be00	cmp w0,#0x4
0010be04	adrp x20,0x10c000
0010be08	b.ne 0x0010c678
0010be0c	adrp x0,0x111000
0010be10	add x0,x0,#0x92
0010be14	add x1,sp,#0x20
0010be18	mov w2,#0x18
0010be1c	bl 0x001089ec
0010be20	tbz w0,#0x1f,0x0010be4c
0010be24	adrp x19,0x10f000
0010be28	add x19,x19,#0x64d
0010be2c	mov w0,wzr
0010be30	mov w1,#0x1
0010be34	mov x2,x19
0010be38	bl 0x00104638
0010be3c	mov w0,#0x2
0010be40	mov w1,#0x1
0010be44	mov x2,x19
0010be48	bl 0x00104638
0010be4c	adrp x21,0x10c000
0010be50	ldr w8,[x21, #0xe18]
0010be54	cbnz w8,0x0010c714
0010be58	ldrb w8,[sp, #0x2d]
0010be5c	ldr w9,[sp, #0x28]
0010be60	adrp x19,0x10e000
0010be64	add x19,x19,#0xa59
0010be68	ldr x11,[sp, #0x20]
0010be6c	mov x27,x23
0010be70	sxtb w10,w8
0010be74	str w9,[x21, #0xe18]
0010be78	orr w9,w8,#0xffffff00
0010be7c	adrp x23,0x10c000
0010be80	mov w0,wzr
0010be84	mov w1,#0x1
0010be88	cmp w10,#0x0
0010be8c	mov x2,x19
0010be90	str x11,[x20, #0xe10]
0010be94	csel w3,w9,w8,lt
0010be98	mov w22,#0x1
0010be9c	str w3,[x23, #0xd9c]
0010bea0	bl 0x00104638
0010bea4	ldr w3,[x23, #0xd9c]
0010bea8	mov w0,#0x2
0010beac	mov w1,#0x1
0010beb0	mov x2,x19
0010beb4	bl 0x00104638
0010beb8	ldr w8,[x23, #0xd9c]
0010bebc	cbnz w8,0x0010bec8
0010bec0	adrp x8,0x10c000
0010bec4	strb w22,[x8, #0xdb4]
0010bec8	ldrb w8,[sp, #0x2e]
0010becc	ldr x3,[x20, #0xe10]
0010bed0	adrp x19,0x10f000
0010bed4	add x19,x19,#0xa57
0010bed8	ldr w4,[x21, #0xe18]
0010bedc	adrp x9,0x10c000
0010bee0	mov w0,wzr
0010bee4	mov w1,#0x1
0010bee8	mov x2,x19
0010beec	str w8,[x9, #0xe30]
0010bef0	bl 0x00104638
0010bef4	ldr x3,[x20, #0xe10]
0010bef8	ldr w4,[x21, #0xe18]
0010befc	mov w0,#0x2
0010bf00	mov w1,#0x1
0010bf04	mov x2,x19
0010bf08	bl 0x00104638
0010bf0c	mov w8,#0xffffffff
0010bf10	ldr x26,[x20, #0xe10]
0010bf14	adrp x0,0x111000
0010bf18	add x0,x0,#0x282
0010bf1c	add x1,sp,#0xc
0010bf20	mov w2,#0x4
0010bf24	stp xzr,xzr,[sp, #0x10]
0010bf28	stp xzr,xzr,[sp, #0x30]
0010bf2c	stp xzr,xzr,[sp, #0x20]
0010bf30	stp w8,wzr,[sp, #0x8]
0010bf34	bl 0x001089ec
0010bf38	tbnz w0,#0x1f,0x0010c6ac
0010bf3c	ldr w3,[sp, #0xc]
0010bf40	adrp x19,0x10e000
0010bf44	add x19,x19,#0xe35
0010bf48	mov w0,wzr
0010bf4c	mov w1,#0x1
0010bf50	mov x2,x19
0010bf54	bl 0x00104638
0010bf58	ldr w3,[sp, #0xc]
0010bf5c	mov w0,#0x2
0010bf60	mov w1,#0x1
0010bf64	mov x2,x19
0010bf68	bl 0x00104638
0010bf6c	ldr w8,[sp, #0xc]
0010bf70	cmp w8,#0x1
0010bf74	b.lt 0x0010c098
0010bf78	mov w19,wzr
0010bf7c	adrp x20,0x110000
0010bf80	add x20,x20,#0x301
0010bf84	adrp x21,0x10e000
0010bf88	add x21,x21,#0xbf6
0010bf8c	adrp x22,0x10f000
0010bf90	add x22,x22,#0xa87
0010bf94	adrp x23,0x10f000
0010bf98	add x23,x23,#0x687
0010bf9c	adrp x24,0x10f000
0010bfa0	add x24,x24,#0xeb1
0010bfa4	add x0,sp,#0x20
0010bfa8	mov w1,#0x20
0010bfac	mov x2,x20
0010bfb0	mov w3,w19
0010bfb4	bl 0x00116008
0010bfb8	add x0,sp,#0x20
0010bfbc	add x1,sp,#0x10
0010bfc0	mov w2,#0x10
0010bfc4	bl 0x001089ec
0010bfc8	cbz w0,0x0010c088
0010bfcc	ldp x3,x8,[sp, #0x10]
0010bfd0	mov w0,wzr
0010bfd4	mov w1,#0x1
0010bfd8	mov x2,x21
0010bfdc	add x4,x8,x3
0010bfe0	sub x5,x3,x26
0010bfe4	sub x6,x4,x26
0010bfe8	bl 0x00104638
0010bfec	ldp x3,x8,[sp, #0x10]
0010bff0	mov w0,#0x2
0010bff4	mov w1,#0x1
0010bff8	mov x2,x21
0010bffc	add x4,x8,x3
0010c000	sub x5,x3,x26
0010c004	sub x6,x4,x26
0010c008	bl 0x00104638
0010c00c	add x1,sp,#0x8
0010c010	mov x0,x22
0010c014	mov w2,#0x4
0010c018	bl 0x001089ec
0010c01c	ldr w3,[sp, #0x8]
0010c020	cbz w0,0x0010c064
0010c024	cmp w3,#0x1
0010c028	b.ne 0x0010c064
0010c02c	ldp x0,x8,[sp, #0x10]
0010c030	add x1,x8,x0
0010c034	bl 0x00108020
0010c038	mov w25,w0
0010c03c	mov w0,wzr
0010c040	mov w1,#0x1
0010c044	mov x2,x23
0010c048	mov w3,w25
0010c04c	bl 0x00104638
0010c050	mov w0,#0x2
0010c054	mov w1,#0x1
0010c058	mov x2,x23
0010c05c	mov w3,w25
0010c060	b 0x0010c084
0010c064	mov w0,wzr
0010c068	mov w1,#0x1
0010c06c	mov x2,x24
0010c070	bl 0x00104638
0010c074	ldr w3,[sp, #0x8]
0010c078	mov w0,#0x2
0010c07c	mov w1,#0x1
0010c080	mov x2,x24
0010c084	bl 0x00104638
0010c088	ldr w8,[sp, #0xc]
0010c08c	add w19,w19,#0x1
0010c090	cmp w19,w8
0010c094	b.lt 0x0010bfa4
0010c098	ldrb w8,[x28, #0xe2c]
0010c09c	cmp w8,#0x1
0010c0a0	b.ne 0x0010c114
0010c0a4	adrp x8,0x116000
0010c0a8	mov w1,#0xcc0
0010c0ac	mov w2,#0x400
0010c0b0	ldr x0,[x8, #0xa8]
0010c0b4	bl 0x00116060
0010c0b8	adrp x8,0x10c000
0010c0bc	str x0,[x8, #0xda8]
0010c0c0	cbz x0,0x0010cc44
0010c0c4	mov x1,x0
0010c0c8	adrp x0,0x111000
0010c0cc	add x0,x0,#0xd33
0010c0d0	mov w2,#0x400
0010c0d4	bl 0x001089ec
0010c0d8	cmp w0,#0xbc
0010c0dc	b.eq 0x0010c0f0
0010c0e0	cmp w0,#0x158
0010c0e4	b.eq 0x0010c0f0
0010c0e8	cmp w0,#0x200
0010c0ec	b.ne 0x0010ccf8
0010c0f0	adrp x8,0x10c000
0010c0f4	adrp x1,0x10c000
0010c0f8	add x1,x1,#0xda4
0010c0fc	str w0,[x8, #0xdb0]
0010c100	adrp x0,0x10e000
0010c104	add x0,x0,#0xc32
0010c108	mov w2,#0x4
0010c10c	bl 0x001089ec
0010c110	tbnz w0,#0x1f,0x0010cca4
0010c114	adrp x0,0x111000
0010c118	add x0,x0,#0x9e
0010c11c	add x1,sp,#0x20
0010c120	mov w2,#0x28
0010c124	stp xzr,xzr,[sp, #0x38]
0010c128	stp xzr,xzr,[sp, #0x28]
0010c12c	str xzr,[sp, #0x20]
0010c130	bl 0x001089ec
0010c134	cmp w0,#0x28
0010c138	b.ne 0x0010c6d0
0010c13c	adrp x19,0x10c000
0010c140	add x19,x19,#0xdb8
0010c144	adrp x0,0x10f000
0010c148	add x0,x0,#0x894
0010c14c	mov x1,x19
0010c150	mov w2,#0x10
0010c154	stp xzr,xzr,[x19]
0010c158	bl 0x001089ec
0010c15c	cmp w0,#0x10
0010c160	b.ne 0x0010c74c
0010c164	ldr x3,[x19]
0010c168	ldr w4,[x19, #0x8]
0010c16c	adrp x20,0x111000
0010c170	add x20,x20,#0xb8f
0010c174	mov w0,wzr
0010c178	mov w1,#0x1
0010c17c	mov x2,x20
0010c180	bl 0x00104638
0010c184	ldr x3,[x19]
0010c188	ldr w4,[x19, #0x8]
0010c18c	mov w0,#0x2
0010c190	mov w1,#0x1
0010c194	mov x2,x20
0010c198	bl 0x00104638
0010c19c	adrp x19,0x10c000
0010c1a0	add x19,x19,#0xdc8
0010c1a4	adrp x0,0x110000
0010c1a8	add x0,x0,#0x836
0010c1ac	mov x1,x19
0010c1b0	mov w2,#0x8
0010c1b4	str xzr,[x19]
0010c1b8	bl 0x001089ec
0010c1bc	cmp w0,#0x8
0010c1c0	b.ne 0x0010c770
0010c1c4	ldp w4,w3,[x19]
0010c1c8	adrp x20,0x10f000
0010c1cc	add x20,x20,#0x3e2
0010c1d0	mov w0,wzr
0010c1d4	mov w1,#0x1
0010c1d8	mov x2,x20
0010c1dc	bl 0x00104638
0010c1e0	ldp w4,w3,[x19]
0010c1e4	mov w0,#0x2
0010c1e8	mov w1,#0x1
0010c1ec	mov x2,x20
0010c1f0	bl 0x00104638
0010c1f4	adrp x19,0x10c000
0010c1f8	add x19,x19,#0xdd8
0010c1fc	adrp x0,0x10f000
0010c200	add x0,x0,#0xf18
0010c204	mov x1,x19
0010c208	mov w2,#0x10
0010c20c	bl 0x001089ec
0010c210	cmp w0,#0x10
0010c214	b.ne 0x0010c794
0010c218	ldr x3,[x19]
0010c21c	ldr w4,[x19, #0x8]
0010c220	adrp x20,0x111000
0010c224	add x20,x20,#0xeb
0010c228	mov w0,wzr
0010c22c	mov w1,#0x1
0010c230	mov x2,x20
0010c234	bl 0x00104638
0010c238	ldr x3,[x19]
0010c23c	ldr w4,[x19, #0x8]
0010c240	mov w0,#0x2
0010c244	mov w1,#0x1
0010c248	mov x2,x20
0010c24c	bl 0x00104638
0010c250	adrp x0,0x111000
0010c254	add x0,x0,#0xd3b
0010c258	adrp x1,0x10c000
0010c25c	add x1,x1,#0xdd0
0010c260	mov w2,#0x4
0010c264	bl 0x001089ec
0010c268	cmp w0,#0x4
0010c26c	b.ne 0x0010c7b8
0010c270	adrp x20,0x10c000
0010c274	adrp x19,0x110000
0010c278	add x19,x19,#0x687
0010c27c	ldr w3,[x20, #0xdd0]
0010c280	mov w0,wzr
0010c284	mov w1,#0x1
0010c288	mov x2,x19
0010c28c	bl 0x00104638
0010c290	ldr w3,[x20, #0xdd0]
0010c294	mov w0,#0x2
0010c298	mov w1,#0x1
0010c29c	mov x2,x19
0010c2a0	bl 0x00104638
0010c2a4	adrp x0,0x10f000
0010c2a8	add x0,x0,#0x89d
0010c2ac	adrp x1,0x112000
0010c2b0	add x1,x1,#0xa10
0010c2b4	mov w2,#0x4
0010c2b8	bl 0x001089ec
0010c2bc	cmp w0,#0x4
0010c2c0	b.ne 0x0010c7dc
0010c2c4	adrp x21,0x112000
0010c2c8	adrp x19,0x111000
0010c2cc	add x19,x19,#0x98d
0010c2d0	ldr w3,[x21, #0xa10]
0010c2d4	mov w0,wzr
0010c2d8	mov w1,#0x1
0010c2dc	mov x2,x19
0010c2e0	bl 0x00104638
0010c2e4	ldr w3,[x21, #0xa10]
0010c2e8	mov w0,#0x2
0010c2ec	mov w1,#0x1
0010c2f0	mov x2,x19
0010c2f4	bl 0x00104638
0010c2f8	adrp x0,0x111000
0010c2fc	add x0,x0,#0x8aa
0010c300	adrp x1,0x112000
0010c304	add x1,x1,#0xa14
0010c308	mov w2,#0x4
0010c30c	bl 0x001089ec
0010c310	cmp w0,#0x4
0010c314	b.ne 0x0010c800
0010c318	adrp x20,0x112000
0010c31c	adrp x19,0x111000
0010c320	add x19,x19,#0x28f
0010c324	ldr w3,[x20, #0xa14]
0010c328	mov w0,wzr
0010c32c	mov w1,#0x1
0010c330	mov x2,x19
0010c334	bl 0x00104638
0010c338	ldr w3,[x20, #0xa14]
0010c33c	mov w0,#0x2
0010c340	mov w1,#0x1
0010c344	mov x2,x19
0010c348	bl 0x00104638
0010c34c	adrp x0,0x10e000
0010c350	add x0,x0,#0xac5
0010c354	adrp x1,0x112000
0010c358	add x1,x1,#0xa18
0010c35c	mov w2,#0x4
0010c360	bl 0x001089ec
0010c364	cmp w0,#0x4
0010c368	b.ne 0x0010c824
0010c36c	adrp x22,0x112000
0010c370	adrp x19,0x111000
0010c374	add x19,x19,#0x9ba
0010c378	ldr w3,[x22, #0xa18]
0010c37c	mov w0,wzr
0010c380	mov w1,#0x1
0010c384	mov x2,x19
0010c388	bl 0x00104638
0010c38c	ldr w3,[x22, #0xa18]
0010c390	mov w0,#0x2
0010c394	mov w1,#0x1
0010c398	mov x2,x19
0010c39c	bl 0x00104638
0010c3a0	adrp x1,0x10c000
0010c3a4	add x1,x1,#0xde8
0010c3a8	adrp x0,0x10f000
0010c3ac	add x0,x0,#0xa96
0010c3b0	mov w2,#0x4
0010c3b4	str wzr,[x1]
0010c3b8	bl 0x001089ec
0010c3bc	cmp w0,#0x4
0010c3c0	adrp x22,0x10c000
0010c3c4	b.ne 0x0010c3d0
0010c3c8	ldr w3,[x22, #0xde8]
0010c3cc	b 0x0010c3d8
0010c3d0	mov w3,#0x8000000
0010c3d4	str w3,[x22, #0xde8]
0010c3d8	adrp x19,0x10f000
0010c3dc	add x19,x19,#0x6af
0010c3e0	mov w0,wzr
0010c3e4	mov w1,#0x1
0010c3e8	mov x2,x19
0010c3ec	bl 0x00104638
0010c3f0	ldr w3,[x22, #0xde8]
0010c3f4	mov w0,#0x2
0010c3f8	mov w1,#0x1
0010c3fc	mov x2,x19
0010c400	bl 0x00104638
0010c404	ldp w9,w3,[sp, #0x28]
0010c408	ldr x8,[sp, #0x20]
0010c40c	adrp x19,0x110000
0010c410	add x19,x19,#0x47a
0010c414	adrp x22,0x10c000
0010c418	adrp x23,0x10c000
0010c41c	mov w0,wzr
0010c420	add x4,x8,x9
0010c424	mov w1,#0x1
0010c428	mov x2,x19
0010c42c	add x8,x3,x4
0010c430	str w3,[x22, #0xe28]
0010c434	sub x5,x8,#0x1
0010c438	str x4,[x23, #0xe20]
0010c43c	bl 0x00104638
0010c440	ldr w3,[x22, #0xe28]
0010c444	ldr x4,[x23, #0xe20]
0010c448	mov w0,#0x2
0010c44c	mov w1,#0x1
0010c450	mov x2,x19
0010c454	add x8,x3,x4
0010c458	sub x5,x8,#0x1
0010c45c	bl 0x00104638
0010c460	adrp x0,0x110000
0010c464	add x0,x0,#0x1d5
0010c468	adrp x1,0x10c000
0010c46c	add x1,x1,#0xda0
0010c470	mov w2,#0x4
0010c474	bl 0x001089ec
0010c478	cmp w0,#0x4
0010c47c	b.ne 0x0010c848
0010c480	adrp x22,0x10c000
0010c484	adrp x19,0x110000
0010c488	add x19,x19,#0x841
0010c48c	ldr w3,[x22, #0xda0]
0010c490	mov w0,wzr
0010c494	mov w1,#0x1
0010c498	mov x2,x19
0010c49c	bl 0x00104638
0010c4a0	ldr w3,[x22, #0xda0]
0010c4a4	mov w0,#0x2
0010c4a8	mov w1,#0x1
0010c4ac	mov x2,x19
0010c4b0	bl 0x00104638
0010c4b4	adrp x0,0x10f000
0010c4b8	add x0,x0,#0xcb9
0010c4bc	add x1,sp,#0x10
0010c4c0	mov w2,#0x4
0010c4c4	str wzr,[sp, #0x10]
0010c4c8	bl 0x001089ec
0010c4cc	cmp w0,#0x4
0010c4d0	b.ne 0x0010c86c
0010c4d4	ldr w8,[sp, #0x10]
0010c4d8	adrp x22,0x10c000
0010c4dc	mov w1,#0xdc0
0010c4e0	lsl w19,w8,#0x4
0010c4e4	str w8,[x22, #0xd90]
0010c4e8	mov x0,x19
0010c4ec	bl 0x00116068
0010c4f0	adrp x23,0x10c000
0010c4f4	str x0,[x23, #0xd88]
0010c4f8	cbz x0,0x0010c898
0010c4fc	mov x1,x0
0010c500	adrp x0,0x112000
0010c504	add x0,x0,#0x8e4
0010c508	mov w2,w19
0010c50c	bl 0x001089ec
0010c510	cmp w0,w19
0010c514	b.ne 0x0010c8cc
0010c518	ldr w8,[sp, #0x10]
0010c51c	cbz w8,0x0010c584
0010c520	mov x24,xzr
0010c524	mov x25,xzr
0010c528	adrp x19,0x110000
0010c52c	add x19,x19,#0xa19
0010c530	ldr x8,[x23, #0xd88]
0010c534	mov w0,wzr
0010c538	mov w1,#0x1
0010c53c	mov x2,x19
0010c540	add x8,x8,x24
0010c544	ldp w4,w5,[x8]
0010c548	ldp w6,w3,[x8, #0x8]
0010c54c	bl 0x00104638
0010c550	ldr x8,[x23, #0xd88]
0010c554	mov w0,#0x2
0010c558	mov w1,#0x1
0010c55c	mov x2,x19
0010c560	add x8,x8,x24
0010c564	ldp w4,w5,[x8]
0010c568	ldp w6,w3,[x8, #0x8]
0010c56c	bl 0x00104638
0010c570	ldr w8,[sp, #0x10]
0010c574	add x25,x25,#0x1
0010c578	add x24,x24,#0x10
0010c57c	cmp x25,x8
0010c580	b.cc 0x0010c530
0010c584	ldr x8,[x23, #0xd88]
0010c588	ldr w9,[x22, #0xd90]
0010c58c	cbz x8,0x0010c5b8
0010c590	cbz w9,0x0010c5b8
0010c594	mov w10,wzr
0010c598	add x11,x8,w10, SXTW  #0x4
0010c59c	sxtw x10,w10
0010c5a0	ldr w11,[x11, #0xc]
0010c5a4	cmp w11,#0xa
0010c5a8	b.eq 0x0010c5c0
0010c5ac	add w10,w10,#0x1
0010c5b0	cmp w9,w10
0010c5b4	b.ne 0x0010c598
0010c5b8	cbnz x8,0x0010c604
0010c5bc	b 0x0010c8ec
0010c5c0	add x8,x8,x10, LSL #0x4
0010c5c4	adrp x19,0x110000
0010c5c8	add x19,x19,#0x85d
0010c5cc	mov w0,wzr
0010c5d0	mov w1,#0x1
0010c5d4	mov x2,x19
0010c5d8	ldr w3,[x8, #0x8]
0010c5dc	str w3,[x21, #0xa10]
0010c5e0	bl 0x00104638
0010c5e4	ldr w3,[x21, #0xa10]
0010c5e8	mov w0,#0x2
0010c5ec	mov w1,#0x1
0010c5f0	mov x2,x19
0010c5f4	bl 0x00104638
0010c5f8	ldr x8,[x23, #0xd88]
0010c5fc	ldr w9,[x22, #0xd90]
0010c600	cbz x8,0x0010c8ec
0010c604	cbz w9,0x0010c8ec
0010c608	mov w10,wzr
0010c60c	add x11,x8,w10, SXTW  #0x4
0010c610	sxtw x10,w10
0010c614	ldr w11,[x11, #0xc]
0010c618	cmp w11,#0x1d
0010c61c	b.eq 0x0010c630
0010c620	add w10,w10,#0x1
0010c624	cmp w9,w10
0010c628	b.ne 0x0010c60c
0010c62c	b 0x0010c8ec
0010c630	add x8,x8,x10, LSL #0x4
0010c634	adrp x19,0x110000
0010c638	add x19,x19,#0x6b5
0010c63c	mov w0,wzr
0010c640	mov w1,#0x1
0010c644	mov x2,x19
0010c648	ldr w3,[x8, #0x8]
0010c64c	str w3,[x20, #0xa14]
0010c650	bl 0x00104638
0010c654	ldr w3,[x20, #0xa14]
0010c658	mov w0,#0x2
0010c65c	mov w1,#0x1
0010c660	mov x2,x19
0010c664	bl 0x00104638
0010c668	b 0x0010c8ec
0010c66c	adrp x19,0x111000
0010c670	add x19,x19,#0x81d
0010c674	b 0x0010bc9c
0010c678	adrp x2,0x110000
0010c67c	add x2,x2,#0x9fd
0010c680	mov w0,wzr
0010c684	mov w1,#0x1
0010c688	mov x27,x23
0010c68c	bl 0x00104638
0010c690	adrp x0,0x110000
0010c694	add x0,x0,#0xbc3
0010c698	bl 0x00116048
0010c69c	adrp x8,0x10c000
0010c6a0	ldr w9,[x8, #0xd94]
0010c6a4	orr w9,w9,#0x4
0010c6a8	b 0x0010c744
0010c6ac	adrp x2,0x10e000
0010c6b0	add x2,x2,#0xf45
0010c6b4	mov w0,wzr
0010c6b8	mov w1,#0x1
0010c6bc	bl 0x00104638
0010c6c0	adrp x0,0x10f000
0010c6c4	add x0,x0,#0xe91
0010c6c8	bl 0x00116048
0010c6cc	b 0x0010c098
0010c6d0	adrp x2,0x111000
0010c6d4	add x2,x2,#0xaa
0010c6d8	mov w0,wzr
0010c6dc	mov w1,#0x1
0010c6e0	bl 0x00104638
0010c6e4	adrp x0,0x110000
0010c6e8	add x0,x0,#0x430
0010c6ec	bl 0x00116048
0010c6f0	adrp x8,0x10c000
0010c6f4	ldrb w10,[x28, #0xe2c]
0010c6f8	adrp x11,0x10c000
0010c6fc	ldr w9,[x8, #0xd94]
0010c700	strb wzr,[x11, #0xdb4]
0010c704	orr w9,w9,#0x8
0010c708	str w9,[x8, #0xd94]
0010c70c	tbz w10,#0x0,0x0010ca10
0010c710	b 0x0010c9e4
0010c714	adrp x2,0x110000
0010c718	add x2,x2,#0x417
0010c71c	mov w0,wzr
0010c720	mov w1,#0x1
0010c724	mov x27,x23
0010c728	bl 0x00104638
0010c72c	adrp x0,0x111000
0010c730	add x0,x0,#0x4dd
0010c734	bl 0x00116048
0010c738	adrp x8,0x10c000
0010c73c	ldr w9,[x8, #0xd94]
0010c740	orr w9,w9,#0x2
0010c744	str w9,[x8, #0xd94]
0010c748	b 0x0010bf0c
0010c74c	adrp x2,0x111000
0010c750	add x2,x2,#0xc9
0010c754	mov w0,wzr
0010c758	mov w1,#0x1
0010c75c	bl 0x00104638
0010c760	adrp x0,0x10e000
0010c764	add x0,x0,#0xa7c
0010c768	bl 0x00116048
0010c76c	b 0x0010c164
0010c770	adrp x2,0x111000
0010c774	add x2,x2,#0xefa
0010c778	mov w0,wzr
0010c77c	mov w1,#0x1
0010c780	bl 0x00104638
0010c784	adrp x0,0x112000
0010c788	add x0,x0,#0x6a0
0010c78c	bl 0x00116048
0010c790	b 0x0010c1c4
0010c794	adrp x2,0x10f000
0010c798	add x2,x2,#0x1f6
0010c79c	mov w0,wzr
0010c7a0	mov w1,#0x1
0010c7a4	bl 0x00104638
0010c7a8	adrp x0,0x10e000
0010c7ac	add x0,x0,#0xaa4
0010c7b0	bl 0x00116048
0010c7b4	b 0x0010c218
0010c7b8	adrp x2,0x10e000
0010c7bc	add x2,x2,#0xf5f
0010c7c0	mov w0,wzr
0010c7c4	mov w1,#0x1
0010c7c8	bl 0x00104638
0010c7cc	adrp x0,0x10f000
0010c7d0	add x0,x0,#0xf25
0010c7d4	bl 0x00116048
0010c7d8	b 0x0010c270
0010c7dc	adrp x2,0x110000
0010c7e0	add x2,x2,#0xbe5
0010c7e4	mov w0,wzr
0010c7e8	mov w1,#0x1
0010c7ec	bl 0x00104638
0010c7f0	adrp x0,0x10f000
0010c7f4	add x0,x0,#0xf50
0010c7f8	bl 0x00116048
0010c7fc	b 0x0010c2c4
0010c800	adrp x2,0x110000
0010c804	add x2,x2,#0x455
0010c808	mov w0,wzr
0010c80c	mov w1,#0x1
0010c810	bl 0x00104638
0010c814	adrp x0,0x10f000
0010c818	add x0,x0,#0xc8e
0010c81c	bl 0x00116048
0010c820	b 0x0010c318
0010c824	adrp x2,0x111000
0010c828	add x2,x2,#0xd47
0010c82c	mov w0,wzr
0010c830	mov w1,#0x1
0010c834	bl 0x00104638
0010c838	adrp x0,0x112000
0010c83c	add x0,x0,#0x4a8
0010c840	bl 0x00116048
0010c844	b 0x0010c36c
0010c848	adrp x2,0x10f000
0010c84c	add x2,x2,#0xaac
0010c850	mov w0,wzr
0010c854	mov w1,#0x1
0010c858	bl 0x00104638
0010c85c	adrp x0,0x111000
0010c860	add x0,x0,#0x6bf
0010c864	bl 0x00116048
0010c868	b 0x0010c4b4
0010c86c	adrp x2,0x112000
0010c870	add x2,x2,#0x8bd
0010c874	mov w0,wzr
0010c878	mov w1,#0x1
0010c87c	bl 0x00104638
0010c880	adrp x0,0x111000
0010c884	add x0,x0,#0xbc2
0010c888	bl 0x00116048
0010c88c	adrp x8,0x10c000
0010c890	str wzr,[x8, #0xd90]
0010c894	b 0x0010c8ec
0010c898	adrp x2,0x112000
0010c89c	add x2,x2,#0x6cf
0010c8a0	mov w1,#0x1
0010c8a4	bl 0x00104638
0010c8a8	adrp x0,0x10e000
0010c8ac	add x0,x0,#0xc39
0010c8b0	b 0x0010c8e8
0010c8b4	adrp x19,0x112000
0010c8b8	add x19,x19,#0x897
0010c8bc	mov w20,#0x4
0010c8c0	adrp x2,0x10f000
0010c8c4	add x2,x2,#0xe71
0010c8c8	b 0x0010cc1c
0010c8cc	adrp x2,0x10e000
0010c8d0	add x2,x2,#0xc67
0010c8d4	mov w0,wzr
0010c8d8	mov w1,#0x1
0010c8dc	bl 0x00104638
0010c8e0	adrp x0,0x110000
0010c8e4	add x0,x0,#0xdd3
0010c8e8	bl 0x00116048
0010c8ec	adrp x19,0x10c000
0010c8f0	add x19,x19,#0xdf0
0010c8f4	adrp x0,0x111000
0010c8f8	add x0,x0,#0x9ed
0010c8fc	mov x1,x19
0010c900	mov w2,#0x18
0010c904	stp xzr,xzr,[x19]
0010c908	str xzr,[x19, #0x10]
0010c90c	bl 0x001089ec
0010c910	cmp w0,#0x18
0010c914	adrp x21,0x10c000
0010c918	b.ne 0x0010cac8
0010c91c	ldr w8,[x21, #0xe00]
0010c920	mov w1,#0xdc0
0010c924	add w8,w8,w8, LSL #0x1
0010c928	lsl w19,w8,#0x3
0010c92c	mov x0,x19
0010c930	bl 0x00116068
0010c934	adrp x8,0x10c000
0010c938	str x0,[x8, #0xe08]
0010c93c	cbz x0,0x0010cb74
0010c940	mov x1,x0
0010c944	adrp x0,0x111000
0010c948	add x0,x0,#0x11c
0010c94c	mov w2,w19
0010c950	bl 0x001089ec
0010c954	cmp w0,w19
0010c958	b.ne 0x0010cb90
0010c95c	ldr w8,[x21, #0xe00]
0010c960	cbz w8,0x0010c9d0
0010c964	mov w19,wzr
0010c968	adrp x22,0x10c000
0010c96c	mov w23,#0x18
0010c970	adrp x20,0x10f000
0010c974	add x20,x20,#0x421
0010c978	ldr x8,[x22, #0xe08]
0010c97c	mov w0,wzr
0010c980	mov w1,#0x1
0010c984	smaddl x8,w19,w23,x8
0010c988	mov x2,x20
0010c98c	mov w3,w19
0010c990	ldp w6,w4,[x8, #0xc]
0010c994	ldr w5,[x8, #0x8]
0010c998	bl 0x00104638
0010c99c	ldr x8,[x22, #0xe08]
0010c9a0	mov w0,#0x2
0010c9a4	mov w1,#0x1
0010c9a8	smaddl x8,w19,w23,x8
0010c9ac	mov x2,x20
0010c9b0	mov w3,w19
0010c9b4	ldp w6,w4,[x8, #0xc]
0010c9b8	ldr w5,[x8, #0x8]
0010c9bc	bl 0x00104638
0010c9c0	ldr w8,[x21, #0xe00]
0010c9c4	add w19,w19,#0x1
0010c9c8	cmp w19,w8
0010c9cc	b.cc 0x0010c978
0010c9d0	adrp x8,0x10c000
0010c9d4	ldrb w9,[x28, #0xe2c]
0010c9d8	ldrb w8,[x8, #0xdb4]
0010c9dc	tbz w9,#0x0,0x0010ca0c
0010c9e0	tbnz w8,#0x0,0x0010ca44
0010c9e4	adrp x2,0x110000
0010c9e8	add x2,x2,#0xc05
0010c9ec	mov w0,wzr
0010c9f0	mov w1,#0x1
0010c9f4	bl 0x00104638
0010c9f8	adrp x0,0x110000
0010c9fc	add x0,x0,#0x30c
0010ca00	bl 0x00116048
0010ca04	strb wzr,[x28, #0xe2c]
0010ca08	b 0x0010ca44
0010ca0c	cbnz w8,0x0010cc6c
0010ca10	adrp x19,0x110000
0010ca14	add x19,x19,#0x6d2
0010ca18	mov w0,wzr
0010ca1c	mov w1,#0x1
0010ca20	mov x2,x19
0010ca24	bl 0x00104638
0010ca28	mov w0,#0x2
0010ca2c	mov w1,#0x1
0010ca30	mov x2,x19
0010ca34	bl 0x00104638
0010ca38	adrp x8,0x10c000
0010ca3c	strb wzr,[x28, #0xe2c]
0010ca40	str wzr,[x8, #0xd9c]
0010ca44	adrp x9,0x10c000
0010ca48	ldr x8,[x27, #0xd78]
0010ca4c	mov w0,wzr
0010ca50	ldr w10,[x9, #0xd94]
0010ca54	orr w10,w10,#0x1
0010ca58	str w10,[x9, #0xd94]
0010ca5c	cbz x8,0x0010ca90
0010ca60	adrp x9,0x10c000
0010ca64	ldr w9,[x9, #0xd84]
0010ca68	cmp w9,#0x2
0010ca6c	b.hi 0x0010ca90
0010ca70	adrp x9,0x10c000
0010ca74	mov x0,x8
0010ca78	mov w1,wzr
0010ca7c	ldr w2,[x9, #0xe44]
0010ca80	bl 0x00116070
0010ca84	ldr x0,[x27, #0xd78]
0010ca88	bl 0x00116078
0010ca8c	mov w0,wzr
0010ca90	mrs x8,sp_el0
0010ca94	ldr x8,[x8, #0x620]
0010ca98	ldur x9,[x29, #-0x8]
0010ca9c	cmp x8,x9
0010caa0	b.ne 0x0010cd34
0010caa4	ldp x20,x19,[sp, #0xa0]
0010caa8	ldp x22,x21,[sp, #0x90]
0010caac	ldp x24,x23,[sp, #0x80]
0010cab0	ldp x26,x25,[sp, #0x70]
0010cab4	ldp x28,x27,[sp, #0x60]
0010cab8	ldp x29,x30,[sp, #0x50]
0010cabc	add sp,sp,#0xb0
0010cac0	autiasp
0010cac4	ret
0010cac8	adrp x2,0x10f000
0010cacc	add x2,x2,#0x8ab
0010cad0	mov w0,wzr
0010cad4	mov w1,#0x1
0010cad8	mov w20,#0x1
0010cadc	bl 0x00104638
0010cae0	adrp x0,0x112000
0010cae4	add x0,x0,#0x4d8
0010cae8	bl 0x00116048
0010caec	adrp x8,0x10c000
0010caf0	ldr x8,[x8, #0xdb8]
0010caf4	cbz x8,0x0010c95c
0010caf8	adrp x9,0x10c000
0010cafc	str x8,[x19]
0010cb00	adrp x8,0x116000
0010cb04	ldr w9,[x9, #0xdc0]
0010cb08	ldr x0,[x8, #0x80]
0010cb0c	mov w1,#0xdc0
0010cb10	mov w2,#0x18
0010cb14	stp w9,w20,[x19, #0xc]
0010cb18	bl 0x00116060
0010cb1c	adrp x8,0x10c000
0010cb20	str x0,[x8, #0xe08]
0010cb24	cbz x0,0x0010cb74
0010cb28	ldr x3,[x19]
0010cb2c	ldr w4,[x19, #0xc]
0010cb30	mov w8,#0x1
0010cb34	adrp x20,0x10f000
0010cb38	add x20,x20,#0xf76
0010cb3c	str w8,[x0, #0x10]
0010cb40	str x3,[x0]
0010cb44	mov w1,#0x1
0010cb48	mov x2,x20
0010cb4c	stp wzr,w4,[x0, #0x8]
0010cb50	mov w0,wzr
0010cb54	bl 0x00104638
0010cb58	ldr x3,[x19]
0010cb5c	ldr w4,[x19, #0xc]
0010cb60	mov w0,#0x2
0010cb64	mov w1,#0x1
0010cb68	mov x2,x20
0010cb6c	bl 0x00104638
0010cb70	b 0x0010c95c
0010cb74	adrp x2,0x111000
0010cb78	add x2,x2,#0x4fc
0010cb7c	mov w1,#0x1
0010cb80	bl 0x00104638
0010cb84	adrp x0,0x10f000
0010cb88	add x0,x0,#0xacd
0010cb8c	b 0x0010cbac
0010cb90	adrp x2,0x111000
0010cb94	add x2,x2,#0xa02
0010cb98	mov w0,wzr
0010cb9c	mov w1,#0x1
0010cba0	bl 0x00104638
0010cba4	adrp x0,0x111000
0010cba8	add x0,x0,#0xf23
0010cbac	bl 0x00116048
0010cbb0	b 0x0010c95c
0010cbb4	adrp x2,0x111000
0010cbb8	add x2,x2,#0x856
0010cbbc	mov w0,wzr
0010cbc0	mov w1,#0x1
0010cbc4	bl 0x00104638
0010cbc8	adrp x0,0x10f000
0010cbcc	add x0,x0,#0x62d
0010cbd0	bl 0x00116048
0010cbd4	adrp x8,0x10c000
0010cbd8	mov w0,wzr
0010cbdc	str xzr,[x8, #0xd78]
0010cbe0	adrp x8,0x10c000
0010cbe4	str wzr,[x8, #0xd94]
0010cbe8	b 0x0010ca90
0010cbec	adrp x19,0x10e000
0010cbf0	add x19,x19,#0xe10
0010cbf4	mov w20,#0x4
0010cbf8	adrp x2,0x10e000
0010cbfc	add x2,x2,#0xdf1
0010cc00	b 0x0010cc18
0010cc04	adrp x19,0x110000
0010cc08	add x19,x19,#0x80b
0010cc0c	mov w20,#0x20
0010cc10	adrp x2,0x10f000
0010cc14	add x2,x2,#0xe4c
0010cc18	adrp x23,0x10c000
0010cc1c	mov w0,wzr
0010cc20	mov w1,#0x1
0010cc24	bl 0x00104638
0010cc28	mov x0,x19
0010cc2c	bl 0x00116048
0010cc30	adrp x8,0x10c000
0010cc34	ldr w9,[x8, #0xd94]
0010cc38	orr w9,w9,w20
0010cc3c	str w9,[x8, #0xd94]
0010cc40	b 0x0010bdc8
0010cc44	adrp x2,0x110000
0010cc48	add x2,x2,#0x626
0010cc4c	mov w1,#0x1
0010cc50	bl 0x00104638
0010cc54	adrp x0,0x10f000
0010cc58	add x0,x0,#0xee5
0010cc5c	bl 0x00116048
0010cc60	adrp x8,0x10c000
0010cc64	strb wzr,[x8, #0xdb4]
0010cc68	b 0x0010c114
0010cc6c	adrp x2,0x10e000
0010cc70	add x2,x2,#0xf84
0010cc74	mov w0,wzr
0010cc78	mov w1,#0x1
0010cc7c	bl 0x00104638
0010cc80	adrp x0,0x111000
0010cc84	add x0,x0,#0x8bd
0010cc88	bl 0x00116048
0010cc8c	adrp x8,0x10c000
0010cc90	strb wzr,[x28, #0xe2c]
0010cc94	ldr w9,[x8, #0xd94]
0010cc98	orr w9,w9,#0x10
0010cc9c	str w9,[x8, #0xd94]
0010cca0	b 0x0010ca44
0010cca4	adrp x2,0x110000
0010cca8	add x2,x2,#0x653
0010ccac	mov w0,wzr
0010ccb0	mov w1,#0x1
0010ccb4	bl 0x00104638
0010ccb8	adrp x0,0x111000
0010ccbc	add x0,x0,#0x870
0010ccc0	bl 0x00116048
0010ccc4	b 0x0010c114
0010ccc8	adrp x19,0x10e000
0010cccc	add x19,x19,#0xe10
0010ccd0	mov w20,#0x4
0010ccd4	adrp x2,0x10e000
0010ccd8	add x2,x2,#0xdf1
0010ccdc	b 0x0010cc1c
0010cce0	adrp x19,0x110000
0010cce4	add x19,x19,#0x80b
0010cce8	mov w20,#0x20
0010ccec	adrp x2,0x10f000
0010ccf0	add x2,x2,#0xe4c
0010ccf4	b 0x0010cc1c
0010ccf8	adrp x2,0x111000
0010ccfc	add x2,x2,#0x699
0010cd00	mov w0,wzr
0010cd04	mov w1,#0x1
0010cd08	bl 0x00104638
0010cd0c	adrp x0,0x111000
0010cd10	add x0,x0,#0xece
0010cd14	bl 0x00116048
0010cd18	adrp x8,0x10c000
0010cd1c	adrp x10,0x10c000
0010cd20	ldr w9,[x8, #0xd94]
0010cd24	strb wzr,[x10, #0xdb4]
0010cd28	orr w9,w9,#0x8
0010cd2c	str w9,[x8, #0xd94]
0010cd30	b 0x0010c114
0010cd34	bl 0x00116010
