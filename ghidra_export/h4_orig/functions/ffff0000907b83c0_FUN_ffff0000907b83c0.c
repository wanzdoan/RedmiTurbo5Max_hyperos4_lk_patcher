// ffff0000907b83c0  size=48  FUN_ffff0000907b83c0

undefined8 FUN_ffff0000907b83c0(long param_1)

{
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) & 0xfff0ffff;
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x50000;
  return 0;
}


