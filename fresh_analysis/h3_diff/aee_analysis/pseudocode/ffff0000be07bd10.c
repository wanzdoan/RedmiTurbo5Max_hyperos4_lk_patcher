
uint FUN_ffff0000be07bd10(undefined8 param_1,long param_2)

{
  short sVar1;
  uint uVar2;
  uint uVar3;
  
  sVar1 = *(short *)(*(long *)(param_2 + 0x10) + (ulong)(*(int *)(param_2 + 8) - 1) * 2);
  if (-1 < sVar1) {
    uVar3 = 0;
    do {
      uVar2 = (uint)sVar1;
      uVar3 = uVar3 + 1;
      sVar1 = (short)(uVar2 << 1);
    } while (((uVar2 & 0x7fffffff) >> 0xe & 1) == 0);
    if ((uVar3 & 0xffff) != 0) {
      FUN_ffff0000be0789a8(param_1,uVar3);
      FUN_ffff0000be0789a8(param_2,uVar3);
      return uVar3;
    }
  }
  return 0;
}


