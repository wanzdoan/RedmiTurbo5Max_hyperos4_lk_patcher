// ffff000090781908  size=60  FUN_ffff000090781908

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090781908(undefined4 *param_1)

{
  undefined4 uVar1;
  
  if ((param_1 == (undefined4 *)0x0) || (DAT_ffff000090887060 != '\x01')) {
    uVar1 = 0;
  }
  else {
    uVar1 = 1;
    *param_1 = _DAT_ffff00009088706c;
  }
  return uVar1;
}


