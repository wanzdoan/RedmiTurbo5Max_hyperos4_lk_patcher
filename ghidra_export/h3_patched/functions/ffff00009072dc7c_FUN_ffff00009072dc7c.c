// ffff00009072dc7c  size=936  FUN_ffff00009072dc7c

void FUN_ffff00009072dc7c(long param_1)

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
    pcVar12 = s__s__d___CCCI__ccci__fdt___NULL_ffff0000907d5fd8;
    uVar13 = 0xaa;
LAB_ffff00009072e000:
    FUN_ffff00009077de54(pcVar12,s_ccci_get_boot_args_from_dt_ffff000090802331,uVar13);
    return;
  }
  iVar8 = FUN_ffff00009076d75c(param_1,s__mddriver_ffff0000907fedfb);
  if (iVar8 < 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__ccci__mddriver_not_ffff0000907e2af3,
               s_ccci_get_boot_args_from_dt_ffff000090802331,0xaf);
    iVar8 = FUN_ffff00009076d75c(param_1,s__soc_mddriver_ffff0000908022c5);
    if (iVar8 < 0) {
      pcVar12 = s__s__d___CCCI__ccci___soc_mddrive_ffff00009080234c;
      uVar13 = 0xb2;
      goto LAB_ffff00009072e000;
    }
  }
  local_64 = 0;
  iVar8 = FUN_ffff00009076d840(param_1,iVar8);
  if (-1 < iVar8) {
    iVar14 = 0;
    do {
      local_64 = 0;
      uVar10 = FUN_ffff00009076dbe8(param_1,iVar8,&local_70,&local_64);
      uVar13 = local_70;
      iVar14 = iVar14 + 1;
      if (uVar10 == 0) {
        FUN_ffff00009077de54
                  (s__s__d___CCCI__ccci___s_line__d__r_ffff0000907d4394,
                   s_ccci_get_boot_args_from_dt_ffff000090802331,0xc2,
                   s_ccci_get_boot_args_from_dt_ffff000090802331,0xc2,iVar14);
      }
      else {
        uVar11 = FUN_ffff00009077ef88(s_ref_args_ffff0000907f7ee5);
        iVar9 = FUN_ffff00009077f080(uVar13,s_ref_args_ffff0000907f7ee5,uVar11);
        if (iVar9 == 0) {
          if ((local_64 != 0) && (0 < local_64)) {
            uVar2 = uVar10 + (long)local_64;
            do {
              iVar9 = FUN_ffff00009077f16c(uVar10,uVar2 - uVar10);
              uVar1 = uVar10 + (long)(iVar9 + 1);
              if (uVar2 < uVar1) break;
              iVar9 = FUN_ffff00009077ede4(uVar10,s_md_disable_ffff0000907d43c9);
              if (iVar9 == 0) {
                DAT_ffff00009086d718 = 1;
              }
              uVar10 = uVar1;
            } while (uVar1 < uVar2);
          }
        }
      }
      iVar8 = FUN_ffff00009076d8c4(param_1,iVar8);
    } while (-1 < iVar8);
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI__s__________________ffff0000907dde73,
             s_dump_args_setting_ffff0000907f462e,0x6d,s_dump_args_setting_ffff0000907f462e);
  FUN_ffff00009077de54
            (s__s__d___CCCI___s____d__ffff0000907eaa34,s_dump_args_setting_ffff0000907f462e,0x6f,
             s_md_disable_ffff0000907d43c9,DAT_ffff00009086d718);
  FUN_ffff00009077de54
            (s__s__d___CCCI__s__________________ffff0000907e9074,
             s_dump_args_setting_ffff0000907f462e,0x70,s_dump_args_setting_ffff0000907f462e);
  iVar8 = FUN_ffff00009071c57c();
  FUN_ffff00009077de54
            (s__s__d___CCCI__s__________________ffff0000907dde73,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x97,
             s_dump_limit_align_map_setting_ffff0000907f10fb);
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
  FUN_ffff00009077de54
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d43d4,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9b,s_ap_md_c_smem_ffff0000907d90ce,
             uVar13,uVar11,0x2000000,0);
  FUN_ffff00009077de54
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d43d4,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9b,s_ap_md_nc_smem_ffff0000907e29b0,
             uVar13,uVar3,0x2000000,0);
  FUN_ffff00009077de54
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d43d4,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9b,s_md_mem_usage_ffff0000907d0f58,
             uVar6,uVar4,0x2000000,0);
  FUN_ffff00009077de54
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d43d4,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9b,s_ccci_tag_mem_ffff0000907f6106,
             uVar6,uVar5,0x10000,0);
  FUN_ffff00009077de54
            (s__s__d___CCCI___s___llx__llx__lx__ffff0000907d43d4,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9b,s_md1_sib_mem_ffff0000907ec1fa,
             uVar6,uVar4,0x8000000,0);
  FUN_ffff00009077de54
            (s__s__d___CCCI__s__________________ffff0000907e9074,
             s_dump_limit_align_map_setting_ffff0000907f10fb,0x9c,
             s_dump_limit_align_map_setting_ffff0000907f10fb);
  return;
}


