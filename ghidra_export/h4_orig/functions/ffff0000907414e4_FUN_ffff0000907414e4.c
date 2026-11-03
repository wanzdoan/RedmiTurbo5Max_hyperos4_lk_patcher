// ffff0000907414e4  size=108  FUN_ffff0000907414e4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907414e4(void)

{
  int iVar1;
  long lVar2;
  uint local_4;
  
  local_4 = 0;
  iVar1 = _DAT_ffff00009086f458;
  if ((_DAT_ffff00009086f458 == 0) &&
     (lVar2 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569), iVar1 = 0, lVar2 != 0)) {
    iVar1 = FUN_ffff000090737554(lVar2,1,&local_4);
    if (iVar1 == 0) {
      _DAT_ffff00009086f458 = (local_4 >> 7) + 1;
      iVar1 = _DAT_ffff00009086f458;
    }
    else {
      iVar1 = 0;
    }
  }
  return iVar1;
}


