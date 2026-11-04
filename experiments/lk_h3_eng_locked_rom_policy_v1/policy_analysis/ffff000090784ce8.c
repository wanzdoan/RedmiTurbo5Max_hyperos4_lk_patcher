
/* WARNING: Type propagation algorithm not settling */

void FUN_ffff000090784ce8(long param_1)

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
    pcVar5 = s__s__d__fdt_is_NULL_ffff0000907f09f2;
    uVar8 = 0xef;
    goto LAB_ffff00009078501c;
  }
  iVar2 = FUN_ffff00009076e2a4(param_1,s__chosen_ffff0000907e307d);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff0000907fbf3b;
    uVar8 = 0xf5;
    goto LAB_ffff00009078501c;
  }
  iVar3 = FUN_ffff00009078427c();
  if (iVar3 == 0xfc11) {
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907e0d1a);
    iVar2 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_enable_ffff0000907eaadb,&DAT_ffff0000907e0d1a,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x104;
LAB_ffff000090784e1c:
      FUN_ffff00009077ea84
                (s__s__d__failed_to_set_aee_enable_n_ffff0000907c8345,
                 s_mrdump_params_fdt_init_ffff0000907cd2cf,uVar8);
    }
  }
  else {
    if (iVar3 != 0x3121) {
      if (iVar3 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077d934
                  (s_platform_mediatek_common_aee_mrd_ffff0000907e2989,0x107,&DAT_ffff0000907ca5f8);
      }
      iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907e9853);
      iVar2 = FUN_ffff00009076ef54
                        (param_1,iVar2,s_aee_enable_ffff0000907eaadb,&DAT_ffff0000907e9853,iVar3 + 1
                        );
      if (iVar2 == 0) {
        return;
      }
      pcVar5 = s__s__d__failed_to_set_aee_enable_n_ffff0000907c8345;
      uVar8 = 0xfc;
      goto LAB_ffff00009078501c;
    }
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907ee23d);
    iVar2 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_enable_ffff0000907eaadb,&DAT_ffff0000907ee23d,iVar3 + 1);
    if (iVar2 != 0) {
      uVar8 = 0x100;
      goto LAB_ffff000090784e1c;
    }
  }
  iVar2 = FUN_ffff00009076e2a4(param_1,s__chosen_ffff0000907e307d);
  if (iVar2 < 0) {
    pcVar5 = s__s__d__chosen_not_found_ffff0000907fbf3b;
    uVar8 = 0x10d;
LAB_ffff00009078501c:
    FUN_ffff00009077ea84(pcVar5,s_mrdump_params_fdt_init_ffff0000907cd2cf,uVar8);
    return;
  }
  iVar3 = FUN_ffff00009077fb34(s_userdebug_ffff0000907e5e2c);
  iVar3 = FUN_ffff00009076ef54
                    (param_1,iVar2,s_aee_load_type_ffff0000907d095d,s_userdebug_ffff0000907e5e2c,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077ea84
              (s__s__d__Error__failed_to_set_aee__ffff0000907f8c5a,
               s_mrdump_params_fdt_init_ffff0000907cd2cf,0x116);
  }
  iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d78b3);
  iVar3 = FUN_ffff00009076ef54
                    (param_1,iVar2,s_aee_support_usb_ffff0000907e78f0,&DAT_ffff0000907d78b3,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077ea84
              (s__s__d__Error__failed_to_set_aee__ffff0000907fa694,
               s_mrdump_params_fdt_init_ffff0000907cd2cf,0x11e);
  }
  iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d580b);
  iVar3 = FUN_ffff00009076ef54
                    (param_1,iVar2,s_aee_encrypt_version_ffff0000907d21e6,&DAT_ffff0000907d580b,
                     iVar3 + 1);
  if (iVar3 != 0) {
    FUN_ffff00009077ea84
              (s__s__d__Error__failed_to_set_aee__ffff0000907e5e36,
               s_mrdump_params_fdt_init_ffff0000907cd2cf,0x125,&DAT_ffff0000907d580b);
  }
  uVar6 = FUN_ffff0000907843b8();
  if ((uVar6 & 1) == 0) {
    puVar9 = &DAT_ffff0000907e9853;
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907e9853);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907cd307,&DAT_ffff0000907e9853,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907e29b5;
      uVar8 = 0x13f;
      goto LAB_ffff000090785074;
    }
  }
  else {
    iVar3 = FUN_ffff00009077fb34(s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e0d91);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_DB_PUBKEY_ffff0000907f7090,
                       s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000907e0d91,iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077ea84
                (s__s__d__Error__fdt_setprop_string_ffff0000907da683,
                 s_mrdump_params_fdt_init_ffff0000907cd2cf,299);
    }
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d9acd);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_DB_PLATFORM_NAME_ffff0000907eaae6,&DAT_ffff0000907d9acd,
                       iVar3 + 1);
    if (iVar3 != 0) {
      FUN_ffff00009077ea84
                (s__s__d__Error__fdt_setprop_string_ffff0000907da683,
                 s_mrdump_params_fdt_init_ffff0000907cd2cf,0x135);
    }
    puVar9 = &DAT_ffff0000907d78b3;
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d78b3);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_aee_encrypt_enable_ffff0000907cd307,&DAT_ffff0000907d78b3,
                       iVar3 + 1);
    if (iVar3 != 0) {
      pcVar5 = s__s__d__Error__failed_to_set_aee__ffff0000907da6ac;
      uVar8 = 0x13c;
LAB_ffff000090785074:
      FUN_ffff00009077ea84(pcVar5,s_mrdump_params_fdt_init_ffff0000907cd2cf,uVar8);
    }
  }
  FUN_ffff00009077ea84
            (s__s__d__Set_aee_encrypt_enable__s_ffff0000907fbf54,
             s_mrdump_params_fdt_init_ffff0000907cd2cf,0x141,puVar9);
  iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d78b3);
  iVar3 = FUN_ffff00009076ef54
                    (param_1,iVar2,s_aee_split_mode_ffff0000907f1b29,&DAT_ffff0000907d78b3,iVar3 + 1
                    );
  if (iVar3 != 0) {
    FUN_ffff00009077ea84
              (s__s__d__Error__fdt_setprop_string_ffff0000907da683,
               s_mrdump_params_fdt_init_ffff0000907cd2cf,0x145);
  }
  uVar4 = FUN_ffff0000907828a0();
  FUN_ffff00009077ea84
            (s__s__d__ddr_reserve_status__x___ffff0000907fbf76,
             s_mrdump_params_fdt_init_ffff0000907cd2cf,0x14c,uVar4);
  iVar3 = FUN_ffff0000907828a0();
  if ((iVar3 == 0x9502) && (iVar3 = FUN_ffff00009078427c(), iVar3 != 0)) {
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907d78b3);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907e7900,&DAT_ffff0000907d78b3,
                       iVar3 + 1);
    if (iVar3 == 0) {
      puVar9 = &DAT_ffff0000907d78b3;
      goto LAB_ffff0000907851dc;
    }
    puVar9 = &DAT_ffff0000907d78b3;
LAB_ffff0000907851a0:
    pcVar5 = s__s__d___s__failed_to_set_ddr_rsv_ffff0000907eaafb;
    uVar8 = 0xbf;
  }
  else {
    iVar3 = FUN_ffff00009077fb34(&DAT_ffff0000907e9853);
    iVar3 = FUN_ffff00009076ef54
                      (param_1,iVar2,s_mrdump_ddr_rsv_ffff0000907e7900,&DAT_ffff0000907e9853,
                       iVar3 + 1);
    if (iVar3 != 0) {
      puVar9 = &DAT_ffff0000907e9853;
      goto LAB_ffff0000907851a0;
    }
    puVar9 = &DAT_ffff0000907e9853;
LAB_ffff0000907851dc:
    pcVar5 = s__s__d___s__set_ddr_rsv____s_ffff0000907e790f;
    uVar8 = 0xbc;
  }
  FUN_ffff00009077ea84
            (pcVar5,s_mrdump_ddr_rsv_fdt_ffff0000907da6e5,uVar8,
             s_mrdump_ddr_rsv_fdt_ffff0000907da6e5,puVar9);
  piVar7 = (int *)FUN_ffff000090719d88(s_aee_debug_kinfo_ffff0000907f1a94,0);
  if (piVar7 != (int *)0x0) {
    uVar1 = (*piVar7 + 0x2000U & 0xff00ff00) >> 8 | (*piVar7 + 0x2000U & 0xff00ff) << 8;
    local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
    iVar3 = FUN_ffff00009076ef54(param_1,iVar2,s_mrdump_cblock_ffff0000907d580e,local_18,4);
    if (iVar3 == 0) {
      local_18[1] = 0x800000;
      iVar3 = FUN_ffff00009076efb4(param_1,iVar2,s_mrdump_cblock_ffff0000907d580e,local_18 + 1,4);
      if (iVar3 == 0) goto LAB_ffff00009078526c;
    }
    FUN_ffff00009077ea84
              (s__s__d__failed_to_set_mrdump_cblo_ffff0000907ec432,
               s_mrdump_params_fdt_init_ffff0000907cd2cf,0x159);
  }
LAB_ffff00009078526c:
  iVar3 = FUN_ffff00009077fb34(s_MRDUMP11_ffff0000907cef29);
  iVar2 = FUN_ffff00009076ef54
                    (param_1,iVar2,s_mrdump_lk_ffff0000907d3cf5,s_MRDUMP11_ffff0000907cef29,
                     iVar3 + 1);
  if (iVar2 == 0) {
    pcVar5 = s__s__d___s__set_lk_version____s_ffff0000907cb917;
    uVar8 = 0xe3;
  }
  else {
    pcVar5 = s__s__d___s__failed_to_set_lk_vers_ffff0000907d8951;
    uVar8 = 0xe6;
  }
  FUN_ffff00009077ea84
            (pcVar5,s_mrdump_lk_version_fdt_ffff0000907d581c,uVar8,
             s_mrdump_lk_version_fdt_ffff0000907d581c,s_MRDUMP11_ffff0000907cef29);
  return;
}
