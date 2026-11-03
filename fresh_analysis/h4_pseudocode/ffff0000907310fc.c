
long FUN_ffff0000907310fc(uint param_1,uint param_2)

{
  long lVar1;
  int iVar2;
  int *piVar3;
  int iVar4;
  
  if (DAT_ffff00009086da54 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_No_need_to_generate_ffff0000907f4816,
               s_ccci_get_1st_sub_mem_blk_ffff0000907fb70a,0x20e);
  }
  else if (0 < DAT_ffff00009086da54) {
    piVar3 = &DAT_ffff00009086da48;
    iVar4 = DAT_ffff00009086da54;
    do {
      iVar2 = *piVar3;
      if (DAT_ffff00009086da4c <= iVar2) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__Invalid_curr_id__d_ffff0000907f2b74,
                   s_ccci_get_1st_sub_mem_blk_ffff0000907fb70a,0x215);
        return 0;
      }
      if (iVar2 == -1) {
        return 0;
      }
      lVar1 = DAT_ffff00009086da30 + (long)iVar2 * 0x20;
      if ((*(uint *)(lVar1 + 8) & param_1) != 0) {
        return lVar1;
      }
      if ((*(uint *)(lVar1 + 0xc) & param_2) != 0) {
        return lVar1;
      }
      iVar4 = iVar4 + -1;
      piVar3 = (int *)(DAT_ffff00009086da30 + (long)iVar2 * 0x20 + 0x18);
    } while (iVar4 != 0);
  }
  return 0;
}


