// ffff00009074a140  size=44  FUN_ffff00009074a140

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074a140(int param_1)

{
  _DAT_ffff000014032000 =
       _DAT_ffff000014032000 & 0xffffffe0 | _DAT_ffff000014032000 & 0xf | (uint)(param_1 != 0) << 4;
  return;
}


