
/* RmcNetworkHandler::convertCSNetworkType(unsigned int) */

undefined8 __thiscall RmcNetworkHandler::convertCSNetworkType(RmcNetworkHandler *this,uint param_1)

{
  uint uVar1;
  
  if (0x87 < (int)param_1) {
    if ((int)param_1 < 0x200) {
      if ((int)param_1 < 200) {
        if ((param_1 - 0x88 < 0x29) &&
           ((1L << ((ulong)(param_1 - 0x88) & 0x3f) & 0x10001010001U) != 0)) {
          return 3;
        }
      }
      else {
        if (param_1 == 200) {
          return 3;
        }
        if (param_1 == 0xe0) {
          return 3;
        }
        if (param_1 == 0x100) {
          return 6;
        }
      }
    }
    else if ((int)param_1 < 0x2000) {
      if (param_1 == 0x200) {
        return 8;
      }
      if (param_1 == 0x400) {
        return 0xd;
      }
      if (param_1 == 0x1000) {
        return 0xe;
      }
    }
    else {
      if (param_1 == 0x2000) {
        return 0xe;
      }
      if (param_1 == 0x4000) {
        return 0xe;
      }
      if (param_1 == 0x8000) {
        return 0x14;
      }
    }
    return 0;
  }
  uVar1 = param_1 - 1;
  if (uVar1 < 0x40) {
    if ((1L << ((ulong)uVar1 & 0x3f) & 0x8000800080808088U) != 0) {
      return 3;
    }
    if ((1L << ((ulong)uVar1 & 0x3f) & 3U) != 0) {
      return 0x10;
    }
  }
  if (param_1 == 0x48) {
    return 3;
  }
  if (param_1 != 0x60) {
    return 0;
  }
  return 3;
}


