
uint FUN_ffff000090766abc(void)

{
  uint uVar1;
  ulong uVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 local_38;
  undefined8 local_30;
  undefined8 local_28;
  undefined8 local_8;
  
  local_30 = 0;
  local_28 = 0;
  local_38 = 0;
  local_8 = 0;
  uVar2 = FUN_ffff0000907661c0((long)&local_8 + 4,1);
  FUN_ffff00009077e11c
            (s__s__d____s___partition_control_o_ffff0000907eb6c6,
             s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf,0xa86,s_BLACKBOX_ffff0000907d9a1f,
             local_8._4_4_);
  if (((uVar2 & 1) != 0) && (local_8._4_4_ != 0)) {
    uVar2 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_38,local_8._4_4_,0x18);
    if ((uVar2 & 1) == 0) {
      pcVar3 = s__s__d____s___get_partition_contr_ffff0000907d9bd5;
      pcVar4 = s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf;
      uVar5 = 0xa8d;
    }
    else {
      uVar1 = (uint)local_28;
      if ((uint)local_28 <= local_28._4_4_) {
        FUN_ffff00009077e11c
                  (s__s__d____s___EIOEntryCount_is____ffff0000907f8c7e,
                   s_GetEIOCount_ffff0000907d1c18,0xada,s_BLACKBOX_ffff0000907d9a1f,
                   local_28 & 0xffffffff);
        return uVar1;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___EIOEntryCount_is_no_ffff0000907f1cb6,s_GetEIOCount_ffff0000907d1c18,
                 0xadc,s_BLACKBOX_ffff0000907d9a1f);
      local_28 = 0x200000000;
      local_38 = 0x7f9d3a6b2e1c8f60;
      local_30 = 0;
      FUN_ffff0000907661c0(&local_8,1);
      uVar2 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_38,local_8 & 0xffffffff,0x18);
      if ((uVar2 & 1) != 0) {
        return (uint)local_28;
      }
      pcVar3 = s__s__d____s___write_black_box_par_ffff0000907d1be3;
      pcVar4 = s_GetEIOCount_ffff0000907d1c18;
      uVar5 = 0xae1;
    }
    FUN_ffff00009077e11c(pcVar3,pcVar4,uVar5,s_BLACKBOX_ffff0000907d9a1f);
  }
  return 0;
}


