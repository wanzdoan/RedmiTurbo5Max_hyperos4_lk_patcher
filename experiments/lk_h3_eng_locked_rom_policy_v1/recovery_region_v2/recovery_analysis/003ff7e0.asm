003ff7e0	adrp x8,0x57d000
003ff7e4	add x8,x8,#0x718
003ff7e8	ldaprb w8,[x8]
003ff7ec	tbz w8,#0x0,0x003ff7fc
003ff7f0	adrp x0,0x57d000
003ff7f4	add x0,x0,#0x658
003ff7f8	ret
003ff7fc	stp x29,x30,[sp, #-0x20]!
003ff800	str x19,[sp, #0x10]
003ff804	mov x29,sp
003ff808	adrp x0,0x57d000
003ff80c	add x0,x0,#0x718
003ff810	bl 0x0041fc90
003ff814	cbz w0,0x003ff84c
003ff818	adrp x19,0x57d000
003ff81c	add x19,x19,#0x658
003ff820	mov x0,x19
003ff824	bl 0x003ff940
003ff828	nop
003ff82c	adr x0,0x3ff860
003ff830	nop
003ff834	adr x2,0x42ce00
003ff838	mov x1,x19
003ff83c	bl 0x0041f690
003ff840	adrp x0,0x57d000
003ff844	add x0,x0,#0x718
003ff848	bl 0x0041fcb0
003ff84c	ldr x19,[sp, #0x10]
003ff850	ldp x29,x30,[sp], #0x20
003ff854	b 0x003ff7f0
