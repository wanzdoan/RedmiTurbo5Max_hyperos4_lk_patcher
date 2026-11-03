
long FUN_ffff0000be00ade4(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  
  lVar1 = FUN_ffff0000be05f1bc(0x2078);
  if (lVar1 != 0) {
    FUN_ffff0000be060ae0(lVar1,0,0x2078);
    *(undefined8 *)(lVar1 + 0x2068) = param_1;
    *(undefined8 *)(lVar1 + 0x2070) = param_2;
  }
  return lVar1;
}


