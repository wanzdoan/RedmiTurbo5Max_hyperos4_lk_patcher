// ffff00009078e0a8  size=1404  FUN_ffff00009078e0a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078e0a8(void)

{
  byte bVar1;
  uint uVar2;
  long lVar3;
  byte local_38 [4];
  byte local_34 [4];
  byte local_18 [4];
  byte local_14 [4];
  
  FUN_ffff00009078d56c(1);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xfffeffff;
  _DAT_ffff000016730320 = _DAT_ffff000016730320 & 0xfffffcff;
  _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xff7fffff;
  _DAT_ffff000016730300 = _DAT_ffff000016730300 | 0x20;
  _DAT_ffff000016730314 = _DAT_ffff000016730314 & 0xfffff7ff;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xfbffffff | 0x40008;
  FUN_ffff00009077cbd8(2000);
  _DAT_ffff00001673036c = _DAT_ffff00001673036c & 0xffffffef | 0x3a2a;
  _DAT_ffff000016730368 = _DAT_ffff000016730368 & 0xff41c30b;
  FUN_ffff00009077cbd8(2000);
  lVar3 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_get_regmap_ffff0000907dbd8a,s_eusb2_phy_tuning_ffff0000907e3afd,
               0x22e);
  }
  else {
    if (-1 < _DAT_ffff00009083671c) {
      FUN_ffff00009077de54(s__EUSB2__rx_sqth_0x_x_ffff0000907d9e7d);
      bVar1 = DAT_ffff00009083671c;
      local_34[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd18,local_34,1);
      local_38[0] = local_34[0] & 0xf0 | bVar1 & 0xf;
      FUN_ffff0000907372f4(lVar3,0xd18,local_38,1);
    }
    if (-1 < (int)DAT_ffff000090836720) {
      FUN_ffff00009077de54(s__EUSB2__pre_emphasis_0x_x_ffff0000907e6cee);
      uVar2 = DAT_ffff000090836720;
      local_14[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd1b,local_14,1);
      local_18[0] = local_14[0] & 0x8f | (byte)((uVar2 & 7) << 4);
      FUN_ffff0000907372f4(lVar3,0xd1b,local_18,1);
    }
  }
  if (((DAT_ffff0000908366e0 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074af94(DAT_ffff0000908366e4), uVar2 != 0)) &&
     (DAT_ffff0000908366f0 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836730)[(int)DAT_ffff0000908366f0] =
         (DAT_ffff0000908366e8 & uVar2) >> (ulong)(DAT_ffff0000908366ec & 0x1f);
  }
  if (((DAT_ffff0000908366f4 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074af94(DAT_ffff0000908366f8), uVar2 != 0)) &&
     (DAT_ffff000090836704 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836730)[(int)DAT_ffff000090836704] =
         (DAT_ffff0000908366fc & uVar2) >> (ulong)(DAT_ffff000090836700 & 0x1f);
  }
  if (((DAT_ffff000090836708 != '\0') &&
      (uVar2 = thunk_FUN_ffff00009074af94(DAT_ffff00009083670c), uVar2 != 0)) &&
     (DAT_ffff000090836718 < 5)) {
    *(uint *)(&PTR_DAT_ffff000090836730)[(int)DAT_ffff000090836718] =
         (DAT_ffff000090836710 & uVar2) >> (ulong)(DAT_ffff000090836714 & 0x1f);
  }
  if (-1 < (int)DAT_ffff0000908366c4) {
    uVar2 = DAT_ffff0000908366c4;
    if (DAT_ffff0000908366d8 + DAT_ffff0000908366c4 < 0x40 &&
        0xffffff80 < DAT_ffff0000908366d8 - 0x40U) {
      uVar2 = DAT_ffff0000908366d8 + DAT_ffff0000908366c4;
    }
    FUN_ffff00009077de54(s__U2PHY___val_efuse_intr_0x_x_ffff0000907fa825,uVar2);
    _DAT_ffff000016730330 =
         _DAT_ffff000016730330 & 0xc0000000 |
         _DAT_ffff000016730330 & 0xffffff | (uVar2 & 0x3f) << 0x18;
  }
  if (-1 < (int)DAT_ffff0000908366c8) {
    uVar2 = DAT_ffff0000908366c8;
    if (DAT_ffff0000908366dc + DAT_ffff0000908366c8 < 0x10 &&
        0xffffffe0 < DAT_ffff0000908366dc - 0x10U) {
      uVar2 = DAT_ffff0000908366dc + DAT_ffff0000908366c8;
    }
    FUN_ffff00009077de54(s__U2PHY___val_efuse_term_cal_0x_x_ffff0000907eb811,uVar2);
    _DAT_ffff000016730334 =
         _DAT_ffff000016730334 & 0xfffff000 | _DAT_ffff000016730334 & 0xff | (uVar2 & 0xf) << 8;
  }
  if (-1 < (int)DAT_ffff0000908366cc) {
    FUN_ffff00009077de54(s__U2PHY__hsrx_term_cal_0x_x_ffff0000907d1fb5);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfff00000 |
         _DAT_ffff000016730340 & 0xffff | (DAT_ffff0000908366cc & 0xf) << 0x10;
  }
  if (-1 < (int)DAT_ffff0000908366d0) {
    FUN_ffff00009077de54(s__U2PHY__hstx_impn_0x_x_ffff0000907e8679);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xfe000000 |
         _DAT_ffff000016730340 & 0xfffff | (DAT_ffff0000908366d0 & 0x1f) << 0x14;
  }
  if (-1 < (int)DAT_ffff0000908366d4) {
    FUN_ffff00009077de54(s__U2PHY__hstx_impp_0x_x_ffff0000907d527a);
    _DAT_ffff000016730344 =
         _DAT_ffff000016730344 & 0xffffe000 |
         _DAT_ffff000016730344 & 0xff | (DAT_ffff0000908366d4 & 0x1f) << 8;
  }
  if (-1 < (int)DAT_ffff00009083669c) {
    FUN_ffff00009077de54(s__U2PHY__eye_src_0x_x_ffff00009080172a);
    _DAT_ffff000016730314 =
         _DAT_ffff000016730314 & 0xffff8000 |
         _DAT_ffff000016730314 & 0xfff | (DAT_ffff00009083669c & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000908366a0) {
    FUN_ffff00009077de54(s__U2PHY__eye_vrt_0x_x_ffff0000907ea0a5);
    _DAT_ffff000016730304 =
         _DAT_ffff000016730304 & 0xffff8000 |
         _DAT_ffff000016730304 & 0xfff | (DAT_ffff0000908366a0 & 7) << 0xc;
  }
  if (-1 < (int)DAT_ffff0000908366a4) {
    FUN_ffff00009077de54(s__U2PHY__eye_vrt_0x_x_ffff0000907ea0a5);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000908366a4 & 0xf;
  }
  if (-1 < (int)DAT_ffff0000908366ac) {
    FUN_ffff00009077de54(s__U2PHY__rx_sqth_0x_x_ffff0000907ed311);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0xfffffff0 | DAT_ffff0000908366ac & 0xf;
  }
  if (-1 < (int)DAT_ffff0000908366a8) {
    FUN_ffff00009077de54(s__U2PHY__discth_0x_x_ffff0000907f1f38);
    _DAT_ffff000016730318 =
         _DAT_ffff000016730318 & 0xffffff00 |
         _DAT_ffff000016730318 & 0xf | (DAT_ffff0000908366a8 & 0xf) << 4;
  }
  if (-1 < DAT_ffff0000908366b0) {
    FUN_ffff00009077de54(s__U2PHY__rev6_0x_x_ffff0000907f7276);
    _DAT_ffff000016730318 = _DAT_ffff000016730318 & 0x3fffffff | DAT_ffff0000908366b0 << 0x1e;
  }
  if (-1 < DAT_ffff0000908366b4) {
    FUN_ffff00009077de54(s__U2PHY__pll_fbksel_0x_x_ffff0000907f378d);
    _DAT_ffff000016730340 = _DAT_ffff000016730340 & 0x7fffffff | DAT_ffff0000908366b4 << 0x1f;
  }
  if (-1 < (int)DAT_ffff0000908366b8) {
    FUN_ffff00009077de54(s__U2PHY__pll_posdiv_0x_x_ffff0000907d0397);
    _DAT_ffff000016730344 = _DAT_ffff000016730344 & 0xfffffff8 | DAT_ffff0000908366b8 & 7;
  }
  if (-1 < (int)DAT_ffff0000908366bc) {
    FUN_ffff00009077de54(s__U2PHY__hsrx_vref_sel_0x_x_ffff0000907f5552);
    _DAT_ffff000016730340 =
         _DAT_ffff000016730340 & 0xffffff80 |
         _DAT_ffff000016730340 & 0xf | (DAT_ffff0000908366bc & 7) << 4;
  }
  if (-1 < (int)DAT_ffff0000908366c0) {
    FUN_ffff00009077de54(s__U2PHY__fs_cr_0x_x_ffff0000907e03b8);
    _DAT_ffff000016730310 =
         _DAT_ffff000016730310 & 0xfffff800 |
         _DAT_ffff000016730310 & 0xff | (DAT_ffff0000908366c0 & 7) << 8;
  }
  return;
}


