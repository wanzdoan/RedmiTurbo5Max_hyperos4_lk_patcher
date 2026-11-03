// ffff0000907946dc  size=136  FUN_ffff0000907946dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907946dc(void)

{
  long lVar1;
  
  lVar1 = _DAT_ffff00009090a330;
  _DAT_ffff00009090a2b0 = *(int *)(_DAT_ffff00009090a2b8 + 0x20);
  if (_DAT_ffff00009090a2b0 != 0) {
    *(int *)(_DAT_ffff00009090a2b8 + 0x20) = _DAT_ffff00009090a2b0;
    DataSynchronizationBarrier(3,3,0);
    if (_DAT_ffff00009090a438 != 0) {
      FUN_ffff000090717624(0xffff00009090a280,0);
      _DAT_ffff00009090a438 =
           _DAT_ffff00009090a438 & (long)~(1 << (ulong)(*(uint *)(lVar1 + 0x3c) & 0x1f));
    }
  }
  return 1;
}


