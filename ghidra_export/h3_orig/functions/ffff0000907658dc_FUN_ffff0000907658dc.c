// ffff0000907658dc  size=656  FUN_ffff0000907658dc

undefined4 FUN_ffff0000907658dc(long param_1,ulong param_2,long param_3)

{
  long lVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  long lVar6;
  char *pcVar7;
  undefined8 uVar8;
  undefined4 uVar9;
  
  FUN_ffff00009077de54
            (s__s__d____s___start_to_Collect_ex_ffff0000907f1a76,s_WriteExpdbFile_ffff0000907e9c64,
             0x940,s_BLACKBOX_ffff0000907d9988);
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff00009077de54
              (s__s__d____s___it_isn_t_a_useful_e_ffff0000907f1aa1,s_WriteExpdbFile_ffff0000907e9c64
               ,0x943,s_BLACKBOX_ffff0000907d9988,param_2);
    return 0;
  }
  if (0xefffff < param_2) {
    FUN_ffff00009077de54
              (s__s__d____s___the_wirting_buffer___ffff0000907d1b06,
               s_WriteExpdbFile_ffff0000907e9c64,0x947,s_BLACKBOX_ffff0000907d9988,param_2,0xf00000)
    ;
    return 0;
  }
  uVar4 = *(uint *)(param_3 + 0x10);
  lVar6 = param_3 + (ulong)uVar4 * 4;
  *(undefined4 *)(param_3 + 0x10) = 0;
  *(undefined4 *)(lVar6 + 0x14) = 1;
  *(int *)(lVar6 + 0x18) = (int)param_2;
  uVar5 = FUN_ffff000090760d38
                    (s_blackbox_ffff0000907f0c1b,param_3,*(undefined4 *)(param_3 + 0x20),0x400);
  if ((uVar5 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___write_expdb_table_h_ffff00009080136b,s_WriteExpdbFile_ffff0000907e9c64
               ,0x961,s_BLACKBOX_ffff0000907d9988);
    lVar6 = 0;
  }
  else {
    iVar2 = *(int *)(param_3 + 0x1c);
    iVar3 = *(int *)(param_3 + 0x20);
    lVar6 = FUN_ffff00009077d24c(iVar2);
    if (lVar6 == 0) {
      pcVar7 = s__s__d____s___malloc_expdb_empty__ffff0000907ee986;
      uVar8 = 0x96a;
LAB_ffff000090765ad4:
      FUN_ffff00009077de54
                (pcVar7,s_WriteExpdbFile_ffff0000907e9c64,uVar8,s_BLACKBOX_ffff0000907d9988);
      uVar9 = 1;
      goto LAB_ffff000090765b44;
    }
    lVar1 = (ulong)(iVar3 + 0x400) + (ulong)(iVar2 * uVar4);
    uVar5 = FUN_ffff000090760c7c
                      (s_blackbox_ffff0000907f0c1b,lVar6,lVar1,*(undefined4 *)(param_3 + 0x1c));
    if ((uVar5 & 1) == 0) {
      pcVar7 = s__s__d____s___get_expdb_empty_log_ffff0000907e0111;
      uVar8 = 0x971;
    }
    else {
      FUN_ffff00009077eb88(lVar6,0,*(undefined4 *)(param_3 + 0x1c));
      uVar5 = FUN_ffff000090760d38
                        (s_blackbox_ffff0000907f0c1b,lVar6,lVar1,*(undefined4 *)(param_3 + 0x1c));
      if ((uVar5 & 1) == 0) {
        pcVar7 = s__s__d____s___write_expdb_empty_l_ffff0000907de90c;
        uVar8 = 0x977;
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d____s__Write_expdb_file_sta_ffff0000907db4bc,
                   s_WriteExpdbFile_ffff0000907e9c64,0x97d,s_BLACKBOX_ffff0000907d9988,param_2,lVar1
                   ,uVar4);
        uVar5 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,param_1,lVar1,param_2);
        if ((uVar5 & 1) != 0) {
          pcVar7 = s__s__d____s__Collect_expdb_file_E_ffff0000907f6f17;
          uVar8 = 0x988;
          goto LAB_ffff000090765ad4;
        }
        pcVar7 = s__s__d____s___write_expdb_file_fa_ffff0000907f5114;
        uVar8 = 0x980;
      }
    }
    FUN_ffff00009077de54(pcVar7,s_WriteExpdbFile_ffff0000907e9c64,uVar8,s_BLACKBOX_ffff0000907d9988)
    ;
  }
  uVar9 = 0;
LAB_ffff000090765b44:
  thunk_FUN_ffff0000907c2ad8(lVar6);
  return uVar9;
}


