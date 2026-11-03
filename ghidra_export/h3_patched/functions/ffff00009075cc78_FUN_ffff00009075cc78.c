// ffff00009075cc78  size=324  FUN_ffff00009075cc78

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075cc78(undefined8 param_1)

{
  long lVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  _DAT_ffff00009087bed0 = 0;
  lVar1 = FUN_ffff000090754d90(0,_DAT_ffff00009087bee0);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  _DAT_ffff00009087bef0 = lVar1;
  if (lVar1 == 0) {
    return;
  }
  if ((_DAT_ffff00009087bed8 == (undefined8 *)0x0) || (*(int *)*_DAT_ffff00009087bed8 != 2)) {
    uVar2 = 0x102;
  }
  else {
    uVar2 = 0x2d;
    if (((int *)*_DAT_ffff00009087bed8)[0x66] != 2) {
      uVar3 = 0x2b;
      if (*(int *)(_DAT_ffff00009087bed8 + 2) == 2) {
        uVar3 = 0x2c;
      }
      uVar2 = 0x2d;
      if (*(int *)(_DAT_ffff00009087bed8 + 2) != 3) {
        uVar2 = uVar3;
      }
    }
  }
  FUN_ffff0000907553a4(lVar1,uVar2);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  FUN_ffff0000907565b0(_DAT_ffff00009087bef0,_DAT_ffff00009087bed8[1],param_1);
  return;
}


