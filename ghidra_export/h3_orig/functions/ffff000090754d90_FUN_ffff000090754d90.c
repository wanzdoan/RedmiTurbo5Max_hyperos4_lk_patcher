// ffff000090754d90  size=464  FUN_ffff000090754d90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090754d90(uint param_1,undefined8 param_2)

{
  bool bVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint *puVar6;
  uint uVar7;
  ulong uVar8;
  
  puVar6 = (uint *)FUN_ffff000090759150();
  uVar5 = FUN_ffff000090758e50(param_1);
  uVar8 = (ulong)uVar5;
  if ((DAT_ffff00009087a900 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087a908,0,0xc50);
    _DAT_ffff00009087a90c = 0xf;
    DAT_ffff00009087a900 = 1;
  }
  FUN_ffff00009077eb88(&DAT_ffff000090879e80,0,0xa80);
  _DAT_ffff000090879e80 = param_2;
  _DAT_ffff000090879f60 = param_1;
  if (DAT_ffff00009087a900 == 1) {
    if (0x13 < param_1) {
LAB_ffff000090754f30:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0xac,
                 s_scenario_>__0____scenario_<_DDP__ffff0000907e8070);
    }
    if (_DAT_ffff00009087a90c == 0) {
      uVar7 = 0;
      goto LAB_ffff000090754eac;
    }
  }
  else {
    FUN_ffff00009077eb88(&DAT_ffff00009087a908,0,0xc50);
    _DAT_ffff00009087a90c = 0xf;
    DAT_ffff00009087a900 = 1;
    if (0x13 < param_1) goto LAB_ffff000090754f30;
  }
  uVar7 = 0;
  uVar4 = _DAT_ffff00009087a90c;
  do {
    if ((uVar4 & 1) != 0) {
      _DAT_ffff00009087a90c = _DAT_ffff00009087a90c & (1 << (ulong)(uVar7 & 0x1f) ^ 0xffffffffU);
      break;
    }
    uVar7 = uVar7 + 1;
    bVar1 = 1 < uVar4;
    uVar4 = (int)uVar4 >> 1;
  } while (bVar1);
  if (3 < uVar7) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0xb6,
               s_mutex_id_<_(DISP_MUTEX_DDP_FIRST_ffff0000907e34cc);
  }
LAB_ffff000090754eac:
  _DAT_ffff000090879e88 = uVar7;
  FUN_ffff00009077e858(0xffff000090879e94,&DAT_ffff00009081e4e8,0x28);
  if (0 < (int)uVar5) {
    do {
      uVar8 = uVar8 - 1;
      lVar2 = (ulong)*puVar6 * 4;
      iVar3 = *(int *)(lVar2 + -0xffff6f7856ec);
      *(undefined1 **)((ulong)*puVar6 * 8 + -0xffff6f7852e0) = &DAT_ffff000090879e80;
      *(int *)(lVar2 + -0xffff6f7856ec) = iVar3 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar8 != 0);
  }
  _DAT_ffff00009087a908 = _DAT_ffff00009087a908 + 1;
  *(undefined1 **)(&DAT_ffff00009087b538 + (ulong)_DAT_ffff000090879e88 * 8) = &DAT_ffff000090879e80
  ;
  return &DAT_ffff000090879e80;
}


