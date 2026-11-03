
ulong FUN_ffff0000b805b7bc(long param_1)

{
  ulong uVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_hash____NULL_ffff0000b80a0ac7,s_platform_mediatek_mt6991_sboot_c_ffff0000b8087aeb,
               0x1a);
  }
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff0000b81087e0 + uVar1 * 8) == param_1) goto LAB_ffff0000b805b818;
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0;
  do {
    if (*(long *)(PTR_DAT_ffff0000b81087e0 + uVar1 * 8) == 0) {
      *(long *)(PTR_DAT_ffff0000b81087e0 + uVar1 * 8) = param_1;
      goto LAB_ffff0000b805b818;
    }
    uVar1 = uVar1 + 1;
  } while (uVar1 != 0x22);
  uVar1 = 0xffffffff;
LAB_ffff0000b805b818:
  return uVar1 & 0xffffffff;
}


