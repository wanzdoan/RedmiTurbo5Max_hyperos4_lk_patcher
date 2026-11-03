
ulong FUN_ffff0000907992e4(long param_1)

{
  ulong uVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_hash____NULL_ffff0000908019f9,s_platform_mediatek_mt6991_sboot_c_ffff0000907d3aaf,
               0x1a);
  }
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff000090839a90 + uVar1 * 8) == param_1) goto LAB_ffff000090799340;
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff000090839a90 + uVar1 * 8) == 0) {
      *(long *)(PTR_DAT_ffff000090839a90 + uVar1 * 8) = param_1;
      goto LAB_ffff000090799340;
    }
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0xffffffff;
LAB_ffff000090799340:
  return uVar1 & 0xffffffff;
}


