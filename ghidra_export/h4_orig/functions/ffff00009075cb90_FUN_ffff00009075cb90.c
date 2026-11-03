// ffff00009075cb90  size=636  FUN_ffff00009075cb90

void FUN_ffff00009075cb90(undefined8 param_1)

{
  char *pcVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  undefined4 uVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_48;
  uint local_28;
  uint uStack_24;
  
  iVar3 = thunk_FUN_ffff00009075bc2c();
  iVar4 = FUN_ffff00009076da24(param_1,s__dispsys_config_ffff0000907d1982);
  if ((-1 < iVar4) ||
     (iVar4 = FUN_ffff00009076da24(param_1,s__soc_dispsys_config_ffff000090801335), -1 < iVar4)) {
    FUN_ffff00009076e6d4(param_1,iVar4,s_force_no_prim_dual_pipe_ffff0000907fa58c,0,0);
  }
  local_28 = 0;
  uStack_24 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_48 = 0;
  FUN_ffff00009077f6fc(&local_a8,99,s__soc_dsi_ffff0000907f8a7c);
  iVar4 = FUN_ffff00009076da24(param_1,&local_a8);
  uVar5 = thunk_FUN_ffff00009075bc18();
  pcVar1 = s__dsi_panel_d_port_endpoint_ffff0000907ff901;
  if (-1 < iVar4) {
    pcVar1 = s__soc_dsi_panel_d_port_endpoint_ffff0000907e68b4;
  }
  FUN_ffff00009077f6fc(&local_a8,99,pcVar1,uVar5);
  iVar6 = FUN_ffff00009076da24(param_1,&local_a8);
  if (-1 < iVar6) {
    uVar7 = FUN_ffff00009076d3e4(param_1,iVar6);
    pcVar1 = s__dsi_ports_port_endpoint_ffff0000907d8184;
    if (-1 < iVar4) {
      pcVar1 = s__soc_dsi_ports_port_endpoint_ffff0000907fde9a;
    }
    local_28 = uVar7;
    iVar6 = FUN_ffff00009076da24(param_1,pcVar1);
    if (-1 < iVar6) {
      lVar8 = FUN_ffff00009076df9c(param_1,iVar6,s_remote_endpoint_ffff0000907f1a66,0);
      if (lVar8 != 0) {
        FUN_ffff00009077eb20(&uStack_24,lVar8,4);
        uVar2 = (uStack_24 & 0xff00ff00) >> 8 | (uStack_24 & 0xff00ff) << 8;
        uStack_24 = uVar2 >> 0x10 | uVar2 << 0x10;
      }
      uVar7 = (uVar7 & 0xff00ff00) >> 8 | (uVar7 & 0xff00ff) << 8;
      local_28 = uVar7 >> 0x10 | uVar7 << 0x10;
      FUN_ffff00009076e6d4(param_1,iVar6,s_remote_endpoint_ffff0000907f1a66,&local_28,4);
      lVar8 = FUN_ffff00009076df9c(param_1,iVar6,s_remote_endpoint_ffff0000907f1a66,0);
      if (lVar8 != 0) {
        FUN_ffff00009077eb20(&uStack_24,lVar8,4);
        uVar7 = (uStack_24 & 0xff00ff00) >> 8 | (uStack_24 & 0xff00ff) << 8;
        uStack_24 = uVar7 >> 0x10 | uVar7 << 0x10;
      }
    }
  }
  FUN_ffff00009077e11c(s_set_dsi_panel_index_to_dtb_faile_ffff0000907d99b9);
  if (iVar3 != 0) {
    pcVar1 = s__mipi_tx_config_ffff0000907f02a1;
    if (-1 < iVar4) {
      pcVar1 = s__soc_mipi_tx_config_ffff0000907e9ca3;
    }
    iVar3 = FUN_ffff00009076da24(param_1,pcVar1);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c(s_set_mipi_tx_to_dtb_failed__ffff0000907ece74);
    }
    else {
      FUN_ffff00009076df9c(param_1,iVar3,s_compatible_ffff0000907d74bf,0);
      iVar4 = FUN_ffff00009077f250(s_mediatek_mt6991_mipi_tx_cphy_ffff0000907fa56f);
      FUN_ffff00009076e6d4
                (param_1,iVar3,s_compatible_ffff0000907d74bf,
                 s_mediatek_mt6991_mipi_tx_cphy_ffff0000907fa56f,iVar4 + 1);
      FUN_ffff00009076df9c(param_1,iVar3,s_compatible_ffff0000907d74bf,0);
    }
  }
  return;
}


