
undefined8 FUN_ffff0000be072060(void)

{
  long lVar1;
  
  lVar1 = DAT_ffff0000be1b49b0;
  DAT_ffff0000be1b4930 = *(int *)(DAT_ffff0000be1b4938 + 0x20);
  if (DAT_ffff0000be1b4930 != 0) {
    *(int *)(DAT_ffff0000be1b4938 + 0x20) = DAT_ffff0000be1b4930;
    DataSynchronizationBarrier(3,3,0);
    if (DAT_ffff0000be1b4ab8 != 0) {
      FUN_ffff0000be01a61c(&DAT_ffff0000be1b4900,0);
      DAT_ffff0000be1b4ab8 =
           DAT_ffff0000be1b4ab8 & (long)~(1 << (ulong)(*(uint *)(lVar1 + 0x3c) & 0x1f));
    }
  }
  return 1;
}


