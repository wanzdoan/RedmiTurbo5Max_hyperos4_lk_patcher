// ffff00009071f608  size=232  FUN_ffff00009071f608

undefined * FUN_ffff00009071f608(void)

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
  iVar1 = FUN_ffff00009071f6f0(&local_30,0);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB__read_partition_in_ffff0000907da6eb,s_get_suffix_ffff0000907fb2df,
               0xcf,iVar1);
  }
  else {
    if (local_30._4_4_ == 0x42414342) {
      FUN_ffff00009077de54
                (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000907ddacc,s_get_suffix_ffff0000907fb2df,
                 0xdb,uStack_28._4_2_ & 0xf,uStack_28._6_2_ & 0xf);
      return &DAT_ffff000090804718 +
             *(int *)(&DAT_ffff000090804718 +
                     (ulong)((uStack_28._4_2_ & 0xf) < (uStack_28._6_2_ & 0xf)) * 4);
    }
    FUN_ffff00009077de54
              (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000907ef3ef,s_get_suffix_ffff0000907fb2df,
               0xd6,local_30._4_4_,0x42414342);
  }
  return &DAT_ffff0000907df27e;
}


