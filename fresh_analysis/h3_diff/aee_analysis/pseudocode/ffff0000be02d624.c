
undefined8 FUN_ffff0000be02d624(long param_1)

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
    iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__lastbus_ffff0000be0d7288);
    if (((iVar2 < 0) &&
        (iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__lastbus_monitors_ffff0000be0d7290), iVar2 < 0))
       && (iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__soc_lastbus_ffff0000be0d7298), iVar2 < 0)) {
      pcVar6 = s__s__couldn_t_find_the_lastbus_no_ffff0000be0a6e6d;
    }
    else {
      puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_enabled_ffff0000be0bf804,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_property_enabl_ffff0000be0be3b5,
                   s_get_lastbus_setting_from_fdt_ffff0000be0bf844);
        DAT_ffff0000be118444 = 0;
        return 0xffffffc3;
      }
      uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be118444 = uVar10 >> 0x10 | uVar10 << 0x10;
      puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_sw_version_ffff0000be0c0a06,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_property_sw_ve_ffff0000be0be3d9,
                   s_get_lastbus_setting_from_fdt_ffff0000be0bf844);
        DAT_ffff0000be118440 = 1;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        DAT_ffff0000be118440 = uVar10 >> 0x10 | uVar10 << 0x10;
      }
      puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_timeout_ms_ffff0000be0a4918,&local_64);
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_property_timeo_ffff0000be0a34fa,
                   s_get_lastbus_setting_from_fdt_ffff0000be0bf844);
        DAT_ffff0000be118448 = 0xffffffff;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        DAT_ffff0000be118448 = uVar10 >> 0x10 | uVar10 << 0x10;
      }
      puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_timeout_type_ffff0000be0ae840,&local_64)
      ;
      if ((puVar3 == (uint *)0x0) || (local_64 == 0)) {
        FUN_ffff0000be05fd98
                  (s__s__couldn_t_find_property_timeo_ffff0000be0aa428,
                   s_get_lastbus_setting_from_fdt_ffff0000be0bf844);
        uVar10 = 0;
      }
      else {
        uVar10 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        uVar10 = uVar10 >> 0x10 | uVar10 << 0x10;
        if (1 < uVar10) {
          pcVar6 = s__s__timeout_type__d__is_invalid__ffff0000be0ad88d;
LAB_ffff0000be02da54:
          FUN_ffff0000be05fd98(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000be0bf844,uVar10);
          return 0xffffffea;
        }
      }
      DAT_ffff0000be11844c = uVar10;
      iVar2 = FUN_ffff0000be055ffc(param_1,iVar2,s_monitors_ffff0000be0c1c5a);
      if (iVar2 < 0) {
        pcVar6 = s__s__couldn_t_find_the_monitors_s_ffff0000be0b535b;
      }
      else {
        iVar2 = FUN_ffff0000be055944(param_1,iVar2);
        if (-1 < iVar2) {
          puVar3 = &DAT_ffff0000be11849c;
          uVar11 = 0;
          do {
            lVar4 = FUN_ffff0000be056494(param_1,iVar2,s_monitor_name_ffff0000be0b5383,&local_64);
            if ((lVar4 == 0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_monit_ffff0000be0b8b35;
              goto LAB_ffff0000be02da98;
            }
            if (local_64 < 0x29) {
              FUN_ffff0000be060eb8(&DAT_ffff0000be118458 + uVar11 * 0x80,lVar4);
            }
            else {
              FUN_ffff0000be060eb8(&DAT_ffff0000be118458 + uVar11 * 0x80,lVar4,0x28);
              (&DAT_ffff0000be11847f)[uVar11 * 0x80] = 0;
            }
            puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0b65ca,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_monit_ffff0000be0c2d54;
              goto LAB_ffff0000be02da98;
            }
            uVar10 = *puVar5;
            if (uVar10 == 0) {
              pcVar6 = s__s__fail_to_get_base_ffff0000be0c1c63;
              goto LAB_ffff0000be02da98;
            }
            uVar10 = (uVar10 & 0xff00ff00) >> 8 | (uVar10 & 0xff00ff) << 8;
            (&DAT_ffff0000be118480)[uVar11 * 0x10] =
                 (long)(int)(uVar10 >> 0x10 | uVar10 << 0x10) + -0x1000000000000;
            puVar5 = (uint *)FUN_ffff0000be056494
                                       (param_1,iVar2,s_num_ports_ffff0000be0b8b5e,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_num_p_ffff0000be0b8b68;
              goto LAB_ffff0000be02da98;
            }
            uVar10 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
            (&DAT_ffff0000be118488)[uVar11 * 0x20] = uVar10 >> 0x10 | uVar10 << 0x10;
            puVar5 = (uint *)FUN_ffff0000be056494
                                       (param_1,iVar2,s_idle_masks_ffff0000be0c63af,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              (&DAT_ffff0000be118490)[uVar11 * 0x20] = 0;
            }
            else {
              uVar10 = local_64 >> 3;
              uVar7 = (ulong)uVar10;
              (&DAT_ffff0000be118490)[uVar11 * 0x20] = 1;
              if (0x47 < local_64) {
                pcVar6 = s__s__Error__number_of_idle_masks__ffff0000be0afa8a;
                uVar8 = 8;
                goto LAB_ffff0000be02dae0;
              }
              (&DAT_ffff0000be118494)[uVar11 * 0x20] = uVar10;
              if (7 < local_64) {
                if ((int)uVar10 < 2) {
                  uVar10 = 1;
                }
                uVar7 = (ulong)uVar10;
                puVar9 = puVar3;
                do {
                  uVar10 = *puVar5;
                  if (uVar10 == 0) {
                    pcVar6 = s__s__invalid_idle_mask_offset__0x_ffff0000be0afac1;
                    uVar10 = 0;
                    goto LAB_ffff0000be02da54;
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
            puVar5 = (uint *)FUN_ffff0000be056494
                                       (param_1,iVar2,s_bus_freq_mhz_ffff0000be0ae84d,&local_64);
            if ((puVar5 == (uint *)0x0) || (local_64 == 0)) {
              pcVar6 = s__s__couldn_t_find_property_bus_f_ffff0000be0a6e91;
              goto LAB_ffff0000be02da98;
            }
            uVar7 = uVar11 + 1;
            uVar10 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
            (&DAT_ffff0000be11848c)[uVar11 * 0x20] = uVar10 >> 0x10 | uVar10 << 0x10;
            iVar2 = FUN_ffff0000be055a14(param_1,iVar2);
            puVar3 = puVar3 + 0x20;
            uVar11 = uVar7;
          } while (-1 < iVar2);
          uVar10 = (uint)uVar7;
          if (uVar10 != 0) {
            if (uVar10 < 0x21) {
              DAT_ffff0000be118450 = uVar10;
              return 0;
            }
            pcVar6 = s__s__Error__number_of_monitors__d_ffff0000be0bd278;
            uVar7 = uVar7 & 0xffffffff;
            uVar8 = 0x20;
            DAT_ffff0000be118450 = uVar10;
LAB_ffff0000be02dae0:
            FUN_ffff0000be05fd98(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000be0bf844,uVar7,uVar8)
            ;
            return 0xffffffea;
          }
        }
        pcVar6 = s__s__there_is_no_lastbus_monitor_n_ffff0000be0bd252;
      }
    }
LAB_ffff0000be02da98:
    FUN_ffff0000be05fd98(pcVar6,s_get_lastbus_setting_from_fdt_ffff0000be0bf844);
  }
  return 0xffffffc3;
}


