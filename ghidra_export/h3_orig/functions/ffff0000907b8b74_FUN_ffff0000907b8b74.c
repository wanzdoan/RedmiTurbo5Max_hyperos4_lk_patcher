// ffff0000907b8b74  size=16  FUN_ffff0000907b8b74

undefined8 FUN_ffff0000907b8b74(long param_1,undefined8 param_2)

{
  if (param_1 != 0) {
    FUN_ffff00009077eb88(param_1,0,0x80);
    *(undefined8 *)(param_1 + 0x40) = param_2;
    return 0;
  }
  return 0x7247;
}


