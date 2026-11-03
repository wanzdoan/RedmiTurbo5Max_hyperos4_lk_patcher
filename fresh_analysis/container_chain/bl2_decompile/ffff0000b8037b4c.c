
undefined8 FUN_ffff0000b8037b4c(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  code *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
    puVar1 = PTR_DAT_ffff0000b8108760;
    if (param_2 == 0) {
      uVar5 = 0;
    }
    else {
      uVar5 = *param_1;
    }
    if (0 < (int)uVar2) {
      uVar6 = (ulong)uVar2;
      do {
        if ((*(long *)(puVar1 + (ulong)*puVar3 * 8) != 0) &&
           (pcVar4 = *(code **)(*(long *)(puVar1 + (ulong)*puVar3 * 8) + 0x38),
           pcVar4 != (code *)0x0)) {
          (*pcVar4)((ulong)*puVar3,uVar5);
        }
        uVar6 = uVar6 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar6 != 0);
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x2d2,
             s_dp_handle____NULL_ffff0000b8092b68);
}


