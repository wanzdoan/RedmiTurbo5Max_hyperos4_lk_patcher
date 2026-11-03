// ffff0000907427fc  size=56  FUN_ffff0000907427fc

uint FUN_ffff0000907427fc(int param_1,uint param_2)

{
  do {
    if (((*(uint **)(s_0123456789ABCDEF_ffff000090804b08 + (long)param_1 * 8 + 0x10))[5] & 1) != 0)
    {
      return **(uint **)(s_0123456789ABCDEF_ffff000090804b08 + (long)param_1 * 8 + 0x10) & 0xff;
    }
  } while ((param_2 & 1) != 0);
  return 0xffffffff;
}


