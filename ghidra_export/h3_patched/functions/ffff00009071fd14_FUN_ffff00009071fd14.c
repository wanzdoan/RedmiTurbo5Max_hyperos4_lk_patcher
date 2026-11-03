// ffff00009071fd14  size=236  FUN_ffff00009071fd14

bool FUN_ffff00009071fd14(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined1 auStack_460 [1088];
  
  lVar3 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe45);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s__recovery_check__open_misc_parti_ffff0000907ddb77);
    bVar1 = false;
  }
  else {
    FUN_ffff00009077eb88(auStack_460,0,0x440);
    FUN_ffff00009077eb88(auStack_460,0,0x440);
    iVar2 = FUN_ffff00009077bdf4(lVar3,auStack_460,0,0x440);
    if (iVar2 < 1) {
      FUN_ffff00009077de54(s__recovery_check__read_bootloader_ffff0000907da721);
      FUN_ffff00009077bce4(lVar3);
      bVar1 = false;
    }
    else {
      FUN_ffff00009077bce4(lVar3);
      iVar2 = FUN_ffff00009077f080(auStack_460,s_boot_recovery_ffff0000907d8bd1,0xd);
      if (iVar2 == 0) {
        bVar1 = true;
      }
      else {
        iVar2 = FUN_ffff00009077f080(auStack_460,s_boot_fastboot_ffff0000907d72ae,0xd);
        bVar1 = iVar2 == 0;
      }
    }
  }
  return bVar1;
}


