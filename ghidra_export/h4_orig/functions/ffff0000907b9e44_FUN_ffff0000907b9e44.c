// ffff0000907b9e44  size=60  FUN_ffff0000907b9e44

undefined8 FUN_ffff0000907b9e44(long param_1,char param_2)

{
  int in_w8;
  
  *(uint *)(param_1 + 0x128) = *(uint *)(param_1 + 0x128) | 0x7f000000;
  if (param_2 != '\0') {
    *(uint *)(param_1 + 0x124) = *(uint *)(param_1 + 0x124) | in_w8 << 0x18;
  }
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x200;
  return 0;
}


