
undefined8 FUN_ffff0000be01df90(long param_1,long param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  int *piVar4;
  
  uVar3 = 0;
  if (param_1 != 0) {
    piVar4 = *(int **)(param_2 + 0x20);
    iVar1 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000be0ae62c);
    if ((iVar1 < 0) && (iVar1 = (**(code **)(*(long *)(param_2 + 0x18) + 8))(param_1), iVar1 < 0)) {
      FUN_ffff0000be05fd98
                (s___s_mt6375_gauge__no_device_tree_ffff0000be0af689,
                 s_init_gauge_cust_data_dt_ffff0000be0af6be);
      uVar3 = 1;
    }
    else {
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_DISABLE_MTKBATTERY_ffff0000be0b0a97);
      if (iVar2 != 0) {
        *piVar4 = iVar2;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_CAR_TUNE_VALUE_ffff0000be0c2abe);
      if (iVar2 != 0) {
        piVar4[1] = iVar2 * 10;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_R_FG_VALUE_ffff0000be0bcf8a);
      if (iVar2 != 0) {
        piVar4[2] = iVar2 * 10;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_CURR_MEASURE_20A_ffff0000be0c4fb8);
      if (iVar2 != 0) {
        piVar4[4] = iVar2;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_UNIT_MULTIPLE_ffff0000be0bac91);
      if (iVar2 != 0) {
        piVar4[3] = iVar2 * 0x95030;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_FG_METER_RESISTANCE_ffff0000be0a8ee8);
      if (iVar2 != 0) {
        piVar4[5] = iVar2;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_RBAT_PULL_DOWN_R_ffff0000be0b5048);
      if (iVar2 != 0) {
        piVar4[9] = iVar2;
      }
      iVar2 = FUN_ffff0000be01d93c(param_1,iVar1,s_RBAT_PULL_UP_VOLT_ffff0000be0b5059);
      if (iVar2 != 0) {
        piVar4[10] = iVar2;
      }
      iVar1 = FUN_ffff0000be01d93c(param_1,iVar1,s_DETECT_BAT_TYPE_ffff0000be0a4598);
      if (iVar1 != 0) {
        piVar4[0xe] = iVar1;
      }
      uVar3 = 0;
      *(undefined4 *)(param_2 + 0x28) = 1;
    }
  }
  return uVar3;
}


