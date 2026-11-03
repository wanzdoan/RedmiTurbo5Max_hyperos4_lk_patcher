
undefined8 * FUN_ffff0000b8036e98(uint param_1,undefined8 param_2)

{
  bool bVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint *puVar6;
  uint uVar7;
  ulong uVar8;
  
  puVar6 = (uint *)FUN_ffff0000b803b000();
  uVar5 = FUN_ffff0000b803ad00(param_1);
  uVar8 = (ulong)uVar5;
  if ((DAT_ffff0000b81282d8 & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
    DAT_ffff0000b81282e4 = 0xf;
    DAT_ffff0000b81282d8 = 1;
  }
  FUN_ffff0000b804d070(&DAT_ffff0000b8127858,0,0xa80);
  DAT_ffff0000b8127858 = param_2;
  DAT_ffff0000b8127938 = param_1;
  if (DAT_ffff0000b81282d8 == 1) {
    if (0x13 < param_1) {
LAB_ffff0000b8037038:
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0xac,
                 s_scenario_>__0____scenario_<_DDP__ffff0000b8092b7a);
    }
    if (DAT_ffff0000b81282e4 == 0) {
      uVar7 = 0;
      goto LAB_ffff0000b8036fb4;
    }
  }
  else {
    FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
    DAT_ffff0000b81282e4 = 0xf;
    DAT_ffff0000b81282d8 = 1;
    if (0x13 < param_1) goto LAB_ffff0000b8037038;
  }
  uVar7 = 0;
  uVar4 = DAT_ffff0000b81282e4;
  do {
    if ((uVar4 & 1) != 0) {
      DAT_ffff0000b81282e4 = DAT_ffff0000b81282e4 & (1 << (ulong)(uVar7 & 0x1f) ^ 0xffffffffU);
      break;
    }
    uVar7 = uVar7 + 1;
    bVar1 = 1 < uVar4;
    uVar4 = (int)uVar4 >> 1;
  } while (bVar1);
  if (3 < uVar7) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0xb6,
               s_mutex_id_<_(DISP_MUTEX_DDP_FIRST_ffff0000b80901d6);
  }
LAB_ffff0000b8036fb4:
  DAT_ffff0000b8127860 = uVar7;
  FUN_ffff0000b804cd40(&DAT_ffff0000b812786c,&DAT_ffff0000b80edd58,0x28);
  if (0 < (int)uVar5) {
    do {
      uVar8 = uVar8 - 1;
      lVar2 = (ulong)*puVar6 * 4;
      iVar3 = *(int *)(&DAT_ffff0000b81282ec + lVar2);
      *(undefined8 **)(&DAT_ffff0000b81286f8 + (ulong)*puVar6 * 8) = &DAT_ffff0000b8127858;
      *(int *)(&DAT_ffff0000b81282ec + lVar2) = iVar3 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar8 != 0);
  }
  DAT_ffff0000b81282e0 = DAT_ffff0000b81282e0 + 1;
  (&DAT_ffff0000b8128f10)[DAT_ffff0000b8127860] = &DAT_ffff0000b8127858;
  return &DAT_ffff0000b8127858;
}


