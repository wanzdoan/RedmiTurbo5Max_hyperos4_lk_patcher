// File offset entry: 0xa6824

ulong FUN_020a6724(void)

{
  undefined1 *puVar1;
  byte bVar2;
  bool bVar3;
  char cVar4;
  char cVar5;
  byte bVar6;
  uint uVar7;
  int iVar8;
  int iVar9;
  ulong uVar10;
  long lVar11;
  long *extraout_x1;
  ulong *puVar12;
  int *in_x3;
  undefined8 in_x4;
  long lVar13;
  char *pcVar14;
  uint uVar15;
  byte *pbVar16;
  uint uVar17;
  uint uVar18;
  uint uVar19;
  uint uVar20;
  uint uVar21;
  undefined8 uVar22;
  ulong *puVar23;
  undefined8 unaff_x20;
  undefined8 unaff_x21;
  ulong unaff_x22;
  ulong uVar24;
  long lStack_198;
  int iStack_18c;
  undefined8 uStack_188;
  undefined8 uStack_180;
  undefined8 uStack_178;
  undefined8 uStack_170;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  ulong uStack_148;
  undefined8 uStack_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  long lStack_108;
  undefined1 *puStack_100;
  code *pcStack_f8;
  ulong uStack_f0;
  ulong uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  long lStack_c8;
  long lStack_b8;
  int iStack_b0;
  undefined4 uStack_ac;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  long lStack_68;
  
  lVar11 = lRam0000000002103800;
  lStack_68 = *plRam0000000002103280;
  uStack_ac = 0;
  lStack_b8 = 0;
  uStack_78 = 0;
  uStack_70 = 0;
  uStack_88 = 0;
  uStack_80 = 0;
  uStack_98 = 0;
  uStack_90 = 0;
  pbRam0000000002123ae0 = (byte *)uRam0000000002123840;
  uStack_a8 = 0;
  uStack_a0 = 0;
  uRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 == 0) {
    uVar24 = 0;
    bVar3 = true;
  }
  else {
    uVar7 = FUN_020a6a40(0xe);
    if (uVar7 == 0) {
      unaff_x20 = 0x20b3fd1;
      bVar3 = true;
      unaff_x21 = 0x20c9773;
      uVar22 = 0x20ccd9b;
      do {
        in_x3 = &iStack_b0;
        in_x4 = 2;
        iStack_b0 = 0;
        uVar7 = FUN_020a6c9c(0x13,&lStack_b8,&uStack_ac);
        if (uVar7 != 0) {
          uVar24 = (ulong)uVar7;
          uVar22 = 0x20b2131;
          goto LAB_020a67b8;
        }
        bVar6 = *(byte *)(lStack_b8 + 1);
        if (((char)bVar6 < '\0') && (uVar24 = (ulong)bVar6 & 0x7f, (int)uVar24 != 0)) {
          unaff_x22 = 0;
          lVar13 = uVar24 + 1;
          pbVar16 = (byte *)(lStack_b8 + 2);
          do {
            uVar7 = (int)uVar24 - 1;
            uVar24 = (ulong)uVar7;
            unaff_x22 = (ulong)((uint)*pbVar16 | (int)unaff_x22 << 8);
            pbVar16 = pbVar16 + 1;
          } while (uVar7 != 0);
        }
        else {
          unaff_x22 = (ulong)bVar6;
          lVar13 = 1;
        }
        uVar7 = (uint)unaff_x22;
        *(uint *)(lVar11 + 0xc0) = uVar7;
        bVar3 = 0x40 < uVar7;
        if (uVar7 < 0x41) {
          FUN_020a0dfc(&uStack_a8,(byte *)(lStack_b8 + 1) + lVar13,unaff_x22);
          if (*(uint *)(lVar11 + 0xc0) != 0) {
            uVar24 = (ulong)*(uint *)(lVar11 + 0xc0) - 1 >> 1;
            if (0x1e < uVar24) {
              uVar24 = 0x1f;
            }
            lVar13 = uVar24 + 1;
            pcVar14 = (char *)(lVar11 + 0x80);
            pbVar16 = (byte *)((ulong)&uStack_a8 | 1);
            do {
              bVar6 = pbVar16[-1];
              uVar17 = bVar6 - 0x30;
              if (9 < uVar17) {
                uVar18 = (uint)bVar6;
                if (bVar6 - 0x41 < 6) {
                  uVar17 = uVar18 - 0x37;
                }
                else {
                  uVar17 = uVar18 - 0x57;
                  if (5 < uVar18 - 0x61) {
                    uVar17 = uVar18;
                  }
                }
              }
              bVar6 = *pbVar16;
              uVar19 = (uint)bVar6;
              uVar18 = bVar6 - 0x57;
              if (5 < bVar6 - 0x61) {
                uVar18 = uVar19;
              }
              if (uVar19 - 0x41 < 6) {
                uVar18 = uVar19 - 0x37;
              }
              uVar15 = uVar19 - 0x30;
              if (9 < uVar19 - 0x30) {
                uVar15 = uVar18;
              }
              lVar13 = lVar13 + -1;
              *pcVar14 = (char)uVar15 + (char)(uVar17 << 4);
              pcVar14 = pcVar14 + 1;
              pbVar16 = pbVar16 + 2;
            } while (lVar13 != 0);
          }
          FUN_020a0fec(lVar11 + 0x80,0x20);
          uVar24 = 0;
        }
        else {
          FUN_0208ada0(0x20b3fd1,0x20c9773);
          uVar24 = 0x101028;
        }
        if ((uVar7 < 0x41) || (uRam0000000002123ae8 == 0)) goto LAB_020a67d4;
        uVar7 = FUN_020a6a40(0xe);
      } while (uVar7 == 0);
      uVar24 = (ulong)uVar7;
    }
    else {
      uVar24 = (ulong)uVar7;
      bVar3 = true;
      uVar22 = 0x20ccd9b;
    }
LAB_020a67b8:
    FUN_0208ada0(uVar22,0x20c9773);
  }
LAB_020a67d4:
  lVar13 = lRam0000000002103808;
  FUN_020a0fec(lRam0000000002103808 + 0x28,0x20);
  if ((bVar3) || (*(int *)(lVar11 + 0xc0) != 0x40)) {
    FUN_0208ada0(0x20ba485,0x20c9773);
    uVar10 = 0;
  }
  else {
    FUN_0208ada0(0x20d01ea,0x20c9773);
    iVar8 = FUN_0209156c(lVar13 + 0x28,lVar11 + 0x80,0x20);
    uVar7 = (uint)uVar24;
    if (iVar8 != 0) {
      uVar7 = 0x101021;
    }
    uVar10 = (ulong)uVar7;
  }
  pbRam0000000002123ae0 = (byte *)0x0;
  uRam0000000002123ae8 = 0;
  if (*plRam0000000002103280 == lStack_68) {
    return uVar10;
  }
  uVar10 = FUN_02091090(uVar10);
  pcStack_f8 = FUN_020a6a40;
  lStack_c8 = lVar13;
  puVar12 = (ulong *)0x40;
  lStack_108 = *plRam0000000002103280;
  uStack_158 = 0;
  uStack_150 = 0;
  uStack_168 = 0;
  uStack_160 = 0;
  uStack_178 = 0;
  uStack_170 = 0;
  uStack_188 = 0;
  uStack_180 = 0;
  lStack_198 = 0;
  uStack_118 = 0;
  uStack_110 = 0;
  uStack_128 = 0;
  uStack_120 = 0;
  uStack_138 = 0;
  uStack_130 = 0;
  uStack_148 = 0;
  uStack_140 = 0;
  puStack_100 = &stack0xffffffffffffffa0;
  uStack_f0 = uVar24;
  uStack_e0 = unaff_x22;
  uStack_d8 = unaff_x21;
  uStack_d0 = unaff_x20;
  FUN_020a0dfc(&uStack_148,(uVar10 & 0xffffffff) * 0x40 + 0x20e3cf8);
  func_0x020a0ebc(&uStack_148,0x20b7430);
  cVar4 = func_0x0209ad9c();
  func_0x020a0ebc(0,0x20b7430);
  cVar5 = func_0x0209ad9c();
  uStack_188 = CONCAT71(uStack_188._1_7_,cVar5 + cVar4 * '(');
  lVar11 = func_0x020a0ebc(0,0x20b7430);
  iVar8 = (int)in_x4;
  puVar23 = (ulong *)0x1;
  if (lVar11 != 0) {
    uVar24 = 1;
    do {
      uVar7 = func_0x0209ad9c();
      iVar8 = (int)in_x4;
      iVar9 = (int)uVar24;
      if (uVar7 < 0x80) {
        *(char *)((long)&uStack_188 + uVar24) = (char)uVar7;
        uVar7 = iVar9 + 1;
        puVar23 = (ulong *)(ulong)((int)puVar23 + 1);
        if (0x3f < uVar7) break;
      }
      else {
        lVar11 = 0;
        uVar17 = 0;
        uVar18 = 0xffffffff;
        uVar19 = 0xfffffff9;
        do {
          uVar15 = uVar19;
          uVar21 = uVar18;
          uVar20 = uVar17;
          lVar13 = lVar11;
          lVar11 = lVar13 + 1;
          uVar17 = uVar20 + 1;
          uVar18 = uVar21 + 1;
          uVar19 = uVar15 + 7;
        } while (uVar7 >> (ulong)((uVar20 & 3) << 3) != 0);
        if (uVar20 != 0) {
          uVar24 = (ulong)uVar21;
          pbVar16 = (byte *)&iStack_18c;
          lVar11 = lVar13;
          do {
            bVar3 = uVar24 != 0;
            uVar17 = uVar15 & 0x1f;
            lVar11 = lVar11 + -1;
            uVar24 = uVar24 - 1;
            uVar15 = uVar15 - 7;
            *pbVar16 = (byte)(uVar7 >> (ulong)uVar17) & 0x7f | bVar3 << 7;
            pbVar16 = pbVar16 + 1;
          } while (lVar11 != 0);
          lVar11 = 0;
          do {
            puVar1 = (undefined1 *)((long)&iStack_18c + lVar11);
            iVar8 = (int)lVar11;
            lVar11 = lVar11 + 1;
            *(undefined1 *)((long)&uStack_188 + (ulong)(uint)(iVar9 + iVar8)) = *puVar1;
          } while (lVar13 != lVar11);
        }
        uVar7 = uVar20 + iVar9;
        puVar23 = (ulong *)(ulong)(uVar20 + (int)puVar23);
      }
      uVar24 = (ulong)uVar7;
      lVar11 = func_0x020a0ebc(0,0x20b7430);
      iVar8 = (int)in_x4;
    } while (lVar11 != 0);
  }
  FUN_020a0fec(&uStack_188,puVar23);
  do {
    if ((int)uRam0000000002123ae8 < 1) {
      uVar24 = 0x101011;
      break;
    }
    puVar12 = &uStack_148;
    in_x3 = &iStack_18c;
    iVar8 = 0;
    uStack_148 = uStack_148 & 0xffffffff00000000;
    iStack_18c = 0;
    iVar9 = FUN_020a6c9c(6,&lStack_198);
  } while ((iVar9 != 0) ||
          (puVar12 = puVar23,
          uVar24 = FUN_020a0e58(lStack_198 + (uStack_148 & 0xffffffff) + 1,&uStack_188),
          (int)uVar24 != 0));
  if (*plRam0000000002103280 == lStack_108) {
    return uVar24;
  }
  bVar6 = FUN_02091090(uVar24);
  if ((int)uRam0000000002123ae8 < 1) {
    uVar7 = 0x101012;
    goto LAB_020a6dc8;
  }
  FUN_020a0fec(pbRam0000000002123ae0,0x10);
  iVar9 = *in_x3;
  *(int *)puVar12 = 0;
  *in_x3 = 0;
  func_0x020a6f34(pbRam0000000002123ae0,puVar12,in_x3);
  bVar2 = *pbRam0000000002123ae0;
  uVar7 = 0;
  if (bVar2 != bVar6) {
    uVar7 = 0x10100d;
  }
  if ((iVar9 != 0) && (iVar9 != *in_x3)) {
    uVar7 = 0x10100e;
  }
  if (iVar8 == 0) {
    uVar17 = (int)*puVar12 + 1;
    if ((bVar2 >> 5 & 1) == 0) {
      iVar8 = *in_x3;
      goto LAB_020a6d88;
    }
LAB_020a6d8c:
    if (uRam0000000002123ae8 < uVar17) {
      uRam0000000002123ae8 = 0;
      uVar7 = 0x101028;
      goto LAB_020a6dc8;
    }
  }
  else {
    if (iVar8 == 1) {
      uVar17 = (uint)*puVar12;
      iVar8 = 1;
      if (bVar2 == 3) {
        iVar8 = 2;
      }
LAB_020a6d88:
      uVar17 = uVar17 + iVar8;
      goto LAB_020a6d8c;
    }
    if (iVar8 == 2) {
      uVar17 = (int)*puVar12 + *in_x3 + 1;
      goto LAB_020a6d8c;
    }
    uVar17 = 0;
    uVar7 = 0x10100f;
  }
  *extraout_x1 = (long)pbRam0000000002123ae0;
  pbRam0000000002123ae0 = pbRam0000000002123ae0 + uVar17;
  uRam0000000002123ae8 = uRam0000000002123ae8 - uVar17;
LAB_020a6dc8:
  return (ulong)uVar7;
}


