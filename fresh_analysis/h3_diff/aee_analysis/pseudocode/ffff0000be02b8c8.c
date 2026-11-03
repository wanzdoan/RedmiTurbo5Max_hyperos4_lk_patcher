
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be02b8c8(long param_1)

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
  iVar3 = FUN_ffff0000be05620c(param_1,PTR_s__soc_dfd_mcu_ffff0000be0d7268);
  if ((iVar3 < 0) &&
     (iVar3 = FUN_ffff0000be05620c(param_1,PTR_s__dfd_mcu_ffff0000be0d7270), iVar3 < 0)) {
    pcVar6 = s__s__couldn_t_find_the_dfd_mcu_no_ffff0000be0c51c7;
    goto LAB_ffff0000be02bfec;
  }
  piVar4 = (int *)FUN_ffff0000be056494(param_1,iVar3,s_enabled_ffff0000be0bf804,&local_24);
  if ((piVar4 == (int *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_enabled_proper_ffff0000be0ab62f;
    goto LAB_ffff0000be02bfec;
  }
  if (*piVar4 == 0) {
    FUN_ffff0000be05fd98
              (s__s__DFD_mcu_is_not_enabled_ffff0000be0a7f66,
               s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
    return 0;
  }
  puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_hw_version_ffff0000be0c632f,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_hw_version_pro_ffff0000be0a6e34;
    goto LAB_ffff0000be02bfec;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  DAT_ffff0000be118348 = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_sw_version_ffff0000be0c0a06,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
    pcVar6 = s__s__couldn_t_find_sw_version_pro_ffff0000be0b52ed;
    goto LAB_ffff0000be02bfec;
  }
  uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  DAT_ffff0000be11834c = uVar1 >> 0x10 | uVar1 << 0x10;
  puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_dfd_timeout_ffff0000be0b5314,&local_24);
  if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
LAB_ffff0000be02bfd8:
    pcVar6 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000be0afa50;
  }
  else {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    DAT_ffff0000be118350 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    puVar5 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar3,s_dump_to_dramc_channel_ffff0000be0ad848,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_property_dump__ffff0000be0a4870,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
      DAT_ffff0000be118358 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118358 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_buf_addr_align_ffff0000be0ac857,&local_24)
    ;
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_buf_addr_align_ffff0000be0bd1a8,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
      _DAT_ffff0000be118370 = 0x400000;
      FUN_ffff0000be05fd98
                (s__s__set_buf_addr_align_as_defaul_ffff0000be0c2d23,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9,0x400000);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff0000be118370 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_buf_addr_max_ffff0000be0be379,&local_24);
    if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_buf_addr_max_p_ffff0000be0a5b3e,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
      DAT_ffff0000be118378 = 0xffffffff;
      FUN_ffff0000be05fd98
                (s__s__set_buf_addr_max_as_default_v_ffff0000be0a7f82,
                 s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9,0xffffffff);
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118378 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    }
    puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_buf_length_ffff0000be0ab653,&local_24);
    if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118368 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_nr_max_core_ffff0000be0ac866,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_max_core_pr_ffff0000be0b5320;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118380 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_nr_big_core_ffff0000be0bf80c,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_big_core_pr_ffff0000be0bd1d3;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118384 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_nr_rs_entry_little_ffff0000be0b5348,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_nr_rs_entry_li_ffff0000be0ad85e,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
        DAT_ffff0000be118388 = 8;
        FUN_ffff0000be05fd98
                  (s__s__set_nr_rs_entry_little_as_de_ffff0000be0a7fb1,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9,8);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff0000be118388 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_nr_rs_entry_big_ffff0000be0baea0,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_nr_rs_entry_bi_ffff0000be0ab65e,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
        DAT_ffff0000be11838c = 0x10;
        FUN_ffff0000be05fd98
                  (s__s__set_nr_rs_entry_big_as_defau_ffff0000be0ac872,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9,0x10);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff0000be11838c = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_nr_header_row_ffff0000be0baeb0,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_nr_header_row_p_ffff0000be0bbe73;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118390 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_check_pattern_offset_ffff0000be0c1c1b,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_check_pattern__ffff0000be0ab68a,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
        DAT_ffff0000be118398 = 0;
        FUN_ffff0000be05fd98
                  (s__s__set_check_pattern_offset_as_d_ffff0000be0a5b67,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff0000be118398 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_chip_id_offset_ffff0000be0be386,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_chip_id_offset_ffff0000be0c0a11;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118394 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_dfd_disable_efuse_ffff0000be0b4113,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_dfd_disable_ef_ffff0000be0c633a;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be1183a4 = uVar1 >> 0x10 | uVar1 << 0x10;
      uVar1 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
      DAT_ffff0000be1183a8 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar5 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar3,s_efuse_sec_ctrl_ffff0000be0b8b0e,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        _DAT_ffff0000be1183ac = 0xffffffffffffffff;
        DAT_ffff0000be1183b4 = 0xffffffff;
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_efuse_sec_ctrl_ffff0000be0a90de,
                   s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
      }
      else {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        uVar2 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
        _DAT_ffff0000be1183ac =
             CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
        uVar1 = (puVar5[2] & 0xff00ff00) >> 8 | (puVar5[2] & 0xff00ff) << 8;
        DAT_ffff0000be1183b4 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      iVar3 = FUN_ffff0000be055ffc(param_1,iVar3,s_dfd_cache_ffff0000be0ae80f);
      if (iVar3 == 0) {
        pcVar6 = s__s__couldn_t_find_the_dfd_cache_s_ffff0000be0a48a2;
        goto LAB_ffff0000be02bfec;
      }
      puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_enabled_ffff0000be0bf804,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
        pcVar6 = s__s__couldn_t_find_property_cache_ffff0000be0c1c30;
        goto LAB_ffff0000be02bfec;
      }
      uVar1 = *puVar5;
      uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      DAT_ffff0000be1183b8 = uVar2 >> 0x10 | uVar2 << 0x10;
      if (uVar1 == 0) {
LAB_ffff0000be02bf94:
        DAT_ffff0000be1183e8 = 1;
        if (0x2000000 < DAT_ffff0000be118368) {
          FUN_ffff0000be05fd98
                    (s__s__buf_length__0x_lx__is_greate_ffff0000be0ac8a3,
                     s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9,DAT_ffff0000be118368,0x2000000)
          ;
          return 0xffffffea;
        }
        DAT_ffff0000be1183e8 = 1;
        return 1;
      }
      puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_dfd_timeout_ffff0000be0b5314,&local_24);
      if ((puVar5 == (uint *)0x0) || (local_24 == 0)) goto LAB_ffff0000be02bfd8;
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000be118350 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
      puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_buf_length_ffff0000be0ab653,&local_24);
      if ((puVar5 != (uint *)0x0) && (local_24 != 0)) {
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff0000be118368 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar3,s_tap_en_ffff0000be0a5b98,&local_24);
        if ((puVar5 == (uint *)0x0) || (local_24 == 0)) {
          pcVar6 = s__s__couldn_t_find_tap_en_propert_ffff0000be0bf818;
          goto LAB_ffff0000be02bfec;
        }
        uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        DAT_ffff0000be1183c0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
        goto LAB_ffff0000be02bf94;
      }
    }
    pcVar6 = s__s__couldn_t_find_buf_length_pro_ffff0000be0b778b;
  }
LAB_ffff0000be02bfec:
  FUN_ffff0000be05fd98(pcVar6,s_get_dfd_mcu_setting_from_fdt_ffff0000be0b0ca9);
  return 0xffffffc3;
}


