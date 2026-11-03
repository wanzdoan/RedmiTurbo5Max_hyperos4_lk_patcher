
undefined8 FUN_ffff0000b80470b4(uint *param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  
  if (((ulong)param_1 & 7) != 0) {
    return 0xffffffed;
  }
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  if ((uVar1 >> 0x10 | uVar1 << 0x10) != 0xd00dfeed) {
    return 0xfffffff7;
  }
  uVar1 = (param_1[5] & 0xff00ff00) >> 8 | (param_1[5] & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 < 2) {
    return 0xfffffff6;
  }
  uVar4 = (param_1[6] & 0xff00ff00) >> 8 | (param_1[6] & 0xff00ff) << 8;
  uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
  if (0x11 < uVar4) {
    return 0xfffffff6;
  }
  if (uVar1 < uVar4) {
    return 0xfffffff6;
  }
  if (uVar1 == 2) {
    uVar4 = 0x20;
  }
  else if (uVar1 < 4) {
    uVar4 = 0x24;
  }
  else {
    uVar4 = 0x24;
    if (0x10 < uVar1) {
      uVar4 = 0x28;
    }
  }
  uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  if ((int)uVar2 < 0) {
    return 0xfffffff8;
  }
  if (uVar2 < uVar4) {
    return 0xfffffff8;
  }
  uVar3 = (param_1[4] & 0xff00ff00) >> 8 | (param_1[4] & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (uVar4 <= uVar3) {
    if (uVar2 < uVar3) {
      return 0xfffffff8;
    }
    uVar3 = (param_1[2] & 0xff00ff00) >> 8 | (param_1[2] & 0xff00ff) << 8;
    uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
    if (uVar1 < 0x11) {
      if (uVar3 < uVar4 || uVar2 < uVar3) {
        return 0xfffffff8;
      }
    }
    else {
      if (uVar3 < uVar4 || uVar2 < uVar3) {
        return 0xfffffff8;
      }
      uVar1 = (param_1[9] & 0xff00ff00) >> 8 | (param_1[9] & 0xff00ff) << 8;
      uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
      if (CARRY4(uVar1,uVar3)) {
        return 0xfffffff8;
      }
      if (uVar2 < uVar1 + uVar3) {
        return 0xfffffff8;
      }
    }
    uVar1 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
    if ((((uVar4 <= uVar1) && (uVar1 <= uVar2)) &&
        (uVar4 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8,
        uVar4 = uVar4 >> 0x10 | uVar4 << 0x10, !CARRY4(uVar4,uVar1))) && (uVar4 + uVar1 <= uVar2)) {
      return 0;
    }
    return 0xfffffff8;
  }
  return 0xfffffff8;
}


