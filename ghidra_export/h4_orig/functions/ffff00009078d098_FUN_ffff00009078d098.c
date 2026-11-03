// ffff00009078d098  size=48  FUN_ffff00009078d098

bool FUN_ffff00009078d098(long param_1)

{
  long lVar1;
  
  if (*(ulong *)(param_1 + 0x20) < *(ulong *)(param_1 + 0x18)) {
    lVar1 = *(ulong *)(param_1 + 0x20) + 0x10;
  }
  else {
    lVar1 = *(long *)(param_1 + 0x10);
  }
  return lVar1 == *(long *)(param_1 + 0x28);
}


