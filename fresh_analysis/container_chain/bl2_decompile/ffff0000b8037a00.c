
undefined8 FUN_ffff0000b8037a00(undefined8 *param_1,int param_2)

{
  undefined4 uVar1;
  undefined *puVar2;
  undefined4 uVar3;
  uint uVar4;
  ulong uVar5;
  uint *puVar6;
  code *pcVar7;
  undefined8 uVar8;
  ulong uVar9;
  
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
    FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
    if (param_2 == 0) {
      uVar8 = 0;
    }
    else {
      uVar8 = *param_1;
    }
    uVar1 = *(undefined4 *)(param_1 + 0x1c);
    uVar5 = FUN_ffff0000b803acd4(uVar1);
    if ((uVar5 & 1) == 0) {
      uVar3 = 0x14;
    }
    else {
      uVar3 = FUN_ffff0000b803ace8(uVar1);
    }
    puVar6 = (uint *)FUN_ffff0000b803b000(uVar1);
    uVar4 = FUN_ffff0000b803ad00(uVar1);
    puVar2 = PTR_DAT_ffff0000b8108760;
    if (0 < (int)uVar4) {
      uVar9 = (ulong)uVar4;
      do {
        if ((*(long *)(puVar2 + (ulong)*puVar6 * 8) != 0) &&
           (pcVar7 = *(code **)(*(long *)(puVar2 + (ulong)*puVar6 * 8) + 0x28),
           pcVar7 != (code *)0x0)) {
          (*pcVar7)((ulong)*puVar6,uVar8);
        }
        uVar9 = uVar9 - 1;
        puVar6 = puVar6 + 1;
      } while (uVar9 != 0);
    }
    if ((uVar5 & 1) != 0) {
      puVar6 = (uint *)FUN_ffff0000b803b000(uVar3);
      uVar4 = FUN_ffff0000b803ad00(uVar3);
      puVar2 = PTR_DAT_ffff0000b8108760;
      if (0 < (int)uVar4) {
        uVar5 = (ulong)uVar4;
        do {
          if ((*(long *)(puVar2 + (ulong)*puVar6 * 8) != 0) &&
             (pcVar7 = *(code **)(*(long *)(puVar2 + (ulong)*puVar6 * 8) + 0x28),
             pcVar7 != (code *)0x0)) {
            (*pcVar7)((ulong)*puVar6,uVar8);
          }
          uVar5 = uVar5 - 1;
          puVar6 = puVar6 + 1;
        } while (uVar5 != 0);
      }
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x2ab,
             s_dp_handle____NULL_ffff0000b8092b68);
}


