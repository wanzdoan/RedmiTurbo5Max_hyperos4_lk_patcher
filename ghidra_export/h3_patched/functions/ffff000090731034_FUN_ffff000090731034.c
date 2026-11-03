// ffff000090731034  size=192  FUN_ffff000090731034

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090731034(void)

{
  long lVar1;
  long lVar2;
  
  if (_DAT_ffff00009086d9bc != 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_________AP_Addr_____ffff0000907ef81f,
               s_ccci_mem_blk_inf_dump_ffff0000907ec336,0x24f);
    if (0 < _DAT_ffff00009086d9bc) {
      lVar1 = 0;
      lVar2 = 0;
      do {
        FUN_ffff0000907310f4(_DAT_ffff00009086d9a0 + lVar1);
        lVar2 = lVar2 + 1;
        lVar1 = lVar1 + 0x18;
      } while (lVar2 < _DAT_ffff00009086d9bc);
    }
    FUN_ffff00009077de54
              (s__s__d___CCCI_________Dump_md_mem_ffff0000907e114a,
               s_ccci_mem_blk_inf_dump_ffff0000907ec336,0x253);
    return;
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d__Table_number_is_ze_ffff0000907eaaf4,
             s_ccci_mem_blk_inf_dump_ffff0000907ec336,0x24b);
  return;
}


