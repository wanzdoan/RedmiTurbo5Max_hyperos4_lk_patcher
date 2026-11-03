
undefined8
FUN_ffff0000907ad43c(uint param_1,long param_2,undefined4 param_3,long param_4,undefined4 param_5)

{
  undefined8 uVar1;
  
  uVar1 = 0x10000c;
  if (0xffffffd2 < param_1 - 0x2e) {
    if (param_2 == 0) {
      uVar1 = 0x100000;
    }
    else if ((param_1 < 0x1f) || (param_4 != 0)) {
      uVar1 = FUN_ffff0000907a28ec(param_2,param_3,(ulong)(param_1 - 1) * 0x4c + -0xffff6f6f4fdc,0);
      if (((int)uVar1 == 0) &&
         ((param_1 < 0x1f ||
          (uVar1 = FUN_ffff0000907a28ec
                             (param_4,param_5,(ulong)(param_1 - 1) * 0x4c + -0xffff6f6f4fbc,0),
          (int)uVar1 == 0)))) {
        if ((param_1 == 0x20) || (param_1 == 7)) {
          uVar1 = FUN_ffff0000907a28ec(param_2,param_3,(ulong)param_1 * 0x4c + -0xffff6f6f4fdc,0);
          if ((0x1e < param_1) && ((int)uVar1 == 0)) {
            uVar1 = FUN_ffff0000907a28ec(param_4,param_5,(ulong)param_1 * 0x4c + -0xffff6f6f4fbc,0);
            return uVar1;
          }
        }
        else {
          uVar1 = 0;
        }
      }
    }
  }
  return uVar1;
}


