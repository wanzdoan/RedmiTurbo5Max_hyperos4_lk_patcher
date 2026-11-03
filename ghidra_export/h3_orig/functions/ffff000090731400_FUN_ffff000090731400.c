// ffff000090731400  size=44  FUN_ffff000090731400

undefined4 FUN_ffff000090731400(uint param_1)

{
  if (7 < param_1) {
    return 0;
  }
  return *(undefined4 *)(&DAT_ffff000090803dc8 + (ulong)param_1 * 4);
}


