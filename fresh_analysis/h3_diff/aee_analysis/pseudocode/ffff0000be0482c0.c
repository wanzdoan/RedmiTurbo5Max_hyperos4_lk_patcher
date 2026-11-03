
void FUN_ffff0000be0482c0(undefined8 param_1)

{
  long lVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  DAT_ffff0000be1260a8 = 0;
  lVar1 = FUN_ffff0000be04025c(0,DAT_ffff0000be1260b8);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  DAT_ffff0000be1260c8 = lVar1;
  if (lVar1 == 0) {
    return;
  }
  if ((DAT_ffff0000be1260b0 == (undefined8 *)0x0) || (*(int *)*DAT_ffff0000be1260b0 != 2)) {
    uVar2 = 0x102;
  }
  else {
    uVar2 = 0x2d;
    if (((int *)*DAT_ffff0000be1260b0)[0x66] != 2) {
      uVar3 = 0x2b;
      if (*(int *)(DAT_ffff0000be1260b0 + 2) == 2) {
        uVar3 = 0x2c;
      }
      uVar2 = 0x2d;
      if (*(int *)(DAT_ffff0000be1260b0 + 2) != 3) {
        uVar2 = uVar3;
      }
    }
  }
  FUN_ffff0000be040870(lVar1,uVar2);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be041824(DAT_ffff0000be1260c8,DAT_ffff0000be1260b0[1],param_1);
  return;
}


