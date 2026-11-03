
void FUN_ffff0000be04d16c(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___log_exception_reboo_ffff0000be0c66f6,
               s_InitExceptionRebootlogHeader_ffff0000be0bea4e,0x254,s_BLACKBOX_ffff0000be0aa9ee);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_journal_z_ffff0000be0b6d05,
               s_InitExceptionRebootlogHeader_ffff0000be0bea4e,600);
    return;
  }
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0x3b9d5f1e7c8a2e4f;
  *(undefined8 *)((long)param_1 + 0xc) = 0xa440800;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0xc8000;
  FUN_ffff0000be060ae0((long)param_1 + 0x24,0,0x50);
  FUN_ffff0000be060ae0((long)param_1 + 0x74,0,0x50);
  FUN_ffff0000be060ae0((long)param_1 + 0xc4,0,0x50);
  FUN_ffff0000be060ae0((long)param_1 + 0x114,0,0x1e0);
  return;
}


