// ffff00009072d3cc  size=1004  FUN_ffff00009072d3cc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072d3cc(long param_1)

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
    pcVar6 = s__CCCI___s__d__fdt_is_invalid_ffff0000907dddfb;
    uVar7 = 0x40a;
LAB_ffff00009072d794:
    FUN_ffff00009072ff90(pcVar6,s_ccci_update_ap_md_smem_info_to_d_ffff000090800773,uVar7);
    return;
  }
  iVar4 = FUN_ffff00009076d75c(param_1,s__mddriver_ffff0000907fedfb);
  if (iVar4 < 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d___mddriver_not_foun_ffff00009080228f,
               s_ccci_update_ap_md_smem_info_to_d_ffff000090800773,0x410);
    iVar4 = FUN_ffff00009076d75c(param_1,s__soc_mddriver_ffff0000908022c5);
    if (iVar4 < 0) {
      pcVar6 = s__CCCI___s__d___soc_mddriver_not_f_ffff0000907e2a8d;
      uVar7 = 0x413;
      goto LAB_ffff00009072d794;
    }
  }
  if (_DAT_ffff00009086d678 != 0) {
    lVar9 = 0;
    uVar8 = 0;
    do {
      puVar1 = (undefined8 *)(_DAT_ffff00009086d668 + lVar9);
      if (*(int *)(puVar1 + 2) == 0) {
        FUN_ffff00009077de54
                  (s__s__d___CCCI_dbm_base___0x_llx_o_ffff0000907d90db,
                   s_ccci_save_nc_smem_to_dt_ffff0000907da985,0x3f6,*puVar1,
                   *(undefined4 *)((long)puVar1 + 0x14),*(undefined4 *)(puVar1 + 3));
        uVar3 = (*(ulong *)(_DAT_ffff00009086d668 + lVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(_DAT_ffff00009086d668 + lVar9) & 0xff00ff00ff00ff) << 8;
        uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
        local_68 = uVar3 >> 0x20 | uVar3 << 0x20;
        iVar5 = FUN_ffff00009076e40c(param_1,iVar4,s_md_dbm_addr_ffff0000907e9068,&local_68,8);
        if (iVar5 == 0) {
          pcVar6 = s__s__d___CCCI_create_md_dbm_addr_i_ffff0000907f108e;
          uVar7 = 0x3fb;
        }
        else {
          pcVar6 = s__s__d___CCCI_create_md_dbm_addr_i_ffff0000907dde19;
          uVar7 = 0x3f9;
        }
LAB_ffff00009072d488:
        FUN_ffff00009077de54(pcVar6,s_ccci_save_nc_smem_to_dt_ffff0000907da985,uVar7);
      }
      else if (*(int *)(puVar1 + 2) == 0x21) {
        puVar1 = (undefined8 *)(_DAT_ffff00009086d668 + lVar9);
        FUN_ffff00009077de54
                  (s__s__d___CCCI_low_power_base___0x_ffff0000907e2ab5,
                   s_ccci_save_nc_smem_to_dt_ffff0000907da985,0x3ec,*puVar1,
                   *(undefined4 *)((long)puVar1 + 0x14),*(undefined4 *)(puVar1 + 3));
        uVar3 = (*(ulong *)(_DAT_ffff00009086d668 + lVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(_DAT_ffff00009086d668 + lVar9) & 0xff00ff00ff00ff) << 8;
        uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
        local_68 = uVar3 >> 0x20 | uVar3 << 0x20;
        iVar5 = FUN_ffff00009076e40c(param_1,iVar4,s_md_low_power_addr_ffff0000907d4370,&local_68,8)
        ;
        if (iVar5 == 0) {
          pcVar6 = s__s__d___CCCI_create_md_low_power_ffff0000907e4677;
          uVar7 = 0x3f1;
        }
        else {
          pcVar6 = s__s__d___CCCI_create_md_low_power_ffff0000907f45fb;
          uVar7 = 0x3ef;
        }
        goto LAB_ffff00009072d488;
      }
      uVar8 = uVar8 + 1;
      lVar9 = lVar9 + 0x28;
    } while (uVar8 < _DAT_ffff00009086d678);
  }
  iVar5 = FUN_ffff00009077ef88(s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f992f);
  iVar5 = FUN_ffff00009076e40c
                    (param_1,iVar4,s_md1_ccb_gear_list_ffff0000907d4382,
                     s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f992f,iVar5 + 1);
  if (iVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_create_ccb_argument_ffff0000908022d3,
               s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3c6);
  }
  else {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__create_ccb_argumen_ffff0000907ef79b,
               s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3c4);
  }
  uVar2 = (_DAT_ffff00009086d660 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d660 & 0xff00ff) << 8;
  local_68 = CONCAT44(local_68._4_4_,uVar2 >> 0x10 | uVar2 << 0x10);
  iVar4 = FUN_ffff00009076e40c(param_1,iVar4,s_md1_ccb_cap_gear_ffff0000907d9068,&local_68,4);
  if (iVar4 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_create_ccb_gear_inf_ffff0000907f7e6a,
               s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3cc);
  }
  else {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__create_ccb_gear_in_ffff0000907dde47,
               s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3ca);
  }
  iVar4 = FUN_ffff00009076e878(param_1,0,s_mdproperties_ffff0000907d77c8);
  if (iVar4 < 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__fdt_add_subnode_md_ffff0000907d5f64,
               s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3d0,iVar4);
  }
  else {
    iVar5 = FUN_ffff00009077ef88(s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f992f);
    iVar5 = FUN_ffff00009076e40c
                      (param_1,iVar4,s_md1_ccb_gear_list_ffff0000907d4382,
                       s_1_2_20__2_2_10__3_0_0__4_2_30__1_ffff0000907f992f,iVar5 + 1);
    if (iVar5 == 0) {
      FUN_ffff00009077de54
                (s__s__d___CCCI_create_ccb_argument_ffff0000907f7e94,
                 s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3d9);
    }
    else {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__create_ccb_argumen_ffff0000907f10ba,
                 s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3d7);
    }
    uVar2 = (_DAT_ffff00009086d660 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d660 & 0xff00ff) << 8;
    local_68 = CONCAT44(local_68._4_4_,uVar2 >> 0x10 | uVar2 << 0x10);
    iVar4 = FUN_ffff00009076e40c(param_1,iVar4,s_md1_ccb_cap_gear_ffff0000907d9068,&local_68,4);
    if (iVar4 == 0) {
      FUN_ffff00009077de54
                (s__s__d___CCCI_create_ccb_gear_inf_ffff0000907e46a8,
                 s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3df);
    }
    else {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__create_ccb_gear_in_ffff0000907d5f9c,
                 s_ccci_save_ccb_info_to_dt_ffff00009080086f,0x3dd);
    }
  }
  return;
}


