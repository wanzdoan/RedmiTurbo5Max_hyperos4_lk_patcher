// ffff0000907c5184  size=204  FUN_ffff0000907c5184

undefined8
FUN_ffff0000907c5184
          (undefined8 param_1,long param_2,long param_3,long param_4,long param_5,long *param_6)

{
  long lVar1;
  long lVar2;
  
  if (param_2 == 0) {
    return 2;
  }
  if (param_5 != 0) {
    if (param_6 == (long *)0x0) {
      return 2;
    }
    lVar1 = FUN_ffff00009077bd60(param_2);
    if (lVar1 != 0) {
      lVar1 = *(long *)(lVar1 + 0x20);
      FUN_ffff00009077bce4();
      if (-1 < lVar1) {
        if ((param_3 < 0) && (param_3 = lVar1 + param_3, param_3 < 0)) {
          return 4;
        }
        lVar1 = FUN_ffff00009077bd60(param_2);
        if (lVar1 != 0) {
          lVar2 = FUN_ffff00009077bdf4(lVar1,param_5,param_3,param_4);
          FUN_ffff00009077bce4(lVar1);
          if (lVar2 < param_4) {
            return 2;
          }
          *param_6 = lVar2;
          return 0;
        }
      }
    }
    return 3;
  }
  return 2;
}


