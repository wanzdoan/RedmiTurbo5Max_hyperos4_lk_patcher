
undefined8 FUN_ffff0000be04042c(undefined8 *param_1)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint *puVar4;
  ulong uVar5;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x161,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  puVar4 = (uint *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0x1c));
  uVar3 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0x1c));
  uVar5 = (ulong)uVar3;
  if ((DAT_ffff0000be124ad8 & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
    DAT_ffff0000be124ae4 = 0xf;
    DAT_ffff0000be124ad8 = 1;
  }
  if (3 < (int)*(uint *)(param_1 + 1)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0xc0,
               s_mutex_idx_<_(DISP_MUTEX_DDP_FIRS_ffff0000be0c330b);
  }
  DAT_ffff0000be124ae4 = DAT_ffff0000be124ae4 | 1 << (ulong)(*(uint *)(param_1 + 1) & 0x1f);
  FUN_ffff0000be040580(*(undefined4 *)(param_1 + 0x1c),*param_1);
  if (0 < (int)uVar3) {
    do {
      uVar5 = uVar5 - 1;
      lVar1 = (ulong)*puVar4 * 4;
      iVar2 = *(int *)(&DAT_ffff0000be124aec + lVar1);
      *(undefined8 *)(&DAT_ffff0000be124ef8 + (ulong)*puVar4 * 8) = 0;
      *(int *)(&DAT_ffff0000be124aec + lVar1) = iVar2 + -1;
      puVar4 = puVar4 + 1;
    } while (uVar5 != 0);
  }
  if (DAT_ffff0000be124ae0 < 1) {
    DAT_ffff0000be124ae0 = DAT_ffff0000be124ae0 + -1;
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x173,
               s_content_>handle_cnt_>__0_ffff0000be0b00f1);
  }
  DAT_ffff0000be124ae0 = DAT_ffff0000be124ae0 + -1;
  (&DAT_ffff0000be125710)[*(uint *)(param_1 + 1)] = 0;
  return 0;
}


