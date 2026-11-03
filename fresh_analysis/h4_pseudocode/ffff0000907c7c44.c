
int FUN_ffff0000907c7c44(short *param_1,short *param_2)

{
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  
  uVar5 = *(ulong *)(param_1 + 4);
  do {
    uVar6 = uVar5;
    uVar5 = uVar6 - 1;
    if (uVar5 == 0xffffffffffffffff) break;
  } while (*(int *)(*(long *)(param_1 + 8) + uVar5 * 4) == 0);
  uVar3 = *(ulong *)(param_2 + 4);
  do {
    uVar7 = uVar3;
    if (uVar7 == 0) {
      if (uVar5 == 0xffffffffffffffff) {
        return 0;
      }
      break;
    }
    uVar3 = uVar7 - 1;
  } while (*(int *)(*(long *)(param_2 + 8) + uVar7 * 4 + -4) == 0);
  if (uVar7 < uVar6) {
    iVar4 = (int)*param_1;
  }
  else {
    if (uVar6 < uVar7) {
      iVar4 = (int)*param_2;
    }
    else {
      iVar4 = (int)*param_1;
      if (iVar4 < 1) {
        if ((0 < *param_2) && (iVar4 != 0)) {
          return -1;
        }
      }
      else if (*param_2 < 0) {
        return 1;
      }
      do {
        if (uVar5 == 0xffffffffffffffff) {
          return 0;
        }
        uVar1 = *(uint *)(*(long *)(param_1 + 8) + uVar5 * 4);
        uVar2 = *(uint *)(*(long *)(param_2 + 8) + uVar5 * 4);
        if (uVar2 < uVar1) {
          return iVar4;
        }
        uVar5 = uVar5 - 1;
      } while (uVar2 <= uVar1);
    }
    iVar4 = -iVar4;
  }
  return iVar4;
}


