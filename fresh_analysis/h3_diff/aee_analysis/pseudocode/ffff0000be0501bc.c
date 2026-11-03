
undefined8 FUN_ffff0000be0501bc(long param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  int local_24;
  
  local_24 = 0;
  if (param_1 == 0) {
    pcVar2 = s__s__d____s___pConfig_is_NULL__re_ffff0000be0bd813;
    uVar3 = 0xa9b;
  }
  else {
    uVar1 = FUN_ffff0000be04fba4(&local_24,1);
    FUN_ffff0000be05fd98
              (s__s__d____s___partition_control_o_ffff0000be0a97b3,
               s_SetBlackBoxPartitionControlConfi_ffff0000be0bfedd,0xa9f,s_BLACKBOX_ffff0000be0aa9ee
               ,local_24);
    if ((uVar1 & 1) == 0) {
      return 0;
    }
    if (local_24 == 0) {
      return 0;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___pconfig_>eioentryco_ffff0000be0beb46,
               s_SetBlackBoxPartitionControlConfi_ffff0000be0bfedd,0xaa3,s_BLACKBOX_ffff0000be0aa9ee
               ,*(undefined4 *)(param_1 + 0x10));
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,param_1,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      return 1;
    }
    pcVar2 = s__s__d____s___write_partition_con_ffff0000be0ba387;
    uVar3 = 0xaa6;
  }
  FUN_ffff0000be05fd98
            (pcVar2,s_SetBlackBoxPartitionControlConfi_ffff0000be0bfedd,uVar3,
             s_BLACKBOX_ffff0000be0aa9ee);
  return 0;
}


