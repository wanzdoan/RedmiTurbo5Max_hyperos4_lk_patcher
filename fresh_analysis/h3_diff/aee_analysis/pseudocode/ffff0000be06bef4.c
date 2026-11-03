
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be06bef4(void)

{
  byte bVar1;
  uint uVar2;
  long lVar3;
  byte local_38 [4];
  byte local_34 [4];
  byte local_18 [4];
  byte local_14 [4];
  
  FUN_ffff0000be06b3b8(1);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xfffeffff;
  _DAT_ffff000016730320 = _DAT_ffff000016730320 & 0xfffffcff;
  _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xff7fffff;
  _DAT_ffff000016730300 = _DAT_ffff000016730300 | 0x20;
  _DAT_ffff000016730314 = _DAT_ffff000016730314 & 0xfffff7ff;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xfbffffff | 0x40008;
  FUN_ffff0000be05ede4(2000);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xffffffef | 0x3a2a;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xff41c30b;
  FUN_ffff0000be05ede4(2000);
  lVar3 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e);
  if (lVar3 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__Failed_to_get_regmap_ffff0000be0ac331,s_eusb2_phy_tuning_ffff0000be0b15a3,
               0x22e);
  }
  else {
    if (-1 < _DAT_ffff0000be0f2d38) {
      FUN_ffff0000be05fd98(s__EUSB2__rx_sqth_0x_x_ffff0000be0aad69);
      bVar1 = DAT_ffff0000be0f2d38;
      local_34[0] = 0;
      FUN_ffff0000be0299c8(lVar3,0xd18,local_34,1);
      local_38[0] = local_34[0] & 0xf0 | bVar1 & 0xf;
      FUN_ffff0000be029990(lVar3,0xd18,local_38,1);
    }
    if (-1 < (int)DAT_ffff0000be0f2d3c) {
      FUN_ffff0000be05fd98(s__EUSB2__pre_emphasis_0x_x_ffff0000be0b36b8);
      uVar2 = DAT_ffff0000be0f2d3c;
      local_14[0] = 0;
      FUN_ffff0000be0299c8(lVar3,0xd1b,local_14,1);
      local_18[0] = local_14[0] & 0x8f | (byte)((uVar2 & 7) << 4);
      FUN_ffff0000be029990(lVar3,0xd1b,local_18,1);
    }
  }
  if (((DAT_ffff0000be0f2cfc != '\0') &&
      (uVar2 = thunk_FUN_ffff0000be0364a4(DAT_ffff0000be0f2d00), uVar2 != 0)) &&
     (DAT_ffff0000be0f2d0c < 5)) {
    *(uint *)(&PTR_DAT_ffff0000be0f2d48)[(int)DAT_ffff0000be0f2d0c] =
         (DAT_ffff0000be0f2d04 & uVar2) >> (ulong)(DAT_ffff0000be0f2d08 & 0x1f);
  }
  if (((DAT_ffff0000be0f2d10 != '\0') &&
      (uVar2 = thunk_FUN_ffff0000be0364a4(DAT_ffff0000be0f2d14), uVar2 != 0)) &&
     (DAT_ffff0000be0f2d20 < 5)) {
    *(uint *)(&PTR_DAT_ffff0000be0f2d48)[(int)DAT_ffff0000be0f2d20] =
         (DAT_ffff0000be0f2d18 & uVar2) >> (ulong)(DAT_ffff0000be0f2d1c & 0x1f);
  }
  if (((DAT_ffff0000be0f2d24 != '\0') &&
      (uVar2 = thunk_FUN_ffff0000be0364a4(DAT_ffff0000be0f2d28), uVar2 != 0)) &&
     (DAT_ffff0000be0f2d34 < 5)) {
    *(uint *)(&PTR_DAT_ffff0000be0f2d48)[(int)DAT_ffff0000be0f2d34] =
         (DAT_ffff0000be0f2d2c & uVar2) >> (ulong)(DAT_ffff0000be0f2d30 & 0x1f);
  }
  if (-1 < (int)DAT_ffff0000be0f2ce0) {
    uVar2 = DAT_ffff0000be0f2ce0;
    if (DAT_ffff0000be0f2cf4 + DAT_ffff0000be0f2ce0 < 0x40 &&
        0xffffff80 < DAT_ffff0000be0f2cf4 - 0x40U) {
      uVar2 = DAT_ffff0000be0f2cf4 + DAT_ffff0000be0f2ce0;
    }
    FUN_ffff0000be05fd98(s__U2PHY___val_efuse_intr_0x_x_ffff0000be0c12be,uVar2);
    _DAT_ffff000016730330 =
         _DAT_ffff000016730330 & 0xc0000000 |
         _DAT_ffff000016730330 & 0xffffff | (uVar2 & 0x3f) << 0x18;
  }
  if (-1 < (int)DAT_ffff0000be0f2ce4) {
    uVar2 = DAT_ffff0000be0f2ce4;
    if (DAT_ffff0000be0f2cf8 + DAT_ffff0000be0f2ce4 < 0x10 &&
        0xffffffe0 < DAT_ffff0000be0f2cf8 - 0x10U) {
      uVar2 = DAT_ffff0000be0f2cf8 + DAT_ffff0000be0f2ce4;
    }
    FUN_ffff0000be05fd98(s__U2PHY___val_efuse_term_cal_0x_x_ffff0000be0b6e8a,uVar2);
    _DAT_ffff000016730334 =
         _DAT_ffff000016730334 & 0xfffff000 | _DAT_ffff000016730334 & 0xff | (uVar2 & 0xf) << 8;
  }
  if (-1 < (int)DAT_ffff0000be0f2ce8) {
    FUN_ffff0000be05fd98(s__U2PHY__hsrx_term_cal_0x_x_ffff0000be0a50cd);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfff00000 |
         _DAT_ffff000016730340 & 0xffff | (DAT_ffff0000be0f2ce8 & 0xf) << 0x10;
  }
  if (-1 < (int)DAT_ffff0000be0f2cec) {
    FUN_ffff0000be05fd98(s__U2PHY__hstx_impn_0x_x_ffff0000be0b4a1c);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfe000000 |
         _DAT_ffff000016730340 & 0xfffff | (DAT_ffff0000be0f2cec & 0x1f) << 0x14;
  }
  if (-1 < (int)DAT_ffff0000be0f2cf0) {
    FUN_ffff0000be05fd98(s__U2PHY__hstx_impp_0x_x_ffff0000be0a7793);
    _DAT_ffff000016730344 =
         _DAT_ffff000016730344 & 0xffffe000 |
         _DAT_ffff000016730344 & 0xff | (DAT_ffff0000be0f2cf0 & 0x1f) << 8;
  }
  if (-1 < (int)DAT_ffff0000be0f2cb8) {
    FUN_ffff0000be05fd98(s__U2PHY__eye_src_0x_x_ffff0000be0c5a54);
    _DAT_ffff000016730314 =
         _DAT_ffff000016730314 & 0xffff8000 |
         _DAT_ffff000016730314 & 0xfff | (DAT_ffff0000be0f2cb8 & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000be0f2cbc) {
    FUN_ffff0000be05fd98(s__U2PHY__eye_vrt_0x_x_ffff0000be0b5c8e);
    _DAT_ffff000016730304 =
         _DAT_ffff000016730304 & 0xffff8000 |
         _DAT_ffff000016730304 & 0xfff | (DAT_ffff0000be0f2cbc & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000be0f2cc0) {
    FUN_ffff0000be05fd98(s__U2PHY__eye_vrt_0x_x_ffff0000be0b5c8e);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000be0f2cc0 & 0xf;
  }
  if (-1 < (int)DAT_ffff0000be0f2cc8) {
    FUN_ffff0000be05fd98(s__U2PHY__rx_sqth_0x_x_ffff0000be0b80b3);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000be0f2cc8 & 0xf;
  }
  if (-1 < (int)DAT_ffff0000be0f2cc4) {
    FUN_ffff0000be05fd98(s__U2PHY__discth_0x_x_ffff0000be0bb65b);
    _DAT_ffff000016730318 =
         _DAT_ffff000016730318 & 0xffffff00 |
         _DAT_ffff000016730318 & 0xf | (DAT_ffff0000be0f2cc4 & 0xf) << 4;
  }
  if (-1 < DAT_ffff0000be0f2ccc) {
    FUN_ffff0000be05fd98(s__U2PHY__rev6_0x_x_ffff0000be0bedc9);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0x3fffffff | DAT_ffff0000be0f2ccc << 0x1e;
  }
  if (-1 < DAT_ffff0000be0f2cd0) {
    FUN_ffff0000be05fd98(s__U2PHY__pll_fbksel_0x_x_ffff0000be0bc78b);
    _DAT_ffff000016730340 = _DAT_ffff000016730340 & 0x7fffffff | DAT_ffff0000be0f2cd0 << 0x1f;
  }
  if (-1 < (int)DAT_ffff0000be0f2cd4) {
    FUN_ffff0000be05fd98(s__U2PHY__pll_posdiv_0x_x_ffff0000be0a3cf5);
    _DAT_ffff000016730344 = _DAT_ffff000016730344 & 0xfffffff8 | DAT_ffff0000be0f2cd4 & 7;
  }
  if (-1 < (int)DAT_ffff0000be0f2cd8) {
    FUN_ffff0000be05fd98(s__U2PHY__hsrx_vref_sel_0x_x_ffff0000be0bda86);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xffffff80 |
         _DAT_ffff000016730340 & 0xf | (DAT_ffff0000be0f2cd8 & 7) << 4;
  }
  if (-1 < (int)DAT_ffff0000be0f2cdc) {
    FUN_ffff0000be05fd98(s__U2PHY__fs_cr_0x_x_ffff0000be0aefdd);
    _DAT_ffff000016730310 =
         _DAT_ffff000016730310 & 0xfffff800 |
         _DAT_ffff000016730310 & 0xff | (DAT_ffff0000be0f2cdc & 7) << 8;
  }
  return;
}


