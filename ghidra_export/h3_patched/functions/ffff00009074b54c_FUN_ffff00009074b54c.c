// ffff00009074b54c  size=48  FUN_ffff00009074b54c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074b54c(int param_1,int param_2,uint param_3,uint param_4)

{
  uint uVar1;
  
  if (param_1 != 0x1e) {
    if ((param_1 != 0xb9) && (param_1 == 0xb8)) {
      FUN_ffff00009074b57c(0x420000);
      return;
    }
    FUN_ffff00009074b57c(0);
    return;
  }
  _DAT_ffff000032120114 = 0;
  _DAT_ffff000032120118 = 0x3004;
  _DAT_ffff00003212011c = 0;
  _DAT_ffff000032120120 = 0;
  _DAT_ffff000032120124 = 0;
  uRamffff000032120128 = 0;
  uRamffff00003212012c = 0;
  uRamffff000032120130 = 0x11;
  uRamffff000032120134 = 0;
  uRamffff000032120138 = 0;
  if (param_4 == 0x18) {
    uRamffff000032120140 = 0x20202020;
    uRamffff00003212013c = 0x20200001;
  }
  else if (param_4 == 0x12) {
    uRamffff00003212013c = 0x40400001;
    uRamffff000032120140 = 0x40404040;
  }
  else {
    if (param_4 != 0x10) {
      if (param_4 < 0x19) {
        uVar1 = 0;
        _DAT_ffff000032120100 = 0;
      }
      else {
        uVar1 = 1;
      }
      goto LAB_ffff00009074b658;
    }
    uRamffff00003212013c = 0x50500001;
    uRamffff000032120140 = 0x50504040;
  }
  uVar1 = 1;
  _DAT_ffff000032120100 = 1;
LAB_ffff00009074b658:
  uRamffff000032120000 = uRamffff000032120000 & 0xfffffffe | uVar1;
  _DAT_ffff000032120020 = _DAT_ffff000032120020 | 3;
  uRamffff000032120030 = param_3 | param_2 << 0x10;
  return;
}


