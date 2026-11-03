
ulong FUN_ffff0000be0759ac(long param_1)

{
  ulong uVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_hash____NULL_ffff0000be0c5b47,s_platform_mediatek_mt6991_sboot_c_ffff0000be0a654a,
               0x1a);
  }
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff0000be0f5710 + uVar1 * 8) == param_1) goto LAB_ffff0000be075a08;
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff0000be0f5710 + uVar1 * 8) == 0) {
      *(long *)(PTR_DAT_ffff0000be0f5710 + uVar1 * 8) = param_1;
      goto LAB_ffff0000be075a08;
    }
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0xffffffff;
LAB_ffff0000be075a08:
  return uVar1 & 0xffffffff;
}


