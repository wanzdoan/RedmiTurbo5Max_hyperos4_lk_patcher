
void FUN_ffff0000b8003564(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  
  uVar3 = FUN_ffff0000b80436ec();
  if ((uVar3 & 1) != 0) {
    iVar1 = FUN_ffff0000b800a01c();
    FUN_ffff0000b80585dc();
    iVar2 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_ufs_init___ffff0000b8097be4,
               iVar2 - iVar1);
  }
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b805b1bc();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_sec_lk_vb_init___ffff0000b8089811,
             iVar2 - iVar1);
  FUN_ffff0000b8018740();
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8014020();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_dconfig_initenv___ffff0000b808e7ce,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8027a68();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_env_init___ffff0000b8085e73,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  lVar4 = FUN_ffff0000b800e1b8();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_fdt___platform_load_device_tree__ffff0000b80a0c44,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8027338();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt6373_device_register___ffff0000b8096d77,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8026ff8(1,6);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt6316_device_register_SPMI_MAST_ffff0000b8095e89,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8026ff8(1,7);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt6316_device_register_SPMI_MAST_ffff0000b8093fcc,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8026ff8(1,8);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt6316_device_register_SPMI_MAST_ffff0000b809e12c,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8026ff8(1,0xf);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt6316_device_register_SPMI_MAST_ffff0000b808b4a0,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8040030(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt_gpio_set_default_fdt__ffff0000b8090665,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8019d14(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_i2c_hw_init_fdt__ffff0000b808e7e0,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b803e220(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_disp_init_fdt__ffff0000b80888ec,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b801baa0(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_leds_init_fdt__ffff0000b8096d90,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b801b994();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_leds_on___ffff0000b809a584,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8025550(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_spi_init_fdt__ffff0000b8095ebd,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b800fd40(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_charger_init_fdt__ffff0000b808a684,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b800e938(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mtk_battery_init_fdt__ffff0000b809b335,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8010e10();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_mtk_charger_start___ffff0000b80921d3,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8015b38(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_dvfs_init_fdt__ffff0000b809f14f,
             iVar2 - iVar1);
  uVar3 = FUN_ffff0000b80163ec();
  if ((uVar3 & 1) == 0) {
    iVar1 = FUN_ffff0000b800e1a4();
    if (iVar1 != 0) {
      iVar1 = FUN_ffff0000b800e1a4();
      goto joined_r0xffff0000b80038e4;
    }
  }
  else {
    iVar1 = FUN_ffff0000b801650c();
    FUN_ffff0000b804c33c(s_fe_boot_mode__d_ffff0000b80a0c66,iVar1);
    if (iVar1 != 0) {
joined_r0xffff0000b80038e4:
      if (iVar1 != 7) goto LAB_ffff0000b8003928;
    }
  }
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b805137c();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,
             s_mt_disp_show_boot_logo___ffff0000b808a696,iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  DAT_ffff0000b810c020 = iVar1 - DAT_ffff0000b810c030;
LAB_ffff0000b8003928:
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b801b5b0(lVar4);
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_hyp_init_by_fdt_fdt__ffff0000b809507d
             ,iVar2 - iVar1);
  if (lVar4 != 0) {
    thunk_FUN_ffff0000b8082558(lVar4);
  }
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b80413d4();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_strongbox_init___ffff0000b8089822,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000b800a01c();
  FUN_ffff0000b8040968();
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_mminfra_init___ffff0000b809ffc3,
             iVar2 - iVar1);
  return;
}


