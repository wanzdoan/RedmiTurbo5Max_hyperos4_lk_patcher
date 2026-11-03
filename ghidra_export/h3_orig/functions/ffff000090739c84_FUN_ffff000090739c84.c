// ffff000090739c84  size=24  FUN_ffff000090739c84

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739c84(int param_1)

{
  uint uVar1;
  
  if (_DAT_ffff00009086de28 != 0) {
    uVar1 = 0x59000000;
    if (param_1 != 0) {
      uVar1 = 0x59000080;
    }
    FUN_ffff000090739868
              ((uint *)(_DAT_ffff00009086de28 + 0x30),
               uVar1 | *(uint *)(_DAT_ffff00009086de28 + 0x30) & 0xa6ffff7f);
    return 0;
  }
  return 0xffffffff;
}


