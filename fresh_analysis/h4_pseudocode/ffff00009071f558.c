
undefined8 FUN_ffff00009071f558(long param_1,long param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  int *piVar4;
  
  uVar3 = 0;
  if (param_1 != 0) {
    piVar4 = *(int **)(param_2 + 0x20);
    iVar1 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000907df2bc);
    if ((iVar1 < 0) && (iVar1 = (**(code **)(*(long *)(param_2 + 0x18) + 8))(param_1), iVar1 < 0)) {
      FUN_ffff00009077e11c
                (s___s_mt6375_gauge__no_device_tree_ffff0000907e0df2,
                 s_init_gauge_cust_data_dt_ffff0000907e0e27);
      uVar3 = 1;
    }
    else {
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_DISABLE_MTKBATTERY_ffff0000907e27fb);
      if (iVar2 != 0) {
        *piVar4 = iVar2;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_CAR_TUNE_VALUE_ffff0000907fcf30);
      if (iVar2 != 0) {
        piVar4[1] = iVar2 * 10;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_R_FG_VALUE_ffff0000907f447e);
      if (iVar2 != 0) {
        piVar4[2] = iVar2 * 10;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_CURR_MEASURE_20A_ffff000090800794);
      if (iVar2 != 0) {
        piVar4[4] = iVar2;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_UNIT_MULTIPLE_ffff0000907f1020);
      if (iVar2 != 0) {
        piVar4[3] = iVar2 * 0x95030;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_FG_METER_RESISTANCE_ffff0000907d76d8);
      if (iVar2 != 0) {
        piVar4[5] = iVar2;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_RBAT_PULL_DOWN_R_ffff0000907e905b);
      if (iVar2 != 0) {
        piVar4[9] = iVar2;
      }
      iVar2 = FUN_ffff00009071ef04(param_1,iVar1,s_RBAT_PULL_UP_VOLT_ffff0000907e906c);
      if (iVar2 != 0) {
        piVar4[10] = iVar2;
      }
      iVar1 = FUN_ffff00009071ef04(param_1,iVar1,s_DETECT_BAT_TYPE_ffff0000907d0cf9);
      if (iVar1 != 0) {
        piVar4[0xe] = iVar1;
      }
      uVar3 = 0;
      *(undefined4 *)(param_2 + 0x28) = 1;
    }
  }
  return uVar3;
}


