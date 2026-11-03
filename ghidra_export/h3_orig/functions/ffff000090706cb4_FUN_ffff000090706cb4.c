// ffff000090706cb4  size=204  FUN_ffff000090706cb4

undefined4 FUN_ffff000090706cb4(undefined4 param_1)

{
  int iVar1;
  long lVar2;
  undefined4 local_24;
  
  local_24 = 0;
  lVar2 = FUN_ffff000090736860(s_mt6379_auxadc_1_ffff0000907f5a45);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_get__s_auxadc_d_ffff0000907d3d24,
               s_mt6379_get_auxadc_ffff00009080017d,0x22f,s_mt6379_bat1_ffff0000907fae01);
    iVar1 = -7;
  }
  else {
    local_24 = 0;
    iVar1 = FUN_ffff000090736670(lVar2,param_1,&local_24,0);
    if (iVar1 == 0) {
      return local_24;
    }
    FUN_ffff00009077de54
              (s__s__d__Failed_to_get__s_auxadc_c_ffff0000907ea466,
               s_mt6379_get_auxadc_ffff00009080017d,0x236,s_mt6379_bat1_ffff0000907fae01,param_1);
  }
  FUN_ffff00009077de54
            (s__s__d__Failed_to_get_mt6379_BAT1_ffff0000907e8ae0,
             s_mt6379_bat1_get_auxadc_value_ffff0000907f0b17,0x244,param_1,iVar1);
  return local_24;
}


