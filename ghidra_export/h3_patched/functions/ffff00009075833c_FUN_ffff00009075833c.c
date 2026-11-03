// ffff00009075833c  size=944  FUN_ffff00009075833c

undefined8 FUN_ffff00009075833c(undefined4 param_1,int *param_2)

{
  uint uVar1;
  undefined4 *puVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  uint uVar8;
  long lVar9;
  ulong uVar10;
  uint uVar11;
  ulong uVar12;
  
  lVar6 = FUN_ffff0000907578a0();
  iVar3 = FUN_ffff000090757aec(param_1);
  lVar9 = 0xffffcd7b0000;
  if (iVar3 != 0) {
    lVar9 = 0xffffcd3b0000;
  }
  iVar3 = FUN_ffff000090757aec(param_1);
  if (param_2[3] != 0) {
    uVar11 = param_2[0x24b];
    if ((char)param_2[0xb6] == '\0') {
      uVar8 = param_2[0x24a];
    }
    else {
      uVar8 = param_2[0xb2];
    }
    iVar4 = FUN_ffff000090757aec(param_1);
    iVar5 = FUN_ffff000090757aec(param_1);
    if (0xfff < (uVar11 | uVar8)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907f18b6,0x1a9,&DAT_ffff0000907d24c9);
    }
    lVar7 = 0xffffcd7b0000;
    if (iVar4 != 0) {
      lVar7 = 0xffffcd3b0000;
    }
    puVar2 = (undefined4 *)
             ((lVar7 + lVar6 & 0xffffffff0000U) + ((ulong)(uint)(iVar5 << 0x16) - 0xffffcd7affe8));
    puVar2[6] = uVar8 | uVar11 << 0x10;
    *puVar2 = 0xff000000;
  }
  if (*param_2 != 0) {
    uVar11 = param_2[4];
    if ((uVar11 & 1) == 0) {
      uVar8 = 0;
    }
    else if ((uVar11 >> 1 & 1) == 0) {
      uVar8 = 1;
    }
    else if ((uVar11 >> 2 & 1) == 0) {
      uVar8 = 2;
    }
    else if ((uVar11 >> 3 & 1) == 0) {
      uVar8 = 3;
    }
    else if ((uVar11 >> 4 & 1) == 0) {
      uVar8 = 4;
    }
    else {
      uVar8 = 5;
      if ((uVar11 & 0x20) != 0) {
        uVar8 = 6;
      }
    }
    lVar7 = FUN_ffff0000907586ec(param_1);
    if (6U - lVar7 < (ulong)uVar8) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907f18b6,0x71d,&DAT_ffff0000907d24c9);
    }
    FUN_ffff0000907586ec(param_1);
    uVar12 = 0;
    uVar11 = 0;
    do {
      uVar1 = uVar8 + (uint)uVar12;
      uVar10 = (ulong)uVar1;
      iVar4 = param_2[uVar10 * 0x1a + 7];
      param_2[4] = param_2[4] | 1 << (ulong)(uVar1 & 0x1f);
      if (iVar4 != 0) {
        if (((param_2[(ulong)uVar1 * 0x1a + 10] == 0) || (param_2[uVar10 * 0x1a + 0x13] == 0)) ||
           (param_2[(ulong)uVar1 * 0x1a + 0x14] == 0)) {
          return 0xffffffff;
        }
        FUN_ffff000090757bd8
                  (param_1,param_2[uVar10 * 0x1a + 6],param_2[uVar10 * 0x1a + 8],
                   param_2[uVar10 * 0x1a + 9],param_2[(ulong)uVar1 * 0x1a + 10],
                   param_2[uVar10 * 0x1a + 0xc],param_2[uVar10 * 0x1a + 0xd],
                   param_2[uVar10 * 0x1a + 0x10],param_2[uVar10 * 0x1a + 0x11],
                   param_2[uVar10 * 0x1a + 0x12],param_2[uVar10 * 0x1a + 0x13],
                   param_2[(ulong)uVar1 * 0x1a + 0x14],param_2[uVar10 * 0x1a + 0x15],
                   param_2[uVar10 * 0x1a + 0x16],param_2[uVar10 * 0x1a + 0x17],
                   (char)param_2[uVar10 * 0x1a + 0x18]);
        uVar11 = 1 << (ulong)((uint)uVar12 & 0x1f) | uVar11;
      }
      uVar10 = FUN_ffff0000907586ec(param_1);
      uVar12 = uVar12 + 1;
    } while (uVar12 < uVar10);
    *(uint *)(((ulong)(uint)(iVar3 << 0x16) | 0xffff000032850018) +
              (lVar9 + lVar6 & 0xffffffff0000U) + 0x14) = uVar11;
    lVar9 = FUN_ffff0000907586ec(param_1);
    *(undefined4 *)(lVar6 + 0x108) = 0x3ff03ff;
    if (((lVar9 != 1) && (*(undefined4 *)(lVar6 + 0x128) = 0x3ff03ff, lVar9 != 2)) &&
       (*(undefined4 *)(lVar6 + 0x148) = 0x3ff03ff, lVar9 != 3)) {
      *(undefined4 *)(lVar6 + 0x168) = 0x3ff03ff;
    }
    *(undefined4 *)(lVar6 + 0x110) = 0x6000000;
    if (((lVar9 != 1) && (*(undefined4 *)(lVar6 + 0x130) = 0x6000000, lVar9 != 2)) &&
       (*(undefined4 *)(lVar6 + 0x150) = 0x6000000, lVar9 != 3)) {
      *(undefined4 *)(lVar6 + 0x170) = 0x6000000;
    }
    *(undefined4 *)(lVar6 + 0x1e0) = 0x20ff01ff;
    if (((lVar9 != 1) && (*(undefined4 *)(lVar6 + 0x1e4) = 0x20ff01ff, lVar9 != 2)) &&
       (*(undefined4 *)(lVar6 + 0x1e8) = 0x20ff01ff, lVar9 != 3)) {
      *(undefined4 *)(lVar6 + 0x1ec) = 0x20ff01ff;
    }
    *(undefined4 *)(lVar6 + 0x1f8) = 0xf1ffffff;
    *(undefined4 *)(lVar6 + 0x1fc) = 0xffff;
    *(undefined4 *)(lVar6 + 0x20c) = 0x8040;
    *(undefined4 *)(lVar6 + 0x210) = 0;
    if (((lVar9 != 1) && (*(undefined4 *)(lVar6 + 0x214) = 0, lVar9 != 2)) &&
       (*(undefined4 *)(lVar6 + 0x218) = 0, lVar9 != 3)) {
      *(undefined4 *)(lVar6 + 0x21c) = 0;
    }
    *(undefined4 *)(lVar6 + 0x220) = 0x80000000;
    if (((lVar9 != 1) && (*(undefined4 *)(lVar6 + 0x224) = 0x80000000, lVar9 != 2)) &&
       (*(undefined4 *)(lVar6 + 0x228) = 0x80000000, lVar9 != 3)) {
      *(undefined4 *)(lVar6 + 0x22c) = 0x80000000;
    }
    *(uint *)(lVar6 + 0x20) = *(uint *)(lVar6 + 0x20) & 0xfff3ffff;
    *(undefined4 *)(lVar6 + 0xae0) = 0;
    *(undefined4 *)(lVar6 + 0xae4) = 1;
  }
  return 0;
}


