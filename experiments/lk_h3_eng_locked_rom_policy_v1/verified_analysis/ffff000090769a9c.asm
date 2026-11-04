ffff000090769a9c	paciasp
ffff000090769aa0	stp x29,x30,[sp, #-0x50]!
ffff000090769aa4	str x25,[sp, #0x10]
ffff000090769aa8	stp x24,x23,[sp, #0x20]
ffff000090769aac	stp x22,x21,[sp, #0x30]
ffff000090769ab0	stp x20,x19,[sp, #0x40]
ffff000090769ab4	mov x29,sp
ffff000090769ab8	cbz x0,0xffff000090769ba0
ffff000090769abc	ldr x19,[x0, #0x30]
ffff000090769ac0	adrp x20,-0xffff6f832000
ffff000090769ac4	add x20,x20,#0xe40
ffff000090769ac8	mov x22,x0
ffff000090769acc	mov x0,x20
ffff000090769ad0	mov w1,wzr
ffff000090769ad4	mov x2,x19
ffff000090769ad8	bl 0xffff00009077ea84
ffff000090769adc	ldp x8,x2,[x22, #0x30]
ffff000090769ae0	mov w23,#0xffffffff
ffff000090769ae4	mov x0,x20
ffff000090769ae8	mov w1,#0x1
ffff000090769aec	cmp x8,x19
ffff000090769af0	ccmp x8,x23,#0x4,cc
ffff000090769af4	csel x19,x8,x19,ne
ffff000090769af8	bl 0xffff00009077ea84
ffff000090769afc	mov x24,xzr
ffff000090769b00	ldr x21,[x22, #0x38]
ffff000090769b04	add x22,x22,#0x40
ffff000090769b08	lsl x25,x24,#0x3
ffff000090769b0c	add w1,w24,#0x2
ffff000090769b10	mov x0,x20
ffff000090769b14	ldr x2,[x22, x25, LSL #0x0]
ffff000090769b18	bl 0xffff00009077ea84
ffff000090769b1c	ldr x8,[x22, x25, LSL #0x0]
ffff000090769b20	add x24,x24,#0x1
ffff000090769b24	cmp x8,x19
ffff000090769b28	ccmp x8,x23,#0x4,cc
ffff000090769b2c	csel x19,x8,x19,ne
ffff000090769b30	cmp x24,#0x1e
ffff000090769b34	b.ne 0xffff000090769b08
ffff000090769b38	cmp x21,#0x3f
ffff000090769b3c	b.hi 0xffff000090769b60
ffff000090769b40	mov w0,#0x3
ffff000090769b44	mov w1,w21
ffff000090769b48	bl 0xffff000090788874
ffff000090769b4c	cbz w0,0xffff000090769b60
ffff000090769b50	mov w1,w0
ffff000090769b54	adrp x0,-0xffff6f82a000
ffff000090769b58	add x0,x0,#0xfc8
ffff000090769b5c	bl 0xffff00009077ea84
ffff000090769b60	cmp x19,#0x3f
ffff000090769b64	b.hi 0xffff000090769ba0
ffff000090769b68	mov w0,#0x2
ffff000090769b6c	mov w1,w19
ffff000090769b70	bl 0xffff000090788874
ffff000090769b74	cbz w0,0xffff000090769ba0
ffff000090769b78	ldp x20,x19,[sp, #0x40]
ffff000090769b7c	mov w1,w0
ffff000090769b80	adrp x0,-0xffff6f81e000
ffff000090769b84	add x0,x0,#0x793
ffff000090769b88	ldr x25,[sp, #0x10]
ffff000090769b8c	ldp x22,x21,[sp, #0x30]
ffff000090769b90	ldp x24,x23,[sp, #0x20]
ffff000090769b94	ldp x29,x30,[sp], #0x50
ffff000090769b98	autiasp
ffff000090769b9c	b 0xffff00009077ea84
ffff000090769ba0	ldp x20,x19,[sp, #0x40]
ffff000090769ba4	ldp x22,x21,[sp, #0x30]
ffff000090769ba8	ldp x24,x23,[sp, #0x20]
ffff000090769bac	ldr x25,[sp, #0x10]
ffff000090769bb0	ldp x29,x30,[sp], #0x50
ffff000090769bb4	autiasp
ffff000090769bb8	ret
