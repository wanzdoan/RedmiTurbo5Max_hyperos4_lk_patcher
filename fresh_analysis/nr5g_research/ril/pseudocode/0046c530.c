
/* RmcNetworkHandler::convertPSNetworkType(unsigned int) */

uint __thiscall RmcNetworkHandler::convertPSNetworkType(RmcNetworkHandler *this,uint param_1)

{
  ulong uVar1;
  
  if ((int)param_1 < 0x88) {
    if ((int)param_1 < 8) {
      if ((param_1 == 1) || (param_1 == 2)) {
        return param_1;
      }
      if (param_1 == 4) {
        return 3;
      }
    }
    else {
      uVar1 = (ulong)(param_1 - 0x10);
      if (param_1 - 0x10 < 0x39) {
        if ((1L << (uVar1 & 0x3f) & 0x101000100010000U) != 0) {
          return 0xf;
        }
        if (uVar1 == 0) {
          return 10;
        }
        if (uVar1 == 8) {
          return 0xb;
        }
      }
      if (param_1 == 8) {
        return 9;
      }
      if (param_1 == 0x60) {
        return 0xf;
      }
    }
  }
  else if ((int)param_1 < 0x200) {
    if ((int)param_1 < 200) {
      if ((param_1 - 0x88 < 0x29) &&
         ((1L << ((ulong)(param_1 - 0x88) & 0x3f) & 0x10001010001U) != 0)) {
        return 0xf;
      }
    }
    else {
      if (param_1 == 200) {
        return 0xf;
      }
      if (param_1 == 0xe0) {
        return 0xf;
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
      return 0x13;
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


