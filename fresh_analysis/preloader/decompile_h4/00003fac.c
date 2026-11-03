// File offset entry: 0x3fac

ulong FUN_02003eac(long param_1)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  
  if (param_1 == 0) {
    FUN_0208ada0(0x20c6b21);
    uVar3 = 0xffffffff;
  }
  else {
    uVar3 = FUN_02091544(param_1,0x20b313c);
    if ((int)uVar3 != 0) {
      iVar2 = FUN_02091544(param_1,0x20cbdd2);
      uVar1 = 0xffffffff;
      if (iVar2 == 0) {
        uVar1 = 1;
      }
      uVar3 = (ulong)uVar1;
    }
  }
  return uVar3;
}


