// ffff000090762180  size=532  FUN_ffff000090762180

void FUN_ffff000090762180(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  undefined8 *puVar3;
  
  FUN_ffff00009077e11c
            (s__s__d__black_box_recovery_mode_l_ffff0000907d35fa,
             s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3e1);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintRecoveryModelogHeader_ffff0000907f51cf,
             0x3e2,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__next_index___d_ffff0000907e698b,s_PrintRecoveryModelogHeader_ffff0000907f51cf,
             0x3e3,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__next_position___llu_ffff0000907dd05b,
             s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3e4,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__log_count___d_ffff0000907d19b5,s_PrintRecoveryModelogHeader_ffff0000907f51cf,
             0x3e5,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,
             s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3e6,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077e11c
            (s__s__d__one_log_size___llu_ffff0000907cffed,
             s_PrintRecoveryModelogHeader_ffff0000907f51cf,999,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = (long)param_1 + 0x13c;
    puVar3 = (undefined8 *)((long)param_1 + 0x114);
    do {
      FUN_ffff00009077e11c
                (s__s__d__last_kmsg_size__u____llu_ffff0000907d4f02,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3e9,uVar1 & 0xffffffff,
                 puVar3[-0x1e]);
      FUN_ffff00009077e11c
                (s__s__d__last_pmsg_size__u____llu_ffff0000907dd098,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3ea,uVar1 & 0xffffffff,
                 puVar3[-0x19]);
      FUN_ffff00009077e11c
                (s__s__d__pl_lk_size__u____llu_ffff0000907d4f23,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3eb,uVar1 & 0xffffffff,
                 puVar3[-0x14]);
      FUN_ffff00009077e11c
                (s__s__d__last_kmsg_offset__u___0x__ffff0000907d6a3b,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3ec,uVar1 & 0xffffffff,puVar3[-0xf]
                );
      FUN_ffff00009077e11c
                (s__s__d__last_pmsg_offset__u___0x__ffff0000907eea86,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3ed,uVar1 & 0xffffffff,puVar3[-10])
      ;
      FUN_ffff00009077e11c
                (s__s__d__pl_lk_offset__u___0x_llx_ffff0000907ff9b3,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3ee,uVar1 & 0xffffffff,puVar3[-5]);
      FUN_ffff00009077e11c
                (s__s__d__log_boot_index__u____llu_ffff0000907dd077,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3ef,uVar1 & 0xffffffff,*puVar3);
      FUN_ffff00009077e11c
                (s__s__d__log_time__u____s_ffff0000907f038c,
                 s_PrintRecoveryModelogHeader_ffff0000907f51cf,0x3f0,uVar1 & 0xffffffff,lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
      puVar3 = puVar3 + 1;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


