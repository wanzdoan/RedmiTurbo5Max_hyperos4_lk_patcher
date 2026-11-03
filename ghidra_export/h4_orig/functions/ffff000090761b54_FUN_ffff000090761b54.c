// ffff000090761b54  size=416  FUN_ffff000090761b54

void FUN_ffff000090761b54(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff00009077e11c
            (s__s__d__black_box_exception_reboo_ffff0000907e82ae,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x392);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x393,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__next_index___d_ffff0000907e698b,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x394,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__next_position___llu_ffff0000907dd05b,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x395,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__log_count___d_ffff0000907d19b5,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x396,
             *(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x397,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077e11c
            (s__s__d__one_log_size__0x_llx_ffff0000907d35dd,
             s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x398,
             *(undefined8 *)((long)param_1 + 0x1c));
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x114;
    do {
      FUN_ffff00009077e11c
                (s__s__d__log_size__u____llu_ffff0000907e1c9e,
                 s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x39a,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff00009077e11c
                (s__s__d__log_offset__u____llu_ffff0000907d826a,
                 s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x39b,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff00009077e11c
                (s__s__d__log_boot_index__u____llu_ffff0000907dd077,
                 s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x39c,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0xc4));
      FUN_ffff00009077e11c
                (s__s__d__log_name__u____s_ffff0000907f0373,
                 s_PrintExceptionRebootlogHeader_ffff0000907fa695,0x39d,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


