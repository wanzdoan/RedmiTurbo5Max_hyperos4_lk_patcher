
undefined8 FUN_ffff0000be099878(long param_1,long param_2,long *param_3)

{
  undefined8 uVar1;
  long lVar2;
  
  uVar1 = 2;
  if ((param_1 != 0) && (param_3 != (long *)0x0)) {
    lVar2 = FUN_ffff0000be05df6c(param_1);
    if (lVar2 != 0) {
      lVar2 = *(long *)(lVar2 + 0x20);
      FUN_ffff0000be05def0();
      if (-1 < lVar2) {
        if ((param_2 < 0) && (param_2 = lVar2 + param_2, param_2 < 0)) {
          return 4;
        }
        *param_3 = param_2;
        return 0;
      }
    }
    uVar1 = 3;
  }
  return uVar1;
}


