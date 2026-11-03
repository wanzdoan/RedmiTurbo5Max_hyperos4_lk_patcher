
undefined8 FUN_ffff0000b800f7c0(long param_1,uint param_2)

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
    FUN_ffff0000b804c33c
              (s__s__d___LK_AB___s_failed__bctrl_i_ffff0000b8097d7d,
               s_read_write_partition_info_ffff0000b809420e,0x8c,
               s_read_write_partition_info_ffff0000b809420e);
    return 0xffffffea;
  }
  if (1 < param_2) {
    FUN_ffff0000b804c33c
              (s__s__d___LK_AB___s_failed__mode_i_ffff0000b8087e7c,
               s_read_write_partition_info_ffff0000b809420e,0x91,
               s_read_write_partition_info_ffff0000b809420e,1);
    return 0xffffffea;
  }
  lVar2 = FUN_ffff0000b804a99c(&DAT_ffff0000b808c3c3);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c
              (s__s__d___LK_AB__open_misc_fail_ffff0000b809c26b,
               s_read_write_partition_info_ffff0000b809420e,0x9b);
    return 0xffffffed;
  }
  if (param_2 == 0) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    iVar1 = FUN_ffff0000b804aa30(lVar2,&local_50,0x800,0x20);
    if (iVar1 < 1) {
      pcVar4 = s__s__d___LK_AB__read_boot_control_ffff0000b8088b08;
      uVar5 = 0xa5;
      goto LAB_ffff0000b800f910;
    }
    if (local_50._4_4_ != 0x42414342) {
      if (local_50._4_4_ == 0) {
        FUN_ffff0000b804c33c
                  (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000b808e8fb,
                   s_read_write_partition_info_ffff0000b809420e,0xb1);
        return 0;
      }
      FUN_ffff0000b800fc04(&local_50);
      FUN_ffff0000b800f7c0(&local_50,1);
    }
    FUN_ffff0000b804cd40(param_1,&local_50,0x20);
  }
  else {
    lVar3 = FUN_ffff0000b804aab8(lVar2,param_1,0x800,0x20);
    if (lVar3 < 1) {
      pcVar4 = s__s__d___LK_AB__bio_write_failed_ffff0000b809a753;
      uVar5 = 0xbd;
LAB_ffff0000b800f910:
      FUN_ffff0000b804c33c(pcVar4,s_read_write_partition_info_ffff0000b809420e,uVar5);
      FUN_ffff0000b804a920(lVar2);
      return 0xfffffffb;
    }
  }
  FUN_ffff0000b804a920(lVar2);
  return 0;
}


