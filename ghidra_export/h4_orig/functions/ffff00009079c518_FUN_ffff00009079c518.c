// ffff00009079c518  size=224  FUN_ffff00009079c518

undefined8 FUN_ffff00009079c518(long param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  ushort uVar3;
  undefined8 uVar4;
  ushort *puVar5;
  ulong uVar6;
  ulong uVar7;
  
  puVar5 = *(ushort **)(param_1 + 0x10);
  uVar2 = *(uint *)(param_1 + 8);
  uVar1 = (uint)*puVar5 + (param_2 & 0xffff);
  *puVar5 = (ushort)uVar1;
  if ((uVar2 < 2) || (uVar1 < 0x10000)) {
    if (uVar1 < 0x10000) {
      return 0;
    }
    uVar7 = 1;
  }
  else {
    uVar6 = 1;
    do {
      uVar7 = uVar6 + 1;
      uVar3 = puVar5[uVar6];
      uVar1 = uVar3 + 1;
      puVar5[uVar6] = (ushort)uVar1;
      if (uVar2 <= uVar7) break;
      uVar6 = uVar7;
    } while (uVar1 >> 0x10 != 0);
    if (uVar3 != 0xffff) {
      return 0;
    }
  }
  if (uVar2 != 0xffffffff) {
    uVar2 = uVar2 + 1;
    if (*(uint *)(param_1 + 4) < uVar2) {
      uVar4 = FUN_ffff00009079f598(param_1,uVar2);
      if ((int)uVar4 != 0) {
        return uVar4;
      }
      puVar5 = *(ushort **)(param_1 + 0x10);
    }
    *(uint *)(param_1 + 8) = uVar2;
  }
  puVar5[uVar7] = 1;
  return 0;
}


