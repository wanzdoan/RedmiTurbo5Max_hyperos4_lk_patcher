
int FUN_ffff0000be056824(undefined8 param_1,int param_2,undefined8 param_3)

{
  int iVar1;
  int iVar2;
  
  iVar1 = FUN_ffff0000be0553b4();
  if (-1 < iVar1) {
    do {
      param_2 = FUN_ffff0000be055848(param_1,param_2,0);
      if (param_2 < 0) {
        return param_2;
      }
      iVar2 = FUN_ffff0000be056708(param_1,param_2,param_3);
      if (iVar2 < -1) {
        return iVar2;
      }
      iVar1 = param_2;
    } while (iVar2 != 0);
  }
  return iVar1;
}


