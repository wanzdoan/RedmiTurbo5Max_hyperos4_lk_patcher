// ffff000090749e7c  size=44  FUN_ffff000090749e7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749e7c(int param_1)

{
  _DAT_ffff000014012004 =
       _DAT_ffff000014012004 & 0xffffffe0 | _DAT_ffff000014012004 & 0xf | (uint)(param_1 != 0) << 4;
  return;
}


