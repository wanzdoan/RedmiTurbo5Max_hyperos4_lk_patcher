// ffff0000907b7a18  size=132  FUN_ffff0000907b7a18

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907b7a18(int param_1)

{
  long lVar1;
  
  if (_DAT_ffff00009090d828 + param_1 < 0x4001) {
    lVar1 = _DAT_ffff00009090d820 + (ulong)_DAT_ffff00009090d828;
    _DAT_ffff00009090d828 = _DAT_ffff00009090d828 + param_1;
    FUN_ffff00009077eb88(lVar1,0,param_1);
    return lVar1;
  }
  FUN_ffff00009077de54(s___s__Memory_is_not_enough_ffff0000907e57e4,&DAT_ffff0000907f20e7);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907e06cc,0x59,&DAT_ffff0000907d24c9);
}


