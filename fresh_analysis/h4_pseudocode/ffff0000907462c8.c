
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907462c8(int param_1,int param_2)

{
  uint uVar1;
  
  if (param_1 == 1) {
    _DAT_ffff00001c010030 = _DAT_ffff00001c010030 | 8;
    _DAT_ffff00001c010088 = 0x1b2a;
    _DAT_ffff00001c01008c = 0x4f59;
  }
  else {
    _DAT_ffff00001c010030 = _DAT_ffff00001c010030 & 0xfffffff7;
    _DAT_ffff00001c010088 = 0;
    _DAT_ffff00001c01008c = 0;
  }
  uVar1 = 0x44000008;
  _DAT_ffff00001c010030 = _DAT_ffff00001c010030 | 0x33000000;
  if (param_2 != 0) {
    uVar1 = 0x44000000;
  }
  _DAT_ffff00001c010034 = uVar1 | _DAT_ffff00001c010034 & 0xbbfffff7;
  return;
}


