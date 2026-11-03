
void FUN_ffff00009072bc60(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int local_4;
  
  local_4 = 0;
  if (param_3 != 0) {
    iVar1 = FUN_ffff0000907324a4(s_drdi_version_ffff0000907d77ec,&local_4,4);
    if (iVar1 != 4) {
      FUN_ffff000090730228
                (s__CCCI___s__d____s__error___drdi__ffff0000907d0eab,
                 s_ccci_get_drdi_copy_size_ffff0000907ec37c,0x17e,
                 s_ccci_get_drdi_copy_size_ffff0000907ec37c);
    }
    if (local_4 == 3) {
      *(undefined4 *)(param_3 + 8) = 0x10000;
    }
    else {
      *(undefined4 *)(param_3 + 8) = 0;
    }
    return;
  }
  FUN_ffff000090730228
            (s__CCCI___s__d____s__region_NULL_ffff0000907cf566,
             s_ccci_get_drdi_copy_size_ffff0000907ec37c,0x179,
             s_ccci_get_drdi_copy_size_ffff0000907ec37c);
  return;
}


