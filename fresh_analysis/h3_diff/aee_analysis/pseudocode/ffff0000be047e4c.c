
int FUN_ffff0000be047e4c(void)

{
  int iVar1;
  long lVar2;
  
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  lVar2 = FUN_ffff0000be047288(DAT_ffff0000be1260b0);
  if ((lVar2 == 0) || (iVar1 = *(int *)(lVar2 + 0x6b0), iVar1 == 0)) {
    iVar1 = 0x7ff;
  }
  return iVar1;
}


