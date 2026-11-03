
undefined8 FUN_ffff0000be0742b4(byte param_1)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = FUN_ffff0000be070acc(0);
  if (param_1 < 4) {
    uVar2 = *(undefined8 *)(lVar1 + (ulong)param_1 * 8 + 0x150);
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


