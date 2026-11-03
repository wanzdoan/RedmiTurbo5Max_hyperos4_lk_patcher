// ffff0000907b9720  size=16  FUN_ffff0000907b9720

undefined8 FUN_ffff0000907b9720(long param_1,undefined8 param_2)

{
  if (param_1 != 0) {
    FUN_ffff00009077ee50(param_1,0,0x100);
    *(undefined8 *)(param_1 + 0x80) = param_2;
    return 0;
  }
  return 0x7247;
}


