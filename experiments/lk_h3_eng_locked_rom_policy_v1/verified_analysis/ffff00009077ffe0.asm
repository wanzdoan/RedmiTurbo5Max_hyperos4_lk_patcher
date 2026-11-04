ffff00009077ffe0	paciasp
ffff00009077ffe4	sub sp,sp,#0x80
ffff00009077ffe8	stp x29,x30,[sp, #0x70]
ffff00009077ffec	add x29,sp,#0x70
ffff00009077fff0	add x9,sp,#0x8
ffff00009077fff4	stp x3,x4,[sp, #0x8]
ffff00009077fff8	add x8,x29,#0x10
ffff00009077fffc	add x9,x9,#0x28
ffff000090780000	mov w10,#0xffffffd8
ffff000090780004	add x3,sp,#0x30
ffff000090780008	stp x5,x6,[sp, #0x18]
ffff00009078000c	stp x7,x8,[sp, #0x28]
ffff000090780010	stp x8,x9,[x29, #-0x20]
ffff000090780014	stp xzr,x10,[x29, #-0x10]
ffff000090780018	stp x9,xzr,[sp, #0x38]
ffff00009078001c	str x10,[sp, #0x48]
ffff000090780020	bl 0xffff000090780034
ffff000090780024	ldp x29,x30,[sp, #0x70]
ffff000090780028	add sp,sp,#0x80
ffff00009078002c	autiasp
ffff000090780030	ret
