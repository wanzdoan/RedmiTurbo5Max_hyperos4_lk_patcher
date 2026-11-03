
undefined4 FUN_ffff0000be048404(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260b0 == 0) {
    lVar1 = FUN_ffff0000be046d24(param_1,param_2);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    uVar2 = 3;
    DAT_ffff0000be1260b0 = lVar1;
    if (lVar1 != 0) {
      uVar2 = 0;
    }
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


