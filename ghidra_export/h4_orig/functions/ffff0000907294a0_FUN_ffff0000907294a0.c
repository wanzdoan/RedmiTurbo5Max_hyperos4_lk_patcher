// ffff0000907294a0  size=1260  FUN_ffff0000907294a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907294a0(long param_1)

{
  int iVar1;
  uint *puVar2;
  long lVar3;
  ulong uVar4;
  uint uVar5;
  int local_24;
  
  FUN_ffff00009077ee50(&DAT_ffff00009086d434,0,0x148);
  if (param_1 == 0) {
    FUN_ffff00009077e11c(s__kpd__fdt_null__fall_back_to_def_ffff0000907f1136);
  }
  else {
    iVar1 = FUN_ffff00009076cb48(param_1);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_Bad_DTB_header__ffff0000907d0dd2);
    }
    iVar1 = FUN_ffff00009076e32c(param_1,0,s_mediatek_kp_ffff0000907ec341);
    if (-1 < iVar1) {
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_sw_pwrkey_ffff0000907fd0bd,&local_24);
      _DAT_ffff00009086d434 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d434 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_hw_pwrkey_ffff0000907d5df7,&local_24);
      _DAT_ffff00009086d438 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d438 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_sw_rstkey_ffff0000907ef7a9,&local_24);
      _DAT_ffff00009086d43c = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d43c = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_hw_rstkey_ffff0000907eab76,&local_24);
      _DAT_ffff00009086d440 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d440 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_use_extend_type_ffff0000907feeb6,
                                  &local_24);
      _DAT_ffff00009086d444 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d444 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_hw_recovery_key_ffff0000907e785d,
                                  &local_24);
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d56c = CONCAT44(_DAT_ffff00009086d570,uVar5);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_hw_factory_key_ffff0000907e2a0d,&local_24
                                 );
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d56c = CONCAT44(uVar5,_DAT_ffff00009086d56c);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_gpio_key_index_ffff0000907ddd75,&local_24
                                 );
      uVar5 = 0xffffffff;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d574 = CONCAT44(_DAT_ffff00009086d578,uVar5);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_factory_key_type_ffff0000907e6046,
                                  &local_24);
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      uVar4 = _DAT_ffff00009086d574 & 0xffffffff;
      _DAT_ffff00009086d578 = uVar5;
      FUN_ffff00009077e11c
                (s__s__d__kpd_dts_data_kpd_gpio_ind_ffff0000907e1082,
                 s_get_kpd_dts_info_ffff0000907f6209,0x87,uVar4);
      FUN_ffff00009077e11c
                (s__s__d__kpd_sw_pwrkey____d__kpd_h_ffff0000907e2a25,
                 s_get_kpd_dts_info_ffff0000907f6209,0x89,_DAT_ffff00009086d434,
                 _DAT_ffff00009086d438);
      FUN_ffff00009077e11c
                (s__s__d__kpd_sw_rstkey____d__kpd_h_ffff0000907d90bb,
                 s_get_kpd_dts_info_ffff0000907f6209,0x8b,_DAT_ffff00009086d43c,
                 _DAT_ffff00009086d440);
      FUN_ffff00009077e11c
                (s__s__d__kpd_hw_recovery_key____d__ffff0000907f4663,
                 s_get_kpd_dts_info_ffff0000907f6209,0x8d,_DAT_ffff00009086d56c & 0xffffffff,
                 _DAT_ffff00009086d570);
      FUN_ffff00009077e11c
                (s__s__d__kpd_factory_key_type____d_ffff0000907ddd8d,
                 s_get_kpd_dts_info_ffff0000907f6209,0x8f,_DAT_ffff00009086d578);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076df9c
                                 (param_1,iVar1,s_mediatek_kpd_hw_map_num_ffff0000907dc5fb,&local_24
                                 );
      _DAT_ffff00009086d448 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d448 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      lVar3 = FUN_ffff00009076df9c
                        (param_1,iVar1,s_mediatek_kpd_hw_init_map_ffff0000907f7fb2,&local_24);
      if ((local_24 < 1) || (lVar3 == 0)) {
        FUN_ffff00009077ee50(0xffff00009086d44c,0,0x120);
      }
      else {
        iVar1 = local_24;
        if (0x47 < local_24) {
          iVar1 = 0x48;
        }
        FUN_ffff00009077eb20(0xffff00009086d44c,lVar3,iVar1);
      }
      goto code_r0xffff0000907298c4;
    }
  }
  _DAT_ffff00009086d570 = 0;
  _DAT_ffff00009086d438 = 8;
  _DAT_ffff00009086d440 = 0x11;
  _DAT_ffff00009086d56c = 0x11;
  _DAT_ffff00009086d56c = 0x11;
  _DAT_ffff00009086d574 = -1;
  _DAT_ffff00009086d578 = 0;
code_r0xffff0000907298c4:
  DAT_ffff000090818e38 = 0x90000002a;
  DAT_ffff000090818e28 = _DAT_ffff00009086d56c;
  DAT_ffff000090818e2c = _DAT_ffff00009086d570;
  DAT_ffff000090818e30 = _DAT_ffff00009086d438;
  DAT_ffff000090818e34 = _DAT_ffff00009086d440;
  DAT_ffff000090818e40 = _DAT_ffff00009086d56c;
  DAT_ffff000090818e44 = _DAT_ffff00009086d56c;
  DAT_ffff000090818e48 = _DAT_ffff00009086d570;
  if (_DAT_ffff00009086d574 != -1) {
    FUN_ffff000090785730(_DAT_ffff00009086d574,0);
    FUN_ffff000090785800(_DAT_ffff00009086d574,1);
    FUN_ffff0000907859e8(_DAT_ffff00009086d574,1);
    FUN_ffff00009078568c(_DAT_ffff00009086d574,0);
  }
  FUN_ffff00009077e11c
            (s__s__d__KP_base_address__0x_lx_ffff0000907ddd56,s_keypad_init_ffff0000907f997a,0xb0,
             &DAT_ffff00001c012000);
  FUN_ffff00009077e11c
            (s__s__d__KP_EN__0x_x__KP_DEBOUNCE__ffff0000907ec31a,s_keypad_init_ffff0000907f997a,0xb1
             ,_DAT_ffff00001c012024,_DAT_ffff00001c012018);
  return;
}


