// File offset entry: 0x5cff8

long FUN_0205cef8(undefined4 param_1)

{
  int *piVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  
  piVar1 = (int *)FUN_020036c8(1);
  lVar3 = 0;
  if (piVar1 != (int *)0x0) {
    if (*piVar1 == 2) {
      uVar2 = (**(code **)(piVar1 + 0x12))(piVar1,param_1);
      if (uVar2 == 0) {
        return 0;
      }
      uVar4 = 0;
      if ((ulong)(uint)piVar1[1] != 0) {
        uVar4 = uVar2 / (uint)piVar1[1];
      }
    }
    else {
      if (*piVar1 != 1) {
        return 0;
      }
      uVar4 = uRam000000000211be88 >> 9;
    }
    lVar3 = uVar4 - 1;
  }
  return lVar3;
}


