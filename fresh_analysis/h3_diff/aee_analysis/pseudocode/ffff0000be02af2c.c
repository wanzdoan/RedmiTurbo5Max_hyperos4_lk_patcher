
void FUN_ffff0000be02af2c(undefined8 param_1)

{
  long lVar1;
  int iVar2;
  uint *puVar3;
  uint uVar4;
  undefined1 uVar5;
  long lVar6;
  ulong uVar7;
  int local_64;
  
  local_64 = 0;
  FUN_ffff0000be05fd98(s_dbg_err_flag_atag_info_dbg_err_f_ffff0000be0a480f,DAT_ffff0000be118194);
  if (2 < DAT_ffff0000be118194) {
    FUN_ffff0000be05fd98(s_DBG_ERR_FLAG_MAX__d__<_dbg_err_f_ffff0000be0af9fc,2);
    return;
  }
  if (DAT_ffff0000be118194 != 0) {
    uVar7 = 0;
    do {
      iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,&DAT_ffff0000be1181d0 + uVar7 * 0xcc);
      if (iVar2 < 0) {
        FUN_ffff0000be05fd98
                  (s__s__d___dts_probe_failed__ffff0000be0b2f7e,
                   s_dbg_err_flag_init_by_fdt_ffff0000be0ad7ed,0x1b3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_platform_mediatek_common_sda_dbg_ffff0000be0b40e7,0x1b4,&DAT_ffff0000be0a553d);
      }
      if ((int)uVar7 == 1) {
        FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,s_soc_dbg_err_flag_wdt_irq_mask_ffff0000be0d7140)
        ;
        puVar3 = (uint *)FUN_ffff0000be056494
                                   (param_1,iVar2,s_soc_dbg_err_flag_wdt_irq_mask_ffff0000be0d7140,
                                    &local_64);
        if ((local_64 < 1) || (puVar3 == (uint *)0x0)) {
          FUN_ffff0000be05fd98(s_fdt_fail_ffff0000be0bd17a);
          DAT_ffff0000be0d7184 = 0;
          DAT_ffff0000be0d7180 = '\0';
        }
        else {
          FUN_ffff0000be05fd98(s_fdt_pass_ffff0000be0a90d4);
          DAT_ffff0000be0d7180 = '\x01';
          uVar4 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
          DAT_ffff0000be0d7184 = uVar4 >> 0x10 | uVar4 << 0x10;
        }
        FUN_ffff0000be05fd98
                  (&DAT_ffff0000be0ba9be,s_mfg2soc_dfd_smmu_trigger_event_m_ffff0000be0d7188);
        puVar3 = (uint *)FUN_ffff0000be056494
                                   (param_1,iVar2,
                                    s_mfg2soc_dfd_smmu_trigger_event_m_ffff0000be0d7188,&local_64);
        if ((local_64 < 1) || (puVar3 == (uint *)0x0)) {
          FUN_ffff0000be05fd98(s_fdt_fail_ffff0000be0bd17a);
          DAT_ffff0000be0d71cc = 0;
          DAT_ffff0000be0d71c8 = '\0';
        }
        else {
          FUN_ffff0000be05fd98(s_fdt_pass_ffff0000be0a90d4);
          DAT_ffff0000be0d71c8 = '\x01';
          uVar4 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
          DAT_ffff0000be0d71cc = uVar4 >> 0x10 | uVar4 << 0x10;
        }
        FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,s_vlp_tracker_timeout_mask_ffff0000be0d71d0);
        puVar3 = (uint *)FUN_ffff0000be056494
                                   (param_1,iVar2,s_vlp_tracker_timeout_mask_ffff0000be0d71d0,
                                    &local_64);
        if ((local_64 < 1) || (puVar3 == (uint *)0x0)) {
          FUN_ffff0000be05fd98(s_fdt_fail_ffff0000be0bd17a);
          DAT_ffff0000be0d7214 = 0;
          DAT_ffff0000be0d7210 = '\0';
        }
        else {
          FUN_ffff0000be05fd98(s_fdt_pass_ffff0000be0a90d4);
          DAT_ffff0000be0d7210 = '\x01';
          uVar4 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
          DAT_ffff0000be0d7214 = uVar4 >> 0x10 | uVar4 << 0x10;
        }
        FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,s_dfd_soc_trigger_event_mask_ffff0000be0d7218);
        puVar3 = (uint *)FUN_ffff0000be056494
                                   (param_1,iVar2,s_dfd_soc_trigger_event_mask_ffff0000be0d7218,
                                    &local_64);
        if ((local_64 < 1) || (puVar3 == (uint *)0x0)) {
          FUN_ffff0000be05fd98(s_fdt_fail_ffff0000be0bd17a);
          DAT_ffff0000be0d725c = 0;
          DAT_ffff0000be0d7258 = '\0';
        }
        else {
          FUN_ffff0000be05fd98(s_fdt_pass_ffff0000be0a90d4);
          DAT_ffff0000be0d7258 = '\x01';
          uVar4 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
          DAT_ffff0000be0d725c = uVar4 >> 0x10 | uVar4 << 0x10;
        }
      }
      else if ((int)uVar7 == 0) {
        lVar6 = 0;
        do {
          FUN_ffff0000be05fd98
                    (&DAT_ffff0000be0ba9be,s_vlp_trace_halt_irq_mask_ffff0000be0d6768 + lVar6);
          puVar3 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar2,
                                      s_vlp_trace_halt_irq_mask_ffff0000be0d6768 + lVar6,&local_64);
          if ((local_64 < 1) || (puVar3 == (uint *)0x0)) {
            FUN_ffff0000be05fd98(s_fdt_fail_ffff0000be0bd17a);
            uVar4 = 0;
            uVar5 = 0;
          }
          else {
            FUN_ffff0000be05fd98(s_fdt_pass_ffff0000be0a90d4);
            uVar5 = 1;
            uVar4 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
            uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
          }
          lVar1 = lVar6 + 0x48;
          *(uint *)((long)&DAT_ffff0000be0d67ac + lVar6) = uVar4;
          (&DAT_ffff0000be0d67a8)[lVar6] = uVar5;
          lVar6 = lVar1;
        } while (lVar1 != 0x9d8);
      }
      uVar7 = uVar7 + 1;
    } while (uVar7 < DAT_ffff0000be118194);
  }
  lVar6 = 0;
  do {
    if ((&DAT_ffff0000be0d67a8)[lVar6] != '\0') {
      FUN_ffff0000be05fd98
                (s__s___0x_x_ffff0000be0b657b,s_vlp_trace_halt_irq_mask_ffff0000be0d6768 + lVar6,
                 *(undefined4 *)((long)&DAT_ffff0000be0d67ac + lVar6));
    }
    lVar6 = lVar6 + 0x48;
  } while (lVar6 != 0x9d8);
  if (DAT_ffff0000be0d7180 != '\0') {
    FUN_ffff0000be05fd98
              (s__s___0x_x_ffff0000be0b657b,s_soc_dbg_err_flag_wdt_irq_mask_ffff0000be0d7140,
               DAT_ffff0000be0d7184);
  }
  if (DAT_ffff0000be0d71c8 != '\0') {
    FUN_ffff0000be05fd98
              (s__s___0x_x_ffff0000be0b657b,s_mfg2soc_dfd_smmu_trigger_event_m_ffff0000be0d7188,
               DAT_ffff0000be0d71cc);
  }
  if (DAT_ffff0000be0d7210 != '\0') {
    FUN_ffff0000be05fd98
              (s__s___0x_x_ffff0000be0b657b,s_vlp_tracker_timeout_mask_ffff0000be0d71d0,
               DAT_ffff0000be0d7214);
  }
  if (DAT_ffff0000be0d7258 != '\0') {
    FUN_ffff0000be05fd98
              (s__s___0x_x_ffff0000be0b657b,s_dfd_soc_trigger_event_mask_ffff0000be0d7218,
               DAT_ffff0000be0d725c);
  }
  return;
}


