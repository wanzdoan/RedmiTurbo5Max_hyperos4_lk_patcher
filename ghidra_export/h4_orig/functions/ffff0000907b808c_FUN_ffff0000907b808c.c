// ffff0000907b808c  size=132  FUN_ffff0000907b808c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907b808c(int param_1)

{
  long lVar1;
  
  if (_DAT_ffff00009090d8e8 + param_1 < 0x4001) {
    lVar1 = _DAT_ffff00009090d8e0 + (ulong)_DAT_ffff00009090d8e8;
    _DAT_ffff00009090d8e8 = _DAT_ffff00009090d8e8 + param_1;
    FUN_ffff00009077ee50(lVar1,0,param_1);
    return lVar1;
  }
  FUN_ffff00009077e11c(s___s__Memory_is_not_enough_ffff0000907e58ff,&DAT_ffff0000907f228a);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907e0786,0x59,&DAT_ffff0000907d2506);
}


