// ffff000090742a68  size=44  FUN_ffff000090742a68

undefined8 FUN_ffff000090742a68(int param_1,uint param_2)

{
  do {
  } while (((*(uint **)(s_0123456789ABCDEF_ffff000090804d30 + (long)param_1 * 8 + 0x10))[5] >> 5 & 1
           ) == 0);
  **(uint **)(s_0123456789ABCDEF_ffff000090804d30 + (long)param_1 * 8 + 0x10) = param_2 & 0xff;
  return 0;
}


