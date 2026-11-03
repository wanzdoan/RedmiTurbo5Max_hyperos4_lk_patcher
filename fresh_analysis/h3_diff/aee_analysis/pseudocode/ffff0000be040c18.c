
undefined8 FUN_ffff0000be040c18(undefined8 *param_1,int param_2)

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
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x282,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  puVar3 = (uint *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0x1c));
  uVar2 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0x1c));
  uVar7 = (ulong)uVar2;
  if (param_2 == 0) {
    uVar6 = 0;
  }
  else {
    uVar6 = *param_1;
  }
  FUN_ffff0000be0454b8(*(undefined4 *)(param_1 + 1),uVar6);
  uVar4 = FUN_ffff0000be044098(*(undefined4 *)(param_1 + 0x1c));
  if ((uVar4 & 1) != 0) {
    FUN_ffff0000be045510(*(undefined4 *)(param_1 + 1),uVar6);
  }
  FUN_ffff0000be040580(*(undefined4 *)(param_1 + 0x1c),uVar6);
  puVar1 = PTR_DAT_ffff0000be0f5660;
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
  FUN_ffff0000be043ed0();
  if ((DAT_ffff0000be124ad8 & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
    DAT_ffff0000be124ae4 = 0xf;
    DAT_ffff0000be124ad8 = 1;
  }
  if ((((DAT_ffff0000be124ae8 != 0) &&
       ((DAT_ffff0000be125710 == 0 || (*(int *)(DAT_ffff0000be125710 + 0xc) == 0)))) &&
      ((DAT_ffff0000be125718 == 0 || (*(int *)(DAT_ffff0000be125718 + 0xc) == 0)))) &&
     (((DAT_ffff0000be125720 == 0 || (*(int *)(DAT_ffff0000be125720 + 0xc) == 0)) &&
      ((DAT_ffff0000be125728 == 0 || (*(int *)(DAT_ffff0000be125728 + 0xc) == 0)))))) {
    DAT_ffff0000be124ae8 = 0;
    FUN_ffff0000be0456d4();
  }
  return 0;
}


