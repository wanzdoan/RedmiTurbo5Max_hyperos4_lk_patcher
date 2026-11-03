
undefined8 FUN_ffff0000b8038460(long param_1,long param_2,undefined8 param_3)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  code *pcVar4;
  ulong uVar5;
  
  if (param_1 != 0) {
    puVar3 = (uint *)FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0xe0));
    uVar2 = FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0xe0));
    puVar1 = PTR_DAT_ffff0000b8108760;
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
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x5b6,
             s_dp_handle____NULL_ffff0000b8092b68);
}


