
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b803a010(int param_1)

{
  switch(param_1) {
  case 0xe2:
    _DAT_ffff000032800e70 = 4;
    break;
  case 0xe3:
    _DAT_ffff000032800e78 = 8;
    break;
  case 0xe4:
    _DAT_ffff000032800e80 = 0x10;
    break;
  case 0xe5:
    _DAT_ffff000032800e88 = 0x20;
    break;
  case 0xe6:
    _DAT_ffff000032800e90 = 0x40;
    break;
  case 0xe7:
    _DAT_ffff000032800e98 = 0x80;
    break;
  default:
    if (param_1 != 1) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_disp_dd_ffff0000b809845d,0x3f6,&DAT_ffff0000b8086efb);
    }
    _DAT_ffff000032800e68 = 2;
  }
  return 0;
}


