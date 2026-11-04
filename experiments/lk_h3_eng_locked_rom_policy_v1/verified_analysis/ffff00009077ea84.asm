ffff00009077ea84	paciasp
ffff00009077ea88	sub sp,sp,#0xc0
ffff00009077ea8c	stp x29,x30,[sp, #0xa0]
ffff00009077ea90	stp x20,x19,[sp, #0xb0]
ffff00009077ea94	add x29,sp,#0xa0
ffff00009077ea98	adrp x20,-0xffff6f7d6000
ffff00009077ea9c	add x20,x20,#0x900
ffff00009077eaa0	add x9,sp,#0x8
ffff00009077eaa4	mov x19,x0
ffff00009077eaa8	add x8,x29,#0x20
ffff00009077eaac	add x9,x9,#0x38
ffff00009077eab0	mov w10,#0xffffffc8
ffff00009077eab4	ldr x0,[x20]
ffff00009077eab8	stp x3,x4,[sp, #0x18]
ffff00009077eabc	stp x1,x2,[sp, #0x8]
ffff00009077eac0	stp x5,x6,[sp, #0x28]
ffff00009077eac4	stp x7,x8,[sp, #0x38]
ffff00009077eac8	stp x8,x9,[x29, #-0x40]
ffff00009077eacc	stp xzr,x10,[x29, #-0x30]
ffff00009077ead0	stp x9,xzr,[sp, #0x48]
ffff00009077ead4	str x10,[sp, #0x58]
ffff00009077ead8	bl 0xffff0000907c35c8
ffff00009077eadc	ldp x8,x9,[sp, #0x40]
ffff00009077eae0	nop
ffff00009077eae4	adr x0,-0xffff6f881650
ffff00009077eae8	ldp x10,x11,[sp, #0x50]
ffff00009077eaec	sub x3,x29,#0x20
ffff00009077eaf0	mov x1,x20
ffff00009077eaf4	mov x2,x19
ffff00009077eaf8	stp x8,x9,[x29, #-0x20]
ffff00009077eafc	stp x10,x11,[x29, #-0x10]
ffff00009077eb00	bl 0xffff000090780934
ffff00009077eb04	mov w19,w0
ffff00009077eb08	ldr x0,[x20]
ffff00009077eb0c	bl 0xffff0000907c35a8
ffff00009077eb10	ldr x0,[x20]
ffff00009077eb14	bl 0xffff0000907c35e8
ffff00009077eb18	mov w0,w19
ffff00009077eb1c	ldp x20,x19,[sp, #0xb0]
ffff00009077eb20	ldp x29,x30,[sp, #0xa0]
ffff00009077eb24	add sp,sp,#0xc0
ffff00009077eb28	autiasp
ffff00009077eb2c	ret
