
undefined8 FUN_ffff0000be0921e4(long param_1)

{
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) & 0xfff0ffff;
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x50000;
  return 0;
}


