// ffff0000907840ec  size=1572  FUN_ffff0000907840ec

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff0000907840ec(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined4 uVar4;
  char *pcVar5;
  ulong uVar6;
  int *piVar7;
  undefined8 uVar8;
  undefined *puVar9;
  uint local_18 [2];
  
  if (param_1 == 0) {
    pcVar5 = s__s__d__fdt_is_NULL_ffff0000907f7c01;
    uVar8 = 0xef;
    goto LAB_ffff000090784420;
  }
  iVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff000090802d32;
    uVar8 = 0xf5;
    goto LAB_ffff000090784420;
  }
  iVar3 = FUN_ffff000090783694();
  if (iVar3 == 0xfc11) {
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907e835d);
    iVar2 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_enable_ffff0000907f1d4b,&DAT_ffff0000907e835d,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x104;
LAB_ffff000090784220:
      FUN_ffff00009077de54
                (s__s__d__failed_to_set_aee_enable_n_ffff0000907d025e,
                 s_mrdump_params_fdt_init_ffff0000907d5136,uVar8);
    }
  }
  else {
    if (iVar3 != 0x3121) {
      if (iVar3 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_aee_mrd_ffff0000907e9efe,0x107,&DAT_ffff0000907d24c9);
      }
      iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f0b86);
      iVar2 = FUN_ffff00009076e40c
                        (param_1,iVar2,s_aee_enable_ffff0000907f1d4b,&DAT_ffff0000907f0b86,iVar3 + 1
                        );
      if (iVar2 == 0) {
        return;
      }
      pcVar5 = s__s__d__failed_to_set_aee_enable_n_ffff0000907d025e;
      uVar8 = 0xfc;
      goto LAB_ffff000090784420;
    }
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f53dd);
    iVar2 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_enable_ffff0000907f1d4b,&DAT_ffff0000907f53dd,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x100;
      goto LAB_ffff000090784220;
    }
  }
  iVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff000090802d32;
    uVar8 = 0x10d;
LAB_ffff000090784420:
    FUN_ffff00009077de54(pcVar5,s_mrdump_params_fdt_init_ffff0000907d5136,uVar8);
    return;
  }
  iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907dd30c);
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_aee_load_type_ffff0000907d8549,&DAT_ffff0000907dd30c,iVar3 + 1)
  ;
  if (iVar3 != 0) {
    FUN_ffff00009077de54
              (s__s__d__Error__failed_to_set_aee__ffff0000907fc3b6,
               s_mrdump_params_fdt_init_ffff0000907d5136,0x113);
  }
  iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907df2ba);
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_aee_support_usb_ffff0000907eec0b,&DAT_ffff0000907df2ba,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077de54
              (s__s__d__Error__failed_to_set_aee__ffff0000908015ae,
               s_mrdump_params_fdt_init_ffff0000907d5136,0x11e);
  }
  iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907dd311);
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_aee_encrypt_version_ffff0000907d9d37,&DAT_ffff0000907dd311,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077de54
              (s__s__d__Error__failed_to_set_aee__ffff0000907ed11f,
               s_mrdump_params_fdt_init_ffff0000907d5136,0x125,&DAT_ffff0000907dd311);
  }
  uVar6 = FUN_ffff0000907837d0();
  if ((uVar6 & 1) == 0) {
    puVar9 = &DAT_ffff0000907f0b86;
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f0b86);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907d516e,&DAT_ffff0000907f0b86,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907e9f2a;
      uVar8 = 0x13f;
      goto LAB_ffff000090784478;
    }
  }
  else {
    iVar3 = FUN_ffff00009077ef88(s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e83d4);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_DB_PUBKEY_ffff0000907fe0f9,
                       s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e83d4,iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077de54
                (s__s__d__Error__fdt_setprop_string_ffff0000907e1ecc,
                 s_mrdump_params_fdt_init_ffff0000907d5136,299);
    }
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907e1405);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_DB_PLATFORM_NAME_ffff0000907f1d56,&DAT_ffff0000907e1405,
                       iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077de54
                (s__s__d__Error__fdt_setprop_string_ffff0000907e1ecc,
                 s_mrdump_params_fdt_init_ffff0000907d5136,0x135);
    }
    puVar9 = &DAT_ffff0000907df2ba;
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907df2ba);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907d516e,&DAT_ffff0000907df2ba,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907e1ef5;
      uVar8 = 0x13c;
LAB_ffff000090784478:
      FUN_ffff00009077de54(pcVar5,s_mrdump_params_fdt_init_ffff0000907d5136,uVar8);
    }
  }
  FUN_ffff00009077de54
            (s__s__d__Set_aee_encrypt_enable__s_ffff000090802d4b,
             s_mrdump_params_fdt_init_ffff0000907d5136,0x141,puVar9);
  iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907df2ba);
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_aee_split_mode_ffff0000907f8c8e,&DAT_ffff0000907df2ba,iVar3 + 1
                    );
  if (iVar3 != 0) {
    FUN_ffff00009077de54
              (s__s__d__Error__fdt_setprop_string_ffff0000907e1ecc,
               s_mrdump_params_fdt_init_ffff0000907d5136,0x145);
  }
  uVar4 = FUN_ffff000090781cd4();
  FUN_ffff00009077de54
            (s__s__d__ddr_reserve_status__x___ffff000090802d6d,
             s_mrdump_params_fdt_init_ffff0000907d5136,0x14c,uVar4);
  iVar3 = FUN_ffff000090781cd4();
  if ((iVar3 == 0x9502) && (iVar3 = FUN_ffff000090783694(), iVar3 != 0)) {
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907df2ba);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907eec1b,&DAT_ffff0000907df2ba,
                       iVar3 + 1);
    if (iVar3 == 0) {
      puVar9 = &DAT_ffff0000907df2ba;
      goto LAB_ffff0000907845e0;
    }
    puVar9 = &DAT_ffff0000907df2ba;
LAB_ffff0000907845a4:
    pcVar5 = s__s__d___s__failed_to_set_ddr_rsv_ffff0000907f1d6b;
    uVar8 = 0xbf;
  }
  else {
    iVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f0b86);
    iVar3 = FUN_ffff00009076e40c
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907eec1b,&DAT_ffff0000907f0b86,
                       iVar3 + 1);
    if (iVar3 != 0) {
      puVar9 = &DAT_ffff0000907f0b86;
      goto LAB_ffff0000907845a4;
    }
    puVar9 = &DAT_ffff0000907f0b86;
LAB_ffff0000907845e0:
    pcVar5 = s__s__d___s__set_ddr_rsv____s_ffff0000907eec2a;
    uVar8 = 0xbc;
  }
  FUN_ffff00009077de54
            (pcVar5,s_mrdump_ddr_rsv_fdt_ffff0000907e1f2e,uVar8,
             s_mrdump_ddr_rsv_fdt_ffff0000907e1f2e,puVar9);
  piVar7 = (int *)FUN_ffff00009071a204(s_aee_debug_kinfo_ffff0000907f8bf9,0);
  if (piVar7 != (int *)0x0) {
    uVar1 = (*piVar7 + 0x2000U & 0xff00ff00) >> 8 | (*piVar7 + 0x2000U & 0xff00ff) << 8;
    local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
    iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_mrdump_cblock_ffff0000907dd314,local_18,4);
    if (iVar3 == 0) {
      local_18[1] = 0x800000;
      iVar3 = FUN_ffff00009076e46c(param_1,iVar2,s_mrdump_cblock_ffff0000907dd314,local_18 + 1,4);
      if (iVar3 == 0) goto LAB_ffff000090784670;
    }
    FUN_ffff00009077de54
              (s__s__d__failed_to_set_mrdump_cblo_ffff0000907f35f9,
               s_mrdump_params_fdt_init_ffff0000907d5136,0x159);
  }
LAB_ffff000090784670:
  iVar3 = FUN_ffff00009077ef88(s_MRDUMP11_ffff0000907d6be3);
  iVar2 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_mrdump_lk_ffff0000907db82f,s_MRDUMP11_ffff0000907d6be3,
                     iVar3 + 1);
  if (iVar2 == 0) {
    pcVar5 = s__s__d___s__set_lk_version____s_ffff0000907d37d5;
    uVar8 = 0xe3;
  }
  else {
    pcVar5 = s__s__d___s__failed_to_set_lk_vers_ffff0000907e02df;
    uVar8 = 0xe6;
  }
  FUN_ffff00009077de54
            (pcVar5,s_mrdump_lk_version_fdt_ffff0000907dd322,uVar8,
             s_mrdump_lk_version_fdt_ffff0000907dd322,s_MRDUMP11_ffff0000907d6be3);
  return;
}


