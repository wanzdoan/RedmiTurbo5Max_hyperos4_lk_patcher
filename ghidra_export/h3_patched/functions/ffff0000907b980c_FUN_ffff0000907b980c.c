// ffff0000907b980c  size=24  FUN_ffff0000907b980c

undefined8 FUN_ffff0000907b980c(long param_1,byte param_2)

{
  if (5 < param_2) {
    return 0x7246;
  }
  *(uint *)(param_1 + 0x128) = *(uint *)(param_1 + 0x128) | 0x7f0000;
  if (param_2 != 0) {
    *(uint *)(param_1 + 0x124) = *(uint *)(param_1 + 0x124) | (uint)param_2 << 0x10;
  }
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x100;
  return 0;
}


