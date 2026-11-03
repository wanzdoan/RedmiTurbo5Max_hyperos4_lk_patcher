
uint FUN_ffff0000be02c4b0(void)

{
  uint uVar1;
  uint uVar2;
  
  if (DAT_ffff0000be1183a8 == 0xffffffff || DAT_ffff0000be1183a4 == -1) {
    uVar1 = 0;
  }
  else {
    uVar1 = thunk_FUN_ffff0000be0364a4();
    FUN_ffff0000be05fd98
              (s__s__get_devinfo_with_index1_____0_ffff0000be0b52c7,
               s_get_efuse_dfd_disabled_ffff0000be0ac840,uVar1);
    if (((DAT_ffff0000be1183ac == -1) || (DAT_ffff0000be1183b0 == 0xffffffff)) ||
       (DAT_ffff0000be1183b4 == 0xffffffff)) {
      uVar1 = (1 << (ulong)(DAT_ffff0000be1183a8 & 0x1f) & uVar1) >>
              (ulong)(DAT_ffff0000be1183a8 & 0x1f);
    }
    else {
      uVar2 = thunk_FUN_ffff0000be0364a4();
      FUN_ffff0000be05fd98
                (s__s__get_devinfo_with_index2_____0_ffff0000be0c1bd0,
                 s_get_efuse_dfd_disabled_ffff0000be0ac840,uVar2);
      uVar1 = (uint)((1 << (ulong)(DAT_ffff0000be1183a8 & 0x1f) &
                      1 << (ulong)(DAT_ffff0000be1183b0 & 0x1f) &
                      1 << (ulong)(DAT_ffff0000be1183b4 & 0x1f) & uVar1 & uVar2) != 0);
    }
  }
  return uVar1;
}


