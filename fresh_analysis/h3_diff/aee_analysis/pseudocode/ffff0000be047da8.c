
undefined4 FUN_ffff0000be047da8(void)

{
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  return *(undefined4 *)(DAT_ffff0000be1260b0 + 0x1c);
}


