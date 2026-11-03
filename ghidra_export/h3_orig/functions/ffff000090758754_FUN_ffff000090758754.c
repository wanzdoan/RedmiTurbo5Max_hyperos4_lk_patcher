// ffff000090758754  size=1  FUN_ffff000090758754

undefined8 FUN_ffff000090758754(undefined4 param_1)

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
    uRamffff0000328e0010 = 0x20;
    uRamffff0000328e0040 = 1;
    return 0;
  case 0xd0:
    uVar2 = FUN_ffff000090781944();
    if (((uVar2 & 1) == 0) || (iVar1 = FUN_ffff000090783694(), iVar1 != 0xfc11)) {
      uRamffff0000328f0040 = 1;
    }
    else {
      uRamffff0000328f0040 = 0;
    }
    uRamffff0000328f0010 = 0x21;
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
    goto code_r0xffff000090758834;
  case 0xd8:
  case 0xd9:
  case 0xda:
  case 0xdb:
  case 0xdc:
  case 0xdd:
  case 0xde:
  case 0xdf:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907f4f73,0x294,&DAT_ffff0000907d24c9);
  default:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907f4f73,0x51,&DAT_ffff0000907d24c9);
  }
  *(undefined4 *)(uVar2 | 0xffff000000000000) = uVar3;
code_r0xffff000090758834:
  return 0;
}


