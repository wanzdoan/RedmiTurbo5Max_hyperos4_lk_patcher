// ffff0000907c115c  size=108  FUN_ffff0000907c115c

long FUN_ffff0000907c115c(long *param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  long lVar2;
  
  lVar2 = *param_1;
  while( true ) {
    if (lVar2 == 0) {
      return 0;
    }
    lVar2 = thunk_FUN_ffff00009077ef88();
    if ((lVar2 == param_3) &&
       (iVar1 = thunk_FUN_ffff00009077e820(*param_1,param_2,param_3), iVar1 == 0)) break;
    param_1 = param_1 + 1;
    lVar2 = *param_1;
  }
  return *param_1;
}


