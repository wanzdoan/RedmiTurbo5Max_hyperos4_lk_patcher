// ffff00009072be5c  size=180  FUN_ffff00009072be5c

void FUN_ffff00009072be5c(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff0000907324a4(s_nv_cache_shm_size_ffff0000907f11a1,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff000090730228
                (s__CCCI___s__d____s__error___nv_ca_ffff0000907e78eb,
                 s_ccci_get_nv_cache_size_ffff0000907fd1b9,0x1b4,
                 s_ccci_get_nv_cache_size_ffff0000907fd1b9);
    }
    FUN_ffff00009077e11c
              (s__s__d___CCCI_Get_nv_cache_smem_s_ffff0000907d77f9,
               s_ccci_get_nv_cache_size_ffff0000907fd1b9,0x1b6,local_4);
    if (local_4 != 0) {
      *(int *)(param_3 + 8) = local_4;
    }
    return;
  }
  FUN_ffff000090730228
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf566,
             s_ccci_get_nv_cache_size_ffff0000907fd1b9,0x1af,
             s_ccci_get_nv_cache_size_ffff0000907fd1b9);
  return;
}


