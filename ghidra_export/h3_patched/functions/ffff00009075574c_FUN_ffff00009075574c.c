// ffff00009075574c  size=428  FUN_ffff00009075574c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075574c(undefined8 *param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  ulong uVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x282,
               s_dp_handle____NULL_ffff0000907e8047);
  }
  puVar3 = (uint *)FUN_ffff000090759150(*(undefined4 *)(param_1 + 0x1c));
  uVar2 = FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0x1c));
  uVar7 = (ulong)uVar2;
  if (param_2 == 0) {
    uVar6 = 0;
  }
  else {
    uVar6 = *param_1;
  }
  FUN_ffff00009075a244(*(undefined4 *)(param_1 + 1),uVar6);
  uVar4 = FUN_ffff000090758e24(*(undefined4 *)(param_1 + 0x1c));
  if ((uVar4 & 1) != 0) {
    FUN_ffff00009075a29c(*(undefined4 *)(param_1 + 1),uVar6);
  }
  FUN_ffff0000907550b4(*(undefined4 *)(param_1 + 0x1c),uVar6);
  lVar1 = DAT_ffff0000908399a8;
  if (0 < (int)uVar2) {
    do {
      uVar4 = (ulong)*puVar3;
      lVar5 = *(long *)(lVar1 + uVar4 * 8);
      if (lVar5 != 0) {
        if (*(code **)(lVar5 + 0x10) != (code *)0x0) {
          (**(code **)(lVar5 + 0x10))(uVar4,uVar6);
          lVar5 = *(long *)(lVar1 + uVar4 * 8);
        }
        if (*(code **)(lVar5 + 0x98) != (code *)0x0) {
          (**(code **)(lVar5 + 0x98))(uVar4,0);
        }
      }
      uVar7 = uVar7 - 1;
      puVar3 = puVar3 + 1;
    } while (uVar7 != 0);
  }
  *(undefined4 *)((long)param_1 + 0xc) = 0;
  FUN_ffff000090758c5c();
  if ((DAT_ffff00009087a900 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087a908,0,0xc50);
    _DAT_ffff00009087a90c = 0xf;
    DAT_ffff00009087a900 = 1;
  }
  if ((((_DAT_ffff00009087a910 != 0) &&
       ((_DAT_ffff00009087b538 == 0 || (*(int *)(_DAT_ffff00009087b538 + 0xc) == 0)))) &&
      ((_DAT_ffff00009087b540 == 0 || (*(int *)(_DAT_ffff00009087b540 + 0xc) == 0)))) &&
     (((_DAT_ffff00009087b548 == 0 || (*(int *)(_DAT_ffff00009087b548 + 0xc) == 0)) &&
      ((_DAT_ffff00009087b550 == 0 || (*(int *)(_DAT_ffff00009087b550 + 0xc) == 0)))))) {
    _DAT_ffff00009087a910 = 0;
    FUN_ffff00009075a460();
  }
  return 0;
}


