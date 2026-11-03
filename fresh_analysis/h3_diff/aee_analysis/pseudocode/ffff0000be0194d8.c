
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be0194d8(uint param_1)

{
  int iVar1;
  
  if (0x3df < param_1) {
    return 0xfffffff8;
  }
  iVar1 = 1 << (ulong)(param_1 & 0x1f);
  if (param_1 < 0x20) {
    _DAT_ffff00000c450180 = iVar1;
    return 0;
  }
  *(int *)(&DAT_ffff00000c400180 + ((ulong)(param_1 >> 3) & 0x1ffffffc)) = iVar1;
  return 0;
}


