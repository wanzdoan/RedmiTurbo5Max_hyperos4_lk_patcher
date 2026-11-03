
char * FUN_ffff00009076f0c8(uint param_1)

{
  if (0 < (int)param_1) {
    return s_<valid_offset_length>_ffff0000907fc43a;
  }
  if (param_1 != 0) {
    if ((0xffffffec < param_1) &&
       (*(char **)(&DAT_ffff000090832628 + (ulong)-param_1 * 8) != (char *)0x0)) {
      return *(char **)(&DAT_ffff000090832628 + (ulong)-param_1 * 8);
    }
    return s_<unknown_error>_ffff0000907d50ef;
  }
  return s_<no_error>_ffff0000907f053f;
}


