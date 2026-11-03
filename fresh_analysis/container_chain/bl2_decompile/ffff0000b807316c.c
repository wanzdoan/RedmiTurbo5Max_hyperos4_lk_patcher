
void FUN_ffff0000b807316c(void)

{
  if (DAT_ffff0000b81b9ac8 == 0) {
    DAT_ffff0000b81b9ac8 = FUN_ffff0000b804bd14(0x40,0x4000);
    if (DAT_ffff0000b81b9ac8 == 0) {
      FUN_ffff0000b804c33c
                (s___s__malloc_memory_for_heap_fail_ffff0000b808c0df,&DAT_ffff0000b80987e6);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_sboot_s_ffff0000b808e778,0x3d,&DAT_ffff0000b8086efb);
    }
    DAT_ffff0000b81b9ad0 = 0;
  }
  return;
}


