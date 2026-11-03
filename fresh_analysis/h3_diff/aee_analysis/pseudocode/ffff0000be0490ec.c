
void FUN_ffff0000be0490ec(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (DAT_ffff0000be12609c != 0) {
    return;
  }
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
    if (plVar1 != (long *)0x0) goto LAB_ffff0000be04917c;
    plVar1 = (long *)FUN_ffff0000be046d24(0,0);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    DAT_ffff0000be1260b0 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff0000be0491b4;
  }
  else {
LAB_ffff0000be04917c:
    if (*DAT_ffff0000be1260b0 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff0000be0491b4;
    }
    uVar2 = *(int *)(*DAT_ffff0000be1260b0 + 0x18) * 4 + 0x7cU & 0xffffff80;
  }
  uVar3 = 0;
  if (*DAT_ffff0000be1260b0 != 0) {
    uVar3 = *(int *)(*DAT_ffff0000be1260b0 + 0x1c) + 0x1fU >> 3 & 0x1ffffffc;
  }
LAB_ffff0000be0491b4:
  DAT_ffff0000be12609c = uVar2 * uVar3;
  return;
}


