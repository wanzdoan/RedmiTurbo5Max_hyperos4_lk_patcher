// ffff00009079441c  size=136  FUN_ffff00009079441c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009079441c(void)

{
  long lVar1;
  
  lVar1 = _DAT_ffff00009090a270;
  _DAT_ffff00009090a1f0 = *(int *)(_DAT_ffff00009090a1f8 + 0x20);
  if (_DAT_ffff00009090a1f0 != 0) {
    *(int *)(_DAT_ffff00009090a1f8 + 0x20) = _DAT_ffff00009090a1f0;
    DataSynchronizationBarrier(3,3,0);
    if (_DAT_ffff00009090a378 != 0) {
      FUN_ffff0000907174e0(0xffff00009090a1c0,0);
      _DAT_ffff00009090a378 =
           _DAT_ffff00009090a378 & (long)~(1 << (ulong)(*(uint *)(lVar1 + 0x3c) & 0x1f));
    }
  }
  return 1;
}


