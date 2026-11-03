// ffff00009072df14  size=936  FUN_ffff00009072df14

void FUN_ffff00009072df14(long param_1)

{
  ulong uVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  bool bVar7;
  int iVar8;
  int iVar9;
  ulong uVar10;
  undefined8 uVar11;
  char *pcVar12;
  undefined8 uVar13;
  int iVar14;
  undefined8 local_70;
  int local_64;
  
  local_70 = 0;
  if (param_1 == 0) {
    pcVar12 = s__s__d___CCCI__ccci__fdt___NULL_ffff0000907d606e;
    uVar13 = 0xaa;
LAB_ffff00009072e298:
    FUN_ffff00009077e11c(pcVar12,s_ccci_get_boot_args_from_dt_ffff000090802535,uVar13);
    return;
  }
  iVar8 = FUN_ffff00009076da24(param_1,s__mddriver_ffff0000907fefa3);
  if (iVar8 < 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__ccci__mddriver_not_ffff0000907e2bdb,
               s_ccci_get_boot_args_from_dt_ffff000090802535,0xaf);
    iVar8 = FUN_ffff00009076da24(param_1,s__soc_mddriver_ffff0000908024c9);
    if (iVar8 < 0) {
      pcVar12 = s__s__d___CCCI__ccci___soc_mddrive_ffff000090802550;
      uVar13 = 0xb2;
      goto LAB_ffff00009072e298;
    }
  }
  local_64 = 0;
  iVar8 = FUN_ffff00009076db08(param_1,iVar8);
  if (-1 < iVar8) {
    iVar14 = 0;
    do {
      local_64 = 0;
      uVar10 = FUN_ffff00009076deb0(param_1,iVar8,&local_70,&local_64);
      uVar13 = local_70;
      iVar14 = iVar14 + 1;
      if (uVar10 == 0) {
        FUN_ffff00009077e11c
                  (s__s__d___CCCI__ccci___s_line__d__r_ffff0000907d43d1,
                   s_ccci_get_boot_args_from_dt_ffff000090802535,0xc2,
                   s_ccci_get_boot_args_from_dt_ffff000090802535,0xc2,iVar14);
      }
      else {
        uVar11 = FUN_ffff00009077f250(s_ref_args_ffff0000907f8081);
        iVar9 = FUN_ffff00009077f348(uVar13,s_ref_args_ffff0000907f8081,uVar11);
        if (iVar9 == 0) {
          if ((local_64 != 0) && (0 < local_64)) {
            uVar2 = uVar10 + (long)local_64;
            do {
              iVar9 = FUN_ffff00009077f434(uVar10,uVar2 - uVar10);
              uVar1 = uVar10 + (long)(iVar9 + 1);
              if (uVar2 < uVar1) break;
              iVar9 = FUN_ffff00009077f0ac(uVar10,s_md_disable_ffff0000907d4406);
              if (iVar9 == 0) {
                DAT_ffff00009086d7b0 = 1;
              }
              uVar10 = uVar1;
            } while (uVar1 < uVar2);
          }
        }
      }
      iVar8 = FUN_ffff00009076db8c(param_1,iVar8);
    } while (-1 < iVar8);
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI__s__________________ffff0000907ddf2d,
             s_dump_args_setting_ffff0000907f47b2,0x6d,s_dump_args_setting_ffff0000907f47b2);
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s____d__ffff0000907eabce,s_dump_args_setting_ffff0000907f47b2,0x6f,
             s_md_disable_ffff0000907d4406,DAT_ffff00009086d7b0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__s__________________ffff0000907e91cf,
             s_dump_args_setting_ffff0000907f47b2,0x70,s_dump_args_setting_ffff0000907f47b2);
  iVar8 = FUN_ffff00009071c708();
  FUN_ffff00009077e11c
            (s__s__d___CCCI__s__________________ffff0000907ddf2d,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x97,
             s_dump_limit_align_map_setting_ffff0000907f1283);
  bVar7 = iVar8 != 0;
  uVar13 = 0x50000000;
  if (bVar7) {
    uVar13 = 0x250000000;
  }
  uVar11 = 0x100000000;
  if (bVar7) {
    uVar11 = 0x2a0000000;
  }
  uVar3 = 0xf0000000;
  if (bVar7) {
    uVar3 = 0x2a0000000;
  }
  uVar4 = 0x200000000;
  if (bVar7) {
    uVar4 = 0x340000000;
  }
  uVar5 = 0xc0000000;
  if (bVar7) {
    uVar5 = 0x340000000;
  }
  uVar6 = 0;
  if (bVar7) {
    uVar6 = 0x240000000;
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d4411,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9b,s_ap_md_c_smem_ffff0000907d9165,
             uVar13,uVar11,0x2000000,0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d4411,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9b,s_ap_md_nc_smem_ffff0000907e2a98,
             uVar13,uVar3,0x2000000,0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d4411,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9b,s_md_mem_usage_ffff0000907d0f95,
             uVar6,uVar4,0x2000000,0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d4411,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9b,s_ccci_tag_mem_ffff0000907f62a7,
             uVar6,uVar5,0x10000,0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d4411,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9b,s_md1_sib_mem_ffff0000907ec394,
             uVar6,uVar4,0x8000000,0);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__s__________________ffff0000907e91cf,
             s_dump_limit_align_map_setting_ffff0000907f1283,0x9c,
             s_dump_limit_align_map_setting_ffff0000907f1283);
  return;
}


