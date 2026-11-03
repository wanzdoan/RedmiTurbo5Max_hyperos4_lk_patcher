// File offset entry: 0x8b8e0

undefined8 FUN_0208b7e0(void)

{
  int iVar1;
  int *piVar2;
  undefined8 uVar3;
  
  cRam000000000211da20 = FUN_0209a3f4();
  piVar2 = piRam0000000002103290;
  iRam000000000211da24 = *piRam0000000002103290;
  uVar3 = func_0x0208b658();
  if ((int)uVar3 != 0) {
    if (iRam00000000020eccd4 == 0xaeedead) {
      FUN_0208ada0(0x20c96f7,0x20cb92f,0x20c31f3);
    }
    iVar1 = *piVar2;
    if (((iVar1 == 0) || (iVar1 == 2)) || (iVar1 == 0x800)) {
      if (cRam000000000211da20 == '\0') {
        return 0;
      }
      FUN_0208ada0(0x20cb941,0x20cb92f);
    }
    else {
      FUN_0208ada0(0x20bdf46,0x20cb92f,iVar1,cRam000000000211da20);
    }
    uVar3 = 1;
  }
  return uVar3;
}


