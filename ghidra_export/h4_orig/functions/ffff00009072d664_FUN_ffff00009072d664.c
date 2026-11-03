// ffff00009072d664  size=1004  FUN_ffff00009072d664

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072d664(long param_1)

{
  undefined8 *puVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  int iVar5;
  char *pcVar6;
  undefined8 uVar7;
  ulong uVar8;
  long lVar9;
  ulong local_68;
  
  if (param_1 == 0) {
    pcVar6 = s__CCCI___s__d__fdt_is_invalid_ffff0000907ddeb5;
    uVar7 = 0x40a;
LAB_ffff00009072da2c:
    FUN_ffff000090730228(pcVar6,s_ccci_update_ap_md_smem_info_to_d_ffff00009080094f,uVar7);
    return;
  }
  iVar4 = FUN_ffff00009076da24(param_1,s__mddriver_ffff0000907fefa3);
  if (iVar4 < 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d___mddriver_not_foun_ffff000090802493,
               s_ccci_update_ap_md_smem_info_to_d_ffff00009080094f,0x410);
    iVar4 = FUN_ffff00009076da24(param_1,s__soc_mddriver_ffff0000908024c9);
    if (iVar4 < 0) {
      pcVar6 = s__CCCI___s__d___soc_mddriver_not_f_ffff0000907e2b75;
      uVar7 = 0x413;
      goto LAB_ffff00009072da2c;
    }
  }
  if (_DAT_ffff00009086d710 != 0) {
    lVar9 = 0;
    uVar8 = 0;
    do {
      puVar1 = (undefined8 *)(_DAT_ffff00009086d700 + lVar9);
      if (*(int *)(puVar1 + 2) == 0) {
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_dbm_base___0x_llx_o_ffff0000907d9172,
                   s_ccci_save_nc_smem_to_dt_ffff0000907daaa7,0x3f6,*puVar1,
                   *(undefined4 *)((long)puVar1 + 0x14),*(undefined4 *)(puVar1 + 3));
        uVar3 = (*(ulong *)(_DAT_ffff00009086d700 + lVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(_DAT_ffff00009086d700 + lVar9) & 0xff00ff00ff00ff) << 8;
        uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
        local_68 = uVar3 >> 0x20 | uVar3 << 0x20;
        iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,s_md_dbm_addr_ffff0000907e91c3,&local_68,8);
        if (iVar5 == 0) {
          pcVar6 = s__s__d___CCCI_create_md_dbm_addr_i_ffff0000907f1216;
          uVar7 = 0x3fb;
        }
        else {
          pcVar6 = s__s__d___CCCI_create_md_dbm_addr_i_ffff0000907dded3;
          uVar7 = 0x3f9;
        }
LAB_ffff00009072d720:
        FUN_ffff00009077e11c(pcVar6,s_ccci_save_nc_smem_to_dt_ffff0000907daaa7,uVar7);
      }
      else if (*(int *)(puVar1 + 2) == 0x21) {
        puVar1 = (undefined8 *)(_DAT_ffff00009086d700 + lVar9);
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_low_power_base___0x_ffff0000907e2b9d,
                   s_ccci_save_nc_smem_to_dt_ffff0000907daaa7,0x3ec,*puVar1,
                   *(undefined4 *)((long)puVar1 + 0x14),*(undefined4 *)(puVar1 + 3));
        uVar3 = (*(ulong *)(_DAT_ffff00009086d700 + lVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(_DAT_ffff00009086d700 + lVar9) & 0xff00ff00ff00ff) << 8;
        uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
        local_68 = uVar3 >> 0x20 | uVar3 << 0x20;
        iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,s_md_low_power_addr_ffff0000907d43ad,&local_68,8)
        ;
        if (iVar5 == 0) {
          pcVar6 = s__s__d___CCCI_create_md_low_power_ffff0000907e4792;
          uVar7 = 0x3f1;
        }
        else {
          pcVar6 = s__s__d___CCCI_create_md_low_power_ffff0000907f477f;
          uVar7 = 0x3ef;
        }
        goto LAB_ffff00009072d720;
      }
      uVar8 = uVar8 + 1;
      lVar9 = lVar9 + 0x28;
    } while (uVar8 < _DAT_ffff00009086d710);
  }
  iVar5 = FUN_ffff00009077f250(s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f9acb);
  iVar5 = FUN_ffff00009076e6d4
                    (param_1,iVar4,s_md1_ccb_gear_list_ffff0000907d43bf,
                     s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f9acb,iVar5 + 1);
  if (iVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_create_ccb_argument_ffff0000908024d7,
               s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3c6);
  }
  else {
    FUN_ffff000090730228
              (s__CCCI___s__d__create_ccb_argumen_ffff0000907ef923,
               s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3c4);
  }
  uVar2 = (_DAT_ffff00009086d6f8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d6f8 & 0xff00ff) << 8;
  local_68 = CONCAT44(local_68._4_4_,uVar2 >> 0x10 | uVar2 << 0x10);
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar4,s_md1_ccb_cap_gear_ffff0000907d90ff,&local_68,4);
  if (iVar4 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_create_ccb_gear_inf_ffff0000907f8006,
               s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3cc);
  }
  else {
    FUN_ffff000090730228
              (s__CCCI___s__d__create_ccb_gear_in_ffff0000907ddf01,
               s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3ca);
  }
  iVar4 = FUN_ffff00009076eb40(param_1,0,s_mdproperties_ffff0000907d785e);
  if (iVar4 < 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__fdt_add_subnode_md_ffff0000907d5ffa,
               s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3d0,iVar4);
  }
  else {
    iVar5 = FUN_ffff00009077f250(s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f9acb);
    iVar5 = FUN_ffff00009076e6d4
                      (param_1,iVar4,s_md1_ccb_gear_list_ffff0000907d43bf,
                       s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f9acb,iVar5 + 1);
    if (iVar5 == 0) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI_create_ccb_argument_ffff0000907f8030,
                 s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3d9);
    }
    else {
      FUN_ffff000090730228
                (s__CCCI___s__d__create_ccb_argumen_ffff0000907f1242,
                 s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3d7);
    }
    uVar2 = (_DAT_ffff00009086d6f8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d6f8 & 0xff00ff) << 8;
    local_68 = CONCAT44(local_68._4_4_,uVar2 >> 0x10 | uVar2 << 0x10);
    iVar4 = FUN_ffff00009076e6d4(param_1,iVar4,s_md1_ccb_cap_gear_ffff0000907d90ff,&local_68,4);
    if (iVar4 == 0) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI_create_ccb_gear_inf_ffff0000907e47c3,
                 s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3df);
    }
    else {
      FUN_ffff000090730228
                (s__CCCI___s__d__create_ccb_gear_in_ffff0000907d6032,
                 s_ccci_save_ccb_info_to_dt_ffff000090800a4b,0x3dd);
    }
  }
  return;
}


