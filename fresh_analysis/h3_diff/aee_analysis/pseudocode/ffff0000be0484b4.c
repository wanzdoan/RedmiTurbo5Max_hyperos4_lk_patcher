
undefined8 FUN_ffff0000be0484b4(void)

{
  undefined8 uVar1;
  
  if ((DAT_ffff0000be126090 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s_skip__s_before_init_ffff0000be0aa96b,s_primary_display_suspend_ffff0000be0b46aa);
    uVar1 = 0xffffffff;
  }
  else {
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be0474dc(DAT_ffff0000be1260b0);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be040f10(DAT_ffff0000be1260c8,0);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be041624(DAT_ffff0000be1260c8,0);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    uVar1 = 0;
    DAT_ffff0000be1260a0 = 0;
  }
  return uVar1;
}


