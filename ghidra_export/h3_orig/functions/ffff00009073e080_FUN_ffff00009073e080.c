// ffff00009073e080  size=772  FUN_ffff00009073e080

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073e080(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  char *pcVar5;
  int iVar6;
  uint local_68 [2];
  
  iVar2 = FUN_ffff00009076d75c(param_1,PTR_s__tracker_ffff000090819f70);
  if ((iVar2 < 0) &&
     (iVar2 = FUN_ffff00009076d75c(param_1,PTR_s__soc_tracker_ffff000090819f78), iVar2 < 0)) {
    pcVar5 = s__s__couldn_t_find_the_tracker_no_ffff0000907d2e07;
  }
  else {
    iVar2 = FUN_ffff00009076d54c(param_1,iVar2,s_subsys_ffff0000907fd4e6);
    if (-1 < iVar2) {
      iVar2 = FUN_ffff00009076cd64(param_1,iVar2);
      if (-1 < iVar2) {
        iVar6 = 0;
        local_68[1] = 0;
        local_68[0] = 0;
        do {
          uVar4 = FUN_ffff00009076d798(param_1,iVar2,local_68 + 1);
          FUN_ffff00009077de54
                    (s__s__the_name_of_subnod__d__is__s_ffff0000907e4af7,
                     s_tracker_dconfig_update_to_fdt_ffff0000907ec5f5,iVar6,uVar4);
          if ((_DAT_ffff00009086f120 >> (ulong)(iVar6 + 0x10U & 0x1f) & 1) == 0) {
            local_68[0] = 0;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_irq_en_ops_ffff0000907fd4ed,local_68,4);
            if (iVar3 != 0) {
LAB_ffff00009073e310:
              pcVar5 = s__s__fail_to_set_fdt_prop_irq_en__ffff0000907fb8d9;
LAB_ffff00009073e354:
              FUN_ffff00009077de54(pcVar5,s_tracker_dconfig_update_to_fdt_ffff0000907ec5f5);
              return;
            }
            FUN_ffff00009077de54
                      (s__s___s_irq_is_not_enabled__ffff0000907e1544,
                       s_tracker_dconfig_update_to_fdt_ffff0000907ec5f5,uVar4);
          }
          else {
            local_68[0] = (_DAT_ffff00009086f120 & 0x3f) << 0x18;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_irq_en_ops_ffff0000907fd4ed,local_68,4);
            if (iVar3 != 0) goto LAB_ffff00009073e310;
            uVar1 = (_DAT_ffff00009086f124 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f124 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wp_addr_ffff0000907ec613,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_addr_ffff0000907ff0bc;
              goto LAB_ffff00009073e354;
            }
            uVar1 = (_DAT_ffff00009086f128 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f128 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wp_addr_ext_ffff0000907f6431,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_addr_ffff0000907e151d;
              goto LAB_ffff00009073e354;
            }
            uVar1 = (_DAT_ffff00009086f12c & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f12c & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wp_mask_ffff0000907ec61b,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_mask_ffff0000907ead18;
              goto LAB_ffff00009073e354;
            }
            uVar1 = (_DAT_ffff00009086f130 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f130 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_wp_mask_ext_ffff0000907d6263,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_mask_ffff0000907d135d;
              goto LAB_ffff00009073e354;
            }
            uVar1 = (_DAT_ffff00009086f134 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f134 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c
                              (param_1,iVar2,s_wp_secure_option_ffff0000907daccd,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_secu_ffff0000907e2e2e;
              goto LAB_ffff00009073e354;
            }
          }
          iVar2 = FUN_ffff00009076ce34(param_1,iVar2);
          iVar6 = iVar6 + 1;
        } while (-1 < iVar2);
      }
      return;
    }
    pcVar5 = s__s__couldn_t_find_the_subsys_sub_ffff0000907e14f7;
  }
  FUN_ffff00009077de54(pcVar5,s_tracker_dconfig_update_to_fdt_ffff0000907ec5f5);
  return;
}


