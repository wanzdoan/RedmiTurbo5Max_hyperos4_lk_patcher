// ffff0000907125d0  size=1392  FUN_ffff0000907125d0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907125d0(void)

{
  bool bVar1;
  uint uVar2;
  long lVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  undefined8 uVar8;
  uint *puVar9;
  long lVar10;
  char *pcVar11;
  ulong uVar12;
  long lVar13;
  long *plVar14;
  ulong uVar15;
  ulong local_90;
  undefined4 local_84;
  undefined1 *local_80;
  code *pcStack_78;
  
  uVar4 = FUN_ffff000090710b48();
  uVar8 = _DAT_ffff00009083f798;
  FUN_ffff00009077de54
            (s__s__d___s_fdt_0x_p__mode__d_ffff0000907d3eea,s_io_task_callback_ffff0000907f0c8a,0xbc
             ,s_io_task_callback_ffff0000907f0c8a,_DAT_ffff00009083f798,uVar4 & 0xff);
  iVar5 = FUN_ffff0000907167d4();
  if ((uVar4 & 0xff) == 2) {
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_scp_ffff0000907fca6c,
               iVar6 - iVar5);
LAB_ffff000090712678:
    iVar5 = FUN_ffff0000907167d4();
    if ((uVar4 & 0xff) != 2) {
      iVar6 = FUN_ffff00009071fe8c(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_ccu_ffff0000907e5adb,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff0000907126d8;
      goto LAB_ffff000090712a40;
    }
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_ccu_ffff0000907e5adb,
               iVar6 - iVar5);
LAB_ffff0000907126d8:
    iVar5 = FUN_ffff0000907167d4();
    if ((uVar4 & 0xff) != 2) {
      iVar6 = FUN_ffff00009073faa4(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_spm_ffff0000907df415,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712a60;
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff00009071d558(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_apu_ffff0000907dbf5f,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712a80;
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff00009071d5a8(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_apusys_rv_ffff0000907ef2de,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712aa0;
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff0000907228a4();
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_dpm_ffff0000907dd905,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff000090712834;
      goto LAB_ffff000090712ac0;
    }
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_spm_ffff0000907df415,
               iVar6 - iVar5);
    iVar5 = FUN_ffff0000907167d4();
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_apu_ffff0000907dbf5f,
               iVar6 - iVar5);
    iVar5 = FUN_ffff0000907167d4();
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_apusys_rv_ffff0000907ef2de,
               iVar6 - iVar5);
    iVar5 = FUN_ffff0000907167d4();
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_dpm_ffff0000907dd905,
               iVar6 - iVar5);
LAB_ffff000090712834:
    iVar5 = FUN_ffff0000907167d4();
    iVar6 = FUN_ffff00009072f42c(uVar8,uVar4);
    iVar7 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_modem_ffff0000907eda59,
               iVar7 - iVar5);
    if (iVar6 != 0) {
      FUN_ffff00009077cc8c
                (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc3,
                 s_app_load_modem_fdt__b_mode_____N_ffff0000907e7432,s_LK_MODEM_ffff0000907e2519);
LAB_ffff000090712a00:
      FUN_ffff00009077cc8c
                (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc6,
                 s_app_load_vcp_fdt__b_mode_____NO__ffff0000907fafd1,s_LK_VCP_ffff0000907d745a);
      goto LAB_ffff000090712a20;
    }
    iVar5 = FUN_ffff0000907167d4();
    if ((uVar4 & 0xff) == 2) {
      iVar6 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_adsp_ffff0000907d7461,
                 iVar6 - iVar5);
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_gpueb_ffff0000907d8c4d,
                 iVar6 - iVar5);
LAB_ffff00009071292c:
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff000090742ab8(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_vcp_ffff0000907faff7,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712a00;
      iVar5 = FUN_ffff0000907167d4();
      if ((uVar4 & 0xff) == 8) {
        iVar6 = FUN_ffff0000907167d4();
        FUN_ffff00009077de54
                  (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_connsys_ffff0000907ea5ca,
                   iVar6 - iVar5);
LAB_ffff0000907129c0:
        FUN_ffff0000907174e0(0xffff00009083f7a0,1);
        return;
      }
      iVar6 = FUN_ffff000090746f5c(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_connsys_ffff0000907ea5ca,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff0000907129c0;
      goto LAB_ffff000090712b00;
    }
    iVar6 = FUN_ffff0000907810ec(uVar8);
    iVar7 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_adsp_ffff0000907d7461,
               iVar7 - iVar5);
    if ((iVar6 == -0x10) || (iVar6 == 0)) {
      iVar5 = FUN_ffff0000907167d4();
      iVar6 = FUN_ffff000090727a74(uVar8);
      iVar7 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_gpueb_ffff0000907d8c4d,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff00009071292c;
      goto LAB_ffff000090712ae0;
    }
  }
  else {
    iVar6 = FUN_ffff00009078a248(uVar8);
    iVar7 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_load_scp_ffff0000907fca6c,
               iVar7 - iVar5);
    if (iVar6 == 0) goto LAB_ffff000090712678;
LAB_ffff000090712a20:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xbd,
               s_app_load_scp_fdt__b_mode_____NO__ffff0000907df3ef,s_LK_SCP_ffff0000907fafc2);
LAB_ffff000090712a40:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xbe,
               s_app_load_ccu_fdt__b_mode_____NO__ffff00009080037a,s_LK_CCU_ffff0000907fe934);
LAB_ffff000090712a60:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xbf,
               s_app_load_spm_fdt__b_mode_____NO__ffff0000907eda33,s_LK_SPM_ffff0000907d261b);
LAB_ffff000090712a80:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc0,
               s_app_load_apu_fdt__b_mode_____NO__ffff0000907d7434,s_LK_APU_ffff0000907e41f1);
LAB_ffff000090712aa0:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc1,
               s_app_load_apusys_rv_fdt__b_mode____ffff0000907e8c0e,s_LK_APUSYS_ffff0000907e0b03);
LAB_ffff000090712ac0:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc2,
               s_app_load_dpm_b_mode_____NO_ERROR_ffff0000907e8c3a,s_LK_DPM_ffff0000907d8c46);
LAB_ffff000090712ae0:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc5,
               s_app_load_gpueb_fdt__b_mode_____N_ffff0000907ea5a2,s_LK_GPUEB_ffff0000908003a0);
LAB_ffff000090712b00:
    FUN_ffff00009077cc8c
              (s_app_mt_boot_io_task_c_ffff0000907d3f07,199,
               s_app_load_connsys_fdt__b_mode_____ffff0000907cf1b7,s_LK_CONNSYS_ffff0000907f5b8c);
  }
  uVar8 = FUN_ffff00009077cc8c
                    (s_app_mt_boot_io_task_c_ffff0000907d3f07,0xc4,
                     s_app_load_adsp_fdt__b_mode_____NO_ffff0000907e8c5b,s_LK_ADSP_ffff0000907fafc9)
  ;
  pcStack_78 = FUN_ffff000090712b40;
  local_84 = 0;
  local_80 = &stack0xffffffffffffffd0;
  iVar5 = FUN_ffff0000907692e0();
  if (iVar5 != 2) {
    pcVar11 = s_met_set_fdt_skip_if_user_load_wi_ffff0000907e5ae4;
LAB_ffff000090712c50:
    FUN_ffff00009077de54(pcVar11);
    return;
  }
  iVar5 = FUN_ffff00009076e064(uVar8,0xffffffff,s_mediatek_met_res_ram_ffff0000907f9274);
  if (iVar5 < 0) {
    pcVar11 = s_met_set_fdt_skip_if_met_res_ram_i_ffff0000907dbf68;
    goto LAB_ffff000090712c50;
  }
  iVar5 = FUN_ffff00009076cd64(uVar8,iVar5);
  if (-1 < iVar5) {
    uVar4 = 0;
    uVar15 = 0xffffffff;
    do {
      puVar9 = (uint *)FUN_ffff00009076dcd4(uVar8,iVar5,&DAT_ffff0000907d582a,&local_84);
      if (puVar9 == (uint *)0x0) {
        pcVar11 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907d8c58;
LAB_ffff000090712bd0:
        FUN_ffff00009077de54(pcVar11);
      }
      else {
        uVar2 = *puVar9;
        pcVar11 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907f3fd3;
        if (uVar2 == 0) goto LAB_ffff000090712bd0;
        if (1 < (int)uVar15) {
          FUN_ffff00009077de54(s_met_set_fdt_skip_if_met_res_ram_t_ffff0000907f9289,3);
          return;
        }
        uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
        uVar15 = (long)(int)uVar15 + 1;
        uVar12 = (ulong)uVar4;
        uVar4 = (uVar2 >> 0x10 | uVar2 << 0x10) + uVar4;
        *(int *)(uVar15 * 0x10 + -0xffff6f7c0830) = iVar5;
        *(ulong *)(uVar15 * 0x10 + -0xffff6f7c0828) = uVar12;
      }
      iVar5 = FUN_ffff00009076ce34(uVar8,iVar5);
    } while (-1 < iVar5);
    if (uVar4 != 0) {
      lVar10 = FUN_ffff000090719798
                         (uVar4,0x10000,0x9fffffff,0,0,s_debug_met_res_ram_ffff0000907d3f1d);
      if (lVar10 != 0) {
        iVar5 = FUN_ffff0000907325a0(lVar10,uVar4);
        if (iVar5 != 0) {
          FUN_ffff00009077de54(s_met_set_emi_mpu_ret__d_ffff0000907e8c82,iVar5);
        }
        if ((int)uVar15 < 0) {
          return;
        }
        plVar14 = (long *)((uVar15 & 0xffffffff) * 0x10 + -0xffff6f7c0828);
        lVar13 = (uVar15 & 0xffffffff) + 1;
        do {
          uVar15 = (*plVar14 + lVar10 & 0xff00ff00ff00ff00U) >> 8 |
                   (*plVar14 + lVar10 & 0xff00ff00ff00ffU) << 8;
          uVar15 = (uVar15 & 0xffff0000ffff0000) >> 0x10 | (uVar15 & 0xffff0000ffff) << 0x10;
          local_90 = uVar15 >> 0x20 | uVar15 << 0x20;
          iVar5 = FUN_ffff00009076e40c(uVar8,(int)plVar14[-1],s_start_ffff0000907d3f2f,&local_90,8);
          plVar14 = plVar14 + -2;
          if (iVar5 < 0) {
            FUN_ffff00009077de54(s_met_set_start_addr_fails_ffff0000907dbf94);
          }
          lVar3 = lVar13 + -1;
          bVar1 = 0 < lVar13;
          lVar13 = lVar3;
        } while (lVar3 != 0 && bVar1);
        return;
      }
      pcVar11 = s_met_res_ram_allocation_failed_ffff0000907f92b5;
      goto LAB_ffff000090712d30;
    }
  }
  pcVar11 = s_met_set_fdt_skip_if_total_res_ra_ffff0000907fb000;
LAB_ffff000090712d30:
  FUN_ffff00009077de54(pcVar11);
  return;
}


