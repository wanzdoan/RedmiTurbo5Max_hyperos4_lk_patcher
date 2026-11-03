// ffff000090739fe0  size=24  FUN_ffff000090739fe0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739fe0(int param_1)

{
  if (_DAT_ffff00009086dec0 != 0) {
    FUN_ffff000090739b00
              ((uint *)(_DAT_ffff00009086dec0 + 0x60),
               *(uint *)(_DAT_ffff00009086dec0 + 0x60) & 0xfffffffe | param_1 != 0 | 0x77000000);
    return 0;
  }
  return 0xffffffff;
}


