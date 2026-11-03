// ffff000090732364  size=572  FUN_ffff000090732364

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090732364(undefined8 param_1)

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
  
  iVar1 = FUN_ffff00009076d75c(param_1,s__mddriver_ffff0000907fedfb);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__ccci__mddriver_not_ffff0000907e2af3,
               s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,0x13c);
    iVar1 = FUN_ffff00009076d75c(param_1,s__soc_mddriver_ffff0000908022c5);
    if (iVar1 < 0) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d___soc_mddriver_not_f_ffff0000907e2a8d,
                 s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,0x13f);
      return;
    }
  }
  _DAT_ffff00009086d9e8 = FUN_ffff00009077b06c(_DAT_ffff00009086d9e8);
  iVar2 = FUN_ffff00009076e40c
                    (param_1,iVar1,s_ccci_modem_info_v2_ffff0000907fef2f,&DAT_ffff00009086d9e8,0x30)
  ;
  if (iVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_create_modem_argume_ffff0000907ee03c,
               s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,0x14a);
  }
  else {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__create_modem_argum_ffff0000907e4801,
               s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,0x148);
  }
  if ((DAT_ffff00009086da00 & 1) == 0) {
    uVar4 = FUN_ffff00009077ef88(s_disabled_ffff0000907f0df6);
    iVar1 = FUN_ffff00009076e40c
                      (param_1,iVar1,s_status_ffff0000907edb53,s_disabled_ffff0000907f0df6,uVar4);
    if (iVar1 != 0) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__create_modem_statu_ffff0000907e4834,
                 s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,0x150);
      goto LAB_ffff0000907324b4;
    }
    pcVar5 = s__s__d___CCCI_create_modem_status_ffff0000907f9a36;
    uVar4 = 0x152;
  }
  else {
    pcVar5 = s__s__d___CCCI_md1_enabled_ffff0000907e78bd;
    uVar4 = 0x154;
  }
  FUN_ffff00009077de54(pcVar5,s_ccci_update_md_arg_info_to_dt_ffff0000907daa94,uVar4);
LAB_ffff0000907324b4:
  local_48[0] = s_mediatek_md_attr_node_ffff0000907d60ac[0];
  local_48[1] = s_mediatek_md_attr_node_ffff0000907d60ac[1];
  local_48[2] = s_mediatek_md_attr_node_ffff0000907d60ac[2];
  local_48[3] = s_mediatek_md_attr_node_ffff0000907d60ac[3];
  local_48[4] = s_mediatek_md_attr_node_ffff0000907d60ac[4];
  local_48[5] = s_mediatek_md_attr_node_ffff0000907d60ac[5];
  local_48[6] = s_mediatek_md_attr_node_ffff0000907d60ac[6];
  local_48[7] = s_mediatek_md_attr_node_ffff0000907d60ac[7];
  acStack_40[0] = s_mediatek_md_attr_node_ffff0000907d60ac[8];
  acStack_40[1] = s_mediatek_md_attr_node_ffff0000907d60ac[9];
  acStack_40[2] = s_mediatek_md_attr_node_ffff0000907d60ac[10];
  acStack_40[3] = s_mediatek_md_attr_node_ffff0000907d60ac[0xb];
  acStack_40[4] = s_mediatek_md_attr_node_ffff0000907d60ac[0xc];
  acStack_40[5] = s_mediatek_md_attr_node_ffff0000907d60ac[0xd];
  local_3a[0] = s_mediatek_md_attr_node_ffff0000907d60ac[0xe];
  local_3a[1] = s_mediatek_md_attr_node_ffff0000907d60ac[0xf];
  acStack_38[0] = s_mediatek_md_attr_node_ffff0000907d60ac[0x10];
  acStack_38[1] = s_mediatek_md_attr_node_ffff0000907d60ac[0x11];
  acStack_38[2] = s_mediatek_md_attr_node_ffff0000907d60ac[0x12];
  acStack_38[3] = s_mediatek_md_attr_node_ffff0000907d60ac[0x13];
  acStack_38[4] = s_mediatek_md_attr_node_ffff0000907d60ac[0x14];
  acStack_38[5] = s_mediatek_md_attr_node_ffff0000907d60ac[0x15];
  iVar1 = FUN_ffff00009076e878(param_1,0,s_md_attr_node_ffff0000907d109d);
  if (-1 < iVar1) {
    iVar2 = FUN_ffff00009076e40c(param_1,iVar1,s_compatible_ffff0000907d7429,local_48,0x16);
    if (iVar2 == 0) {
      uVar3 = FUN_ffff00009073230c();
      uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      local_14 = uVar3 >> 0x10 | uVar3 << 0x10;
      iVar2 = FUN_ffff00009076e40c
                        (param_1,iVar1,s_mediatek_md_drdi_rf_set_idx_ffff0000907dc650,&local_14,4);
      if (iVar2 == 0) {
        uVar3 = FUN_ffff000090732354();
        uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
        local_14 = uVar3 >> 0x10 | uVar3 << 0x10;
        FUN_ffff00009076e40c
                  (param_1,iVar1,s_mediatek_md_product_name_model_i_ffff0000907d91d3,&local_14,4);
      }
    }
  }
  return;
}


