// ffff0000907843b4  size=1572  FUN_ffff0000907843b4

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff0000907843b4(long param_1)

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
    pcVar5 = s__s__d__fdt_is_NULL_ffff0000907f7d9d;
    uVar8 = 0xef;
    goto LAB_ffff0000907846e8;
  }
  iVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff000090802f36;
    uVar8 = 0xf5;
    goto LAB_ffff0000907846e8;
  }
  iVar3 = FUN_ffff00009078395c();
  if (iVar3 == 0xfc11) {
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907e84a1);
    iVar2 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_enable_ffff0000907f1ed7,&DAT_ffff0000907e84a1,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x104;
LAB_ffff0000907844e8:
      FUN_ffff00009077e11c
                (s__s__d__failed_to_set_aee_enable_n_ffff0000907d027d,
                 s_mrdump_params_fdt_init_ffff0000907d5173,uVar8);
    }
  }
  else {
    if (iVar3 != 0x3121) {
      if (iVar3 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_aee_mrd_ffff0000907ea042,0x107,&DAT_ffff0000907d2506);
      }
      iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f0cf7);
      iVar2 = FUN_ffff00009076e6d4
                        (param_1,iVar2,s_aee_enable_ffff0000907f1ed7,&DAT_ffff0000907f0cf7,iVar3 + 1
                        );
      if (iVar2 == 0) {
        return;
      }
      pcVar5 = s__s__d__failed_to_set_aee_enable_n_ffff0000907d027d;
      uVar8 = 0xfc;
      goto LAB_ffff0000907846e8;
    }
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f554a);
    iVar2 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_enable_ffff0000907f1ed7,&DAT_ffff0000907f554a,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x100;
      goto LAB_ffff0000907844e8;
    }
  }
  iVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff000090802f36;
    uVar8 = 0x10d;
LAB_ffff0000907846e8:
    FUN_ffff00009077e11c(pcVar5,s_mrdump_params_fdt_init_ffff0000907d5173,uVar8);
    return;
  }
  iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907dd3e2);
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_aee_load_type_ffff0000907d85df,&DAT_ffff0000907dd3e2,iVar3 + 1)
  ;
  if (iVar3 != 0) {
    FUN_ffff00009077e11c
              (s__s__d__Error__failed_to_set_aee__ffff0000907fc552,
               s_mrdump_params_fdt_init_ffff0000907d5173,0x113);
  }
  iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907df35d);
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_aee_support_usb_ffff0000907eed7c,&DAT_ffff0000907df35d,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077e11c
              (s__s__d__Error__failed_to_set_aee__ffff00009080178a,
               s_mrdump_params_fdt_init_ffff0000907d5173,0x11e);
  }
  iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907dd3e7);
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_aee_encrypt_version_ffff0000907d9dce,&DAT_ffff0000907dd3e7,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077e11c
              (s__s__d__Error__failed_to_set_aee__ffff0000907ed2a2,
               s_mrdump_params_fdt_init_ffff0000907d5173,0x125,&DAT_ffff0000907dd3e7);
  }
  uVar6 = FUN_ffff000090783a98();
  if ((uVar6 & 1) == 0) {
    puVar9 = &DAT_ffff0000907f0cf7;
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f0cf7);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907d51ab,&DAT_ffff0000907f0cf7,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907ea06e;
      uVar8 = 0x13f;
      goto LAB_ffff000090784740;
    }
  }
  else {
    iVar3 = FUN_ffff00009077f250(s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e8518);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_DB_PUBKEY_ffff0000907fe2a1,
                       s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e8518,iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077e11c
                (s__s__d__Error__fdt_setprop_string_ffff0000907e1f9d,
                 s_mrdump_params_fdt_init_ffff0000907d5173,299);
    }
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907e14d6);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_DB_PLATFORM_NAME_ffff0000907f1ee2,&DAT_ffff0000907e14d6,
                       iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077e11c
                (s__s__d__Error__fdt_setprop_string_ffff0000907e1f9d,
                 s_mrdump_params_fdt_init_ffff0000907d5173,0x135);
    }
    puVar9 = &DAT_ffff0000907df35d;
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907df35d);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907d51ab,&DAT_ffff0000907df35d,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907e1fc6;
      uVar8 = 0x13c;
LAB_ffff000090784740:
      FUN_ffff00009077e11c(pcVar5,s_mrdump_params_fdt_init_ffff0000907d5173,uVar8);
    }
  }
  FUN_ffff00009077e11c
            (s__s__d__Set_aee_encrypt_enable__s_ffff000090802f4f,
             s_mrdump_params_fdt_init_ffff0000907d5173,0x141,puVar9);
  iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907df35d);
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_aee_split_mode_ffff0000907f8e2a,&DAT_ffff0000907df35d,iVar3 + 1
                    );
  if (iVar3 != 0) {
    FUN_ffff00009077e11c
              (s__s__d__Error__fdt_setprop_string_ffff0000907e1f9d,
               s_mrdump_params_fdt_init_ffff0000907d5173,0x145);
  }
  uVar4 = FUN_ffff000090781f9c();
  FUN_ffff00009077e11c
            (s__s__d__ddr_reserve_status__x___ffff000090802f71,
             s_mrdump_params_fdt_init_ffff0000907d5173,0x14c,uVar4);
  iVar3 = FUN_ffff000090781f9c();
  if ((iVar3 == 0x9502) && (iVar3 = FUN_ffff00009078395c(), iVar3 != 0)) {
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907df35d);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907eed8c,&DAT_ffff0000907df35d,
                       iVar3 + 1);
    if (iVar3 == 0) {
      puVar9 = &DAT_ffff0000907df35d;
      goto LAB_ffff0000907848a8;
    }
    puVar9 = &DAT_ffff0000907df35d;
LAB_ffff00009078486c:
    pcVar5 = s__s__d___s__failed_to_set_ddr_rsv_ffff0000907f1ef7;
    uVar8 = 0xbf;
  }
  else {
    iVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f0cf7);
    iVar3 = FUN_ffff00009076e6d4
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907eed8c,&DAT_ffff0000907f0cf7,
                       iVar3 + 1);
    if (iVar3 != 0) {
      puVar9 = &DAT_ffff0000907f0cf7;
      goto LAB_ffff00009078486c;
    }
    puVar9 = &DAT_ffff0000907f0cf7;
LAB_ffff0000907848a8:
    pcVar5 = s__s__d___s__set_ddr_rsv____s_ffff0000907eed9b;
    uVar8 = 0xbc;
  }
  FUN_ffff00009077e11c
            (pcVar5,s_mrdump_ddr_rsv_fdt_ffff0000907e1fff,uVar8,
             s_mrdump_ddr_rsv_fdt_ffff0000907e1fff,puVar9);
  piVar7 = (int *)FUN_ffff00009071a348(s_aee_debug_kinfo_ffff0000907f8d95,0);
  if (piVar7 != (int *)0x0) {
    uVar1 = (*piVar7 + 0x2000U & 0xff00ff00) >> 8 | (*piVar7 + 0x2000U & 0xff00ff) << 8;
    local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_mrdump_cblock_ffff0000907dd3ea,local_18,4);
    if (iVar3 == 0) {
      local_18[1] = 0x800000;
      iVar3 = FUN_ffff00009076e734(param_1,iVar2,s_mrdump_cblock_ffff0000907dd3ea,local_18 + 1,4);
      if (iVar3 == 0) goto LAB_ffff000090784938;
    }
    FUN_ffff00009077e11c
              (s__s__d__failed_to_set_mrdump_cblo_ffff0000907f3785,
               s_mrdump_params_fdt_init_ffff0000907d5173,0x159);
  }
LAB_ffff000090784938:
  iVar3 = FUN_ffff00009077f250(s_MRDUMP11_ffff0000907d6c79);
  iVar2 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_mrdump_lk_ffff0000907db93a,s_MRDUMP11_ffff0000907d6c79,
                     iVar3 + 1);
  if (iVar2 == 0) {
    pcVar5 = s__s__d___s__set_lk_version____s_ffff0000907d3812;
    uVar8 = 0xe3;
  }
  else {
    pcVar5 = s__s__d___s__failed_to_set_lk_vers_ffff0000907e0382;
    uVar8 = 0xe6;
  }
  FUN_ffff00009077e11c
            (pcVar5,s_mrdump_lk_version_fdt_ffff0000907dd3f8,uVar8,
             s_mrdump_lk_version_fdt_ffff0000907dd3f8,s_MRDUMP11_ffff0000907d6c79);
  return;
}


