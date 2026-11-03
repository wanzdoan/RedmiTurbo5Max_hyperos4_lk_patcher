// ffff0000907aac70  size=112  FUN_ffff0000907aac70

int FUN_ffff0000907aac70(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = -1;
  }
  else {
    FUN_ffff0000907b9e2c(&DAT_ffff000010000000,3);
    iVar1 = FUN_ffff0000907b9720(param_1,0xffff000018005000);
    if (iVar1 != 0) {
      FUN_ffff0000907b9e2c(&DAT_ffff000010000000,0);
    }
  }
  return iVar1;
}


