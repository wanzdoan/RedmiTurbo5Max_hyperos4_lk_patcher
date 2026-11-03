// ffff000090745fb8  size=120  FUN_ffff000090745fb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745fb8(int param_1,int param_2,uint param_3)

{
  uint uVar1;
  
  if (param_1 == 1) {
    _DAT_ffff00001c010030 = _DAT_ffff00001c010030 | 4;
    _DAT_ffff00001c010038 = (param_3 & 0xf) << 4 | 1;
  }
  else {
    _DAT_ffff00001c010030 = _DAT_ffff00001c010030 & 0xfffffffb;
    _DAT_ffff00001c010038 = 0;
  }
  uVar1 = 0x44000004;
  _DAT_ffff00001c010030 = _DAT_ffff00001c010030 | 0x33000000;
  if (param_2 != 0) {
    uVar1 = 0x44000000;
  }
  _DAT_ffff00001c010034 = uVar1 | _DAT_ffff00001c010034 & 0xbbfffffb;
  return;
}


