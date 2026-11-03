
void FUN_ffff0000be091dc8(void)

{
  if (DAT_ffff0000be1b7da0 == 0) {
    DAT_ffff0000be1b7da0 = FUN_ffff0000be05f204(0x40,0x4000);
    if (DAT_ffff0000be1b7da0 == 0) {
      FUN_ffff0000be05fd98
                (s___s__malloc_memory_for_heap_fail_ffff0000be0ac10d,&DAT_ffff0000be0bb811);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_sboot_s_ffff0000be0af295,0x3d,&DAT_ffff0000be0a553d);
    }
    DAT_ffff0000be1b7da8 = 0;
  }
  return;
}


