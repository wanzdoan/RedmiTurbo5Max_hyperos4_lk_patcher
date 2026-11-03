
void FUN_ffff0000be04dbec(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_exception_reboot_log__ffff0000be0c57cf,
               s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x38b);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_exception_ffff0000be0bc52f,
               s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x38f);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__black_box_exception_reboo_ffff0000be0b47f3,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x392);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x393,*param_1);
  FUN_ffff0000be05fd98
            (s__s__d__next_index___d_ffff0000be0b33fc,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x394,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__next_position___llu_ffff0000be0acdf7,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x395,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff0000be05fd98
            (s__s__d__log_count___d_ffff0000be0a4e7f,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x396,
             *(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x397,*(undefined4 *)(param_1 + 3));
  FUN_ffff0000be05fd98
            (s__s__d__one_log_size__0x_llx_ffff0000be0a629c,
             s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x398,
             *(undefined8 *)((long)param_1 + 0x1c));
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x114;
    do {
      FUN_ffff0000be05fd98
                (s__s__d__log_size__u____llu_ffff0000be0b025b,
                 s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x39a,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff0000be05fd98
                (s__s__d__log_offset__u____llu_ffff0000be0a96ff,
                 s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x39b,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff0000be05fd98
                (s__s__d__log_boot_index__u____llu_ffff0000be0ace13,
                 s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x39c,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0xc4));
      FUN_ffff0000be05fd98
                (s__s__d__log_name__u____s_ffff0000be0ba324,
                 s_PrintExceptionRebootlogHeader_ffff0000be0c1095,0x39d,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


