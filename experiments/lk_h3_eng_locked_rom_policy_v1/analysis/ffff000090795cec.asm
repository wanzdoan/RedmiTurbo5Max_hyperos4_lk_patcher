ffff000090795cec	paciasp
ffff000090795cf0	sub sp,sp,#0xa0
ffff000090795cf4	stp x29,x30,[sp, #0x40]
ffff000090795cf8	stp x28,x27,[sp, #0x50]
ffff000090795cfc	stp x26,x25,[sp, #0x60]
ffff000090795d00	stp x24,x23,[sp, #0x70]
ffff000090795d04	stp x22,x21,[sp, #0x80]
ffff000090795d08	stp x20,x19,[sp, #0x90]
ffff000090795d0c	add x29,sp,#0x40
ffff000090795d10	mov w24,w0
ffff000090795d14	adrp x8,-0xffff6f6f6000
ffff000090795d18	add x8,x8,#0x4d0
ffff000090795d1c	mov w0,wzr
ffff000090795d20	mov x21,x4
ffff000090795d24	mov w25,w3
ffff000090795d28	mov x22,x2
ffff000090795d2c	mov w23,w1
ffff000090795d30	str x8,[sp, #0x38]
ffff000090795d34	bl 0xffff000090792dbc
ffff000090795d38	tst w25,#0xff
ffff000090795d3c	b.eq 0xffff000090795d5c
ffff000090795d40	adrp x0,-0xffff6f810000
ffff000090795d44	add x0,x0,#0x188
ffff000090795d48	adrp x1,-0xffff6f835000
ffff000090795d4c	add x1,x1,#0xb3e
ffff000090795d50	bl 0xffff00009077ea84
ffff000090795d54	mov w27,#0xfffffffa
ffff000090795d58	b 0xffff000090795fc4
ffff000090795d5c	add x20,x0,#0x270
ffff000090795d60	mov x19,x0
ffff000090795d64	mov x0,x20
ffff000090795d68	mov w1,#0xffffffff
ffff000090795d6c	stp xzr,xzr,[sp, #0x28]
ffff000090795d70	stp xzr,xzr,[sp, #0x18]
ffff000090795d74	str xzr,[sp, #0x10]
ffff000090795d78	str wzr,[sp, #0xc]
ffff000090795d7c	bl 0xffff000090716f9c
ffff000090795d80	add x1,sp,#0xc
ffff000090795d84	mov x0,x19
ffff000090795d88	bl 0xffff00009078fc78
ffff000090795d8c	tbz w0,#0x0,0xffff000090795da4
ffff000090795d90	bl 0xffff0000907953e0
ffff000090795d94	cmp w25,#0x100
ffff000090795d98	b.cs 0xffff000090795dac
ffff000090795d9c	mov w27,wzr
ffff000090795da0	b 0xffff000090795fb0
ffff000090795da4	mov w27,#0xfffffffa
ffff000090795da8	b 0xffff000090795fbc
ffff000090795dac	lsr w9,w25,#0x8
ffff000090795db0	str x20,[sp]
ffff000090795db4	add x8,sp,#0x10
ffff000090795db8	cmp w9,#0x1
ffff000090795dbc	mov x20,#0x1
ffff000090795dc0	add x25,x8,#0xd
ffff000090795dc4	csinc w28,w9,wzr,hi
ffff000090795dc8	movk x20,#0x200, LSL #32
ffff000090795dcc	adrp x26,-0xffff6f6f6000
ffff000090795dd0	add x26,x26,#0x700
ffff000090795dd4	mov x0,x25
ffff000090795dd8	mov w1,wzr
ffff000090795ddc	mov w2,#0x10
ffff000090795de0	ldr w27,[sp, #0xc]
ffff000090795de4	bl 0xffff00009077f7b8
ffff000090795de8	mov w8,#0xc4
ffff000090795dec	strb wzr,[sp, #0x1c]
ffff000090795df0	strb w24,[sp, #0x1f]
ffff000090795df4	str x20,[sp, #0x20]
ffff000090795df8	stp w8,w27,[sp, #0x10]
ffff000090795dfc	mov w8,#0x1
ffff000090795e00	ldr x27,[sp, #0x38]
ffff000090795e04	strb wzr,[sp, #0x28]
ffff000090795e08	str w8,[sp, #0x18]
ffff000090795e0c	mov w8,#0x200
ffff000090795e10	str w8,[sp, #0x30]
ffff000090795e14	mov w8,#0xc
ffff000090795e18	strh w8,[sp, #0x2e]
ffff000090795e1c	mov w8,#0xecb5
ffff000090795e20	sturh w8,[sp, #0x1d]
ffff000090795e24	cbz x27,0xffff000090795f18
ffff000090795e28	mov x0,x27
ffff000090795e2c	mov w1,wzr
ffff000090795e30	mov w2,#0x200
ffff000090795e34	bl 0xffff00009077f7b8
ffff000090795e38	rev w8,w23
ffff000090795e3c	mov w9,#0x100
ffff000090795e40	lsr w8,w8,#0x10
ffff000090795e44	add x0,x27,#0x1e4
ffff000090795e48	mov x1,x26
ffff000090795e4c	mov w2,#0x10
ffff000090795e50	strh w9,[x27, #0x1fa]
ffff000090795e54	strh w8,[x27, #0x1f8]
ffff000090795e58	mov w8,#0x400
ffff000090795e5c	strh w8,[x27, #0x1fe]
ffff000090795e60	bl 0xffff00009077f488
ffff000090795e64	add x1,sp,#0x10
ffff000090795e68	mov x0,x19
ffff000090795e6c	bl 0xffff000090790d40
ffff000090795e70	cbnz w0,0xffff000090795f44
ffff000090795e74	mov x0,x25
ffff000090795e78	mov w1,wzr
ffff000090795e7c	mov w2,#0x10
ffff000090795e80	ldr w27,[sp, #0xc]
ffff000090795e84	bl 0xffff00009077f7b8
ffff000090795e88	mov w8,#0xc4
ffff000090795e8c	add x1,sp,#0x10
ffff000090795e90	mov x0,x19
ffff000090795e94	strb wzr,[sp, #0x1c]
ffff000090795e98	strb w24,[sp, #0x1f]
ffff000090795e9c	stp w8,w27,[sp, #0x10]
ffff000090795ea0	mov w8,#0x2
ffff000090795ea4	str x20,[sp, #0x20]
ffff000090795ea8	strb wzr,[sp, #0x28]
ffff000090795eac	str w8,[sp, #0x18]
ffff000090795eb0	mov w8,#0x200
ffff000090795eb4	str w8,[sp, #0x30]
ffff000090795eb8	mov w8,#0xc
ffff000090795ebc	strh w8,[sp, #0x2e]
ffff000090795ec0	mov w8,#0xeca2
ffff000090795ec4	sturh w8,[sp, #0x1d]
ffff000090795ec8	bl 0xffff000090790d40
ffff000090795ecc	cbnz w0,0xffff000090795f54
ffff000090795ed0	cbz x21,0xffff000090795ee8
ffff000090795ed4	ldr x8,[sp, #0x38]
ffff000090795ed8	ldrh w8,[x8, #0x1fc]
ffff000090795edc	rev16 w2,w8
ffff000090795ee0	str w2,[x21]
ffff000090795ee4	cbnz w8,0xffff000090795f94
ffff000090795ee8	add x0,sp,#0x10
ffff000090795eec	mov x1,x26
ffff000090795ef0	mov x2,x22
ffff000090795ef4	mov w3,#0x400
ffff000090795ef8	bl 0xffff000090795fec
ffff000090795efc	cbnz w0,0xffff000090795f74
ffff000090795f00	add w23,w23,#0x1
ffff000090795f04	add x22,x22,#0x100
ffff000090795f08	subs x28,x28,#0x1
ffff000090795f0c	b.ne 0xffff000090795dd4
ffff000090795f10	mov w27,wzr
ffff000090795f14	b 0xffff000090795fac
ffff000090795f18	adrp x0,-0xffff6f82b000
ffff000090795f1c	add x0,x0,#0x90c
ffff000090795f20	adrp x1,-0xffff6f824000
ffff000090795f24	add x1,x1,#0x410
ffff000090795f28	bl 0xffff00009077ea84
ffff000090795f2c	adrp x0,-0xffff6f831000
ffff000090795f30	add x0,x0,#0x157
ffff000090795f34	adrp x1,-0xffff6f835000
ffff000090795f38	add x1,x1,#0xb3e
ffff000090795f3c	mov w2,#0xfffffffb
ffff000090795f40	b 0xffff000090795f68
ffff000090795f44	mov w2,w0
ffff000090795f48	adrp x0,-0xffff6f816000
ffff000090795f4c	add x0,x0,#0xd74
ffff000090795f50	b 0xffff000090795f60
ffff000090795f54	mov w2,w0
ffff000090795f58	adrp x0,-0xffff6f812000
ffff000090795f5c	add x0,x0,#0x43f
ffff000090795f60	adrp x1,-0xffff6f835000
ffff000090795f64	add x1,x1,#0xb3e
ffff000090795f68	bl 0xffff00009077ea84
ffff000090795f6c	mov w27,#0xfffffffa
ffff000090795f70	b 0xffff000090795fac
ffff000090795f74	mov w27,w0
ffff000090795f78	adrp x0,-0xffff6f821000
ffff000090795f7c	add x0,x0,#0x70d
ffff000090795f80	adrp x1,-0xffff6f835000
ffff000090795f84	add x1,x1,#0xb3e
ffff000090795f88	mov w2,w27
ffff000090795f8c	bl 0xffff00009077ea84
ffff000090795f90	b 0xffff000090795fac
ffff000090795f94	adrp x0,-0xffff6f810000
ffff000090795f98	add x0,x0,#0x1b3
ffff000090795f9c	adrp x1,-0xffff6f835000
ffff000090795fa0	add x1,x1,#0xb3e
ffff000090795fa4	bl 0xffff00009077ea84
ffff000090795fa8	mov w27,#0xfffffffe
ffff000090795fac	ldr x20,[sp]
ffff000090795fb0	ldr w1,[sp, #0xc]
ffff000090795fb4	mov x0,x19
ffff000090795fb8	bl 0xffff00009078fd5c
ffff000090795fbc	mov x0,x20
ffff000090795fc0	bl 0xffff000090717060
ffff000090795fc4	mov w0,w27
ffff000090795fc8	ldp x20,x19,[sp, #0x90]
ffff000090795fcc	ldp x22,x21,[sp, #0x80]
ffff000090795fd0	ldp x24,x23,[sp, #0x70]
ffff000090795fd4	ldp x26,x25,[sp, #0x60]
ffff000090795fd8	ldp x28,x27,[sp, #0x50]
ffff000090795fdc	ldp x29,x30,[sp, #0x40]
ffff000090795fe0	add sp,sp,#0xa0
ffff000090795fe4	autiasp
ffff000090795fe8	ret
