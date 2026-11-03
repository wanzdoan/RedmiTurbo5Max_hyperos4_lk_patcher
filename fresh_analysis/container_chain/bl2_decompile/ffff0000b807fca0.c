
undefined4 FUN_ffff0000b807fca0(long param_1,long param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  long lVar6;
  
  lVar6 = *(long *)(param_1 + 0x38);
  lVar4 = *(long *)(lVar6 + 0x48);
  if (lVar4 == 0) {
    lVar4 = (**(code **)(param_1 + 0x40))
                      (*(undefined8 *)(param_1 + 0x50),1 << (ulong)(*(uint *)(lVar6 + 0x38) & 0x1f),
                       1);
    *(long *)(lVar6 + 0x48) = lVar4;
    if (lVar4 == 0) {
      return 1;
    }
  }
  uVar3 = *(uint *)(lVar6 + 0x3c);
  if (uVar3 == 0) {
    *(undefined8 *)(lVar6 + 0x40) = 0;
    uVar3 = 1 << (ulong)(*(uint *)(lVar6 + 0x38) & 0x1f);
    *(uint *)(lVar6 + 0x3c) = uVar3;
  }
  if (param_3 < uVar3) {
    uVar3 = uVar3 - *(uint *)(lVar6 + 0x44);
    uVar1 = uVar3;
    if (param_3 <= uVar3) {
      uVar1 = param_3;
    }
    FUN_ffff0000b804cd40(lVar4 + (ulong)*(uint *)(lVar6 + 0x44),param_2 - (ulong)param_3,uVar1);
    if (uVar3 < param_3) {
      param_3 = param_3 - uVar1;
      FUN_ffff0000b804cd40(*(undefined8 *)(lVar6 + 0x48),param_2 - (ulong)param_3,(ulong)param_3);
      iVar5 = *(int *)(lVar6 + 0x3c);
      *(uint *)(lVar6 + 0x44) = param_3;
    }
    else {
      uVar3 = *(int *)(lVar6 + 0x44) + uVar1;
      uVar2 = 0;
      if (uVar3 != *(uint *)(lVar6 + 0x3c)) {
        uVar2 = uVar3;
      }
      *(uint *)(lVar6 + 0x44) = uVar2;
      if (*(uint *)(lVar6 + 0x3c) <= *(uint *)(lVar6 + 0x40)) {
        return 0;
      }
      iVar5 = *(uint *)(lVar6 + 0x40) + uVar1;
    }
  }
  else {
    FUN_ffff0000b804cd40(lVar4,param_2 - (ulong)uVar3);
    iVar5 = *(int *)(lVar6 + 0x3c);
    *(undefined4 *)(lVar6 + 0x44) = 0;
  }
  *(int *)(lVar6 + 0x40) = iVar5;
  return 0;
}


