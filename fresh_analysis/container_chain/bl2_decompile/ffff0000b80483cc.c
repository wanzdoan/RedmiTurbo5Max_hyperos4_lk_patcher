
int FUN_ffff0000b80483cc(undefined8 param_1,int param_2,undefined8 param_3)

{
  int iVar1;
  int iVar2;
  
  iVar1 = FUN_ffff0000b8047008();
  if (-1 < iVar1) {
    do {
      param_2 = FUN_ffff0000b804749c(param_1,param_2,0);
      if (param_2 < 0) {
        return param_2;
      }
      iVar2 = FUN_ffff0000b80482b0(param_1,param_2,param_3);
      if (iVar2 < -1) {
        return iVar2;
      }
      iVar1 = param_2;
    } while (iVar2 != 0);
  }
  return iVar1;
}


