
undefined8 FUN_ffff0000907a5a3c(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    param_1[10] = 0;
    *param_1 = 0;
    FUN_ffff0000907aac70(&DAT_ffff00009090aec0);
    return 0;
  }
  return 0xffffffff;
}


