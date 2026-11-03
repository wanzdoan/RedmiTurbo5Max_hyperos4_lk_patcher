// ffff00009073a270  size=2504  FUN_ffff00009073a270

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073a270(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  ulong uVar5;
  uint local_64;
  
  if ((_DAT_ffff00009086ded4 < 2) &&
     (FUN_ffff00009077e11c
                (s__s__d___dbg_err_flag_num__d__<_D_ffff0000907f9d64,
                 s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x1ef), _DAT_ffff00009086ded4 == 0)) {
    return;
  }
  uVar5 = 0;
  puVar4 = (uint *)&DAT_ffff00009086df54;
  do {
    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,puVar4 + -0x11);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__s__d___dts_probe_failed__ffff0000907e630d,
                 s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x1f6);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x1f7,&DAT_ffff0000907d2506);
    }
    if ((int)uVar5 == 0) {
      uVar1 = (puVar4[-0x18] & 0xff00ff00) >> 8 | (puVar4[-0x18] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_systimer_l_offset_ffff0000907e7b32,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_systimer_l_offset_pr_ffff0000907cf816,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x288,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x289,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[-0x17] & 0xff00ff00) >> 8 | (puVar4[-0x17] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_systimer_h_offset_ffff0000907f9d96,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_systimer_h_offset_pr_ffff0000907f9da8,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x291,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x292,&DAT_ffff0000907d2506);
      }
      if (_DAT_ffff00009086ded8 < 3) {
        uVar1 = (puVar4[-0x1a] & 0xff00ff00) >> 8 | (puVar4[-0x1a] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_status0_offset_ffff0000907ee386,&local_64,4);
        if (iVar3 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_status0_offset_prope_ffff0000907de270,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x2b6,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x2b7,&DAT_ffff0000907d2506
                    );
        }
        uVar1 = (puVar4[-0x19] & 0xff00ff00) >> 8 | (puVar4[-0x19] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_status1_offset_ffff0000907e633b,&local_64,4);
        if (iVar3 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_status1_offset_prope_ffff0000907e2ded,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x2bf,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x2c0,&DAT_ffff0000907d2506
                    );
        }
        uVar1 = (puVar4[-0x16] & 0xff00ff00) >> 8 | (puVar4[-0x16] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff00009076e6d4
                          (param_1,iVar2,s_error_stage_offset_ffff0000907e2e29,&local_64,4);
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_error_stage_offset_p_ffff0000907dfa77,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x2c8,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x2c9,&DAT_ffff0000907d2506
                    );
        }
      }
      else {
        uVar1 = (_DAT_ffff00009086e074 & 0xff00ff00) >> 8 | (_DAT_ffff00009086e074 & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_irq_status_offset_ffff0000907ee374,&local_64,4)
        ;
        if (iVar3 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_irq_status_offset_pr_ffff0000907d1252,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x29b,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x29c,&DAT_ffff0000907d2506
                    );
        }
        uVar1 = (_DAT_ffff00009086e078 & 0xff00ff00) >> 8 | (_DAT_ffff00009086e078 & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wdt_status_offset_ffff0000907f2d2e,&local_64,4)
        ;
        if (iVar3 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_wdt_status_offset_pr_ffff000090800be5,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x2a4,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x2a5,&DAT_ffff0000907d2506
                    );
        }
        uVar1 = (_DAT_ffff00009086e07c & 0xff00ff00) >> 8 | (_DAT_ffff00009086e07c & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_error_type_offset_ffff0000907f8358,&local_64,4)
        ;
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___set_error_type_offset_pr_ffff0000907cf7d7,
                     s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x2ad,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x2ae,&DAT_ffff0000907d2506
                    );
        }
      }
    }
    else if ((int)uVar5 == 1) {
      uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_irq_status0_offset_ffff0000907e7b1f,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_irq_status0_offset_p_ffff0000907dc854,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x201,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x202,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[1] & 0xff00ff00) >> 8 | (puVar4[1] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_irq_status1_offset_ffff0000907e6328,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_irq_status1_offset_p_ffff0000907d1212,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x20a,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x20b,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[2] & 0xff00ff00) >> 8 | (puVar4[2] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wdt_status0_offset_ffff0000907e2dda,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_wdt_status0_offset_p_ffff0000907f151e,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x213,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x214,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[3] & 0xff00ff00) >> 8 | (puVar4[3] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wdt_status1_offset_ffff0000907efbc9,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_wdt_status1_offset_p_ffff0000907ec60e,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x21c,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x21d,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[4] & 0xff00ff00) >> 8 | (puVar4[4] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_irq_systimer_l_offset_ffff0000907dfa61,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_irq_systimer_l_offse_ffff0000907ec64e,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x225,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x226,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[5] & 0xff00ff00) >> 8 | (puVar4[5] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_irq_systimer_h_offset_ffff0000907fb961,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_irq_systimer_h_offse_ffff0000907fd45b,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x22e,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x22f,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[6] & 0xff00ff00) >> 8 | (puVar4[6] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_error_type_offset_ffff0000907f8358,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_error_type_offset_pr_ffff0000907cf7d7,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x237,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x238,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0xb] & 0xff00ff00) >> 8 | (puVar4[0xb] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_status_mon_offset_ffff0000907fd49e,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_status_mon_offset_pr_ffff0000907f491d,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x240,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x241,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0xc] & 0xff00ff00) >> 8 | (puVar4[0xc] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_status_mon_1_offset_ffff0000907f495c,&local_64,4)
      ;
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_status_mon_1_offset_p_ffff0000907fd4b0,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x249,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x24a,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0xd] & 0xff00ff00) >> 8 | (puVar4[0xd] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_drm_req_systimer_l_offset_ffff0000907f155e,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_req_systimer_l_offse_ffff0000907fd4f1,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x252,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x253,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0xe] & 0xff00ff00) >> 8 | (puVar4[0xe] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_drm_req_systimer_h_offset_ffff0000907dc894,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_req_systimer_h_offse_ffff0000907de213,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x25b,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x25c,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0xf] & 0xff00ff00) >> 8 | (puVar4[0xf] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_drm_ack_systimer_l_offset_ffff0000907eae0c,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_ack_systimer_l_offse_ffff0000907f2ceb,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x264,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x265,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0x10] & 0xff00ff00) >> 8 | (puVar4[0x10] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_drm_ack_systimer_h_offset_ffff0000907de256,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_ack_systimer_h_offse_ffff0000907dac94,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x26d,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x26e,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0x13] & 0xff00ff00) >> 8 | (puVar4[0x13] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_wdt_systimer_l_offset_ffff0000907fd534,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_wdt_systimer_l_offse_ffff0000907d7b1b,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x276,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x277,&DAT_ffff0000907d2506);
      }
      uVar1 = (puVar4[0x14] & 0xff00ff00) >> 8 | (puVar4[0x14] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar2 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_wdt_systimer_h_offset_ffff0000907ff1db,&local_64,4);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___set_wdt_systimer_h_offse_ffff0000907d9324,
                   s_dbg_err_flag_set_fdt_ffff0000907ff1c6,0x27f,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7af3,0x280,&DAT_ffff0000907d2506);
      }
    }
    uVar5 = uVar5 + 1;
    puVar4 = puVar4 + 0x33;
    if (_DAT_ffff00009086ded4 <= uVar5) {
      return;
    }
  } while( true );
}


