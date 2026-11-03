
undefined8 FUN_ffff0000be093d48(long param_1,byte param_2)

{
  long lVar1;
  
  if (1 < param_2) {
    return 0x7245;
  }
  lVar1 = 0x124;
  if (param_2 != 0) {
    lVar1 = 0x128;
  }
  *(uint *)(lVar1 + param_1) = *(uint *)(lVar1 + param_1) | 0x800000;
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x100;
  return 0;
}


