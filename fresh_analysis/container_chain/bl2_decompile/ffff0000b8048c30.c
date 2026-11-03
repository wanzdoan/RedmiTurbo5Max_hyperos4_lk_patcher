
char * FUN_ffff0000b8048c30(uint param_1)

{
  if (0 < (int)param_1) {
    return s_<valid_offset_length>_ffff0000b809dd74;
  }
  if (param_1 != 0) {
    if ((0xffffffec < param_1) &&
       (*(char **)(&DAT_ffff0000b8101f50 + (ulong)-param_1 * 8) != (char *)0x0)) {
      return *(char **)(&DAT_ffff0000b8101f50 + (ulong)-param_1 * 8);
    }
    return s_<unknown_error>_ffff0000b8088770;
  }
  return s_<no_error>_ffff0000b80977cf;
}


