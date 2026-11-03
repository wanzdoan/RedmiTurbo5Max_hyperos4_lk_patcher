// ffff0000907565b0  size=184  FUN_ffff0000907565b0

undefined8 FUN_ffff0000907565b0(long param_1,long param_2,undefined8 param_3)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  long lVar4;
  code *pcVar5;
  ulong uVar6;
  
  if (param_1 != 0) {
    puVar3 = (uint *)FUN_ffff000090759150(*(undefined4 *)(param_1 + 0xe0));
    uVar2 = FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0xe0));
    lVar1 = DAT_ffff0000908399a8;
    if (0 < (int)uVar2) {
      uVar6 = (ulong)uVar2;
      do {
        lVar4 = *(long *)(lVar1 + (ulong)*puVar3 * 8);
        if (((lVar4 != 0) && (param_2 != 0)) &&
           (pcVar5 = *(code **)(lVar4 + 0x90), pcVar5 != (code *)0x0)) {
          (*pcVar5)((ulong)*puVar3,param_2,param_3);
        }
        uVar6 = uVar6 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar6 != 0);
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x5b6,
             s_dp_handle____NULL_ffff0000907e805e);
}


