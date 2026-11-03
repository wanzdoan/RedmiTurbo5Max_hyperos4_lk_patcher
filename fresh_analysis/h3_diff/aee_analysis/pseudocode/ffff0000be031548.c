
int FUN_ffff0000be031548(void)

{
  int iVar1;
  long lVar2;
  uint local_4;
  
  local_4 = 0;
  iVar1 = DAT_ffff0000be119710;
  if ((DAT_ffff0000be119710 == 0) &&
     (lVar2 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e), iVar1 = 0, lVar2 != 0)) {
    iVar1 = FUN_ffff0000be029958(lVar2,1,&local_4);
    if (iVar1 == 0) {
      DAT_ffff0000be119710 = (local_4 >> 7) + 1;
      iVar1 = DAT_ffff0000be119710;
    }
    else {
      iVar1 = 0;
    }
  }
  return iVar1;
}


