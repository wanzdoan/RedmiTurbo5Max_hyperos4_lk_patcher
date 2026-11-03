// ffff000090799024  size=132  FUN_ffff000090799024

ulong FUN_ffff000090799024(long param_1)

{
  ulong uVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_hash____NULL_ffff00009080181d,s_platform_mediatek_mt6991_sboot_c_ffff0000907d3a72,
               0x1a);
  }
  uVar1 = 0;
  do {
    if (*(long *)(DAT_ffff000090839a70 + uVar1 * 8) == param_1) goto LAB_ffff000090799080;
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0;
  do {
    if (*(long *)(DAT_ffff000090839a70 + uVar1 * 8) == 0) {
      *(long *)(DAT_ffff000090839a70 + uVar1 * 8) = param_1;
      goto LAB_ffff000090799080;
    }
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0xffffffff;
LAB_ffff000090799080:
  return uVar1 & 0xffffffff;
}


