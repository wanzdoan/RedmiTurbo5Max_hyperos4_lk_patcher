// ffff00009077ee84  size=152  FUN_ffff00009077ee84

long FUN_ffff00009077ee84(long param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar1 = FUN_ffff00009077f16c(param_1,param_3);
  if (param_3 == 0) {
    lVar2 = FUN_ffff00009077ef88(param_2);
  }
  else {
    lVar2 = 0;
    param_3 = param_3 + -1;
    lVar3 = lVar2;
    if (param_3 != 0) {
      do {
        lVar3 = lVar2;
        if (*(char *)(param_2 + lVar2) == '\0') break;
        *(char *)(param_1 + lVar1 + lVar2) = *(char *)(param_2 + lVar2);
        lVar2 = lVar2 + 1;
        lVar3 = param_3;
      } while (param_3 != lVar2);
    }
    lVar2 = lVar3 + lVar1;
    *(undefined1 *)(param_1 + lVar1 + lVar3) = 0;
    lVar1 = FUN_ffff00009077ef88(param_2 + lVar3);
  }
  return lVar2 + lVar1;
}


