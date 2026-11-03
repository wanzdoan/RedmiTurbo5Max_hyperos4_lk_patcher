// ffff0000907aa9b0  size=112  FUN_ffff0000907aa9b0

int FUN_ffff0000907aa9b0(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = -1;
  }
  else {
    FUN_ffff0000907b97b8(&DAT_ffff000010000000,3);
    iVar1 = FUN_ffff0000907b90ac(param_1,0xffff000018005000);
    if (iVar1 != 0) {
      FUN_ffff0000907b97b8(&DAT_ffff000010000000,0);
    }
  }
  return iVar1;
}


