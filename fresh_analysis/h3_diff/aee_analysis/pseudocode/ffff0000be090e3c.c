
bool FUN_ffff0000be090e3c(undefined8 *param_1)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = *param_1;
  iVar2 = FUN_ffff0000be060778(uVar3,s_EMMC_BOOT_ffff0000be0c3941,9);
  if ((iVar2 == 0) ||
     (iVar2 = FUN_ffff0000be060778(uVar3,s_UFS_BOOT_ffff0000be0ac184,8), iVar2 == 0)) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff0000be060778(uVar3,s_COMBO_BOOT_ffff0000be0c15e9,10);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


