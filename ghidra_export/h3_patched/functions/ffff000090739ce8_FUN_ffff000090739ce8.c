// ffff000090739ce8  size=24  FUN_ffff000090739ce8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739ce8(int param_1)

{
  uint uVar1;
  
  if (_DAT_ffff00009086de28 != 0) {
    uVar1 = 0x95000000;
    if (param_1 != 0) {
      uVar1 = 0x95000080;
    }
    FUN_ffff000090739868
              ((uint *)(_DAT_ffff00009086de28 + 0x40),
               uVar1 | *(uint *)(_DAT_ffff00009086de28 + 0x40) & 0x6affff7f);
    return 0;
  }
  return 0xffffffff;
}


