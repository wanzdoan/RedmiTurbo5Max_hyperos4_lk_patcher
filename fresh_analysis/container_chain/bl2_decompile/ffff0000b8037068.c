
undefined8 FUN_ffff0000b8037068(undefined8 *param_1)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint *puVar4;
  ulong uVar5;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x161,
               s_dp_handle____NULL_ffff0000b8092b68);
  }
  puVar4 = (uint *)FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
  uVar3 = FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
  uVar5 = (ulong)uVar3;
  if ((DAT_ffff0000b81282d8 & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
    DAT_ffff0000b81282e4 = 0xf;
    DAT_ffff0000b81282d8 = 1;
  }
  if (3 < (int)*(uint *)(param_1 + 1)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0xc0,
               s_mutex_idx_<_(DISP_MUTEX_DDP_FIRS_ffff0000b809ec4d);
  }
  DAT_ffff0000b81282e4 = DAT_ffff0000b81282e4 | 1 << (ulong)(*(uint *)(param_1 + 1) & 0x1f);
  FUN_ffff0000b80371bc(*(undefined4 *)(param_1 + 0x1c),*param_1);
  if (0 < (int)uVar3) {
    do {
      uVar5 = uVar5 - 1;
      lVar1 = (ulong)*puVar4 * 4;
      iVar2 = *(int *)(&DAT_ffff0000b81282ec + lVar1);
      *(undefined8 *)(&DAT_ffff0000b81286f8 + (ulong)*puVar4 * 8) = 0;
      *(int *)(&DAT_ffff0000b81282ec + lVar1) = iVar2 + -1;
      puVar4 = puVar4 + 1;
    } while (uVar5 != 0);
  }
  if (DAT_ffff0000b81282e0 < 1) {
    DAT_ffff0000b81282e0 = DAT_ffff0000b81282e0 + -1;
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x173,
               s_content_>handle_cnt_>__0_ffff0000b808f3b8);
  }
  DAT_ffff0000b81282e0 = DAT_ffff0000b81282e0 + -1;
  (&DAT_ffff0000b8128f10)[*(uint *)(param_1 + 1)] = 0;
  return 0;
}


