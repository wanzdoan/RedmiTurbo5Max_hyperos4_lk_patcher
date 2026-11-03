// ffff000090758df0  size=52  FUN_ffff000090758df0

char * FUN_ffff000090758df0(uint param_1)

{
  if (param_1 < 6) {
    return &DAT_ffff00009080e1fc + *(int *)(&DAT_ffff00009080e1fc + (long)(int)param_1 * 4);
  }
  return s_unknown_ffff0000907d64e8;
}


