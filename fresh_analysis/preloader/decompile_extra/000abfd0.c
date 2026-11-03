// File offset entry: 0xabfd0

undefined8 FUN_020abed0(long param_1,byte param_2,long param_3)

{
  ulong uVar1;
  
  if (1 < param_2) {
    return 0x7245;
  }
  if (param_3 != 0) {
    uVar1 = (ulong)*(uint *)(param_3 + (ulong)(param_2 == 0) * 4);
    *(uint *)(uVar1 + param_1) =
         *(uint *)(uVar1 + param_1) | 1 << (ulong)(*(int *)(param_3 + 8) + 7U & 0x1f);
    *(uint *)((ulong)*(uint *)(param_3 + 0x10) + param_1) =
         *(uint *)((ulong)*(uint *)(param_3 + 0x10) + param_1) |
         1 << (ulong)(*(uint *)(param_3 + 0x14) & 0x1f);
    return 0;
  }
  return 0x7247;
}


