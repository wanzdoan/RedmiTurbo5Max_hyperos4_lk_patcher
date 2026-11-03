// ffff0000907b90b4  size=60  FUN_ffff0000907b90b4

undefined8 FUN_ffff0000907b90b4(long param_1,undefined8 param_2)

{
  FUN_ffff00009077eb88(param_1,0,0x100);
  *(undefined8 *)(param_1 + 0x80) = param_2;
  return 0;
}


