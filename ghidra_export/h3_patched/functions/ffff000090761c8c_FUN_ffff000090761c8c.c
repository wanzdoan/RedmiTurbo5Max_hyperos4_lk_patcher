// ffff000090761c8c  size=532  FUN_ffff000090761c8c

void FUN_ffff000090761c8c(undefined8 *param_1)

{
  ulong uVar1;
  long lVar2;
  undefined8 *puVar3;
  
  FUN_ffff00009077de54
            (s__s__d__black_box_fastboot_mode_l_ffff0000907f6d8d,
             s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3c4);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintFastbootModelogHeader_ffff0000907ff7f0,
             0x3c5,*param_1);
  FUN_ffff00009077de54
            (s__s__d__next_index___d_ffff0000907e6860,s_PrintFastbootModelogHeader_ffff0000907ff7f0,
             0x3c6,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__next_position___llu_ffff0000907dcf6e,
             s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3c7,
             *(undefined8 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__log_count___d_ffff0000907d1978,s_PrintFastbootModelogHeader_ffff0000907ff7f0,
             0x3c8,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,
             s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3c9,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077de54
            (s__s__d__one_log_size__0x_llx_ffff0000907d35a0,
             s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3ca,
             *(undefined8 *)((long)param_1 + 0x1c));
  if (*(int *)((long)param_1 + 0x14) != 0) {
    uVar1 = 0;
    lVar2 = (long)param_1 + 0x13c;
    puVar3 = (undefined8 *)((long)param_1 + 0x114);
    do {
      FUN_ffff00009077de54
                (s__s__d__last_kmsg_size__u____llu_ffff0000907d4ec5,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3cc,uVar1 & 0xffffffff,
                 puVar3[-0x1e]);
      FUN_ffff00009077de54
                (s__s__d__last_pmsg_size__u____llu_ffff0000907dcfab,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3cd,uVar1 & 0xffffffff,
                 puVar3[-0x19]);
      FUN_ffff00009077de54
                (s__s__d__pl_lk_size__u____llu_ffff0000907d4ee6,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3ce,uVar1 & 0xffffffff,
                 puVar3[-0x14]);
      FUN_ffff00009077de54
                (s__s__d__last_kmsg_offset__u___0x__ffff0000907d69a5,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3cf,uVar1 & 0xffffffff,puVar3[-0xf]
                );
      FUN_ffff00009077de54
                (s__s__d__last_pmsg_offset__u___0x__ffff0000907ee8fe,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3d0,uVar1 & 0xffffffff,puVar3[-10])
      ;
      FUN_ffff00009077de54
                (s__s__d__pl_lk_offset__u___0x_llx_ffff0000907ff80b,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3d1,uVar1 & 0xffffffff,puVar3[-5]);
      FUN_ffff00009077de54
                (s__s__d__log_boot_index__u____llu_ffff0000907dcf8a,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3d2,uVar1 & 0xffffffff,*puVar3);
      FUN_ffff00009077de54
                (s__s__d__log_time__u____s_ffff0000907f0204,
                 s_PrintFastbootModelogHeader_ffff0000907ff7f0,0x3d3,uVar1 & 0xffffffff,lVar2);
      uVar1 = uVar1 + 1;
      lVar2 = lVar2 + 0x30;
      puVar3 = puVar3 + 1;
    } while (uVar1 < *(uint *)((long)param_1 + 0x14));
  }
  return;
}


