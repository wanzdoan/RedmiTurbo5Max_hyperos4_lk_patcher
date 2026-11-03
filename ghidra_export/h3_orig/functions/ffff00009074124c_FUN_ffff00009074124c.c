// ffff00009074124c  size=108  FUN_ffff00009074124c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009074124c(void)

{
  int iVar1;
  long lVar2;
  uint local_4;
  
  local_4 = 0;
  iVar1 = _DAT_ffff00009086f3c0;
  if ((_DAT_ffff00009086f3c0 == 0) &&
     (lVar2 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425), iVar1 = 0, lVar2 != 0)) {
    iVar1 = FUN_ffff0000907372bc(lVar2,1,&local_4);
    if (iVar1 == 0) {
      _DAT_ffff00009086f3c0 = (local_4 >> 7) + 1;
      iVar1 = _DAT_ffff00009086f3c0;
    }
    else {
      iVar1 = 0;
    }
  }
  return iVar1;
}


