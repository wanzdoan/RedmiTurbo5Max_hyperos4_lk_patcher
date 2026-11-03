
void FUN_ffff0000907035c8(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  
  uVar3 = FUN_ffff00009075fb2c();
  if ((uVar3 & 1) != 0) {
    iVar1 = FUN_ffff000090716918();
    FUN_ffff0000907949e4();
    iVar2 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_ufs_init___ffff0000907f0bac,
               iVar2 - iVar1);
  }
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090798c9c();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_sec_lk_vb_init___ffff0000907d7214,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009072195c();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_dconfig_initenv___ffff0000907e093e,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff0000907419fc();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_env_init___ffff0000907d08a3,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  lVar4 = FUN_ffff00009071c4fc();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_fdt___platform_load_device_tree__ffff000090801d1c,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff0000907411ac();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_mt6373_device_register___ffff0000907ef25a,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090740e6c(1,6);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_mt6316_device_register_SPMI_MAST_ffff0000907ed99b,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090740e6c(1,7);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_mt6316_device_register_SPMI_MAST_ffff0000907ea535,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090740e6c(1,8);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_mt6316_device_register_SPMI_MAST_ffff0000907fca15,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090740e6c(1,0xf);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_mt6316_device_register_SPMI_MAST_ffff0000907da45d,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009078e95c(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_mt_usb_phy_init_fdt__ffff0000907f0bb7
             ,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090746dfc(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_clk_buf_init_fdt__ffff0000907e4003,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff0000907491ac(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_mt_dcm_init_fdt__ffff0000907dbe5e,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090728c70(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_i2c_hw_init_fdt__ffff0000907e0950,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff0000907294a0(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_keypad_init_fdt__ffff0000907e4015,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009075c408(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_disp_sw_init_fdt__ffff0000907e0961,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090745d58();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_drv_video_init___ffff0000907faf4c,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090724fb0(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_emi_init_fdt__ffff0000907dbe6f,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff000090739740(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_dbgtop_drm_init_fdt__ffff0000907f3dac
             ,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009073c870(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_dfd_soc_init_fdt__ffff0000907d7225,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009073b184(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_dfd_mcu_init_fdt__ffff0000907d241f,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009073d1ac(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_lastbus_init_fdt__ffff0000907e0973,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff0000907819d8(lVar4);
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,
             s_aee_mboot_expdb_init_fdt__ffff0000907e2448,iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009075e710();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_gpu_dfd_init___ffff0000907fe889,
             iVar2 - iVar1);
  iVar1 = FUN_ffff000090716918();
  FUN_ffff00009074a688();
  iVar2 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_efuse_check_vefuse___ffff0000907d7237
             ,iVar2 - iVar1);
  if (lVar4 != 0) {
    thunk_FUN_ffff0000907c314c(lVar4);
  }
  FUN_ffff0000907281b8();
  return;
}


