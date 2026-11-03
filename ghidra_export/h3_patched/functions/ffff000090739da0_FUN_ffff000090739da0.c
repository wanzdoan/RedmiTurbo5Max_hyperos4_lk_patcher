// ffff000090739da0  size=24  FUN_ffff000090739da0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739da0(int param_1)

{
  uint uVar1;
  
  if (_DAT_ffff00009086de28 != 0) {
    uVar1 = 0x77000000;
    if (param_1 != 0) {
      uVar1 = 0x77000002;
    }
    FUN_ffff000090739868
              ((uint *)(_DAT_ffff00009086de28 + 0x60),
               uVar1 | *(uint *)(_DAT_ffff00009086de28 + 0x60) & 0x88fffffd);
    return 0;
  }
  return 0xffffffff;
}


