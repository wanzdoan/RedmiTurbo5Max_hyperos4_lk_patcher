// ffff00009075de94  size=212  FUN_ffff00009075de94

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075de94(undefined4 param_1)

{
  int iVar1;
  
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  if (_DAT_ffff00009087bef0 != 0) {
    iVar1 = FUN_ffff000090756274();
    if (iVar1 != 0) {
      if ((DAT_ffff00009087a904 & 1) == 0) {
        FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
        DAT_ffff00009087a904 = 1;
      }
      FUN_ffff000090756880(_DAT_ffff00009087bef0,1,100);
    }
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff00009075c09c(_DAT_ffff00009087bed8,param_1);
  }
  return 0;
}


