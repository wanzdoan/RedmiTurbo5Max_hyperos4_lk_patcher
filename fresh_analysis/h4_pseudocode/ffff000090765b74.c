
undefined4 FUN_ffff000090765b74(long param_1,ulong param_2,long param_3)

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
  
  FUN_ffff00009077e11c
            (s__s__d____s___start_to_Collect_ex_ffff0000907f1be7,s_WriteExpdbFile_ffff0000907e9da8,
             0x940,s_BLACKBOX_ffff0000907d9a1f);
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff00009077e11c
              (s__s__d____s___it_isn_t_a_useful_e_ffff0000907f1c12,s_WriteExpdbFile_ffff0000907e9da8
               ,0x943,s_BLACKBOX_ffff0000907d9a1f,param_2);
    return 0;
  }
  if (0xefffff < param_2) {
    FUN_ffff00009077e11c
              (s__s__d____s___the_wirting_buffer___ffff0000907d1b43,
               s_WriteExpdbFile_ffff0000907e9da8,0x947,s_BLACKBOX_ffff0000907d9a1f,param_2,0xf00000)
    ;
    return 0;
  }
  uVar4 = *(uint *)(param_3 + 0x10);
  lVar6 = param_3 + (ulong)uVar4 * 4;
  *(undefined4 *)(param_3 + 0x10) = 0;
  *(undefined4 *)(lVar6 + 0x14) = 1;
  *(int *)(lVar6 + 0x18) = (int)param_2;
  uVar5 = FUN_ffff000090760fd0
                    (s_blackbox_ffff0000907f0d8c,param_3,*(undefined4 *)(param_3 + 0x20),0x400);
  if ((uVar5 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___write_expdb_table_h_ffff000090801547,s_WriteExpdbFile_ffff0000907e9da8
               ,0x961,s_BLACKBOX_ffff0000907d9a1f);
    lVar6 = 0;
  }
  else {
    iVar2 = *(int *)(param_3 + 0x1c);
    iVar3 = *(int *)(param_3 + 0x20);
    lVar6 = FUN_ffff00009077d514(iVar2);
    if (lVar6 == 0) {
      pcVar7 = s__s__d____s___malloc_expdb_empty__ffff0000907eeaf7;
      uVar8 = 0x96a;
LAB_ffff000090765d6c:
      FUN_ffff00009077e11c
                (pcVar7,s_WriteExpdbFile_ffff0000907e9da8,uVar8,s_BLACKBOX_ffff0000907d9a1f);
      uVar9 = 1;
      goto LAB_ffff000090765ddc;
    }
    lVar1 = (ulong)(iVar3 + 0x400) + (ulong)(iVar2 * uVar4);
    uVar5 = FUN_ffff000090760f14
                      (s_blackbox_ffff0000907f0d8c,lVar6,lVar1,*(undefined4 *)(param_3 + 0x1c));
    if ((uVar5 & 1) == 0) {
      pcVar7 = s__s__d____s___get_expdb_empty_log_ffff0000907e01b4;
      uVar8 = 0x971;
    }
    else {
      FUN_ffff00009077ee50(lVar6,0,*(undefined4 *)(param_3 + 0x1c));
      uVar5 = FUN_ffff000090760fd0
                        (s_blackbox_ffff0000907f0d8c,lVar6,lVar1,*(undefined4 *)(param_3 + 0x1c));
      if ((uVar5 & 1) == 0) {
        pcVar7 = s__s__d____s___write_expdb_empty_l_ffff0000907de9af;
        uVar8 = 0x977;
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d____s__Write_expdb_file_sta_ffff0000907db5c7,
                   s_WriteExpdbFile_ffff0000907e9da8,0x97d,s_BLACKBOX_ffff0000907d9a1f,param_2,lVar1
                   ,uVar4);
        uVar5 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,param_1,lVar1,param_2);
        if ((uVar5 & 1) != 0) {
          pcVar7 = s__s__d____s__Collect_expdb_file_E_ffff0000907f70a1;
          uVar8 = 0x988;
          goto LAB_ffff000090765d6c;
        }
        pcVar7 = s__s__d____s___write_expdb_file_fa_ffff0000907f5281;
        uVar8 = 0x980;
      }
    }
    FUN_ffff00009077e11c(pcVar7,s_WriteExpdbFile_ffff0000907e9da8,uVar8,s_BLACKBOX_ffff0000907d9a1f)
    ;
  }
  uVar9 = 0;
LAB_ffff000090765ddc:
  thunk_FUN_ffff0000907c314c(lVar6);
  return uVar9;
}


