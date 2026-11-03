// ffff000090739c28  size=24  FUN_ffff000090739c28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739c28(uint param_1)

{
  if (_DAT_ffff00009086de28 != 0) {
    if (0x1fffe < param_1) {
      param_1 = 0x1ffff;
    }
    FUN_ffff000090739868
              ((uint *)(_DAT_ffff00009086de28 + 0x50),
               param_1 | 0x95000000 | *(uint *)(_DAT_ffff00009086de28 + 0x50) & 0xfffe0000);
    return 0;
  }
  return 0xffffffff;
}


