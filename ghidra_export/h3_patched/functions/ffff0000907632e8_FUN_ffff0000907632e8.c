// ffff0000907632e8  size=1760  FUN_ffff0000907632e8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907632e8(int param_1,int param_2)

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
  undefined1 *puVar12;
  long lVar13;
  uint uVar14;
  ulong uVar15;
  long local_68;
  
  local_68 = 0;
  lVar5 = FUN_ffff00009077d24c(0x100000);
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d__alloc_log_compressed_buff_ffff0000907e1c68,
               s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x5e6);
    return;
  }
  if (param_2 == 2) {
    lVar13 = FUN_ffff000090719218();
    lVar6 = FUN_ffff00009071922c();
    if ((lVar13 == 0) || (lVar6 == 0)) {
      FUN_ffff00009077de54
                (s__s__d____s___pllk_log_header_is_n_ffff0000907fbf1e,
                 s_Read_Pl_Lk_Log_ffff0000907ecd0f,0x191,s_BLACKBOX_ffff0000907d9988,lVar13,lVar6);
      iVar4 = 0;
      lVar6 = 0;
    }
    else {
      iVar4 = *(int *)(lVar13 + 0x4c) + *(int *)(lVar13 + 0x48) + *(int *)(lVar13 + 0x58);
    }
    local_68 = CONCAT44(local_68._4_4_,iVar4);
    FUN_ffff00009077de54
              (s__s__d____s___the_size_of_pl_lk_l_ffff0000907f5066,
               s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x5f5,s_BLACKBOX_ffff0000907d9988
               ,local_68);
    if (lVar6 != 0) goto LAB_ffff000090763474;
LAB_ffff000090763570:
    pcVar8 = s__s__d____s___No_valid_data_was_f_ffff0000907d826f;
    uVar9 = 0x5fc;
LAB_ffff00009076358c:
    FUN_ffff00009077de54
              (pcVar8,s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,uVar9,
               s_BLACKBOX_ffff0000907d9988);
  }
  else {
    if (param_2 == 1) {
      lVar6 = FUN_ffff00009076083c(&local_68);
      FUN_ffff00009077de54
                (s__s__d____s___the_size_of_last_pm_ffff0000908012c9,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x5f1,
                 s_BLACKBOX_ffff0000907d9988,local_68);
    }
    else {
      if (param_2 != 0) {
        lVar6 = 0;
        goto LAB_ffff000090763570;
      }
      lVar6 = FUN_ffff0000907606e0(&local_68);
      FUN_ffff00009077de54
                (s__s__d____s___the_size_of_last_km_ffff0000907d823b,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x5ed,
                 s_BLACKBOX_ffff0000907d9988,local_68);
    }
    if (lVar6 == 0) goto LAB_ffff000090763570;
LAB_ffff000090763474:
    if (local_68 == 0) goto LAB_ffff000090763570;
    iVar4 = FUN_ffff000090760184(lVar6,lVar5,local_68,0x100000);
    lVar13 = (long)iVar4;
    FUN_ffff00009077de54
              (s__s__d____s___the_size_of_log_com_ffff0000907d35ea,
               s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x600,s_BLACKBOX_ffff0000907d9988
               ,lVar13);
    uVar14 = _DAT_ffff00009087c630;
    uVar3 = _DAT_ffff00009087c400;
    if (param_1 != 1) {
      if (param_1 != 2) {
        if (param_1 == 3) {
          FUN_ffff00009077de54
                    (s__s__d____s___cur_index_is___d_ffff0000908012fd,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x60b,
                     s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087c630);
          iVar4 = uVar14 * 0xbb800;
          uVar7 = (ulong)(iVar4 + 0xb00ac00);
          if (param_2 != 0) {
            uVar7 = _DAT_ffff00009087c634;
          }
          uVar15 = (ulong)(iVar4 + 0xb0c6400);
          FUN_ffff00009077de54
                    (s__s__d____s___offset_is__0x_llx_ffff0000907f509a,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x611,
                     s_BLACKBOX_ffff0000907d9988,uVar7);
          FUN_ffff00009077de54
                    (s__s__d____s___cur_boundary_is__0x_ffff0000907fbfb1,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x612,
                     s_BLACKBOX_ffff0000907d9988,uVar15);
          goto LAB_ffff000090763678;
        }
        uVar14 = 0;
        goto LAB_ffff000090763734;
      }
      uVar15 = (ulong)_DAT_ffff00009087c400;
      uVar7 = (ulong)(_DAT_ffff00009087c400 * 0xbb800 + 0xac61000);
      if (param_2 != 0) {
        uVar7 = _DAT_ffff00009087c404;
      }
      uVar2 = uVar7 + lVar13;
      if (_DAT_ffff00009087c400 * 0xbb800 + 0xad1c800 <= uVar2) {
        FUN_ffff00009077de54
                  (s__s__d____s___the_log_is_too_larg_ffff0000907d1a42,
                   s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x61d,
                   s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087c400,2,param_2);
        if (param_2 != 2) {
          if (param_2 == 1) {
            puVar12 = &DAT_ffff00009087c3f8 + uVar15 * 8;
LAB_ffff00009076387c:
            puVar11 = (undefined8 *)(puVar12 + 0xc4);
            *(undefined8 *)(puVar12 + 0x4c) = 0;
          }
          else {
            if (param_2 != 0) goto LAB_ffff000090763590;
            puVar12 = &DAT_ffff00009087c3f8 + uVar15 * 8;
LAB_ffff0000907637f8:
            puVar11 = (undefined8 *)(puVar12 + 0x9c);
            *(undefined8 *)(puVar12 + 0x24) = 0;
          }
          *puVar11 = 0;
          thunk_FUN_ffff0000907c2ad8(lVar5);
          goto LAB_ffff0000907635a4;
        }
        puVar12 = &DAT_ffff00009087c3f8 + uVar15 * 8;
LAB_ffff00009076385c:
        *(undefined8 *)(puVar12 + 0x74) = 0;
        *(undefined8 *)(puVar12 + 0xec) = 0;
        lVar6 = lVar5;
        goto LAB_ffff0000907635a4;
      }
      uVar7 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,lVar5,uVar7,lVar13);
      if ((uVar7 & 1) != 0) {
        if (param_2 == 2) {
          *(long *)(uVar15 * 8 + -0xffff6f783b94) = lVar13;
          *(ulong *)(uVar15 * 8 + -0xffff6f783b1c) = _DAT_ffff00009087c404;
          _DAT_ffff00009087c404 = uVar2;
        }
        else if (param_2 == 1) {
          *(long *)(uVar15 * 8 + -0xffff6f783bbc) = lVar13;
          *(ulong *)(uVar15 * 8 + -0xffff6f783b44) = _DAT_ffff00009087c404;
          _DAT_ffff00009087c404 = uVar2;
        }
        else {
          _DAT_ffff00009087c404 = uVar2;
          if (param_2 == 0) {
            *(long *)(uVar15 * 8 + -0xffff6f783be4) = lVar13;
            *(ulong *)(uVar15 * 8 + -0xffff6f783b6c) = (ulong)(uVar3 * 0xbb800 + 0xac61000);
          }
        }
        goto LAB_ffff000090763590;
      }
LAB_ffff00009076378c:
      pcVar8 = s__s__d____s___WritePartitionInfo_f_ffff0000907d4f5b;
      uVar9 = 0x646;
      goto LAB_ffff00009076358c;
    }
    uVar15 = (ulong)(_DAT_ffff00009087c270 * 0x10000 + 0xac20c00);
    uVar7 = _DAT_ffff00009087c274;
    uVar14 = _DAT_ffff00009087c270;
LAB_ffff000090763678:
    uVar2 = uVar7 + lVar13;
    if (uVar15 <= uVar2) {
LAB_ffff000090763734:
      FUN_ffff00009077de54
                (s__s__d____s___the_log_is_too_larg_ffff0000907d1a42,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x61d,
                 s_BLACKBOX_ffff0000907d9988,uVar14,param_1,param_2);
      if (param_1 == 3) {
        if (param_2 == 2) {
          puVar12 = &DAT_ffff00009087c628 + (ulong)uVar14 * 8;
          goto LAB_ffff00009076385c;
        }
        if (param_2 == 1) {
          puVar12 = &DAT_ffff00009087c628 + (ulong)uVar14 * 8;
          goto LAB_ffff00009076387c;
        }
        if (param_2 == 0) {
          puVar12 = &DAT_ffff00009087c628 + (ulong)uVar14 * 8;
          goto LAB_ffff0000907637f8;
        }
      }
      goto LAB_ffff000090763590;
    }
    uVar15 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,lVar5,uVar7,lVar13);
    uVar7 = _DAT_ffff00009087c634;
    if ((uVar15 & 1) == 0) goto LAB_ffff00009076378c;
    if (param_1 == 1) {
      *(long *)((ulong)uVar14 * 8 + -0xffff6f783d74) = lVar13;
      *(ulong *)((ulong)uVar14 * 8 + -0xffff6f783d4c) = _DAT_ffff00009087c274;
      _DAT_ffff00009087c274 = (ulong)(uVar14 * 0x10000 + 0xac20c00);
    }
    else if (param_1 == 3) {
      if (param_2 == 2) {
        lVar1 = (ulong)uVar14 * 8;
        *(long *)(lVar1 + -0xffff6f783964) = lVar13;
        *(ulong *)(lVar1 + -0xffff6f7838ec) = uVar7;
        FUN_ffff00009077de54
                  (s__s__d____s___pl_lk_size_is__0x_l_ffff0000907ecd85,
                   s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x66c,
                   s_BLACKBOX_ffff0000907d9988,lVar13);
        uVar10 = *(undefined8 *)(lVar1 + -0xffff6f7838ec);
        pcVar8 = s__s__d____s___pl_lk_offset_is__0x_ffff0000907f50bb;
        uVar9 = 0x66d;
LAB_ffff00009076398c:
        FUN_ffff00009077de54
                  (pcVar8,s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,uVar9,
                   s_BLACKBOX_ffff0000907d9988,uVar10);
      }
      else {
        if (param_2 == 1) {
          lVar1 = (ulong)uVar14 * 8;
          *(long *)(lVar1 + -0xffff6f78398c) = lVar13;
          *(ulong *)(lVar1 + -0xffff6f783914) = uVar7;
          FUN_ffff00009077de54
                    (s__s__d____s___last_pmsg_size_is__0_ffff0000907f6e20,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x666,
                     s_BLACKBOX_ffff0000907d9988,lVar13);
          uVar10 = *(undefined8 *)(lVar1 + -0xffff6f783914);
          pcVar8 = s__s__d____s___last_pmsg_offset_is_ffff0000907d9ae4;
          uVar9 = 0x667;
          goto LAB_ffff00009076398c;
        }
        if (param_2 == 0) {
          lVar1 = (ulong)uVar14 * 8;
          *(long *)(lVar1 + -0xffff6f7839b4) = lVar13;
          *(ulong *)(lVar1 + -0xffff6f78393c) = (ulong)(uVar14 * 0xbb800 + 0xb00ac00);
          FUN_ffff00009077de54
                    (s__s__d____s___last_kmsg_size_is__0_ffff0000907de86e,
                     s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x660,
                     s_BLACKBOX_ffff0000907d9988,lVar13);
          uVar10 = *(undefined8 *)(lVar1 + -0xffff6f78393c);
          pcVar8 = s__s__d____s___last_kmsg_offset_is_ffff0000907fbfd8;
          uVar9 = 0x661;
          goto LAB_ffff00009076398c;
        }
      }
      _DAT_ffff00009087c634 = uVar2;
      FUN_ffff00009077de54
                (s__s__d____s___next_position_is__0_ffff0000907f1a13,
                 s_save_log_to_blackbox_journal_zon_ffff0000907e68f4,0x673,
                 s_BLACKBOX_ffff0000907d9988,uVar2);
    }
  }
LAB_ffff000090763590:
  thunk_FUN_ffff0000907c2ad8(lVar5);
  if (param_2 == 2) {
    return;
  }
LAB_ffff0000907635a4:
  thunk_FUN_ffff0000907c2ad8(lVar6);
  return;
}


