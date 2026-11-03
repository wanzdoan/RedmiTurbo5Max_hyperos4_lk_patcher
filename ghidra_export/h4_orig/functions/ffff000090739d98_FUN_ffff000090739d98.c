// ffff000090739d98  size=24  FUN_ffff000090739d98

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739d98(int param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  if (_DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x6af7ffff;
    if (DAT_ffff00009081942c != 1) {
      uVar1 = 0x6adfffff;
    }
    uVar3 = 0x80000;
    if (DAT_ffff00009081942c != 1) {
      uVar3 = 0x200000;
    }
    uVar2 = 0;
    if (param_1 != 0) {
      uVar2 = uVar3;
    }
    FUN_ffff000090739b00
              ((uint *)(_DAT_ffff00009086dec0 + 0x44),
               *(uint *)(_DAT_ffff00009086dec0 + 0x44) & uVar1 | uVar2 | 0x95000000);
    return 0;
  }
  return 0xffffffff;
}


