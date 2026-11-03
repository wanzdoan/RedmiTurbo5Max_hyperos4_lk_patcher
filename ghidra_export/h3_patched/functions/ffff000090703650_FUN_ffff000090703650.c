// ffff000090703650  size=1056  FUN_ffff000090703650

void FUN_ffff000090703650(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  
  uVar3 = FUN_ffff00009075f894();
  if ((uVar3 & 1) != 0) {
    iVar1 = FUN_ffff0000907167d4();
    FUN_ffff000090794724();
    iVar2 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_ufs_init___ffff0000907f0a24,
               iVar2 - iVar1);
  }
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff0000907989dc();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_sec_lk_vb_init___ffff0000907d717e,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009072171c();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_dconfig_initenv___ffff0000907e0884,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090741764();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_env_init___ffff0000907d0884,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  lVar4 = FUN_ffff00009071c3b8();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_fdt___platform_load_device_tree__ffff000090801b40,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090740f14();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_mt6373_device_register___ffff0000907ef0d2,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090740bd4(1,6);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_mt6316_device_register_SPMI_MAST_ffff0000907ed801,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090740bd4(1,7);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_mt6316_device_register_SPMI_MAST_ffff0000907ea3da,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090740bd4(1,8);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_mt6316_device_register_SPMI_MAST_ffff0000907fc879,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090740bd4(1,0xf);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_mt6316_device_register_SPMI_MAST_ffff0000907da3af,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009078e69c(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_mt_usb_phy_init_fdt__ffff0000907f0a2f
             ,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090746b64(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_clk_buf_init_fdt__ffff0000907e3f1b,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090748f14(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_mt_dcm_init_fdt__ffff0000907dbd3c,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090728a2c(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_i2c_hw_init_fdt__ffff0000907e0896,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009072925c(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_keypad_init_fdt__ffff0000907e3f2d,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009075c170(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_disp_sw_init_fdt__ffff0000907e08a7,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090745ac0();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_drv_video_init___ffff0000907fadb0,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090724d6c(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_emi_init_fdt__ffff0000907dbd4d,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff0000907394a8(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_dbgtop_drm_init_fdt__ffff0000907f3c09
             ,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009073c5d8(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_dfd_soc_init_fdt__ffff0000907d718f,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009073aeec(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_dfd_mcu_init_fdt__ffff0000907d23e2,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009073cf14(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_lastbus_init_fdt__ffff0000907e08b9,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff000090781710(lVar4);
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
             s_aee_mboot_expdb_init_fdt__ffff0000907e2360,iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009075e478();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_gpu_dfd_init___ffff0000907fe6e1,
             iVar2 - iVar1);
  iVar1 = FUN_ffff0000907167d4();
  FUN_ffff00009074a3f0();
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_efuse_check_vefuse___ffff0000907d71a1
             ,iVar2 - iVar1);
  if (lVar4 != 0) {
    thunk_FUN_ffff0000907c2ad8(lVar4);
  }
  FUN_ffff000090727f74();
  return;
}


