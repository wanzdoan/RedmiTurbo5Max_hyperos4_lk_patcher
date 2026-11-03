
int FUN_ffff0000b8027550(void)

{
  int iVar1;
  long lVar2;
  uint local_4;
  
  local_4 = 0;
  iVar1 = DAT_ffff0000b811cf20;
  if ((DAT_ffff0000b811cf20 == 0) &&
     (lVar2 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000), iVar1 = 0, lVar2 != 0)) {
    iVar1 = FUN_ffff0000b802120c(lVar2,1,&local_4);
    if (iVar1 == 0) {
      DAT_ffff0000b811cf20 = (local_4 >> 7) + 1;
      iVar1 = DAT_ffff0000b811cf20;
    }
    else {
      iVar1 = 0;
    }
  }
  return iVar1;
}


