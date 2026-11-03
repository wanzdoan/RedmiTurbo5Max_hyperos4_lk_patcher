// ffff0000907c2f18  size=40  FUN_ffff0000907c2f18

void FUN_ffff0000907c2f18(void)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    FUN_ffff00009071768c(&LAB_ffff000090838480);
    return;
  }
  if ((DAT_ffff00009090d8a0 & 1) == 0) {
    return;
  }
  uVar2 = daif;
  daif = uVar2 & 0xffffffffffffff7f;
  return;
}


