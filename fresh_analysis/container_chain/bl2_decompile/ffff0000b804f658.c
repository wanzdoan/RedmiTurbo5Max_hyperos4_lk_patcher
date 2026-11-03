
void FUN_ffff0000b804f658(long param_1)

{
  long lVar1;
  
  FUN_ffff0000b804cd40(&DAT_ffff0000b8133d94,param_1 + 8,0x10);
  if (DAT_ffff0000b8133d9c - 3U < 0xfffffffe) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b744(s_unknown_def_type_ffff0000b808cdb9);
  }
  if (DAT_ffff0000b8133d94 != 0) {
    if (DAT_ffff0000b8133d98 != 0) {
      if (DAT_ffff0000b8133d98 < DAT_ffff0000b8133da0) {
        lVar1 = FUN_ffff0000b8049e60();
        DAT_ffff0000b8133c80 = lVar1 + (ulong)DAT_ffff0000b8133da0;
        FUN_ffff0000b804f4a8
                  (s_mboot_params_info_init_hook__0x__ffff0000b8089548,DAT_ffff0000b8133d94,
                   DAT_ffff0000b8133d98,DAT_ffff0000b8133d9c);
        FUN_ffff0000b804c33c
                  (s_mboot_params_info_init_hook__0x__ffff0000b8089548,DAT_ffff0000b8133d94,
                   DAT_ffff0000b8133d98,DAT_ffff0000b8133d9c,DAT_ffff0000b8133da0);
        return;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s_memory_offset_invalid_value_ffff0000b809235c);
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b744(s_sram_addr_size_zero_ffff0000b809c197);
}


