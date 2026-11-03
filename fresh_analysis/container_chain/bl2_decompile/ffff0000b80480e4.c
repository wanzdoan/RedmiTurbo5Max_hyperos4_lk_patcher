
int FUN_ffff0000b80480e4(undefined8 param_1,int param_2)

{
  int iVar1;
  int iVar2;
  
  if (param_2 + 1U < 2) {
    iVar1 = -6;
  }
  else {
    iVar1 = FUN_ffff0000b8047008();
    if (-1 < iVar1) {
      iVar1 = -1;
      do {
        iVar1 = FUN_ffff0000b804749c(param_1,iVar1,0);
        if (iVar1 < 0) {
          return iVar1;
        }
        iVar2 = FUN_ffff0000b804779c(param_1,iVar1);
      } while (iVar2 != param_2);
    }
  }
  return iVar1;
}


