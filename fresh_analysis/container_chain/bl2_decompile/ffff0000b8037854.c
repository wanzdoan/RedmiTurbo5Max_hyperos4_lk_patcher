
undefined8 FUN_ffff0000b8037854(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  ulong uVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x282,
               s_dp_handle____NULL_ffff0000b8092b68);
  }
  puVar3 = (uint *)FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
  uVar2 = FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
  uVar7 = (ulong)uVar2;
  if (param_2 == 0) {
    uVar6 = 0;
  }
  else {
    uVar6 = *param_1;
  }
  FUN_ffff0000b803c0f4(*(undefined4 *)(param_1 + 1),uVar6);
  uVar4 = FUN_ffff0000b803acd4(*(undefined4 *)(param_1 + 0x1c));
  if ((uVar4 & 1) != 0) {
    FUN_ffff0000b803c14c(*(undefined4 *)(param_1 + 1),uVar6);
  }
  FUN_ffff0000b80371bc(*(undefined4 *)(param_1 + 0x1c),uVar6);
  puVar1 = PTR_DAT_ffff0000b8108760;
  if (0 < (int)uVar2) {
    do {
      uVar4 = (ulong)*puVar3;
      lVar5 = *(long *)(puVar1 + uVar4 * 8);
      if (lVar5 != 0) {
        if (*(code **)(lVar5 + 0x10) != (code *)0x0) {
          (**(code **)(lVar5 + 0x10))(uVar4,uVar6);
          lVar5 = *(long *)(puVar1 + uVar4 * 8);
        }
        if (*(code **)(lVar5 + 0x98) != (code *)0x0) {
          (**(code **)(lVar5 + 0x98))(uVar4,0);
        }
      }
      uVar7 = uVar7 - 1;
      puVar3 = puVar3 + 1;
    } while (uVar7 != 0);
  }
  *(undefined4 *)((long)param_1 + 0xc) = 0;
  FUN_ffff0000b803ab0c();
  if ((DAT_ffff0000b81282d8 & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
    DAT_ffff0000b81282e4 = 0xf;
    DAT_ffff0000b81282d8 = 1;
  }
  if ((((DAT_ffff0000b81282e8 != 0) &&
       ((DAT_ffff0000b8128f10 == 0 || (*(int *)(DAT_ffff0000b8128f10 + 0xc) == 0)))) &&
      ((DAT_ffff0000b8128f18 == 0 || (*(int *)(DAT_ffff0000b8128f18 + 0xc) == 0)))) &&
     (((DAT_ffff0000b8128f20 == 0 || (*(int *)(DAT_ffff0000b8128f20 + 0xc) == 0)) &&
      ((DAT_ffff0000b8128f28 == 0 || (*(int *)(DAT_ffff0000b8128f28 + 0xc) == 0)))))) {
    DAT_ffff0000b81282e8 = 0;
    FUN_ffff0000b803c310();
  }
  return 0;
}


