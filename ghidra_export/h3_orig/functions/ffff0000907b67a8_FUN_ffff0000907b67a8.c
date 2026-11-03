// ffff0000907b67a8  size=120  FUN_ffff0000907b67a8

bool FUN_ffff0000907b67a8(undefined8 *param_1)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = *param_1;
  iVar2 = FUN_ffff00009077e820(uVar3,s_EMMC_BOOT_ffff0000907fe4dc,9);
  if ((iVar2 == 0) ||
     (iVar2 = FUN_ffff00009077e820(uVar3,s_UFS_BOOT_ffff0000907dbc27,8), iVar2 == 0)) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff00009077e820(uVar3,s_COMBO_BOOT_ffff0000907fabf8,10);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


