
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be06c4e8(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  int local_24;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee24(s__U2PHY__null_fdt_ffff0000be0b8071);
  }
  iVar2 = FUN_ffff0000be05620c(param_1,s__soc_usb_phy0_16730000_usb2_phy0_ffff0000be0bb631);
  if (-1 < iVar2) {
    local_24 = 0;
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_efuse_intr_ffff0000be0ba5bd,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2ce0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2ce0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__efuse_intr_0x_x_in_dts_ffff0000be0b8083);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_efuse_term_ffff0000be0abe52,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2ce4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2ce4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__efuse_term_cal_0x_x_in_d_ffff0000be0aef9d);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_eye_src_ffff0000be0bc75d,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cb8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cb8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__eye_src_0x_x_in_dts_ffff0000be0bc76e);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_eye_vrt_ffff0000be0b6e79,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cbc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cbc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__eye_vrt_0x_x_in_dts_ffff0000be0c48db);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_eye_term_ffff0000be0b4a0a,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cc0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cc0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__eye_term_0x_x_in_dts_ffff0000be0b369a);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_rx_sqth_ffff0000be0b276c,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cc8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cc8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__rx_sqth_0x_x_in_dts_ffff0000be0acff6);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_discth_ffff0000be0b80a3,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cc4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cc4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__discth_0x_x_in_dts_ffff0000be0aefc1);
    }
    puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_mediatek_rev6_ffff0000be0a8862,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2ccc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2ccc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__rev6_0x_x_in_dts_ffff0000be0c69ef);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_pll_fbksel_ffff0000be0bedb5,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cd0 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cd0 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__pll_fbksel_0x_x_in_dts_ffff0000be0bda66);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_pll_posdiv_ffff0000be0abe66,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cd4 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cd4 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__pll_posdiv_0x_x_in_dts_ffff0000be0a50ad);
    }
    puVar3 = (uint *)FUN_ffff0000be056494
                               (param_1,iVar2,s_mediatek_hsrx_vref_sel_ffff0000be0b054c,&local_24);
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cd8 = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cd8 = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__hsrx_vref_sel_0x_x_in_dt_ffff0000be0a3cd2);
    }
    puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_mediatek_fs_cr_ffff0000be0c48f8,&local_24)
    ;
    if ((local_24 < 1) || (puVar3 == (uint *)0x0)) {
      DAT_ffff0000be0f2cdc = 0xffffffea;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be0f2cdc = uVar1 >> 0x10 | uVar1 << 0x10;
      FUN_ffff0000be05fd98(s__U2PHY__fs_cr_0x_x_in_dts_ffff0000be0b5c73);
    }
    _DAT_ffff0000be0f2ce8 = 0xffffffeaffffffea;
    DAT_ffff0000be0f2cf0 = 0xffffffea;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee24(s__U2PHY__Failed_to_find__soc_usb__ffff0000be0b2723);
}


