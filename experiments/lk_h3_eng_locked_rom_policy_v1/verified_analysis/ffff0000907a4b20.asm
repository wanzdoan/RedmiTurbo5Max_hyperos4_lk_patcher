ffff0000907a4b20	paciasp
ffff0000907a4b24	sub sp,sp,#0x110
ffff0000907a4b28	stp x29,x30,[sp, #0xe0]
ffff0000907a4b2c	stp x28,x21,[sp, #0xf0]
ffff0000907a4b30	stp x20,x19,[sp, #0x100]
ffff0000907a4b34	add x29,sp,#0xe0
ffff0000907a4b38	mov x19,x2
ffff0000907a4b3c	mov x20,x1
ffff0000907a4b40	mov x21,x0
ffff0000907a4b44	mov x0,sp
ffff0000907a4b48	mov w1,wzr
ffff0000907a4b4c	mov w2,#0xe0
ffff0000907a4b50	stp xzr,xzr,[sp, #0xd0]
ffff0000907a4b54	stp xzr,xzr,[sp, #0xc0]
ffff0000907a4b58	stp xzr,xzr,[sp, #0xb0]
ffff0000907a4b5c	stp xzr,xzr,[sp, #0xa0]
ffff0000907a4b60	stp xzr,xzr,[sp, #0x90]
ffff0000907a4b64	stp xzr,xzr,[sp, #0x80]
ffff0000907a4b68	stp xzr,xzr,[sp, #0x70]
ffff0000907a4b6c	stp xzr,xzr,[sp, #0x60]
ffff0000907a4b70	stp xzr,xzr,[sp, #0x50]
ffff0000907a4b74	stp xzr,xzr,[sp, #0x40]
ffff0000907a4b78	stp xzr,xzr,[sp, #0x30]
ffff0000907a4b7c	stp xzr,xzr,[sp, #0x20]
ffff0000907a4b80	stp xzr,xzr,[sp, #0x10]
ffff0000907a4b84	stp xzr,xzr,[sp]
ffff0000907a4b88	bl 0xffff00009077f7b8
ffff0000907a4b8c	mov w8,#0xe667
ffff0000907a4b90	mov w9,#0xae85
ffff0000907a4b94	movk w8,#0x6a09, LSL #16
ffff0000907a4b98	mov w10,#0xf372
ffff0000907a4b9c	mov w11,#0xf53a
ffff0000907a4ba0	movk w9,#0xbb67, LSL #16
ffff0000907a4ba4	movk w10,#0x3c6e, LSL #16
ffff0000907a4ba8	movk w11,#0xa54f, LSL #16
ffff0000907a4bac	stp xzr,x8,[sp]
ffff0000907a4bb0	mov w8,#0x527f
ffff0000907a4bb4	movk w8,#0x510e, LSL #16
ffff0000907a4bb8	mov x0,sp
ffff0000907a4bbc	stp x9,x10,[sp, #0x10]
ffff0000907a4bc0	mov w9,#0x688c
ffff0000907a4bc4	mov w10,#0xd9ab
ffff0000907a4bc8	movk w9,#0x9b05, LSL #16
ffff0000907a4bcc	stp x11,x8,[sp, #0x20]
ffff0000907a4bd0	mov w8,#0xcd19
ffff0000907a4bd4	movk w10,#0x1f83, LSL #16
ffff0000907a4bd8	movk w8,#0x5be0, LSL #16
ffff0000907a4bdc	mov x1,x21
ffff0000907a4be0	mov x2,x20
ffff0000907a4be4	stp x9,x10,[sp, #0x30]
ffff0000907a4be8	stp x8,xzr,[sp, #0x40]
ffff0000907a4bec	bl 0xffff0000907a4720
ffff0000907a4bf0	cbnz w0,0xffff0000907a4c00
ffff0000907a4bf4	mov x0,sp
ffff0000907a4bf8	mov x1,x19
ffff0000907a4bfc	bl 0xffff0000907a482c
ffff0000907a4c00	ldp x20,x19,[sp, #0x100]
ffff0000907a4c04	ldp x28,x21,[sp, #0xf0]
ffff0000907a4c08	ldp x29,x30,[sp, #0xe0]
ffff0000907a4c0c	add sp,sp,#0x110
ffff0000907a4c10	autiasp
ffff0000907a4c14	ret
