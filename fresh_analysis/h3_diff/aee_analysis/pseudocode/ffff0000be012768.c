
long * FUN_ffff0000be012768(undefined8 *param_1)

{
  uint uVar1;
  undefined8 *puVar2;
  long lVar3;
  int iVar4;
  char *pcVar5;
  undefined8 uVar6;
  long *plVar7;
  long lVar8;
  ulong uVar9;
  
  if (param_1 == (undefined8 *)0x0) {
    return (long *)0x0;
  }
  if (DAT_ffff0000be107938 == 0) {
    DAT_ffff0000be107938 = FUN_ffff0000be05f1bc(0x7800);
    if (DAT_ffff0000be107938 != 0) {
      FUN_ffff0000be060ae0(DAT_ffff0000be107938,0,0x7800);
      DAT_ffff0000be107940 = 0x7800;
      goto LAB_ffff0000be0127e4;
    }
    pcVar5 = s__ERROR_MT_RAMDUMP__failed_to_all_ffff0000be0bce18;
  }
  else {
    FUN_ffff0000be05fd98(s__ERROR_MT_RAMDUMP__mrdump_string_ffff0000be0aa0c8);
    pcVar5 = s_initialized_mrdump_string_wbuf_b_ffff0000be0b746d;
  }
  FUN_ffff0000be05fd98(pcVar5);
LAB_ffff0000be0127e4:
  FUN_ffff0000be05fd98
            (s__s_mrdump_cblock__p__crach_recor_ffff0000be0a31b0,s_gen_cmm_ffff0000be0c3c5d,param_1,
             param_1 + 0x1f);
  FUN_ffff0000be05fd98(s_crash_recode_msg___s_ffff0000be0b096e,(long)param_1 + 0xfc);
  FUN_ffff0000be0130ec(s__________________________________ffff0000be0aa0f9);
  FUN_ffff0000be0130ec(s__________________________________ffff0000be0aa0f9);
  FUN_ffff0000be0130ec(s_B__AREA_ffff0000be0b60de);
  FUN_ffff0000be0130ec(s_local__vmlinux_file_ffff0000be0bf411);
  FUN_ffff0000be0130ec(s__vmlinux_file____vmlinux__ffff0000be0bf426);
  FUN_ffff0000be0130ec(s_IF_OS_FILE___vmlinux_file____TRU_ffff0000be0a7a85);
  FUN_ffff0000be0130ec(&DAT_ffff0000be0af50d);
  FUN_ffff0000be0131c4(s_Load_Vmlinux_ffff0000be0c0490,s_vmlinux_file_ffff0000be0bab77);
  FUN_ffff0000be0130ec(&DAT_ffff0000be0a69f1);
  FUN_ffff0000be0130ec(s_local__ramdump_elf_core_file_ffff0000be0a7aaa);
  FUN_ffff0000be0130ec(s__ramdump_elf_core_file____SYS_CO_ffff0000be0b4fb1);
  FUN_ffff0000be0130ec(s_IF_OS_FILE___ramdump_elf_core_fi_ffff0000be0a4300);
  FUN_ffff0000be0130ec(&DAT_ffff0000be0af50d);
  FUN_ffff0000be0131c4
            (s_Load_RAMDUMP_Core_ELF__ffff0000be0a432e,s_ramdump_elf_core_file_ffff0000be0b3e0a);
  FUN_ffff0000be0130ec(&DAT_ffff0000be0a69f1);
  FUN_ffff0000be0130ec(s_sys_res_ffff0000be0ad390);
  FUN_ffff0000be0130ec(s_sys_config_cn__d_ffff0000be0b0984,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be0130ec(s_core_num__d_ffff0000be0b996d,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be0130ec(s_sys_o_mmuspaces_on_ffff0000be0a69f5);
  FUN_ffff0000be0130ec(s_sys_u_ffff0000be0bbc08);
  FUN_ffff0000be0130ec(s_title_os_pwd___ffff0000be0b2c59);
  FUN_ffff0000be0130ec(s_d_load_e__vmlinux_file_0x_llx_ffff0000be0c4dbf,0);
  FUN_ffff0000be0130ec(s__vmlinux_banner_V_STRING_linux_b_ffff0000be0c5fce);
  FUN_ffff0000be0130ec(s__vmlinux_banner_len_STRing_LENgt_ffff0000be0c4dde);
  FUN_ffff0000be0130ec(s__vmlinux_build_time_STRing_SPLIT_ffff0000be0aa132);
  FUN_ffff0000be0130ec(s_d_load_e__ramdump_elf_core_file___ffff0000be0c296d);
  FUN_ffff0000be0130ec(&DAT_ffff0000be0bf441);
  uVar1 = *(uint *)(param_1 + 1);
  iVar4 = FUN_ffff0000be0130ec(&DAT_ffff0000be0a4345,(long)param_1 + 0xfc);
  if (iVar4 != 0) {
    if (0 < (int)uVar1) {
      uVar9 = 0;
      puVar2 = param_1;
      do {
        FUN_ffff0000be0130ec(s___CPU_d_s_Registers_ffff0000be0aa19a,uVar9 & 0xffffffff);
        FUN_ffff0000be0130ec(s_core_select__d_ffff0000be0b997a,uVar9 & 0xffffffff);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0,puVar2[0x30]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,1,puVar2[0x31]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,2,puVar2[0x32]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,3,puVar2[0x33]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,4,puVar2[0x34]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,5,puVar2[0x35]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,6,puVar2[0x36]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,7,puVar2[0x37]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,8,puVar2[0x38]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,9,puVar2[0x39]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,10,puVar2[0x3a]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0xb,puVar2[0x3b]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0xc,puVar2[0x3c]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0xd,puVar2[0x3d]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0xe,puVar2[0x3e]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0xf,puVar2[0x3f]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x10,puVar2[0x40]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x11,puVar2[0x41]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x12,puVar2[0x42]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x13,puVar2[0x43]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x14,puVar2[0x44]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x15,puVar2[0x45]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x16,puVar2[0x46]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x17,puVar2[0x47]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x18,puVar2[0x48]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x19,puVar2[0x49]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x1a,puVar2[0x4a]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x1b,puVar2[0x4b]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x1c,puVar2[0x4c]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x1d,puVar2[0x4d]);
        FUN_ffff0000be0130ec(s_R_S_X_d_0x_016llx_ffff0000be0c18a9,0x1e,puVar2[0x4e]);
        FUN_ffff0000be0130ec(s_R_S_SP_0x_016llx_ffff0000be0a31d6,puVar2[0x4f]);
        FUN_ffff0000be0130ec(s_R_S_PC_0x_016llx_ffff0000be0ad3b5,puVar2[0x50]);
        FUN_ffff0000be0130ec(s_R_S_CPSR_0x_llx_ffff0000be0b2c6a,puVar2[0x51]);
        FUN_ffff0000be0130ec(s___END_CPU_Regs_ffff0000be0be00c);
        FUN_ffff0000be0130ec(s___CPU_d_s_MMU___System_Control_R_ffff0000be0b1aad,uVar9 & 0xffffffff)
        ;
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x30200___QUAD_0x_01_ffff0000be0bab9b,puVar2[0x58]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x34200___QUAD_0x_01_ffff0000be0a434a,puVar2[0x59]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x36200___QUAD_0x_01_ffff0000be0c4e31,puVar2[0x5a]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x30201___QUAD_0x_01_ffff0000be0ad3c7,puVar2[0x5b]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x30100___QUAD_0x_01_ffff0000be0a7ac8,puVar2[0x52]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x34100___QUAD_0x_01_ffff0000be0c4e62,puVar2[0x53]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x36100___QUAD_0x_01_ffff0000be0a576f,puVar2[0x54]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x30202___QUAD_0x_01_ffff0000be0af510,puVar2[0x55]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x34202___QUAD_0x_01_ffff0000be0a7af9,puVar2[0x56]);
        FUN_ffff0000be0130ec(s_PER_Set_SPR_0x36202___QUAD_0x_01_ffff0000be0ae4e8,puVar2[0x57]);
        FUN_ffff0000be0130ec(s_R_S_sp_el_d_0x_016llx_ffff0000be0bce62,0,puVar2[0x5c]);
        FUN_ffff0000be0130ec(s_R_S_sp_el_d_0x_016llx_ffff0000be0bce62,1,puVar2[0x5d]);
        FUN_ffff0000be0130ec(s_R_S_sp_el_d_0x_016llx_ffff0000be0bce62,2,puVar2[0x5e]);
        FUN_ffff0000be0130ec(s_R_S_sp_el_d_0x_016llx_ffff0000be0bce62,3,puVar2[0x5f]);
        FUN_ffff0000be0130ec(s___END_MMU___SCTLR_ffff0000be0a8d97);
        uVar9 = uVar9 + 1;
        puVar2 = puVar2 + 0x30;
      } while (uVar1 != uVar9);
    }
    FUN_ffff0000be0130ec(s___select_fault_cpu_ffff0000be0ae4bc);
    FUN_ffff0000be0130ec(s_core_select__u_ffff0000be0a8d64,*(undefined4 *)((long)param_1 + 0x17c));
    FUN_ffff0000be0130ec(s___select_fault_cpu_ffff0000be0ae4bc);
    FUN_ffff0000be0130ec(s_core_select__u_ffff0000be0a8d64,*(undefined4 *)((long)param_1 + 0x17c));
    uVar6 = FUN_ffff0000be01cd48();
    FUN_ffff0000be0130ec(s_mmu_format_linuxswap3_swapper_pg_ffff0000be0a437b,param_1[2]);
    FUN_ffff0000be0130ec(s_0xffffffffffffffff_0x_016llx_ffff0000be0a31e8,uVar6);
    uVar9 = param_1[2];
    if ((ulong)param_1[0xf] <= (ulong)param_1[2]) {
      uVar9 = param_1[0xf];
    }
    FUN_ffff0000be0130ec(s_trans_common_0x_016llx__0xffffff_ffff0000be0bf443,uVar9);
    FUN_ffff0000be0130ec(s_trans_tw_on_ffff0000be0bf46f);
    FUN_ffff0000be0130ec(s_trans_on_ffff0000be0b3e20);
    FUN_ffff0000be0130ec(s_mmu_scan_ffff0000be0bf47c);
    FUN_ffff0000be0130ec(s_mmu_list_pagetable_0x_llx_ffff0000be0a8daa,param_1[2]);
    FUN_ffff0000be0130ec(s___load_the_awareness_for_Linux_3_ffff0000be0b0996);
    FUN_ffff0000be0130ec(s_TASK_CONFIG____demo_arm64_kernel_ffff0000be0b6106);
    FUN_ffff0000be0130ec(s___load_Linux_menu__ffff0000be0b2c7b);
    FUN_ffff0000be0130ec(s_MENU_ReProgram____demo_arm64_ker_ffff0000be0b3e2a);
    FUN_ffff0000be0130ec(s_task_dmesg_ffff0000be0b1ad7);
    FUN_ffff0000be0130ec(s__ramdump_banner_V_STRING_linux_b_ffff0000be0babcc);
    FUN_ffff0000be0130ec(s__t32_max_strlen_255__ffff0000be0bf487);
    FUN_ffff0000be0130ec(s_IF__vmlinux_banner_len>__t32_max_ffff0000be0b7498);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0af50d);
    FUN_ffff0000be0130ec(s__ramdump_build_time_STRing_SPLIT_ffff0000be0c4e93);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__vmlinux_banner_____ffff0000be0b85f3);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__ramdump_banner_____ffff0000be0b6143);
    FUN_ffff0000be0130ec(s_IF____vmlinux_banner_____ramdump_ffff0000be0aa1af);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b998a);
    FUN_ffff0000be0130ec(s_PRINT___ERROR_________No_maching_ffff0000be0a57a0);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__or_check_your_mmu_ffff0000be0ab328);
    FUN_ffff0000be0130ec(s_ENDDO_ffff0000be0c3c65);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b2c8f);
    FUN_ffff0000be0130ec(s_IF____ramdump_build_time_____vml_ffff0000be0ac55c);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b998a);
    FUN_ffff0000be0130ec(s_PRINT___ERROR____________No_mach_ffff0000be0af53f);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__check_binary_and_v_ffff0000be0ad3f8);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__Symbole_build_tim_ffff0000be0a6a16);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__RAMDUM_build_time_ffff0000be0ac590);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b2c8f);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b1ae4);
    FUN_ffff0000be0130ec(s_ELSE_ffff0000be0a7b28);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0af50d);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__vmlinux_banner_____ffff0000be0be01c);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__ramdump_banner_____ffff0000be0bbc2b);
    FUN_ffff0000be0130ec(s_IF____vmlinux_banner_____ramdump_ffff0000be0aa1af);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b998a);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__________No_machin_ffff0000be0c3c71);
    FUN_ffff0000be0130ec(s_PRINT___ERROR__or_check_your_mmu_ffff0000be0c6027);
    FUN_ffff0000be0130ec(s_ENDDO_ffff0000be0bce79);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0a7b2e);
    FUN_ffff0000be0130ec(&DAT_ffff0000be0b1ae4);
    FUN_ffff0000be0130ec(s__y_l_sec_ffff0000be0c4edd);
    FUN_ffff0000be0130ec(s_l_r_f_ffff0000be0be04f);
    FUN_ffff0000be0130ec(s_WinPOS_0_0_0_0_100__6__0__0__W00_ffff0000be0b998e);
    FUN_ffff0000be0130ec(s_AREA_ffff0000be0a43ad);
    FUN_ffff0000be0130ec(s_ENDDO_ffff0000be0ae517);
    FUN_ffff0000be0130ec(s__________________________________ffff0000be0aa0f9);
    FUN_ffff0000be0130ec(s____End_CMM___ffff0000be0b1a36);
    FUN_ffff0000be0130ec(s__________________________________ffff0000be0aa0f9);
    plVar7 = (long *)0x0;
    if (((DAT_ffff0000be107938 != 0) && (0 < DAT_ffff0000be107940._4_4_)) &&
       (plVar7 = (long *)FUN_ffff0000be05f1bc(0x60), lVar3 = DAT_ffff0000be107938,
       plVar7 != (long *)0x0)) {
      *(undefined4 *)(plVar7 + 2) = 0;
      lVar8 = (long)DAT_ffff0000be107940._4_4_;
      plVar7[3] = 0;
      plVar7[4] = 0;
      *(undefined4 *)(plVar7 + 5) = 0;
      plVar7[6] = 0;
      plVar7[7] = 0;
      *plVar7 = lVar3;
      plVar7[1] = lVar8;
      *(undefined4 *)(plVar7 + 8) = 0;
      plVar7[9] = 0;
      plVar7[10] = 0;
      *(undefined4 *)(plVar7 + 0xb) = 0;
    }
    return plVar7;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_app_aee_mrdump_dbgscript_c_ffff0000be0c29db,0x14c,s_err_>_0_ffff0000be0bbb4e);
}


