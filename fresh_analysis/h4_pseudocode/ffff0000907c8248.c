
undefined8 FUN_ffff0000907c8248(short *param_1,short *param_2,short *param_3)

{
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  ulong uVar4;
  int iVar5;
  undefined8 uVar6;
  ulong uVar7;
  ulong uVar8;
  short sVar9;
  
  sVar9 = *param_2;
  if ((int)*param_3 * (int)sVar9 < 0) {
    uVar7 = *(ulong *)(param_2 + 4);
    do {
      uVar3 = uVar7;
      uVar7 = uVar3 - 1;
      if (uVar7 == 0xffffffffffffffff) break;
    } while (*(int *)(*(long *)(param_2 + 8) + uVar7 * 4) == 0);
    uVar8 = *(ulong *)(param_3 + 4);
    do {
      uVar4 = uVar8;
      uVar8 = uVar4 - 1;
      if (uVar8 == 0xffffffffffffffff) goto LAB_ffff0000907c82dc;
    } while (*(int *)(*(long *)(param_3 + 8) + uVar8 * 4) == 0);
    if (uVar3 <= uVar4) {
      if (uVar4 <= uVar3) {
        do {
          if (uVar7 == 0xffffffffffffffff) goto LAB_ffff0000907c82dc;
          uVar1 = *(uint *)(*(long *)(param_2 + 8) + uVar7 * 4);
          uVar2 = *(uint *)(*(long *)(param_3 + 8) + uVar7 * 4);
          if (uVar2 < uVar1) goto LAB_ffff0000907c82dc;
          uVar7 = uVar7 - 1;
        } while (uVar2 <= uVar1);
      }
      uVar6 = FUN_ffff0000907c8058(param_1,param_3,param_2);
      if ((int)uVar6 != 0) {
        return uVar6;
      }
      sVar9 = -sVar9;
      goto LAB_ffff0000907c82ec;
    }
LAB_ffff0000907c82dc:
    uVar6 = FUN_ffff0000907c8058(param_1,param_2);
    iVar5 = (int)uVar6;
  }
  else {
    uVar6 = FUN_ffff0000907c7de8(param_1,param_2);
    iVar5 = (int)uVar6;
  }
  if (iVar5 != 0) {
    return uVar6;
  }
LAB_ffff0000907c82ec:
  *param_1 = sVar9;
  return 0;
}


