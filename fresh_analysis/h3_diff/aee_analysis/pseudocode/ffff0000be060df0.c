
long FUN_ffff0000be060df0(long param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  
  if (param_3 == 0) {
    lVar1 = FUN_ffff0000be060e5c(param_2);
    return lVar1;
  }
  lVar1 = 0;
  param_3 = param_3 + -1;
  lVar2 = lVar1;
  if (param_3 != 0) {
    do {
      lVar2 = lVar1;
      if (*(char *)(param_2 + lVar1) == '\0') break;
      *(char *)(param_1 + lVar1) = *(char *)(param_2 + lVar1);
      lVar1 = lVar1 + 1;
      lVar2 = param_3;
    } while (param_3 != lVar1);
  }
  *(undefined1 *)(param_1 + lVar2) = 0;
  lVar1 = FUN_ffff0000be060e5c(param_2 + lVar2);
  return lVar1 + lVar2;
}


