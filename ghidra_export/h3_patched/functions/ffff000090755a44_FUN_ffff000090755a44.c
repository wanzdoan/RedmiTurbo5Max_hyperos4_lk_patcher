// ffff000090755a44  size=188  FUN_ffff000090755a44

undefined8 FUN_ffff000090755a44(undefined8 *param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  long lVar4;
  code *pcVar5;
  undefined8 uVar6;
  ulong uVar7;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff000090759150(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0x1c));
    lVar1 = DAT_ffff0000908399a8;
    if (param_2 == 0) {
      uVar6 = 0;
    }
    else {
      uVar6 = *param_1;
    }
    if (0 < (int)uVar2) {
      uVar7 = (ulong)uVar2;
      do {
        lVar4 = *(long *)(lVar1 + (ulong)*puVar3 * 8);
        if ((lVar4 != 0) && (pcVar5 = *(code **)(lVar4 + 0x38), pcVar5 != (code *)0x0)) {
          (*pcVar5)((ulong)*puVar3,uVar6);
        }
        uVar7 = uVar7 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar7 != 0);
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x2d2,
             s_dp_handle____NULL_ffff0000907e8047);
}


