002a8ba0	sub sp,sp,#0x40
002a8ba4	stp x29,x30,[sp, #0x20]
002a8ba8	str x19,[sp, #0x30]
002a8bac	add x29,sp,#0x20
002a8bb0	mrs x19,tpidr_el0
002a8bb4	ldr x8,[x19, #0x28]
002a8bb8	stur x8,[x29, #-0x8]
002a8bbc	stp x0,x1,[sp, #0x8]
002a8bc0	nop
002a8bc4	adr x0,0x2a8c00
002a8bc8	add x1,sp,#0x8
002a8bcc	bl 0x004209f0
002a8bd0	ldr x8,[x19, #0x28]
002a8bd4	ldur x9,[x29, #-0x8]
002a8bd8	cmp x8,x9
002a8bdc	b.ne 0x002a8bf0
002a8be0	ldp x29,x30,[sp, #0x20]
002a8be4	ldr x19,[sp, #0x30]
002a8be8	add sp,sp,#0x40
002a8bec	ret
002a8bf0	bl 0x004217e0
