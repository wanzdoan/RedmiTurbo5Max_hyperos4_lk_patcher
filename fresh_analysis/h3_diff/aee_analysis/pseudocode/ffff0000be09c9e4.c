
ulong FUN_ffff0000be09c9e4(short *param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  short sVar3;
  ulong uVar4;
  ulong uVar5;
  
  uVar4 = *(ulong *)(param_1 + 4);
  uVar1 = -param_2;
  if (-1 < (int)param_2) {
    uVar1 = param_2;
  }
  do {
    uVar5 = uVar4;
    uVar4 = uVar5 - 1;
    if (uVar4 == 0xffffffffffffffff) {
      uVar1 = 1;
      if (-1 < (int)param_2) {
        uVar1 = 0xffffffff;
      }
      uVar2 = 0;
      if (param_2 != 0) {
        uVar2 = uVar1;
      }
      return (ulong)uVar2;
    }
  } while (*(int *)(*(long *)(param_1 + 8) + uVar4 * 4) == 0);
  sVar3 = *param_1;
  uVar4 = (ulong)sVar3;
  if ((param_2 != 0) && (uVar5 < 2)) {
    if (sVar3 < 1) {
      if ((-1 < (int)param_2) && (sVar3 != 0)) {
        return 0xffffffff;
      }
    }
    else if ((int)param_2 < 0) {
      return 1;
    }
    if (**(uint **)(param_1 + 8) <= uVar1) {
      if (uVar1 <= **(uint **)(param_1 + 8)) {
        return 0;
      }
      uVar4 = (ulong)(uint)-(int)sVar3;
    }
  }
  return uVar4;
}


