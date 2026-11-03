// ffff00009072ba70  size=164  FUN_ffff00009072ba70

void FUN_ffff00009072ba70(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff00009073220c(s_udc_en_ffff0000907f98b9,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d____s__error___udc_e_ffff000090802231,
                 s_ccci_get_udc_desc_size_ffff0000907d0ea3,0x191,
                 s_ccci_get_udc_desc_size_ffff0000907d0ea3);
    }
    if (local_4 == 0) {
      *(undefined4 *)(param_3 + 8) = 0;
    }
    else {
      *(undefined4 *)(param_3 + 8) = 0x80000;
    }
    return;
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf547,
             s_ccci_get_udc_desc_size_ffff0000907d0ea3,0x18c,
             s_ccci_get_udc_desc_size_ffff0000907d0ea3);
  return;
}


