
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b803a604(undefined4 param_1)

{
  int iVar1;
  ulong uVar2;
  undefined4 uVar3;
  
  switch(param_1) {
  case 0xce:
    uVar3 = 0x20;
    uVar2 = 0x328d0010;
    break;
  case 0xcf:
    _DAT_ffff0000328e0010 = 0x20;
    _DAT_ffff0000328e0040 = 1;
    return 0;
  case 0xd0:
    uVar2 = FUN_ffff0000b804f554();
    if (((uVar2 & 1) == 0) || (iVar1 = FUN_ffff0000b8050384(), iVar1 != 0xfc11)) {
      _DAT_ffff0000328f0040 = 1;
    }
    else {
      _DAT_ffff0000328f0040 = 0;
    }
    _DAT_ffff0000328f0010 = 0x21;
    return 0;
  case 0xd1:
    uVar3 = 0x21;
    uVar2 = 0x32900010;
    break;
  case 0xd2:
    uVar3 = 0x21;
    uVar2 = 0x32910010;
    break;
  case 0xd3:
    uVar3 = 0x21;
    uVar2 = 0x32920010;
    break;
  case 0xd4:
    uVar3 = 0x11;
    uVar2 = 0x32930010;
    break;
  case 0xd5:
  case 0xd6:
  case 0xd7:
    goto code_r0xffff0000b803a6e4;
  case 0xd8:
  case 0xd9:
  case 0xda:
  case 0xdb:
  case 0xdc:
  case 0xdd:
  case 0xde:
  case 0xdf:
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809a10d,0x294,&DAT_ffff0000b8086efb);
  default:
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809a10d,0x51,&DAT_ffff0000b8086efb);
  }
  *(undefined4 *)(uVar2 | 0xffff000000000000) = uVar3;
code_r0xffff0000b803a6e4:
  return 0;
}


