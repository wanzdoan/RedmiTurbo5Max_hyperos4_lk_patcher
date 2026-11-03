// File offset entry: 0x8b758

undefined4 FUN_0208b658(void)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  lVar2 = FUN_02006dbc(0x20ca6ce);
  if ((lVar2 == 0) || (iVar1 = FUN_02091544(lVar2,0x20bd000), iVar1 == 0)) {
    uVar3 = 0x3121;
  }
  else {
    iVar1 = FUN_02091544(lVar2,0x20bb036);
    if (iVar1 == 0) {
      uVar3 = 0xfc11;
    }
    else {
      iVar1 = FUN_02091544(lVar2,0x20c76a5);
      uVar3 = 0;
      if (iVar1 != 0) {
        uVar3 = 0x3121;
      }
    }
  }
  FUN_0208ada0(0x20c864a,uVar3);
  return uVar3;
}


