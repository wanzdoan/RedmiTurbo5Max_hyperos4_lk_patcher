// ffff00009073cac8  size=620  FUN_ffff00009073cac8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073cac8(long param_1)

{
  uint uVar1;
  int iVar2;
  int *piVar3;
  uint *puVar4;
  char *pcVar5;
  int local_14;
  
  if (param_1 != 0) {
    local_14 = 2;
    iVar2 = FUN_ffff00009076da24(param_1,PTR_s__soc_dfd_soc_ffff000090819f40);
    if ((iVar2 < 0) &&
       (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__dfd_soc_ffff000090819f48), iVar2 < 0)) {
      pcVar5 = s__s__couldn_t_find_the_dfd_soc_no_ffff0000907dadab;
    }
    else {
      piVar3 = (int *)FUN_ffff00009076df9c(param_1,iVar2,s_enabled_ffff0000907f8393,&local_14);
      if ((piVar3 == (int *)0x0) || (local_14 == 0)) {
        pcVar5 = s__s__couldn_t_find_enabled_proper_ffff0000907dad1f;
      }
      else {
        if (*piVar3 == 0) {
          FUN_ffff00009077e11c
                    (s__s__dfd_soc_isn_t_enabled_ffff0000907d939a,
                     s_get_dfd_soc_setting_from_fdt_ffff00009080282b);
          return 0;
        }
        puVar4 = (uint *)FUN_ffff00009076df9c
                                   (param_1,iVar2,s_dfd_timeout_ffff0000907e9548,&local_14);
        if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
          pcVar5 = s__s__couldn_t_find_dfd_timeout_pr_ffff0000907e1504;
        }
        else {
          uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
          _DAT_ffff00009086e148 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          puVar4 = (uint *)FUN_ffff00009076df9c
                                     (param_1,iVar2,s_buf_addr_align_ffff0000907dc8d0,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff00009077e11c
                      (s__s__couldn_t_find_buf_addr_align_ffff0000907f4994,
                       s_get_dfd_soc_setting_from_fdt_ffff00009080282b);
            _DAT_ffff00009086e160 = 0x400000;
            FUN_ffff00009077e11c
                      (s__s__set_buf_addr_align_as_defaul_ffff0000907e63eb,
                       s_get_dfd_soc_setting_from_fdt_ffff00009080282b,0x400000);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            _DAT_ffff00009086e160 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff00009076df9c
                                     (param_1,iVar2,s_buf_addr_max_ffff0000907f6534,&local_14);
          if ((puVar4 == (uint *)0x0) || (local_14 == 0)) {
            FUN_ffff00009077e11c
                      (s__s__couldn_t_find_buf_addr_max_p_ffff0000907d2d33,
                       s_get_dfd_soc_setting_from_fdt_ffff00009080282b);
            _DAT_ffff00009086e168 = 0xffffffff;
            FUN_ffff00009077e11c
                      (s__s__set_buf_addr_max_as_default_v_ffff0000907d2e0e,
                       s_get_dfd_soc_setting_from_fdt_ffff00009080282b,0xffffffff);
          }
          else {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            _DAT_ffff00009086e168 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
          }
          puVar4 = (uint *)FUN_ffff00009076df9c
                                     (param_1,iVar2,s_buf_length_ffff0000907dad43,&local_14);
          if ((puVar4 != (uint *)0x0) && (local_14 != 0)) {
            uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
            uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
            _DAT_ffff00009086e158 = (ulong)uVar1;
            _DAT_ffff00009086e170 = 1;
            if (uVar1 < 0x4000001) {
              _DAT_ffff00009086e170 = 1;
              return 1;
            }
            FUN_ffff00009077e11c
                      (s__s__buf_length__0x_lx__is_greate_ffff0000907ec71c,
                       s_get_dfd_soc_setting_from_fdt_ffff00009080282b,_DAT_ffff00009086e158,
                       0x4000000);
            return 0xffffffea;
          }
          pcVar5 = s__s__couldn_t_find_buf_length_pro_ffff0000907ec6b5;
        }
      }
    }
    FUN_ffff00009077e11c(pcVar5,s_get_dfd_soc_setting_from_fdt_ffff00009080282b);
  }
  return 0xffffffc3;
}


