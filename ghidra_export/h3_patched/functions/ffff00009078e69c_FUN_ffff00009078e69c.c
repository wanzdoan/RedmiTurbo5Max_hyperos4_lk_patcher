// ffff00009078e69c  size=1124  FUN_ffff00009078e69c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078e69c(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  int local_24;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s__U2PHY__null_fdt_ffff0000907ed2b8);
  }
  iVar2 = FUN_ffff00009076d75c(param_1,s__soc_usb_phy0_16730000_usb2_phy0_ffff0000907f1ef7);
  if (-1 < iVar2) {
    local_24 = 0;
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_efuse_intr_ffff0000907f05cb,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__efuse_intr_0x_x_in_dts_ffff0000907ed2ca);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_efuse_term_ffff0000907db8de,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__efuse_term_cal_0x_x_in_d_ffff0000907e0361);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_eye_src_ffff0000907f3748,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff00009083669c = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff00009083669c = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__eye_src_0x_x_in_dts_ffff0000907f3759);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_eye_vrt_ffff0000907eb7e9,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366a0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366a0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__eye_vrt_0x_x_in_dts_ffff0000907ffd0e);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_eye_term_ffff0000907e8650,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366a4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366a4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__eye_term_0x_x_in_dts_ffff0000907e6cb9);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_rx_sqth_ffff0000907e554f,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366ac = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366ac = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__rx_sqth_0x_x_in_dts_ffff0000907dd408);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_discth_ffff0000907ed2ea,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366a8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366a8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__discth_0x_x_in_dts_ffff0000907e0385);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_mediatek_rev6_ffff0000907d6d93,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366b0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366b0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__rev6_0x_x_in_dts_ffff000090802eae);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_pll_fbksel_ffff0000907f7262,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366b4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366b4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__pll_fbksel_0x_x_in_dts_ffff0000907f551b);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_pll_posdiv_ffff0000907db8f2,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366b8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366b8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__pll_posdiv_0x_x_in_dts_ffff0000907d1f95);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar2,s_mediatek_hsrx_vref_sel_ffff0000907e1fea,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366bc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366bc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__hsrx_vref_sel_0x_x_in_dt_ffff0000907d0374);
    }
    puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_mediatek_fs_cr_ffff0000907ffd2b,&local_24)
    ;
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000908366c0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000908366c0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff00009077de54(s__U2PHY__fs_cr_0x_x_in_dts_ffff0000907ea073);
    }
    _DAT_ffff0000908366cc = 0xffffffeaffffffea;
    DAT_ffff0000908366d4 = 0xffffffea;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s__U2PHY__Failed_to_find__soc_usb__ffff0000907e5506);
}


