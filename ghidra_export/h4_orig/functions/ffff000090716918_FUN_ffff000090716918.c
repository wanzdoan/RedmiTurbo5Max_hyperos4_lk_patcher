// ffff000090716918  size=80  FUN_ffff000090716918

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090716918(void)

{
  long lVar1;
  ulong uVar2;
  
  uVar2 = cntpct_el0;
  lVar1 = (_DAT_ffff000090843690 >> 0x20) * (uVar2 & 0xffffffff) +
          (uVar2 >> 0x20) * (ulong)_DAT_ffff000090843698 +
          ((uVar2 & 0xffffffff) * (ulong)_DAT_ffff000090843698 >> 0x20);
  return ((int)_DAT_ffff000090843690 * (int)uVar2 +
          (int)(_DAT_ffff000090843690 >> 0x20) * (int)(uVar2 >> 0x20) + (int)((ulong)lVar1 >> 0x20))
         - ((int)lVar1 >> 0x1f);
}


