// ffff000090739e14  size=172  FUN_ffff000090739e14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739e14(uint param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  if (_DAT_ffff00009086dec0 == 0) {
    return 0xffffffff;
  }
  uVar2 = 0x1ffff;
  if (DAT_ffff00009081942c != 1) {
    uVar2 = 0x7ffff;
  }
  if (_DAT_ffff00009086dec8 < (uVar2 & param_2)) {
    _DAT_ffff00009086dec8 = uVar2 & param_2;
  }
  uVar2 = uVar2 & param_1;
  uVar1 = *(uint *)(_DAT_ffff00009086dec0 + 0x44);
  if (DAT_ffff00009081942c == 1) {
    if (uVar2 <= (uVar1 & 0x1ffff)) {
      return 0;
    }
    uVar3 = 0xfffe0000;
  }
  else {
    if (uVar2 <= (uVar1 & 0x7ffff)) {
      return 0;
    }
    uVar3 = 0xfff80000;
  }
  FUN_ffff000090739b00((uint *)(_DAT_ffff00009086dec0 + 0x44),uVar2 | uVar3 & uVar1 | 0x95000000);
  return 0;
}


