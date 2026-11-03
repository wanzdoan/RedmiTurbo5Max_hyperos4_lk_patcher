// ffff00009073b140  size=1904  FUN_ffff00009073b140

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073b140(long param_1)

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
  iVar3 = FUN_ffff00009076d75c(param_1,PTR_s__soc_dfd_mcu_ffff000090819f10);
  if ((iVar3 < 0) &&
     (iVar3 = FUN_ffff00009076d75c(param_1,PTR_s__dfd_mcu_ffff000090819f18), iVar3 < 0)) {
    pcVar6 = s__s__couldn_t_find_the_dfd_mcu_no_ffff000090800a8b;
    goto LAB_ffff00009073b864;
  }
  piVar4 = (int *)FUN_ffff00009076dcd4(param_1,iVar3,s_enabled_ffff0000907f81f7,&local_24);
  if ((piVar4 == (int *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_enabled_proper_ffff0000907dac14;
    goto LAB_ffff00009073b864;
  }
  if (*piVar4 == 0) {
    FUN_ffff00009077de54
              (s__s__DFD_mcu_is_not_enabled_ffff0000907d61e4,
               s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
    return 0;
  }
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_hw_version_ffff0000908025ee,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_hw_version_pro_ffff0000907d4679;
    goto LAB_ffff00009073b864;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  _DAT_ffff00009086dfe8 = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_sw_version_ffff0000907f9c98,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_sw_version_pro_ffff0000907e93dd;
    goto LAB_ffff00009073b864;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  _DAT_ffff00009086dfec = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_dfd_timeout_ffff0000907e9404,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
LAB_ffff00009073b850:
    pcVar6 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000907e1433;
  }
  else {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086dff0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    puVar5 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar3,s_dump_to_dramc_channel_ffff0000907de24b,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077de54
                (s__s__couldn_t_find_property_dump__ffff0000907d12a6,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
      _DAT_ffff00009086dff8 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086dff8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_buf_addr_align_ffff0000907dc7fa,&local_24)
    ;
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077de54
                (s__s__couldn_t_find_buf_addr_align_ffff0000907f4827,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
      _DAT_ffff00009086e010 = 0x400000;
      FUN_ffff00009077de54
                (s__s__set_buf_addr_align_as_defaul_ffff0000907fd43a,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6,0x400000);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e010 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_buf_addr_max_ffff0000907f63aa,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff00009077de54
                (s__s__couldn_t_find_buf_addr_max_p_ffff0000907d2cf6,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
      _DAT_ffff00009086e018 = 0xffffffff;
      FUN_ffff00009077de54
                (s__s__set_buf_addr_max_as_default_v_ffff0000907d6200,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6,0xffffffff);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e018 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_buf_length_ffff0000907dac38,&local_24);
    if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e008 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_nr_max_core_ffff0000907dc809,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_max_core_pr_ffff0000907e9410;
        goto LAB_ffff00009073b864;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e020 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_nr_big_core_ffff0000907f81ff,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_big_core_pr_ffff0000907f4852;
        goto LAB_ffff00009073b864;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e024 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_nr_rs_entry_little_ffff0000907e9438,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_nr_rs_entry_li_ffff0000907de261,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
        _DAT_ffff00009086e028 = 8;
        FUN_ffff00009077de54
                  (s__s__set_nr_rs_entry_little_as_de_ffff0000907d622f,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6,8);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e028 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_nr_rs_entry_big_ffff0000907f1407,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_nr_rs_entry_bi_ffff0000907dac43,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
        _DAT_ffff00009086e02c = 0x10;
        FUN_ffff00009077de54
                  (s__s__set_nr_rs_entry_big_as_defau_ffff0000907dc815,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6,0x10);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e02c = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_nr_header_row_ffff0000907f1417,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_header_row_p_ffff0000907f2bc4;
        goto LAB_ffff00009073b864;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e030 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_check_pattern_offset_ffff0000907fb829,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_check_pattern__ffff0000907dac6f,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
        _DAT_ffff00009086e038 = 0;
        FUN_ffff00009077de54
                  (s__s__set_check_pattern_offset_as_d_ffff0000907d2d1f,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e038 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_chip_id_offset_ffff0000907f63b7,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_chip_id_offset_ffff0000907f9ca3;
        goto LAB_ffff00009073b864;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e034 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_dfd_disable_efuse_ffff0000907e7a29,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_dfd_disable_ef_ffff0000908025f9;
        goto LAB_ffff00009073b864;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e044 = uVar1 >> 0x10 | uVar1 << 0x10;
      uVar1 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
      _DAT_ffff00009086e048 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar3,s_efuse_sec_ctrl_ffff0000907ee248,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        _DAT_ffff00009086e04c = 0xffffffffffffffff;
        _DAT_ffff00009086e054 = 0xffffffff;
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_efuse_sec_ctrl_ffff0000907d7adf,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        uVar2 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
        _DAT_ffff00009086e04c =
             CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
        uVar1 = (puVar5[2] & 0xff00ff00) >> 8 | (puVar5[2] & 0xff00ff) << 8;
        _DAT_ffff00009086e054 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      iVar3 = FUN_ffff00009076d54c(param_1,iVar3,s_dfd_cache_ffff0000907dfa1d);
      if (iVar3 == 0) {
        pcVar6 = s__s__couldn_t_find_the_dfd_cache_s_ffff0000907d12d8;
        goto LAB_ffff00009073b864;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_enabled_ffff0000907f81f7,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_property_cache_ffff0000907fb83e;
        goto LAB_ffff00009073b864;
      }
      uVar1 = *puVar5;
      uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      _DAT_ffff00009086e058 = uVar2 >> 0x10 | uVar2 << 0x10;
      if (uVar1 == 0) {
LAB_ffff00009073b80c:
        _DAT_ffff00009086e088 = 1;
        if (0x2000000 < _DAT_ffff00009086e008) {
          FUN_ffff00009077de54
                    (s__s__buf_length__0x_lx__is_greate_ffff0000907dc846,
                     s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6,_DAT_ffff00009086e008,0x2000000
                    );
          return 0xffffffea;
        }
        _DAT_ffff00009086e088 = 1;
        return 1;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_dfd_timeout_ffff0000907e9404,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) goto LAB_ffff00009073b850;
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086dff0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_buf_length_ffff0000907dac38,&local_24);
      if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e008 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_tap_en_ffff0000907d2d50,&local_24);
        if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
          pcVar6 = s__s__couldn_t_find_tap_en_propert_ffff0000907f820b;
          goto LAB_ffff00009073b864;
        }
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e060 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        goto LAB_ffff00009073b80c;
      }
    }
    pcVar6 = s__s__couldn_t_find_buf_length_pro_ffff0000907ec532;
  }
LAB_ffff00009073b864:
  FUN_ffff00009077de54(pcVar6,s_get_dfd_mcu_setting_from_fdt_ffff0000907e2df6);
  return 0xffffffc3;
}


