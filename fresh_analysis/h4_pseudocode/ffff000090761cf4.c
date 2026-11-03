
void FUN_ffff000090761cf4(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__log_exception_shutdown_lo_ffff0000907eea54,
               s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3a4);
    return;
  }
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_exception_ffff0000907e1cb9,
               s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3a8);
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__black_box_exception_shutd_ffff0000907d19cb,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3ab);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3ac,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__next_index___d_ffff0000907e698b,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3ad,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__next_position___llu_ffff0000907dd05b,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3ae,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__log_count___d_ffff0000907d19b5,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3af,
             *(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b0,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077e11c
            (s__s__d__one_log_size__0x_llx_ffff0000907d35dd,
             s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b1,
             *(undefined8 *)((long)param_1 + 0x1c));
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x9c;
    do {
      FUN_ffff00009077e11c
                (s__s__d__log_size__u____llu_ffff0000907e1c9e,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b3,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff00009077e11c
                (s__s__d__log_offset__u___0x_llx_ffff0000907d8287,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b4,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x4c));
      FUN_ffff00009077e11c
                (s__s__d__log_boot_index__u____llu_ffff0000907dd077,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b5,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff00009077e11c
                (s__s__d__log_time__u____s_ffff0000907f038c,
                 s_PrintExceptionShutdownlogHeader_ffff0000907e5244,0x3b6,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


