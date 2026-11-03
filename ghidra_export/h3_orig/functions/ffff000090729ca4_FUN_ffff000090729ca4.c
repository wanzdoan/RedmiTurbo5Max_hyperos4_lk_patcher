// ffff000090729ca4  size=60  FUN_ffff000090729ca4

undefined ** FUN_ffff000090729ca4(uint param_1)

{
  undefined **ppuVar1;
  
  if (2 < param_1) {
    return (undefined **)0x0;
  }
  ppuVar1 = (undefined **)0x0;
  if ((&PTR_DAT_ffff000090818e68)[(ulong)param_1 * 3] != (undefined *)0x0) {
    ppuVar1 = &PTR_DAT_ffff000090818e68 + (ulong)param_1 * 3;
  }
  return ppuVar1;
}


