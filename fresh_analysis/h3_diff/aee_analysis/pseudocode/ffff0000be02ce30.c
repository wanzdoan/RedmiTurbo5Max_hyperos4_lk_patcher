
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be02ce30(long param_1)

{
  uint uVar1;
  int iVar2;
  int *piVar3;
  uint *puVar4;
  char *pcVar5;
  int local_14;
  
  if (param_1 != 0) {
    local_14 = 2;
    iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__soc_dfd_soc_ffff0000be0d7278);
    if ((iVar2 < 0) &&
       (iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__dfd_soc_ffff0000be0d7280), iVar2 < 0)) {
      pcVar5 = s__s__couldn_t_find_the_dfd_soc_no_ffff0000be0ab6bb;
    }
    else {
      piVar3 = (int *)FUN_ffff0000be056494(param_1,iVar2,s_enabled_ffff0000be0bf804,&local_14);
      if ((piVar3 == (int *)0x0) || (local_14 == 0)) {
        pcVar5 = s__s__couldn_t_find_enabled_proper_ffff0000be0ab62f;
      }
      else {
        if (*piVar3 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__dfd_soc_isn_t_enabled_ffff0000be0aa40d,
                     s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368);
          return 0;
        }
        puVar4 = (uint *)FUN_ffff0000be056494
                                   (param_1,iVar2,s_dfd_timeout_ffff0000be0b5314,&local_14);
        if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
          pcVar5 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000be0afa50;
        }
        else {
          uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
          DAT_ffff0000be118410 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          puVar4 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar2,s_buf_addr_align_ffff0000be0ac857,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff0000be05fd98
                      (s__s__couldn_t_find_buf_addr_align_ffff0000be0bd1a8,
                       s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368);
            _DAT_ffff0000be118428 = 0x400000;
            FUN_ffff0000be05fd98
                      (s__s__set_buf_addr_align_as_defaul_ffff0000be0b2fdb,
                       s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368,0x400000);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            _DAT_ffff0000be118428 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar2,s_buf_addr_max_ffff0000be0be379,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff0000be05fd98
                      (s__s__couldn_t_find_buf_addr_max_p_ffff0000be0a5b3e,
                       s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368);
            DAT_ffff0000be118430 = 0xffffffff;
            FUN_ffff0000be05fd98
                      (s__s__set_buf_addr_max_as_default_v_ffff0000be0a5bf6,
                       s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368,0xffffffff);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            DAT_ffff0000be118430 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff0000be056494
                                     (param_1,iVar2,s_buf_length_ffff0000be0ab653,&local_14);
          if ((puVar4 != (uint *)0x0) && (local_14 != 0)) {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
            DAT_ffff0000be118420 = (ulong)uVar1;
            DAT_ffff0000be118438 = 1;
            if (uVar1 < 0x4000001) {
              DAT_ffff0000be118438 = 1;
              return 1;
            }
            FUN_ffff0000be05fd98
                      (s__s__buf_length__0x_lx__is_greate_ffff0000be0b77e5,
                       s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368,DAT_ffff0000be118420,
                       0x4000000);
            return 0xffffffea;
          }
          pcVar5 = s__s__couldn_t_find_buf_length_pro_ffff0000be0b778b;
        }
      }
    }
    FUN_ffff0000be05fd98(pcVar5,s_get_dfd_soc_setting_from_fdt_ffff0000be0c6368);
  }
  return 0xffffffc3;
}


