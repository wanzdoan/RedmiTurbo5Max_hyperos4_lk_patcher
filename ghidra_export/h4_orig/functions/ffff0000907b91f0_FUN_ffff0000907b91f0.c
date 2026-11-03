// ffff0000907b91f0  size=60  FUN_ffff0000907b91f0

undefined8 FUN_ffff0000907b91f0(long param_1,undefined8 param_2)

{
  FUN_ffff00009077ee50(param_1,0,0x80);
  *(undefined8 *)(param_1 + 0x40) = param_2;
  return 0;
}


