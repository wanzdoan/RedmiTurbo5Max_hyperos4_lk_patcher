
undefined8 FUN_ffff0000b800ea4c(long param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  uVar3 = 0;
  if (param_1 != 0) {
    iVar1 = FUN_ffff0000b80483cc(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000b808dbec);
    if ((iVar1 < 0) && (iVar1 = FUN_ffff0000b800ee90(param_1,param_2), iVar1 < 0)) {
      FUN_ffff0000b804c33c
                (s___s_battery_no_device_tree__use_d_ffff0000b809e228,
                 s_init_bat_cust_data_dt_ffff0000b8088af2);
      uVar3 = 1;
    }
    else {
      uVar4 = (ulong)param_2;
      iVar2 = FUN_ffff0000b800eb7c(param_1,iVar1,s_DISABLE_MTKBATTERY_ffff0000b808f89b);
      if (iVar2 != 0) {
        *(int *)((long)&DAT_ffff0000b811ae68 + (ulong)param_2 * 0xc) = iVar2;
      }
      iVar2 = FUN_ffff0000b800eb7c(param_1,iVar1,s_PMIC_MIN_VOL_ffff0000b808c3b6);
      if (iVar2 != 0) {
        *(int *)((long)&DAT_ffff0000b811ae68 + uVar4 * 0xc + 4) = iVar2;
      }
      iVar1 = FUN_ffff0000b800eb7c(param_1,iVar1,s_POWERON_SYSTEM_IBOOT_ffff0000b809c256);
      if (iVar1 != 0) {
        (&DAT_ffff0000b811ae70)[uVar4 * 3] = iVar1;
      }
      uVar3 = 0;
      (&DAT_ffff0000b811ae64)[uVar4] = 1;
    }
  }
  return uVar3;
}


