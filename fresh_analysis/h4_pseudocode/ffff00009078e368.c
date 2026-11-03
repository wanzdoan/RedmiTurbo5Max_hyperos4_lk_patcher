
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078e368(void)

{
  byte bVar1;
  uint uVar2;
  long lVar3;
  byte local_38 [4];
  byte local_34 [4];
  byte local_18 [4];
  byte local_14 [4];
  
  FUN_ffff00009078d82c(1);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xfffeffff;
  _DAT_ffff000016730320 = _DAT_ffff000016730320 & 0xfffffcff;
  _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xff7fffff;
  _DAT_ffff000016730300 = _DAT_ffff000016730300 | 0x20;
  _DAT_ffff000016730314 = _DAT_ffff000016730314 & 0xfffff7ff;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xfbffffff | 0x40008;
  FUN_ffff00009077cea0(2000);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xffffffef | 0x3a2a;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xff41c30b;
  FUN_ffff00009077cea0(2000);
  lVar3 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_get_regmap_ffff0000907dbe95,s_eusb2_phy_tuning_ffff0000907e3bce,
               0x22e);
  }
  else {
    if (-1 < _DAT_ffff00009083673c) {
      FUN_ffff00009077e11c(s__EUSB2__rx_sqth_0x_x_ffff0000907d9f14);
      bVar1 = DAT_ffff00009083673c;
      local_34[0] = 0;
      FUN_ffff0000907375c4(lVar3,0xd18,local_34,1);
      local_38[0] = local_34[0] & 0xf0 | bVar1 & 0xf;
      FUN_ffff00009073758c(lVar3,0xd18,local_38,1);
    }
    if (-1 < (int)DAT_ffff000090836740) {
      FUN_ffff00009077e11c(s__EUSB2__pre_emphasis_0x_x_ffff0000907e6e32);
      uVar2 = DAT_ffff000090836740;
      local_14[0] = 0;
      FUN_ffff0000907375c4(lVar3,0xd1b,local_14,1);
      local_18[0] = local_14[0] & 0x8f | (byte)((uVar2 & 7) << 4);
      FUN_ffff00009073758c(lVar3,0xd1b,local_18,1);
    }
  }
  if (((DAT_ffff000090836700 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074b22c(DAT_ffff000090836704), uVar2 != 0)) &&
     (DAT_ffff000090836710 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836750)[(int)DAT_ffff000090836710] =
         (DAT_ffff000090836708 & uVar2) >> (ulong)(DAT_ffff00009083670c & 0x1f);
  }
  if (((DAT_ffff000090836714 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074b22c(DAT_ffff000090836718), uVar2 != 0)) &&
     (DAT_ffff000090836724 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836750)[(int)DAT_ffff000090836724] =
         (DAT_ffff00009083671c & uVar2) >> (ulong)(DAT_ffff000090836720 & 0x1f);
  }
  if (((DAT_ffff000090836728 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074b22c(DAT_ffff00009083672c), uVar2 != 0)) &&
     (DAT_ffff000090836738 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836750)[(int)DAT_ffff000090836738] =
         (DAT_ffff000090836730 & uVar2) >> (ulong)(DAT_ffff000090836734 & 0x1f);
  }
  if (-1 < (int)DAT_ffff0000908366e4) {
    uVar2 = DAT_ffff0000908366e4;
    if (DAT_ffff0000908366f8 + DAT_ffff0000908366e4 < 0x40 &&
        0xffffff80 < DAT_ffff0000908366f8 - 0x40U) {
      uVar2 = DAT_ffff0000908366f8 + DAT_ffff0000908366e4;
    }
    FUN_ffff00009077e11c(s__U2PHY___val_efuse_intr_0x_x_ffff0000907fa9c1,uVar2);
    _DAT_ffff000016730330 =
         _DAT_ffff000016730330 & 0xc0000000 |
         _DAT_ffff000016730330 & 0xffffff | (uVar2 & 0x3f) << 0x18;
  }
  if (-1 < (int)DAT_ffff0000908366e8) {
    uVar2 = DAT_ffff0000908366e8;
    if (DAT_ffff0000908366fc + DAT_ffff0000908366e8 < 0x10 &&
        0xffffffe0 < DAT_ffff0000908366fc - 0x10U) {
      uVar2 = DAT_ffff0000908366fc + DAT_ffff0000908366e8;
    }
    FUN_ffff00009077e11c(s__U2PHY___val_efuse_term_cal_0x_x_ffff0000907eb994,uVar2);
    _DAT_ffff000016730334 =
         _DAT_ffff000016730334 & 0xfffff000 | _DAT_ffff000016730334 & 0xff | (uVar2 & 0xf) << 8;
  }
  if (-1 < (int)DAT_ffff0000908366ec) {
    FUN_ffff00009077e11c(s__U2PHY__hsrx_term_cal_0x_x_ffff0000907d1ff2);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfff00000 |
         _DAT_ffff000016730340 & 0xffff | (DAT_ffff0000908366ec & 0xf) << 0x10;
  }
  if (-1 < (int)DAT_ffff0000908366f0) {
    FUN_ffff00009077e11c(s__U2PHY__hstx_impn_0x_x_ffff0000907e87bd);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfe000000 |
         _DAT_ffff000016730340 & 0xfffff | (DAT_ffff0000908366f0 & 0x1f) << 0x14;
  }
  if (-1 < (int)DAT_ffff0000908366f4) {
    FUN_ffff00009077e11c(s__U2PHY__hstx_impp_0x_x_ffff0000907d52b7);
    _DAT_ffff000016730344 =
         _DAT_ffff000016730344 & 0xffffe000 |
         _DAT_ffff000016730344 & 0xff | (DAT_ffff0000908366f4 & 0x1f) << 8;
  }
  if (-1 < (int)DAT_ffff0000908366bc) {
    FUN_ffff00009077e11c(s__U2PHY__eye_src_0x_x_ffff000090801906);
    _DAT_ffff000016730314 =
         _DAT_ffff000016730314 & 0xffff8000 |
         _DAT_ffff000016730314 & 0xfff | (DAT_ffff0000908366bc & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000908366c0) {
    FUN_ffff00009077e11c(s__U2PHY__eye_vrt_0x_x_ffff0000907ea1e9);
    _DAT_ffff000016730304 =
         _DAT_ffff000016730304 & 0xffff8000 |
         _DAT_ffff000016730304 & 0xfff | (DAT_ffff0000908366c0 & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000908366c4) {
    FUN_ffff00009077e11c(s__U2PHY__eye_vrt_0x_x_ffff0000907ea1e9);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000908366c4 & 0xf;
  }
  if (-1 < (int)DAT_ffff0000908366cc) {
    FUN_ffff00009077e11c(s__U2PHY__rx_sqth_0x_x_ffff0000907ed494);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000908366cc & 0xf;
  }
  if (-1 < (int)DAT_ffff0000908366c8) {
    FUN_ffff00009077e11c(s__U2PHY__discth_0x_x_ffff0000907f20c4);
    _DAT_ffff000016730318 =
         _DAT_ffff000016730318 & 0xffffff00 |
         _DAT_ffff000016730318 & 0xf | (DAT_ffff0000908366c8 & 0xf) << 4;
  }
  if (-1 < DAT_ffff0000908366d0) {
    FUN_ffff00009077e11c(s__U2PHY__rev6_0x_x_ffff0000907f7400);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0x3fffffff | DAT_ffff0000908366d0 << 0x1e;
  }
  if (-1 < DAT_ffff0000908366d4) {
    FUN_ffff00009077e11c(s__U2PHY__pll_fbksel_0x_x_ffff0000907f3919);
    _DAT_ffff000016730340 = _DAT_ffff000016730340 & 0x7fffffff | DAT_ffff0000908366d4 << 0x1f;
  }
  if (-1 < (int)DAT_ffff0000908366d8) {
    FUN_ffff00009077e11c(s__U2PHY__pll_posdiv_0x_x_ffff0000907d03b6);
    _DAT_ffff000016730344 = _DAT_ffff000016730344 & 0xfffffff8 | DAT_ffff0000908366d8 & 7;
  }
  if (-1 < (int)DAT_ffff0000908366dc) {
    FUN_ffff00009077e11c(s__U2PHY__hsrx_vref_sel_0x_x_ffff0000907f56bf);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xffffff80 |
         _DAT_ffff000016730340 & 0xf | (DAT_ffff0000908366dc & 7) << 4;
  }
  if (-1 < (int)DAT_ffff0000908366e0) {
    FUN_ffff00009077e11c(s__U2PHY__fs_cr_0x_x_ffff0000907e045b);
    _DAT_ffff000016730310 =
         _DAT_ffff000016730310 & 0xfffff800 |
         _DAT_ffff000016730310 & 0xff | (DAT_ffff0000908366e0 & 7) << 8;
  }
  return;
}


