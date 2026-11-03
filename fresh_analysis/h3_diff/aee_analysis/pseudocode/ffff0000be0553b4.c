
uint FUN_ffff0000be0553b4(uint *param_1)

{
  uint uVar1;
  
  if (((ulong)param_1 & 7) != 0) {
    return 0xffffffed;
  }
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 == 0x2ff20112) {
    uVar1 = (param_1[9] & 0xff00ff00) >> 8 | (param_1[9] & 0xff00ff) << 8;
    if (uVar1 >> 0x10 == 0 && (uVar1 & 0xffff) == 0) {
      return 0xfffffff9;
    }
  }
  else {
    if (uVar1 != 0xd00dfeed) {
      return 0xfffffff7;
    }
    uVar1 = (param_1[5] & 0xff00ff00) >> 8 | (param_1[5] & 0xff00ff) << 8;
    if (((uVar1 >> 0x10 | uVar1 << 0x10) < 2) ||
       (uVar1 = (param_1[6] & 0xff00ff00) >> 8 | (param_1[6] & 0xff00ff) << 8,
       0x11 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
      return 0xfffffff6;
    }
  }
  uVar1 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (0x7ffffffe < uVar1) {
    uVar1 = 0xfffffff8;
  }
  return uVar1;
}


