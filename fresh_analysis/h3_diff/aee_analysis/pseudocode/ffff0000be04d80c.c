
void FUN_ffff0000be04d80c(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__print_black_box_partition_ffff0000be0aecf8,
               s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x348);
    FUN_ffff0000be05fd98
              (s__s__d__magic__0x_llx_ffff0000be0abc7b,
               s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x349,*param_1);
    FUN_ffff0000be05fd98
              (s__s__d__isUartEnable___d_ffff0000be0b0214,
               s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x34a,*(undefined4 *)(param_1 + 1)
              );
    FUN_ffff0000be05fd98
              (s__s__d__isOEMDebugUartLogLevel____ffff0000be0b59fb,
               s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x34b,
               *(undefined4 *)((long)param_1 + 0xc));
    FUN_ffff0000be05fd98
              (s__s__d__EIOEntryCount___d_ffff0000be0b47d9,
               s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x34c,*(undefined4 *)(param_1 + 2)
              );
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__partition_config_is_null_ffff0000be0c1074,
             s_PrintPartitionSizeControlConfig_ffff0000be0c4651,0x345);
  return;
}


