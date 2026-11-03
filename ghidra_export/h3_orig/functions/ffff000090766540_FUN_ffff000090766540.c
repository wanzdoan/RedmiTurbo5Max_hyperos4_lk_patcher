// ffff000090766540  size=260  FUN_ffff000090766540

undefined8 FUN_ffff000090766540(long param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  int local_24;
  
  local_24 = 0;
  if (param_1 == 0) {
    pcVar2 = s__s__d____s___pConfig_is_NULL__re_ffff0000907f513b;
    uVar3 = 0xa9b;
  }
  else {
    uVar1 = FUN_ffff000090765f28(&local_24,1);
    FUN_ffff00009077de54
              (s__s__d____s___partition_control_o_ffff0000907d82ce,
               s_SetBlackBoxPartitionControlConfi_ffff0000907f8ac0,0xa9f,s_BLACKBOX_ffff0000907d9988
               ,local_24);
    if ((uVar1 & 1) == 0) {
      return 0;
    }
    if (local_24 == 0) {
      return 0;
    }
    FUN_ffff00009077de54
              (s__s__d____s___pconfig_>eioentryco_ffff0000907f6f3c,
               s_SetBlackBoxPartitionControlConfi_ffff0000907f8ac0,0xaa3,s_BLACKBOX_ffff0000907d9988
               ,*(undefined4 *)(param_1 + 0x10));
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,param_1,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      return 1;
    }
    pcVar2 = s__s__d____s___write_partition_con_ffff0000907f02a3;
    uVar3 = 0xaa6;
  }
  FUN_ffff00009077de54
            (pcVar2,s_SetBlackBoxPartitionControlConfi_ffff0000907f8ac0,uVar3,
             s_BLACKBOX_ffff0000907d9988);
  return 0;
}


