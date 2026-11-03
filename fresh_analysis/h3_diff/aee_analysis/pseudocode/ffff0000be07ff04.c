
undefined8 FUN_ffff0000be07ff04(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    param_1[9] = 0;
    *param_1 = 0;
    FUN_ffff0000be085a54(&DAT_ffff0000be1b5340);
    return 0;
  }
  return 0xffffffff;
}


