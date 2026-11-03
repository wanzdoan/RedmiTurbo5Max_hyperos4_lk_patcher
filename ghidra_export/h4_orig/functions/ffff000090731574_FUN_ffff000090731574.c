// ffff000090731574  size=292  FUN_ffff000090731574

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090731574(void)

{
  uint uVar1;
  ulong uVar2;
  int iVar3;
  
  uVar1 = _DAT_ffff00009086da48;
  if (_DAT_ffff00009086da54 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__Temp_list_is_NULL_ffff0000907d7933,
               s_ccci_mem_blk_tmp_list_dump_ffff0000907d91ec,0x25c);
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI_________AP_Addr_____ffff0000907ef9a7,
             s_ccci_mem_blk_tmp_list_dump_ffff0000907d91ec,0x261);
  if (0 < _DAT_ffff00009086da54) {
    iVar3 = 0;
    do {
      if (_DAT_ffff00009086da4c <= (int)uVar1) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__Invalid_curr_id__d_ffff0000907f2b74,
                   s_ccci_mem_blk_tmp_list_dump_ffff0000907d91ec,0x264,uVar1);
        return;
      }
      if (uVar1 == 0xffffffff) break;
      uVar2 = -(ulong)(uVar1 >> 0x1f) & 0xffffffe000000000 | (ulong)uVar1 << 5;
      FUN_ffff00009073138c(_DAT_ffff00009086da30 + uVar2);
      iVar3 = iVar3 + 1;
      uVar1 = *(uint *)(_DAT_ffff00009086da30 + uVar2 + 0x18);
    } while (iVar3 < _DAT_ffff00009086da54);
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI_________Dump_md_mem_ffff0000907e6136,
             s_ccci_mem_blk_tmp_list_dump_ffff0000907d91ec,0x26c);
  return;
}


