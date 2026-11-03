
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073b3d8(long param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int *piVar4;
  uint *puVar5;
  char *pcVar6;
  int local_24;
  
  if (param_1 == 0) {
    return 0xffffffc3;
  }
  local_24 = 2;
  iVar3 = FUN_ffff00009076da24(param_1,PTR_s__soc_dfd_mcu_ffff000090819f30);
  if ((iVar3 < 0) &&
     (iVar3 = FUN_ffff00009076da24(param_1,PTR_s__dfd_mcu_ffff000090819f38), iVar3 < 0)) {
    pcVar6 = s__s__couldn_t_find_the_dfd_mcu_no_ffff000090800c67;
    goto LAB_ffff00009073bafc;
  }
  piVar4 = (int *)FUN_ffff00009076df9c(param_1,iVar3,s_enabled_ffff0000907f8393,&local_24);
  if ((piVar4 == (int *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_enabled_proper_ffff0000907dad1f;
    goto LAB_ffff00009073bafc;
  }
  if (*piVar4 == 0) {
    FUN_ffff00009077e11c
              (s__s__DFD_mcu_is_not_enabled_ffff0000907d627a,
               s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
    return 0;
  }
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_hw_version_ffff0000908027f2,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_hw_version_pro_ffff0000907d46b6;
    goto LAB_ffff00009073bafc;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  DAT_ffff00009086e080 = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_sw_version_ffff0000907f9e34,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_sw_version_pro_ffff0000907e9521;
    goto LAB_ffff00009073bafc;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  DAT_ffff00009086e084 = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_dfd_timeout_ffff0000907e9548,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
LAB_ffff00009073bae8:
    pcVar6 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000907e1504;
  }
  else {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086e088 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    puVar5 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar3,s_dump_to_dramc_channel_ffff0000907de2ee,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077e11c
                (s__s__couldn_t_find_property_dump__ffff0000907d12e3,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
      DAT_ffff00009086e090 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e090 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_buf_addr_align_ffff0000907dc8d0,&local_24)
    ;
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077e11c
                (s__s__couldn_t_find_buf_addr_align_ffff0000907f4994,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
      _DAT_ffff00009086e0a8 = 0x400000;
      FUN_ffff00009077e11c
                (s__s__set_buf_addr_align_as_defaul_ffff0000907fd5e2,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7,0x400000);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e0a8 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_buf_addr_max_ffff0000907f6534,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077e11c
                (s__s__couldn_t_find_buf_addr_max_p_ffff0000907d2d33,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
      DAT_ffff00009086e0b0 = 0xffffffff;
      FUN_ffff00009077e11c
                (s__s__set_buf_addr_max_as_default_v_ffff0000907d6296,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7,0xffffffff);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0b0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_buf_length_ffff0000907dad43,&local_24);
    if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0a0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_nr_max_core_ffff0000907dc8df,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_max_core_pr_ffff0000907e9554;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0b8 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_nr_big_core_ffff0000907f839b,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_big_core_pr_ffff0000907f49bf;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0bc = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_nr_rs_entry_little_ffff0000907e957c,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_nr_rs_entry_li_ffff0000907de304,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
        DAT_ffff00009086e0c0 = 8;
        FUN_ffff00009077e11c
                  (s__s__set_nr_rs_entry_little_as_de_ffff0000907d62c5,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7,8);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff00009086e0c0 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_nr_rs_entry_big_ffff0000907f1578,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_nr_rs_entry_bi_ffff0000907dad4e,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
        DAT_ffff00009086e0c4 = 0x10;
        FUN_ffff00009077e11c
                  (s__s__set_nr_rs_entry_big_as_defau_ffff0000907dc8eb,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7,0x10);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff00009086e0c4 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_nr_header_row_ffff0000907f1588,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_header_row_p_ffff0000907f2d50;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0c8 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_check_pattern_offset_ffff0000907fb9c5,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_check_pattern__ffff0000907dad7a,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
        DAT_ffff00009086e0d0 = 0;
        FUN_ffff00009077e11c
                  (s__s__set_check_pattern_offset_as_d_ffff0000907d2d5c,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff00009086e0d0 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_chip_id_offset_ffff0000907f6541,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_chip_id_offset_ffff0000907f9e3f;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0cc = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_dfd_disable_efuse_ffff0000907e7b6d,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_dfd_disable_ef_ffff0000908027fd;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff00009086e0dc = uVar1 >> 0x10 | uVar1 << 0x10;
      uVar1 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
      DAT_ffff00009086e0e0 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar3,s_efuse_sec_ctrl_ffff0000907ee3b9,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        _DAT_ffff00009086e0e4 = 0xffffffffffffffff;
        DAT_ffff00009086e0ec = 0xffffffff;
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_efuse_sec_ctrl_ffff0000907d7b75,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        uVar2 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
        _DAT_ffff00009086e0e4 =
             CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
        uVar1 = (puVar5[2] & 0xff00ff00) >> 8 | (puVar5[2] & 0xff00ff) << 8;
        DAT_ffff00009086e0ec = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      iVar3 = FUN_ffff00009076d814(param_1,iVar3,s_dfd_cache_ffff0000907dfac0);
      if (iVar3 == 0) {
        pcVar6 = s__s__couldn_t_find_the_dfd_cache_s_ffff0000907d1315;
        goto LAB_ffff00009073bafc;
      }
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_enabled_ffff0000907f8393,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_property_cache_ffff0000907fb9da;
        goto LAB_ffff00009073bafc;
      }
      uVar1 = *puVar5;
      uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      DAT_ffff00009086e0f0 = uVar2 >> 0x10 | uVar2 << 0x10;
      if (uVar1 == 0) {
LAB_ffff00009073baa4:
        DAT_ffff00009086e120 = 1;
        if (0x2000000 < DAT_ffff00009086e0a0) {
          FUN_ffff00009077e11c
                    (s__s__buf_length__0x_lx__is_greate_ffff0000907dc91c,
                     s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7,DAT_ffff00009086e0a0,0x2000000)
          ;
          return 0xffffffea;
        }
        DAT_ffff00009086e120 = 1;
        return 1;
      }
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_dfd_timeout_ffff0000907e9548,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) goto LAB_ffff00009073bae8;
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e088 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_buf_length_ffff0000907dad43,&local_24);
      if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff00009086e0a0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_tap_en_ffff0000907d2d8d,&local_24);
        if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
          pcVar6 = s__s__couldn_t_find_tap_en_propert_ffff0000907f83a7;
          goto LAB_ffff00009073bafc;
        }
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff00009086e0f8 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        goto LAB_ffff00009073baa4;
      }
    }
    pcVar6 = s__s__couldn_t_find_buf_length_pro_ffff0000907ec6b5;
  }
LAB_ffff00009073bafc:
  FUN_ffff00009077e11c(pcVar6,s_get_dfd_mcu_setting_from_fdt_ffff0000907e2ec7);
  return 0xffffffc3;
}


