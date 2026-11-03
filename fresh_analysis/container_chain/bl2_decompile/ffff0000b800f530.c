
undefined8 FUN_ffff0000b800f530(long param_1,long param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  int *piVar4;
  
  uVar3 = 0;
  if (param_1 != 0) {
    piVar4 = *(int **)(param_2 + 0x20);
    iVar1 = FUN_ffff0000b80483cc(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000b808dbec);
    if ((iVar1 < 0) && (iVar1 = (**(code **)(*(long *)(param_2 + 0x18) + 8))(param_1), iVar1 < 0)) {
      FUN_ffff0000b804c33c
                (s___s_mt6375_gauge__no_device_tree_ffff0000b808e8ae,
                 s_init_gauge_cust_data_dt_ffff0000b808e8e3);
      uVar3 = 1;
    }
    else {
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_DISABLE_MTKBATTERY_ffff0000b808f89b);
      if (iVar2 != 0) {
        *piVar4 = iVar2;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_CAR_TUNE_VALUE_ffff0000b809e26a);
      if (iVar2 != 0) {
        piVar4[1] = iVar2 * 10;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_R_FG_VALUE_ffff0000b8099961);
      if (iVar2 != 0) {
        piVar4[2] = iVar2 * 10;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_CURR_MEASURE_20A_ffff0000b80a00cf);
      if (iVar2 != 0) {
        piVar4[4] = iVar2;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_UNIT_MULTIPLE_ffff0000b8097d6f);
      if (iVar2 != 0) {
        piVar4[3] = iVar2 * 0x95030;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_FG_METER_RESISTANCE_ffff0000b808994a);
      if (iVar2 != 0) {
        piVar4[5] = iVar2;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_RBAT_PULL_DOWN_R_ffff0000b8093160);
      if (iVar2 != 0) {
        piVar4[9] = iVar2;
      }
      iVar2 = FUN_ffff0000b800eedc(param_1,iVar1,s_RBAT_PULL_UP_VOLT_ffff0000b8093171);
      if (iVar2 != 0) {
        piVar4[10] = iVar2;
      }
      iVar1 = FUN_ffff0000b800eedc(param_1,iVar1,s_DETECT_BAT_TYPE_ffff0000b8086081);
      if (iVar1 != 0) {
        piVar4[0xe] = iVar1;
      }
      uVar3 = 0;
      *(undefined4 *)(param_2 + 0x28) = 1;
    }
  }
  return uVar3;
}


