// ffff00009073e318  size=772  FUN_ffff00009073e318

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073e318(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  char *pcVar5;
  int iVar6;
  uint local_68 [2];
  
  iVar2 = FUN_ffff00009076da24(param_1,PTR_s__tracker_ffff000090819f90);
  if ((iVar2 < 0) &&
     (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__soc_tracker_ffff000090819f98), iVar2 < 0)) {
    pcVar5 = s__s__couldn_t_find_the_tracker_no_ffff0000907d2e44;
  }
  else {
    iVar2 = FUN_ffff00009076d814(param_1,iVar2,s_subsys_ffff0000907fd68e);
    if (-1 < iVar2) {
      iVar2 = FUN_ffff00009076d02c(param_1,iVar2);
      if (-1 < iVar2) {
        iVar6 = 0;
        local_68[1] = 0;
        local_68[0] = 0;
        do {
          uVar4 = FUN_ffff00009076da60(param_1,iVar2,local_68 + 1);
          FUN_ffff00009077e11c
                    (s__s__the_name_of_subnod__d__is__s_ffff0000907e4bfb,
                     s_tracker_dconfig_update_to_fdt_ffff0000907ec778,iVar6,uVar4);
          if ((_DAT_ffff00009086f1b8 >> (ulong)(iVar6 + 0x10U & 0x1f) & 1) == 0) {
            local_68[0] = 0;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_irq_en_ops_ffff0000907fd695,local_68,4);
            if (iVar3 != 0) {
LAB_ffff00009073e5a8:
              pcVar5 = s__s__fail_to_set_fdt_prop_irq_en__ffff0000907fba75;
LAB_ffff00009073e5ec:
              FUN_ffff00009077e11c(pcVar5,s_tracker_dconfig_update_to_fdt_ffff0000907ec778);
              return;
            }
            FUN_ffff00009077e11c
                      (s__s___s_irq_is_not_enabled__ffff0000907e1615,
                       s_tracker_dconfig_update_to_fdt_ffff0000907ec778,uVar4);
          }
          else {
            local_68[0] = (_DAT_ffff00009086f1b8 & 0x3f) << 0x18;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_irq_en_ops_ffff0000907fd695,local_68,4);
            if (iVar3 != 0) goto LAB_ffff00009073e5a8;
            uVar1 = (_DAT_ffff00009086f1bc & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f1bc & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wp_addr_ffff0000907ec796,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_addr_ffff0000907ff264;
              goto LAB_ffff00009073e5ec;
            }
            uVar1 = (_DAT_ffff00009086f1c0 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f1c0 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wp_addr_ext_ffff0000907f65bb,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_addr_ffff0000907e15ee;
              goto LAB_ffff00009073e5ec;
            }
            uVar1 = (_DAT_ffff00009086f1c4 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f1c4 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wp_mask_ffff0000907ec79e,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_mask_ffff0000907eae9b;
              goto LAB_ffff00009073e5ec;
            }
            uVar1 = (_DAT_ffff00009086f1c8 & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f1c8 & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_wp_mask_ext_ffff0000907d62f9,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_mask_ffff0000907d139a;
              goto LAB_ffff00009073e5ec;
            }
            uVar1 = (_DAT_ffff00009086f1cc & 0xff00ff00) >> 8 |
                    (_DAT_ffff00009086f1cc & 0xff00ff) << 8;
            local_68[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4
                              (param_1,iVar2,s_wp_secure_option_ffff0000907dadd8,local_68,4);
            if (iVar3 != 0) {
              pcVar5 = s__s__fail_to_set_fdt_prop_wp_secu_ffff0000907e2eff;
              goto LAB_ffff00009073e5ec;
            }
          }
          iVar2 = FUN_ffff00009076d0fc(param_1,iVar2);
          iVar6 = iVar6 + 1;
        } while (-1 < iVar2);
      }
      return;
    }
    pcVar5 = s__s__couldn_t_find_the_subsys_sub_ffff0000907e15c8;
  }
  FUN_ffff00009077e11c(pcVar5,s_tracker_dconfig_update_to_fdt_ffff0000907ec778);
  return;
}


