
undefined8 FUN_ffff0000be04156c(long param_1,undefined8 param_2)

{
  uint uVar1;
  uint *puVar2;
  code *pcVar3;
  ulong uVar4;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x4a0,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  puVar2 = (uint *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0xe0));
  uVar1 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0xe0));
  uVar4 = (ulong)uVar1;
  FUN_ffff0000be045568(*(undefined4 *)(param_1 + 8),*(undefined4 *)(param_1 + 0xe0),param_2);
  if (0 < (int)uVar1) {
    do {
      if (((&DAT_ffff0000be123840)[*puVar2] != 0) &&
         (pcVar3 = *(code **)((&DAT_ffff0000be123840)[*puVar2] + 0x30), pcVar3 != (code *)0x0)) {
        (*pcVar3)((ulong)*puVar2,param_2);
      }
      uVar4 = uVar4 - 1;
      puVar2 = puVar2 + 1;
    } while (uVar4 != 0);
  }
  return 0;
}


