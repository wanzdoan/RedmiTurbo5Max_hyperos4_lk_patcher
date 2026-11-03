// ffff0000907558f8  size=332  FUN_ffff0000907558f8

undefined8 FUN_ffff0000907558f8(undefined8 *param_1,int param_2)

{
  undefined4 uVar1;
  long lVar2;
  undefined4 uVar3;
  uint uVar4;
  ulong uVar5;
  uint *puVar6;
  long lVar7;
  code *pcVar8;
  undefined8 uVar9;
  ulong uVar10;
  
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff000090759150(*(undefined4 *)(param_1 + 0x1c));
    FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0x1c));
    if (param_2 == 0) {
      uVar9 = 0;
    }
    else {
      uVar9 = *param_1;
    }
    uVar1 = *(undefined4 *)(param_1 + 0x1c);
    uVar5 = FUN_ffff000090758e24(uVar1);
    if ((uVar5 & 1) == 0) {
      uVar3 = 0x14;
    }
    else {
      uVar3 = FUN_ffff000090758e38(uVar1);
    }
    puVar6 = (uint *)FUN_ffff000090759150(uVar1);
    uVar4 = FUN_ffff000090758e50(uVar1);
    lVar2 = DAT_ffff0000908399a8;
    if (0 < (int)uVar4) {
      uVar10 = (ulong)uVar4;
      do {
        lVar7 = *(long *)(lVar2 + (ulong)*puVar6 * 8);
        if ((lVar7 != 0) && (pcVar8 = *(code **)(lVar7 + 0x28), pcVar8 != (code *)0x0)) {
          (*pcVar8)((ulong)*puVar6,uVar9);
        }
        uVar10 = uVar10 - 1;
        puVar6 = puVar6 + 1;
      } while (uVar10 != 0);
    }
    if ((uVar5 & 1) != 0) {
      puVar6 = (uint *)FUN_ffff000090759150(uVar3);
      uVar4 = FUN_ffff000090758e50(uVar3);
      lVar2 = DAT_ffff0000908399a8;
      if (0 < (int)uVar4) {
        uVar5 = (ulong)uVar4;
        do {
          lVar7 = *(long *)(lVar2 + (ulong)*puVar6 * 8);
          if ((lVar7 != 0) && (pcVar8 = *(code **)(lVar7 + 0x28), pcVar8 != (code *)0x0)) {
            (*pcVar8)((ulong)*puVar6,uVar9);
          }
          uVar5 = uVar5 - 1;
          puVar6 = puVar6 + 1;
        } while (uVar5 != 0);
      }
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x2ab,
             s_dp_handle____NULL_ffff0000907e8047);
}


