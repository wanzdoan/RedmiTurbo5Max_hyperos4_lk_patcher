// ffff00009075d3c0  size=272  FUN_ffff00009075d3c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075d3c0(void)

{
  undefined8 uVar1;
  
  if ((DAT_ffff00009087beb8 & 1) == 0) {
    FUN_ffff00009077de54
              (s_skip__s_before_init_ffff0000907d9946,s_primary_display_suspend_ffff0000907e8096);
    uVar1 = 0xffffffff;
  }
  else {
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff00009075c044(_DAT_ffff00009087bed8);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff000090755a44(_DAT_ffff00009087bef0,0);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff000090756124(_DAT_ffff00009087bef0,0);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    uVar1 = 0;
    _DAT_ffff00009087bec8 = 0;
  }
  return uVar1;
}


