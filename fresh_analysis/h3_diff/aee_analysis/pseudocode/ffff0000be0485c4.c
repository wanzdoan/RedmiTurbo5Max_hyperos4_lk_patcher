
undefined4 FUN_ffff0000be0485c4(void)

{
  long *plVar1;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260b0 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff0000be046d24(0,0);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    DAT_ffff0000be1260b0 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*DAT_ffff0000be1260b0 == 0) {
    return 0;
  }
  return *(undefined4 *)(*DAT_ffff0000be1260b0 + 0x18);
}


