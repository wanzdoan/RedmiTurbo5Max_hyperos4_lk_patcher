// ffff00009071e660  size=776  FUN_ffff00009071e660

int FUN_ffff00009071e660(long param_1)

{
  ulong uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  char *pcVar5;
  long lVar6;
  char *pcVar7;
  ulong *puVar8;
  undefined8 local_50;
  long lStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  int local_14;
  
  iVar2 = FUN_ffff00009076da24(param_1,s__trusty_ffff0000907e9053);
  if ((-1 < iVar2) &&
     (iVar2 = FUN_ffff00009076e32c(param_1,iVar2,s_mediatek_trusty_sapu_ffff0000907d4121),
     -1 < iVar2)) {
    local_50 = (ulong)local_50._4_4_ << 0x20;
    lVar3 = FUN_ffff00009076ddb4(param_1,iVar2,s_status_ffff0000907edcdb,&local_50);
    if (lVar3 != 0) {
      uVar4 = FUN_ffff00009077f250(&DAT_ffff0000907df649);
      iVar2 = FUN_ffff00009077f348(lVar3 + 0xc,&DAT_ffff0000907df649,uVar4);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff00009076da24(param_1,s__trusty_ffff0000907e9053);
        if (-1 < iVar2) {
          iVar2 = FUN_ffff00009076e32c(param_1,iVar2,s_mediatek_trusty_sapu_ffff0000907d4121);
          if (iVar2 < 0) {
            pcVar7 = s__s_trusty_sapu_not_found_ffff0000907dc463;
          }
          else {
            local_14 = 0;
            lVar3 = FUN_ffff00009076ddb4(param_1,iVar2,s_datamem_size_ffff0000907da7e2,&local_14);
            if (lVar3 == 0) {
              pcVar7 = s__s_prop_datamem_size_not_found_ffff0000907d28ac;
            }
            else {
              if (local_14 == 0x10) {
                puVar8 = (ulong *)(lVar3 + 0x14);
              }
              else {
                if (local_14 != 8) {
                  return -1;
                }
                puVar8 = (ulong *)(lVar3 + 0xc);
              }
              local_50 = *puVar8;
              uVar1 = (local_50 & 0xff00ff00ff00ff00) >> 8 | (local_50 & 0xff00ff00ff00ff) << 8;
              uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
              pcVar7 = (char *)(uVar1 >> 0x20 | uVar1 << 0x20);
              if ((char *)0x400000000 < pcVar7) {
                pcVar5 = s__s_datamem_size_0x_llx_is_out_of_ffff0000907d0cbf;
LAB_ffff00009071e7cc:
                FUN_ffff00009077e11c(pcVar5,s_set_sapu_datamem_size_ffff0000907da7cc,pcVar7);
                return -1;
              }
              iVar2 = FUN_ffff00009076da24(param_1,s__memory_ssmr_features_ffff0000907f96b8);
              if (iVar2 < 0) {
                pcVar7 = s__s_ssmr_not_found_ffff0000907e5e4e;
              }
              else {
                lVar3 = FUN_ffff00009076ddb4
                                  (param_1,iVar2,s_sapu_data_shm_size_ffff0000907f0fde,&local_14);
                if (lVar3 == 0) {
                  pcVar5 = s__s__s_not_found_ffff0000907e76b5;
                  pcVar7 = s_sapu_data_shm_size_ffff0000907f0fde;
                  goto LAB_ffff00009071e7cc;
                }
                iVar2 = FUN_ffff00009076e6d4
                                  (param_1,iVar2,s_sapu_data_shm_size_ffff0000907f0fde,&local_50,8);
                if (-1 < iVar2) {
                  if (param_1 == 0) {
                    FUN_ffff00009077e11c
                              (s__s_fdt_not_found_ffff0000907d0ce7,
                               s_smmu_v3_enabled_ffff0000907f7e8c);
                    lVar3 = 0x100000;
                  }
                  else {
                    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_arm_smmu_v3_ffff0000907d76cc);
                    lVar3 = 0x200000;
                    if (iVar2 < 0) {
                      lVar3 = 0x100000;
                    }
                  }
                  iVar2 = FUN_ffff00009071c708();
                  if (iVar2 == 0) {
                    lVar6 = FUN_ffff0000907198dc
                                      (lVar3,0x200000,0x90000000,0,0,
                                       s_apu_sapu_apurvgzctrlshm_ffff0000907e44bd);
                  }
                  else {
                    lVar6 = FUN_ffff00009071a09c
                                      (lVar3,0x200000,0x240000000,0x340000000,0,0,
                                       s_apu_sapu_apurvgzctrlshm_ffff0000907e44bd);
                  }
                  if (lVar6 == 0) {
                    FUN_ffff00009077e11c
                              (s__s_mblock_allocation_failed_ffff0000907f4461,
                               s_apu_sapu_apurvgzctrlshm_ffff0000907e44bd);
                  }
                  else {
                    FUN_ffff00009077e11c
                              (s__s_mblock_pa___llx__sz__llx__ffff0000907dc445,
                               s_apu_sapu_apurvgzctrlshm_ffff0000907e44bd,lVar6,lVar3);
                  }
                  lStack_48 = lVar6 + lVar3;
                  uStack_38 = 0;
                  local_40 = 0x18;
                  local_50 = lVar6;
                  iVar2 = FUN_ffff00009078b0fc(&local_50);
                  if (iVar2 != 0) {
                    FUN_ffff00009071a7f0(lVar6);
                    return iVar2;
                  }
                  return 0;
                }
                pcVar7 = s__s_set_dts_sapu_data_size_failed_ffff0000907d5c4b;
              }
            }
          }
          FUN_ffff00009077e11c(pcVar7,s_set_sapu_datamem_size_ffff0000907da7cc);
        }
        return -1;
      }
    }
  }
  return 0;
}


