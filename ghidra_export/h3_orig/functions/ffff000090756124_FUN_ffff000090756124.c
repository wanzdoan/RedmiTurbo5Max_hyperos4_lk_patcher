// ffff000090756124  size=336  FUN_ffff000090756124

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756124(undefined8 *param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  undefined8 uVar4;
  long lVar5;
  code *pcVar6;
  ulong uVar7;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff000090759150(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0x1c));
    lVar1 = DAT_ffff0000908399a8;
    if (0 < (int)uVar2) {
      uVar7 = (ulong)uVar2;
      do {
        lVar5 = *(long *)(lVar1 + (ulong)*puVar3 * 8);
        if ((lVar5 != 0) && (pcVar6 = *(code **)(lVar5 + 0x58), pcVar6 != (code *)0x0)) {
          if (param_2 == 0) {
            uVar4 = 0;
          }
          else {
            uVar4 = *param_1;
          }
          (*pcVar6)((ulong)*puVar3,uVar4);
        }
        uVar7 = uVar7 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar7 != 0);
    }
    *(undefined4 *)((long)param_1 + 0xc) = 0;
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
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x4c9,
             s_dp_handle____NULL_ffff0000907e805e);
}


