
undefined8 FUN_ffff0000b80583f0(void)

{
  long lVar1;
  
  lVar1 = DAT_ffff0000b81b66f0;
  DAT_ffff0000b81b6670 = *(int *)(DAT_ffff0000b81b6678 + 0x20);
  if (DAT_ffff0000b81b6670 != 0) {
    *(int *)(DAT_ffff0000b81b6678 + 0x20) = DAT_ffff0000b81b6670;
    DataSynchronizationBarrier(3,3,0);
    if (DAT_ffff0000b81b67f8 != 0) {
      FUN_ffff0000b800ac94(&DAT_ffff0000b81b6640,0);
      DAT_ffff0000b81b67f8 =
           DAT_ffff0000b81b67f8 & (long)~(1 << (ulong)(*(uint *)(lVar1 + 0x3c) & 0x1f));
    }
  }
  return 1;
}


