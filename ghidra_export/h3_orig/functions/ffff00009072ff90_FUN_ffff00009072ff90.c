// ffff00009072ff90  size=264  FUN_ffff00009072ff90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009072ff90(undefined8 param_1)

{
  int iVar1;
  
  if (_DAT_ffff00009086d988 != 0) {
    iVar1 = FUN_ffff00009077f488
                      (_DAT_ffff00009086d988 + _DAT_ffff00009086d990,
                       0x4000 - (long)_DAT_ffff00009086d990,param_1,&stack0xffffffffffffff90);
    if ((-1 < iVar1) && (iVar1 < 0x4000 - _DAT_ffff00009086d990)) {
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff00009077de54
                (s__s__d___CCCI___s_ffff0000907cf66d,s_ccci_lk_trace_ffff0000907feec6,0x2d,
                 _DAT_ffff00009086d988 + _DAT_ffff00009086d990);
      _DAT_ffff00009086d990 = _DAT_ffff00009086d990 + iVar1;
      return iVar1;
    }
    FUN_ffff00009077de54
              (s__s__d___CCCI___s_ret_abnormal__d_ffff0000907da9ef,s_ccci_lk_trace_ffff0000907feec6,
               0x28,s_ccci_lk_trace_ffff0000907feec6,iVar1,0x28);
  }
  return -1;
}


