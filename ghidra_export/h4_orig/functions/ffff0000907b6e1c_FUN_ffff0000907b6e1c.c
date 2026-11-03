// ffff0000907b6e1c  size=120  FUN_ffff0000907b6e1c

bool FUN_ffff0000907b6e1c(undefined8 *param_1)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = *param_1;
  iVar2 = FUN_ffff00009077eae8(uVar3,s_EMMC_BOOT_ffff0000907fe684,9);
  if ((iVar2 == 0) ||
     (iVar2 = FUN_ffff00009077eae8(uVar3,s_UFS_BOOT_ffff0000907dbd32,8), iVar2 == 0)) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff00009077eae8(uVar3,s_COMBO_BOOT_ffff0000907fad94,10);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


