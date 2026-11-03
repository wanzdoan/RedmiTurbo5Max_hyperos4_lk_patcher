
void FUN_ffff0000be04e218(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  undefined8 *puVar3;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_recovery_mode_log_hea_ffff0000be0ace55,
               s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3da);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_recovery_m_ffff0000be0a6259,
               s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3de);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__black_box_recovery_mode_l_ffff0000be0a62b9,
             s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3e1);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,
             0x3e2,*param_1);
  FUN_ffff0000be05fd98
            (s__s__d__next_index___d_ffff0000be0b33fc,s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,
             0x3e3,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__next_position___llu_ffff0000be0acdf7,
             s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3e4,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff0000be05fd98
            (s__s__d__log_count___d_ffff0000be0a4e7f,s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,
             0x3e5,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,
             s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3e6,*(undefined4 *)(param_1 + 3));
  FUN_ffff0000be05fd98
            (s__s__d__one_log_size___llu_ffff0000be0a3b07,
             s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,999,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = (long)param_1 + 0x13c;
    puVar3 = (undefined8 *)((long)param_1 + 0x114);
    do {
      FUN_ffff0000be05fd98
                (s__s__d__last_kmsg_size__u____llu_ffff0000be0a7567,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3e9,uVar1 & 0xffffffff,
                 puVar3[-0x1e]);
      FUN_ffff0000be05fd98
                (s__s__d__last_pmsg_size__u____llu_ffff0000be0ace34,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3ea,uVar1 & 0xffffffff,
                 puVar3[-0x19]);
      FUN_ffff0000be05fd98
                (s__s__d__pl_lk_size__u____llu_ffff0000be0a7588,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3eb,uVar1 & 0xffffffff,
                 puVar3[-0x14]);
      FUN_ffff0000be05fd98
                (s__s__d__last_kmsg_offset__u___0x__ffff0000be0a86a9,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3ec,uVar1 & 0xffffffff,puVar3[-0xf]
                );
      FUN_ffff0000be05fd98
                (s__s__d__last_pmsg_offset__u___0x__ffff0000be0b90c3,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3ed,uVar1 & 0xffffffff,puVar3[-10])
      ;
      FUN_ffff0000be05fd98
                (s__s__d__pl_lk_offset__u___0x_llx_ffff0000be0c468c,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3ee,uVar1 & 0xffffffff,puVar3[-5]);
      FUN_ffff0000be05fd98
                (s__s__d__log_boot_index__u____llu_ffff0000be0ace13,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3ef,uVar1 & 0xffffffff,*puVar3);
      FUN_ffff0000be05fd98
                (s__s__d__log_time__u____s_ffff0000be0ba33d,
                 s_PrintRecoveryModelogHeader_ffff0000be0bd7f8,0x3f0,uVar1 & 0xffffffff,lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
      puVar3 = puVar3 + 1;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


