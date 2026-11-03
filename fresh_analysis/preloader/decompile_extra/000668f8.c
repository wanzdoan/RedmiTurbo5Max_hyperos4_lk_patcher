// File offset entry: 0x668f8

undefined8 FUN_020667f8(uint param_1)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar1 = param_1 & 0xffff;
  if (0x47 < uVar1) {
    return 0;
  }
  if (uVar1 == 0x11) {
    FUN_0208ada0(0x20bd85b);
    iVar2 = func_0x020864a4();
    if (iVar2 != 1) {
      return 0;
    }
    uVar3 = 0x20c9250;
  }
  else {
    if (uVar1 != 8) {
      if ((*(ushort *)(((ulong)(uVar1 >> 2) & 0x3ffc) + 0x1c012004) >> (ulong)(param_1 & 0xf) & 1)
          == 0) {
        FUN_0208ada0(0x20b9bdb);
        return 1;
      }
      return 0;
    }
    iVar2 = func_0x0208642c();
    if (iVar2 != 1) {
      return 0;
    }
    uVar3 = 0x20b29d9;
  }
  FUN_0208ada0(uVar3);
  return 1;
}


