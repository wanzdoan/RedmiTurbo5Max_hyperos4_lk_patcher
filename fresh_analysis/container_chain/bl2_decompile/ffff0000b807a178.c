
undefined8 FUN_ffff0000b807a178(int *param_1,byte *param_2)

{
  uint uVar1;
  byte *pbVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  long lVar6;
  int iVar7;
  long lVar8;
  
  uVar5 = (uint)*param_2;
  uVar4 = 0;
  pbVar2 = param_2;
  if (*param_2 != 0) {
    do {
      uVar4 = uVar5 + uVar4 * 0x334b;
      uVar5 = (uint)pbVar2[1];
      pbVar2 = pbVar2 + 1;
    } while (uVar5 != 0);
  }
  iVar7 = *param_1;
  uVar5 = iVar7 - 1;
  if (0 < iVar7) {
    do {
      uVar4 = uVar4 & uVar5;
      lVar8 = *(long *)(param_1 + 4);
      lVar6 = *(long *)(lVar8 + (long)(int)uVar4 * 8);
      if (lVar6 == 0) {
        return 0;
      }
      uVar1 = *(uint *)(lVar6 + 8);
      uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      lVar6 = FUN_ffff0000b804778c(*(undefined8 *)(param_1 + 2),uVar1 >> 0x10 | uVar1 << 0x10);
      if ((lVar6 != 0) && (iVar3 = thunk_FUN_ffff0000b804d248(lVar6,param_2), iVar3 == 0)) {
        return *(undefined8 *)(lVar8 + (long)(int)uVar4 * 8);
      }
      uVar4 = uVar4 + 1;
      iVar7 = iVar7 + -1;
    } while (iVar7 != 0);
  }
  return 0;
}


