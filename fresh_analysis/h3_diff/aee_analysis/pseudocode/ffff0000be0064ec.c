
void FUN_ffff0000be0064ec(undefined8 param_1,ulong param_2,uint param_3)

{
  int iVar1;
  uint local_14;
  
  local_14 = 0;
  iVar1 = FUN_ffff0000be029958(param_1,param_2,&local_14);
  if (-1 < iVar1) {
    local_14 = local_14 & (param_3 ^ 0xffffffff);
    FUN_ffff0000be029920(param_1,param_2 & 0xffffffff);
  }
  return;
}


