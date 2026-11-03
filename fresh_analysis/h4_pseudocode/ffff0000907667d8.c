
undefined8 FUN_ffff0000907667d8(long param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  int local_24;
  
  local_24 = 0;
  if (param_1 == 0) {
    pcVar2 = s__s__d____s___pConfig_is_NULL__re_ffff0000907f52a8;
    uVar3 = 0xa9b;
  }
  else {
    uVar1 = FUN_ffff0000907661c0(&local_24,1);
    FUN_ffff00009077e11c
              (s__s__d____s___partition_control_o_ffff0000907d8364,
               s_SetBlackBoxPartitionControlConfi_ffff0000907f8c5c,0xa9f,s_BLACKBOX_ffff0000907d9a1f
               ,local_24);
    if ((uVar1 & 1) == 0) {
      return 0;
    }
    if (local_24 == 0) {
      return 0;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___pconfig_>eioentryco_ffff0000907f70c6,
               s_SetBlackBoxPartitionControlConfi_ffff0000907f8c5c,0xaa3,s_BLACKBOX_ffff0000907d9a1f
               ,*(undefined4 *)(param_1 + 0x10));
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,param_1,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      return 1;
    }
    pcVar2 = s__s__d____s___write_partition_con_ffff0000907f0414;
    uVar3 = 0xaa6;
  }
  FUN_ffff00009077e11c
            (pcVar2,s_SetBlackBoxPartitionControlConfi_ffff0000907f8c5c,uVar3,
             s_BLACKBOX_ffff0000907d9a1f);
  return 0;
}


