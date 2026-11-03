
undefined8 FUN_ffff0000b8063070(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    param_1[10] = 0;
    *param_1 = 0;
    FUN_ffff0000b80682a4(&DAT_ffff0000b81b7100);
    return 0;
  }
  return 0xffffffff;
}


