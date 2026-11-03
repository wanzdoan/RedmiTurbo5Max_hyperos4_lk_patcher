
void FUN_ffff0000be063ff4(long param_1)

{
  long lVar1;
  
  FUN_ffff0000be0607b0(&DAT_ffff0000be1322f4,param_1 + 8,0x10);
  if (DAT_ffff0000be1322fc - 3U < 0xfffffffe) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee24(s_unknown_def_type_ffff0000be0ad525);
  }
  if (DAT_ffff0000be1322f4 != 0) {
    if (DAT_ffff0000be1322f8 != 0) {
      if (DAT_ffff0000be1322f8 < DAT_ffff0000be132300) {
        lVar1 = FUN_ffff0000be05d1c0();
        DAT_ffff0000be1321e0 = lVar1 + (ulong)DAT_ffff0000be132300;
        FUN_ffff0000be063b40
                  (s_mboot_params_info_init_hook__0x__ffff0000be0a8769,DAT_ffff0000be1322f4,
                   DAT_ffff0000be1322f8,DAT_ffff0000be1322fc);
        FUN_ffff0000be05fd98
                  (s_mboot_params_info_init_hook__0x__ffff0000be0a8769,DAT_ffff0000be1322f4,
                   DAT_ffff0000be1322f8,DAT_ffff0000be1322fc,DAT_ffff0000be132300);
        return;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s_memory_offset_invalid_value_ffff0000be0b3f5f);
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee24(s_sram_addr_size_zero_ffff0000be0c05d8);
}


