// ffff0000907504ac  size=2188  FUN_ffff0000907504ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907504ac(int param_1,undefined8 param_2,uint *param_3)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  uint *puVar9;
  long *plVar10;
  uint uVar11;
  uint uVar12;
  int iVar13;
  long lVar14;
  byte *pbVar15;
  ulong uVar16;
  int *piVar17;
  byte *pbVar18;
  undefined4 uVar19;
  uint *puVar20;
  long lVar21;
  uint uVar22;
  ulong uVar23;
  ulong uVar24;
  uint local_64;
  
  uVar3 = param_3[2];
  uVar4 = *param_3;
  pbVar15 = *(byte **)(param_3 + 4);
  if (param_3[3] == 0) {
    uVar11 = 0;
    if (param_1 != 0x2b) {
      uVar11 = (uint)(param_1 != 0x2d);
    }
    uVar16 = (ulong)uVar11;
    lVar14 = 1;
    if (param_1 != 0x2b) {
      lVar14 = 2;
    }
    lVar14 = lVar14 - uVar16;
    piVar17 = (int *)(&DAT_ffff0000908787d0 + uVar16 * 0xbd8);
    plVar10 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar16 * 8 + 0x10);
    do {
      iVar13 = *piVar17;
      piVar17 = piVar17 + 0x2f6;
      lVar21 = *plVar10;
      uVar19 = 0x55;
      if (iVar13 != 0) {
        uVar19 = 0xaa;
      }
      lVar14 = lVar14 + -1;
      *(undefined4 *)(lVar21 + 0x610) = uVar19;
      *(uint *)(lVar21 + 0x1d0) = *(uint *)(lVar21 + 0x1d0) | 1;
      plVar10 = plVar10 + 1;
    } while (lVar14 != 0);
  }
  uVar16 = (ulong)uVar3 & 0xff;
  uVar11 = 0;
  if (param_1 != 0x2b) {
    uVar11 = (uint)(param_1 != 0x2d);
  }
  uVar23 = (ulong)uVar11;
  uVar5 = (uVar4 & 0xff) << 0x10;
  uVar11 = uVar3 & 0xff;
  uVar3 = uVar3 * 0x10000 + 0x10000 & 0xff0000;
  uVar2 = 1;
  if (param_1 != 0x2b) {
    uVar2 = 2;
  }
  uVar22 = (uint)uVar16;
  uVar1 = uVar22 * 0x10000 + 0x10000;
  lVar14 = uVar2 - uVar23;
  uVar24 = uVar23;
  do {
    if ((*(byte *)((&DAT_ffff00009080cf90)[uVar24] + 0x34) & 3) == 0) {
      if (((*(byte *)((&DAT_ffff00009080cf90)[uVar23] + 0x34) & 3) == 0) &&
         (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar23 * 8 + 0x10) + 0xc) < 0)) {
        iVar13 = 0;
        do {
          iVar13 = iVar13 + 1;
          lVar21 = lVar14;
          if (((uint)(iVar13 * 0x26e978d5) >> 3 | iVar13 * -0x60000000) < 0x418938) {
            do {
              lVar21 = lVar21 + -1;
            } while (lVar21 != 0);
          }
        } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar23 * 8 + 0x10) + 0xc)
                 < 0);
      }
      uVar12 = param_3[3];
      if (uVar4 < 0xb0) {
        if (uVar22 < 2) {
          uVar12 = uVar5 | (uint)(uVar12 == 0) << 3;
          if (uVar11 == 0) {
            uVar6 = 0x500;
          }
          else {
            uVar12 = uVar12 | (uint)*pbVar15 << 0x18;
            uVar6 = 0x1500;
          }
          goto LAB_ffff000090750aa4;
        }
        puVar9 = *(uint **)(&DAT_ffff000090804cf0 + uVar24 * 8);
        uVar6 = 0x390a;
        if (uVar12 != 0) {
          uVar6 = 0x3902;
        }
        puVar20 = (uint *)((ulong)(puVar9 + 1) & 0xfffffffffffffffc);
        uVar12 = (int)puVar9 * 8 + 0x28;
        uVar8 = (long)puVar9 + 5;
        *puVar9 = uVar6 | uVar1;
        uVar6 = ((uint)(puVar9 + 1) & 3) << 3;
        *puVar20 = *puVar20 & (0xff << (ulong)uVar6 ^ 0xffffffffU) | uVar4 << (ulong)uVar6;
        uVar7 = uVar16;
        pbVar18 = pbVar15;
        do {
          puVar9 = (uint *)(uVar8 & 0xfffffffffffffffc);
          uVar6 = uVar12 & 0x18;
          uVar12 = uVar12 + 8;
          uVar8 = uVar8 + 1;
          uVar7 = uVar7 - 1;
          *puVar9 = *puVar9 & (0xff << (ulong)uVar6 ^ 0xffffffffU) | (uint)*pbVar18 << (ulong)uVar6;
          pbVar18 = pbVar18 + 1;
        } while (uVar7 != 0);
LAB_ffff00009075094c:
        lVar21 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar24 * 8 + 0x10);
        iVar13 = (uVar22 >> 2) + 0x8002;
      }
      else {
        if (1 < uVar22) {
          puVar9 = *(uint **)(&DAT_ffff000090804cf0 + uVar24 * 8);
          uVar6 = 0x290a;
          if (uVar12 != 0) {
            uVar6 = 0x2902;
          }
          puVar20 = (uint *)((ulong)(puVar9 + 1) & 0xfffffffffffffffc);
          uVar12 = (int)puVar9 * 8 + 0x28;
          uVar8 = (long)puVar9 + 5;
          *puVar9 = uVar6 | uVar1;
          uVar6 = ((uint)(puVar9 + 1) & 3) << 3;
          *puVar20 = *puVar20 & (0xff << (ulong)uVar6 ^ 0xffffffffU) | uVar4 << (ulong)uVar6;
          uVar7 = uVar16;
          pbVar18 = pbVar15;
          do {
            puVar9 = (uint *)(uVar8 & 0xfffffffffffffffc);
            uVar6 = uVar12 & 0x18;
            uVar12 = uVar12 + 8;
            uVar8 = uVar8 + 1;
            uVar7 = uVar7 - 1;
            *puVar9 = *puVar9 & (0xff << (ulong)uVar6 ^ 0xffffffffU) |
                      (uint)*pbVar18 << (ulong)uVar6;
            pbVar18 = pbVar18 + 1;
          } while (uVar7 != 0);
          goto LAB_ffff00009075094c;
        }
        uVar12 = uVar5 | (uint)(uVar12 == 0) << 3;
        if (uVar11 == 0) {
          uVar6 = 0x1300;
        }
        else {
          uVar12 = uVar12 | (uint)*pbVar15 << 0x18;
          uVar6 = 0x2300;
        }
LAB_ffff000090750aa4:
        **(uint **)(&DAT_ffff000090804cf0 + uVar24 * 8) = uVar12 | uVar6;
        lVar21 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar24 * 8 + 0x10);
        iVar13 = 0x8001;
      }
      *(int *)(lVar21 + 0x44) = iVar13;
    }
    else {
      local_64 = 0;
      FUN_ffff00009077ee50(&local_64,0,4);
      lVar21 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar24 * 8 + 0x10);
      uVar6 = (*(uint *)(lVar21 + 0x110) >> 4 & 1) << 4 | (*(uint *)(lVar21 + 0x110) >> 3 & 1) << 3
              | (*(uint *)(lVar21 + 0x110) >> 5 & 1) << 5;
      uVar12 = *(uint *)(lVar21 + 0x110) & 1 | (*(uint *)(lVar21 + 0x110) >> 2 & 1) << 2;
      if (uVar4 < 0xb0) {
        if (uVar22 < 2) {
          if (uVar11 == 0) {
            uVar12 = local_64 & 0xc0 | uVar6 | uVar12 | uVar5;
            uVar6 = 0x500;
          }
          else {
            uVar12 = local_64 & 0xc0 | uVar6 | uVar12 | uVar5 | (uint)*pbVar15 << 0x18;
            uVar6 = 0x1500;
          }
LAB_ffff0000907505f0:
          *(uint *)(lVar21 + 0x110) = uVar12 | uVar6;
        }
        else {
          *(uint *)(lVar21 + 0x110) = uVar3 | 0x3902 | local_64 & 0xff0000c0 | uVar6 | uVar12;
          uVar7 = *(ulong *)(&DAT_ffff000090804d20 + uVar24 * 8);
          uVar12 = (int)uVar7 * 8;
          *(uint *)(uVar7 & 0xfffffffffffffffc) =
               *(uint *)(uVar7 & 0xfffffffffffffffc) &
               (0xff << (ulong)(uVar12 & 0x18) ^ 0xffffffffU) | uVar4 << (ulong)(uVar12 & 0x18);
          uVar8 = uVar16;
          pbVar18 = pbVar15;
          do {
            uVar12 = uVar12 + 8;
            uVar7 = uVar7 + 1;
            uVar8 = uVar8 - 1;
            *(uint *)(uVar7 & 0xfffffffffffffffc) =
                 *(uint *)(uVar7 & 0xfffffffffffffffc) &
                 (0xff << (ulong)(uVar12 & 0x18) ^ 0xffffffffU) |
                 (uint)*pbVar18 << (ulong)(uVar12 & 0x18);
            pbVar18 = pbVar18 + 1;
          } while (uVar8 != 0);
        }
      }
      else {
        if (uVar22 < 2) {
          if (uVar11 == 0) {
            uVar12 = local_64 & 0xc0 | uVar6 | uVar12 | uVar5;
            uVar6 = 0x1300;
          }
          else {
            uVar12 = local_64 & 0xc0 | uVar6 | uVar12 | uVar5 | (uint)*pbVar15 << 0x18;
            uVar6 = 0x2300;
          }
          goto LAB_ffff0000907505f0;
        }
        *(uint *)(lVar21 + 0x110) = uVar3 | 0x2902 | local_64 & 0xff0000c0 | uVar6 | uVar12;
        uVar7 = *(ulong *)(&DAT_ffff000090804d20 + uVar24 * 8);
        uVar12 = (int)uVar7 * 8;
        *(uint *)(uVar7 & 0xfffffffffffffffc) =
             *(uint *)(uVar7 & 0xfffffffffffffffc) & (0xff << (ulong)(uVar12 & 0x18) ^ 0xffffffffU)
             | uVar4 << (ulong)(uVar12 & 0x18);
        uVar8 = uVar16;
        pbVar18 = pbVar15;
        do {
          uVar12 = uVar12 + 8;
          uVar7 = uVar7 + 1;
          uVar8 = uVar8 - 1;
          *(uint *)(uVar7 & 0xfffffffffffffffc) =
               *(uint *)(uVar7 & 0xfffffffffffffffc) &
               (0xff << (ulong)(uVar12 & 0x18) ^ 0xffffffffU) |
               (uint)*pbVar18 << (ulong)(uVar12 & 0x18);
          pbVar18 = pbVar18 + 1;
        } while (uVar8 != 0);
      }
    }
    uVar24 = uVar24 + 1;
  } while (uVar24 != uVar2);
  if (param_1 == 0x2c) {
    if ((DAT_ffff0000324a0034 & 3) != 0) goto LAB_ffff000090750c6c;
  }
  else {
    if ((DAT_ffff000032490034 & 3) != 0) {
      if ((param_1 == 0x2d) || (param_1 == 0x2b)) {
        _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffeffff | 0x10000;
      }
      goto LAB_ffff000090750c6c;
    }
    if (param_1 == 0x2d) {
      _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
      if (((DAT_ffff000032490034 & 3) == 0) && (_DAT_ffff00003249000c < 0)) {
        iVar13 = 0;
        do {
          iVar13 = iVar13 + 1;
          uVar16 = uVar2;
          if (((uint)(iVar13 * 0x26e978d5) >> 3 | iVar13 * -0x60000000) < 0x418938) {
            do {
              uVar16 = uVar16 - 1;
            } while (uVar16 != 0);
          }
        } while (_DAT_ffff00003249000c < 0);
      }
      _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 & 0xffffffef;
      goto LAB_ffff000090750c6c;
    }
  }
  uVar16 = (ulong)(param_1 != 0x2b);
  puVar9 = *(uint **)(s_0123456789abcdef_ffff000090804c70 + uVar16 * 8 + 0x10);
  *puVar9 = *puVar9 & 0xfffffffe;
  *puVar9 = *puVar9 | 1;
  if (((*(byte *)((&DAT_ffff00009080cf90)[uVar16] + 0x34) & 3) == 0) && ((int)puVar9[3] < 0)) {
    iVar13 = 0;
    do {
      iVar13 = iVar13 + 1;
      lVar21 = uVar2 - uVar16;
      if (((uint)(iVar13 * 0x26e978d5) >> 3 | iVar13 * -0x60000000) < 0x418938) {
        do {
          lVar21 = lVar21 + -1;
        } while (lVar21 != 0);
      }
    } while ((int)puVar9[3] < 0);
  }
LAB_ffff000090750c6c:
  if (param_3[3] == 0) {
    plVar10 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar23 * 8 + 0x10);
    do {
      lVar14 = lVar14 + -1;
      *(uint *)(*plVar10 + 0x1d0) = *(uint *)(*plVar10 + 0x1d0) & 0xfffffffe;
      plVar10 = plVar10 + 1;
    } while (lVar14 != 0);
  }
  return;
}


