// ffff000090706c00  size=92  FUN_ffff000090706c00

void FUN_ffff000090706c00(undefined8 param_1,ulong param_2,uint param_3)

{
  int iVar1;
  uint local_14;
  
  local_14 = 0;
  iVar1 = FUN_ffff0000907372bc(param_1,param_2,&local_14);
  if (-1 < iVar1) {
    local_14 = local_14 & (param_3 ^ 0xffffffff);
    FUN_ffff000090737284(param_1,param_2 & 0xffffffff);
  }
  return;
}


