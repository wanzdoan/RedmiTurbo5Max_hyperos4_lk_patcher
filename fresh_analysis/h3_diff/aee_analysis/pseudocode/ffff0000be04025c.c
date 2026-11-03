
undefined8 * FUN_ffff0000be04025c(uint param_1,undefined8 param_2)

{
  bool bVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint *puVar6;
  uint uVar7;
  ulong uVar8;
  
  puVar6 = (uint *)FUN_ffff0000be0443c4();
  uVar5 = FUN_ffff0000be0440c4(param_1);
  uVar8 = (ulong)uVar5;
  if ((DAT_ffff0000be124ad8 & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
    DAT_ffff0000be124ae4 = 0xf;
    DAT_ffff0000be124ad8 = 1;
  }
  FUN_ffff0000be060ae0(&DAT_ffff0000be124058,0,0xa80);
  DAT_ffff0000be124058 = param_2;
  DAT_ffff0000be124138 = param_1;
  if (DAT_ffff0000be124ad8 == 1) {
    if (0x13 < param_1) {
LAB_ffff0000be0403fc:
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0xac,
                 s_scenario_>__0____scenario_<_DDP__ffff0000be0b464e);
    }
    if (DAT_ffff0000be124ae4 == 0) {
      uVar7 = 0;
      goto LAB_ffff0000be040378;
    }
  }
  else {
    FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
    DAT_ffff0000be124ae4 = 0xf;
    DAT_ffff0000be124ad8 = 1;
    if (0x13 < param_1) goto LAB_ffff0000be0403fc;
  }
  uVar7 = 0;
  uVar4 = DAT_ffff0000be124ae4;
  do {
    if ((uVar4 & 1) != 0) {
      DAT_ffff0000be124ae4 = DAT_ffff0000be124ae4 & (1 << (ulong)(uVar7 & 0x1f) ^ 0xffffffffU);
      break;
    }
    uVar7 = uVar7 + 1;
    bVar1 = 1 < uVar4;
    uVar4 = (int)uVar4 >> 1;
  } while (bVar1);
  if (3 < uVar7) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0xb6,
               s_mutex_id_<_(DISP_MUTEX_DDP_FIRST_ffff0000be0b11fa);
  }
LAB_ffff0000be040378:
  DAT_ffff0000be124060 = uVar7;
  FUN_ffff0000be0607b0(&DAT_ffff0000be12406c,&DAT_ffff0000be0dae28,0x28);
  if (0 < (int)uVar5) {
    do {
      uVar8 = uVar8 - 1;
      lVar2 = (ulong)*puVar6 * 4;
      iVar3 = *(int *)(&DAT_ffff0000be124aec + lVar2);
      *(undefined8 **)(&DAT_ffff0000be124ef8 + (ulong)*puVar6 * 8) = &DAT_ffff0000be124058;
      *(int *)(&DAT_ffff0000be124aec + lVar2) = iVar3 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar8 != 0);
  }
  DAT_ffff0000be124ae0 = DAT_ffff0000be124ae0 + 1;
  (&DAT_ffff0000be125710)[DAT_ffff0000be124060] = &DAT_ffff0000be124058;
  return &DAT_ffff0000be124058;
}


