// ffff0000907b9e2c  size=24  FUN_ffff0000907b9e2c

undefined8 FUN_ffff0000907b9e2c(long param_1,byte param_2)

{
  if (5 < param_2) {
    return 0x7246;
  }
  *(uint *)(param_1 + 0x128) = *(uint *)(param_1 + 0x128) | 0x7f000000;
  if (param_2 != 0) {
    *(uint *)(param_1 + 0x124) = *(uint *)(param_1 + 0x124) | (uint)param_2 << 0x18;
  }
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x200;
  return 0;
}


