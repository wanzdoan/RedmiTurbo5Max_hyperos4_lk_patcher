// ffff0000907b9878  size=56  FUN_ffff0000907b9878

undefined8 FUN_ffff0000907b9878(long param_1,char param_2)

{
  long lVar1;
  
  lVar1 = 0x134;
  if (param_2 != '\0') {
    lVar1 = 0x138;
  }
  *(uint *)(lVar1 + param_1) = *(uint *)(lVar1 + param_1) | 0x80;
  *(uint *)(param_1 + 0xc) = *(uint *)(param_1 + 0xc) | 0x400;
  return 0;
}


