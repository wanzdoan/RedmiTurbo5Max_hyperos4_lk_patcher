ffff0000907be700	paciasp
ffff0000907be704	sub sp,sp,#0x1d0
ffff0000907be708	stp x29,x30,[sp, #0x170]
ffff0000907be70c	stp x28,x27,[sp, #0x180]
ffff0000907be710	stp x26,x25,[sp, #0x190]
ffff0000907be714	stp x24,x23,[sp, #0x1a0]
ffff0000907be718	stp x22,x21,[sp, #0x1b0]
ffff0000907be71c	stp x20,x19,[sp, #0x1c0]
ffff0000907be720	add x29,sp,#0x170
ffff0000907be724	mov x20,x5
ffff0000907be728	mov w24,w4
ffff0000907be72c	mov w25,w3
ffff0000907be730	mov x21,x2
ffff0000907be734	mov x27,x1
ffff0000907be738	mov x22,x0
ffff0000907be73c	stur wzr,[x29, #-0xc]
ffff0000907be740	stp xzr,xzr,[sp, #0x50]
ffff0000907be744	cbz x5,0xffff0000907be74c
ffff0000907be748	str xzr,[x20]
ffff0000907be74c	cmp w24,#0x3
ffff0000907be750	b.ne 0xffff0000907be758
ffff0000907be754	tbz w25,#0x0,0xffff0000907be988
ffff0000907be758	cmp w24,#0x4
ffff0000907be75c	b.ne 0xffff0000907be770
ffff0000907be760	ldr x8,[x22, #0x60]
ffff0000907be764	cbz x8,0xffff0000907be8f0
ffff0000907be768	ldr x8,[x22, #0x68]
ffff0000907be76c	cbz x8,0xffff0000907be8f0
ffff0000907be770	tbz w25,#0x2,0xffff0000907be77c
ffff0000907be774	ldr x8,[x22, #0x70]
ffff0000907be778	cbz x8,0xffff0000907be954
ffff0000907be77c	mov w0,#0x138
ffff0000907be780	bl 0xffff0000907c159c
ffff0000907be784	cbz x0,0xffff0000907be94c
ffff0000907be788	mov x19,x0
ffff0000907be78c	add x8,sp,#0x60
ffff0000907be790	mov w0,#0x400
ffff0000907be794	stp xzr,xzr,[sp, #0xc0]
ffff0000907be798	stp xzr,xzr,[sp, #0xb0]
ffff0000907be79c	stp xzr,xzr,[x8, #0xf0]
ffff0000907be7a0	stp xzr,xzr,[x8, #0xe0]
ffff0000907be7a4	stp xzr,xzr,[x8, #0xd0]
ffff0000907be7a8	stp xzr,xzr,[x8, #0xc0]
ffff0000907be7ac	stp xzr,xzr,[x8, #0xb0]
ffff0000907be7b0	stp xzr,xzr,[x8, #0xa0]
ffff0000907be7b4	stp xzr,xzr,[x8, #0x90]
ffff0000907be7b8	stp xzr,xzr,[x8, #0x80]
ffff0000907be7bc	stp xzr,xzr,[x8, #0x70]
ffff0000907be7c0	stp xzr,xzr,[sp, #0xa0]
ffff0000907be7c4	stp xzr,xzr,[sp, #0x90]
ffff0000907be7c8	stp xzr,xzr,[sp, #0x80]
ffff0000907be7cc	stp xzr,xzr,[sp, #0x70]
ffff0000907be7d0	stp xzr,xzr,[sp, #0x60]
ffff0000907be7d4	bl 0xffff0000907c159c
ffff0000907be7d8	str x0,[x19, #0x8]
ffff0000907be7dc	cbz x0,0xffff0000907be920
ffff0000907be7e0	mov w0,#0x400
ffff0000907be7e4	bl 0xffff0000907c159c
ffff0000907be7e8	str x0,[x19, #0x18]
ffff0000907be7ec	cbz x0,0xffff0000907be920
ffff0000907be7f0	mov w8,#0xffffffff
ffff0000907be7f4	stp x8,x8,[x19, #0x30]
ffff0000907be7f8	stp x8,x8,[x19, #0x40]
ffff0000907be7fc	stp x8,x8,[x19, #0x50]
ffff0000907be800	stp x8,x8,[x19, #0x60]
ffff0000907be804	stp x8,x8,[x19, #0x70]
ffff0000907be808	stp x8,x8,[x19, #0x80]
ffff0000907be80c	stp x8,x8,[x19, #0x90]
ffff0000907be810	stp x8,x8,[x19, #0xa0]
ffff0000907be814	stp x8,x8,[x19, #0xb0]
ffff0000907be818	stp x8,x8,[x19, #0xc0]
ffff0000907be81c	stp x8,x8,[x19, #0xd0]
ffff0000907be820	stp x8,x8,[x19, #0xe0]
ffff0000907be824	stp x8,x8,[x19, #0xf0]
ffff0000907be828	stp x8,x8,[x19, #0x100]
ffff0000907be82c	stp x8,x8,[x19, #0x110]
ffff0000907be830	stp x8,x8,[x19, #0x120]
ffff0000907be834	bl 0xffff0000907bcc58
ffff0000907be838	mov x23,x0
ffff0000907be83c	cbz x0,0xffff0000907be924
ffff0000907be840	str x23,[sp, #0x40]
ffff0000907be844	tbnz w25,#0x2,0xffff0000907be9b4
ffff0000907be848	adrp x28,-0xffff6f82e000
ffff0000907be84c	add x28,x28,#0x919
ffff0000907be850	mov x0,x28
ffff0000907be854	and w26,w25,#0x1
ffff0000907be858	bl 0xffff0000907c0e98
ffff0000907be85c	mov x8,x0
ffff0000907be860	mov w5,wzr
ffff0000907be864	mov w6,wzr
ffff0000907be868	add x9,sp,#0x50
ffff0000907be86c	add x10,sp,#0x58
ffff0000907be870	sub x11,x29,#0xc
ffff0000907be874	mov x0,x22
ffff0000907be878	mov x1,x27
ffff0000907be87c	mov x2,x21
ffff0000907be880	mov w3,w25
ffff0000907be884	mov w4,w26
ffff0000907be888	mov x7,x28
ffff0000907be88c	stp x9,x23,[sp, #0x30]
ffff0000907be890	stp x11,x10,[sp, #0x20]
ffff0000907be894	stp x8,xzr,[sp]
ffff0000907be898	stp xzr,x19,[sp, #0x10]
ffff0000907be89c	bl 0xffff0000907beba0
ffff0000907be8a0	mov w26,w0
ffff0000907be8a4	tbnz w25,#0x0,0xffff0000907be8ac
ffff0000907be8a8	cbnz w26,0xffff0000907bea28
ffff0000907be8ac	cmp w26,#0x5
ffff0000907be8b0	b.hi 0xffff0000907bea28
ffff0000907be8b4	mov w8,#0x1
ffff0000907be8b8	ldr x23,[sp, #0x40]
ffff0000907be8bc	lsl w8,w8,w26
ffff0000907be8c0	mov w9,#0x39
ffff0000907be8c4	tst w8,w9
ffff0000907be8c8	b.eq 0xffff0000907be928
ffff0000907be8cc	tbnz w25,#0x2,0xffff0000907bea30
ffff0000907be8d0	ldr x8,[x19, #0x8]
ffff0000907be8d4	adrp x1,-0xffff6f82e000
ffff0000907be8d8	add x1,x1,#0x919
ffff0000907be8dc	ldr x0,[x8]
ffff0000907be8e0	bl 0xffff0000907c0e80
ffff0000907be8e4	cmp w0,#0x0
ffff0000907be8e8	cset w27,ne
ffff0000907be8ec	b 0xffff0000907bea34
ffff0000907be8f0	adrp x0,-0xffff6f806000
ffff0000907be8f4	add x0,x0,#0xb02
ffff0000907be8f8	bl 0xffff0000907c119c
ffff0000907be8fc	adrp x1,-0xffff6f81c000
ffff0000907be900	add x1,x1,#0xabf
ffff0000907be904	nop
ffff0000907be908	adr x2,-0xffff6f837858
ffff0000907be90c	adrp x3,-0xffff6f828000
ffff0000907be910	add x3,x3,#0xdec
ffff0000907be914	adrp x4,-0xffff6f806000
ffff0000907be918	add x4,x4,#0xb28
ffff0000907be91c	b 0xffff0000907be980
ffff0000907be920	mov x23,xzr
ffff0000907be924	mov w26,#0x1
ffff0000907be928	mov x0,x19
ffff0000907be92c	bl 0xffff0000907bff28
ffff0000907be930	ldr x0,[sp, #0x58]
ffff0000907be934	cbz x0,0xffff0000907be93c
ffff0000907be938	bl 0xffff0000907c0fd4
ffff0000907be93c	cbz x23,0xffff0000907be98c
ffff0000907be940	mov x0,x23
ffff0000907be944	bl 0xffff0000907bcc68
ffff0000907be948	b 0xffff0000907be98c
ffff0000907be94c	mov w26,#0x1
ffff0000907be950	b 0xffff0000907be98c
ffff0000907be954	adrp x0,-0xffff6f806000
ffff0000907be958	add x0,x0,#0xb02
ffff0000907be95c	bl 0xffff0000907c119c
ffff0000907be960	adrp x1,-0xffff6f81c000
ffff0000907be964	add x1,x1,#0xabf
ffff0000907be968	adrp x2,-0xffff6f833000
ffff0000907be96c	add x2,x2,#0x71b
ffff0000907be970	adrp x3,-0xffff6f828000
ffff0000907be974	add x3,x3,#0xdec
ffff0000907be978	adrp x4,-0xffff6f829000
ffff0000907be97c	add x4,x4,#0x6ca
ffff0000907be980	mov x5,xzr
ffff0000907be984	bl 0xffff0000907c0ea8
ffff0000907be988	mov w26,#0x8
ffff0000907be98c	mov w0,w26
ffff0000907be990	ldp x20,x19,[sp, #0x1c0]
ffff0000907be994	ldp x22,x21,[sp, #0x1b0]
ffff0000907be998	ldp x24,x23,[sp, #0x1a0]
ffff0000907be99c	ldp x26,x25,[sp, #0x190]
ffff0000907be9a0	ldp x28,x27,[sp, #0x180]
ffff0000907be9a4	ldp x29,x30,[sp, #0x170]
ffff0000907be9a8	add sp,sp,#0x1d0
ffff0000907be9ac	autiasp
ffff0000907be9b0	ret
ffff0000907be9b4	cbz x27,0xffff0000907beb68
ffff0000907be9b8	ldr x28,[x27]
ffff0000907be9bc	cbz x28,0xffff0000907beb68
ffff0000907be9c0	mov w23,#0x8
ffff0000907be9c4	b 0xffff0000907be9d4
ffff0000907be9c8	ldr x28,[x27, x23, LSL #0x0]
ffff0000907be9cc	add x23,x23,#0x8
ffff0000907be9d0	cbz x28,0xffff0000907be8ac
ffff0000907be9d4	mov x0,x28
ffff0000907be9d8	and w26,w25,#0x1
ffff0000907be9dc	bl 0xffff0000907c0e98
ffff0000907be9e0	ldr x8,[sp, #0x40]
ffff0000907be9e4	mov w5,wzr
ffff0000907be9e8	mov w6,wzr
ffff0000907be9ec	stp x0,xzr,[sp]
ffff0000907be9f0	mov x0,x22
ffff0000907be9f4	mov x1,x27
ffff0000907be9f8	stp xzr,x8,[sp, #0x30]
ffff0000907be9fc	mov x2,x21
ffff0000907bea00	mov w3,w25
ffff0000907bea04	mov w4,w26
ffff0000907bea08	mov x7,x28
ffff0000907bea0c	sub x8,x29,#0xc
ffff0000907bea10	stp xzr,x19,[sp, #0x10]
ffff0000907bea14	stp x8,xzr,[sp, #0x20]
ffff0000907bea18	bl 0xffff0000907beba0
ffff0000907bea1c	mov w26,w0
ffff0000907bea20	tbnz w25,#0x0,0xffff0000907be9c8
ffff0000907bea24	cbz w26,0xffff0000907be9c8
ffff0000907bea28	ldr x23,[sp, #0x40]
ffff0000907bea2c	b 0xffff0000907be928
ffff0000907bea30	mov w27,wzr
ffff0000907bea34	ldr x8,[x19, #0x8]
ffff0000907bea38	add x1,sp,#0x60
ffff0000907bea3c	ldr x0,[x8, #0x8]
ffff0000907bea40	bl 0xffff0000907c268c
ffff0000907bea44	mov x0,x21
ffff0000907bea48	bl 0xffff0000907c1650
ffff0000907bea4c	str x0,[x19]
ffff0000907bea50	cbz x0,0xffff0000907be924
ffff0000907bea54	ldrb w8,[sp, #0xd8]
ffff0000907bea58	tbnz w8,#0x1,0xffff0000907bea90
ffff0000907bea5c	mov w8,w24
ffff0000907bea60	cmp w24,#0x4
ffff0000907bea64	str w24,[sp, #0x4c]
ffff0000907bea68	b.ne 0xffff0000907beac8
ffff0000907bea6c	add x2,sp,#0x4c
ffff0000907bea70	mov x0,x22
ffff0000907bea74	mov x1,x19
ffff0000907bea78	bl 0xffff0000907bfc08
ffff0000907bea7c	cbz w0,0xffff0000907beac4
ffff0000907bea80	cmp w0,#0x1
ffff0000907bea84	mov w8,#0x1
ffff0000907bea88	cinc w26,w8,ne
ffff0000907bea8c	b 0xffff0000907be928
ffff0000907bea90	mov x0,x22
ffff0000907bea94	mov x1,x21
ffff0000907bea98	bl 0xffff0000907bfaec
ffff0000907bea9c	adrp x8,-0xffff6f829000
ffff0000907beaa0	add x8,x8,#0x88e
ffff0000907beaa4	adrp x9,-0xffff6f828000
ffff0000907beaa8	add x9,x9,#0xe3f
ffff0000907beaac	tst w0,#0x1
ffff0000907beab0	csel x0,x9,x8,ne
ffff0000907beab4	bl 0xffff0000907c1650
ffff0000907beab8	str x0,[x19, #0x28]
ffff0000907beabc	cbnz x0,0xffff0000907beb04
ffff0000907beac0	b 0xffff0000907be924
ffff0000907beac4	ldr w8,[sp, #0x4c]
ffff0000907beac8	ldp x5,x4,[sp, #0x50]
ffff0000907beacc	add x3,sp,#0x60
ffff0000907bead0	mov x0,x22
ffff0000907bead4	ldur w6,[x29, #-0xc]
ffff0000907bead8	mov w1,w25
ffff0000907beadc	mov x2,x19
ffff0000907beae0	mov w7,w24
ffff0000907beae4	str w8,[x19, #0x130]
ffff0000907beae8	str w8,[sp]
ffff0000907beaec	bl 0xffff0000907bc594
ffff0000907beaf0	cbz w0,0xffff0000907beafc
ffff0000907beaf4	mov w26,w0
ffff0000907beaf8	b 0xffff0000907be928
ffff0000907beafc	ldr x0,[x19, #0x28]
ffff0000907beb00	cbz x0,0xffff0000907beb44
ffff0000907beb04	bl 0xffff0000907c0e98
ffff0000907beb08	cbz x0,0xffff0000907beb44
ffff0000907beb0c	ldr x1,[x19, #0x28]
ffff0000907beb10	mov x0,x22
ffff0000907beb14	mov x2,x21
ffff0000907beb18	mov w3,w27
ffff0000907beb1c	mov x4,x23
ffff0000907beb20	bl 0xffff0000907bc208
ffff0000907beb24	ldr x8,[x19, #0x28]
ffff0000907beb28	cmp x0,x8
ffff0000907beb2c	b.eq 0xffff0000907beb44
ffff0000907beb30	mov x21,x0
ffff0000907beb34	cbz x0,0xffff0000907be924
ffff0000907beb38	mov x0,x8
ffff0000907beb3c	bl 0xffff0000907c0fd4
ffff0000907beb40	str x21,[x19, #0x28]
ffff0000907beb44	cbz x20,0xffff0000907beb50
ffff0000907beb48	str x19,[x20]
ffff0000907beb4c	b 0xffff0000907beb58
ffff0000907beb50	mov x0,x19
ffff0000907beb54	bl 0xffff0000907bff28
ffff0000907beb58	ldr x0,[sp, #0x58]
ffff0000907beb5c	cbz x0,0xffff0000907be940
ffff0000907beb60	bl 0xffff0000907c0fd4
ffff0000907beb64	b 0xffff0000907be940
ffff0000907beb68	adrp x0,-0xffff6f806000
ffff0000907beb6c	add x0,x0,#0xb02
ffff0000907beb70	bl 0xffff0000907c119c
ffff0000907beb74	adrp x1,-0xffff6f81c000
ffff0000907beb78	add x1,x1,#0xabf
ffff0000907beb7c	adrp x2,-0xffff6f814000
ffff0000907beb80	add x2,x2,#0x9f7
ffff0000907beb84	adrp x3,-0xffff6f812000
ffff0000907beb88	add x3,x3,#0x78e
ffff0000907beb8c	adrp x4,-0xffff6f810000
ffff0000907beb90	add x4,x4,#0x463
ffff0000907beb94	mov x5,xzr
ffff0000907beb98	bl 0xffff0000907c0ea8
ffff0000907beb9c	bl 0xffff0000907c0ea4
