
bool FUN_ffff00009071fe9c(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined1 auStack_460 [1088];
  
  lVar3 = FUN_ffff00009077c028(&DAT_ffff0000907dbf67);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s__recovery_check__open_misc_parti_ffff0000907ddc31);
    bVar1 = false;
  }
  else {
    FUN_ffff00009077ee50(auStack_460,0,0x440);
    FUN_ffff00009077ee50(auStack_460,0,0x440);
    iVar2 = FUN_ffff00009077c0bc(lVar3,auStack_460,0,0x440);
    if (iVar2 < 1) {
      FUN_ffff00009077e11c(s__recovery_check__read_bootloader_ffff0000907da843);
      FUN_ffff00009077bfac(lVar3);
      bVar1 = false;
    }
    else {
      FUN_ffff00009077bfac(lVar3);
      iVar2 = FUN_ffff00009077f348(auStack_460,s_boot_recovery_ffff0000907d8c68,0xd);
      if (iVar2 == 0) {
        bVar1 = true;
      }
      else {
        iVar2 = FUN_ffff00009077f348(auStack_460,s_boot_fastboot_ffff0000907d7344,0xd);
        bVar1 = iVar2 == 0;
      }
    }
  }
  return bVar1;
}


