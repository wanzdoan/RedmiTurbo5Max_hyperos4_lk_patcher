// ffff000090781bd0  size=60  FUN_ffff000090781bd0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090781bd0(undefined4 *param_1)

{
  undefined4 uVar1;
  
  if ((param_1 == (undefined4 *)0x0) || (DAT_ffff0000908870f8 != '\x01')) {
    uVar1 = 0;
  }
  else {
    uVar1 = 1;
    *param_1 = _DAT_ffff000090887104;
  }
  return uVar1;
}


