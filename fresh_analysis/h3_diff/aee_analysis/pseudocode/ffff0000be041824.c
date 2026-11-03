
undefined8 FUN_ffff0000be041824(long param_1,long param_2,undefined8 param_3)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  code *pcVar4;
  ulong uVar5;
  
  if (param_1 != 0) {
    puVar3 = (uint *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0xe0));
    uVar2 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0xe0));
    puVar1 = PTR_DAT_ffff0000be0f5660;
    if (0 < (int)uVar2) {
      uVar5 = (ulong)uVar2;
      do {
        if (((*(long *)(puVar1 + (ulong)*puVar3 * 8) != 0) && (param_2 != 0)) &&
           (pcVar4 = *(code **)(*(long *)(puVar1 + (ulong)*puVar3 * 8) + 0x90),
           pcVar4 != (code *)0x0)) {
          (*pcVar4)((ulong)*puVar3,param_2,param_3);
        }
        uVar5 = uVar5 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar5 != 0);
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x5b6,
             s_dp_handle____NULL_ffff0000be0b463c);
}


