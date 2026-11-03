
undefined8 FUN_ffff0000be080a30(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    param_1[10] = 0;
    *param_1 = 0;
    FUN_ffff0000be085c64(&DAT_ffff0000be1b53c0);
    return 0;
  }
  return 0xffffffff;
}


