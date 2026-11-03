
char * FUN_ffff0000b803aca0(uint param_1)

{
  if (param_1 < 6) {
    return &DAT_ffff0000b80a6bcc + *(int *)(&DAT_ffff0000b80a6bcc + (long)(int)param_1 * 4);
  }
  return s_unknown_ffff0000b808906b;
}


