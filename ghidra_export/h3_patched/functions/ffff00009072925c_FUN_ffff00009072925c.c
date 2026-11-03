// ffff00009072925c  size=1260  FUN_ffff00009072925c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072925c(long param_1)

{
  int iVar1;
  uint *puVar2;
  long lVar3;
  ulong uVar4;
  uint uVar5;
  int local_24;
  
  FUN_ffff00009077eb88(&DAT_ffff00009086d39c,0,0x148);
  if (param_1 == 0) {
    FUN_ffff00009077de54(s__kpd__fdt_null__fall_back_to_def_ffff0000907f0fae);
  }
  else {
    iVar1 = FUN_ffff00009076c880(param_1);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_Bad_DTB_header__ffff0000907d0d95);
    }
    iVar1 = FUN_ffff00009076e064(param_1,0,s_mediatek_kp_ffff0000907ec1a7);
    if (-1 < iVar1) {
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_sw_pwrkey_ffff0000907fcf15,&local_24);
      _DAT_ffff00009086d39c = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d39c = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_hw_pwrkey_ffff0000907d5d61,&local_24);
      _DAT_ffff00009086d3a0 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d3a0 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_sw_rstkey_ffff0000907ef621,&local_24);
      _DAT_ffff00009086d3a4 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d3a4 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_hw_rstkey_ffff0000907ea9dc,&local_24);
      _DAT_ffff00009086d3a8 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d3a8 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_use_extend_type_ffff0000907fed0e,
                                  &local_24);
      _DAT_ffff00009086d3ac = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d3ac = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_hw_recovery_key_ffff0000907e7702,
                                  &local_24);
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d4d4 = CONCAT44(_DAT_ffff00009086d4d8,uVar5);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_hw_factory_key_ffff0000907e2925,&local_24
                                 );
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d4d4 = CONCAT44(uVar5,_DAT_ffff00009086d4d4);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_gpio_key_index_ffff0000907ddcbb,&local_24
                                 );
      uVar5 = 0xffffffff;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      _DAT_ffff00009086d4dc = CONCAT44(_DAT_ffff00009086d4e0,uVar5);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_factory_key_type_ffff0000907e5f1b,
                                  &local_24);
      uVar5 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      uVar4 = _DAT_ffff00009086d4dc & 0xffffffff;
      _DAT_ffff00009086d4e0 = uVar5;
      FUN_ffff00009077de54
                (s__s__d__kpd_dts_data_kpd_gpio_ind_ffff0000907e0f9a,
                 s_get_kpd_dts_info_ffff0000907f6068,0x87,uVar4);
      FUN_ffff00009077de54
                (s__s__d__kpd_sw_pwrkey____d__kpd_h_ffff0000907e293d,
                 s_get_kpd_dts_info_ffff0000907f6068,0x89,_DAT_ffff00009086d39c,
                 _DAT_ffff00009086d3a0);
      FUN_ffff00009077de54
                (s__s__d__kpd_sw_rstkey____d__kpd_h_ffff0000907d9024,
                 s_get_kpd_dts_info_ffff0000907f6068,0x8b,_DAT_ffff00009086d3a4,
                 _DAT_ffff00009086d3a8);
      FUN_ffff00009077de54
                (s__s__d__kpd_hw_recovery_key____d__ffff0000907f44df,
                 s_get_kpd_dts_info_ffff0000907f6068,0x8d,_DAT_ffff00009086d4d4 & 0xffffffff,
                 _DAT_ffff00009086d4d8);
      FUN_ffff00009077de54
                (s__s__d__kpd_factory_key_type____d_ffff0000907ddcd3,
                 s_get_kpd_dts_info_ffff0000907f6068,0x8f,_DAT_ffff00009086d4e0);
      local_24 = 0;
      puVar2 = (uint *)FUN_ffff00009076dcd4
                                 (param_1,iVar1,s_mediatek_kpd_hw_map_num_ffff0000907dc50e,&local_24
                                 );
      _DAT_ffff00009086d3b0 = 0;
      if ((0 < local_24) && (puVar2 != (uint *)0x0)) {
        uVar5 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
        _DAT_ffff00009086d3b0 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
      local_24 = 0;
      lVar3 = FUN_ffff00009076dcd4
                        (param_1,iVar1,s_mediatek_kpd_hw_init_map_ffff0000907f7e16,&local_24);
      if ((local_24 < 1) || (lVar3 == 0)) {
        FUN_ffff00009077eb88(0xffff00009086d3b4,0,0x120);
      }
      else {
        iVar1 = local_24;
        if (0x47 < local_24) {
          iVar1 = 0x48;
        }
        FUN_ffff00009077e858(0xffff00009086d3b4,lVar3,iVar1);
      }
      goto code_r0xffff000090729680;
    }
  }
  _DAT_ffff00009086d4d8 = 0;
  _DAT_ffff00009086d3a0 = 8;
  _DAT_ffff00009086d3a8 = 0x11;
  _DAT_ffff00009086d4d4 = 0x11;
  _DAT_ffff00009086d4d4 = 0x11;
  _DAT_ffff00009086d4dc = -1;
  _DAT_ffff00009086d4e0 = 0;
code_r0xffff000090729680:
  DAT_ffff000090818e18 = 0x90000002a;
  DAT_ffff000090818e08 = _DAT_ffff00009086d4d4;
  DAT_ffff000090818e0c = _DAT_ffff00009086d4d8;
  DAT_ffff000090818e10 = _DAT_ffff00009086d3a0;
  DAT_ffff000090818e14 = _DAT_ffff00009086d3a8;
  DAT_ffff000090818e20 = _DAT_ffff00009086d4d4;
  DAT_ffff000090818e24 = _DAT_ffff00009086d4d4;
  DAT_ffff000090818e28 = _DAT_ffff00009086d4d8;
  if (_DAT_ffff00009086d4dc != -1) {
    FUN_ffff000090785468(_DAT_ffff00009086d4dc,0);
    FUN_ffff000090785538(_DAT_ffff00009086d4dc,1);
    FUN_ffff000090785720(_DAT_ffff00009086d4dc,1);
    FUN_ffff0000907853c4(_DAT_ffff00009086d4dc,0);
  }
  FUN_ffff00009077de54
            (s__s__d__KP_base_address__0x_lx_ffff0000907ddc9c,s_keypad_init_ffff0000907f97de,0xb0,
             &DAT_ffff00001c012000);
  FUN_ffff00009077de54
            (s__s__d__KP_EN__0x_x__KP_DEBOUNCE__ffff0000907ec180,s_keypad_init_ffff0000907f97de,0xb1
             ,_DAT_ffff00001c012024,_DAT_ffff00001c012018);
  return;
}


