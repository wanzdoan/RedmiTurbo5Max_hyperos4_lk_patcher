
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090750d38
               (int param_1,undefined8 param_2,undefined4 *param_3,uint param_4,char param_5)

{
  ulong uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  int iVar4;
  ulong uVar5;
  undefined4 *puVar6;
  long lVar7;
  uint uVar8;
  uint *puVar9;
  ulong uVar10;
  
  FUN_ffff000090754cc8();
  uVar8 = 0;
  if (param_1 != 0x2b) {
    uVar8 = (uint)(param_1 != 0x2d);
  }
  uVar10 = (ulong)uVar8;
  uVar1 = 1;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  uVar2 = uVar10;
  do {
    if ((*(byte *)((&DAT_ffff00009080cf90)[uVar2] + 0x34) & 3) == 0) {
      if (0x20 < param_4) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0x124e,
                   s_queue_size_<__32_ffff0000907e7e46);
      }
      if (((*(byte *)((&DAT_ffff00009080cf90)[uVar10] + 0x34) & 3) == 0) &&
         (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10) + 0xc) < 0)) {
        iVar4 = 0;
        do {
          iVar4 = iVar4 + 1;
          lVar7 = uVar1 - uVar10;
          if (((uint)(iVar4 * 0x26e978d5) >> 3 | iVar4 * -0x60000000) < 0x418938) {
            do {
              lVar7 = lVar7 + -1;
            } while (lVar7 != 0);
          }
        } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10) + 0xc)
                 < 0);
      }
      if (param_4 != 0) {
        puVar3 = *(undefined4 **)(&DAT_ffff000090804cf0 + uVar2 * 8);
        uVar5 = (ulong)param_4;
        puVar6 = param_3;
        do {
          uVar5 = uVar5 - 1;
          *puVar3 = *puVar6;
          puVar3 = puVar3 + 1;
          puVar6 = puVar6 + 1;
        } while (uVar5 != 0);
      }
      *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar2 * 8 + 0x10) + 0x44) =
           param_4 | 0x8000;
    }
    uVar2 = uVar2 + 1;
  } while (uVar2 != uVar1);
  if (param_1 == 0x2c) {
    if (param_5 == '\0') {
      return;
    }
    if ((_DAT_ffff0000324a0034 & 3) != 0) {
      return;
    }
  }
  else {
    if (param_5 == '\0') {
      return;
    }
    if ((_DAT_ffff000032490034 & 3) != 0) {
      return;
    }
    if (param_1 == 0x2d) {
      if (((_DAT_ffff000032490034 & 3) == 0) && (_DAT_ffff00003249000c < 0)) {
        iVar4 = 0;
        do {
          iVar4 = iVar4 + 1;
          uVar10 = uVar1;
          if (((uint)(iVar4 * 0x26e978d5) >> 3 | iVar4 * -0x60000000) < 0x418938) {
            do {
              uVar10 = uVar10 - 1;
            } while (uVar10 != 0);
          }
        } while (_DAT_ffff00003249000c < 0);
      }
      _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
      _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 & 0xffffffef;
      return;
    }
  }
  uVar10 = (ulong)(param_1 != 0x2b);
  puVar9 = *(uint **)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
  *puVar9 = *puVar9 & 0xfffffffe;
  *puVar9 = *puVar9 | 1;
  if (((*(byte *)((&DAT_ffff00009080cf90)[uVar10] + 0x34) & 3) == 0) && ((int)puVar9[3] < 0)) {
    iVar4 = 0;
    do {
      iVar4 = iVar4 + 1;
      lVar7 = uVar1 - uVar10;
      if (((uint)(iVar4 * 0x26e978d5) >> 3 | iVar4 * -0x60000000) < 0x418938) {
        do {
          lVar7 = lVar7 + -1;
        } while (lVar7 != 0);
      }
    } while ((int)puVar9[3] < 0);
  }
  return;
}


