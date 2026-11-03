// ffff00009072bb14  size=176  FUN_ffff00009072bb14

void FUN_ffff00009072bb14(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff00009073220c(s_consys_size_ffff0000907f7e5e,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d____s__error___consy_ffff0000907df775,
                 s_ccci_get_consys_size_ffff0000907e5f35,0x1a4,
                 s_ccci_get_consys_size_ffff0000907e5f35);
    }
    FUN_ffff00009077de54
              (s__s__d___CCCI_Get_WIFI_proxy_smem_ffff0000907e7764,
               s_ccci_get_consys_size_ffff0000907e5f35,0x1a6,local_4);
    *(undefined4 *)(param_3 + 8) = local_4;
    return;
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf547,
             s_ccci_get_consys_size_ffff0000907e5f35,0x19e,s_ccci_get_consys_size_ffff0000907e5f35);
  return;
}


