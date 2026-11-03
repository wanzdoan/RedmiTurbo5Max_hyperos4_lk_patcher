// ffff000090761a5c  size=72  FUN_ffff000090761a5c

void FUN_ffff000090761a5c(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__log_exception_shutdown_lo_ffff0000907ee8e3,
               s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3a4);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_exception_ffff0000907e1be8,
               s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3a8);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__black_box_exception_shutd_ffff0000907d198e,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3ab);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db484,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3ac,*param_1);
  FUN_ffff00009077de54
            (s__s__d__next_index___d_ffff0000907e6877,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3ad,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__next_position___llu_ffff0000907dcf85,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3ae,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__log_count___d_ffff0000907d1978,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3af,
             *(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4ff9,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b0,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077de54
            (s__s__d__one_log_size__0x_llx_ffff0000907d35a0,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b1,
             *(undefined8 *)((long)param_1 + 0x1c));
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x9c;
    do {
      FUN_ffff00009077de54
                (s__s__d__log_size__u____llu_ffff0000907e1bcd,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b3,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff00009077de54
                (s__s__d__log_offset__u___0x_llx_ffff0000907d81f1,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b4,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x4c));
      FUN_ffff00009077de54
                (s__s__d__log_boot_index__u____llu_ffff0000907dcfa1,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b5,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff00009077de54
                (s__s__d__log_time__u____s_ffff0000907f021b,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5140,0x3b6,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


