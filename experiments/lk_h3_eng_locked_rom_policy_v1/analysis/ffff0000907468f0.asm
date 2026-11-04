ffff0000907468f0	paciasp
ffff0000907468f4	sub sp,sp,#0x1b0
ffff0000907468f8	stp x29,x30,[sp, #0x180]
ffff0000907468fc	stp x28,x21,[sp, #0x190]
ffff000090746900	stp x20,x19,[sp, #0x1a0]
ffff000090746904	add x29,sp,#0x180
ffff000090746908	adrp x20,-0xffff6f789000
ffff00009074690c	stp x1,x2,[sp, #0x8]
ffff000090746910	stp x3,x4,[sp, #0x18]
ffff000090746914	stp x5,x6,[sp, #0x28]
ffff000090746918	ldr x9,[x20, #0x4f8]
ffff00009074691c	str x7,[sp, #0x38]
ffff000090746920	cbz x9,0xffff000090746a0c
ffff000090746924	mov x8,x0
ffff000090746928	stp xzr,xzr,[x29, #-0x10]
ffff00009074692c	add x10,sp,#0x8
ffff000090746930	mov w9,#0xffffffc8
ffff000090746934	add x10,x10,#0x38
ffff000090746938	add x11,x29,#0x30
ffff00009074693c	ldur x12,[x29, #-0x10]
ffff000090746940	add x0,sp,#0x60
ffff000090746944	add x2,sp,#0x40
ffff000090746948	mov x1,x8
ffff00009074694c	stp xzr,xzr,[x29, #-0x20]
ffff000090746950	stp xzr,xzr,[sp, #0x60]
ffff000090746954	stp xzr,xzr,[sp, #0x70]
ffff000090746958	stp xzr,xzr,[sp, #0x80]
ffff00009074695c	stp xzr,xzr,[sp, #0x90]
ffff000090746960	stp xzr,xzr,[sp, #0xa0]
ffff000090746964	stp xzr,xzr,[sp, #0xb0]
ffff000090746968	stp xzr,xzr,[sp, #0xc0]
ffff00009074696c	stp xzr,xzr,[sp, #0xd0]
ffff000090746970	stp xzr,xzr,[sp, #0xe0]
ffff000090746974	stp xzr,xzr,[sp, #0xf0]
ffff000090746978	stp xzr,xzr,[sp, #0x100]
ffff00009074697c	stp xzr,xzr,[sp, #0x110]
ffff000090746980	stp xzr,xzr,[sp, #0x120]
ffff000090746984	stp xzr,xzr,[sp, #0x130]
ffff000090746988	stp xzr,xzr,[sp, #0x140]
ffff00009074698c	stp xzr,xzr,[sp, #0x150]
ffff000090746990	stur x9,[x29, #-0x8]
ffff000090746994	stp x11,x10,[x29, #-0x20]
ffff000090746998	stp x11,x10,[sp, #0x40]
ffff00009074699c	stp x12,x9,[sp, #0x50]
ffff0000907469a0	bl 0xffff00009077ff9c
ffff0000907469a4	tbnz w0,#0x1f,0xffff0000907469dc
ffff0000907469a8	add x0,sp,#0x60
ffff0000907469ac	bl 0xffff00009077fb34
ffff0000907469b0	mov x19,x0
ffff0000907469b4	cbz w19,0xffff0000907469cc
ffff0000907469b8	add x21,sp,#0x60
ffff0000907469bc	ldrb w0,[x21], #0x1
ffff0000907469c0	sub w19,w19,#0x1
ffff0000907469c4	bl 0xffff000090745bc0
ffff0000907469c8	cbnz w19,0xffff0000907469bc
ffff0000907469cc	ldr x8,[x20, #0x4f8]
ffff0000907469d0	cbz x8,0xffff0000907469f8
ffff0000907469d4	ldp w2,w3,[x8, #0x14]
ffff0000907469d8	b 0xffff000090746a00
ffff0000907469dc	adrp x0,-0xffff6f807000
ffff0000907469e0	add x0,x0,#0xd28
ffff0000907469e4	adrp x1,-0xffff6f831000
ffff0000907469e8	add x1,x1,#0xffe
ffff0000907469ec	mov w2,#0x25b
ffff0000907469f0	bl 0xffff00009077ea84
ffff0000907469f4	b 0xffff000090746a0c
ffff0000907469f8	mov w2,wzr
ffff0000907469fc	mov w3,wzr
ffff000090746a00	mov w0,wzr
ffff000090746a04	mov w1,wzr
ffff000090746a08	bl 0xffff00009075d714
ffff000090746a0c	ldp x20,x19,[sp, #0x1a0]
ffff000090746a10	ldp x28,x21,[sp, #0x190]
ffff000090746a14	ldp x29,x30,[sp, #0x180]
ffff000090746a18	add sp,sp,#0x1b0
ffff000090746a1c	autiasp
ffff000090746a20	ret
