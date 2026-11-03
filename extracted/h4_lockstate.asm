089aec: paciasp    
089af0: sub        sp, sp, #0x150
089af4: stp        x29, x30, [sp, #0x120]
089af8: stp        x28, x21, [sp, #0x130]
089afc: stp        x20, x19, [sp, #0x140]
089b00: add        x29, sp, #0x120
089b04: mov        x19, x0
089b08: adrp       x0, #0xffff0000907fe000
089b0c: add        x0, x0, #0x339
089b10: adrp       x1, #0xffff0000907e3000
089b14: add        x1, x1, #0xb88
089b18: stp        xzr, xzr, [sp, #0xf8]
089b1c: stp        xzr, xzr, [sp, #0xe8]
089b20: stp        xzr, xzr, [sp, #0xd8]
089b24: stp        xzr, xzr, [sp, #0xc8]
089b28: stp        xzr, xzr, [sp, #0xb8]
089b2c: stp        xzr, xzr, [sp, #0xa8]
089b30: stp        xzr, xzr, [sp, #0x98]
089b34: stp        xzr, xzr, [sp, #0x88]
089b38: stp        xzr, xzr, [sp, #0x78]
089b3c: stp        xzr, xzr, [sp, #0x68]
089b40: stp        xzr, xzr, [sp, #0x58]
089b44: stp        xzr, xzr, [sp, #0x48]
089b48: stp        xzr, xzr, [sp, #0x38]
089b4c: stp        xzr, xzr, [sp, #0x28]
089b50: stp        xzr, xzr, [sp, #0x18]
089b54: stp        xzr, xzr, [sp, #8]
089b58: bl         #0xffff00009077e11c
089b5c: mov        w8, #9
089b60: adrp       x0, #0xffff0000907dd000
089b64: add        x0, x0, #0x40e
089b68: adrp       x1, #0xffff0000907d6000
089b6c: add        x1, x1, #0xdc6
089b70: sturb      wzr, [x29, #-8]
089b74: stur       xzr, [x29, #-0x10]
089b78: stur       w8, [x29, #-0x14]
089b7c: bl         #0xffff00009077e11c
089b80: sub        x0, x29, #0x10
089b84: sub        x1, x29, #0x14
089b88: bl         #0xffff00009078a0c8
089b8c: cbz        w0, #0xffff000090789be0
089b90: mov        w20, w0
089b94: adrp       x0, #0xffff0000907f3000
089b98: add        x0, x0, #0x88b
089b9c: adrp       x1, #0xffff0000907d6000
089ba0: add        x1, x1, #0xdc6
089ba4: mov        w2, w20
089ba8: bl         #0xffff00009077e11c
089bac: cmp        w20, #3
089bb0: b.eq       #0xffff000090789c04
089bb4: cmp        w20, #4
089bb8: b.eq       #0xffff000090789da4
089bbc: cmp        w20, #6
089bc0: b.ne       #0xffff000090789c6c
089bc4: mov        w0, #0x1b
089bc8: bl         #0xffff00009074a384
089bcc: mov        w8, #0xe
089bd0: bics       wzr, w8, w0
089bd4: mov        w8, #3
089bd8: cinc       w8, w8, eq
089bdc: b          #0xffff000090789da8
089be0: nop        
089be4: adr        x1, #0xffff0000908366b0
089be8: sub        x0, x29, #0x10
089bec: mov        w2, #8
089bf0: bl         #0xffff00009077eae8
089bf4: cbz        w0, #0xffff000090789c58
089bf8: adrp       x0, #0xffff0000907d9000
089bfc: add        x0, x0, #0xecc
089c00: bl         #0xffff00009077e11c
089c04: adrp       x0, #0xffff0000907dd000
089c08: add        x0, x0, #0x40e
089c0c: adrp       x1, #0xffff0000907e3000
089c10: add        x1, x1, #0xb9a
089c14: bl         #0xffff00009077e11c
089c18: bl         #0xffff00009073527c
089c1c: cmp        w0, #2
089c20: b.ne       #0xffff000090789da4
089c24: bl         #0xffff000090796b1c
089c28: cmp        x0, #0x400, lsl #12
089c2c: b.eq       #0xffff000090789ccc
089c30: mov        x1, x0
089c34: mov        w8, #0x1000000
089c38: cmp        x0, x8
089c3c: b.ne       #0xffff000090789d04
089c40: mov        w0, wzr
089c44: bl         #0xffff000090796b40
089c48: mov        x8, #0xffffffe000
089c4c: add        x8, x0, x8
089c50: lsr        x0, x8, #8
089c54: b          #0xffff000090789cdc
089c58: adrp       x0, #0xffff0000907e2000
089c5c: add        x0, x0, #0x83
089c60: adrp       x1, #0xffff0000907d6000
089c64: add        x1, x1, #0xdc6
089c68: bl         #0xffff00009077e11c
089c6c: adrp       x0, #0xffff0000907dd000
089c70: add        x0, x0, #0x40e
089c74: adrp       x1, #0xffff0000907ee000
089c78: add        x1, x1, #0xef2
089c7c: stur       wzr, [x29, #-0x10]
089c80: bl         #0xffff00009077e11c
089c84: bl         #0xffff00009073527c
089c88: cmp        w0, #2
089c8c: b.ne       #0xffff000090789cc4
089c90: bl         #0xffff000090796b1c
089c94: cmp        x0, #0x400, lsl #12
089c98: b.eq       #0xffff000090789d10
089c9c: mov        x1, x0
089ca0: mov        w8, #0x1000000
089ca4: cmp        x0, x8
089ca8: b.ne       #0xffff000090789d6c
089cac: mov        w0, wzr
089cb0: bl         #0xffff000090796b40
089cb4: mov        x8, #0xffffffe000
089cb8: add        x8, x0, x8
089cbc: lsr        x20, x8, #8
089cc0: b          #0xffff000090789d20
089cc4: mov        w20, #5
089cc8: b          #0xffff000090789d94
089ccc: adrp       x8, #0xffff000090887000
089cd0: mov        w9, #1
089cd4: mov        w0, #0x3fe0
089cd8: str        x9, [x8, #0x598]
089cdc: nop        
089ce0: adr        x1, #0xffff0000908366b0
089ce4: mov        w2, #8
089ce8: bl         #0xffff00009078a3d8
089cec: adrp       x0, #0xffff0000907fa000
089cf0: add        x0, x0, #0x94e
089cf4: adrp       x1, #0xffff0000907e3000
089cf8: add        x1, x1, #0xb9a
089cfc: bl         #0xffff00009077e11c
089d00: b          #0xffff000090789da4
089d04: adrp       x0, #0xffff0000907ea000
089d08: add        x0, x0, #0x175
089d0c: b          #0xffff000090789da0
089d10: adrp       x8, #0xffff000090887000
089d14: mov        w9, #1
089d18: mov        w20, #0x3fe0
089d1c: str        x9, [x8, #0x598]
089d20: add        w0, w20, #1
089d24: sub        x1, x29, #0x10
089d28: mov        w2, #4
089d2c: bl         #0xffff00009078a1b8
089d30: ldur       w2, [x29, #-0x10]
089d34: cbnz       w0, #0xffff000090789d80
089d38: cmp        w2, #0x101
089d3c: b.hs       #0xffff000090789d80
089d40: add        w0, w20, #2
089d44: add        x1, sp, #8
089d48: bl         #0xffff00009078a1b8
089d4c: cbz        w0, #0xffff000090789dc8
089d50: mov        w20, w0
089d54: adrp       x0, #0xffff0000907fa000
089d58: add        x0, x0, #0x94e
089d5c: adrp       x1, #0xffff0000907ee000
089d60: add        x1, x1, #0xef2
089d64: bl         #0xffff00009077e11c
089d68: b          #0xffff000090789d94
089d6c: adrp       x0, #0xffff0000907ea000
089d70: add        x0, x0, #0x175
089d74: bl         #0xffff00009077e11c
089d78: mov        w20, #7
089d7c: b          #0xffff000090789d94
089d80: adrp       x0, #0xffff0000907eb000
089d84: add        x0, x0, #0x8f6
089d88: mov        w1, w2
089d8c: bl         #0xffff00009077e11c
089d90: mov        w20, #1
089d94: adrp       x0, #0xffff0000907f1000
089d98: add        x0, x0, #0xfe0
089d9c: mov        w1, w20
089da0: bl         #0xffff00009077e11c
089da4: mov        w8, #4
089da8: str        w8, [x19]
089dac: ldp        x20, x19, [sp, #0x140]
089db0: mov        w0, wzr
089db4: ldp        x28, x21, [sp, #0x130]
089db8: ldp        x29, x30, [sp, #0x120]
089dbc: add        sp, sp, #0x150
089dc0: autiasp    
089dc4: ret        
089dc8: adrp       x0, #0xffff0000907fa000
089dcc: add        x0, x0, #0x94e
089dd0: adrp       x1, #0xffff0000907ee000
089dd4: add        x1, x1, #0xef2
089dd8: ldur       w20, [x29, #-0x10]
089ddc: bl         #0xffff00009077e11c
089de0: cbz        w20, #0xffff000090789e30
089de4: adrp       x21, #0xffff0000907e3000
089de8: add        x21, x21, #0xb88
089dec: adrp       x0, #0xffff0000907ee000
089df0: add        x0, x0, #0xed4
089df4: mov        x1, x21
089df8: mov        w2, w20
089dfc: bl         #0xffff00009077e11c
089e00: add        x0, sp, #8
089e04: mov        w1, w20
089e08: bl         #0xffff0000907898cc
089e0c: mov        w8, #3
089e10: cmp        w0, #0
089e14: cinc       w8, w8, ne
089e18: adrp       x0, #0xffff0000907d8000
089e1c: add        x0, x0, #0x67e
089e20: mov        x1, x21
089e24: str        w8, [x19]
089e28: bl         #0xffff00009077e11c
089e2c: b          #0xffff000090789dac
089e30: adrp       x0, #0xffff0000907e5000
089e34: add        x0, x0, #0x59c
089e38: bl         #0xffff00009077e11c
089e3c: b          #0xffff000090789da4
089e40: paciasp    
089e44: stp        x29, x30, [sp, #-0x40]!
089e48: str        x28, [sp, #0x10]
089e4c: stp        x22, x21, [sp, #0x20]
089e50: stp        x20, x19, [sp, #0x30]
089e54: mov        x29, sp
089e58: sub        sp, sp, #0xa00
089e5c: mov        w19, w0
089e60: adrp       x0, #0xffff0000907fe000
089e64: add        x0, x0, #0x339
089e68: adrp       x1, #0xffff0000907fc000
089e6c: add        x1, x1, #0x69c
089e70: bl         #0xffff00009077e11c
089e74: sub        w8, w19, #5
089e78: cmn        w8, #3
089e7c: b.hi       #0xffff000090789ea0
089e80: adrp       x0, #0xffff0000907e2000
089e84: add        x0, x0, #0x5d
089e88: adrp       x1, #0xffff0000907fc000
089e8c: add        x1, x1, #0x69c
089e90: mov        w2, w19
089e94: bl         #0xffff00009077e11c
089e98: mov        w21, #4
089e9c: b          #0xffff00009078a0a8
089ea0: bl         #0xffff00009078900c
089ea4: ldr        x19, [x0]
089ea8: mov        x21, x0
089eac: ldr        w20, [x0, #8]
089eb0: adrp       x1, #0xffff0000907f3000
089eb4: add        x1, x1, #0xfb1
089eb8: mov        x0, x19
089ebc: mov        x2, x20
089ec0: bl         #0xffff00009077eae8
089ec4: cbz        w0, #0xffff000090789f70
089ec8: add        x0, sp, #0x800
089ecc: mov        w1, wzr
089ed0: mov        w2, #0x200
089ed4: bl         #0xffff00009077ee50
089ed8: mov        x0, sp
089edc: mov        w1, wzr
089ee0: mov        w2, #0x800
089ee4: mov        x22, sp
089ee8: bl         #0xffff00009077ee50
089eec: add        x2, sp, #0x800
089ef0: mov        x0, x19
089ef4: mov        w1, w20
089ef8: bl         #0xffff000090789504
089efc: cmp        w0, #1
089f00: b.lt       #0xffff000090789f3c
089f04: add        x0, sp, #0x800
089f08: mov        x1, sp
089f0c: bl         #0xffff000090789734
089f10: sxtw       x2, w0
089f14: mov        x1, sp
089f18: nop        
089f1c: adr        x0, #0xffff0000908875a0
089f20: strb       wzr, [x22, x2]
089f24: bl         #0xffff00009077eae8
089f28: cbz        w0, #0xffff000090789f68
089f2c: adrp       x0, #0xffff0000907d9000
089f30: add        x0, x0, #0xebb
089f34: bl         #0xffff00009077e11c
089f38: b          #0xffff000090789f54
089f3c: mov        w19, w0
089f40: adrp       x0, #0xffff0000907ed000
089f44: add        x0, x0, #0x393
089f48: mov        w1, w19
089f4c: bl         #0xffff00009077e11c
089f50: cbz        w19, #0xffff000090789f68
089f54: adrp       x0, #0xffff0000907fc000
089f58: add        x0, x0, #0x6ae
089f5c: bl         #0xffff00009077e11c