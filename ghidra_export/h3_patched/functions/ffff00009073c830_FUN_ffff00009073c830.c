// ffff00009073c830  size=620  FUN_ffff00009073c830

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073c830(long param_1)

{
  uint uVar1;
  int iVar2;
  int *piVar3;
  uint *puVar4;
  char *pcVar5;
  int local_14;
  
  if (param_1 != 0) {
    local_14 = 2;
    iVar2 = FUN_ffff00009076d75c(param_1,PTR_s__soc_dfd_soc_ffff000090819f20);
    if ((iVar2 < 0) &&
       (iVar2 = FUN_ffff00009076d75c(param_1,PTR_s__dfd_soc_ffff000090819f28), iVar2 < 0)) {
      pcVar5 = s__s__couldn_t_find_the_dfd_soc_no_ffff0000907dac89;
    }
    else {
      piVar3 = (int *)FUN_ffff00009076dcd4(param_1,iVar2,s_enabled_ffff0000907f81f7,&local_14);
      if ((piVar3 == (int *)0x0) || (local_14 == 0)) {
        pcVar5 = s__s__couldn_t_find_enabled_proper_ffff0000907dabfd;
      }
      else {
        if (*piVar3 == 0) {
          FUN_ffff00009077de54
                    (s__s__dfd_soc_isn_t_enabled_ffff0000907d9303,
                     s_get_dfd_soc_setting_from_fdt_ffff000090802627);
          return 0;
        }
        puVar4 = (uint *)FUN_ffff00009076dcd4
                                   (param_1,iVar2,s_dfd_timeout_ffff0000907e93ed,&local_14);
        if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
          pcVar5 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000907e141c;
        }
        else {
          uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
          _DAT_ffff00009086e0b0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          puVar4 = (uint *)FUN_ffff00009076dcd4
                                     (param_1,iVar2,s_buf_addr_align_ffff0000907dc7e3,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff00009077de54
                      (s__s__couldn_t_find_buf_addr_align_ffff0000907f4810,
                       s_get_dfd_soc_setting_from_fdt_ffff000090802627);
            _DAT_ffff00009086e0c8 = 0x400000;
            FUN_ffff00009077de54
                      (s__s__set_buf_addr_align_as_defaul_ffff0000907e62c0,
                       s_get_dfd_soc_setting_from_fdt_ffff000090802627,0x400000);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            _DAT_ffff00009086e0c8 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff00009076dcd4
                                     (param_1,iVar2,s_buf_addr_max_ffff0000907f6393,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff00009077de54
                      (s__s__couldn_t_find_buf_addr_max_p_ffff0000907d2cf6,
                       s_get_dfd_soc_setting_from_fdt_ffff000090802627);
            _DAT_ffff00009086e0d0 = 0xffffffff;
            FUN_ffff00009077de54
                      (s__s__set_buf_addr_max_as_default_v_ffff0000907d2dd1,
                       s_get_dfd_soc_setting_from_fdt_ffff000090802627,0xffffffff);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            _DAT_ffff00009086e0d0 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff00009076dcd4
                                     (param_1,iVar2,s_buf_length_ffff0000907dac21,&local_14);
          if ((puVar4 != (uint *)0x0) && (local_14 != 0)) {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
            _DAT_ffff00009086e0c0 = (ulong)uVar1;
            _DAT_ffff00009086e0d8 = 1;
            if (uVar1 < 0x4000001) {
              _DAT_ffff00009086e0d8 = 1;
              return 1;
            }
            FUN_ffff00009077de54
                      (s__s__buf_length__0x_lx__is_greate_ffff0000907ec582,
                       s_get_dfd_soc_setting_from_fdt_ffff000090802627,_DAT_ffff00009086e0c0,
                       0x4000000);
            return 0xffffffea;
          }
          pcVar5 = s__s__couldn_t_find_buf_length_pro_ffff0000907ec51b;
        }
      }
    }
    FUN_ffff00009077de54(pcVar5,s_get_dfd_soc_setting_from_fdt_ffff000090802627);
  }
  return 0xffffffc3;
}


