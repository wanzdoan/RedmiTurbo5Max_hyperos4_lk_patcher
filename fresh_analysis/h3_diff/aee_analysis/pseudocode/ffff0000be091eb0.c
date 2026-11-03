
long FUN_ffff0000be091eb0(int param_1)

{
  long lVar1;
  
  if (DAT_ffff0000be1b7da8 + param_1 < 0x4001) {
    lVar1 = DAT_ffff0000be1b7da0 + (ulong)DAT_ffff0000be1b7da8;
    DAT_ffff0000be1b7da8 = DAT_ffff0000be1b7da8 + param_1;
    FUN_ffff0000be060ae0(lVar1,0,param_1);
    return lVar1;
  }
  FUN_ffff0000be05fd98(s___s__Memory_is_not_enough_ffff0000be0b29af,&DAT_ffff0000be0bb811);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_sboot_s_ffff0000be0af295,0x59,&DAT_ffff0000be0a553d);
}


