
int FUN_ffff0000be05653c(undefined8 param_1,int param_2)

{
  int iVar1;
  int iVar2;
  
  if (param_2 + 1U < 2) {
    iVar1 = -6;
  }
  else {
    iVar1 = FUN_ffff0000be0553b4();
    if (-1 < iVar1) {
      iVar1 = -1;
      do {
        iVar1 = FUN_ffff0000be055848(param_1,iVar1,0);
        if (iVar1 < 0) {
          return iVar1;
        }
        iVar2 = FUN_ffff0000be055c80(param_1,iVar1);
      } while (iVar2 != param_2);
    }
  }
  return iVar1;
}


