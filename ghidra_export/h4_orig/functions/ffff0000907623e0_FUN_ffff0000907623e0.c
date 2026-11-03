// ffff0000907623e0  size=416  FUN_ffff0000907623e0

void FUN_ffff0000907623e0(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff00009077e11c
            (s__s__d__black_box_normal_boot_log_ffff0000907e5264,
             s_PrintNormalBootlogHeader_ffff0000907d6a60,0x3fe);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintNormalBootlogHeader_ffff0000907d6a60,
             0x3ff,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__next_index___d_ffff0000907e698b,s_PrintNormalBootlogHeader_ffff0000907d6a60,
             0x400,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__next_position___llu_ffff0000907dd05b,
             s_PrintNormalBootlogHeader_ffff0000907d6a60,0x401,*(undefined8 *)((long)param_1 + 0xc))
  ;
  FUN_ffff00009077e11c
            (s__s__d__log_count___d_ffff0000907d19b5,s_PrintNormalBootlogHeader_ffff0000907d6a60,
             0x402,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,s_PrintNormalBootlogHeader_ffff0000907d6a60
             ,0x403,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077e11c
            (s__s__d__one_log_size___llu_ffff0000907cffed,
             s_PrintNormalBootlogHeader_ffff0000907d6a60,0x404,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x114;
    do {
      FUN_ffff00009077e11c
                (s__s__d__log_size__u____llu_ffff0000907e1c9e,
                 s_PrintNormalBootlogHeader_ffff0000907d6a60,0x406,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff00009077e11c
                (s__s__d__log_offset__u___0x_llx_ffff0000907d8287,
                 s_PrintNormalBootlogHeader_ffff0000907d6a60,0x407,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff00009077e11c
                (s__s__d__log_boot_index__u____llu_ffff0000907dd077,
                 s_PrintNormalBootlogHeader_ffff0000907d6a60,0x408,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0xc4));
      FUN_ffff00009077e11c
                (s__s__d__log_name__u____s_ffff0000907f0373,
                 s_PrintNormalBootlogHeader_ffff0000907d6a60,0x409,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


