
void FUN_ffff0000b8006770(undefined8 param_1,ulong param_2,uint param_3)

{
  int iVar1;
  uint local_14;
  
  local_14 = 0;
  iVar1 = FUN_ffff0000b802120c(param_1,param_2,&local_14);
  if (-1 < iVar1) {
    local_14 = local_14 & (param_3 ^ 0xffffffff);
    FUN_ffff0000b80211d4(param_1,param_2 & 0xffffffff);
  }
  return;
}


