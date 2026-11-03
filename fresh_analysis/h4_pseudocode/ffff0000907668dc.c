
undefined4 FUN_ffff0000907668dc(void)

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
  DAT_ffff00009087ccf8 = 1;
  local_24 = 0;
  uVar1 = FUN_ffff0000907661c0(&local_24,1);
  FUN_ffff00009077e11c
            (s__s__d____s___partition_control_o_ffff0000907eb6c6,
             s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf,0xa86,s_BLACKBOX_ffff0000907d9a1f,
             local_24);
  if ((uVar1 & 1) == 0) {
    return 0;
  }
  if (local_24 == 0) {
    return 0;
  }
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_40,local_24,0x18);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_partition_contr_ffff0000907d9bd5;
    pcVar3 = s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf;
    uVar4 = 0xa8d;
    goto LAB_ffff0000907669b0;
  }
  FUN_ffff00009077e11c
            (s__s__d__EIOEntryCount_is___d_EIOM_ffff0000907d9c06,s_VerityModeInit_ffff0000907ffaf0,
             0xab7,local_30 & 0xffffffff,local_30._4_4_);
  if (local_30._4_4_ < 3) {
    uVar5 = 2;
LAB_ffff0000907669dc:
    local_30 = CONCAT44(uVar5,(uint)local_30);
  }
  else {
    uVar5 = local_30._4_4_;
    if (10 < local_30._4_4_) {
      uVar5 = 10;
      goto LAB_ffff0000907669dc;
    }
  }
  if ((uint)local_30 < uVar5) {
    FUN_ffff00009077e11c
              (s__s__d__EIOEntryCount_is_in_norma_ffff0000907fc221,s_VerityModeInit_ffff0000907ffaf0
               ,0xabf);
    return 1;
  }
  if ((uint)local_30 == uVar5) {
    FUN_ffff00009077e11c
              (s__s__d__set_VerityMode_to_false_ffff0000907f52d8,s_VerityModeInit_ffff0000907ffaf0,
               0xac1);
    DAT_ffff00009087ccf8 = 0;
    return 1;
  }
  FUN_ffff00009077e11c
            (s__s__d____s___EIOEntryCount_is_no_ffff0000907f1cb6,s_VerityModeInit_ffff0000907ffaf0,
             0xac4,s_BLACKBOX_ffff0000907d9a1f);
  local_30 = 0x200000000;
  local_40 = 0x7f9d3a6b2e1c8f60;
  local_38 = 0;
  FUN_ffff0000907661c0(&local_44,1);
  uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_40,local_44,0x18);
  if ((uVar1 & 1) != 0) {
    return 1;
  }
  pcVar2 = s__s__d____s___write_black_box_par_ffff0000907d1be3;
  pcVar3 = s_VerityModeInit_ffff0000907ffaf0;
  uVar4 = 0xac9;
LAB_ffff0000907669b0:
  FUN_ffff00009077e11c(pcVar2,pcVar3,uVar4,s_BLACKBOX_ffff0000907d9a1f);
  return 0;
}


