
undefined8 FUN_ffff000090739d1c(int param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  if (DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x6afbffff;
    if (DAT_ffff00009081942c != 1) {
      uVar1 = 0x6aefffff;
    }
    uVar3 = 0x40000;
    if (DAT_ffff00009081942c != 1) {
      uVar3 = 0x100000;
    }
    uVar2 = 0;
    if (param_1 != 0) {
      uVar2 = uVar3;
    }
    FUN_ffff000090739b00
              ((uint *)(DAT_ffff00009086dec0 + 0x44),
               *(uint *)(DAT_ffff00009086dec0 + 0x44) & uVar1 | uVar2 | 0x95000000);
    return 0;
  }
  return 0xffffffff;
}


