
char * FUN_ffff0000be056c58(uint param_1)

{
  if (0 < (int)param_1) {
    return s_<valid_offset_length>_ffff0000be0c2361;
  }
  if (param_1 != 0) {
    if ((0xffffffec < param_1) &&
       (*(char **)(&DAT_ffff0000be0ef020 + (ulong)-param_1 * 8) != (char *)0x0)) {
      return *(char **)(&DAT_ffff0000be0ef020 + (ulong)-param_1 * 8);
    }
    return s_<unknown_error>_ffff0000be0a7660;
  }
  return s_<no_error>_ffff0000be0ba43f;
}


