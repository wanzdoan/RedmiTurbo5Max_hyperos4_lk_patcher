// ffff000090761494  size=32  FUN_ffff000090761494

void FUN_ffff000090761494(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__print_black_box_partition_ffff0000907dffeb,
               s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x348);
    FUN_ffff00009077de54
              (s__s__d__magic__0x_llx_ffff0000907db46d,
               s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x349,*param_1);
    FUN_ffff00009077de54
              (s__s__d__isUartEnable___d_ffff0000907e1b6f,
               s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x34a,*(undefined4 *)(param_1 + 1)
              );
    FUN_ffff00009077de54
              (s__s__d__isOEMDebugUartLogLevel____ffff0000907e9bcb,
               s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x34b,
               *(undefined4 *)((long)param_1 + 0xc));
    FUN_ffff00009077de54
              (s__s__d__EIOEntryCount___d_ffff0000907e8139,
               s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x34c,*(undefined4 *)(param_1 + 2)
              );
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__partition_config_is_null_ffff0000907fa4d8,
             s_PrintPartitionSizeControlConfig_ffff0000907ff7d0,0x345);
  return;
}


