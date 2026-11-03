// ffff0000907c30b0  size=156  FUN_ffff0000907c30b0

long FUN_ffff0000907c30b0(long param_1,long param_2)

{
  long lVar1;
  
  if (param_1 != 0) {
    if (param_2 == 0) {
      *(undefined8 *)(*(long *)(param_1 + -0x10) + 0x10) = *(undefined8 *)(param_1 + -8);
      FUN_ffff0000907c3170();
      lVar1 = 0;
    }
    else {
      lVar1 = FUN_ffff0000907c2e74(param_2,0);
      if (lVar1 != 0) {
        FUN_ffff00009077eb20(lVar1,param_1,param_2);
        *(undefined8 *)(*(long *)(param_1 + -0x10) + 0x10) = *(undefined8 *)(param_1 + -8);
        FUN_ffff0000907c3170();
      }
    }
    return lVar1;
  }
  lVar1 = FUN_ffff0000907c2e74(param_2,0);
  return lVar1;
}


