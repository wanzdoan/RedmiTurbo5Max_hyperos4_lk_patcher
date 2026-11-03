// ffff0000907c58c4  size=180  FUN_ffff0000907c58c4

long FUN_ffff0000907c58c4(undefined8 param_1,long param_2,long param_3,long param_4,long param_5)

{
  long lVar1;
  long lVar2;
  
  if (param_2 == 0) {
    return 2;
  }
  if (param_5 != 0) {
    lVar1 = FUN_ffff00009077c028(param_2);
    if (lVar1 != 0) {
      lVar1 = *(long *)(lVar1 + 0x20);
      FUN_ffff00009077bfac();
      if (-1 < lVar1) {
        if ((param_3 < 0) && (param_3 = lVar1 + param_3, param_3 < 0)) {
          return 4;
        }
        lVar1 = FUN_ffff00009077c028(param_2);
        if (lVar1 != 0) {
          lVar2 = FUN_ffff00009077c144(lVar1,param_5,param_3,param_4);
          FUN_ffff00009077bfac(lVar1);
          return (ulong)(lVar2 < param_4) << 1;
        }
      }
    }
    return 3;
  }
  return 2;
}


