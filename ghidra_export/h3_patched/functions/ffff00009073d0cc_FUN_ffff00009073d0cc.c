// ffff00009073d0cc  size=1228  FUN_ffff00009073d0cc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073d0cc(long param_1)

{
  uint *puVar1;
  long lVar2;
  undefined4 *puVar3;
  int iVar4;
  uint *puVar5;
  long lVar6;
  uint *puVar7;
  char *pcVar8;
  ulong uVar9;
  undefined8 uVar10;
  uint *puVar11;
  uint uVar12;
  ulong uVar13;
  int local_64;
  
  if (param_1 != 0) {
    local_64 = 3;
    iVar4 = FUN_ffff00009076d75c(param_1,PTR_s__lastbus_ffff000090819f30);
    if (((iVar4 < 0) &&
        (iVar4 = FUN_ffff00009076d75c(param_1,PTR_s__lastbus_monitors_ffff000090819f38), iVar4 < 0))
       && (iVar4 = FUN_ffff00009076d75c(param_1,PTR_s__soc_lastbus_ffff000090819f40), iVar4 < 0)) {
      pcVar8 = s__s__couldn_t_find_the_lastbus_no_ffff0000907d46d1;
    }
    else {
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar4,s_enabled_ffff0000907f81f7,&local_64);
      if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_property_enabl_ffff0000907f63cf,
                   s_get_lastbus_setting_from_fdt_ffff0000907f8237);
        _DAT_ffff00009086e0e4 = 0;
        return 0xffffffc3;
      }
      uVar12 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009086e0e4 = uVar12 >> 0x10 | uVar12 << 0x10;
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar4,s_sw_version_ffff0000907f9c98,&local_64);
      if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_property_sw_ve_ffff0000907f63f3,
                   s_get_lastbus_setting_from_fdt_ffff0000907f8237);
        _DAT_ffff00009086e0e0 = 1;
      }
      else {
        uVar12 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e0e0 = uVar12 >> 0x10 | uVar12 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar4,s_timeout_ms_ffff0000907d133c,&local_64);
      if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_property_timeo_ffff0000907cf8bf,
                   s_get_lastbus_setting_from_fdt_ffff0000907f8237);
        _DAT_ffff00009086e0e8 = 0xffffffff;
      }
      else {
        uVar12 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        _DAT_ffff00009086e0e8 = uVar12 >> 0x10 | uVar12 << 0x10;
      }
      puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar4,s_timeout_type_ffff0000907dfa37,&local_64)
      ;
      if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff00009077de54
                  (s__s__couldn_t_find_property_timeo_ffff0000907d931e,
                   s_get_lastbus_setting_from_fdt_ffff0000907f8237);
        uVar12 = 0;
      }
      else {
        uVar12 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        uVar12 = uVar12 >> 0x10 | uVar12 << 0x10;
        if (1 < uVar12) {
          pcVar8 = s__s__timeout_type__d__is_invalid__ffff0000907de279;
LAB_ffff00009073d4fc:
          FUN_ffff00009077de54(pcVar8,s_get_lastbus_setting_from_fdt_ffff0000907f8237,uVar12);
          return 0xffffffea;
        }
      }
      _DAT_ffff00009086e0ec = uVar12;
      iVar4 = FUN_ffff00009076d54c(param_1,iVar4,s_monitors_ffff0000907fb8a9);
      if (iVar4 < 0) {
        pcVar8 = s__s__couldn_t_find_the_monitors_s_ffff0000907e944f;
      }
      else {
        iVar4 = FUN_ffff00009076cd64(param_1,iVar4);
        if (-1 < iVar4) {
          uVar13 = 0;
          puVar5 = (uint *)&DAT_ffff00009086e13c;
          do {
            lVar6 = FUN_ffff00009076dcd4(param_1,iVar4,s_monitor_name_ffff0000907e9477,&local_64);
            if ((lVar6 == 0) || (local_64 == 0)) {
              pcVar8 = s__s__couldn_t_find_property_monit_ffff0000907ee258;
              goto LAB_ffff00009073d540;
            }
            lVar2 = uVar13 * 0x80 + -0xffff6f791f08;
            if (local_64 < 0x29) {
              FUN_ffff00009077efe4(lVar2,lVar6);
            }
            else {
              FUN_ffff00009077efe4(lVar2,lVar6,0x28);
              *(undefined1 *)(uVar13 * 0x80 + -0xffff6f791ee1) = 0;
            }
            puVar7 = (uint *)FUN_ffff00009076dcd4(param_1,iVar4,&DAT_ffff0000907eaccf,&local_64);
            if ((puVar7 == (uint *)0x0) || (local_64 == 0)) {
              pcVar8 = s__s__couldn_t_find_property_monit_ffff0000907fd46b;
              goto LAB_ffff00009073d540;
            }
            uVar12 = *puVar7;
            if (uVar12 == 0) {
              pcVar8 = s__s__fail_to_get_base_ffff0000907fb8b2;
              goto LAB_ffff00009073d540;
            }
            uVar12 = (uVar12 & 0xff00ff00) >> 8 | (uVar12 & 0xff00ff) << 8;
            *(long *)(uVar13 * 0x80 + -0xffff6f791ee0) =
                 (long)(int)(uVar12 >> 0x10 | uVar12 << 0x10) + -0x1000000000000;
            puVar7 = (uint *)FUN_ffff00009076dcd4
                                       (param_1,iVar4,s_num_ports_ffff0000907ee281,&local_64);
            if ((puVar7 == (uint *)0x0) || (local_64 == 0)) {
              pcVar8 = s__s__couldn_t_find_property_num_p_ffff0000907ee28b;
              goto LAB_ffff00009073d540;
            }
            uVar12 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            *(uint *)(uVar13 * 0x80 + -0xffff6f791ed8) = uVar12 >> 0x10 | uVar12 << 0x10;
            puVar7 = (uint *)FUN_ffff00009076dcd4
                                       (param_1,iVar4,s_idle_masks_ffff00009080266e,&local_64);
            puVar3 = (undefined4 *)(uVar13 * 0x80 + -0xffff6f791ed0);
            if ((puVar7 == (uint *)0x0) || (local_64 == 0)) {
              *puVar3 = 0;
            }
            else {
              uVar12 = local_64 >> 3;
              uVar9 = (ulong)uVar12;
              *puVar3 = 1;
              if (0x47 < local_64) {
                pcVar8 = s__s__Error__number_of_idle_masks__ffff0000907e1462;
                uVar10 = 8;
                goto LAB_ffff00009073d588;
              }
              *(uint *)(uVar13 * 0x80 + -0xffff6f791ecc) = uVar12;
              if (7 < local_64) {
                if ((int)uVar12 < 2) {
                  uVar12 = 1;
                }
                uVar9 = (ulong)uVar12;
                puVar11 = puVar5;
                do {
                  uVar12 = *puVar7;
                  if (uVar12 == 0) {
                    pcVar8 = s__s__invalid_idle_mask_offset__0x_ffff0000907e1499;
                    uVar12 = 0;
                    goto LAB_ffff00009073d4fc;
                  }
                  uVar12 = (uVar12 & 0xff00ff00) >> 8 | (uVar12 & 0xff00ff) << 8;
                  uVar9 = uVar9 - 1;
                  puVar11[-1] = uVar12 >> 0x10 | uVar12 << 0x10;
                  puVar1 = puVar7 + 1;
                  puVar7 = puVar7 + 2;
                  uVar12 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
                  *puVar11 = uVar12 >> 0x10 | uVar12 << 0x10;
                  puVar11 = puVar11 + 2;
                } while (uVar9 != 0);
              }
            }
            puVar7 = (uint *)FUN_ffff00009076dcd4
                                       (param_1,iVar4,s_bus_freq_mhz_ffff0000907dfa44,&local_64);
            if ((puVar7 == (uint *)0x0) || (local_64 == 0)) {
              pcVar8 = s__s__couldn_t_find_property_bus_f_ffff0000907d46f5;
              goto LAB_ffff00009073d540;
            }
            lVar6 = uVar13 * 0x80;
            uVar13 = uVar13 + 1;
            uVar12 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            *(uint *)(lVar6 + -0xffff6f791ed4) = uVar12 >> 0x10 | uVar12 << 0x10;
            iVar4 = FUN_ffff00009076ce34(param_1,iVar4);
            puVar5 = puVar5 + 0x20;
          } while (-1 < iVar4);
          uVar12 = (uint)uVar13;
          if (uVar12 != 0) {
            if (uVar12 < 0x21) {
              _DAT_ffff00009086e0f0 = uVar12;
              return 0;
            }
            pcVar8 = s__s__Error__number_of_monitors__d_ffff0000907f4927;
            uVar9 = uVar13 & 0xffffffff;
            uVar10 = 0x20;
            _DAT_ffff00009086e0f0 = uVar12;
LAB_ffff00009073d588:
            FUN_ffff00009077de54
                      (pcVar8,s_get_lastbus_setting_from_fdt_ffff0000907f8237,uVar9,uVar10);
            return 0xffffffea;
          }
        }
        pcVar8 = s__s__there_is_no_lastbus_monitor_n_ffff0000907f4901;
      }
    }
LAB_ffff00009073d540:
    FUN_ffff00009077de54(pcVar8,s_get_lastbus_setting_from_fdt_ffff0000907f8237);
  }
  return 0xffffffc3;
}


