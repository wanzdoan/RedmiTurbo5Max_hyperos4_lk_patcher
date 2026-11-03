// ffff000090706d94  size=204  FUN_ffff000090706d94

undefined4 FUN_ffff000090706d94(undefined4 param_1)

{
  int iVar1;
  long lVar2;
  undefined4 local_24;
  
  local_24 = 0;
  lVar2 = FUN_ffff000090736af8(s_mt6379_auxadc_2_ffff0000907d8b46);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_get__s_auxadc_d_ffff0000907d3d61,
               s_mt6379_get_auxadc_ffff000090800325,0x22f,s_mt6379_bat2_ffff0000907f923f);
    iVar1 = -7;
  }
  else {
    local_24 = 0;
    iVar1 = FUN_ffff000090736908(lVar2,param_1,&local_24,0);
    if (iVar1 == 0) {
      return local_24;
    }
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_get__s_auxadc_c_ffff0000907ea5aa,
               s_mt6379_get_auxadc_ffff000090800325,0x236,s_mt6379_bat2_ffff0000907f923f,param_1);
  }
  FUN_ffff00009077e11c
            (s__s__d__Failed_to_get_mt6379_BAT2_ffff0000907ef32b,
             s_mt6379_bat2_get_auxadc_value_ffff0000907df29f,0x250,param_1,iVar1);
  return local_24;
}


