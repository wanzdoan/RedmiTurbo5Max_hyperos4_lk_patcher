
void FUN_ffff000090763580(int param_1,int param_2)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  char *pcVar8;
  undefined8 uVar9;
  undefined8 uVar10;
  undefined8 *puVar11;
  undefined8 *puVar12;
  long lVar13;
  uint uVar14;
  ulong uVar15;
  long local_68;
  
  local_68 = 0;
  lVar5 = FUN_ffff00009077d514(0x100000);
  if (lVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__alloc_log_compressed_buff_ffff0000907e1d50,
               s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x5e6);
    return;
  }
  if (param_2 == 2) {
    lVar13 = FUN_ffff00009071935c();
    lVar6 = FUN_ffff000090719370();
    if ((lVar13 == 0) || (lVar6 == 0)) {
      FUN_ffff00009077e11c
                (s__s__d____s___pllk_log_header_is_n_ffff0000907fc0ba,
                 s_Read_Pl_Lk_Log_ffff0000907ecea9,0x191,s_BLACKBOX_ffff0000907d9a1f,lVar13,lVar6);
      iVar4 = 0;
      lVar6 = 0;
    }
    else {
      iVar4 = *(int *)(lVar13 + 0x4c) + *(int *)(lVar13 + 0x48) + *(int *)(lVar13 + 0x58);
    }
    local_68 = CONCAT44(local_68._4_4_,iVar4);
    FUN_ffff00009077e11c
              (s__s__d____s___the_size_of_pl_lk_l_ffff0000907f51ea,
               s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x5f5,s_BLACKBOX_ffff0000907d9a1f
               ,local_68);
    if (lVar6 != 0) goto LAB_ffff00009076370c;
LAB_ffff000090763808:
    pcVar8 = s__s__d____s___No_valid_data_was_f_ffff0000907d8305;
    uVar9 = 0x5fc;
LAB_ffff000090763824:
    FUN_ffff00009077e11c
              (pcVar8,s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,uVar9,
               s_BLACKBOX_ffff0000907d9a1f);
  }
  else {
    if (param_2 == 1) {
      lVar6 = FUN_ffff000090760ad4(&local_68);
      FUN_ffff00009077e11c
                (s__s__d____s___the_size_of_last_pm_ffff0000908014a5,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x5f1,
                 s_BLACKBOX_ffff0000907d9a1f,local_68);
    }
    else {
      if (param_2 != 0) {
        lVar6 = 0;
        goto LAB_ffff000090763808;
      }
      lVar6 = FUN_ffff000090760978(&local_68);
      FUN_ffff00009077e11c
                (s__s__d____s___the_size_of_last_km_ffff0000907d82d1,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x5ed,
                 s_BLACKBOX_ffff0000907d9a1f,local_68);
    }
    if (lVar6 == 0) goto LAB_ffff000090763808;
LAB_ffff00009076370c:
    if (local_68 == 0) goto LAB_ffff000090763808;
    iVar4 = FUN_ffff00009076041c(lVar6,lVar5,local_68,0x100000);
    lVar13 = (long)iVar4;
    FUN_ffff00009077e11c
              (s__s__d____s___the_size_of_log_com_ffff0000907d3627,
               s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x600,s_BLACKBOX_ffff0000907d9a1f
               ,lVar13);
    uVar14 = DAT_ffff00009087c6c8;
    uVar3 = DAT_ffff00009087c498;
    if (param_1 != 1) {
      if (param_1 != 2) {
        if (param_1 == 3) {
          FUN_ffff00009077e11c
                    (s__s__d____s___cur_index_is___d_ffff0000908014d9,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x60b,
                     s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087c6c8);
          iVar4 = uVar14 * 0xbb800;
          uVar7 = (ulong)(iVar4 + 0xb00ac00);
          if (param_2 != 0) {
            uVar7 = DAT_ffff00009087c6cc;
          }
          uVar15 = (ulong)(iVar4 + 0xb0c6400);
          FUN_ffff00009077e11c
                    (s__s__d____s___offset_is__0x_llx_ffff0000907f521e,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x611,
                     s_BLACKBOX_ffff0000907d9a1f,uVar7);
          FUN_ffff00009077e11c
                    (s__s__d____s___cur_boundary_is__0x_ffff0000907fc14d,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x612,
                     s_BLACKBOX_ffff0000907d9a1f,uVar15);
          goto LAB_ffff000090763910;
        }
        uVar14 = 0;
        goto LAB_ffff0000907639cc;
      }
      uVar15 = (ulong)DAT_ffff00009087c498;
      uVar7 = (ulong)(DAT_ffff00009087c498 * 0xbb800 + 0xac61000);
      if (param_2 != 0) {
        uVar7 = DAT_ffff00009087c49c;
      }
      uVar2 = uVar7 + lVar13;
      if (DAT_ffff00009087c498 * 0xbb800 + 0xad1c800 <= uVar2) {
        FUN_ffff00009077e11c
                  (s__s__d____s___the_log_is_too_larg_ffff0000907d1a7f,
                   s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x61d,
                   s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087c498,2,param_2);
        if (param_2 != 2) {
          if (param_2 == 1) {
            puVar12 = &DAT_ffff00009087c490 + uVar15;
LAB_ffff000090763b14:
            puVar11 = (undefined8 *)((long)puVar12 + 0xc4);
            *(undefined8 *)((long)puVar12 + 0x4c) = 0;
          }
          else {
            if (param_2 != 0) goto LAB_ffff000090763828;
            puVar12 = &DAT_ffff00009087c490 + uVar15;
LAB_ffff000090763a90:
            puVar11 = (undefined8 *)((long)puVar12 + 0x9c);
            *(undefined8 *)((long)puVar12 + 0x24) = 0;
          }
          *puVar11 = 0;
          thunk_FUN_ffff0000907c314c(lVar5);
          goto LAB_ffff00009076383c;
        }
        puVar12 = &DAT_ffff00009087c490 + uVar15;
LAB_ffff000090763af4:
        *(undefined8 *)((long)puVar12 + 0x74) = 0;
        *(undefined8 *)((long)puVar12 + 0xec) = 0;
        lVar6 = lVar5;
        goto LAB_ffff00009076383c;
      }
      uVar7 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,lVar5,uVar7,lVar13);
      if ((uVar7 & 1) != 0) {
        if (param_2 == 2) {
          *(long *)(&DAT_ffff00009087c504 + uVar15 * 8) = lVar13;
          *(ulong *)(&DAT_ffff00009087c57c + uVar15 * 8) = DAT_ffff00009087c49c;
          DAT_ffff00009087c49c = uVar2;
        }
        else if (param_2 == 1) {
          *(long *)(&DAT_ffff00009087c4dc + uVar15 * 8) = lVar13;
          *(ulong *)(&DAT_ffff00009087c554 + uVar15 * 8) = DAT_ffff00009087c49c;
          DAT_ffff00009087c49c = uVar2;
        }
        else {
          DAT_ffff00009087c49c = uVar2;
          if (param_2 == 0) {
            *(long *)(&DAT_ffff00009087c4b4 + uVar15 * 8) = lVar13;
            *(ulong *)(&DAT_ffff00009087c52c + uVar15 * 8) = (ulong)(uVar3 * 0xbb800 + 0xac61000);
          }
        }
        goto LAB_ffff000090763828;
      }
LAB_ffff000090763a24:
      pcVar8 = s__s__d____s___WritePartitionInfo_f_ffff0000907d4f98;
      uVar9 = 0x646;
      goto LAB_ffff000090763824;
    }
    uVar15 = (ulong)(DAT_ffff00009087c308 * 0x10000 + 0xac20c00);
    uVar7 = DAT_ffff00009087c30c;
    uVar14 = DAT_ffff00009087c308;
LAB_ffff000090763910:
    uVar2 = uVar7 + lVar13;
    if (uVar15 <= uVar2) {
LAB_ffff0000907639cc:
      FUN_ffff00009077e11c
                (s__s__d____s___the_log_is_too_larg_ffff0000907d1a7f,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x61d,
                 s_BLACKBOX_ffff0000907d9a1f,uVar14,param_1,param_2);
      if (param_1 == 3) {
        if (param_2 == 2) {
          puVar12 = &DAT_ffff00009087c6c0 + uVar14;
          goto LAB_ffff000090763af4;
        }
        if (param_2 == 1) {
          puVar12 = &DAT_ffff00009087c6c0 + uVar14;
          goto LAB_ffff000090763b14;
        }
        if (param_2 == 0) {
          puVar12 = &DAT_ffff00009087c6c0 + uVar14;
          goto LAB_ffff000090763a90;
        }
      }
      goto LAB_ffff000090763828;
    }
    uVar15 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,lVar5,uVar7,lVar13);
    uVar7 = DAT_ffff00009087c6cc;
    if ((uVar15 & 1) == 0) goto LAB_ffff000090763a24;
    if (param_1 == 1) {
      *(long *)(&DAT_ffff00009087c324 + (ulong)uVar14 * 8) = lVar13;
      *(ulong *)(&DAT_ffff00009087c34c + (ulong)uVar14 * 8) = DAT_ffff00009087c30c;
      DAT_ffff00009087c30c = (ulong)(uVar14 * 0x10000 + 0xac20c00);
    }
    else if (param_1 == 3) {
      if (param_2 == 2) {
        lVar1 = (ulong)uVar14 * 8;
        *(long *)(&DAT_ffff00009087c734 + lVar1) = lVar13;
        *(ulong *)(&DAT_ffff00009087c7ac + lVar1) = uVar7;
        FUN_ffff00009077e11c
                  (s__s__d____s___pl_lk_size_is__0x_l_ffff0000907ecf1f,
                   s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x66c,
                   s_BLACKBOX_ffff0000907d9a1f,lVar13);
        uVar10 = *(undefined8 *)(&DAT_ffff00009087c7ac + lVar1);
        pcVar8 = s__s__d____s___pl_lk_offset_is__0x_ffff0000907f523f;
        uVar9 = 0x66d;
LAB_ffff000090763c24:
        FUN_ffff00009077e11c
                  (pcVar8,s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,uVar9,
                   s_BLACKBOX_ffff0000907d9a1f,uVar10);
      }
      else {
        if (param_2 == 1) {
          lVar1 = (ulong)uVar14 * 8;
          *(long *)(&DAT_ffff00009087c70c + lVar1) = lVar13;
          *(ulong *)(&DAT_ffff00009087c784 + lVar1) = uVar7;
          FUN_ffff00009077e11c
                    (s__s__d____s___last_pmsg_size_is__0_ffff0000907f6fc1,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x666,
                     s_BLACKBOX_ffff0000907d9a1f,lVar13);
          uVar10 = *(undefined8 *)(&DAT_ffff00009087c784 + lVar1);
          pcVar8 = s__s__d____s___last_pmsg_offset_is_ffff0000907d9b7b;
          uVar9 = 0x667;
          goto LAB_ffff000090763c24;
        }
        if (param_2 == 0) {
          lVar1 = (ulong)uVar14 * 8;
          *(long *)(&DAT_ffff00009087c6e4 + lVar1) = lVar13;
          *(ulong *)(&DAT_ffff00009087c75c + lVar1) = (ulong)(uVar14 * 0xbb800 + 0xb00ac00);
          FUN_ffff00009077e11c
                    (s__s__d____s___last_kmsg_size_is__0_ffff0000907de928,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x660,
                     s_BLACKBOX_ffff0000907d9a1f,lVar13);
          uVar10 = *(undefined8 *)(&DAT_ffff00009087c75c + lVar1);
          pcVar8 = s__s__d____s___last_kmsg_offset_is_ffff0000907fc174;
          uVar9 = 0x661;
          goto LAB_ffff000090763c24;
        }
      }
      DAT_ffff00009087c6cc = uVar2;
      FUN_ffff00009077e11c
                (s__s__d____s___next_position_is__0_ffff0000907f1b9b,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e6a1f,0x673,
                 s_BLACKBOX_ffff0000907d9a1f,uVar2);
    }
  }
LAB_ffff000090763828:
  thunk_FUN_ffff0000907c314c(lVar5);
  if (param_2 == 2) {
    return;
  }
LAB_ffff00009076383c:
  thunk_FUN_ffff0000907c314c(lVar6);
  return;
}


