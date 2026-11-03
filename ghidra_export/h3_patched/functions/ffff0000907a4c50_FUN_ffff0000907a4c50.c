// ffff0000907a4c50  size=16  FUN_ffff0000907a4c50

undefined8 FUN_ffff0000907a4c50(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    param_1[9] = 0;
    *param_1 = 0;
    FUN_ffff0000907aa7a0(0xffff00009090ad80);
    return 0;
  }
  return 0xffffffff;
}


