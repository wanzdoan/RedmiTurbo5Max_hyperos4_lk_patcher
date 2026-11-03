// ffff0000907586ec  size=60  FUN_ffff0000907586ec

undefined8 FUN_ffff0000907586ec(undefined4 param_1)

{
  undefined8 uVar1;
  
  uVar1 = 4;
  switch(param_1) {
  case 0:
  case 5:
  case 0x51:
    goto code_r0xffff000090758724;
  case 1:
  case 6:
  case 0x37:
  case 0x38:
  case 0x52:
  case 0x5e:
  case 0x5f:
  case 0x60:
  case 0xe2:
  case 0xe3:
  case 0xe4:
  case 0xe5:
  case 0xe6:
  case 0xe7:
  case 0xe8:
  case 0xe9:
  case 0xea:
  case 0xeb:
  case 0xec:
  case 0xed:
  case 0xee:
    uVar1 = 1;
code_r0xffff000090758724:
    return uVar1;
  default:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907f18cd,0x66b,&DAT_ffff0000907d24c9);
  }
}


