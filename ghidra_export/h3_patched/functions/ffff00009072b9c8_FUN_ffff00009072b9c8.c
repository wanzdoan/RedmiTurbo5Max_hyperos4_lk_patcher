// ffff00009072b9c8  size=168  FUN_ffff00009072b9c8

void FUN_ffff00009072b9c8(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff00009073220c(s_drdi_version_ffff0000907d7756,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d____s__error___drdi__ffff0000907d0e6e,
                 s_ccci_get_drdi_copy_size_ffff0000907ec1e2,0x17e,
                 s_ccci_get_drdi_copy_size_ffff0000907ec1e2);
    }
    if (local_4 == 3) {
      *(undefined4 *)(param_3 + 8) = 0x10000;
    }
    else {
      *(undefined4 *)(param_3 + 8) = 0;
    }
    return;
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf547,
             s_ccci_get_drdi_copy_size_ffff0000907ec1e2,0x179,
             s_ccci_get_drdi_copy_size_ffff0000907ec1e2);
  return;
}


