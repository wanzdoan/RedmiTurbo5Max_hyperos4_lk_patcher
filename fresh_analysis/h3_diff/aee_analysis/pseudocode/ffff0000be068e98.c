
undefined8 * FUN_ffff0000be068e98(uint param_1,char param_2)

{
  long lVar1;
  
  if ((DAT_ffff0000be1347f0 == param_1) && ((bool)DAT_ffff0000be1347f1 == (param_2 == -0x80))) {
    lVar1 = 1;
  }
  else {
    if (DAT_ffff0000be134838 != param_1) {
      return (undefined8 *)0x0;
    }
    if ((bool)DAT_ffff0000be134839 != (param_2 == -0x80)) {
      return (undefined8 *)0x0;
    }
    lVar1 = 2;
  }
  return &DAT_ffff0000be134768 + lVar1 * 9;
}


