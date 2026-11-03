// ffff00009073a038  size=24  FUN_ffff00009073a038

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073a038(int param_1)

{
  uint uVar1;
  
  if (_DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x77000000;
    if (param_1 != 0) {
      uVar1 = 0x77000002;
    }
    FUN_ffff000090739b00
              ((uint *)(_DAT_ffff00009086dec0 + 0x60),
               uVar1 | *(uint *)(_DAT_ffff00009086dec0 + 0x60) & 0x88fffffd);
    return 0;
  }
  return 0xffffffff;
}


