
void FUN_ffff0000907312cc(void)

{
  long lVar1;
  long lVar2;
  
  if (DAT_ffff00009086da54 != 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_________AP_Addr_____ffff0000907ef9a7,
               s_ccci_mem_blk_inf_dump_ffff0000907ec4d0,0x24f);
    if (0 < DAT_ffff00009086da54) {
      lVar1 = 0;
      lVar2 = 0;
      do {
        FUN_ffff00009073138c(DAT_ffff00009086da38 + lVar1);
        lVar2 = lVar2 + 1;
        lVar1 = lVar1 + 0x18;
      } while (lVar2 < DAT_ffff00009086da54);
    }
    FUN_ffff00009077e11c
              (s__s__d___CCCI_________Dump_md_mem_ffff0000907e1232,
               s_ccci_mem_blk_inf_dump_ffff0000907ec4d0,0x253);
    return;
  }
  FUN_ffff000090730228
            (s__CCCI___s__d__Table_number_is_ze_ffff0000907eac8e,
             s_ccci_mem_blk_inf_dump_ffff0000907ec4d0,0x24b);
  return;
}


