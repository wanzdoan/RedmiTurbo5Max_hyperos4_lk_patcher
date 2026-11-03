
char * FUN_ffff0000be044064(uint param_1)

{
  if (param_1 < 6) {
    return &DAT_ffff0000be0cc35c + *(int *)(&DAT_ffff0000be0cc35c + (long)(int)param_1 * 4);
  }
  return s_unknown_ffff0000be0a8186;
}


