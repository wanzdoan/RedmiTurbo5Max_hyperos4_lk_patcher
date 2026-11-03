// ffff000090738b90  size=64  FUN_ffff000090738b90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090738b90(uint param_1,int param_2)

{
  if (param_1 < 6) {
    if (param_1 - 1 == 0) {
      _DAT_ffff00009086de14 = param_2;
      return;
    }
    *(int *)(&DAT_ffff00009086de10 + (ulong)param_1 * 4) =
         *(int *)(&DAT_ffff00009086de10 + (ulong)(param_1 - 1) * 4) + param_2;
  }
  return;
}


