// ffff00009072bdac  size=176  FUN_ffff00009072bdac

void FUN_ffff00009072bdac(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff0000907324a4(s_consys_size_ffff0000907f7ffa,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff000090730228
                (s__CCCI___s__d____s__error___consy_ffff0000907df82f,
                 s_ccci_get_consys_size_ffff0000907e6060,0x1a4,
                 s_ccci_get_consys_size_ffff0000907e6060);
    }
    FUN_ffff00009077e11c
              (s__s__d___CCCI_Get_WIFI_proxy_smem_ffff0000907e78bf,
               s_ccci_get_consys_size_ffff0000907e6060,0x1a6,local_4);
    *(undefined4 *)(param_3 + 8) = local_4;
    return;
  }
  FUN_ffff000090730228
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf566,
             s_ccci_get_consys_size_ffff0000907e6060,0x19e,s_ccci_get_consys_size_ffff0000907e6060);
  return;
}


