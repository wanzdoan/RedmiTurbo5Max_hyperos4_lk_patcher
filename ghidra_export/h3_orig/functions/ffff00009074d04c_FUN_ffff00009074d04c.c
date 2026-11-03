// ffff00009074d04c  size=560  FUN_ffff00009074d04c

void FUN_ffff00009074d04c(int param_1,undefined8 param_2,uint *param_3)

{
  int *piVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  int iVar8;
  long lVar9;
  int iVar10;
  int iVar11;
  uint uVar12;
  long *plVar13;
  int *piVar14;
  ulong uVar15;
  long lVar16;
  
  uVar12 = 0;
  if (param_1 != 0x2b) {
    uVar12 = (uint)(param_1 != 0x2d);
  }
  uVar15 = (ulong)uVar12;
  uVar12 = param_3[0xc];
  lVar16 = 1;
  if (param_1 != 0x2b) {
    lVar16 = 2;
  }
  uVar7 = param_3[0xd] - 8;
  plVar13 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar15 * 8 + 0x10);
  piVar14 = (int *)(uVar15 * 0xbd8 + -0xffff6f787e14);
  lVar16 = lVar16 - uVar15;
  iVar11 = 0;
  do {
    if (uVar12 < 8) {
      iVar8 = *(int *)(&DAT_ffff000090803da8 + (long)(int)uVar12 * 4);
    }
    else {
      iVar8 = 0;
    }
    if ((uVar7 < 5) && (iVar8 = 0, uVar7 < 5)) {
      iVar8 = *(int *)(&DAT_ffff00009080cd9c + (long)(int)uVar7 * 4);
    }
    lVar9 = *plVar13;
    iVar4 = *piVar14;
    *(uint *)(lVar9 + 0x60) = param_3[0x11];
    if (3 < iVar4 - 1U) {
      iVar4 = iVar11;
    }
    *(uint *)(lVar9 + 100) = param_3[0x12];
    *(uint *)(lVar9 + 0x68) = param_3[0x13];
    *(uint *)(lVar9 + 0x6c) = param_3[0x15];
    uVar5 = param_3[0x16] * iVar8;
    if (((*param_3 & 0xfffffffe) == 2) || ((param_3[1] & 0xfffffffe) == 2)) {
      uVar6 = (param_3[0x17] + param_3[0x16]) * iVar8;
      if (uVar6 < 10) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0x721,
                   s__dsi_params_>horizontal_backporc_ffff0000907d3097);
      }
      iVar11 = uVar5 - 4;
      iVar10 = uVar6 - 10;
    }
    else {
      iVar10 = 4;
      iVar11 = 4;
      if (iVar4 * 10 + 0x1fU <= uVar5) {
        iVar11 = uVar5 + iVar4 * -10 + -0x1a;
      }
      if (iVar4 * 0xc + 0x1fU <= param_3[0x17] * iVar8) {
        iVar10 = param_3[0x17] * iVar8 + iVar4 * -0xc + -0x1a;
      }
    }
    iVar3 = piVar14[0x168];
    piVar1 = piVar14 + 0x167;
    uVar6 = param_3[0x18];
    plVar13 = plVar13 + 1;
    piVar14 = piVar14 + 0x2f6;
    iVar2 = iVar4 * 10 + *piVar1 * iVar4 * 2;
    *(uint *)(lVar9 + 0x80) = iVar11 + 1U & 0xfffffffe;
    iVar11 = (iVar3 * 2 + 2) * iVar4 + iVar4 * -6;
    *(uint *)(lVar9 + 0x84) = iVar10 + 1U & 0xfffffffe;
    uVar5 = iVar11 - 0xe;
    if (iVar2 + iVar11 + 0xeU <= uVar6 * iVar8) {
      uVar5 = (uVar6 * iVar8 - iVar2) - 0x1c;
    }
    if (uVar5 < 9) {
      uVar5 = 8;
    }
    lVar16 = lVar16 + -1;
    *(uint *)(lVar9 + 0x88) = uVar5 + 1 & 0xfffffffe;
    *(int *)(lVar9 + 0x8c) = iVar4 << 4;
    iVar11 = iVar4;
  } while (lVar16 != 0);
  return;
}


