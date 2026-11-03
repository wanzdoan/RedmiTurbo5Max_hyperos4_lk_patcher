// ffff00009079c7c4  size=244  FUN_ffff00009079c7c4

undefined8 FUN_ffff00009079c7c4(long param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  undefined8 uVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  
  uVar1 = *(uint *)(param_1 + 8);
  lVar4 = *(long *)(param_1 + 0x10);
  if (((uint)*(ushort *)(lVar4 + (ulong)(uVar1 - 1) * 2) * (param_2 & 0xffff) & 0xffff0000) == 0) {
    uVar8 = (ulong)uVar1;
LAB_ffff00009079c830:
    if (uVar1 == 0) goto LAB_ffff00009079c874;
  }
  else {
    if (uVar1 != 0xffffffff) {
      uVar5 = uVar1 + 1;
      uVar8 = (ulong)uVar5;
      if (*(uint *)(param_1 + 4) < uVar5) {
        uVar3 = FUN_ffff00009079f598(param_1,uVar5);
        if ((int)uVar3 != 0) {
          return uVar3;
        }
        lVar4 = *(long *)(param_1 + 0x10);
      }
      goto LAB_ffff00009079c830;
    }
    uVar8 = 0xffffffff;
  }
  uVar6 = 0;
  uVar5 = 0;
  do {
    lVar7 = uVar6 * 2;
    uVar6 = uVar6 + 1;
    uVar2 = uVar5 + (uint)*(ushort *)(lVar4 + lVar7) * (param_2 & 0xffff);
    uVar5 = uVar2 >> 0x10;
    *(short *)(lVar4 + lVar7) = (short)uVar2;
  } while (uVar1 != uVar6);
  if (0xffff < uVar2) {
    uVar8 = (ulong)(uVar1 + 1);
    *(short *)(lVar4 + (ulong)uVar1 * 2) = (short)(uVar2 >> 0x10);
  }
LAB_ffff00009079c874:
  if (1 < (uint)uVar8) {
    do {
      if (*(short *)(lVar4 + -2 + uVar8 * 2) != 0) goto LAB_ffff00009079c89c;
      uVar8 = uVar8 - 1;
    } while (1 < (uint)uVar8);
    uVar8 = 1;
  }
LAB_ffff00009079c89c:
  *(int *)(param_1 + 8) = (int)uVar8;
  return 0;
}


