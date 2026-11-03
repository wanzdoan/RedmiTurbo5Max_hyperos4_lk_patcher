
undefined8 FUN_ffff0000907ccbc4(long *param_1,long param_2,undefined8 param_3,uint param_4)

{
  undefined8 uVar1;
  byte *pbVar2;
  
  pbVar2 = (byte *)*param_1;
  if (param_2 - (long)pbVar2 < 1) {
    return 0xffffffa0;
  }
  if (*pbVar2 == param_4) {
    *param_1 = (long)(pbVar2 + 1);
    uVar1 = FUN_ffff0000907ccab8();
    return uVar1;
  }
  return 0xffffff9e;
}


