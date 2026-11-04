ffff0000907b7170	paciasp
ffff0000907b7174	sub sp,sp,#0x90
ffff0000907b7178	stp x29,x30,[sp, #0x60]
ffff0000907b717c	stp x22,x21,[sp, #0x70]
ffff0000907b7180	stp x20,x19,[sp, #0x80]
ffff0000907b7184	add x29,sp,#0x60
ffff0000907b7188	mov x19,x0
ffff0000907b718c	mov w8,#0x1
ffff0000907b7190	add x0,sp,#0x14
ffff0000907b7194	stp xzr,xzr,[sp]
ffff0000907b7198	str w8,[sp, #0x14]
ffff0000907b719c	bl 0xffff0000907b6588
ffff0000907b71a0	cbnz w0,0xffff0000907b7350
ffff0000907b71a4	mov w0,#0xc
ffff0000907b71a8	stp xzr,xzr,[x29, #-0x10]
ffff0000907b71ac	stp xzr,xzr,[x29, #-0x20]
ffff0000907b71b0	str wzr,[sp, #0x38]
ffff0000907b71b4	stp xzr,xzr,[sp, #0x28]
ffff0000907b71b8	bl 0xffff00009074b114
ffff0000907b71bc	str w0,[sp, #0x18]
ffff0000907b71c0	mov w0,#0xd
ffff0000907b71c4	bl 0xffff00009074b114
ffff0000907b71c8	str w0,[sp, #0x1c]
ffff0000907b71cc	mov w0,#0xe
ffff0000907b71d0	bl 0xffff00009074b114
ffff0000907b71d4	str w0,[sp, #0x20]
ffff0000907b71d8	mov w0,#0xf
ffff0000907b71dc	bl 0xffff00009074b114
ffff0000907b71e0	adrp x21,-0xffff6f7cf000
ffff0000907b71e4	ldr x21,[x21, #0x808]
ffff0000907b71e8	str w0,[sp, #0x24]
ffff0000907b71ec	add x0,sp,#0x18
ffff0000907b71f0	ldrh w8,[x21, #0x12]
ffff0000907b71f4	lsl w8,w8,#0x10
ffff0000907b71f8	cmp w8,#0x20, LSL #12
ffff0000907b71fc	b.ne 0xffff0000907b7218
ffff0000907b7200	sub x2,x29,#0x20
ffff0000907b7204	mov w1,#0x10
ffff0000907b7208	sub x20,x29,#0x20
ffff0000907b720c	bl 0xffff0000907a4628
ffff0000907b7210	cbnz w0,0xffff0000907b7350
ffff0000907b7214	b 0xffff0000907b7228
ffff0000907b7218	add x2,sp,#0x28
ffff0000907b721c	mov w1,#0x10
ffff0000907b7220	add x20,sp,#0x28
ffff0000907b7224	bl 0xffff0000907a4428
ffff0000907b7228	mov x0,sp
ffff0000907b722c	mov x1,x20
ffff0000907b7230	mov w2,#0x10
ffff0000907b7234	bl 0xffff00009077f488
ffff0000907b7238	adrp x20,-0xffff6f7cf000
ffff0000907b723c	adrp x22,-0xffff6f7cf000
ffff0000907b7240	ldr x20,[x20, #0x818]
ffff0000907b7244	ldr w8,[x20]
ffff0000907b7248	ldr x22,[x22, #0x820]
ffff0000907b724c	str w8,[x19]
ffff0000907b7250	ldr w8,[x22]
ffff0000907b7254	str w8,[x19, #0x4]
ffff0000907b7258	bl 0xffff0000907aa5f4
ffff0000907b725c	ldr w9,[sp]
ffff0000907b7260	add x11,x21,#0x2a9
ffff0000907b7264	ldr w8,[sp, #0x14]
ffff0000907b7268	mov w2,#0x10
ffff0000907b726c	str w9,[x19, #0x10]
ffff0000907b7270	adrp x9,-0xffff6f7cf000
ffff0000907b7274	stp w0,w8,[x19, #0x8]
ffff0000907b7278	add x0,x19,#0x20
ffff0000907b727c	ldrh w8,[x21, #0x12]
ffff0000907b7280	ldr x9,[x9, #0x810]
ffff0000907b7284	ldur x10,[sp, #0x4]
ffff0000907b7288	lsl w8,w8,#0x10
ffff0000907b728c	cmp w8,#0x20, LSL #12
ffff0000907b7290	add x9,x9,#0x68
ffff0000907b7294	ldr w8,[sp, #0xc]
ffff0000907b7298	csel x1,x9,x11,eq
ffff0000907b729c	stur x10,[x19, #0x14]
ffff0000907b72a0	str w8,[x19, #0x1c]
ffff0000907b72a4	bl 0xffff00009077fb90
ffff0000907b72a8	mov x8,#0x90
ffff0000907b72ac	adrp x0,-0xffff6f817000
ffff0000907b72b0	add x0,x0,#0x5d7
ffff0000907b72b4	movk x8,#0x1326, LSL #16
ffff0000907b72b8	movk x8,#0xffff, LSL #48
ffff0000907b72bc	add x21,x19,#0x50
ffff0000907b72c0	ldr w9,[x8]
ffff0000907b72c4	str w9,[x19, #0x30]
ffff0000907b72c8	ldr w9,[x8, #0x10]
ffff0000907b72cc	str w9,[x19, #0x34]
ffff0000907b72d0	ldr w9,[x8, #0x20]
ffff0000907b72d4	str w9,[x19, #0x38]
ffff0000907b72d8	ldr w9,[x8, #0x30]
ffff0000907b72dc	str w9,[x19, #0x3c]
ffff0000907b72e0	ldr w9,[x8, #0x40]
ffff0000907b72e4	str w9,[x19, #0x40]
ffff0000907b72e8	ldr w9,[x8, #0x50]
ffff0000907b72ec	str w9,[x19, #0x44]
ffff0000907b72f0	ldr w9,[x8, #0x60]
ffff0000907b72f4	str w9,[x19, #0x48]
ffff0000907b72f8	ldr w8,[x8, #0x70]
ffff0000907b72fc	str w8,[x19, #0x4c]
ffff0000907b7300	bl 0xffff00009077ea84
ffff0000907b7304	ldr w19,[x20]
ffff0000907b7308	ldr w20,[x22]
ffff0000907b730c	bl 0xffff0000907aa5f4
ffff0000907b7310	mov w3,w0
ffff0000907b7314	adrp x0,-0xffff6f812000
ffff0000907b7318	add x0,x0,#0x6bb
ffff0000907b731c	mov w1,w19
ffff0000907b7320	mov w2,w20
ffff0000907b7324	bl 0xffff00009077ea84
ffff0000907b7328	ldr w1,[sp, #0x14]
ffff0000907b732c	adrp x0,-0xffff6f817000
ffff0000907b7330	add x0,x0,#0x5f1
ffff0000907b7334	bl 0xffff00009077ea84
ffff0000907b7338	ldp w1,w2,[sp]
ffff0000907b733c	ldp w3,w4,[sp, #0x8]
ffff0000907b7340	adrp x0,-0xffff6f80d000
ffff0000907b7344	add x0,x0,#0xc01
ffff0000907b7348	bl 0xffff00009077ea84
ffff0000907b734c	mov x19,x21
ffff0000907b7350	mov x0,x19
ffff0000907b7354	ldp x20,x19,[sp, #0x80]
ffff0000907b7358	ldp x22,x21,[sp, #0x70]
ffff0000907b735c	ldp x29,x30,[sp, #0x60]
ffff0000907b7360	add sp,sp,#0x90
ffff0000907b7364	autiasp
ffff0000907b7368	ret
