
undefined4 FUN_ffff0000907ad298(long param_1,long param_2,ulong param_3)

{
  undefined4 uVar1;
  ulong uVar2;
  
  if (param_3 != 0) {
    uVar2 = 0;
    do {
      if (*(byte *)(param_1 + uVar2) != *(byte *)(param_2 + uVar2)) {
        uVar1 = 0xffffffff;
        if (*(byte *)(param_2 + uVar2) < *(byte *)(param_1 + uVar2)) {
          uVar1 = 1;
        }
        return uVar1;
      }
      uVar2 = (ulong)((int)uVar2 + 1);
    } while (uVar2 < param_3);
  }
  return 0;
}


