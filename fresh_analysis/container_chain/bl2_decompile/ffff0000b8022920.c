
uint FUN_ffff0000b8022920(void)

{
  uint uVar1;
  uint uVar2;
  
  if (DAT_ffff0000b811bb70 == 0xffffffff || DAT_ffff0000b811bb6c == -1) {
    uVar1 = 0;
  }
  else {
    uVar1 = thunk_FUN_ffff0000b802ce0c();
    FUN_ffff0000b804c33c
              (s__s__get_devinfo_with_index1_____0_ffff0000b809352b,
               s_get_efuse_dfd_disabled_ffff0000b808c6fb,uVar1);
    if (((DAT_ffff0000b811bb74 == -1) || (DAT_ffff0000b811bb78 == 0xffffffff)) ||
       (DAT_ffff0000b811bb7c == 0xffffffff)) {
      uVar1 = (1 << (ulong)(DAT_ffff0000b811bb70 & 0x1f) & uVar1) >>
              (ulong)(DAT_ffff0000b811bb70 & 0x1f);
    }
    else {
      uVar2 = thunk_FUN_ffff0000b802ce0c();
      FUN_ffff0000b804c33c
                (s__s__get_devinfo_with_index2_____0_ffff0000b809d7a8,
                 s_get_efuse_dfd_disabled_ffff0000b808c6fb,uVar2);
      uVar1 = (uint)((1 << (ulong)(DAT_ffff0000b811bb70 & 0x1f) &
                      1 << (ulong)(DAT_ffff0000b811bb78 & 0x1f) &
                      1 << (ulong)(DAT_ffff0000b811bb7c & 0x1f) & uVar1 & uVar2) != 0);
    }
  }
  return uVar1;
}


