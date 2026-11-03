
int FUN_ffff0000be019910(void)

{
  long lVar1;
  ulong uVar2;
  
  uVar2 = cntpct_el0;
  lVar1 = (DAT_ffff0000be10c6c8 >> 0x20) * (uVar2 & 0xffffffff) +
          (uVar2 >> 0x20) * (ulong)DAT_ffff0000be10c6d0 +
          ((uVar2 & 0xffffffff) * (ulong)DAT_ffff0000be10c6d0 >> 0x20);
  return ((int)DAT_ffff0000be10c6c8 * (int)uVar2 +
          (int)(DAT_ffff0000be10c6c8 >> 0x20) * (int)(uVar2 >> 0x20) + (int)((ulong)lVar1 >> 0x20))
         - ((int)lVar1 >> 0x1f);
}


