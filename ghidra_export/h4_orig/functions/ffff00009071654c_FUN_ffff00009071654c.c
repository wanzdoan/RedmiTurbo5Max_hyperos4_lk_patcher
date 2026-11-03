// ffff00009071654c  size=108  FUN_ffff00009071654c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009071654c(uint param_1)

{
  int iVar1;
  
  if (0x3df < param_1) {
    return 0xfffffff8;
  }
  iVar1 = 1 << (ulong)(param_1 & 0x1f);
  if (param_1 < 0x20) {
    _DAT_ffff00000c450100 = iVar1;
    return 0;
  }
  *(int *)(((ulong)(param_1 >> 3) & 0x1ffffffc) - 0xfffff3bfff00) = iVar1;
  return 0;
}


