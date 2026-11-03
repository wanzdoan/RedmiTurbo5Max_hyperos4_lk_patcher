// ffff000090730e64  size=196  FUN_ffff000090730e64

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff000090730e64(uint param_1,uint param_2)

{
  long lVar1;
  int iVar2;
  int *piVar3;
  int iVar4;
  
  if (_DAT_ffff00009086d9bc == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_No_need_to_generate_ffff0000907f4692,
               s_ccci_get_1st_sub_mem_blk_ffff0000907fb56e,0x20e);
  }
  else if (0 < _DAT_ffff00009086d9bc) {
    piVar3 = (int *)&DAT_ffff00009086d9b0;
    iVar4 = _DAT_ffff00009086d9bc;
    do {
      iVar2 = *piVar3;
      if (_DAT_ffff00009086d9b4 <= iVar2) {
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__Invalid_curr_id__d_ffff0000907f29d1,
                   s_ccci_get_1st_sub_mem_blk_ffff0000907fb56e,0x215);
        return 0;
      }
      if (iVar2 == -1) {
        return 0;
      }
      lVar1 = _DAT_ffff00009086d998 + (long)iVar2 * 0x20;
      if ((*(uint *)(lVar1 + 8) & param_1) != 0) {
        return lVar1;
      }
      if ((*(uint *)(lVar1 + 0xc) & param_2) != 0) {
        return lVar1;
      }
      iVar4 = iVar4 + -1;
      piVar3 = (int *)(_DAT_ffff00009086d998 + (long)iVar2 * 0x20 + 0x18);
    } while (iVar4 != 0);
  }
  return 0;
}


