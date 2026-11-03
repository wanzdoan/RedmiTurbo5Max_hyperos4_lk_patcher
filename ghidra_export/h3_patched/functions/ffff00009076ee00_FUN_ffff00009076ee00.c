// ffff00009076ee00  size=100  FUN_ffff00009076ee00

char * FUN_ffff00009076ee00(uint param_1)

{
  if (0 < (int)param_1) {
    return s_<valid_offset_length>_ffff0000907fc29e;
  }
  if (param_1 != 0) {
    if ((0xffffffec < param_1) &&
       (*(char **)(&DAT_ffff000090832608 + (ulong)-param_1 * 8) != (char *)0x0)) {
      return *(char **)(&DAT_ffff000090832608 + (ulong)-param_1 * 8);
    }
    return s_<unknown_error>_ffff0000907d50b2;
  }
  return s_<no_error>_ffff0000907f03b7;
}


