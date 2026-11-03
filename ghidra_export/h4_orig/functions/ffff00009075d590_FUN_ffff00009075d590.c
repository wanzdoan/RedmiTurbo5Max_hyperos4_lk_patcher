// ffff00009075d590  size=176  FUN_ffff00009075d590

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009075d590(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (_DAT_ffff00009087bf70 == 0) {
    lVar1 = FUN_ffff00009075bc40(param_1,param_2);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    uVar2 = 3;
    _DAT_ffff00009087bf70 = lVar1;
    if (lVar1 != 0) {
      uVar2 = 0;
    }
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


