
void FUN_ffff000090781edc(long param_1)

{
  long lVar1;
  
  FUN_ffff00009077eb20(&DAT_ffff000090887224,param_1 + 8,0x10);
  if (DAT_ffff00009088722c - 3U < 0xfffffffe) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_unknown_def_type_ffff0000907ddb51);
  }
  if ((int)DAT_ffff000090887224 != 0) {
    if (DAT_ffff000090887224._4_4_ != 0) {
      if (DAT_ffff000090887224._4_4_ < DAT_ffff000090887230) {
        lVar1 = FUN_ffff00009077b2cc();
        DAT_ffff000090887110 = lVar1 + (ulong)DAT_ffff000090887230;
        FUN_ffff000090781ae8
                  (s_mboot_params_info_init_hook__0x__ffff0000907d6c20,(int)DAT_ffff000090887224,
                   DAT_ffff000090887224._4_4_,DAT_ffff00009088722c);
        FUN_ffff00009077e11c
                  (s_mboot_params_info_init_hook__0x__ffff0000907d6c20,(int)DAT_ffff000090887224,
                   DAT_ffff000090887224._4_4_,DAT_ffff00009088722c,DAT_ffff000090887230);
        return;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_memory_offset_invalid_value_ffff0000907e7666);
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s_sram_addr_size_zero_ffff0000907f95d6);
}


