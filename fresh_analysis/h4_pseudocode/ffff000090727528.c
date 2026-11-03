
void FUN_ffff000090727528(uint param_1,int param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  ulong uVar4;
  
  uVar4 = (ulong)(param_1 >> 2) & 0x3ffffffc;
  uVar2 = *(uint *)(uVar4 - 0xfffff3bff400);
  uVar3 = 2 << (ulong)((param_1 & 0xf) << 1);
  uVar1 = uVar2 | uVar3;
  if (param_2 != 0) {
    uVar1 = uVar2 & (uVar3 ^ 0xffffffff);
  }
  *(uint *)(uVar4 - 0xfffff3bff400) = uVar1;
  DataSynchronizationBarrier(3,3,0);
  return;
}


