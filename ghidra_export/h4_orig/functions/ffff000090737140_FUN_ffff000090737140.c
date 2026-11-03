// ffff000090737140  size=40  FUN_ffff000090737140

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090737140(long param_1)

{
  undefined4 uVar1;
  
  if (param_1 == 0) {
    uVar1 = 0xfffffff8;
  }
  else {
    uVar1 = 0;
    _DAT_ffff00009086dc90 = param_1;
  }
  return uVar1;
}


