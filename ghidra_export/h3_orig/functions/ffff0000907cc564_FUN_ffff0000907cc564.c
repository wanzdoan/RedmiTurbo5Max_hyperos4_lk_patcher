// ffff0000907cc564  size=40  FUN_ffff0000907cc564

undefined8
FUN_ffff0000907cc564(byte *param_1,long *param_2,undefined8 param_3,undefined8 param_4,uint param_5)

{
  undefined8 uVar1;
  
  if (*param_1 == param_5) {
    *param_2 = (long)(param_1 + 1);
    uVar1 = FUN_ffff0000907cc444();
    return uVar1;
  }
  return 0xffffff9e;
}


