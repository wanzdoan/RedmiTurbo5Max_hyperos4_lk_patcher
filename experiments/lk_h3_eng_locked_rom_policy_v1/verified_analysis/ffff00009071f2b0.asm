ffff00009071f2b0	paciasp
ffff00009071f2b4	sub sp,sp,#0x30
ffff00009071f2b8	stp x29,x30,[sp, #0x20]
ffff00009071f2bc	add x29,sp,#0x20
ffff00009071f2c0	mov x0,sp
ffff00009071f2c4	mov w1,wzr
ffff00009071f2c8	stp xzr,xzr,[sp, #0x10]
ffff00009071f2cc	stp xzr,xzr,[sp]
ffff00009071f2d0	bl 0xffff00009071f398
ffff00009071f2d4	tbnz w0,#0x1f,0xffff00009071f340
ffff00009071f2d8	ldr w3,[sp, #0x4]
ffff00009071f2dc	mov w8,#0x4342
ffff00009071f2e0	movk w8,#0x4241, LSL #16
ffff00009071f2e4	cmp w3,w8
ffff00009071f2e8	b.ne 0xffff00009071f360
ffff00009071f2ec	ldrh w8,[sp, #0xc]
ffff00009071f2f0	adrp x0,-0xffff6f82b000
ffff00009071f2f4	add x0,x0,#0xf66
ffff00009071f2f8	ldrh w9,[sp, #0xe]
ffff00009071f2fc	adrp x1,-0xffff6f80c000
ffff00009071f300	add x1,x1,#0x2fb
ffff00009071f304	and w3,w8,#0xf
ffff00009071f308	mov w2,#0xdb
ffff00009071f30c	and w4,w9,#0xf
ffff00009071f310	bl 0xffff00009077ea84
ffff00009071f314	ldrh w8,[sp, #0xc]
ffff00009071f318	ldrh w9,[sp, #0xe]
ffff00009071f31c	and w8,w8,#0xf
ffff00009071f320	and w9,w9,#0xf
ffff00009071f324	cmp w8,w9
ffff00009071f328	nop
ffff00009071f32c	adr x9,-0xffff6f8026d0
ffff00009071f330	cset w8,cc
ffff00009071f334	ldrsw x8,[x9, w8, UXTW #0x2]
ffff00009071f338	add x0,x9,x8
ffff00009071f33c	b 0xffff00009071f388
ffff00009071f340	mov w3,w0
ffff00009071f344	adrp x0,-0xffff6f82e000
ffff00009071f348	add x0,x0,#0xb90
ffff00009071f34c	adrp x1,-0xffff6f80c000
ffff00009071f350	add x1,x1,#0x2fb
ffff00009071f354	mov w2,#0xcf
ffff00009071f358	bl 0xffff00009077ea84
ffff00009071f35c	b 0xffff00009071f380
ffff00009071f360	mov w4,#0x4342
ffff00009071f364	adrp x0,-0xffff6f818000
ffff00009071f368	add x0,x0,#0x74
ffff00009071f36c	adrp x1,-0xffff6f80c000
ffff00009071f370	add x1,x1,#0x2fb
ffff00009071f374	mov w2,#0xd6
ffff00009071f378	movk w4,#0x4241, LSL #16
ffff00009071f37c	bl 0xffff00009077ea84
ffff00009071f380	adrp x0,-0xffff6f829000
ffff00009071f384	add x0,x0,#0x88e
ffff00009071f388	ldp x29,x30,[sp, #0x20]
ffff00009071f38c	add sp,sp,#0x30
ffff00009071f390	autiasp
ffff00009071f394	ret
