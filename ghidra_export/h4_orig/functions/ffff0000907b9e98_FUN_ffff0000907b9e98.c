// ffff0000907b9e98  size=60  FUN_ffff0000907b9e98

undefined8 FUN_ffff0000907b9e98(long param_1,char param_2)

{
  int in_w8;
  
  *(uint *)(param_1 + 0x128) = *(uint *)(param_1 + 0x128) | 0x7f0000;
  if (param_2 != '\0') {
    *(uint *)(param_1 + 0x124) = *(uint *)(param_1 + 0x124) | in_w8 << 0x10;
  }
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x100;
  return 0;
}


