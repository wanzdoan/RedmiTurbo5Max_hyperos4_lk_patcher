// ffff000090739d48  size=24  FUN_ffff000090739d48

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739d48(int param_1)

{
  if (_DAT_ffff00009086de28 != 0) {
    FUN_ffff000090739868
              ((uint *)(_DAT_ffff00009086de28 + 0x60),
               *(uint *)(_DAT_ffff00009086de28 + 0x60) & 0xfffffffe | param_1 != 0 | 0x77000000);
    return 0;
  }
  return 0xffffffff;
}


