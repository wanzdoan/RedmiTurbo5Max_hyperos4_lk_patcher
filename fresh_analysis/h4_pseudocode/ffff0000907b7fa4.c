
void FUN_ffff0000907b7fa4(void)

{
  if (DAT_ffff00009090d8e0 == 0) {
    DAT_ffff00009090d8e0 = FUN_ffff00009077d55c(0x40,0x4000);
    if (DAT_ffff00009090d8e0 == 0) {
      FUN_ffff00009077e11c
                (s___s__malloc_memory_for_heap_fail_ffff0000907dbcbb,&DAT_ffff0000907f228a);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_mt6991_sboot_s_ffff0000907e0786,0x3d,&DAT_ffff0000907d2506);
    }
    DAT_ffff00009090d8e8 = 0;
  }
  return;
}


