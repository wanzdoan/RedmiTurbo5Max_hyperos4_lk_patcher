// ffff000090766644  size=480  FUN_ffff000090766644

undefined4 FUN_ffff000090766644(void)

{
  ulong uVar1;
  char *pcVar2;
  char *pcVar3;
  undefined8 uVar4;
  uint uVar5;
  undefined4 local_44;
  undefined8 local_40;
  undefined8 local_38;
  undefined8 local_30;
  int local_24;
  
  local_38 = 0;
  local_30 = 0;
  local_40 = 0;
  local_44 = 0;
  DAT_ffff00009087cc60 = 1;
  local_24 = 0;
  uVar1 = FUN_ffff000090765f28(&local_24,1);
  FUN_ffff00009077de54
            (s__s__d____s___partition_control_o_ffff0000907eb52c,
             s_GetBlackBoxPartitionControlConfi_ffff0000907e1ce7,0xa86,s_BLACKBOX_ffff0000907d9988,
             local_24);
  if ((uVar1 & 1) == 0) {
    return 0;
  }
  if (local_24 == 0) {
    return 0;
  }
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&local_40,local_24,0x18);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_partition_contr_ffff0000907d9b3e;
    pcVar3 = s_GetBlackBoxPartitionControlConfi_ffff0000907e1ce7;
    uVar4 = 0xa8d;
    goto LAB_ffff000090766718;
  }
  FUN_ffff00009077de54
            (s__s__d__EIOEntryCount_is___d_EIOM_ffff0000907d9b6f,s_VerityModeInit_ffff0000907ff948,
             0xab7,local_30 & 0xffffffff,local_30._4_4_);
  if (local_30._4_4_ < 3) {
    uVar5 = 2;
LAB_ffff000090766744:
    local_30 = CONCAT44(uVar5,(uint)local_30);
  }
  else {
    uVar5 = local_30._4_4_;
    if (10 < local_30._4_4_) {
      uVar5 = 10;
      goto LAB_ffff000090766744;
    }
  }
  if ((uint)local_30 < uVar5) {
    FUN_ffff00009077de54
              (s__s__d__EIOEntryCount_is_in_norma_ffff0000907fc085,s_VerityModeInit_ffff0000907ff948
               ,0xabf);
    return 1;
  }
  if ((uint)local_30 == uVar5) {
    FUN_ffff00009077de54
              (s__s__d__set_VerityMode_to_false_ffff0000907f5154,s_VerityModeInit_ffff0000907ff948,
               0xac1);
    DAT_ffff00009087cc60 = 0;
    return 1;
  }
  FUN_ffff00009077de54
            (s__s__d____s___EIOEntryCount_is_no_ffff0000907f1b2e,s_VerityModeInit_ffff0000907ff948,
             0xac4,s_BLACKBOX_ffff0000907d9988);
  local_30 = 0x200000000;
  local_40 = 0x7f9d3a6b2e1c8f60;
  local_38 = 0;
  FUN_ffff000090765f28(&local_44,1);
  uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_40,local_44,0x18);
  if ((uVar1 & 1) != 0) {
    return 1;
  }
  pcVar2 = s__s__d____s___write_black_box_par_ffff0000907d1ba6;
  pcVar3 = s_VerityModeInit_ffff0000907ff948;
  uVar4 = 0xac9;
LAB_ffff000090766718:
  FUN_ffff00009077de54(pcVar2,pcVar3,uVar4,s_BLACKBOX_ffff0000907d9988);
  return 0;
}


