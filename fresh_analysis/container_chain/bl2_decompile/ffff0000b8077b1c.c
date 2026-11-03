
undefined8 FUN_ffff0000b8077b1c(long param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  uint uVar5;
  
  uVar2 = *(uint *)(param_1 + 0x18);
  if ((int)uVar2 < 2) {
    lVar4 = 0;
  }
  else {
    uVar3 = 0;
    uVar5 = uVar2;
    do {
      uVar2 = uVar3 + (uVar2 >> 1);
      uVar1 = uVar2;
      if (*(uint *)(*(long *)(param_1 + 0x20) +
                   (-(ulong)(uVar2 >> 0x1f) & 0xfffffff000000000 | (ulong)uVar2 << 4)) <= param_2) {
        uVar1 = uVar5;
        uVar3 = uVar2;
      }
      uVar2 = uVar1 - uVar3;
      uVar5 = uVar1;
    } while (1 < (int)uVar2);
    lVar4 = (long)(int)uVar3;
  }
  if ((uVar2 == 1) && (*(uint *)(*(long *)(param_1 + 0x20) + lVar4 * 0x10) == param_2)) {
    return *(undefined8 *)(*(long *)(param_1 + 0x20) + lVar4 * 0x10 + 8);
  }
  return 0;
}


