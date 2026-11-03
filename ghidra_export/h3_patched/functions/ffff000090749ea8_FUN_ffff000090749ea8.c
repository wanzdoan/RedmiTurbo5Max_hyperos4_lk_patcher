// ffff000090749ea8  size=44  FUN_ffff000090749ea8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749ea8(int param_1)

{
  _DAT_ffff000014032000 =
       _DAT_ffff000014032000 & 0xffffffe0 | _DAT_ffff000014032000 & 0xf | (uint)(param_1 != 0) << 4;
  return;
}


