// ffff0000907c70d0  size=312  FUN_ffff0000907c70d0

undefined8 FUN_ffff0000907c70d0(undefined2 *param_1,long param_2,ulong param_3)

{
  uint uVar1;
  undefined2 uVar2;
  undefined8 uVar3;
  uint uVar4;
  ulong uVar5;
  long lVar6;
  ulong uVar7;
  
  uVar5 = param_3 >> 2;
  if ((param_3 & 3) != 0) {
    uVar5 = uVar5 + 1;
  }
  if (*(ulong *)(param_1 + 4) != uVar5) {
    uVar2 = param_1[1];
    if (*(long *)(param_1 + 8) != 0) {
      FUN_ffff0000907cc428(*(long *)(param_1 + 8),*(ulong *)(param_1 + 4) << 2);
      thunk_FUN_ffff0000907c2ad8(*(undefined8 *)(param_1 + 8));
    }
    param_1[1] = uVar2;
    *(undefined8 *)(param_1 + 4) = 0;
    *(undefined8 *)(param_1 + 8) = 0;
    *param_1 = 1;
    if (10000 < uVar5) {
      return 0xfffffff0;
    }
    if (uVar5 != 0) {
      lVar6 = FUN_ffff00009077d2e0(uVar5,4);
      if (lVar6 == 0) {
        return 0xfffffff0;
      }
      if (*(long *)(param_1 + 8) != 0) {
        FUN_ffff00009077e858(lVar6,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
        FUN_ffff0000907cc428(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
        thunk_FUN_ffff0000907c2ad8(*(undefined8 *)(param_1 + 8));
      }
      *(ulong *)(param_1 + 4) = uVar5;
      *(long *)(param_1 + 8) = lVar6;
    }
  }
  uVar3 = FUN_ffff0000907c6eb4(param_1,0);
  if ((int)uVar3 == 0) {
    if (param_3 != 0) {
      uVar4 = 0;
      uVar5 = 0;
      lVar6 = *(long *)(param_1 + 8);
      do {
        param_2 = param_2 + -1;
        uVar7 = uVar5 & 0xfffffffffffffffc;
        uVar1 = uVar4 & 0x18;
        uVar5 = uVar5 + 1;
        uVar4 = uVar4 + 8;
        *(uint *)(lVar6 + uVar7) =
             *(uint *)(lVar6 + uVar7) | (uint)*(byte *)(param_2 + param_3) << (ulong)uVar1;
      } while (param_3 != uVar5);
    }
    uVar3 = 0;
  }
  return uVar3;
}


