// ffff000090739fd8  size=2504  FUN_ffff000090739fd8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090739fd8(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  ulong uVar5;
  uint local_64;
  
  if ((_DAT_ffff00009086de3c < 2) &&
     (FUN_ffff00009077de54
                (s__s__d___dbg_err_flag_num__d__<_D_ffff0000907f9bc8,
                 s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x1ef), _DAT_ffff00009086de3c == 0)) {
    return;
  }
  uVar5 = 0;
  puVar4 = (uint *)&DAT_ffff00009086debc;
  do {
    iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,puVar4 + -0x11);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__s__d___dts_probe_failed__ffff0000907e61e2,
                 s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x1f6);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x1f7,&DAT_ffff0000907d24c9);
    }
    if ((int)uVar5 == 0) {
      uVar1 = (puVar4[-0x18] & 0xff00ff00) >> 8 | (puVar4[-0x18] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_systimer_l_offset_ffff0000907e79d7,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_systimer_l_offset_pr_ffff0000907cf7f7,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x288,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x289,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[-0x17] & 0xff00ff00) >> 8 | (puVar4[-0x17] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_systimer_h_offset_ffff0000907f9bfa,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_systimer_h_offset_pr_ffff0000907f9c0c,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x291,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x292,&DAT_ffff0000907d24c9);
      }
      if (_DAT_ffff00009086de40 < 3) {
        uVar1 = (puVar4[-0x1a] & 0xff00ff00) >> 8 | (puVar4[-0x1a] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_status0_offset_ffff0000907ee1fe,&local_64,4);
        if (iVar3 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_status0_offset_prope_ffff0000907de1b6,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x2b6,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x2b7,&DAT_ffff0000907d24c9
                    );
        }
        uVar1 = (puVar4[-0x19] & 0xff00ff00) >> 8 | (puVar4[-0x19] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_status1_offset_ffff0000907e6210,&local_64,4);
        if (iVar3 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_status1_offset_prope_ffff0000907e2d05,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x2bf,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x2c0,&DAT_ffff0000907d24c9
                    );
        }
        uVar1 = (puVar4[-0x16] & 0xff00ff00) >> 8 | (puVar4[-0x16] & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff00009076e40c
                          (param_1,iVar2,s_error_stage_offset_ffff0000907e2d41,&local_64,4);
        if (iVar2 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_error_stage_offset_p_ffff0000907df9bd,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x2c8,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x2c9,&DAT_ffff0000907d24c9
                    );
        }
      }
      else {
        uVar1 = (_DAT_ffff00009086dfdc & 0xff00ff00) >> 8 | (_DAT_ffff00009086dfdc & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_irq_status_offset_ffff0000907ee1ec,&local_64,4)
        ;
        if (iVar3 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_irq_status_offset_pr_ffff0000907d1215,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x29b,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x29c,&DAT_ffff0000907d24c9
                    );
        }
        uVar1 = (_DAT_ffff00009086dfe0 & 0xff00ff00) >> 8 | (_DAT_ffff00009086dfe0 & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wdt_status_offset_ffff0000907f2b8b,&local_64,4)
        ;
        if (iVar3 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_wdt_status_offset_pr_ffff000090800a09,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x2a4,iVar3);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x2a5,&DAT_ffff0000907d24c9
                    );
        }
        uVar1 = (_DAT_ffff00009086dfe4 & 0xff00ff00) >> 8 | (_DAT_ffff00009086dfe4 & 0xff00ff) << 8;
        local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_error_type_offset_ffff0000907f81bc,&local_64,4)
        ;
        if (iVar2 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___set_error_type_offset_pr_ffff0000907cf7b8,
                     s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x2ad,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x2ae,&DAT_ffff0000907d24c9
                    );
        }
      }
    }
    else if ((int)uVar5 == 1) {
      uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_irq_status0_offset_ffff0000907e79c4,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_irq_status0_offset_p_ffff0000907dc767,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x201,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x202,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[1] & 0xff00ff00) >> 8 | (puVar4[1] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_irq_status1_offset_ffff0000907e61fd,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_irq_status1_offset_p_ffff0000907d11d5,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x20a,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x20b,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[2] & 0xff00ff00) >> 8 | (puVar4[2] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wdt_status0_offset_ffff0000907e2cf2,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_wdt_status0_offset_p_ffff0000907f1396,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x213,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x214,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[3] & 0xff00ff00) >> 8 | (puVar4[3] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wdt_status1_offset_ffff0000907efa41,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_wdt_status1_offset_p_ffff0000907ec474,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x21c,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x21d,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[4] & 0xff00ff00) >> 8 | (puVar4[4] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_irq_systimer_l_offset_ffff0000907df9a7,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_irq_systimer_l_offse_ffff0000907ec4b4,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x225,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x226,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[5] & 0xff00ff00) >> 8 | (puVar4[5] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_irq_systimer_h_offset_ffff0000907fb7c5,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_irq_systimer_h_offse_ffff0000907fd2b3,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x22e,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x22f,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[6] & 0xff00ff00) >> 8 | (puVar4[6] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_error_type_offset_ffff0000907f81bc,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_error_type_offset_pr_ffff0000907cf7b8,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x237,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x238,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0xb] & 0xff00ff00) >> 8 | (puVar4[0xb] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_status_mon_offset_ffff0000907fd2f6,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_status_mon_offset_pr_ffff0000907f4799,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x240,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x241,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0xc] & 0xff00ff00) >> 8 | (puVar4[0xc] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_status_mon_1_offset_ffff0000907f47d8,&local_64,4)
      ;
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_status_mon_1_offset_p_ffff0000907fd308,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x249,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x24a,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0xd] & 0xff00ff00) >> 8 | (puVar4[0xd] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_drm_req_systimer_l_offset_ffff0000907f13d6,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_req_systimer_l_offse_ffff0000907fd349,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x252,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x253,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0xe] & 0xff00ff00) >> 8 | (puVar4[0xe] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_drm_req_systimer_h_offset_ffff0000907dc7a7,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_req_systimer_h_offse_ffff0000907de159,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x25b,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x25c,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0xf] & 0xff00ff00) >> 8 | (puVar4[0xf] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_drm_ack_systimer_l_offset_ffff0000907eac72,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_ack_systimer_l_offse_ffff0000907f2b48,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x264,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x265,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0x10] & 0xff00ff00) >> 8 | (puVar4[0x10] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_drm_ack_systimer_h_offset_ffff0000907de19c,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_ack_systimer_h_offse_ffff0000907dab72,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x26d,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x26e,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0x13] & 0xff00ff00) >> 8 | (puVar4[0x13] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar3 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_wdt_systimer_l_offset_ffff0000907fd38c,&local_64,4);
      if (iVar3 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_wdt_systimer_l_offse_ffff0000907d7a85,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x276,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x277,&DAT_ffff0000907d24c9);
      }
      uVar1 = (puVar4[0x14] & 0xff00ff00) >> 8 | (puVar4[0x14] & 0xff00ff) << 8;
      local_64 = uVar1 >> 0x10 | uVar1 << 0x10;
      iVar2 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_wdt_systimer_h_offset_ffff0000907ff033,&local_64,4);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___set_wdt_systimer_h_offse_ffff0000907d928d,
                   s_dbg_err_flag_set_fdt_ffff0000907ff01e,0x27f,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_sda_dbg_ffff0000907e7998,0x280,&DAT_ffff0000907d24c9);
      }
    }
    uVar5 = uVar5 + 1;
    puVar4 = puVar4 + 0x33;
    if (_DAT_ffff00009086de3c <= uVar5) {
      return;
    }
  } while( true );
}


