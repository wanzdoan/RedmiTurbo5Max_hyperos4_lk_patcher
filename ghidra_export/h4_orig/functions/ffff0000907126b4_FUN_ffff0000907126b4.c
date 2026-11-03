// ffff0000907126b4  size=1392  FUN_ffff0000907126b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907126b4(void)

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
  
  uVar4 = FUN_ffff000090710bc0();
  uVar8 = _DAT_ffff00009083f7d0;
  FUN_ffff00009077e11c
            (s__s__d___s_fdt_0x_p__mode__d_ffff0000907d3f27,s_io_task_callback_ffff0000907f0e12,0xbc
             ,s_io_task_callback_ffff0000907f0e12,_DAT_ffff00009083f7d0,uVar4 & 0xff);
  iVar5 = FUN_ffff000090716918();
  if ((uVar4 & 0xff) == 2) {
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_scp_ffff0000907fcc14,
               iVar6 - iVar5);
LAB_ffff00009071275c:
    iVar5 = FUN_ffff000090716918();
    if ((uVar4 & 0xff) != 2) {
      iVar6 = FUN_ffff000090720014(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_ccu_ffff0000907e5c06,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff0000907127bc;
      goto LAB_ffff000090712b24;
    }
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_ccu_ffff0000907e5c06,
               iVar6 - iVar5);
LAB_ffff0000907127bc:
    iVar5 = FUN_ffff000090716918();
    if ((uVar4 & 0xff) != 2) {
      iVar6 = FUN_ffff00009073fd3c(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_spm_ffff0000907df4cf,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712b44;
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff00009071d6e4(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_apu_ffff0000907dc04c,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712b64;
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff00009071d734(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_apusys_rv_ffff0000907ef466,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712b84;
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff000090722ae8();
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_dpm_ffff0000907dd9bf,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff000090712918;
      goto LAB_ffff000090712ba4;
    }
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_spm_ffff0000907df4cf,
               iVar6 - iVar5);
    iVar5 = FUN_ffff000090716918();
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_apu_ffff0000907dc04c,
               iVar6 - iVar5);
    iVar5 = FUN_ffff000090716918();
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_apusys_rv_ffff0000907ef466,
               iVar6 - iVar5);
    iVar5 = FUN_ffff000090716918();
    iVar6 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_dpm_ffff0000907dd9bf,
               iVar6 - iVar5);
LAB_ffff000090712918:
    iVar5 = FUN_ffff000090716918();
    iVar6 = FUN_ffff00009072f6c4(uVar8,uVar4);
    iVar7 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_modem_ffff0000907edbe1,
               iVar7 - iVar5);
    if (iVar6 != 0) {
      FUN_ffff00009077cf54
                (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc3,
                 s_app_load_modem_fdt__b_mode_____N_ffff0000907e758d,s_LK_MODEM_ffff0000907e2601);
LAB_ffff000090712ae4:
      FUN_ffff00009077cf54
                (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc6,
                 s_app_load_vcp_fdt__b_mode_____NO__ffff0000907fb16d,s_LK_VCP_ffff0000907d74f0);
      goto LAB_ffff000090712b04;
    }
    iVar5 = FUN_ffff000090716918();
    if ((uVar4 & 0xff) == 2) {
      iVar6 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_adsp_ffff0000907d74f7,
                 iVar6 - iVar5);
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_gpueb_ffff0000907d8ce4,
                 iVar6 - iVar5);
LAB_ffff000090712a10:
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff000090742d50(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_vcp_ffff0000907fb193,
                 iVar7 - iVar5);
      if (iVar6 != 0) goto LAB_ffff000090712ae4;
      iVar5 = FUN_ffff000090716918();
      if ((uVar4 & 0xff) == 8) {
        iVar6 = FUN_ffff000090716918();
        FUN_ffff00009077e11c
                  (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_connsys_ffff0000907ea73b,
                   iVar6 - iVar5);
LAB_ffff000090712aa4:
        FUN_ffff000090717624(0xffff00009083f7d8,1);
        return;
      }
      iVar6 = FUN_ffff0000907471f4(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_connsys_ffff0000907ea73b,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff000090712aa4;
      goto LAB_ffff000090712be4;
    }
    iVar6 = FUN_ffff0000907813b4(uVar8);
    iVar7 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_adsp_ffff0000907d74f7,
               iVar7 - iVar5);
    if ((iVar6 == -0x10) || (iVar6 == 0)) {
      iVar5 = FUN_ffff000090716918();
      iVar6 = FUN_ffff000090727cb8(uVar8);
      iVar7 = FUN_ffff000090716918();
      FUN_ffff00009077e11c
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_gpueb_ffff0000907d8ce4,
                 iVar7 - iVar5);
      if (iVar6 == 0) goto LAB_ffff000090712a10;
      goto LAB_ffff000090712bc4;
    }
  }
  else {
    iVar6 = FUN_ffff00009078a508(uVar8);
    iVar7 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_load_scp_ffff0000907fcc14,
               iVar7 - iVar5);
    if (iVar6 == 0) goto LAB_ffff00009071275c;
LAB_ffff000090712b04:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xbd,
               s_app_load_scp_fdt__b_mode_____NO__ffff0000907df4a9,s_LK_SCP_ffff0000907fb15e);
LAB_ffff000090712b24:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xbe,
               s_app_load_ccu_fdt__b_mode_____NO__ffff000090800556,s_LK_CCU_ffff0000907feadc);
LAB_ffff000090712b44:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xbf,
               s_app_load_spm_fdt__b_mode_____NO__ffff0000907edbbb,s_LK_SPM_ffff0000907d2658);
LAB_ffff000090712b64:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc0,
               s_app_load_apu_fdt__b_mode_____NO__ffff0000907d74ca,s_LK_APU_ffff0000907e430c);
LAB_ffff000090712b84:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc1,
               s_app_load_apusys_rv_fdt__b_mode____ffff0000907e8d69,s_LK_APUSYS_ffff0000907e0bbd);
LAB_ffff000090712ba4:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc2,
               s_app_load_dpm_b_mode_____NO_ERROR_ffff0000907e8d95,s_LK_DPM_ffff0000907d8cdd);
LAB_ffff000090712bc4:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc5,
               s_app_load_gpueb_fdt__b_mode_____N_ffff0000907ea713,s_LK_GPUEB_ffff00009080057c);
LAB_ffff000090712be4:
    FUN_ffff00009077cf54
              (s_app_mt_boot_io_task_c_ffff0000907d3f44,199,
               s_app_load_connsys_fdt__b_mode_____ffff0000907cf1b7,s_LK_CONNSYS_ffff0000907f5d1a);
  }
  uVar8 = FUN_ffff00009077cf54
                    (s_app_mt_boot_io_task_c_ffff0000907d3f44,0xc4,
                     s_app_load_adsp_fdt__b_mode_____NO_ffff0000907e8db6,s_LK_ADSP_ffff0000907fb165)
  ;
  pcStack_78 = FUN_ffff000090712c24;
  local_84 = 0;
  local_80 = &stack0xffffffffffffffd0;
  iVar5 = FUN_ffff0000907695a8();
  if (iVar5 != 2) {
    pcVar11 = s_met_set_fdt_skip_if_user_load_wi_ffff0000907e5c0f;
LAB_ffff000090712d34:
    FUN_ffff00009077e11c(pcVar11);
    return;
  }
  iVar5 = FUN_ffff00009076e32c(uVar8,0xffffffff,s_mediatek_met_res_ram_ffff0000907f9410);
  if (iVar5 < 0) {
    pcVar11 = s_met_set_fdt_skip_if_met_res_ram_i_ffff0000907dc055;
    goto LAB_ffff000090712d34;
  }
  iVar5 = FUN_ffff00009076d02c(uVar8,iVar5);
  if (-1 < iVar5) {
    uVar4 = 0;
    uVar15 = 0xffffffff;
    do {
      puVar9 = (uint *)FUN_ffff00009076df9c(uVar8,iVar5,&DAT_ffff0000907d587a,&local_84);
      if (puVar9 == (uint *)0x0) {
        pcVar11 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907d8cef;
LAB_ffff000090712cb4:
        FUN_ffff00009077e11c(pcVar11);
      }
      else {
        uVar2 = *puVar9;
        pcVar11 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907f4176;
        if (uVar2 == 0) goto LAB_ffff000090712cb4;
        if (1 < (int)uVar15) {
          FUN_ffff00009077e11c(s_met_set_fdt_skip_if_met_res_ram_t_ffff0000907f9425,3);
          return;
        }
        uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
        uVar15 = (long)(int)uVar15 + 1;
        uVar12 = (ulong)uVar4;
        uVar4 = (uVar2 >> 0x10 | uVar2 << 0x10) + uVar4;
        *(int *)(uVar15 * 0x10 + -0xffff6f7c07f8) = iVar5;
        *(ulong *)(uVar15 * 0x10 + -0xffff6f7c07f0) = uVar12;
      }
      iVar5 = FUN_ffff00009076d0fc(uVar8,iVar5);
    } while (-1 < iVar5);
    if (uVar4 != 0) {
      lVar10 = FUN_ffff0000907198dc
                         (uVar4,0x10000,0x9fffffff,0,0,s_debug_met_res_ram_ffff0000907d3f5a);
      if (lVar10 != 0) {
        iVar5 = FUN_ffff000090732838(lVar10,uVar4);
        if (iVar5 != 0) {
          FUN_ffff00009077e11c(s_met_set_emi_mpu_ret__d_ffff0000907e8ddd,iVar5);
        }
        if ((int)uVar15 < 0) {
          return;
        }
        plVar14 = (long *)((uVar15 & 0xffffffff) * 0x10 + -0xffff6f7c07f0);
        lVar13 = (uVar15 & 0xffffffff) + 1;
        do {
          uVar15 = (*plVar14 + lVar10 & 0xff00ff00ff00ff00U) >> 8 |
                   (*plVar14 + lVar10 & 0xff00ff00ff00ffU) << 8;
          uVar15 = (uVar15 & 0xffff0000ffff0000) >> 0x10 | (uVar15 & 0xffff0000ffff) << 0x10;
          local_90 = uVar15 >> 0x20 | uVar15 << 0x20;
          iVar5 = FUN_ffff00009076e6d4(uVar8,(int)plVar14[-1],s_start_ffff0000907d3f6c,&local_90,8);
          plVar14 = plVar14 + -2;
          if (iVar5 < 0) {
            FUN_ffff00009077e11c(s_met_set_start_addr_fails_ffff0000907dc081);
          }
          lVar3 = lVar13 + -1;
          bVar1 = 0 < lVar13;
          lVar13 = lVar3;
        } while (lVar3 != 0 && bVar1);
        return;
      }
      pcVar11 = s_met_res_ram_allocation_failed_ffff0000907f9451;
      goto LAB_ffff000090712e14;
    }
  }
  pcVar11 = s_met_set_fdt_skip_if_total_res_ra_ffff0000907fb19c;
LAB_ffff000090712e14:
  FUN_ffff00009077e11c(pcVar11);
  return;
}


