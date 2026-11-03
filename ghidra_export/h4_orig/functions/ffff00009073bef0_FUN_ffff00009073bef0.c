// ffff00009073bef0  size=252  FUN_ffff00009073bef0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009073bef0(void)

{
  uint uVar1;
  uint uVar2;
  
  if (_DAT_ffff00009086e0e0 == 0xffffffff || _DAT_ffff00009086e0dc == -1) {
    uVar1 = 0;
  }
  else {
    uVar1 = thunk_FUN_ffff00009074b22c();
    FUN_ffff00009077e11c
              (s__s__get_devinfo_with_index1_____0_ffff0000907e94fb,
               s_get_efuse_dfd_disabled_ffff0000907dc8b9,uVar1);
    if (((_DAT_ffff00009086e0e4 == -1) || (_DAT_ffff00009086e0e8 == 0xffffffff)) ||
       (_DAT_ffff00009086e0ec == 0xffffffff)) {
      uVar1 = (1 << (ulong)(_DAT_ffff00009086e0e0 & 0x1f) & uVar1) >>
              (ulong)(_DAT_ffff00009086e0e0 & 0x1f);
    }
    else {
      uVar2 = thunk_FUN_ffff00009074b22c();
      FUN_ffff00009077e11c
                (s__s__get_devinfo_with_index2_____0_ffff0000907fb97a,
                 s_get_efuse_dfd_disabled_ffff0000907dc8b9,uVar2);
      uVar1 = (uint)((1 << (ulong)(_DAT_ffff00009086e0e0 & 0x1f) &
                      1 << (ulong)(_DAT_ffff00009086e0e8 & 0x1f) &
                      1 << (ulong)(_DAT_ffff00009086e0ec & 0x1f) & uVar1 & uVar2) != 0);
    }
  }
  return uVar1;
}


