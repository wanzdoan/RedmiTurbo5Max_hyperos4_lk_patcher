
void FUN_ffff0000be04e474(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_normal_boot_log_heade_ffff0000be0a973b,
               s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x3f7);
    return;
  }
  if (DAT_ffff0000be1261e0 - 1 < 2) {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000be0a7491,
               s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x3fb);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__black_box_normal_boot_log_ffff0000be0b24a6,
             s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x3fe);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintNormalBootlogHeader_ffff0000be0a86ce,
             0x3ff,*param_1);
  FUN_ffff0000be05fd98
            (s__s__d__next_index___d_ffff0000be0b33fc,s_PrintNormalBootlogHeader_ffff0000be0a86ce,
             0x400,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__next_position___llu_ffff0000be0acdf7,
             s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x401,*(undefined8 *)((long)param_1 + 0xc))
  ;
  FUN_ffff0000be05fd98
            (s__s__d__log_count___d_ffff0000be0a4e7f,s_PrintNormalBootlogHeader_ffff0000be0a86ce,
             0x402,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,s_PrintNormalBootlogHeader_ffff0000be0a86ce
             ,0x403,*(undefined4 *)(param_1 + 3));
  FUN_ffff0000be05fd98
            (s__s__d__one_log_size___llu_ffff0000be0a3b07,
             s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x404,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x114;
    do {
      FUN_ffff0000be05fd98
                (s__s__d__log_size__u____llu_ffff0000be0b025b,
                 s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x406,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff0000be05fd98
                (s__s__d__log_offset__u___0x_llx_ffff0000be0a971c,
                 s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x407,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff0000be05fd98
                (s__s__d__log_boot_index__u____llu_ffff0000be0ace13,
                 s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x408,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0xc4));
      FUN_ffff0000be05fd98
                (s__s__d__log_name__u____s_ffff0000be0ba324,
                 s_PrintNormalBootlogHeader_ffff0000be0a86ce,0x409,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


