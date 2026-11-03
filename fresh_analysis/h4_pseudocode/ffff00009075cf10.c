
void FUN_ffff00009075cf10(undefined8 param_1)

{
  long lVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  DAT_ffff00009087bf68 = 0;
  lVar1 = FUN_ffff000090755028(0,DAT_ffff00009087bf78);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  DAT_ffff00009087bf88 = lVar1;
  if (lVar1 == 0) {
    return;
  }
  if ((DAT_ffff00009087bf70 == (undefined8 *)0x0) || (*(int *)*DAT_ffff00009087bf70 != 2)) {
    uVar2 = 0x102;
  }
  else {
    uVar2 = 0x2d;
    if (((int *)*DAT_ffff00009087bf70)[0x66] != 2) {
      uVar3 = 0x2b;
      if (*(int *)(DAT_ffff00009087bf70 + 2) == 2) {
        uVar3 = 0x2c;
      }
      uVar2 = 0x2d;
      if (*(int *)(DAT_ffff00009087bf70 + 2) != 3) {
        uVar2 = uVar3;
      }
    }
  }
  FUN_ffff00009075563c(lVar1,uVar2);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff000090756848(DAT_ffff00009087bf88,DAT_ffff00009087bf70[1],param_1);
  return;
}


