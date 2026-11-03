
int FUN_ffff0000be085ac4(long param_1,long param_2,ulong param_3)

{
  int iVar1;
  
  iVar1 = 2;
  if ((param_1 != 0) && (param_2 != 0)) {
    if (param_3 == 0) {
      iVar1 = 1;
    }
    else {
      for (; 0xfffff0 < param_3; param_3 = param_3 - 0xfffff0) {
        iVar1 = FUN_ffff0000be093058(param_1,param_2,0xfffff0);
        if (iVar1 != 0) goto LAB_ffff0000be085b60;
        param_2 = param_2 + 0xfffff0;
      }
      iVar1 = FUN_ffff0000be093058(param_1,param_2,param_3 & 0xffffffff);
      if (iVar1 == 0) {
        return 0;
      }
    }
  }
LAB_ffff0000be085b60:
  FUN_ffff0000be093c50(0xffff000010000000,0);
  return iVar1;
}


