
long FUN_ffff0000b8073254(int param_1)

{
  long lVar1;
  
  if (DAT_ffff0000b81b9ad0 + param_1 < 0x4001) {
    lVar1 = DAT_ffff0000b81b9ac8 + (ulong)DAT_ffff0000b81b9ad0;
    DAT_ffff0000b81b9ad0 = DAT_ffff0000b81b9ad0 + param_1;
    FUN_ffff0000b804d070(lVar1,0,param_1);
    return lVar1;
  }
  FUN_ffff0000b804c33c(s___s__Memory_is_not_enough_ffff0000b80913f3,&DAT_ffff0000b80987e6);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_sboot_s_ffff0000b808e778,0x59,&DAT_ffff0000b8086efb);
}


