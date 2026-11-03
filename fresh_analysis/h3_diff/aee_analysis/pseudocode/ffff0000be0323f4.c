
undefined8 FUN_ffff0000be0323f4(int param_1,uint param_2)

{
  do {
  } while (((*(uint **)(s_0123456789ABCDEF_ffff0000be0c82f0 + (long)param_1 * 8 + 0x10))[5] >> 5 & 1
           ) == 0);
  **(uint **)(s_0123456789ABCDEF_ffff0000be0c82f0 + (long)param_1 * 8 + 0x10) = param_2 & 0xff;
  return 0;
}


