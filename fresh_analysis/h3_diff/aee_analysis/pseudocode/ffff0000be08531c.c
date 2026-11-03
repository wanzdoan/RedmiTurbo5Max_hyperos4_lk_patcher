
undefined4 FUN_ffff0000be08531c(long param_1,uint param_2,uint param_3)

{
  undefined4 uVar1;
  int iVar2;
  ulong uVar3;
  
  uVar1 = 0x10101a;
  if ((param_2 & 0xffffffef) == 0x20) {
    if ((param_2 == 0x20) && (param_3 < 4)) {
      iVar2 = param_3 << 3;
    }
    else {
      if (param_2 != 0x30) {
        return 0x101027;
      }
      if (2 < param_3) {
        return 0x101027;
      }
      iVar2 = param_3 * 0xc;
    }
    uVar3 = (ulong)(param_2 >> 2);
    iVar2 = iVar2 + 0xdc;
    param_1 = param_1 + 3;
    do {
      uVar1 = thunk_FUN_ffff0000be0364a4(iVar2);
      iVar2 = iVar2 + 1;
      *(undefined4 *)(param_1 + -3) = uVar1;
      param_1 = param_1 + 4;
      uVar3 = uVar3 - 1;
    } while (uVar3 != 0);
    uVar1 = 0;
  }
  return uVar1;
}


