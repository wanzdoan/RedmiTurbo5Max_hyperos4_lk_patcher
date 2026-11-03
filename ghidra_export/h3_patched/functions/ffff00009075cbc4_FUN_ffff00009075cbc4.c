// ffff00009075cbc4  size=100  FUN_ffff00009075cbc4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009075cbc4(void)

{
  int iVar1;
  long lVar2;
  
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  lVar2 = FUN_ffff00009075bdf0(_DAT_ffff00009087bed8);
  if ((lVar2 == 0) || (iVar1 = *(int *)(lVar2 + 0x6b0), iVar1 == 0)) {
    iVar1 = 0x7ff;
  }
  return iVar1;
}


