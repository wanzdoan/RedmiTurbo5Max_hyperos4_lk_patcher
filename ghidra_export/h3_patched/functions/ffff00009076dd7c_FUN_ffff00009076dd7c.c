// ffff00009076dd7c  size=140  FUN_ffff00009076dd7c

int FUN_ffff00009076dd7c(undefined8 param_1,int param_2)

{
  int iVar1;
  int iVar2;
  
  if (param_2 + 1U < 2) {
    iVar1 = -6;
  }
  else {
    iVar1 = FUN_ffff00009076c7d4();
    if (-1 < iVar1) {
      iVar1 = -1;
      do {
        iVar1 = FUN_ffff00009076cc68(param_1,iVar1,0);
        if (iVar1 < 0) {
          return iVar1;
        }
        iVar2 = FUN_ffff00009076d11c(param_1,iVar1);
      } while (iVar2 != param_2);
    }
  }
  return iVar1;
}


