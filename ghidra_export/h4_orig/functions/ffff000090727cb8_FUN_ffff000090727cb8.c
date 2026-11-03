// ffff000090727cb8  size=1280  FUN_ffff000090727cb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090727cb8(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  ulong *puVar4;
  uint *puVar5;
  char *pcVar6;
  undefined4 *puVar7;
  long lVar8;
  undefined4 *puVar9;
  uint uVar10;
  ulong uVar11;
  undefined4 *local_60;
  long local_58;
  int local_38;
  int local_34;
  
  local_58 = 0;
  if (_DAT_ffff00004b800504 >> 0x1e < 3) goto LAB_ffff000090727d24;
  if ((_DAT_ffff00004b800504 >> 0xc & 1) == 0) {
    if ((_DAT_ffff00004b800504 >> 0x17 & 1) != 0) {
      lVar3 = FUN_ffff0000907198dc(0x200000,0x10000,0x100000000,0,0,s_me_GPUEB_FW_ffff0000907fd00c);
      if (lVar3 == 0) {
        FUN_ffff00009077e11c
                  (s__s__GPUEB_FW_mblock_allocation_f_ffff0000907e2998,
                   s__GPUEB__LDR__ffff0000907e0ff2);
        return -5;
      }
      local_60 = (undefined4 *)0x0;
      local_38 = 0;
      puVar4 = (ulong *)FUN_ffff00009071a348(s_me_GPUmputab_PMA_ffff0000907ddd2e,0);
      uVar11 = *puVar4;
      iVar1 = FUN_ffff00009077ba64
                        (DAT_ffff000090839928,s_vm_GPUEB_reserved_ffff0000907f2919,0x200000,
                         &local_58,0xc,lVar3,0,0);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c
                  (s__s__GPUEB_vmm_alloc_physical_fai_ffff0000907e77fc,
                   s__GPUEB__LDR__ffff0000907e0ff2,iVar1);
        goto LAB_ffff000090727fb8;
      }
      lVar8 = local_58 + 0x18;
      iVar1 = FUN_ffff000090785d18
                        (s_gpueb_ffff0000907eaa9f,s_tinysys_gpueb_RV33_A_ffff0000907fd018,lVar8,
                         0x200000);
      if (iVar1 < 0) {
        pcVar6 = s__s__GPUEB_load_from_partition_fa_ffff0000907f986c;
LAB_ffff000090727f40:
        FUN_ffff00009077e11c(pcVar6,s__GPUEB__LDR__ffff0000907e0ff2,iVar1);
      }
      else {
        FUN_ffff000090707c70(local_58,0x200000);
        local_60 = (undefined4 *)0x0;
        local_38 = 0;
        iVar1 = FUN_ffff0000907977ac(lVar8,&local_60,&local_38);
        if (iVar1 < 0) {
          pcVar6 = s__s__Parsing_gpueb_autoDMA_fw_fai_ffff0000907d0da4;
          goto LAB_ffff000090727f40;
        }
        lVar8 = 0;
        _DAT_ffff00004b140100 = 0;
        do {
          *(undefined8 *)(&DAT_ffff00004b000000 + lVar8) = 0;
          lVar8 = lVar8 + 8;
        } while (lVar8 != 0xa0000);
        if (0 < local_38) {
          uVar10 = local_38 + 4;
          puVar7 = (undefined4 *)&DAT_ffff00004b000000;
          puVar9 = local_60;
          do {
            uVar10 = uVar10 - 4;
            *puVar7 = *puVar9;
            puVar7 = puVar7 + 1;
            puVar9 = puVar9 + 1;
          } while (4 < uVar10);
        }
        FUN_ffff000090797ad0();
        _DAT_ffff00004b09fd00 = (undefined4)lVar3;
        _DAT_ffff00004b09fd04 = 0;
        _DAT_ffff00004b09fd5c = 0;
        _DAT_ffff00004b09fd18 = (undefined4)(uVar11 >> 0xc);
        local_34 = 0;
        iVar2 = FUN_ffff00009076da24(param_1,s__soc_gpueb_ffff0000907e7852);
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s__fail_to_find_gpueb_fdt_path_ffff0000907f45d5,
                     s__GPUEB__LDR__ffff0000907e0ff2);
        }
        else {
          puVar5 = (uint *)FUN_ffff00009076df9c
                                     (param_1,iVar2,s_gpueb_diagnosis_mode_ffff0000907d428b,
                                      &local_34);
          if ((puVar5 == (uint *)0x0) || (local_34 == 0)) {
            FUN_ffff00009077e11c
                      (s__s__fail_to_find_gpueb_diagnosis_ffff0000907e0fff,
                       s__GPUEB__LDR__ffff0000907e0ff2);
            _DAT_ffff00004b09fd48 = 0;
          }
          else {
            uVar10 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
            _DAT_ffff00004b09fd48 = uVar10 >> 0x10 | uVar10 << 0x10;
          }
        }
        _DAT_ffff00004b140100 = 0x9f00000b;
        if (_DAT_ffff00004b09fd08 != 0x55667788) {
          lVar8 = FUN_ffff0000907168b0();
          while (uVar11 = FUN_ffff0000907168b0(), _DAT_ffff00004b09fd08 != 0x55667788) {
            if (lVar8 + 1000000U <= uVar11) {
              FUN_ffff00009077e11c
                        (s__s__GPUEB_boot_up_timed_out____ffff0000908023e0,
                         s__GPUEB__LDR__ffff0000907e0ff2);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_MUX_0x_x____0x_x_ffff0000907d77b9,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b860078,_DAT_ffff00004b860078);
              FUN_ffff00009077e11c
                        (s__s__MFG0_PWR_CON_0x_x____0x_x_ffff0000907da955,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b800504,_DAT_ffff00004b800504);
              FUN_ffff00009077e11c
                        (s__s__SPM_MFG0_PWR_CON_0x_x____0x__ffff0000907f10f9,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x1c004ea8,_DAT_ffff00001c004ea8);
              FUN_ffff00009077e11c
                        (s__s__MFG_RPC_GPUEB_CFG_0x_x____0x_ffff0000907da974,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b860074,_DAT_ffff00004b860074);
              FUN_ffff00009077e11c
                        (s__s__SPM2GPUPM_CON_0x_x____0x_x_ffff0000907d297d,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x1c004410,_DAT_ffff00001c004410);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_CFGREG_SW_RSTN_0x_x____ffff0000907dc5c5,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b140100,_DAT_ffff00004b140100);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_CFGREG_DBG_APB_PC_0x_x_ffff0000907eaaa5,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b14021c,_DAT_ffff00004b14021c);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_CFGREG_DBG_APB_LR_0x_x_ffff0000907fd02d,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b140220,_DAT_ffff00004b140220);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_CFGREG_DBG_APB_SP_0x_x_ffff0000907eaacf,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b140224,_DAT_ffff00004b140224);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_INIT_FOOTPRINT_0x_x____ffff0000907eaaf9,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b09fd08,_DAT_ffff00004b09fd08);
              FUN_ffff00009077e11c
                        (s__s__GPUFREQ_FOOTPRINT_GPR_0x_x____ffff0000907e782a,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b09fd44,_DAT_ffff00004b09fd44);
              FUN_ffff00009077e11c
                        (s__s__GPUPTP3_BAD_SAMPLE_0x_x____0_ffff0000907d4266,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b09fd78,_DAT_ffff00004b09fd78);
              FUN_ffff00009077e11c
                        (s__s__GPUEB_DIAGNOSIS_GPR_0x_x____0_ffff0000907eab20,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b09fd48,_DAT_ffff00004b09fd48);
              _DAT_ffff00004b09fd00 = 0x5a5a5a5a;
              FUN_ffff00009077e11c
                        (s__s__GPUEB_INFO_0x_x____0x_x_afte_ffff0000907f292b,
                         s__GPUEB__LDR__ffff0000907e0ff2,0x4b09fd00,0x5a5a5a5a);
              return -0xd;
            }
          }
        }
      }
      FUN_ffff00009077be30(DAT_ffff000090839928,local_58);
LAB_ffff000090727fb8:
      FUN_ffff00009071a7f0(lVar3);
      return iVar1;
    }
    pcVar6 = s__s__GPUEB_SRAM_is_under_sleep_mo_ffff0000907e4611;
  }
  else {
    pcVar6 = s__s__GPUEB_SRAM_is_power_down_0x__ffff0000907df7c1;
  }
  FUN_ffff00009077e11c(pcVar6,s__GPUEB__LDR__ffff0000907e0ff2);
LAB_ffff000090727d24:
  FUN_ffff00009077e11c
            (s__s__GPUEB_MFG0__NOT_power_on____ffff0000907e5fd2,s__GPUEB__LDR__ffff0000907e0ff2);
  return -0x10;
}


