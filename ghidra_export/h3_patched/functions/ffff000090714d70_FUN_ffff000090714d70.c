// ffff000090714d70  size=160  FUN_ffff000090714d70

undefined8 FUN_ffff000090714d70(undefined8 param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  
  lVar1 = FUN_ffff00009077d24c(param_2);
  if (lVar1 == 0) {
    pcVar3 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5a2;
  }
  else {
    lVar2 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
    if (lVar2 == 0) {
      pcVar3 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
    }
    else {
      lVar2 = FUN_ffff00009077bdf4(lVar2,lVar1,param_1,param_2);
      if (0 < lVar2) {
        *param_3 = lVar1;
        return 1;
      }
      pcVar3 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
    }
  }
  FUN_ffff00009077de54(pcVar3);
  return 0;
}


