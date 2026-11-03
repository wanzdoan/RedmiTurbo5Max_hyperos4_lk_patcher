
int FUN_ffff0000be055848(undefined8 param_1,ulong param_2,int *param_3)

{
  int iVar1;
  int iVar2;
  int local_18;
  int local_14;
  
  local_18 = 0;
  if (-1 < (int)param_2) {
    if (((param_2 & 0x80000003) != 0) ||
       (local_14 = (int)param_2, iVar1 = FUN_ffff0000be055660(param_1,param_2,&local_14), iVar1 != 1
       )) {
      return -4;
    }
    local_18 = local_14;
    if (local_14 < 0) {
      return local_14;
    }
  }
  do {
    while (iVar1 = local_18, iVar2 = FUN_ffff0000be055660(param_1,local_18,&local_18), iVar2 == 2) {
      if ((param_3 != (int *)0x0) && (iVar1 = *param_3, *param_3 = iVar1 + -1, iVar1 < 1)) {
        return local_18;
      }
    }
    if (iVar2 == 1) {
      if (param_3 == (int *)0x0) {
        return iVar1;
      }
      *param_3 = *param_3 + 1;
      return iVar1;
    }
  } while (iVar2 != 9);
  if (local_18 < 0) {
    if (local_18 == -8 && param_3 == (int *)0x0) {
      local_18 = -1;
    }
  }
  else {
    local_18 = -1;
  }
  return local_18;
}


