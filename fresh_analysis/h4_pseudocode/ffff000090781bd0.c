
undefined4 FUN_ffff000090781bd0(undefined4 *param_1)

{
  undefined4 uVar1;
  
  if ((param_1 == (undefined4 *)0x0) || (DAT_ffff0000908870f8 != '\x01')) {
    uVar1 = 0;
  }
  else {
    uVar1 = 1;
    *param_1 = DAT_ffff000090887104;
  }
  return uVar1;
}


