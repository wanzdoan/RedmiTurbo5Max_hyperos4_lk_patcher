
int FUN_ffff0000907aaad0(long param_1,long param_2,ulong param_3)

{
  int iVar1;
  
  iVar1 = 2;
  if ((param_1 != 0) && (param_2 != 0)) {
    if (param_3 == 0) {
      iVar1 = 1;
    }
    else {
      for (; 0xfffff0 < param_3; param_3 = param_3 - 0xfffff0) {
        iVar1 = FUN_ffff0000907b9234(param_1,param_2,0xfffff0);
        if (iVar1 != 0) goto LAB_ffff0000907aab6c;
        param_2 = param_2 + 0xfffff0;
      }
      iVar1 = FUN_ffff0000907b9234(param_1,param_2,param_3 & 0xffffffff);
      if (iVar1 == 0) {
        return 0;
      }
    }
  }
LAB_ffff0000907aab6c:
  FUN_ffff0000907b9e2c(&DAT_ffff000010000000,0);
  return iVar1;
}


