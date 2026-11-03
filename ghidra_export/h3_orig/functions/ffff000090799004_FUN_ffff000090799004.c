// ffff000090799004  size=32  FUN_ffff000090799004

undefined4 FUN_ffff000090799004(ulong param_1,undefined4 param_2)

{
  if (*(long *)((param_1 & 0xffffffff) * 8 + -0xffff6f6f5648) != 0) {
    param_2 = 0;
  }
  return param_2;
}


