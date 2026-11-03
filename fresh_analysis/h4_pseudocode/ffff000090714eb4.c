
undefined8 FUN_ffff000090714eb4(undefined8 param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  
  lVar1 = FUN_ffff00009077d514(param_2);
  if (lVar1 == 0) {
    pcVar3 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
  }
  else {
    lVar2 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar2 == 0) {
      pcVar3 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
    }
    else {
      lVar2 = FUN_ffff00009077c0bc(lVar2,lVar1,param_1,param_2);
      if (0 < lVar2) {
        *param_3 = lVar1;
        return 1;
      }
      pcVar3 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
    }
  }
  FUN_ffff00009077e11c(pcVar3);
  return 0;
}


