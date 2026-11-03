// File offset entry: 0xa6b40

/* WARNING: Possible PIC construction at 0x020a6c14: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a6c18) */
/* WARNING: Removing unreachable block (ram,0x020a6c1c) */
/* WARNING: Removing unreachable block (ram,0x020a6c8c) */
/* WARNING: Removing unreachable block (ram,0x020a6c94) */
/* WARNING: Removing unreachable block (ram,0x020a6c3c) */
/* WARNING: Removing unreachable block (ram,0x020a6c48) */

undefined4
FUN_020a6a40(ulong param_1,undefined8 param_2,undefined8 param_3,byte *param_4,undefined8 param_5)

{
  undefined4 uVar1;
  byte bVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  bool bVar6;
  char cVar7;
  char cVar8;
  byte bVar9;
  uint uVar10;
  long lVar11;
  undefined8 *extraout_x1;
  undefined8 *puVar12;
  ulong *puVar13;
  uint uVar14;
  int iVar15;
  int iVar16;
  long lVar17;
  uint uVar18;
  byte *pbVar19;
  uint uVar20;
  int iVar21;
  ulong uVar22;
  undefined8 uStack_d8;
  byte abStack_cc [4];
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  ulong auStack_88 [9];
  
  puVar13 = (ulong *)0x40;
  auStack_88[8] = *plRam0000000002103280;
  uStack_98 = 0;
  uStack_90 = 0;
  uStack_a8 = 0;
  uStack_a0 = 0;
  uStack_b8 = 0;
  uStack_b0 = 0;
  uStack_c8 = 0;
  uStack_c0 = 0;
  uStack_d8 = 0;
  auStack_88[6] = 0;
  auStack_88[7] = 0;
  auStack_88[4] = 0;
  auStack_88[5] = 0;
  auStack_88[2] = 0;
  auStack_88[3] = 0;
  auStack_88[0] = 0;
  auStack_88[1] = 0;
  func_0x020a0dfc(auStack_88,(param_1 & 0xffffffff) * 0x40 + 0x20e3cf8);
  func_0x020a0ebc(auStack_88,0x20b7430);
  cVar7 = func_0x0209ad9c();
  func_0x020a0ebc(0,0x20b7430);
  cVar8 = func_0x0209ad9c();
  uStack_c8 = CONCAT71(uStack_c8._1_7_,cVar8 + cVar7 * '(');
  lVar11 = func_0x020a0ebc(0,0x20b7430);
  iVar15 = (int)param_5;
  iVar16 = 1;
  if (lVar11 != 0) {
    uVar22 = 1;
    do {
      uVar10 = func_0x0209ad9c();
      iVar15 = (int)param_5;
      iVar21 = (int)uVar22;
      if (uVar10 < 0x80) {
        *(char *)((long)&uStack_c8 + uVar22) = (char)uVar10;
        uVar10 = iVar21 + 1;
        iVar16 = iVar16 + 1;
        if (0x3f < uVar10) break;
      }
      else {
        lVar11 = 0;
        uVar3 = 0;
        uVar4 = 0xffffffff;
        uVar5 = 0xfffffff9;
        do {
          uVar14 = uVar5;
          uVar20 = uVar4;
          uVar18 = uVar3;
          lVar17 = lVar11;
          lVar11 = lVar17 + 1;
          uVar3 = uVar18 + 1;
          uVar4 = uVar20 + 1;
          uVar5 = uVar14 + 7;
        } while (uVar10 >> (ulong)((uVar18 & 3) << 3) != 0);
        if (uVar18 != 0) {
          uVar22 = (ulong)uVar20;
          pbVar19 = abStack_cc;
          lVar11 = lVar17;
          do {
            bVar6 = uVar22 != 0;
            uVar3 = uVar14 & 0x1f;
            lVar11 = lVar11 + -1;
            uVar22 = uVar22 - 1;
            uVar14 = uVar14 - 7;
            *pbVar19 = (byte)(uVar10 >> (ulong)uVar3) & 0x7f | bVar6 << 7;
            pbVar19 = pbVar19 + 1;
          } while (lVar11 != 0);
          lVar11 = 0;
          do {
            pbVar19 = abStack_cc + lVar11;
            iVar15 = (int)lVar11;
            lVar11 = lVar11 + 1;
            *(byte *)((long)&uStack_c8 + (ulong)(uint)(iVar21 + iVar15)) = *pbVar19;
          } while (lVar17 != lVar11);
        }
        uVar10 = uVar18 + iVar21;
        iVar16 = uVar18 + iVar16;
      }
      uVar22 = (ulong)uVar10;
      lVar11 = func_0x020a0ebc(0,0x20b7430);
      iVar15 = (int)param_5;
    } while (lVar11 != 0);
  }
  FUN_020a0fec(&uStack_c8,iVar16);
  if ((int)uRam0000000002123ae8 < 1) {
    if (*plRam0000000002103280 == auStack_88[8]) {
      return 0x101011;
    }
    bVar9 = FUN_02091090(0x101011);
    puVar12 = extraout_x1;
  }
  else {
    puVar12 = &uStack_d8;
    puVar13 = auStack_88;
    param_4 = abStack_cc;
    bVar9 = 6;
    iVar15 = 0;
    auStack_88[0] = auStack_88[0] & 0xffffffff00000000;
    abStack_cc[0] = 0;
    abStack_cc[1] = 0;
    abStack_cc[2] = 0;
    abStack_cc[3] = 0;
  }
  if ((int)uRam0000000002123ae8 < 1) {
    return 0x101012;
  }
  FUN_020a0fec(pbRam0000000002123ae0,0x10);
  iVar16 = *(int *)param_4;
  *(int *)puVar13 = 0;
  param_4[0] = 0;
  param_4[1] = 0;
  param_4[2] = 0;
  param_4[3] = 0;
  func_0x020a6f34(pbRam0000000002123ae0,puVar13,param_4);
  bVar2 = *pbRam0000000002123ae0;
  uVar1 = 0;
  if (bVar2 != bVar9) {
    uVar1 = 0x10100d;
  }
  if ((iVar16 != 0) && (iVar16 != *(int *)param_4)) {
    uVar1 = 0x10100e;
  }
  if (iVar15 == 0) {
    uVar10 = (int)*puVar13 + 1;
    if ((bVar2 >> 5 & 1) == 0) {
      iVar16 = *(int *)param_4;
      goto LAB_020a6d88;
    }
  }
  else if (iVar15 == 1) {
    uVar10 = (uint)*puVar13;
    iVar16 = 1;
    if (bVar2 == 3) {
      iVar16 = 2;
    }
LAB_020a6d88:
    uVar10 = uVar10 + iVar16;
  }
  else {
    if (iVar15 != 2) {
      uVar10 = 0;
      uVar1 = 0x10100f;
      goto LAB_020a6db0;
    }
    uVar10 = (int)*puVar13 + *(int *)param_4 + 1;
  }
  if (uRam0000000002123ae8 < uVar10) {
    uRam0000000002123ae8 = 0;
    return 0x101028;
  }
LAB_020a6db0:
  *puVar12 = pbRam0000000002123ae0;
  uRam0000000002123ae8 = uRam0000000002123ae8 - uVar10;
  pbRam0000000002123ae0 = pbRam0000000002123ae0 + uVar10;
  return uVar1;
}


