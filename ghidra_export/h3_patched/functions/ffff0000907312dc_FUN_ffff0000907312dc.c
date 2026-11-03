// ffff0000907312dc  size=292  FUN_ffff0000907312dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907312dc(void)

{
  uint uVar1;
  ulong uVar2;
  int iVar3;
  
  uVar1 = _DAT_ffff00009086d9b0;
  if (_DAT_ffff00009086d9bc == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Temp_list_is_NULL_ffff0000907d789d,
               s_ccci_mem_blk_tmp_list_dump_ffff0000907d9155,0x25c);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI_________AP_Addr_____ffff0000907ef81f,
             s_ccci_mem_blk_tmp_list_dump_ffff0000907d9155,0x261);
  if (0 < _DAT_ffff00009086d9bc) {
    iVar3 = 0;
    do {
      if (_DAT_ffff00009086d9b4 <= (int)uVar1) {
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__Invalid_curr_id__d_ffff0000907f29d1,
                   s_ccci_mem_blk_tmp_list_dump_ffff0000907d9155,0x264,uVar1);
        return;
      }
      if (uVar1 == 0xffffffff) break;
      uVar2 = -(ulong)(uVar1 >> 0x1f) & 0xffffffe000000000 | (ulong)uVar1 << 5;
      FUN_ffff0000907310f4(_DAT_ffff00009086d998 + uVar2);
      iVar3 = iVar3 + 1;
      uVar1 = *(uint *)(_DAT_ffff00009086d998 + uVar2 + 0x18);
    } while (iVar3 < _DAT_ffff00009086d9bc);
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI_________Dump_md_mem_ffff0000907e600b,
             s_ccci_mem_blk_tmp_list_dump_ffff0000907d9155,0x26c);
  return;
}


