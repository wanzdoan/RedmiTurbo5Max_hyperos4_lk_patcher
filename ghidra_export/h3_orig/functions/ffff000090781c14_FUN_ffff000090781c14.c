// ffff000090781c14  size=192  FUN_ffff000090781c14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090781c14(long param_1)

{
  FUN_ffff00009077e858(&DAT_ffff00009088718c,param_1 + 8,0x10);
  if (_DAT_ffff000090887194 - 3U < 0xfffffffe) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_unknown_def_type_ffff0000907ddaae);
  }
  if (_DAT_ffff00009088718c != 0) {
    if (_DAT_ffff000090887190 != 0) {
      if (_DAT_ffff000090887190 < _DAT_ffff000090887198) {
        _DAT_ffff000090887078 = FUN_ffff00009077b004();
        _DAT_ffff000090887078 = _DAT_ffff000090887078 + (ulong)_DAT_ffff000090887198;
        FUN_ffff000090781820
                  (s_mboot_params_info_init_hook__0x__ffff0000907d6b8a,_DAT_ffff00009088718c,
                   _DAT_ffff000090887190,_DAT_ffff000090887194);
        FUN_ffff00009077de54
                  (s_mboot_params_info_init_hook__0x__ffff0000907d6b8a,_DAT_ffff00009088718c,
                   _DAT_ffff000090887190,_DAT_ffff000090887194,_DAT_ffff000090887198);
        return;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_memory_offset_invalid_value_ffff0000907e7522);
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s_sram_addr_size_zero_ffff0000907f943a);
}


