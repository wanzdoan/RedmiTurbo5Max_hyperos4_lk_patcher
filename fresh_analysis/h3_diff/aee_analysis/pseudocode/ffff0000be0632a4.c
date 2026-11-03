
void FUN_ffff0000be0632a4(void)

{
  long lVar1;
  int iVar2;
  
  lVar1 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar1 != 0) {
    iVar2 = -0x600000;
    if (DAT_ffff0000be1271a8 == '\0') {
      iVar2 = -0x300000;
    }
    FUN_ffff0000be05e110(lVar1,0,(long)(iVar2 + *(int *)(lVar1 + 0x20)));
    FUN_ffff0000be05def0(lVar1);
    return;
  }
  FUN_ffff0000be063b40
            (s_kedump__erase_failed__partition___ffff0000be0b0449,s_expdb_ffff0000be0a79cf);
  FUN_ffff0000be05fd98
            (s_kedump__erase_failed__partition___ffff0000be0b0449,s_expdb_ffff0000be0a79cf);
  return;
}


