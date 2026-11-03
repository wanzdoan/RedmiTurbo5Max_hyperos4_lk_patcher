
undefined8 FUN_ffff0000be09de20(short *param_1,undefined8 param_2,short *param_3)

{
  uint uVar1;
  uint uVar2;
  short sVar3;
  ulong uVar4;
  int iVar5;
  undefined8 uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  
  lVar7 = *(long *)(param_3 + 4);
  do {
    lVar7 = lVar7 + -1;
    if (lVar7 == -1) goto LAB_ffff0000be09de64;
  } while (*(int *)(*(long *)(param_3 + 8) + lVar7 * 4) == 0);
  if (*param_3 < 0) {
    return 0xfffffff6;
  }
LAB_ffff0000be09de64:
  uVar6 = FUN_ffff0000be09d754(0,param_1,param_2,param_3);
  iVar5 = (int)uVar6;
  while( true ) {
    if (iVar5 != 0) {
      return uVar6;
    }
    lVar7 = *(long *)(param_1 + 4);
    do {
      lVar7 = lVar7 + -1;
      if (lVar7 == -1) goto LAB_ffff0000be09ded8;
    } while (*(int *)(*(long *)(param_1 + 8) + lVar7 * 4) == 0);
    if (-1 < *param_1) break;
    uVar6 = FUN_ffff0000be09cef8(param_1,param_1,param_3);
    iVar5 = (int)uVar6;
  }
LAB_ffff0000be09ded8:
  do {
    uVar8 = *(ulong *)(param_1 + 4);
    uVar4 = uVar8;
    do {
      uVar9 = uVar4;
      uVar8 = uVar8 - 1;
      if (uVar9 == 0) break;
      uVar4 = uVar9 - 1;
    } while (*(int *)(*(long *)(param_1 + 8) + uVar9 * 4 + -4) == 0);
    uVar4 = *(ulong *)(param_3 + 4);
    do {
      uVar10 = uVar4;
      if (uVar10 == 0) {
        if (uVar9 == 0) goto LAB_ffff0000be09dec4;
        break;
      }
      uVar4 = uVar10 - 1;
    } while (*(int *)(*(long *)(param_3 + 8) + uVar10 * 4 + -4) == 0);
    if (uVar10 < uVar9) {
      iVar5 = (int)*param_1;
LAB_ffff0000be09dec0:
      if (iVar5 < 0) {
        return 0;
      }
    }
    else {
      if (uVar9 < uVar10) {
        iVar5 = -(int)*param_3;
        goto LAB_ffff0000be09dec0;
      }
      sVar3 = *param_1;
      iVar5 = (int)sVar3;
      if (iVar5 < 1) {
        if ((0 < *param_3) && (sVar3 != 0)) {
          return 0;
        }
LAB_ffff0000be09df6c:
        do {
          if (uVar8 == 0xffffffffffffffff) goto LAB_ffff0000be09dec4;
          uVar1 = *(uint *)(*(long *)(param_1 + 8) + uVar8 * 4);
          uVar2 = *(uint *)(*(long *)(param_3 + 8) + uVar8 * 4);
          if (uVar2 < uVar1) goto LAB_ffff0000be09dec0;
          uVar8 = uVar8 - 1;
        } while (uVar2 <= uVar1);
        iVar5 = -(int)sVar3;
        goto LAB_ffff0000be09dec0;
      }
      if (-1 < *param_3) goto LAB_ffff0000be09df6c;
    }
LAB_ffff0000be09dec4:
    uVar6 = FUN_ffff0000be09d008(param_1,param_1,param_3);
    if ((int)uVar6 != 0) {
      return uVar6;
    }
  } while( true );
}


