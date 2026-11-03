// ffff0000907b98c8  size=56  FUN_ffff0000907b98c8

undefined8 FUN_ffff0000907b98c8(long param_1,char param_2)

{
  long lVar1;
  
  lVar1 = 0x124;
  if (param_2 != '\0') {
    lVar1 = 0x128;
  }
  *(uint *)(lVar1 + param_1) = *(uint *)(lVar1 + param_1) | 0x800000;
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x100;
  return 0;
}


