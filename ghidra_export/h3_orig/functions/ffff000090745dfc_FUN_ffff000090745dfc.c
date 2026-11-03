// ffff000090745dfc  size=76  FUN_ffff000090745dfc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

char * FUN_ffff000090745dfc(void)

{
  uint uVar1;
  
  uVar1 = (_DAT_ffff00001c010024 >> 0x1a & 7) - 1;
  if (uVar1 < 7) {
    return &DAT_ffff000090805d88 + *(int *)(&DAT_ffff000090805d88 + (long)(int)uVar1 * 4);
  }
  return s_rst_from__unknown_ffff0000907e4c75;
}


