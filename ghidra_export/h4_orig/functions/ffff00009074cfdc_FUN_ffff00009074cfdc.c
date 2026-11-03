// ffff00009074cfdc  size=776  FUN_ffff00009074cfdc

void FUN_ffff00009074cfdc(int param_1,undefined8 param_2,int *param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  uint uVar7;
  long *plVar8;
  int *piVar9;
  ulong uVar10;
  long lVar11;
  int iVar12;
  long lVar13;
  
  uVar7 = 0;
  if (param_1 != 0x2b) {
    uVar7 = (uint)(param_1 != 0x2d);
  }
  uVar10 = (ulong)uVar7;
  uVar7 = param_3[0xc];
  lVar11 = 1;
  if (param_1 != 0x2b) {
    lVar11 = 2;
  }
  uVar1 = param_3[0xd] - 8;
  plVar8 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
  piVar9 = (int *)(uVar10 * 0xbd8 + -0xffff6f787d7c);
  lVar11 = lVar11 - uVar10;
  do {
    if (uVar7 < 8) {
      iVar12 = *(int *)(&DAT_ffff000090803fd0 + (long)(int)uVar7 * 4);
    }
    else {
      iVar12 = 0;
    }
    if ((uVar1 < 5) && (iVar12 = 0, uVar1 < 5)) {
      iVar12 = *(int *)(&DAT_ffff00009080cfc4 + (long)(int)uVar1 * 4);
    }
    lVar13 = *plVar8;
    *(int *)(lVar13 + 0x60) = param_3[0x11];
    *(int *)(lVar13 + 100) = param_3[0x12];
    *(int *)(lVar13 + 0x68) = param_3[0x13];
    *(int *)(lVar13 + 0x6c) = param_3[0x15];
    uVar6 = param_3[0x16] * iVar12;
    if (*param_3 == 1) {
      if (param_3[0x16a] != 0) {
        if (uVar6 < 10) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x685,
                     s_dsi_params_>horizontal_sync_acti_ffff0000907f3069);
        }
        uVar4 = param_3[0x17] * iVar12;
        if (uVar4 < 10) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x688,
                     s_dsi_params_>horizontal_backporch_ffff0000907ee62b);
        }
        iVar5 = -10;
        goto LAB_ffff00009074d108;
      }
      if (uVar6 < 10) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x68b,
                   s_dsi_params_>horizontal_sync_acti_ffff0000907f3069);
      }
      uVar4 = param_3[0x17] * iVar12;
      if (uVar4 < 10) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x68e,
                   s_dsi_params_>horizontal_backporch_ffff0000907ee62b);
      }
      iVar5 = -7;
LAB_ffff00009074d154:
      if ((uint)(param_3[0x18] * iVar12) < 0xc) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x6ac,
                   s_dsi_params_>horizontal_frontporc_ffff0000907f8758);
      }
      uVar6 = uVar6 + iVar5 & 0xfffffffc;
      uVar4 = uVar4 - 7 & 0xfffffffc;
      uVar3 = param_3[0x18] * iVar12 - 9U & 0xfffffffc;
      uVar2 = param_3[0x1b] * iVar12 + 3U & 0xfffffffc;
    }
    else {
      uVar4 = (param_3[0x17] + param_3[0x16]) * iVar12;
      if (param_3[0x16a] == 0) {
        if (uVar4 < 10) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x6a0,
                     s__dsi_params_>horizontal_backporc_ffff0000907d30d4);
        }
        iVar5 = -1;
        goto LAB_ffff00009074d154;
      }
      if (uVar4 < 10) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x697,
                   s__dsi_params_>horizontal_backporc_ffff0000907d30d4);
      }
      iVar5 = -4;
LAB_ffff00009074d108:
      if ((uint)(param_3[0x18] * iVar12) < 0xc) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x6a7,
                   s_dsi_params_>horizontal_frontporc_ffff0000907f8758);
      }
      uVar4 = uVar4 - 10;
      uVar6 = uVar6 + iVar5;
      uVar3 = param_3[0x18] * iVar12 - 0xc;
      uVar2 = param_3[0x1b] * iVar12;
    }
    *(uint *)(lVar13 + 0x80) = uVar6 + 3 & 0xfffffffc;
    *(uint *)(lVar13 + 0x84) = uVar4 + 3 & 0xfffffffc;
    *(uint *)(lVar13 + 0x88) = uVar3 + 3 & 0xfffffffc;
    *(uint *)(lVar13 + 0x8c) = uVar2 + 3 & 0xfffffffc;
    if (param_3[0x168] != 0) {
      uVar6 = *(uint *)(lVar13 + 0x600);
      iVar12 = -4;
      if (*piVar9 - 1U < 3) {
        iVar12 = -*piVar9;
      }
      *(uint *)(lVar13 + 0x88) = *(uint *)(lVar13 + 0x88) | 0x80000000;
      *(uint *)(lVar13 + 0x88) =
           *(uint *)(lVar13 + 0x88) & 0x80000000 |
           *(uint *)(lVar13 + 0x88) & 0xffff |
           (*(int *)(lVar13 + 0x3c) +
            ((*(uint *)(lVar13 + 0x604) >> 0x18) + (uVar6 & 0xff) + (uVar6 >> 8 & 0xff) +
             (uVar6 >> 0x10 & 0xff) + 2) * iVar12 & 0x7fff) << 0x10;
    }
    plVar8 = plVar8 + 1;
    piVar9 = piVar9 + 0x2f6;
    lVar11 = lVar11 + -1;
    if (lVar11 == 0) {
      return;
    }
  } while( true );
}


