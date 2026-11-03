
undefined8 FUN_ffff0000be09300c(long param_1,undefined8 param_2)

{
  if (param_1 != 0) {
    FUN_ffff0000be060ae0(param_1,0,0x80);
    *(undefined8 *)(param_1 + 0x40) = param_2;
    return 0;
  }
  return 0x7247;
}


