// ffff0000907620fc  size=76  FUN_ffff0000907620fc

void FUN_ffff0000907620fc(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__log_normal_boot_log_heade_ffff0000907d8210,
               s_PrintNormalBootlogHeader_ffff0000907d69ca,0x3f7);
    return;
  }
  if (DAT_ffff00009087bf14 - 1 < 2) {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000907d4def,
               s_PrintNormalBootlogHeader_ffff0000907d69ca,0x3fb);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__black_box_normal_boot_log_ffff0000907e5149,
             s_PrintNormalBootlogHeader_ffff0000907d69ca,0x3fe);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintNormalBootlogHeader_ffff0000907d69ca,
             0x3ff,*param_1);
  FUN_ffff00009077de54
            (s__s__d__next_index___d_ffff0000907e6860,s_PrintNormalBootlogHeader_ffff0000907d69ca,
             0x400,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__next_position___llu_ffff0000907dcf6e,
             s_PrintNormalBootlogHeader_ffff0000907d69ca,0x401,*(undefined8 *)((long)param_1 + 0xc))
  ;
  FUN_ffff00009077de54
            (s__s__d__log_count___d_ffff0000907d1978,s_PrintNormalBootlogHeader_ffff0000907d69ca,
             0x402,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,s_PrintNormalBootlogHeader_ffff0000907d69ca
             ,0x403,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077de54
            (s__s__d__one_log_size___llu_ffff0000907cffce,
             s_PrintNormalBootlogHeader_ffff0000907d69ca,0x404,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = 0x114;
    do {
      FUN_ffff00009077de54
                (s__s__d__log_size__u____llu_ffff0000907e1bb6,
                 s_PrintNormalBootlogHeader_ffff0000907d69ca,0x406,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x24));
      FUN_ffff00009077de54
                (s__s__d__log_offset__u___0x_llx_ffff0000907d81f1,
                 s_PrintNormalBootlogHeader_ffff0000907d69ca,0x407,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0x74));
      FUN_ffff00009077de54
                (s__s__d__log_boot_index__u____llu_ffff0000907dcf8a,
                 s_PrintNormalBootlogHeader_ffff0000907d69ca,0x408,uVar1 & 0xffffffff,
                 *(undefined8 *)((long)param_1 + uVar1 * 8 + 0xc4));
      FUN_ffff00009077de54
                (s__s__d__log_name__u____s_ffff0000907f01eb,
                 s_PrintNormalBootlogHeader_ffff0000907d69ca,0x409,uVar1 & 0xffffffff,
                 (long)param_1 + lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


