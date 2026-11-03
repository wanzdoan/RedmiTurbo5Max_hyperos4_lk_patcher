// ffff00009073bc58  size=252  FUN_ffff00009073bc58

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009073bc58(void)

{
  uint uVar1;
  uint uVar2;
  
  if (_DAT_ffff00009086e048 == 0xffffffff || _DAT_ffff00009086e044 == -1) {
    uVar1 = 0;
  }
  else {
    uVar1 = thunk_FUN_ffff00009074af94();
    FUN_ffff00009077de54
              (s__s__get_devinfo_with_index1_____0_ffff0000907e93a0,
               s_get_efuse_dfd_disabled_ffff0000907dc7cc,uVar1);
    if (((_DAT_ffff00009086e04c == -1) || (_DAT_ffff00009086e050 == 0xffffffff)) ||
       (_DAT_ffff00009086e054 == 0xffffffff)) {
      uVar1 = (1 << (ulong)(_DAT_ffff00009086e048 & 0x1f) & uVar1) >>
              (ulong)(_DAT_ffff00009086e048 & 0x1f);
    }
    else {
      uVar2 = thunk_FUN_ffff00009074af94();
      FUN_ffff00009077de54
                (s__s__get_devinfo_with_index2_____0_ffff0000907fb7de,
                 s_get_efuse_dfd_disabled_ffff0000907dc7cc,uVar2);
      uVar1 = (uint)((1 << (ulong)(_DAT_ffff00009086e048 & 0x1f) &
                      1 << (ulong)(_DAT_ffff00009086e050 & 0x1f) &
                      1 << (ulong)(_DAT_ffff00009086e054 & 0x1f) & uVar1 & uVar2) != 0);
    }
  }
  return uVar1;
}


