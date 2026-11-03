// ffff000090761ee8  size=532  FUN_ffff000090761ee8

void FUN_ffff000090761ee8(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  undefined8 *puVar3;
  
  FUN_ffff00009077de54
            (s__s__d__black_box_recovery_mode_l_ffff0000907d35bd,
             s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3e1);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintRecoveryModelogHeader_ffff0000907f504b,
             0x3e2,*param_1);
  FUN_ffff00009077de54
            (s__s__d__next_index___d_ffff0000907e6860,s_PrintRecoveryModelogHeader_ffff0000907f504b,
             0x3e3,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__next_position___llu_ffff0000907dcf6e,
             s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3e4,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__log_count___d_ffff0000907d1978,s_PrintRecoveryModelogHeader_ffff0000907f504b,
             0x3e5,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,
             s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3e6,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077de54
            (s__s__d__one_log_size___llu_ffff0000907cffce,
             s_PrintRecoveryModelogHeader_ffff0000907f504b,999,*(undefined8 *)((long)param_1 + 0x1c)
            );
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = (long)param_1 + 0x13c;
    puVar3 = (undefined8 *)((long)param_1 + 0x114);
    do {
      FUN_ffff00009077de54
                (s__s__d__last_kmsg_size__u____llu_ffff0000907d4ec5,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3e9,uVar1 & 0xffffffff,
                 puVar3[-0x1e]);
      FUN_ffff00009077de54
                (s__s__d__last_pmsg_size__u____llu_ffff0000907dcfab,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3ea,uVar1 & 0xffffffff,
                 puVar3[-0x19]);
      FUN_ffff00009077de54
                (s__s__d__pl_lk_size__u____llu_ffff0000907d4ee6,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3eb,uVar1 & 0xffffffff,
                 puVar3[-0x14]);
      FUN_ffff00009077de54
                (s__s__d__last_kmsg_offset__u___0x__ffff0000907d69a5,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3ec,uVar1 & 0xffffffff,puVar3[-0xf]
                );
      FUN_ffff00009077de54
                (s__s__d__last_pmsg_offset__u___0x__ffff0000907ee8fe,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3ed,uVar1 & 0xffffffff,puVar3[-10])
      ;
      FUN_ffff00009077de54
                (s__s__d__pl_lk_offset__u___0x_llx_ffff0000907ff80b,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3ee,uVar1 & 0xffffffff,puVar3[-5]);
      FUN_ffff00009077de54
                (s__s__d__log_boot_index__u____llu_ffff0000907dcf8a,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3ef,uVar1 & 0xffffffff,*puVar3);
      FUN_ffff00009077de54
                (s__s__d__log_time__u____s_ffff0000907f0204,
                 s_PrintRecoveryModelogHeader_ffff0000907f504b,0x3f0,uVar1 & 0xffffffff,lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
      puVar3 = puVar3 + 1;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


