// File offset entry: 0x4014

/* WARNING: Possible PIC construction at 0x02003f58: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020044a8: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02004590: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02004634: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020046e4: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020047cc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02004880: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02004964: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020043f0: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x02004968) */
/* WARNING: Removing unreachable block (ram,0x0200498c) */
/* WARNING: Removing unreachable block (ram,0x0200496c) */
/* WARNING: Removing unreachable block (ram,0x02004884) */
/* WARNING: Removing unreachable block (ram,0x020048c8) */
/* WARNING: Removing unreachable block (ram,0x02004888) */
/* WARNING: Removing unreachable block (ram,0x020048d4) */
/* WARNING: Removing unreachable block (ram,0x020048dc) */
/* WARNING: Removing unreachable block (ram,0x020048b0) */
/* WARNING: Removing unreachable block (ram,0x020047d0) */
/* WARNING: Removing unreachable block (ram,0x020047e8) */
/* WARNING: Removing unreachable block (ram,0x020047d4) */
/* WARNING: Removing unreachable block (ram,0x02004800) */
/* WARNING: Removing unreachable block (ram,0x020047e0) */
/* WARNING: Removing unreachable block (ram,0x0200480c) */
/* WARNING: Removing unreachable block (ram,0x02004824) */
/* WARNING: Removing unreachable block (ram,0x020048b8) */
/* WARNING: Removing unreachable block (ram,0x020048e4) */
/* WARNING: Removing unreachable block (ram,0x020048e8) */
/* WARNING: Removing unreachable block (ram,0x02004908) */
/* WARNING: Removing unreachable block (ram,0x0200497c) */
/* WARNING: Removing unreachable block (ram,0x0200499c) */
/* WARNING: Removing unreachable block (ram,0x020049a0) */
/* WARNING: Removing unreachable block (ram,0x020049c0) */
/* WARNING: Removing unreachable block (ram,0x020049f4) */
/* WARNING: Removing unreachable block (ram,0x020049dc) */
/* WARNING: Removing unreachable block (ram,0x020049e4) */
/* WARNING: Removing unreachable block (ram,0x020049ec) */
/* WARNING: Removing unreachable block (ram,0x020049f8) */
/* WARNING: Removing unreachable block (ram,0x020049a8) */
/* WARNING: Removing unreachable block (ram,0x02004940) */
/* WARNING: Removing unreachable block (ram,0x020048f0) */
/* WARNING: Removing unreachable block (ram,0x0200485c) */
/* WARNING: Removing unreachable block (ram,0x02004814) */
/* WARNING: Removing unreachable block (ram,0x020046e8) */
/* WARNING: Removing unreachable block (ram,0x02004748) */
/* WARNING: Removing unreachable block (ram,0x020046ec) */
/* WARNING: Removing unreachable block (ram,0x02004704) */
/* WARNING: Removing unreachable block (ram,0x02004720) */
/* WARNING: Removing unreachable block (ram,0x02004754) */
/* WARNING: Removing unreachable block (ram,0x0200475c) */
/* WARNING: Removing unreachable block (ram,0x02004730) */
/* WARNING: Removing unreachable block (ram,0x02004638) */
/* WARNING: Removing unreachable block (ram,0x02004658) */
/* WARNING: Removing unreachable block (ram,0x0200463c) */
/* WARNING: Removing unreachable block (ram,0x02004594) */
/* WARNING: Removing unreachable block (ram,0x020045a0) */
/* WARNING: Removing unreachable block (ram,0x02004598) */
/* WARNING: Removing unreachable block (ram,0x020044ac) */
/* WARNING: Removing unreachable block (ram,0x02004500) */
/* WARNING: Removing unreachable block (ram,0x020045a8) */
/* WARNING: Removing unreachable block (ram,0x020044b0) */
/* WARNING: Removing unreachable block (ram,0x0200450c) */
/* WARNING: Removing unreachable block (ram,0x020044c8) */
/* WARNING: Removing unreachable block (ram,0x02004510) */
/* WARNING: Removing unreachable block (ram,0x0200451c) */
/* WARNING: Removing unreachable block (ram,0x0200456c) */
/* WARNING: Removing unreachable block (ram,0x02004588) */
/* WARNING: Removing unreachable block (ram,0x02003f5c) */
/* WARNING: Removing unreachable block (ram,0x02003fb8) */
/* WARNING: Removing unreachable block (ram,0x02003f60) */
/* WARNING: Removing unreachable block (ram,0x02003fd0) */
/* WARNING: Removing unreachable block (ram,0x02003f74) */
/* WARNING: Removing unreachable block (ram,0x02003fe4) */
/* WARNING: Removing unreachable block (ram,0x02003fec) */
/* WARNING: Removing unreachable block (ram,0x02004008) */
/* WARNING: Removing unreachable block (ram,0x02003ff4) */
/* WARNING: Removing unreachable block (ram,0x020043f4) */

undefined8 FUN_02003f14(void)

{
  byte bVar1;
  undefined1 *puVar2;
  undefined1 in_ZR;
  int iVar3;
  undefined4 uVar4;
  uint uVar5;
  uint uVar6;
  long lVar7;
  undefined8 uVar8;
  ulong uVar9;
  byte *pbVar10;
  undefined1 uVar11;
  undefined8 *unaff_x19;
  undefined8 *puVar12;
  undefined1 *unaff_x20;
  undefined1 *puVar13;
  ulong unaff_x21;
  undefined8 unaff_x22;
  ulong unaff_x23;
  undefined1 *puVar14;
  undefined8 uVar15;
  undefined1 auStack_50 [8];
  undefined8 uStack_48;
  undefined8 uStack_40;
  undefined8 uStack_38;
  undefined8 uStack_30;
  undefined8 uStack_28;
  
  puVar14 = &stack0xffffffffffffffe0;
  uStack_28 = *puRam0000000002103280;
  uStack_38 = 0;
  uStack_30 = 0;
  uStack_48 = 0;
  uStack_40 = 0;
  FUN_02091664(&uStack_48,0,0x20);
  uVar8 = 0;
  uVar15 = 0x2003f5c;
  puVar2 = auStack_50;
  puVar12 = &uStack_48;
code_r0x02004010:
  do {
    while( true ) {
      *(undefined1 **)(puVar2 + -0x40) = puVar14;
      *(undefined8 *)(puVar2 + -0x38) = uVar15;
      *(ulong *)(puVar2 + -0x30) = unaff_x23;
      *(undefined8 *)(puVar2 + -0x20) = unaff_x22;
      *(ulong *)(puVar2 + -0x18) = unaff_x21;
      *(undefined1 **)(puVar2 + -0x10) = unaff_x20;
      *(undefined8 **)(puVar2 + -8) = unaff_x19;
      puVar14 = puVar2 + -0x40;
      *(undefined8 *)(puVar2 + -0x48) = uRam0000000002100880;
      if (puVar12 == (undefined8 *)0x0) break;
      puVar13 = (undefined1 *)0x20e5000;
      uVar9 = (ulong)uRam00000000020e51e8;
      if ((int)uRam00000000020e51e8 < 0) {
        iVar3 = FUN_0207e344(0x20cbe9f);
        uVar9 = (ulong)(iVar3 == 0);
        uRam00000000020e51e8 = (uint)(iVar3 == 0);
      }
      unaff_x21 = *(ulong *)(uVar9 * 8 + 0x20e51f0);
      uVar6 = (uint)uVar8;
      unaff_x19 = puVar12;
      if (uVar6 != 0) {
        in_ZR = uVar6 == 2;
        unaff_x22 = uVar8;
        if (2 < uVar6) {
          uVar8 = 0x20caca0;
          goto LAB_020041e4;
        }
        in_ZR = ((uint)*(byte *)((long)puVar12 + 6) << 0x10 |
                 (uint)*(byte *)((long)puVar12 + 7) << 0x18 | (uint)*(ushort *)((long)puVar12 + 4))
                == 0x42414342;
        if ((bool)in_ZR) {
          uVar4 = func_0x02004b10(puVar12);
          puVar13 = (undefined1 *)0x210e008;
          *(char *)((long)puVar12 + 0x1c) = (char)uVar4;
          *(char *)((long)puVar12 + 0x1f) = (char)((uint)uVar4 >> 0x18);
          *(char *)((long)puVar12 + 0x1e) = (char)((uint)uVar4 >> 0x10);
          *(char *)((long)puVar12 + 0x1d) = (char)((uint)uVar4 >> 8);
          FUN_020915ac(0x210e008,puVar12,0x20);
          if (uVar6 == 2 && *(char *)((long)puVar12 + 0x14) == '\x01') {
            *(undefined8 *)(puVar2 + -0x58) = 0;
            *(undefined8 *)(puVar2 + -0x50) = 0;
            *(undefined8 *)(puVar2 + -0x68) = 0;
            *(undefined8 *)(puVar2 + -0x60) = 0;
            FUN_0208ada0(0x20b00a8,0x20c0607);
            puVar12 = (undefined8 *)(puVar2 + -0x68);
            FUN_020915ac(puVar2 + -0x68,0x210e008,0x20);
            FUN_02091664(0x210e008,0,0x20);
            FUN_020915ac(0x210e008,0x20d1708,4);
            iRam000000000210e00c = CONCAT22(iRam000000000210e00c._2_2_,1);
            pbVar10 = (byte *)0x210e012;
            for (lVar7 = 0xc; lVar7 != 0x10; lVar7 = lVar7 + 2) {
              bVar1 = *(byte *)((long)puVar12 + lVar7);
              pbVar10[-2] = bVar1 & 0xf;
              *pbVar10 = bVar1 >> 7;
              pbVar10[-1] = bVar1 >> 4 & 7;
              pbVar10 = pbVar10 + 4;
            }
            func_0x02004b10(0x210e008);
            uRam000000000210e024 = func_0x02003498();
            lVar7 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
            in_ZR = lVar7 == 0;
            if (lVar7 < 1) {
              uVar8 = 0x20bb3e5;
              goto LAB_020041f8;
            }
          }
          else {
            lVar7 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
            in_ZR = lVar7 == 0;
            if (lVar7 < 1) {
              uVar8 = 0x20bd3a4;
              goto LAB_020041f8;
            }
          }
        }
        else {
          uVar8 = 0x20c6b2e;
LAB_020041f8:
          FUN_0208ada0(uVar8,0x20c0607);
        }
LAB_0200441c:
        uVar8 = 0;
        goto LAB_02004420;
      }
      unaff_x23 = 0x210e000;
      unaff_x22 = 0x42414342;
      if (iRam000000000210e00c == 0x42414342) {
LAB_02004408:
        in_ZR = 1;
        FUN_020915ac(puVar12,0x210e008,0x20);
        goto LAB_0200441c;
      }
      puVar13 = (undefined1 *)0x210e008;
      unaff_x20 = (undefined1 *)0x210e008;
      lVar7 = FUN_0207e78c(unaff_x21,0x800,0x210e008,0x20);
      uVar6 = uRam000000000210e024;
      in_ZR = lVar7 == 0;
      if (lVar7 < 1) {
        FUN_0208ada0(0x20c25a3,0x20c0607);
        goto LAB_020041e8;
      }
      in_ZR = iRam000000000210e00c == 0x42414342;
      if ((bool)in_ZR) {
        unaff_x21 = (ulong)uRam000000000210e024;
        uVar5 = func_0x02004b10(0x210e008);
        if (uVar6 == uVar5) goto LAB_02004408;
        unaff_x21 = 0x210e008;
        iVar3 = func_0x020915d8(0x210e008,0x20d1708,4);
        uVar6 = uRam000000000210e024;
        if (iVar3 == 0) {
          unaff_x23 = (ulong)uRam000000000210e024;
          func_0x02004b10(0x210e008);
          uVar5 = func_0x02003498();
          if (uVar6 == uVar5) {
            uVar8 = 0x20b0069;
            in_ZR = 1;
            goto LAB_020043dc;
          }
        }
        uVar8 = 0x20c9af5;
LAB_0200431c:
        FUN_0208ada0(uVar8);
        uVar9 = FUN_02091524(0x20b313c);
        if (uVar9 < 4) {
          unaff_x21 = FUN_02091524(0x20b313c);
        }
        else {
          unaff_x21 = 2;
        }
        FUN_020915ac(0x210e008,0x20b313c,unaff_x21);
        *(undefined1 *)(unaff_x21 + 0x210e008) = 0;
        iRam000000000210e00c = 0x42414342;
        uRam000000000210e010 = 1;
        bRam000000000210e011 = bRam000000000210e011 & 0xf8 | 2;
        lVar7 = 0;
        while (in_ZR = lVar7 == 4, !(bool)in_ZR) {
          uVar11 = 0x7f;
          if (lVar7 == 0) {
            uVar11 = 0xff;
          }
          *(undefined1 *)(lVar7 + 0x210e014) = uVar11;
          lVar7 = lVar7 + 2;
        }
        uRam000000000210e024 = func_0x02004b10(0x210e008);
        iVar3 = func_0x02004aac(1);
        if (iVar3 != 0) {
          FUN_0208ada0(0x20b52cc);
        }
      }
      else {
        iVar3 = func_0x020915d8(0x210e008,0x20d1708,4);
        if (iVar3 != 0) {
          uVar8 = 0x20b4101;
          goto LAB_0200431c;
        }
        uVar8 = 0x20c9b23;
LAB_020043dc:
        FUN_0208ada0(uVar8);
        func_0x02004b88();
      }
      uVar8 = 1;
      uVar15 = 0x20043f4;
      puVar2 = puVar2 + -0x70;
      puVar12 = (undefined8 *)0x210e008;
    }
    uVar8 = 0x20bf43f;
    puVar13 = unaff_x20;
LAB_020041e4:
    FUN_0208ada0(uVar8);
    puVar12 = unaff_x19;
LAB_020041e8:
    uVar8 = 0xffffffff;
LAB_02004420:
    uVar8 = func_0x02004cf8(uVar8);
    if ((bool)in_ZR) {
      return uVar8;
    }
    unaff_x20 = (undefined1 *)FUN_02091090();
    *(undefined1 **)(puVar2 + -0xa0) = puVar14;
    *(undefined8 *)(puVar2 + -0x98) = 0x2004448;
    *(undefined8 *)(puVar2 + -0x90) = unaff_x22;
    *(ulong *)(puVar2 + -0x88) = unaff_x21;
    *(undefined1 **)(puVar2 + -0x80) = puVar13;
    *(undefined8 **)(puVar2 + -0x78) = puVar12;
    puVar14 = puVar2 + -0xa0;
    *(undefined8 *)(puVar2 + -0xa8) = *puRam0000000002103280;
    uVar6 = func_0x02003eac();
    in_ZR = uVar6 == 0xffffffff;
    if ((bool)in_ZR) {
      FUN_0208ada0(0x20bf41f,0xffffffff);
    }
    else {
      if (unaff_x20 != (undefined1 *)0x0) {
        *(undefined8 *)(puVar2 + -0xb8) = 0;
        *(undefined8 *)(puVar2 + -0xb0) = 0;
        *(undefined8 *)(puVar2 + -200) = 0;
        *(undefined8 *)(puVar2 + -0xc0) = 0;
        FUN_02091664(puVar2 + -200,0,0x20);
        puVar12 = (undefined8 *)(puVar2 + -200);
        uVar8 = 0;
        uVar15 = 0x20044ac;
        puVar2 = puVar2 + -0xd0;
        unaff_x19 = (undefined8 *)(ulong)uVar6;
        goto code_r0x02004010;
      }
      FUN_0208ada0(0x20c6b21);
    }
    func_0x02004cf8();
    if ((bool)in_ZR) {
      return 0xffffffff;
    }
    FUN_02091090();
    *(undefined1 **)(puVar2 + -0xf0) = puVar14;
    *(undefined8 *)(puVar2 + -0xe8) = 0x20045dc;
    *(undefined1 **)(puVar2 + -0xe0) = unaff_x20;
    *(undefined8 *)(puVar2 + -0xd8) = 0xffffffff;
    puVar14 = puVar2 + -0xf0;
    *(undefined8 *)(puVar2 + -0xf8) = uRam0000000002100880;
    uVar6 = func_0x02003eac();
    in_ZR = uVar6 == 0xffffffff;
    if ((bool)in_ZR) {
      FUN_0208ada0(0x20b40e9,0xffffffff);
      uVar8 = func_0x02004cf8(0xff);
      if ((bool)in_ZR) {
        return uVar8;
      }
      FUN_02091090();
      *(undefined1 **)(puVar2 + -0x140) = puVar14;
      *(undefined8 *)(puVar2 + -0x138) = 0x200468c;
      *(undefined1 **)(puVar2 + -0x130) = unaff_x20;
      *(undefined8 *)(puVar2 + -0x128) = 0xffffffff;
      puVar14 = puVar2 + -0x140;
      *(undefined8 *)(puVar2 + -0x148) = uRam0000000002100880;
      uVar6 = func_0x02003eac();
      in_ZR = uVar6 == 0xffffffff;
      unaff_x19 = (undefined8 *)(ulong)uVar6;
      if ((bool)in_ZR) {
        FUN_0208ada0(0x20b40e9,0xffffffff);
        func_0x02004cf8();
        if ((bool)in_ZR) {
          return 0xffffffff;
        }
        FUN_02091090();
        *(undefined1 **)(puVar2 + -0x180) = puVar14;
        *(undefined8 *)(puVar2 + -0x178) = 0x200478c;
        puVar14 = puVar2 + -0x180;
        *(undefined8 *)(puVar2 + -0x188) = *puRam0000000002103280;
        *(undefined8 *)(puVar2 + -0x198) = 0;
        *(undefined8 *)(puVar2 + -400) = 0;
        *(undefined8 *)(puVar2 + -0x1a8) = 0;
        *(undefined8 *)(puVar2 + -0x1a0) = 0;
        FUN_02091664(puVar2 + -0x1a8,0,0x20);
        puVar12 = (undefined8 *)(puVar2 + -0x1a8);
        uVar8 = 0;
        uVar15 = 0x20047d0;
        puVar2 = puVar2 + -0x1b0;
      }
      else {
        *(undefined8 *)(puVar2 + -0x158) = 0;
        *(undefined8 *)(puVar2 + -0x150) = 0;
        unaff_x20 = puVar2 + -0x168;
        *(undefined8 *)(puVar2 + -0x168) = 0;
        *(undefined8 *)(puVar2 + -0x160) = 0;
        FUN_02091664(puVar2 + -0x168,0,0x20);
        puVar12 = (undefined8 *)(puVar2 + -0x168);
        uVar8 = 0;
        uVar15 = 0x20046e8;
        puVar2 = puVar2 + -0x170;
      }
    }
    else {
      *(undefined8 *)(puVar2 + -0x108) = 0;
      *(undefined8 *)(puVar2 + -0x100) = 0;
      unaff_x20 = puVar2 + -0x118;
      *(undefined8 *)(puVar2 + -0x118) = 0;
      *(undefined8 *)(puVar2 + -0x110) = 0;
      FUN_02091664(puVar2 + -0x118,0,0x20);
      puVar12 = (undefined8 *)(puVar2 + -0x118);
      uVar8 = 0;
      uVar15 = 0x2004638;
      puVar2 = puVar2 + -0x120;
      unaff_x19 = (undefined8 *)(ulong)uVar6;
    }
  } while( true );
}


