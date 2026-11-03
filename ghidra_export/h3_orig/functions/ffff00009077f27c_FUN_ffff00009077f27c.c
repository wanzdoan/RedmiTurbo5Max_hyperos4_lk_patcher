// ffff00009077f27c  size=140  FUN_ffff00009077f27c

long FUN_ffff00009077f27c(long param_1,undefined8 param_2)

{
  int iVar1;
  int iVar2;
  int iVar3;
  
  iVar1 = FUN_ffff00009077ef88(param_2);
  if (iVar1 != 0) {
    iVar2 = FUN_ffff00009077ef88(param_1);
    if (iVar1 <= iVar2) {
      iVar2 = iVar2 + 1;
      do {
        iVar3 = FUN_ffff00009077e820(param_1,param_2,(long)iVar1);
        if (iVar3 == 0) {
          return param_1;
        }
        param_1 = param_1 + 1;
        iVar2 = iVar2 + -1;
      } while (iVar1 < iVar2);
    }
    param_1 = 0;
  }
  return param_1;
}


