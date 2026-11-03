// ffff00009071f6f0  size=428  FUN_ffff00009071f6f0

undefined8 FUN_ffff00009071f6f0(long param_1,uint param_2)

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
    FUN_ffff00009077de54
              (s__s__d___LK_AB___s_failed__bctrl_i_ffff0000907f0ee3,
               s_read_write_partition_info_ffff0000907ea83b,0x8c,
               s_read_write_partition_info_ffff0000907ea83b);
    return 0xffffffea;
  }
  if (1 < param_2) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB___s_failed__mode_i_ffff0000907d40f9,
               s_read_write_partition_info_ffff0000907ea83b,0x91,
               s_read_write_partition_info_ffff0000907ea83b,1);
    return 0xffffffea;
  }
  lVar2 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe5c);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB__open_misc_fail_ffff0000907f9557,
               s_read_write_partition_info_ffff0000907ea83b,0x9b);
    return 0xffffffed;
  }
  if (param_2 == 0) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    iVar1 = FUN_ffff00009077bdf4(lVar2,&local_50,0x800,0x20);
    if (iVar1 < 1) {
      pcVar4 = s__s__d___LK_AB__read_boot_control_ffff0000907d5c2d;
      uVar5 = 0xa5;
      goto LAB_ffff00009071f840;
    }
    if (local_50._4_4_ != 0x42414342) {
      if (local_50._4_4_ == 0) {
        FUN_ffff00009077de54
                  (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000907e0d9c,
                   s_read_write_partition_info_ffff0000907ea83b,0xb1);
        return 0;
      }
      FUN_ffff00009071fe00(&local_50);
      FUN_ffff00009071f6f0(&local_50,1);
    }
    FUN_ffff00009077e858(param_1,&local_50,0x20);
  }
  else {
    lVar3 = FUN_ffff00009077be7c(lVar2,param_1,0x800,0x20);
    if (lVar3 < 1) {
      pcVar4 = s__s__d___LK_AB__bio_write_failed_ffff0000907f5f1a;
      uVar5 = 0xbd;
LAB_ffff00009071f840:
      FUN_ffff00009077de54(pcVar4,s_read_write_partition_info_ffff0000907ea83b,uVar5);
      FUN_ffff00009077bce4(lVar2);
      return 0xfffffffb;
    }
  }
  FUN_ffff00009077bce4(lVar2);
  return 0;
}


