
undefined8 FUN_ffff0000be048e48(undefined4 param_1)

{
  int iVar1;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260c8 != 0) {
    iVar1 = FUN_ffff0000be041774();
    if (iVar1 != 0) {
      if ((DAT_ffff0000be124adc & 1) == 0) {
        FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
        DAT_ffff0000be124adc = 1;
      }
      FUN_ffff0000be041af4(DAT_ffff0000be1260c8,1,100);
    }
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be047534(DAT_ffff0000be1260b0,param_1);
  }
  return 0;
}


