
undefined8 * FUN_ffff000090755028(uint param_1,undefined8 param_2)

{
  bool bVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint *puVar6;
  uint uVar7;
  ulong uVar8;
  
  puVar6 = (uint *)FUN_ffff0000907593e8();
  uVar5 = FUN_ffff0000907590e8(param_1);
  uVar8 = (ulong)uVar5;
  if ((DAT_ffff00009087a998 & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
    DAT_ffff00009087a9a4 = 0xf;
    DAT_ffff00009087a998 = 1;
  }
  FUN_ffff00009077ee50(&DAT_ffff000090879f18,0,0xa80);
  DAT_ffff000090879f18 = param_2;
  DAT_ffff000090879ff8 = param_1;
  if (DAT_ffff00009087a998 == 1) {
    if (0x13 < param_1) {
LAB_ffff0000907551c8:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0xac,
                 s_scenario_>__0____scenario_<_DDP__ffff0000907e81b4);
    }
    if (DAT_ffff00009087a9a4 == 0) {
      uVar7 = 0;
      goto LAB_ffff000090755144;
    }
  }
  else {
    FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
    DAT_ffff00009087a9a4 = 0xf;
    DAT_ffff00009087a998 = 1;
    if (0x13 < param_1) goto LAB_ffff0000907551c8;
  }
  uVar7 = 0;
  uVar4 = DAT_ffff00009087a9a4;
  do {
    if ((uVar4 & 1) != 0) {
      DAT_ffff00009087a9a4 = DAT_ffff00009087a9a4 & (1 << (ulong)(uVar7 & 0x1f) ^ 0xffffffffU);
      break;
    }
    uVar7 = uVar7 + 1;
    bVar1 = 1 < uVar4;
    uVar4 = (int)uVar4 >> 1;
  } while (bVar1);
  if (3 < uVar7) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0xb6,
               s_mutex_id_<_(DISP_MUTEX_DDP_FIRST_ffff0000907e359d);
  }
LAB_ffff000090755144:
  DAT_ffff000090879f20 = uVar7;
  FUN_ffff00009077eb20(&DAT_ffff000090879f2c,&DAT_ffff00009081e508,0x28);
  if (0 < (int)uVar5) {
    do {
      uVar8 = uVar8 - 1;
      lVar2 = (ulong)*puVar6 * 4;
      iVar3 = *(int *)(&DAT_ffff00009087a9ac + lVar2);
      *(undefined8 **)(&DAT_ffff00009087adb8 + (ulong)*puVar6 * 8) = &DAT_ffff000090879f18;
      *(int *)(&DAT_ffff00009087a9ac + lVar2) = iVar3 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar8 != 0);
  }
  DAT_ffff00009087a9a0 = DAT_ffff00009087a9a0 + 1;
  (&DAT_ffff00009087b5d0)[DAT_ffff000090879f20] = &DAT_ffff000090879f18;
  return &DAT_ffff000090879f18;
}


