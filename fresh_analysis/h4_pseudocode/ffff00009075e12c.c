
undefined8 FUN_ffff00009075e12c(undefined4 param_1)

{
  int iVar1;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (DAT_ffff00009087bf88 != 0) {
    iVar1 = FUN_ffff00009075650c();
    if (iVar1 != 0) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      FUN_ffff000090756b18(DAT_ffff00009087bf88,1,100);
    }
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff00009075c334(DAT_ffff00009087bf70,param_1);
  }
  return 0;
}


