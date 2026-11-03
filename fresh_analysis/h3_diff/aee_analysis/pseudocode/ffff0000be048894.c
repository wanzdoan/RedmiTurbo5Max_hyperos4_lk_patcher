
undefined8 FUN_ffff0000be048894(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar1 = FUN_ffff0000be047368(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar2 = FUN_ffff0000be047398(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar3 = FUN_ffff0000be0472e8(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar4 = FUN_ffff0000be047328(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260a8 == 0) {
    iVar5 = FUN_ffff0000be041774(DAT_ffff0000be1260c8);
    if (iVar5 != 0) {
      if ((DAT_ffff0000be124adc & 1) == 0) {
        FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
        DAT_ffff0000be124adc = 1;
      }
      iVar5 = FUN_ffff0000be047450(DAT_ffff0000be1260b0);
      if (iVar5 != 0) {
        return 0;
      }
      FUN_ffff0000be03759c(0x2b);
    }
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be0473c8(DAT_ffff0000be1260b0,uVar1,uVar2,uVar3,uVar4,0);
    if (DAT_ffff0000be126088 == 0) {
      if ((DAT_ffff0000be124adc & 1) == 0) {
        FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
        DAT_ffff0000be124adc = 1;
      }
      iVar5 = FUN_ffff0000be047450(DAT_ffff0000be1260b0);
      if (iVar5 == 0) {
        if ((DAT_ffff0000be124adc & 1) == 0) {
          FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
          DAT_ffff0000be124adc = 1;
        }
        if (*(int *)(DAT_ffff0000be1260b0 + 0x1c) != 0) {
          FUN_ffff0000be0376c8(0x2b,0);
        }
      }
    }
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be040dc4(DAT_ffff0000be1260c8,DAT_ffff0000be126088);
    if (DAT_ffff0000be126088 == 0) {
      if ((DAT_ffff0000be124adc & 1) == 0) {
        FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
        DAT_ffff0000be124adc = 1;
        iVar5 = DAT_ffff0000be126088;
      }
      else {
        iVar5 = 0;
      }
      FUN_ffff0000be04156c(DAT_ffff0000be1260c8,0,iVar5);
    }
  }
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be041988(DAT_ffff0000be1260c8);
  return 0;
}


