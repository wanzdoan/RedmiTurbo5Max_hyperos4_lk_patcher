
undefined8 FUN_ffff00009073d364(long param_1)

{
  uint *puVar1;
  int iVar2;
  uint *puVar3;
  long lVar4;
  uint *puVar5;
  char *pcVar6;
  ulong uVar7;
  undefined8 uVar8;
  uint *puVar9;
  uint uVar10;
  ulong uVar11;
  int local_64;
  
  if (param_1 != 0) {
    local_64 = 3;
    iVar2 = FUN_ffff00009076da24(param_1,PTR_s__lastbus_ffff000090819f50);
    if (((iVar2 < 0) &&
        (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__lastbus_monitors_ffff000090819f58), iVar2 < 0))
       && (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__soc_lastbus_ffff000090819f60), iVar2 < 0)) {
      pcVar6 = s__s__couldn_t_find_the_lastbus_no_ffff0000907d470e;
    }
    else {
      puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_enabled_ffff0000907f8393,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_property_enabl_ffff0000907f6570,
                   s_get_lastbus_setting_from_fdt_ffff0000907f83d3);
        DAT_ffff00009086e17c = 0;
        return 0xffffffc3;
      }
      uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff00009086e17c = uVar10 >> 0x10 | uVar10 << 0x10;
      puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_sw_version_ffff0000907f9e34,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_property_sw_ve_ffff0000907f6594,
                   s_get_lastbus_setting_from_fdt_ffff0000907f83d3);
        DAT_ffff00009086e178 = 1;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        DAT_ffff00009086e178 = uVar10 >> 0x10 | uVar10 << 0x10;
      }
      puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_timeout_ms_ffff0000907d1379,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_property_timeo_ffff0000907cf8de,
                   s_get_lastbus_setting_from_fdt_ffff0000907f83d3);
        DAT_ffff00009086e180 = 0xffffffff;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        DAT_ffff00009086e180 = uVar10 >> 0x10 | uVar10 << 0x10;
      }
      puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_timeout_type_ffff0000907dfaf1,&local_64)
      ;
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__couldn_t_find_property_timeo_ffff0000907d93b5,
                   s_get_lastbus_setting_from_fdt_ffff0000907f83d3);
        uVar10 = 0;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        uVar10 = uVar10 >> 0x10 | uVar10 << 0x10;
        if (1 < uVar10) {
          pcVar6 = s__s__timeout_type__d__is_invalid__ffff0000907de333;
LAB_ffff00009073d794:
          FUN_ffff00009077e11c(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000907f83d3,uVar10);
          return 0xffffffea;
        }
      }
      DAT_ffff00009086e184 = uVar10;
      iVar2 = FUN_ffff00009076d814(param_1,iVar2,s_monitors_ffff0000907fba45);
      if (iVar2 < 0) {
        pcVar6 = s__s__couldn_t_find_the_monitors_s_ffff0000907e95aa;
      }
      else {
        iVar2 = FUN_ffff00009076d02c(param_1,iVar2);
        if (-1 < iVar2) {
          puVar3 = &DAT_ffff00009086e1d4;
          uVar11 = 0;
          do {
            lVar4 = FUN_ffff00009076df9c(param_1,iVar2,s_monitor_name_ffff0000907e95d2,&local_64);
            if ((lVar4 == 0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_monit_ffff0000907ee3e0;
              goto LAB_ffff00009073d7d8;
            }
            if (local_64 < 0x29) {
              FUN_ffff00009077f2ac(&DAT_ffff00009086e190 + uVar11 * 0x80,lVar4);
            }
            else {
              FUN_ffff00009077f2ac(&DAT_ffff00009086e190 + uVar11 * 0x80,lVar4,0x28);
              (&DAT_ffff00009086e1b7)[uVar11 * 0x80] = 0;
            }
            puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,&DAT_ffff0000907eae69,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_monit_ffff0000907fd613;
              goto LAB_ffff00009073d7d8;
            }
            uVar10 = *puVar5;
            if (uVar10 == 0) {
              pcVar6 = s__s__fail_to_get_base_ffff0000907fba4e;
              goto LAB_ffff00009073d7d8;
            }
            uVar10 = (uVar10 & 0xff00ff00) >> 8 | (uVar10 & 0xff00ff) << 8;
            (&DAT_ffff00009086e1b8)[uVar11 * 0x10] =
                 (long)(int)(uVar10 >> 0x10 | uVar10 << 0x10) + -0x1000000000000;
            puVar5 = (uint *)FUN_ffff00009076df9c
                                       (param_1,iVar2,s_num_ports_ffff0000907ee409,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_num_p_ffff0000907ee413;
              goto LAB_ffff00009073d7d8;
            }
            uVar10 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
            (&DAT_ffff00009086e1c0)[uVar11 * 0x20] = uVar10 >> 0x10 | uVar10 << 0x10;
            puVar5 = (uint *)FUN_ffff00009076df9c
                                       (param_1,iVar2,s_idle_masks_ffff000090802872,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              (&DAT_ffff00009086e1c8)[uVar11 * 0x20] = 0;
            }
            else {
              uVar10 = local_64 >> 3;
              uVar7 = (ulong)uVar10;
              (&DAT_ffff00009086e1c8)[uVar11 * 0x20] = 1;
              if (0x47 < local_64) {
                pcVar6 = s__s__Error__number_of_idle_masks__ffff0000907e154a;
                uVar8 = 8;
                goto LAB_ffff00009073d820;
              }
              (&DAT_ffff00009086e1cc)[uVar11 * 0x20] = uVar10;
              if (7 < local_64) {
                if ((int)uVar10 < 2) {
                  uVar10 = 1;
                }
                uVar7 = (ulong)uVar10;
                puVar9 = puVar3;
                do {
                  uVar10 = *puVar5;
                  if (uVar10 == 0) {
                    pcVar6 = s__s__invalid_idle_mask_offset__0x_ffff0000907e1581;
                    uVar10 = 0;
                    goto LAB_ffff00009073d794;
                  }
                  uVar10 = (uVar10 & 0xff00ff00) >> 8 | (uVar10 & 0xff00ff) << 8;
                  uVar7 = uVar7 - 1;
                  puVar9[-1] = uVar10 >> 0x10 | uVar10 << 0x10;
                  puVar1 = puVar5 + 1;
                  puVar5 = puVar5 + 2;
                  uVar10 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
                  *puVar9 = uVar10 >> 0x10 | uVar10 << 0x10;
                  puVar9 = puVar9 + 2;
                } while (uVar7 != 0);
              }
            }
            puVar5 = (uint *)FUN_ffff00009076df9c
                                       (param_1,iVar2,s_bus_freq_mhz_ffff0000907dfafe,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_bus_f_ffff0000907d4732;
              goto LAB_ffff00009073d7d8;
            }
            uVar7 = uVar11 + 1;
            uVar10 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
            (&DAT_ffff00009086e1c4)[uVar11 * 0x20] = uVar10 >> 0x10 | uVar10 << 0x10;
            iVar2 = FUN_ffff00009076d0fc(param_1,iVar2);
            puVar3 = puVar3 + 0x20;
            uVar11 = uVar7;
          } while (-1 < iVar2);
          uVar10 = (uint)uVar7;
          if (uVar10 != 0) {
            if (uVar10 < 0x21) {
              DAT_ffff00009086e188 = uVar10;
              return 0;
            }
            pcVar6 = s__s__Error__number_of_monitors__d_ffff0000907f4aab;
            uVar7 = uVar7 & 0xffffffff;
            uVar8 = 0x20;
            DAT_ffff00009086e188 = uVar10;
LAB_ffff00009073d820:
            FUN_ffff00009077e11c(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000907f83d3,uVar7,uVar8)
            ;
            return 0xffffffea;
          }
        }
        pcVar6 = s__s__there_is_no_lastbus_monitor_n_ffff0000907f4a85;
      }
    }
LAB_ffff00009073d7d8:
    FUN_ffff00009077e11c(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000907f83d3);
  }
  return 0xffffffc3;
}


