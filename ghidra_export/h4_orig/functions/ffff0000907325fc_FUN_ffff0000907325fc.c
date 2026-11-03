// ffff0000907325fc  size=572  FUN_ffff0000907325fc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907325fc(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  char *pcVar5;
  char local_48 [8];
  char acStack_40 [6];
  char local_3a [2];
  char acStack_38 [8];
  uint local_14;
  
  iVar1 = FUN_ffff00009076da24(param_1,s__mddriver_ffff0000907fefa3);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__ccci__mddriver_not_ffff0000907e2bdb,
               s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,0x13c);
    iVar1 = FUN_ffff00009076da24(param_1,s__soc_mddriver_ffff0000908024c9);
    if (iVar1 < 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d___soc_mddriver_not_f_ffff0000907e2b75,
                 s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,0x13f);
      return;
    }
  }
  _DAT_ffff00009086da80 = FUN_ffff00009077b334(_DAT_ffff00009086da80);
  iVar2 = FUN_ffff00009076e6d4
                    (param_1,iVar1,s_ccci_modem_info_v2_ffff0000907ff0d7,&DAT_ffff00009086da80,0x30)
  ;
  if (iVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_create_modem_argume_ffff0000907ee1c4,
               s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,0x14a);
  }
  else {
    FUN_ffff000090730228
              (s__CCCI___s__d__create_modem_argum_ffff0000907e491c,
               s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,0x148);
  }
  if ((DAT_ffff00009086da98 & 1) == 0) {
    uVar4 = FUN_ffff00009077f250(s_disabled_ffff0000907f0f7e);
    iVar1 = FUN_ffff00009076e6d4
                      (param_1,iVar1,s_status_ffff0000907edcdb,s_disabled_ffff0000907f0f7e,uVar4);
    if (iVar1 != 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d__create_modem_statu_ffff0000907e494f,
                 s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,0x150);
      goto LAB_ffff00009073274c;
    }
    pcVar5 = s__s__d___CCCI_create_modem_status_ffff0000907f9bd2;
    uVar4 = 0x152;
  }
  else {
    pcVar5 = s__s__d___CCCI_md1_enabled_ffff0000907e7a18;
    uVar4 = 0x154;
  }
  FUN_ffff00009077e11c(pcVar5,s_ccci_update_md_arg_info_to_dt_ffff0000907dabb6,uVar4);
LAB_ffff00009073274c:
  local_48[0] = s_mediatek_md_attr_node_ffff0000907d6142[0];
  local_48[1] = s_mediatek_md_attr_node_ffff0000907d6142[1];
  local_48[2] = s_mediatek_md_attr_node_ffff0000907d6142[2];
  local_48[3] = s_mediatek_md_attr_node_ffff0000907d6142[3];
  local_48[4] = s_mediatek_md_attr_node_ffff0000907d6142[4];
  local_48[5] = s_mediatek_md_attr_node_ffff0000907d6142[5];
  local_48[6] = s_mediatek_md_attr_node_ffff0000907d6142[6];
  local_48[7] = s_mediatek_md_attr_node_ffff0000907d6142[7];
  acStack_40[0] = s_mediatek_md_attr_node_ffff0000907d6142[8];
  acStack_40[1] = s_mediatek_md_attr_node_ffff0000907d6142[9];
  acStack_40[2] = s_mediatek_md_attr_node_ffff0000907d6142[10];
  acStack_40[3] = s_mediatek_md_attr_node_ffff0000907d6142[0xb];
  acStack_40[4] = s_mediatek_md_attr_node_ffff0000907d6142[0xc];
  acStack_40[5] = s_mediatek_md_attr_node_ffff0000907d6142[0xd];
  local_3a[0] = s_mediatek_md_attr_node_ffff0000907d6142[0xe];
  local_3a[1] = s_mediatek_md_attr_node_ffff0000907d6142[0xf];
  acStack_38[0] = s_mediatek_md_attr_node_ffff0000907d6142[0x10];
  acStack_38[1] = s_mediatek_md_attr_node_ffff0000907d6142[0x11];
  acStack_38[2] = s_mediatek_md_attr_node_ffff0000907d6142[0x12];
  acStack_38[3] = s_mediatek_md_attr_node_ffff0000907d6142[0x13];
  acStack_38[4] = s_mediatek_md_attr_node_ffff0000907d6142[0x14];
  acStack_38[5] = s_mediatek_md_attr_node_ffff0000907d6142[0x15];
  iVar1 = FUN_ffff00009076eb40(param_1,0,s_md_attr_node_ffff0000907d10da);
  if (-1 < iVar1) {
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar1,s_compatible_ffff0000907d74bf,local_48,0x16);
    if (iVar2 == 0) {
      uVar3 = FUN_ffff0000907325a4();
      uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      local_14 = uVar3 >> 0x10 | uVar3 << 0x10;
      iVar2 = FUN_ffff00009076e6d4
                        (param_1,iVar1,s_mediatek_md_drdi_rf_set_idx_ffff0000907dc73d,&local_14,4);
      if (iVar2 == 0) {
        uVar3 = FUN_ffff0000907325ec();
        uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
        local_14 = uVar3 >> 0x10 | uVar3 << 0x10;
        FUN_ffff00009076e6d4
                  (param_1,iVar1,s_mediatek_md_product_name_model_i_ffff0000907d926a,&local_14,4);
      }
    }
  }
  return;
}


