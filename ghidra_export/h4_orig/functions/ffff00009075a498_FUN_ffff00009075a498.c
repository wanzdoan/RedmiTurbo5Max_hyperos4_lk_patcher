// ffff00009075a498  size=40  FUN_ffff00009075a498

undefined8 FUN_ffff00009075a498(ulong param_1)

{
  uint in_w8;
  long in_x9;
  ulong uVar1;
  
  uVar1 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
  *(uint *)(in_x9 + uVar1) = *(uint *)(in_x9 + uVar1) | 1 << (ulong)(in_w8 & 0x1f);
  return 0;
}


