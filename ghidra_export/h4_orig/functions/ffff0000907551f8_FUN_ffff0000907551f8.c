// ffff0000907551f8  size=340  FUN_ffff0000907551f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907551f8(undefined8 *param_1)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint *puVar4;
  ulong uVar5;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x161,
               s_dp_handle____NULL_ffff0000907e81a2);
  }
  puVar4 = (uint *)FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0x1c));
  uVar3 = FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0x1c));
  uVar5 = (ulong)uVar3;
  if ((DAT_ffff00009087a998 & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
    _DAT_ffff00009087a9a4 = 0xf;
    DAT_ffff00009087a998 = 1;
  }
  if (3 < (int)*(uint *)(param_1 + 1)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0xc0,
               s_mutex_idx_<_(DISP_MUTEX_DDP_FIRS_ffff0000907fddf0);
  }
  _DAT_ffff00009087a9a4 = _DAT_ffff00009087a9a4 | 1 << (ulong)(*(uint *)(param_1 + 1) & 0x1f);
  FUN_ffff00009075534c(*(undefined4 *)(param_1 + 0x1c),*param_1);
  if (0 < (int)uVar3) {
    do {
      uVar5 = uVar5 - 1;
      lVar1 = (ulong)*puVar4 * 4;
      iVar2 = *(int *)(lVar1 + -0xffff6f785654);
      *(undefined8 *)((ulong)*puVar4 * 8 + -0xffff6f785248) = 0;
      *(int *)(lVar1 + -0xffff6f785654) = iVar2 + -1;
      puVar4 = puVar4 + 1;
    } while (uVar5 != 0);
  }
  if (_DAT_ffff00009087a9a0 < 1) {
    _DAT_ffff00009087a9a0 = _DAT_ffff00009087a9a0 + -1;
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x173,
               s_content_>handle_cnt_>__0_ffff0000907e1bdc);
  }
  _DAT_ffff00009087a9a0 = _DAT_ffff00009087a9a0 + -1;
  *(undefined8 *)(&DAT_ffff00009087b5d0 + (ulong)*(uint *)(param_1 + 1) * 8) = 0;
  return 0;
}


