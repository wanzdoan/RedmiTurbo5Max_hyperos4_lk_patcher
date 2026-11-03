// ffff00009075650c  size=176  FUN_ffff00009075650c

undefined8 FUN_ffff00009075650c(long param_1)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  code *pcVar6;
  ulong uVar7;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x59b,
               s_dp_handle____NULL_ffff0000907e81a2);
  }
  lVar4 = FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0xe0));
  uVar2 = FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0xe0));
  lVar1 = DAT_ffff0000908399c8;
  if (0 < (int)uVar2) {
    uVar7 = (ulong)uVar2 + 1;
    do {
      lVar5 = *(long *)(lVar1 + (ulong)*(uint *)(lVar4 + (ulong)((int)uVar7 - 2) * 4) * 8);
      if (((lVar5 != 0) && (pcVar6 = *(code **)(lVar5 + 0x70), pcVar6 != (code *)0x0)) &&
         (iVar3 = (*pcVar6)(), iVar3 != 0)) {
        return 1;
      }
      uVar7 = uVar7 - 1;
    } while (1 < uVar7);
  }
  return 0;
}


