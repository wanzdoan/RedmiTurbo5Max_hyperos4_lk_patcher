
undefined * FUN_ffff0000b800f6d8(void)

{
  int iVar1;
  undefined8 local_30;
  undefined8 uStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  local_20 = 0;
  uStack_18 = 0;
  local_30 = 0;
  uStack_28 = 0;
  iVar1 = FUN_ffff0000b800f7c0(&local_30,0);
  if (iVar1 < 0) {
    FUN_ffff0000b804c33c
              (s__s__d___LK_AB__read_partition_in_ffff0000b808b557,s_get_suffix_ffff0000b809d48b,
               0xcf,iVar1);
  }
  else {
    if (local_30._4_4_ == 0x42414342) {
      FUN_ffff0000b804c33c
                (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000b808cdca,s_get_suffix_ffff0000b809d48b,
                 0xdb,uStack_28._4_2_ & 0xf,uStack_28._6_2_ & 0xf);
      return &DAT_ffff0000b80a2548 +
             *(int *)(&DAT_ffff0000b80a2548 +
                     (ulong)((uStack_28._4_2_ & 0xf) < (uStack_28._6_2_ & 0xf)) * 4);
    }
    FUN_ffff0000b804c33c
              (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000b8096eb4,s_get_suffix_ffff0000b809d48b,
               0xd6,local_30._4_4_,0x42414342);
  }
  return &DAT_ffff0000b808dbfe;
}


