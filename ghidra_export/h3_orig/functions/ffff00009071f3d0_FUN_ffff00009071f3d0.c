// ffff00009071f3d0  size=424  FUN_ffff00009071f3d0

undefined8 FUN_ffff00009071f3d0(long param_1,long param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  int *piVar4;
  
  uVar3 = 0;
  if (param_1 != 0) {
    piVar4 = *(int **)(param_2 + 0x20);
    iVar1 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000907df219);
    if ((iVar1 < 0) && (iVar1 = (**(code **)(*(long *)(param_2 + 0x18) + 8))(param_1), iVar1 < 0)) {
      FUN_ffff00009077de54
                (s___s_mt6375_gauge__no_device_tree_ffff0000907e0d4f,
                 s_init_gauge_cust_data_dt_ffff0000907e0d84);
      uVar3 = 1;
    }
    else {
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_DISABLE_MTKBATTERY_ffff0000907e272a);
      if (iVar2 != 0) {
        *piVar4 = iVar2;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_CAR_TUNE_VALUE_ffff0000907fcd88);
      if (iVar2 != 0) {
        piVar4[1] = iVar2 * 10;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_R_FG_VALUE_ffff0000907f4311);
      if (iVar2 != 0) {
        piVar4[2] = iVar2 * 10;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_CURR_MEASURE_20A_ffff0000908005b8);
      if (iVar2 != 0) {
        piVar4[4] = iVar2;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_UNIT_MULTIPLE_ffff0000907f0eaf);
      if (iVar2 != 0) {
        piVar4[3] = iVar2 * 0x95030;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_FG_METER_RESISTANCE_ffff0000907d7642);
      if (iVar2 != 0) {
        piVar4[5] = iVar2;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_RBAT_PULL_DOWN_R_ffff0000907e8f17);
      if (iVar2 != 0) {
        piVar4[9] = iVar2;
      }
      iVar2 = FUN_ffff00009071ed7c(param_1,iVar1,s_RBAT_PULL_UP_VOLT_ffff0000907e8f28);
      if (iVar2 != 0) {
        piVar4[10] = iVar2;
      }
      iVar1 = FUN_ffff00009071ed7c(param_1,iVar1,s_DETECT_BAT_TYPE_ffff0000907d0cbc);
      if (iVar1 != 0) {
        piVar4[0xe] = iVar1;
      }
      uVar3 = 0;
      *(undefined4 *)(param_2 + 0x28) = 1;
    }
  }
  return uVar3;
}


