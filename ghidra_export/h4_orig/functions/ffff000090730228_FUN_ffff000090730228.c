// ffff000090730228  size=264  FUN_ffff000090730228

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090730228(undefined8 param_1)

{
  int iVar1;
  
  if (_DAT_ffff00009086da20 != 0) {
    iVar1 = FUN_ffff00009077f750
                      (_DAT_ffff00009086da20 + _DAT_ffff00009086da28,
                       0x4000 - (long)_DAT_ffff00009086da28,param_1,&stack0xffffffffffffff90);
    if ((-1 < iVar1) && (iVar1 < 0x4000 - _DAT_ffff00009086da28)) {
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff00009077e11c
                (s__s__d___CCCI___s_ffff0000907cf68c,s_ccci_lk_trace_ffff0000907ff06e,0x2d,
                 _DAT_ffff00009086da20 + _DAT_ffff00009086da28);
      _DAT_ffff00009086da28 = _DAT_ffff00009086da28 + iVar1;
      return iVar1;
    }
    FUN_ffff00009077e11c
              (s__s__d___CCCI___s_ret_abnormal__d_ffff0000907daafa,s_ccci_lk_trace_ffff0000907ff06e,
               0x28,s_ccci_lk_trace_ffff0000907ff06e,iVar1,0x28);
  }
  return -1;
}


