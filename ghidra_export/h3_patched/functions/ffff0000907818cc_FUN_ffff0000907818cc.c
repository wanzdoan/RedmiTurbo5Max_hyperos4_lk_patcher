// ffff0000907818cc  size=60  FUN_ffff0000907818cc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907818cc(undefined4 *param_1)

{
  undefined4 uVar1;
  
  if ((param_1 == (undefined4 *)0x0) || (DAT_ffff000090887060 != '\x01')) {
    uVar1 = 0;
  }
  else {
    uVar1 = 1;
    *param_1 = _DAT_ffff000090887064;
  }
  return uVar1;
}


