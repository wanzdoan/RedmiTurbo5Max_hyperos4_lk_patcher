
undefined8 FUN_ffff0000be01e220(long param_1,uint param_2)

{
  int iVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  if (param_1 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___LK_AB___s_failed__bctrl_i_ffff0000be0bacc5,
               s_read_write_partition_info_ffff0000be0b6309,0x8c,
               s_read_write_partition_info_ffff0000be0b6309);
    return 0xffffffea;
  }
  if (1 < param_2) {
    FUN_ffff0000be05fd98
              (s__s__d___LK_AB___s_failed__mode_i_ffff0000be0a6b82,
               s_read_write_partition_info_ffff0000be0b6309,0x91,
               s_read_write_partition_info_ffff0000be0b6309,1);
    return 0xffffffea;
  }
  lVar2 = FUN_ffff0000be05df6c(&DAT_ffff0000be0ac5ed);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___LK_AB__open_misc_fail_ffff0000be0c0611,
               s_read_write_partition_info_ffff0000be0b6309,0x9b);
    return 0xffffffed;
  }
  if (param_2 == 0) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    iVar1 = FUN_ffff0000be05e000(lVar2,&local_50,0x800,0x20);
    if (iVar1 < 1) {
      pcVar4 = s__s__d___LK_AB__read_boot_control_ffff0000be0a7d12;
      uVar5 = 0xa5;
      goto LAB_ffff0000be01e370;
    }
    if (local_50._4_4_ != 0x42414342) {
      if (local_50._4_4_ == 0) {
        FUN_ffff0000be05fd98
                  (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000be0af6d6,
                   s_read_write_partition_info_ffff0000be0b6309,0xb1);
        return 0;
      }
      FUN_ffff0000be01e844(&local_50);
      FUN_ffff0000be01e220(&local_50,1);
    }
    FUN_ffff0000be0607b0(param_1,&local_50,0x20);
  }
  else {
    lVar3 = FUN_ffff0000be05e088(lVar2,param_1,0x800,0x20);
    if (lVar3 < 1) {
      pcVar4 = s__s__d___LK_AB__bio_write_failed_ffff0000be0be18a;
      uVar5 = 0xbd;
LAB_ffff0000be01e370:
      FUN_ffff0000be05fd98(pcVar4,s_read_write_partition_info_ffff0000be0b6309,uVar5);
      FUN_ffff0000be05def0(lVar2);
      return 0xfffffffb;
    }
  }
  FUN_ffff0000be05def0(lVar2);
  return 0;
}


