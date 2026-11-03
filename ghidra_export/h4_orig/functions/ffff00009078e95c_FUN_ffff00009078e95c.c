// ffff00009078e95c  size=1124  FUN_ffff00009078e95c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078e95c(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  int local_24;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s__U2PHY__null_fdt_ffff0000907ed452);
  }
  iVar2 = FUN_ffff00009076da24(param_1,s__soc_usb_phy0_16730000_usb2_phy0_ffff0000907f209a);
  if (-1 < iVar2) {
    local_24 = 0;
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_efuse_intr_ffff0000907f0753,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366e4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366e4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__efuse_intr_0x_x_in_dts_ffff0000907ed464);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_efuse_term_ffff0000907dba00,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366e8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366e8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__efuse_term_cal_0x_x_in_d_ffff0000907e041b);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_eye_src_ffff0000907f38eb,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366bc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366bc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__eye_src_0x_x_in_dts_ffff0000907f38fc);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_eye_vrt_ffff0000907eb983,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__eye_vrt_0x_x_in_dts_ffff0000907ffeb6);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_eye_term_ffff0000907e87ab,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__eye_term_0x_x_in_dts_ffff0000907e6e14);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_rx_sqth_ffff0000907e566a,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366cc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366cc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__rx_sqth_0x_x_in_dts_ffff0000907dd4c2);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_discth_ffff0000907ed484,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__discth_0x_x_in_dts_ffff0000907e043f);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_mediatek_rev6_ffff0000907d6e29,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366d0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366d0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__rev6_0x_x_in_dts_ffff0000908030b2);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_pll_fbksel_ffff0000907f73ec,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366d4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366d4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__pll_fbksel_0x_x_in_dts_ffff0000907f569f);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_pll_posdiv_ffff0000907dba14,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366d8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366d8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__pll_posdiv_0x_x_in_dts_ffff0000907d1fd2);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar2,s_mediatek_hsrx_vref_sel_ffff0000907e20d2,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366dc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366dc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__hsrx_vref_sel_0x_x_in_dt_ffff0000907d0393);
    }
    puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_mediatek_fs_cr_ffff0000907ffed3,&local_24)
    ;
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366e0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366e0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077e11c(s__U2PHY__fs_cr_0x_x_in_dts_ffff0000907ea1ce);
    }
    _DAT_ffff0000908366ec = 0xffffffeaffffffea;
    DAT_ffff0000908366f4 = 0xffffffea;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__U2PHY__Failed_to_find__soc_usb__ffff0000907e5621);
}


