
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be00d19c(void)

{
  uint *puVar1;
  ulong *puVar2;
  char *pcVar3;
  undefined *puVar4;
  undefined *puVar5;
  bool bVar6;
  bool bVar7;
  bool bVar8;
  int iVar9;
  undefined4 uVar10;
  int iVar11;
  long lVar12;
  ulong uVar13;
  undefined8 *puVar14;
  undefined8 uVar15;
  long lVar16;
  ulong uVar17;
  undefined4 *puVar18;
  long lVar19;
  undefined8 uVar20;
  ulong uVar21;
  uint uVar22;
  long *plVar23;
  ulong uVar24;
  ulong uVar25;
  int *piVar26;
  uint uVar27;
  ulong uVar28;
  ulong uVar29;
  long lVar30;
  ulong uVar31;
  uint uVar32;
  char *pcVar33;
  int *piVar34;
  char *pcVar35;
  uint *puVar36;
  undefined4 *puVar37;
  undefined8 *puVar38;
  uint uVar39;
  ulong uVar40;
  int *piVar41;
  uint uVar42;
  uint uVar43;
  long lVar44;
  uint uVar45;
  uint *puVar46;
  ulong uVar47;
  ulong local_d0;
  int local_bc;
  ulong local_b8;
  undefined4 local_9c;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined4 local_68;
  
  lVar12 = FUN_ffff0000be00d18c();
  if (lVar12 != 0) {
    FUN_ffff0000be063b40(&DAT_ffff0000be0ba9be,lVar12);
    FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,lVar12);
  }
  lVar12 = FUN_ffff0000be03572c();
  if (lVar12 != 0) {
    FUN_ffff0000be063b40(&DAT_ffff0000be0ba9be,lVar12);
    FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,lVar12);
  }
  FUN_ffff0000be0357ec();
  FUN_ffff0000be063b40(s_kedump_mini_start_ffff0000be0a9ff7);
  FUN_ffff0000be05fd98(s_kedump_mini_start_ffff0000be0a9ff7);
  local_88 = (ulong)local_88._4_4_ << 0x20;
  local_90 = 0;
  FUN_ffff0000be029a20(&local_90);
  FUN_ffff0000be063b40
            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_88._2_2_,local_88 & 0xffff,
             local_90._6_2_,local_90._4_2_,local_90._2_2_,local_90 & 0xffff);
  FUN_ffff0000be05fd98
            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_88._2_2_,local_88 & 0xffff,
             local_90._6_2_,local_90._4_2_,local_90._2_2_,local_90 & 0xffff);
  iVar9 = FUN_ffff0000be0640b4();
  pcVar33 = s_kedump__ddr_reserve_mode_disable_ffff0000be0c28d6;
  if (iVar9 != 0) {
    pcVar33 = s_kedump__ddr_reserve_mode_enabled_ffff0000be0ac4ad;
  }
  FUN_ffff0000be063b40(pcVar33);
  FUN_ffff0000be05fd98(pcVar33);
  iVar9 = FUN_ffff0000be0640c8();
  if (iVar9 == 0) {
    FUN_ffff0000be063b40(s_kedump__ddr_reserve_mode_failed_ffff0000be0af426);
    FUN_ffff0000be05fd98(s_kedump__ddr_reserve_mode_failed_ffff0000be0af426);
  }
  uVar10 = FUN_ffff0000be01d174();
  FUN_ffff0000be063b40(s_kedump__boot_reason__d__ffff0000be0bdf7f,uVar10);
  uVar10 = FUN_ffff0000be01d174();
  FUN_ffff0000be05fd98(s_kedump__boot_reason__d__ffff0000be0bdf7f,uVar10);
  uVar13 = FUN_ffff0000be063030();
  if ((uVar13 & 1) != 0) {
    pcVar33 = s_kedump__skip_due_to_expdb_has_da_ffff0000be0c4cfd;
    goto LAB_ffff0000be00e1f8;
  }
  local_90 = 0;
  local_70 = local_70 & 0xffffffff00000000;
  FUN_ffff0000be063e40(&local_90,&local_70);
  iVar9 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_ke_mem_addr_ffff0000be0a3103,local_70 & 0xffffffff,
                     &DAT_ffff0000be0fd048,0xc,local_90,0,0);
  if (iVar9 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee24(s_mini_start_vmm_alloc_physical_fa_ffff0000be0ac4cf);
  }
  if ((((*DAT_ffff0000be0fd048 == -0x7f) && (DAT_ffff0000be0fd048[1] == 'E')) &&
      (DAT_ffff0000be0fd048[2] == 'L')) && (DAT_ffff0000be0fd048[3] == 'F')) {
    FUN_ffff0000be063b40(s_kedump__already_dumped_in_lk_ffff0000be0c184e);
    FUN_ffff0000be05fd98(s_kedump__already_dumped_in_lk_ffff0000be0c184e);
    FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,DAT_ffff0000be0fd048);
    return;
  }
  puVar14 = (undefined8 *)FUN_ffff0000be011bd8();
  DAT_ffff0000be0fb000 = puVar14;
  if (puVar14 == (undefined8 *)0x0) {
    FUN_ffff0000be063b40(s__s__mrdump_cb_is_NULL_ffff0000be0b089e,s_show_info_ffff0000be0b2b3b);
    FUN_ffff0000be05fd98(s__s__mrdump_cb_is_NULL_ffff0000be0b089e,s_show_info_ffff0000be0b2b3b);
  }
  else {
    iVar9 = FUN_ffff0000be060778(puVar14,s_MRDUMP11_ffff0000be0a7a51,8);
    if ((iVar9 == 0) ||
       (iVar9 = FUN_ffff0000be060778(puVar14,s_XRDUMP_ffff0000be0a6986,6), iVar9 == 0)) {
      FUN_ffff0000be063b40
                (s_vmalloc_start_0x_llx__vmalloc_en_ffff0000be0b735d,puVar14[0xd],puVar14[0xe],
                 puVar14[0x12],puVar14[3],puVar14[0xb],puVar14[0xc]);
      FUN_ffff0000be05fd98
                (s_vmalloc_start_0x_llx__vmalloc_en_ffff0000be0b735d,puVar14[0xd],puVar14[0xe],
                 puVar14[0x12],puVar14[3],puVar14[0xb],puVar14[0xc]);
    }
    else {
      FUN_ffff0000be063b40
                (s_mrdump_cb__sig_error_0x_llx_in___ffff0000be0b08b5,*puVar14,
                 s_show_info_ffff0000be0b2b3b);
      FUN_ffff0000be05fd98
                (s_mrdump_cb__sig_error_0x_llx_in___ffff0000be0b08b5,*puVar14,
                 s_show_info_ffff0000be0b2b3b);
    }
  }
  uVar13 = FUN_ffff0000be01cd48();
  DAT_ffff0000be0fd038 = uVar13;
  lVar12 = FUN_ffff0000be01cc78();
  DAT_ffff0000be0fd040 = lVar12 + uVar13;
  FUN_ffff0000be063b40(s_kedump_dram_range_0x_llx___0x_ll_ffff0000be0c4d21,DAT_ffff0000be0fd038);
  FUN_ffff0000be05fd98
            (s_kedump_dram_range_0x_llx___0x_ll_ffff0000be0c4d21,DAT_ffff0000be0fd038,
             DAT_ffff0000be0fd040);
  DAT_ffff0000be0fd050 = FUN_ffff0000be063f24();
  if (DAT_ffff0000be0fd050 != 0) {
    FUN_ffff0000be063b40(s_kedump__last_lk_dump_is_not_fini_ffff0000be0c28f9,DAT_ffff0000be0fd050);
    FUN_ffff0000be05fd98(s_kedump__last_lk_dump_is_not_fini_ffff0000be0c28f9,DAT_ffff0000be0fd050);
  }
  DAT_ffff0000be0fb008 = 0x12aee0dead;
  DAT_ffff0000be0fb010 = 0x2030;
  DAT_ffff0000be0fb01c = FUN_ffff0000be06333c();
  DAT_ffff0000be0fb020 = FUN_ffff0000be0633fc();
  DAT_ffff0000be0fb030 = FUN_ffff0000be065018();
  DAT_ffff0000be0fb034 = FUN_ffff0000be06502c();
  FUN_ffff0000be05fd98(s_panic_header_exp_type___0x_x_ffff0000be0bbb30,DAT_ffff0000be0fb030);
  FUN_ffff0000be05fd98(s_panic_header_wdt_status___0x_x_ffff0000be0af447,DAT_ffff0000be0fb034);
  _DAT_ffff0000be0fd058 = 0x30;
  uVar13 = FUN_ffff0000be062f38();
  if ((uVar13 & 1) == 0) {
    FUN_ffff0000be063628(0,&DAT_ffff0000be0fb008,_DAT_ffff0000be0fd058 & 0xffffffff);
  }
  else {
    FUN_ffff0000be0632a4();
  }
  local_98 = 0;
  local_9c = 0;
  FUN_ffff0000be063b40(s_kedump__block_size_0x_x_ffff0000be0a7a5a,DAT_ffff0000be0fb01c);
  FUN_ffff0000be05fd98(s_kedump__block_size_0x_x_ffff0000be0a7a5a,DAT_ffff0000be0fb01c);
  FUN_ffff0000be063600(DAT_ffff0000be0fb01c + 0x202f & -DAT_ffff0000be0fb01c);
  FUN_ffff0000be024a0c();
  local_90 = 0;
  local_70 = local_70 & 0xffffffff00000000;
  DAT_ffff0000be0fd060 = 1;
  FUN_ffff0000be063f04(1);
  FUN_ffff0000be063ca0(&local_90,&local_70);
  if ((local_90 != 0) && ((int)local_70 != 0)) {
    uVar15 = FUN_ffff0000be05d228();
    uVar10 = FUN_ffff0000be00f560(uVar15,0,local_70 & 0xffffffff);
    FUN_ffff0000be00f3d4(local_70 & 0xffffffff,uVar10,s_SYS_RAMCONSOLE_RAW_ffff0000be0b08d8);
  }
  FUN_ffff0000be063f04(2);
  local_90 = 0;
  local_70 = local_70 & 0xffffffff00000000;
  FUN_ffff0000be063d7c(&local_90,&local_70);
  if ((local_90 != 0) && ((int)local_70 != 0)) {
    uVar10 = FUN_ffff0000be00f560(local_90,0);
    FUN_ffff0000be00f3d4(local_70 & 0xffffffff,uVar10,s_SYS_PSTORE_RAW_ffff0000be0c186c);
  }
  local_90 = 0;
  local_70 = local_70 & 0xffffffff00000000;
  FUN_ffff0000be063e40(&local_90,&local_70);
  puVar4 = PTR_DAT_ffff0000be0f5578;
  puVar14 = (undefined8 *)PTR_PTR_ffff0000be0f5580;
  if ((local_90 != 0) && ((int)local_70 != 0)) {
    uVar10 = FUN_ffff0000be00f560(local_90,0);
    FUN_ffff0000be00f3d4(local_70 & 0xffffffff,uVar10,s_SYS_RS_MNRD_RAW_ffff0000be0b2b45);
    puVar4 = PTR_DAT_ffff0000be0f5578;
    puVar14 = (undefined8 *)PTR_PTR_ffff0000be0f5580;
  }
  for (; puVar5 = PTR_DAT_ffff0000be0f5578, puVar14 != (undefined8 *)PTR_DAT_ffff0000be0f5578;
      puVar14 = puVar14 + 3) {
    uVar15 = *puVar14;
    PTR_DAT_ffff0000be0f5578 = puVar4;
    iVar9 = FUN_ffff0000be060cb8(uVar15,s_DFD_dfd_ffff0000be0bdfab);
    if (iVar9 == 0) {
      plVar23 = (long *)&DAT_ffff0000be0fd068;
LAB_ffff0000be00d7c8:
      *plVar23 = (long)puVar14;
    }
    else {
      iVar9 = FUN_ffff0000be060cb8(uVar15,s_SYS_LAST_CPU_BUS_ffff0000be0c187b);
      if (iVar9 == 0) {
        plVar23 = (long *)&DAT_ffff0000be0fd070;
        goto LAB_ffff0000be00d7c8;
      }
      iVar9 = FUN_ffff0000be060cb8(uVar15,s_TFA_DEBUG_BUF_ffff0000be0c4d44);
      if (iVar9 == 0) {
        plVar23 = (long *)&DAT_ffff0000be0fd078;
        goto LAB_ffff0000be00d7c8;
      }
      iVar9 = FUN_ffff0000be060cb8(uVar15,s_SYS_GPU_DFD_ffff0000be0b6090);
      if (iVar9 == 0) {
        plVar23 = (long *)&DAT_ffff0000be0fd080;
        goto LAB_ffff0000be00d7c8;
      }
      iVar9 = FUN_ffff0000be060cb8(uVar15,s_SYS_GPU_DFD_SMMU_ffff0000be0bdfb3);
      if (iVar9 == 0) {
        plVar23 = (long *)&DAT_ffff0000be0fd088;
        goto LAB_ffff0000be00d7c8;
      }
      iVar9 = FUN_ffff0000be060cb8(uVar15,s_DFD_SOC_dfd_ffff0000be0af467);
      if (iVar9 == 0) {
        plVar23 = (long *)&DAT_ffff0000be0fd090;
        goto LAB_ffff0000be00d7c8;
      }
    }
    puVar4 = PTR_DAT_ffff0000be0f5578;
    PTR_DAT_ffff0000be0f5578 = puVar5;
  }
  lVar12 = 0;
  iVar9 = 100;
  DAT_ffff0000be0fd060 = 1;
  PTR_DAT_ffff0000be0f5578 = puVar4;
  do {
    puVar14 = *(undefined8 **)((long)&DAT_ffff0000be0fd068 + lVar12);
    if (puVar14 != (undefined8 *)0x0) {
      iVar9 = iVar9 + 1;
      DAT_ffff0000be0fd098 = puVar14[1];
      DAT_ffff0000be0fd0a0 = 0;
      DAT_ffff0000be0fd0a8 = 0;
      FUN_ffff0000be063f04(iVar9);
      FUN_ffff0000be063b40(s_kedump__start__s_ffff0000be0a310f,*puVar14);
      FUN_ffff0000be05fd98(s_kedump__start__s_ffff0000be0a310f,*puVar14);
      (*(code *)puVar14[2])(&LAB_ffff0000be00f778);
      if (0 < DAT_ffff0000be0fd0a8) {
        FUN_ffff0000be00f3d4(DAT_ffff0000be0fd0a0 & 0xffffffff,DAT_ffff0000be0fd0a8,*puVar14);
      }
    }
    lVar12 = lVar12 + 8;
  } while (lVar12 != 0x30);
  if (PTR_PTR_ffff0000be0f5580 != puVar5) {
    iVar9 = 200;
    puVar14 = (undefined8 *)PTR_PTR_ffff0000be0f5580;
    do {
      DAT_ffff0000be0fd060 = 0;
      if ((((puVar14 != DAT_ffff0000be0fd068) && (puVar14 != DAT_ffff0000be0fd070)) &&
          (puVar14 != DAT_ffff0000be0fd078)) &&
         (((puVar14 != DAT_ffff0000be0fd080 && (puVar14 != DAT_ffff0000be0fd088)) &&
          (puVar14 != DAT_ffff0000be0fd090)))) {
        uVar15 = *puVar14;
        lVar12 = FUN_ffff0000be0610a8(uVar15,&DAT_ffff0000be0c0377);
        if (lVar12 != 0) {
          DAT_ffff0000be0fd060 = 1;
        }
        iVar9 = iVar9 + 1;
        DAT_ffff0000be0fd098 = puVar14[1];
        DAT_ffff0000be0fd0a0 = 0;
        DAT_ffff0000be0fd0a8 = 0;
        FUN_ffff0000be063f04(iVar9);
        FUN_ffff0000be063b40(s_kedump__start__s_ffff0000be0a310f,uVar15);
        FUN_ffff0000be05fd98(s_kedump__start__s_ffff0000be0a310f,uVar15);
        (*(code *)puVar14[2])(&LAB_ffff0000be00f778);
        if (0 < DAT_ffff0000be0fd0a8) {
          FUN_ffff0000be00f3d4(DAT_ffff0000be0fd0a0 & 0xffffffff,DAT_ffff0000be0fd0a8,uVar15);
        }
      }
      puVar14 = puVar14 + 3;
    } while (puVar14 != (undefined8 *)puVar5);
  }
  DAT_ffff0000be0fd060 = 0;
  local_88 = local_88 & 0xffffffff00000000;
  local_90 = 0;
  FUN_ffff0000be029a20(&local_90);
  FUN_ffff0000be063b40
            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_88._2_2_,local_88 & 0xffff,
             local_90._6_2_,local_90._4_2_,local_90._2_2_,local_90 & 0xffff);
  FUN_ffff0000be05fd98
            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_88._2_2_,local_88 & 0xffff,
             local_90._6_2_,local_90._4_2_,local_90._2_2_,local_90 & 0xffff);
  FUN_ffff0000be024a0c();
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  local_88 = 0;
  if (DAT_ffff0000be0fd0b0 == (undefined8 *)0x0) {
    DAT_ffff0000be0fd0b8 = DAT_ffff0000be0fd048;
    FUN_ffff0000be063b40(s_kedump__KEHeader__p_ffff0000be0aa00a);
    FUN_ffff0000be05fd98(s_kedump__KEHeader__p_ffff0000be0aa00a,DAT_ffff0000be0fd0b8);
    pcVar33 = DAT_ffff0000be0fd0b8;
    if (DAT_ffff0000be0fd0b8 != (char *)0x0) {
      FUN_ffff0000be063b40
                (s_kedump__read_header_0x_p_0x_x_x__ffff0000be0af4be,DAT_ffff0000be0fd0b8,
                 *DAT_ffff0000be0fd0b8,DAT_ffff0000be0fd0b8[1],DAT_ffff0000be0fd0b8[2],
                 DAT_ffff0000be0fd0b8[3]);
      FUN_ffff0000be05fd98
                (s_kedump__read_header_0x_p_0x_x_x__ffff0000be0af4be,pcVar33,*pcVar33,pcVar33[1],
                 pcVar33[2],pcVar33[3]);
      if (((*pcVar33 == '\x7f') && (pcVar33[1] == 'E')) &&
         ((pcVar33[2] == 'L' && (pcVar33[3] == 'F')))) {
        DAT_ffff0000be0fd0c0 = (uint)*(ushort *)(DAT_ffff0000be0fd0b8 + 0x12);
        if (DAT_ffff0000be0fd0c0 == 0xb7) {
          DAT_ffff0000be0fd0b0 = &DAT_ffff0000be0fd0b8;
          DAT_ffff0000be0fd0c4 = (uint)*(undefined8 *)(DAT_ffff0000be0fd0b8 + 0x20);
          DAT_ffff0000be0fd0c8 = (uint)*(ushort *)(DAT_ffff0000be0fd0b8 + 0x38);
        }
        else {
          if (*(ushort *)(DAT_ffff0000be0fd0b8 + 0x12) != 0x28) goto LAB_ffff0000be00dbac;
          DAT_ffff0000be0fd0b0 = &DAT_ffff0000be0fd0b8;
          DAT_ffff0000be0fd0c4 = *(uint *)(DAT_ffff0000be0fd0b8 + 0x1c);
          DAT_ffff0000be0fd0c8 = (uint)*(ushort *)(DAT_ffff0000be0fd0b8 + 0x2c);
        }
      }
      else {
LAB_ffff0000be00dbac:
        if (DAT_ffff0000be0fd0b0 == (undefined8 *)0x0) {
          FUN_ffff0000be063b40
                    (s_kedump__invalid_header_0x_x_x_x__ffff0000be0b97a2,*pcVar33,pcVar33[1],
                     pcVar33[2],pcVar33[3]);
          FUN_ffff0000be05fd98
                    (s_kedump__invalid_header_0x_x_x_x__ffff0000be0b97a2,*pcVar33,pcVar33[1],
                     pcVar33[2],pcVar33[3]);
        }
      }
    }
    FUN_ffff0000be063b40
              (s_kedump__mach_0x_x___phnum_0x_x___ffff0000be0bf37d,DAT_ffff0000be0fd0c0,
               DAT_ffff0000be0fd0c8,DAT_ffff0000be0fd0c4);
    FUN_ffff0000be05fd98
              (s_kedump__mach_0x_x___phnum_0x_x___ffff0000be0bf37d,DAT_ffff0000be0fd0c0,
               DAT_ffff0000be0fd0c8,DAT_ffff0000be0fd0c4);
    if ((DAT_ffff0000be0fd0c8 == 0x3c) && (DAT_ffff0000be0fd0c4 < 0x41)) {
      if (DAT_ffff0000be0fd0b0 != (undefined8 *)0x0) goto LAB_ffff0000be00d910;
    }
    else {
      DAT_ffff0000be0fd0b0 = (undefined8 *)0x0;
    }
  }
  else {
LAB_ffff0000be00d910:
    FUN_ffff0000be063f04(4);
    uVar27 = DAT_ffff0000be0fd0c8;
    lVar12 = 0x20;
    if (DAT_ffff0000be0fd0c0 != 0x28) {
      lVar12 = 0x38;
    }
    pcVar33 = DAT_ffff0000be0fd0b8 + lVar12 + (ulong)DAT_ffff0000be0fd0c4;
    if (DAT_ffff0000be0fd0c0 != 0x28) {
      uVar13 = *(ulong *)(pcVar33 + 8);
      uVar28 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8);
      uVar31 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 0x20);
    }
    else {
      uVar13 = (ulong)*(uint *)(pcVar33 + 4);
      uVar28 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 4);
      uVar31 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 0x10);
    }
    if (uVar13 == uVar31 + uVar28) {
      if (DAT_ffff0000be0fb000 == (undefined8 *)0x0) {
        pcVar35 = s_mrdump_skip_mini_rdump___invalid_ffff0000be0ad2b7;
      }
      else {
        FUN_ffff0000be0634dc();
        lVar16 = FUN_ffff0000be063614();
        lVar12 = 0x20;
        if (DAT_ffff0000be0fd0c0 != 0x28) {
          lVar12 = 0x38;
        }
        lVar12 = lVar12 + (ulong)DAT_ffff0000be0fd0c4;
        if (DAT_ffff0000be0fd0c0 != 0x28) {
          uVar13 = *(ulong *)(DAT_ffff0000be0fd0b8 + lVar12 + 8);
          uVar28 = *(ulong *)(DAT_ffff0000be0fd0b8 + lVar12 + 0x20);
        }
        else {
          uVar13 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + lVar12 + 4);
          uVar28 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + lVar12 + 0x10);
        }
        uVar31 = FUN_ffff0000be062f38();
        pcVar35 = DAT_ffff0000be0fd0b8;
        if ((uVar31 & 1) == 0) {
          uVar31 = (ulong)((int)(uVar28 + uVar13) + 0xfff) & 0xfffff000;
        }
        else {
          uVar31 = (ulong)DAT_ffff0000be0fb01c;
          uVar29 = (ulong)((DAT_ffff0000be0fb01c + (int)lVar16) - 1 & -DAT_ffff0000be0fb01c) -
                   lVar16;
          if ((uVar28 + uVar13 & 0xffffffff) <= uVar29) {
            uVar31 = 0;
          }
          uVar31 = uVar31 + uVar29;
        }
        if (DAT_ffff0000be0fd0b0 == (undefined8 *)0x0) {
          pcVar35 = s_NULL_ehdr_ffff0000be0c293c;
        }
        else {
          lVar12 = 0x20;
          if (DAT_ffff0000be0fd0c0 != 0x28) {
            lVar12 = 0x38;
          }
          uVar42 = *(uint *)((long)DAT_ffff0000be0fb000 + 0x17c);
          uVar39 = *(uint *)(DAT_ffff0000be0fb000 + 1);
          lVar12 = lVar12 + (ulong)DAT_ffff0000be0fd0c4;
          if (DAT_ffff0000be0fd0c0 != 0x28) {
            uVar13 = *(ulong *)(DAT_ffff0000be0fd0b8 + lVar12 + 8);
            if (uVar13 == *(long *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 0x20) +
                          *(long *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8))
            goto LAB_ffff0000be00e244;
          }
          else {
            uVar13 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + lVar12 + 4);
            if ((ulong)*(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 0x10) +
                (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 4) == uVar13)
            {
LAB_ffff0000be00e244:
              puVar1 = (uint *)(DAT_ffff0000be0fd0b8 + uVar13);
              uVar28 = 0;
              uVar13 = (ulong)*puVar1 + (ulong)puVar1[1] + 0xc;
LAB_ffff0000be00e28c:
              uVar43 = DAT_ffff0000be0fd0c0;
              if (DAT_ffff0000be0fd0c0 == 0x28) {
                uVar29 = (ulong)*(uint *)(pcVar35 + lVar12 + 0x10);
              }
              else {
                uVar29 = *(ulong *)(pcVar35 + lVar12 + 0x20);
              }
              uVar40 = 0;
              if (uVar13 != 0) {
                uVar40 = uVar29 / uVar13;
              }
              if (uVar28 < uVar40) {
                puVar36 = (uint *)((long)puVar1 + uVar13 * uVar28);
                puVar46 = puVar36 + 3;
                if ((((char)*puVar46 == 'N') && (*(char *)((long)puVar36 + 0xd) == 'A')) &&
                   (*(char *)((long)puVar36 + 0xe) == '\0')) goto LAB_ffff0000be00e3cc;
                if (puVar36[2] == 0xfff) goto code_r0xffff0000be00e2e4;
                FUN_ffff0000be063b40(s_misc_invalid_type__0x_x_ffff0000be0b2b55);
                FUN_ffff0000be05fd98(s_misc_invalid_type__0x_x_ffff0000be0b2b55,puVar36[2]);
              }
              else {
LAB_ffff0000be00e3cc:
                if (0x10 < uVar39) {
                  FUN_ffff0000be063b40(s_kedump__invalid_control_block_nr_ffff0000be0aa01f,uVar39);
                  FUN_ffff0000be05fd98(s_kedump__invalid_control_block_nr_ffff0000be0aa01f,uVar39);
                  uVar39 = 0x10;
                }
                if (uVar39 <= uVar42) {
                  uVar42 = 0;
                }
                FUN_ffff0000be00fbd4(uVar42,0);
                if (uVar42 != 0) {
                  uVar43 = 0;
                  do {
                    uVar45 = uVar43 + 1;
                    FUN_ffff0000be00fbd4(uVar43,uVar45);
                    uVar43 = uVar45;
                  } while (uVar42 != uVar45);
                }
                uVar42 = uVar42 + 1;
                if (uVar42 < uVar39) {
                  do {
                    FUN_ffff0000be00fbd4(uVar42,uVar42);
                    uVar42 = uVar42 + 1;
                  } while (uVar39 != uVar42);
                }
                if (DAT_ffff0000be0fd0b0 == (undefined8 *)0x0) {
                  FUN_ffff0000be063b40(s_aee_invalid_ehdr_ffff0000be0a7a73);
                  FUN_ffff0000be05fd98(s_aee_invalid_ehdr_ffff0000be0a7a73);
                }
                else {
                  uVar42 = *(uint *)(DAT_ffff0000be0fb000 + 1);
                  if (0xf < uVar42) {
                    uVar42 = 0x10;
                  }
                  if (DAT_ffff0000be0fd0c0 == 0x28) {
                    if (uVar42 != 0) {
                      uVar39 = 0;
                      do {
                        uVar45 = DAT_ffff0000be0fd0c4;
                        uVar43 = DAT_ffff0000be0fd0c0;
                        pcVar35 = DAT_ffff0000be0fd0b8;
                        if (DAT_ffff0000be0fd0c0 == 0x28) {
                          uVar13 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 +
                                                   (ulong)DAT_ffff0000be0fd0c4 + 4);
                        }
                        else {
                          uVar13 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8
                                             );
                        }
                        piVar41 = (int *)(DAT_ffff0000be0fd0b8 + uVar13);
                        piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                        iVar9 = FUN_ffff0000be060f54
                                          ((long)piVar41 +
                                           (ulong)((*piVar41 + piVar41[1] + 0xc) * uVar39) + 0xc,
                                           &DAT_ffff0000be0b2b94,2);
                        if (iVar9 != 0) {
                          if (uVar43 == 0x28) {
                            uVar13 = (ulong)*(uint *)(pcVar35 + (ulong)uVar45 + 4);
                          }
                          else {
                            uVar13 = *(ulong *)(pcVar35 + (ulong)uVar45 + 8);
                          }
                          piVar41 = (int *)(pcVar35 + uVar13);
                          piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                          lVar12 = (ulong)(*piVar41 + 0xcU) +
                                   (ulong)((piVar41[1] + *piVar41 + 0xcU) * uVar39);
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x50));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x54));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x58));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x5c));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x60));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 100));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x68));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x6c));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x70));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x74));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x78));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x7c));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x80));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x84));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x88));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x8c));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x90));
                          FUN_ffff0000be010000(*(undefined4 *)((long)piVar41 + lVar12 + 0x94));
                          FUN_ffff0000be010000(*(undefined8 *)((long)piVar41 + lVar12 + 0x10));
                          if ((uVar39 == 0) && (DAT_ffff0000be0fb030 == '\x02')) {
                            uVar45 = *(uint *)((long)piVar41 + lVar12 + 0x84);
                            uVar13 = (ulong)uVar45;
                            uVar43 = uVar45 + 0x1fff & 0xffffe000;
                            if (uVar43 < uVar45) {
                              FUN_ffff0000be063b40(s_invalid_sp_0x_x_ffff0000be0ad2dd,uVar13);
                              FUN_ffff0000be05fd98(s_invalid_sp_0x_x_ffff0000be0ad2dd,uVar13);
                            }
                            else {
                              iVar9 = FUN_ffff0000be010524(uVar13);
                              if ((iVar9 != 0) &&
                                 (puVar18 = (undefined4 *)FUN_ffff0000be05f1bc(0x2000),
                                 puVar18 != (undefined4 *)0x0)) {
                                FUN_ffff0000be060ae0(puVar18,0,0x2000);
                                FUN_ffff0000be00cbbc(iVar9,uVar43 - uVar45,puVar18);
                                for (puVar37 = puVar18;
                                    puVar37 < (undefined4 *)((long)puVar18 + (uVar43 - uVar13));
                                    puVar37 = puVar37 + 1) {
                                  FUN_ffff0000be010000(*puVar37);
                                }
                                thunk_FUN_ffff0000be09937c(puVar18);
                              }
                            }
                          }
                        }
                        uVar39 = uVar39 + 1;
                      } while (uVar39 != uVar42);
                    }
                  }
                  else if (uVar42 != 0) {
                    uVar39 = 0;
                    do {
                      uVar45 = DAT_ffff0000be0fd0c4;
                      uVar43 = DAT_ffff0000be0fd0c0;
                      pcVar35 = DAT_ffff0000be0fd0b8;
                      if (DAT_ffff0000be0fd0c0 == 0x28) {
                        uVar13 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 +
                                                 (ulong)DAT_ffff0000be0fd0c4 + 4);
                      }
                      else {
                        uVar13 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8);
                      }
                      piVar41 = (int *)(DAT_ffff0000be0fd0b8 + uVar13);
                      piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                      iVar9 = FUN_ffff0000be060f54
                                        ((long)piVar41 +
                                         (ulong)((*piVar41 + piVar41[1] + 0xc) * uVar39) + 0xc,
                                         &DAT_ffff0000be0b2b94,2);
                      if (iVar9 != 0) {
                        if (uVar43 == 0x28) {
                          uVar13 = (ulong)*(uint *)(pcVar35 + (ulong)uVar45 + 4);
                        }
                        else {
                          uVar13 = *(ulong *)(pcVar35 + (ulong)uVar45 + 8);
                        }
                        piVar41 = (int *)(pcVar35 + uVar13);
                        lVar12 = 0;
                        uVar28 = (ulong)(*piVar41 + piVar41[1] + 0xc);
                        piVar41 = (int *)((long)piVar41 + uVar28);
                        uVar43 = *piVar41 + 0xc;
                        uVar29 = (ulong)((piVar41[1] + uVar43) * uVar39);
                        lVar30 = uVar29 + uVar43;
                        do {
                          FUN_ffff0000be010000
                                    (*(undefined8 *)
                                      (pcVar35 +
                                      lVar12 + uVar29 + uVar13 + uVar28 + (ulong)uVar43 + 0x70));
                          lVar12 = lVar12 + 8;
                        } while (lVar12 != 0x110);
                        FUN_ffff0000be010000(*(undefined8 *)((long)piVar41 + lVar30 + 0x10));
                        if ((uVar39 == 0) && (DAT_ffff0000be0fb030 == '\x02')) {
                          lVar30 = *(long *)((long)piVar41 + lVar30 + 0x168);
                          lVar12 = FUN_ffff0000be010288(lVar30);
                          if (lVar12 == 0) {
                            FUN_ffff0000be063b40(s_convert_sp_failed_0x_llx_ffff0000be0a8d4a,lVar30)
                            ;
                            FUN_ffff0000be05fd98(s_convert_sp_failed_0x_llx_ffff0000be0a8d4a,lVar30)
                            ;
                          }
                          else {
                            puVar14 = (undefined8 *)FUN_ffff0000be05f1bc(0x4000);
                            if (puVar14 != (undefined8 *)0x0) {
                              uVar13 = lVar30 + 0x3fffU & 0xffffffffffffc000;
                              FUN_ffff0000be060ae0(puVar14,0,0x4000);
                              FUN_ffff0000be00cbbc(lVar12,uVar13 - lVar30,puVar14);
                              for (puVar38 = puVar14;
                                  puVar38 < (undefined8 *)((long)puVar14 + (uVar13 - lVar30));
                                  puVar38 = puVar38 + 1) {
                                FUN_ffff0000be010000(*puVar38);
                              }
                              thunk_FUN_ffff0000be09937c(puVar14);
                            }
                          }
                        }
                      }
                      uVar39 = uVar39 + 1;
                    } while (uVar39 != uVar42);
                  }
                }
                uVar13 = uVar31 & 0xffffffff;
                lVar12 = FUN_ffff0000be05f1bc(uVar13);
                if (lVar12 == 0) {
                  FUN_ffff0000be063b40
                            (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,
                             s_kedump_mini_rdump_ffff0000be0c037b);
                  FUN_ffff0000be05fd98
                            (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,
                             s_kedump_mini_rdump_ffff0000be0c037b);
                }
                else {
                  FUN_ffff0000be060ae0(lVar12,0,uVar13);
                  uVar28 = (ulong)uVar27;
                  if (uVar27 < 0x3d) {
                    if (uVar27 != 0) goto LAB_ffff0000be00e928;
                    local_bc = 0;
                  }
                  else {
                    FUN_ffff0000be063b40(s_kedump__invalid_e_phnum__d_ffff0000be0ae44c,uVar28);
                    FUN_ffff0000be05fd98(s_kedump__invalid_e_phnum__d_ffff0000be0ae44c,uVar27);
                    uVar28 = 0x3c;
LAB_ffff0000be00e928:
                    lVar44 = 0;
                    lVar30 = 0;
                    uVar29 = 0;
                    local_bc = 0;
                    local_b8 = 0;
                    local_d0 = 0;
                    do {
                      if (DAT_ffff0000be0fd0c0 == 0x28) {
                        piVar41 = (int *)(DAT_ffff0000be0fd0b8 +
                                         lVar30 + (ulong)DAT_ffff0000be0fd0c4);
                        uVar40 = (ulong)(uint)piVar41[4];
                        iVar9 = *piVar41;
                        if (piVar41[4] != 0 || iVar9 != 0) {
                          uVar21 = (ulong)(uint)piVar41[1];
                          uVar17 = (ulong)(uint)piVar41[2];
LAB_ffff0000be00e9e0:
                          FUN_ffff0000be063b40
                                    (s_kedump___d_PT__d___llx__llx__>___ffff0000be0bf3ab,
                                     uVar29 & 0xffffffff,iVar9,uVar40,uVar17,uVar31,uVar21);
                          if (DAT_ffff0000be0fd0c0 == 0x28) {
                            uVar40 = (ulong)(uint)piVar41[1];
                            uVar21 = (ulong)(uint)piVar41[2];
                            uVar17 = (ulong)(uint)piVar41[4];
                          }
                          else {
                            uVar40 = *(ulong *)(piVar41 + 2);
                            uVar21 = *(ulong *)(piVar41 + 4);
                            uVar17 = *(ulong *)(piVar41 + 8);
                          }
                          FUN_ffff0000be05fd98
                                    (s_kedump___d_PT__d___llx__llx__>___ffff0000be0bf3ab,
                                     uVar29 & 0xffffffff,*piVar41,uVar17,uVar21,uVar31,uVar40);
                          if (*piVar41 == 1) {
                            if (DAT_ffff0000be0fd0c0 == 0x28) {
                              uVar40 = (ulong)(uint)piVar41[5];
                            }
                            else {
                              uVar40 = *(ulong *)(piVar41 + 10);
                            }
                            if (uVar40 == 0) {
                              lVar19 = 0x18;
                              if (DAT_ffff0000be0fd0c0 != 0x28) {
                                lVar19 = 4;
                              }
                              if (*(int *)((long)piVar41 + lVar19) != 0) goto LAB_ffff0000be00ee08;
                              if (DAT_ffff0000be0fd0c0 != 0x28) {
                                uVar40 = *(ulong *)(piVar41 + 0xc);
                                if (uVar40 == 0) {
                                  uVar40 = *(ulong *)(piVar41 + 4);
                                  uVar21 = *(ulong *)(piVar41 + 6);
                                  uVar17 = *(ulong *)(piVar41 + 8);
                                  goto LAB_ffff0000be00ea8c;
                                }
                                goto LAB_ffff0000be00ee64;
                              }
                              uVar27 = piVar41[7];
                              if (uVar27 == 0) {
                                uVar40 = (ulong)(uint)piVar41[2];
                                uVar21 = (ulong)(uint)piVar41[3];
                                uVar17 = (ulong)(uint)piVar41[4];
LAB_ffff0000be00ea8c:
                                if ((uVar17 == 0) || (uVar31 == 0)) {
                                  FUN_ffff0000be063b40
                                            (s_kedump__dump_addr_0x_llx__size_0_ffff0000be0b2b6e,
                                             uVar21,uVar17);
                                  FUN_ffff0000be05fd98
                                            (s_kedump__dump_addr_0x_llx__size_0_ffff0000be0b2b6e,
                                             uVar21,uVar17);
                                }
                                if (DAT_ffff0000be0fd0c0 == 0x28) {
                                  piVar41[1] = (int)uVar31;
                                }
                                else {
                                  *(ulong *)(piVar41 + 2) = uVar31;
                                }
                                puVar14 = DAT_ffff0000be0fb000;
                                if ((uVar17 != 0) && (uVar31 != 0)) {
                                  if (DAT_ffff0000be0fb000 == (undefined8 *)0x0) {
                                    bVar6 = uVar40 < local_d0;
                                    bVar8 = local_b8 < uVar17 + uVar40;
                                    if (((uVar40 >> 0x20 == 0) || (uVar40 < local_d0)) ||
                                       (uVar47 = local_d0, local_b8 < uVar17 + uVar40)) {
                                      if (uVar40 >> 0x20 != 0) goto LAB_ffff0000be00eccc;
LAB_ffff0000be00ed38:
                                      FUN_ffff0000be0634dc();
                                      FUN_ffff0000be063600(uVar31 + lVar16);
                                      if (uVar21 != 0) {
                                        iVar9 = FUN_ffff0000be0636d8(uVar21,uVar17);
                                        goto LAB_ffff0000be00edc4;
                                      }
                                      lVar19 = FUN_ffff0000be010524(uVar40);
                                      if (lVar19 == 0) {
                                        FUN_ffff0000be063b40
                                                  (
                                                  s_kedump__32va_to_pa_failed_0x_llx_ffff0000be0c03ea
                                                  ,uVar40,0);
                                        FUN_ffff0000be05fd98
                                                  (
                                                  s_kedump__32va_to_pa_failed_0x_llx_ffff0000be0c03ea
                                                  ,uVar40,0);
                                        iVar9 = 0;
                                      }
                                      else {
                                        iVar9 = FUN_ffff0000be0636d8(lVar19,uVar17);
                                      }
                                    }
                                    else {
LAB_ffff0000be00ebf4:
                                      iVar9 = FUN_ffff0000be00f9c0
                                                        (uVar40,uVar21,uVar17,uVar31 + lVar16);
                                      local_d0 = uVar47;
                                    }
                                    local_bc = local_bc + iVar9;
                                  }
                                  else {
                                    iVar9 = FUN_ffff0000be060778
                                                      (DAT_ffff0000be0fb000,
                                                       s_MRDUMP11_ffff0000be0a7a51,8);
                                    if ((iVar9 == 0) ||
                                       (iVar11 = FUN_ffff0000be060778
                                                           (puVar14,s_XRDUMP_ffff0000be0a6986,6),
                                       iVar11 == 0)) {
                                      local_d0 = puVar14[0xd];
                                      uVar24 = puVar14[0xe];
                                      bVar6 = uVar40 < local_d0;
                                      uVar25 = puVar14[2] - 1;
                                      bVar8 = uVar24 < uVar17 + uVar40;
                                      if ((0xfffffffe < uVar25) &&
                                         ((local_d0 <= uVar40 &&
                                          (uVar47 = local_d0, local_b8 = uVar24,
                                          uVar17 + uVar40 <= uVar24)))) goto LAB_ffff0000be00ebf4;
                                      bVar7 = uVar40 >> 0x20 == 0;
                                      if (iVar9 != 0) goto LAB_ffff0000be00ec84;
                                    }
                                    else {
                                      bVar6 = false;
                                      bVar7 = uVar40 >> 0x20 == 0;
                                      uVar24 = 0;
                                      bVar8 = uVar17 + uVar40 != 0;
                                      if (uVar40 >> 0x20 == 0) {
                                        local_d0 = 0;
                                      }
                                      else {
                                        local_d0 = 0;
                                        local_b8 = 0;
                                        uVar47 = uVar24;
                                        if (uVar17 + uVar40 == 0) goto LAB_ffff0000be00ebf4;
                                      }
LAB_ffff0000be00ec84:
                                      iVar9 = FUN_ffff0000be060778
                                                        (puVar14,s_XRDUMP_ffff0000be0a6986,6);
                                      if (iVar9 != 0) {
                                        local_b8 = uVar24;
                                        if (!bVar7) goto LAB_ffff0000be00eccc;
                                        goto LAB_ffff0000be00ed38;
                                      }
                                      uVar25 = puVar14[2] - 1;
                                    }
                                    local_b8 = uVar24;
                                    if (uVar25 < 0xffffffff) goto LAB_ffff0000be00ed38;
LAB_ffff0000be00eccc:
                                    if ((local_d0 < uVar17 + uVar40) && (uVar40 < local_b8)) {
                                      if ((local_d0 < uVar40) || (uVar17 + uVar40 < local_b8)) {
                                        if (!(bool)(bVar8 & bVar6)) {
                                          uVar10 = 4;
                                          if (!bVar8) {
                                            uVar10 = 5;
                                          }
                                          FUN_ffff0000be063b40
                                                    (
                                                  s_kedump__should_not_be_overlapp_c_ffff0000be0af4e4
                                                  ,uVar10);
                                          FUN_ffff0000be05fd98
                                                    (
                                                  s_kedump__should_not_be_overlapp_c_ffff0000be0af4e4
                                                  ,uVar10);
                                        }
                                      }
                                      else {
                                        FUN_ffff0000be063b40
                                                  (s_kedump__never_overlapp_case__6_ffff0000be0b97c6
                                                  );
                                        FUN_ffff0000be05fd98
                                                  (s_kedump__never_overlapp_case__6_ffff0000be0b97c6
                                                  );
                                      }
                                    }
                                    iVar9 = FUN_ffff0000be00f9c0
                                                      (uVar40,uVar21,uVar17,uVar31 + lVar16);
LAB_ffff0000be00edc4:
                                    local_bc = local_bc + iVar9;
                                  }
                                }
                                uVar31 = (uVar17 + 0xfff & 0xfffffffffffff000) + uVar31;
                                goto LAB_ffff0000be00e978;
                              }
LAB_ffff0000be00ee44:
                              uVar28 = (ulong)(uint)piVar41[5];
                              piVar26 = piVar41 + 6;
                              uVar40 = (ulong)uVar27;
                            }
                            else {
LAB_ffff0000be00ee08:
                              if (DAT_ffff0000be0fd0c0 == 0x28) {
                                uVar27 = piVar41[7];
                                goto LAB_ffff0000be00ee44;
                              }
                              uVar40 = *(ulong *)(piVar41 + 0xc);
LAB_ffff0000be00ee64:
                              piVar26 = piVar41 + 1;
                              uVar28 = *(ulong *)(piVar41 + 10);
                            }
                            piVar34 = piVar41 + 1;
                            FUN_ffff0000be063b40
                                      (s_memory_check_fail__memsz_0x_llx__ffff0000be0a8cfe,uVar28,
                                       *piVar26,uVar40);
                            if (DAT_ffff0000be0fd0c0 == 0x28) {
                              uVar28 = (ulong)(uint)piVar41[5];
                              uVar29 = (ulong)(uint)piVar41[7];
                              piVar34 = piVar41 + 6;
                            }
                            else {
                              uVar28 = *(ulong *)(piVar41 + 10);
                              uVar29 = *(ulong *)(piVar41 + 0xc);
                            }
                            FUN_ffff0000be05fd98
                                      (s_memory_check_fail__memsz_0x_llx__ffff0000be0a8cfe,uVar28,
                                       *piVar34,uVar29);
                            break;
                          }
                        }
                      }
                      else {
                        piVar41 = (int *)(DAT_ffff0000be0fd0b8 +
                                         lVar44 + (ulong)DAT_ffff0000be0fd0c4);
                        uVar40 = *(ulong *)(piVar41 + 8);
                        iVar9 = *piVar41;
                        if (uVar40 != 0 || iVar9 != 0) {
                          uVar21 = *(ulong *)(piVar41 + 2);
                          uVar17 = *(ulong *)(piVar41 + 4);
                          piVar41 = (int *)(DAT_ffff0000be0fd0b8 +
                                           uVar29 * 0x38 + (ulong)DAT_ffff0000be0fd0c4);
                          goto LAB_ffff0000be00e9e0;
                        }
                      }
LAB_ffff0000be00e978:
                      uVar29 = uVar29 + 1;
                      lVar30 = lVar30 + 0x20;
                      lVar44 = lVar44 + 0x38;
                    } while (uVar28 != uVar29);
                  }
                  FUN_ffff0000be063600(uVar31 + lVar16);
                  FUN_ffff0000be00f3d4
                            (uVar31 & 0xffffffff,uVar31 & 0xffffffff,
                             s_SYS_MINI_RDUMP_ffff0000be0b1a27);
                  FUN_ffff0000be0634dc();
                  uVar15 = FUN_ffff0000be063614();
                  FUN_ffff0000be063600(lVar16);
                  uVar27 = *(uint *)(DAT_ffff0000be0fb000 + 1);
                  if (0xf < uVar27) {
                    uVar27 = 0x10;
                  }
                  if (DAT_ffff0000be0fd0c0 == 0x28) {
                    uVar42 = *(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 4);
                  }
                  else {
                    uVar42 = *(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8);
                  }
                  uVar28 = (ulong)uVar42;
                  FUN_ffff0000be0607b0(lVar12,DAT_ffff0000be0fd0b8,uVar28);
                  piVar41 = (int *)(DAT_ffff0000be0fd0b8 + uVar28);
                  uVar39 = *piVar41 + piVar41[1] + 0xc;
                  FUN_ffff0000be0607b0(lVar12 + uVar28,piVar41,(ulong)uVar39);
                  if (DAT_ffff0000be0fb000[0xb] != 0) {
                    DAT_ffff0000be0fd0d0 = 0x1000000006;
                    DAT_ffff0000be0fd0d8 = 0x406;
                    FUN_ffff0000be060df0(&DAT_ffff0000be0fd0dc,s_LINUX_ffff0000be0b97e6,6);
                    DAT_ffff0000be0fd0e4 = DAT_ffff0000be0fb000[0xb];
                    DAT_ffff0000be0fd0f4 = 0x2000000005;
                    DAT_ffff0000be0fd0fc = 6;
                    DAT_ffff0000be0fd0ec = DAT_ffff0000be0fd0e4;
                    FUN_ffff0000be060df0(&DAT_ffff0000be0fd100,&DAT_ffff0000be0bcd54,5);
                    DAT_ffff0000be0fd108 = 0x10;
                    DAT_ffff0000be0fd110 = 0xffffffff;
                  }
                  lVar30 = lVar12 + uVar28 + (ulong)uVar39;
                  if (DAT_ffff0000be0fd0c0 == 0x28) {
                    uVar29 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 4
                                             );
                  }
                  else {
                    uVar29 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8);
                  }
                  if (uVar27 == 0) {
                    iVar9 = 0;
                  }
                  else {
                    piVar41 = (int *)(DAT_ffff0000be0fd0b8 + uVar29);
                    uVar45 = 0;
                    iVar9 = 0;
                    piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                    uVar43 = *piVar41 + piVar41[1] + 0xc;
                    do {
                      uVar22 = DAT_ffff0000be0fd0c4;
                      uVar32 = DAT_ffff0000be0fd0c0;
                      pcVar35 = DAT_ffff0000be0fd0b8;
                      if (DAT_ffff0000be0fd0c0 == 0x28) {
                        uVar29 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 +
                                                 (ulong)DAT_ffff0000be0fd0c4 + 4);
                      }
                      else {
                        uVar29 = *(ulong *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + 8);
                      }
                      piVar41 = (int *)(DAT_ffff0000be0fd0b8 + uVar29);
                      piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                      iVar11 = FUN_ffff0000be060f54
                                         ((long)piVar41 +
                                          (ulong)((*piVar41 + piVar41[1] + 0xc) * uVar45) + 0xc,
                                          &DAT_ffff0000be0b2b94,2);
                      if (iVar11 != 0) {
                        if (uVar32 == 0x28) {
                          uVar29 = (ulong)*(uint *)(pcVar35 + (ulong)uVar22 + 4);
                        }
                        else {
                          uVar29 = *(ulong *)(pcVar35 + (ulong)uVar22 + 8);
                        }
                        piVar41 = (int *)(pcVar35 + uVar29);
                        piVar41 = (int *)((long)piVar41 + (ulong)(*piVar41 + piVar41[1] + 0xc));
                        FUN_ffff0000be0607b0
                                  (lVar30,(long)piVar41 +
                                          (ulong)((*piVar41 + piVar41[1] + 0xc) * uVar45),
                                   (ulong)uVar43);
                        lVar30 = lVar30 + (ulong)uVar43;
                        iVar9 = iVar9 + uVar43;
                        if (DAT_ffff0000be0fb000[0xb] != 0) {
                          FUN_ffff0000be0607b0(lVar30,&DAT_ffff0000be0fd0d0,0x24);
                          lVar30 = lVar30 + 0x24;
                          iVar9 = iVar9 + 0x24;
                        }
                      }
                      uVar45 = uVar45 + 1;
                    } while (uVar27 != uVar45);
                  }
                  if (DAT_ffff0000be0fb000[0xb] != 0) {
                    FUN_ffff0000be0607b0(lVar30,&DAT_ffff0000be0fd0f4,0x34);
                    lVar30 = lVar30 + 0x34;
                    iVar9 = iVar9 + 0x34;
                  }
                  uVar29 = (ulong)DAT_ffff0000be0fd0c4;
                  puVar14 = (undefined8 *)(lVar12 + uVar29);
                  if (DAT_ffff0000be0fd0c0 == 0x28) {
                    if (puVar14 != (undefined8 *)0x0) {
                      puVar14[1] = 0;
                      *(undefined8 *)((long)puVar14 + 0x14) = 0;
                      *(undefined4 *)puVar14 = 4;
                      *(undefined4 *)((long)puVar14 + 0x1c) = 0;
                      *(uint *)(puVar14 + 2) = iVar9 + uVar39;
                      *(uint *)((long)puVar14 + 4) = uVar42;
                    }
                    uVar43 = 0xf00;
                    uVar27 = *(uint *)(DAT_ffff0000be0fd0b8 + uVar29 + 0x30);
                    uVar28 = (ulong)*(uint *)(DAT_ffff0000be0fd0b8 + uVar29 + 0x24);
                  }
                  else {
                    if (puVar14 != (undefined8 *)0x0) {
                      puVar14[2] = 0;
                      puVar14[3] = 0;
                      puVar14[6] = 0;
                      *puVar14 = 4;
                      puVar14[1] = uVar28;
                      puVar14[4] = (long)(int)(iVar9 + uVar39);
                      puVar14[5] = 0;
                    }
                    uVar43 = 0x1400;
                    uVar27 = *(uint *)(DAT_ffff0000be0fd0b8 + uVar29 + 0x58);
                    uVar28 = *(ulong *)(DAT_ffff0000be0fd0b8 + uVar29 + 0x40);
                  }
                  pcVar35 = DAT_ffff0000be0fd0b8 + uVar28;
                  if (uVar43 < uVar27) {
                    FUN_ffff0000be063b40
                              (s_kedump__WARN__invalid_dram_data___ffff0000be0a56c3,uVar27,uVar43);
                    FUN_ffff0000be05fd98
                              (s_kedump__WARN__invalid_dram_data___ffff0000be0a56c3,uVar27,uVar43);
                    uVar27 = uVar43;
                  }
                  FUN_ffff0000be0607b0(lVar30,pcVar35,uVar27);
                  lVar30 = lVar12 + (ulong)DAT_ffff0000be0fd0c4;
                  if (DAT_ffff0000be0fd0c0 == 0x28) {
                    if (lVar30 != -0x20) {
                      *(undefined8 *)(lVar30 + 0x28) = 0;
                      *(uint *)(lVar30 + 0x30) = uVar27;
                      *(undefined8 *)(lVar30 + 0x34) = 0;
                      *(undefined4 *)(lVar30 + 0x3c) = 0;
                      *(undefined4 *)(lVar30 + 0x20) = 4;
                      *(uint *)(lVar30 + 0x24) = iVar9 + uVar39 + uVar42;
                    }
                  }
                  else if (lVar30 != -0x38) {
                    *(undefined8 *)(lVar30 + 0x48) = 0;
                    *(undefined8 *)(lVar30 + 0x50) = 0;
                    *(undefined8 *)(lVar30 + 0x68) = 0;
                    *(undefined8 *)(lVar30 + 0x38) = 4;
                    *(ulong *)(lVar30 + 0x40) = (ulong)(iVar9 + uVar39 + uVar42);
                    *(long *)(lVar30 + 0x58) = (long)(int)uVar27;
                    *(undefined8 *)(lVar30 + 0x60) = 0;
                  }
                  uVar20 = FUN_ffff0000be05d228(lVar12);
                  iVar9 = FUN_ffff0000be0636d8(uVar20,uVar13);
                  FUN_ffff0000be0634dc();
                  FUN_ffff0000be063600(uVar15);
                  FUN_ffff0000be063b40
                            (s_kedump__mini_rdump_0x_x_0x_lx__0_ffff0000be0b3d61,local_bc + iVar9,
                             uVar31,lVar16);
                  FUN_ffff0000be05fd98
                            (s_kedump__mini_rdump_0x_x_0x_lx__0_ffff0000be0b3d61,local_bc + iVar9,
                             uVar31,lVar16);
                  local_68 = 0;
                  local_70 = 0;
                  FUN_ffff0000be029a20(&local_70);
                  FUN_ffff0000be063b40
                            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_68._2_2_,
                             (undefined2)local_68,local_70._6_2_,local_70._4_2_,local_70._2_2_,
                             local_70 & 0xffff);
                  FUN_ffff0000be05fd98
                            (s_kedump__current_time____d__d__d___ffff0000be0b19fc,local_68._2_2_,
                             (undefined2)local_68,local_70._6_2_,local_70._4_2_,local_70._2_2_,
                             local_70 & 0xffff);
                  thunk_FUN_ffff0000be09937c(lVar12);
                }
              }
              goto LAB_ffff0000be00dd1c;
            }
          }
          pcVar35 = s_first_2_ehdr_invalid_ffff0000be0b609c;
        }
      }
      FUN_ffff0000be063b40(pcVar35);
      FUN_ffff0000be05fd98(pcVar35);
LAB_ffff0000be00dd1c:
      if (DAT_ffff0000be0fd0c0 == 0x28) {
        uVar28 = (ulong)*(uint *)(pcVar33 + 4);
        uVar13 = (ulong)*(uint *)(pcVar33 + 0x10);
      }
      else {
        uVar28 = *(ulong *)(pcVar33 + 8);
        uVar13 = *(ulong *)(pcVar33 + 0x20);
      }
      puVar1 = (uint *)(DAT_ffff0000be0fd0b8 + uVar28);
      FUN_ffff0000be063b40(s_kedump__misc__p___llx__llx_ffff0000be0c4d52,pcVar33,uVar13);
      if (DAT_ffff0000be0fd0c0 == 0x28) {
        uVar13 = (ulong)*(uint *)(pcVar33 + 0x10);
        uVar28 = (ulong)*(uint *)(pcVar33 + 4);
      }
      else {
        uVar13 = *(ulong *)(pcVar33 + 0x20);
        uVar28 = *(ulong *)(pcVar33 + 8);
      }
      FUN_ffff0000be05fd98(s_kedump__misc__p___llx__llx_ffff0000be0c4d52,pcVar33,uVar13,uVar28);
      uVar13 = (ulong)*puVar1 + (ulong)puVar1[1] + 0xc;
      FUN_ffff0000be063b40(s_kedump__miscs__p___size__lx_ffff0000be0ac51c,puVar1,uVar13);
      FUN_ffff0000be05fd98(s_kedump__miscs__p___size__lx_ffff0000be0ac51c,puVar1,uVar13);
      if (DAT_ffff0000be0fd0c0 == 0x28) {
        uVar28 = (ulong)*(uint *)(pcVar33 + 0x10);
      }
      else {
        uVar28 = *(ulong *)(pcVar33 + 0x20);
      }
      uVar31 = 0;
      if (uVar13 != 0) {
        uVar31 = uVar28 / uVar13;
      }
      if (uVar31 < 0x51) {
        FUN_ffff0000be024a0c();
        if (uVar28 < uVar13) goto LAB_ffff0000be00e16c;
      }
      else {
        FUN_ffff0000be063b40(s_kedump__invalid_misc_size_ffff0000be0a56a8);
        FUN_ffff0000be05fd98(s_kedump__invalid_misc_size_ffff0000be0a56a8);
        FUN_ffff0000be024a0c();
        uVar31 = 0x50;
      }
      uVar28 = 0;
      do {
        puVar36 = (uint *)((long)puVar1 + uVar28 * uVar13);
        puVar46 = puVar36 + 3;
        if ((((char)*puVar46 == 'N') && (*(char *)((long)puVar36 + 0xd) == 'A')) &&
           (*(char *)((long)puVar36 + 0xe) == '\0')) break;
        if (puVar36[2] != 0xfff) {
          FUN_ffff0000be063b40(s_misc_invalid_type__0x_x_ffff0000be0b2b55);
          FUN_ffff0000be05fd98(s_misc_invalid_type__0x_x_ffff0000be0b2b55,puVar36[2]);
          break;
        }
        iVar9 = FUN_ffff0000be060cb8(puVar46,&DAT_ffff0000be0ae447);
        if (iVar9 != 0) {
          puVar2 = (ulong *)((long)puVar46 + (ulong)*puVar36);
          if (puVar36[1] == 0x10) {
            iVar9 = *(int *)((long)puVar2 + 4);
            local_70 = (ulong)local_70._4_4_ << 0x20;
            if (iVar9 == 0) {
              uVar29 = *puVar2;
              FUN_ffff0000be063b40(s_kedump__not_support_32bit_kernel_ffff0000be0bdfcb,(int)uVar29);
              FUN_ffff0000be05fd98(s_kedump__not_support_32bit_kernel_ffff0000be0bdfcb,(int)uVar29);
              uVar10 = *(undefined4 *)((long)puVar2 + 0xc);
              iVar9 = 0;
            }
            else {
              uVar10 = *(undefined4 *)((long)puVar2 + 0xc);
              if ((int)puVar2[1] == 0) {
                uVar29 = 0;
              }
              else {
                FUN_ffff0000be00cbbc((int)puVar2[1],4,&local_70);
                uVar29 = local_70 & 0xffffffff;
              }
              iVar9 = FUN_ffff0000be00f560(iVar9,uVar29,uVar10);
              uVar10 = *(undefined4 *)((long)puVar2 + 0xc);
            }
          }
          else {
            uVar40 = puVar2[1];
            local_70 = (ulong)local_70._4_4_ << 0x20;
            uVar29 = puVar2[3];
            if (uVar40 == 0) {
              uVar40 = *puVar2 | 0xff00000000000000;
              uVar21 = FUN_ffff0000be0101cc(uVar40);
              if ((uVar21 & 1) == 0) {
                FUN_ffff0000be063b40(s_kedump__invalid_vaddr_0x_llx_ffff0000be0ac539,uVar40);
                FUN_ffff0000be05fd98(s_kedump__invalid_vaddr_0x_llx_ffff0000be0ac539,uVar40);
              }
              else {
                uVar29 = uVar29 & 0xffffffff;
                uVar21 = uVar40 + uVar29;
                if (uVar40 < uVar21) {
                  iVar9 = 0;
                  do {
                    uVar17 = FUN_ffff0000be010288(uVar40);
                    if (uVar17 == 0) {
LAB_ffff0000be00e07c:
                      FUN_ffff0000be063b40
                                (s_kedump__v2P_fail_0x_llx_0x_llx_ffff0000be0c0416,uVar40,uVar17);
                      FUN_ffff0000be05fd98
                                (s_kedump__v2P_fail_0x_llx_0x_llx_ffff0000be0c0416,uVar40,uVar17);
                      break;
                    }
                    if ((DAT_ffff0000be0fd038 == 0) || (DAT_ffff0000be0fd040 == 0)) {
                      FUN_ffff0000be063b40(s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5);
                      FUN_ffff0000be05fd98
                                (s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5,
                                 DAT_ffff0000be0fd038,DAT_ffff0000be0fd040);
                      goto LAB_ffff0000be00e07c;
                    }
                    if ((uVar17 < DAT_ffff0000be0fd038) || (DAT_ffff0000be0fd040 <= uVar17))
                    goto LAB_ffff0000be00e07c;
                    if ((uVar40 & 0xfff) == 0) {
                      uVar24 = uVar40 + 0x1000;
                      uVar25 = uVar21 - uVar40;
                      if (uVar24 <= uVar21) {
                        uVar25 = 0x1000;
                      }
                    }
                    else {
                      uVar24 = uVar40 + 0xfff & 0xfffffffffffff000;
                      uVar25 = uVar24 - uVar40;
                      if (uVar29 <= uVar24 - uVar40) {
                        uVar25 = uVar29;
                      }
                    }
                    lVar12 = FUN_ffff0000be0636d8(uVar17,uVar25);
                    if (lVar12 < 1) {
                      FUN_ffff0000be063b40
                                (s_kedump__expdb_write_fail_0x_lx_0_ffff0000be0b73e9,lVar12,uVar25,
                                 uVar24);
                      FUN_ffff0000be05fd98
                                (s_kedump__expdb_write_fail_0x_lx_0_ffff0000be0b73e9,lVar12,uVar25,
                                 uVar24);
                      break;
                    }
                    iVar9 = iVar9 + (int)lVar12;
                    uVar40 = uVar24;
                  } while (uVar24 < uVar21);
                  goto LAB_ffff0000be00e0e4;
                }
              }
              iVar9 = 0;
            }
            else {
              if (puVar2[2] == 0) {
                uVar21 = 0;
              }
              else {
                FUN_ffff0000be00cbbc(puVar2[2],4,&local_70);
                uVar21 = local_70 & 0xffffffff;
              }
              iVar9 = FUN_ffff0000be00f560(uVar40,uVar21,uVar29 & 0xffffffff);
            }
LAB_ffff0000be00e0e4:
            uVar10 = (undefined4)puVar2[3];
          }
          if ((char)*puVar46 == '_') {
            FUN_ffff0000be060df0(&local_90,&DAT_ffff0000be0af4ba,0x20);
          }
          else {
            local_90 = local_90 & 0xffffffffffffff00;
          }
          FUN_ffff0000be060d58(&local_90,puVar46,0x20);
          lVar12 = FUN_ffff0000be060e5c(puVar46);
          if (*(char *)((long)puVar46 + lVar12 + -1) == '_') {
            FUN_ffff0000be060d58(&local_90,&DAT_ffff0000be0c4d6e,0x20);
          }
          FUN_ffff0000be00f3d4(uVar10,iVar9,&local_90);
        }
        uVar28 = uVar28 + 1;
      } while (uVar28 != uVar31);
    }
    else {
      FUN_ffff0000be063b40(s_first_2_ehdr_invalid_ffff0000be0b609c);
      FUN_ffff0000be05fd98(s_first_2_ehdr_invalid_ffff0000be0b609c);
    }
  }
LAB_ffff0000be00e16c:
  FUN_ffff0000be063bec(&local_98,&local_9c);
  uVar15 = FUN_ffff0000be05d228(local_98);
  uVar15 = FUN_ffff0000be0636d8(uVar15,local_9c);
  FUN_ffff0000be00f3d4(local_9c,uVar15,s_ZAEE_LOG_ffff0000be0b73e0);
  uVar13 = FUN_ffff0000be062f38();
  if ((uVar13 & 1) != 0) {
    FUN_ffff0000be0634dc();
    FUN_ffff0000be063628(0,&DAT_ffff0000be0fb008,0x2030);
  }
  FUN_ffff0000be063f04(0);
  pcVar33 = DAT_ffff0000be0fd048;
  pcVar33[0] = -0x7f;
  pcVar33[1] = 'E';
  pcVar33[2] = 'L';
  pcVar33[3] = 'F';
  FUN_ffff0000be007308(pcVar33,0x18);
  FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,DAT_ffff0000be0fd048);
  pcVar33 = s_kedump_mini_done_ffff0000be0ab2a2;
LAB_ffff0000be00e1f8:
  FUN_ffff0000be063b40(pcVar33);
  FUN_ffff0000be05fd98(pcVar33);
  return;
code_r0xffff0000be00e2e4:
  iVar9 = FUN_ffff0000be060cb8(puVar46,&DAT_ffff0000be0ae447);
  if (iVar9 == 0) {
    puVar2 = (ulong *)((long)puVar46 + (ulong)*puVar36);
    if (uVar43 == 0x28) {
      uVar45 = (uint)*puVar2;
      lVar30 = 0;
      uVar43 = *(uint *)((long)puVar2 + 0xc) + uVar45;
      do {
        piVar41 = (int *)(DAT_ffff0000be0fd0b8 + lVar30 + (ulong)DAT_ffff0000be0fd0c4 + 0x10);
        if (piVar41[-4] == 1) {
          uVar22 = piVar41[-2];
          uVar32 = *piVar41 + uVar22;
          if (uVar22 <= uVar45 && uVar43 <= uVar32) goto LAB_ffff0000be00e280;
          if ((uVar22 < uVar43) && (uVar45 < uVar32)) {
            if (uVar45 <= uVar22) {
              uVar22 = uVar45;
            }
            if (uVar32 <= uVar43) {
              uVar32 = uVar43;
            }
LAB_ffff0000be00e388:
            pcVar3 = DAT_ffff0000be0fd0b8 + lVar30 + (ulong)DAT_ffff0000be0fd0c4;
            pcVar3[0] = '\x01';
            pcVar3[1] = '\0';
            pcVar3[2] = '\0';
            pcVar3[3] = '\0';
            *(uint *)(pcVar3 + 8) = uVar22;
            pcVar3[0xc] = '\0';
            pcVar3[0xd] = '\0';
            pcVar3[0xe] = '\0';
            pcVar3[0xf] = '\0';
            *(uint *)(pcVar3 + 0x10) = uVar32 - uVar22;
            pcVar3[0x14] = '\0';
            pcVar3[0x15] = '\0';
            pcVar3[0x16] = '\0';
            pcVar3[0x17] = '\0';
            pcVar3[0x18] = '\0';
            pcVar3[0x19] = '\0';
            pcVar3[0x1a] = '\0';
            pcVar3[0x1b] = '\0';
            pcVar3[0x1c] = '\0';
            pcVar3[0x1d] = '\0';
            pcVar3[0x1e] = '\0';
            pcVar3[0x1f] = '\0';
            goto LAB_ffff0000be00e280;
          }
        }
        else {
          uVar32 = uVar43;
          uVar22 = uVar45;
          if (piVar41[-4] == 0) goto LAB_ffff0000be00e388;
        }
        lVar30 = lVar30 + 0x20;
      } while (lVar30 != 0x780);
      FUN_ffff0000be063b40(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
      FUN_ffff0000be05fd98(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
    }
    else {
      FUN_ffff0000be00feb8(*puVar2 | 0xff00000000000000,puVar2[3] + *puVar2 | 0xff00000000000000);
    }
  }
LAB_ffff0000be00e280:
  uVar28 = (ulong)((int)uVar28 + 1);
  goto LAB_ffff0000be00e28c;
}


