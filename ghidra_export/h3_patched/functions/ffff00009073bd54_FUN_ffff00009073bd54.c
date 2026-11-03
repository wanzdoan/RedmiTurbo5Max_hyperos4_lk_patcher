// ffff00009073bd54  size=76  FUN_ffff00009073bd54

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff00009073bd54(void)

{
  byte bVar1;
  
  if (_DAT_ffff00009086dfe8 < 0x1e) {
    return 0;
  }
  bVar1 = DAT_ffff00009086e0a8;
  if (_DAT_ffff00009086dff8 != 1) {
    bVar1 = DAT_ffff00009086e0a8 & DAT_ffff00009086e0a9;
  }
  return bVar1;
}


