
undefined8 FUN_ffff0000907946dc(void)

{
  long lVar1;
  
  lVar1 = DAT_ffff00009090a330;
  DAT_ffff00009090a2b0 = *(int *)(DAT_ffff00009090a2b8 + 0x20);
  if (DAT_ffff00009090a2b0 != 0) {
    *(int *)(DAT_ffff00009090a2b8 + 0x20) = DAT_ffff00009090a2b0;
    DataSynchronizationBarrier(3,3,0);
    if (DAT_ffff00009090a438 != 0) {
      FUN_ffff000090717624(&DAT_ffff00009090a280,0);
      DAT_ffff00009090a438 =
           DAT_ffff00009090a438 & (long)~(1 << (ulong)(*(uint *)(lVar1 + 0x3c) & 0x1f));
    }
  }
  return 1;
}


