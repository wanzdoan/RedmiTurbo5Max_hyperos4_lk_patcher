
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be034190(void)

{
  ulong uVar1;
  
  uVar1 = (ulong)_DAT_31800024;
  if ((&DAT_31800004 + uVar1 < (undefined1 *)0x31860000) &&
     (*(uint *)(&DAT_31800018 + uVar1) < 0x200001)) {
    FUN_ffff0000be0607b0
              (_DAT_3180004c + _DAT_31800048 + _DAT_31800050,
               (*(int *)(&DAT_31800014 + uVar1) - _DAT_31800004) + _DAT_31800048);
    return;
  }
  return;
}


