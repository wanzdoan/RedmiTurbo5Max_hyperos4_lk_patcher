// ffff00009076e044  size=140  FUN_ffff00009076e044

int FUN_ffff00009076e044(undefined8 param_1,int param_2)

{
  int iVar1;
  int iVar2;
  
  if (param_2 + 1U < 2) {
    iVar1 = -6;
  }
  else {
    iVar1 = FUN_ffff00009076ca9c();
    if (-1 < iVar1) {
      iVar1 = -1;
      do {
        iVar1 = FUN_ffff00009076cf30(param_1,iVar1,0);
        if (iVar1 < 0) {
          return iVar1;
        }
        iVar2 = FUN_ffff00009076d3e4(param_1,iVar1);
      } while (iVar2 != param_2);
    }
  }
  return iVar1;
}


