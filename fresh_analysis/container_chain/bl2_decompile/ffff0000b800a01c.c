
int FUN_ffff0000b800a01c(void)

{
  long lVar1;
  ulong uVar2;
  
  uVar2 = cntpct_el0;
  lVar1 = (DAT_ffff0000b810ff08 >> 0x20) * (uVar2 & 0xffffffff) +
          (uVar2 >> 0x20) * (ulong)DAT_ffff0000b810ff10 +
          ((uVar2 & 0xffffffff) * (ulong)DAT_ffff0000b810ff10 >> 0x20);
  return ((int)DAT_ffff0000b810ff08 * (int)uVar2 +
          (int)(DAT_ffff0000b810ff08 >> 0x20) * (int)(uVar2 >> 0x20) + (int)((ulong)lVar1 >> 0x20))
         - ((int)lVar1 >> 0x1f);
}


